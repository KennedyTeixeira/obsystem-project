package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.ClienteDTO;
import com.obsystem.entities.Cliente;
import com.obsystem.entities.Contato;
import com.obsystem.entities.Endereco;
import com.obsystem.entities.Fisica;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.ClienteStatus;
import com.obsystem.entities.enums.TipoContato;
import com.obsystem.repositories.ClienteRepository;
import com.obsystem.repositories.ContatoRepository;
import com.obsystem.repositories.EnderecoRepository;
import com.obsystem.repositories.FisicaRepository;
import com.obsystem.repositories.JuridicaRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FisicaRepository fisicaRepository;

    @Autowired
    private JuridicaRepository juridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll(String search, ClienteStatus status) {
        List<Cliente> list;

        if (search != null && !search.trim().isEmpty()) {
            list = clienteRepository.searchByNome(search.trim());
        } else if (status != null) {
            list = clienteRepository.findByStatus(status);
        } else {
            list = clienteRepository.findAll();
        }

        return list.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO findById(Integer id) {
        Cliente entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));
        return new ClienteDTO(entity);
    }

    @Transactional
    public ClienteDTO create(ClienteDTO dto) {
        validarRegrasBasicas(dto);

        Pessoa pessoaSalva;
        char tipo = dto.getTipoPessoa() != null ? Character.toUpperCase(dto.getTipoPessoa()) : 'F';

        if (tipo == 'F') {
            pessoaSalva = criarPessoaFisica(dto);
        } else {
            pessoaSalva = criarPessoaJuridica(dto);
        }

        Cliente cliente = new Cliente();
        cliente.setPessoa(pessoaSalva);
        cliente.setStatus(dto.getStatus() != null ? dto.getStatus() : ClienteStatus.ATIVO);
        cliente.setSaldo(dto.getSaldo() != null ? dto.getSaldo() : BigDecimal.ZERO);
        cliente.setLimite(dto.getLimite() != null ? dto.getLimite() : BigDecimal.ZERO);

        cliente = clienteRepository.save(cliente);

        // Se informou endereço, persiste
        if (dto.getEndereco() != null && !dto.getEndereco().trim().isEmpty()) {
            Endereco end = new Endereco();
            end.setPessoa(pessoaSalva);
            end.setEndereco(dto.getEndereco().trim());
            end.setNumero(dto.getNumero() != null ? dto.getNumero().trim() : "S/N");
            end.setComplemento(dto.getComplemento());
            end.setBairro(dto.getBairro() != null ? dto.getBairro().trim() : "Centro");
            end.setCidade(dto.getCidade() != null ? dto.getCidade().trim() : "Cidade");
            end.setUf(dto.getUf() != null ? dto.getUf().trim().toUpperCase() : "SP");
            end.setCep(dto.getCep() != null ? dto.getCep().trim() : "00000-000");
            enderecoRepository.save(end);
        }

        // Se informou telefone, persiste
        if (dto.getTelefone() != null && !dto.getTelefone().trim().isEmpty()) {
            Contato contato = new Contato();
            contato.setPessoa(pessoaSalva);
            contato.setTipo(dto.getTipoContato() != null ? dto.getTipoContato() : TipoContato.CELULAR);
            contato.setNumero(dto.getTelefone().trim());
            contatoRepository.save(contato);
        }

        return new ClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO update(Integer id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));

        validarRegrasBasicas(dto);
        Pessoa p = cliente.getPessoa();

        p.setNome(dto.getNome().trim());
        p.setEmail(dto.getEmail());
        p.setSegmento(dto.getSegmento());
        p.setSite(dto.getSite());
        p.setObservacao(dto.getObservacao());

        if (p instanceof Fisica) {
            Fisica f = (Fisica) p;
            if (dto.getCpf() != null && !dto.getCpf().trim().isEmpty()) {
                String cpfLimpo = dto.getCpf().trim();
                if (fisicaRepository.existsByCpfAndIdNot(cpfLimpo, f.getId())) {
                    throw new BusinessException("Já existe outra pessoa cadastrada com o CPF: " + cpfLimpo);
                }
                f.setCpf(cpfLimpo);
            }
            f.setRg(dto.getRg());
            f.setGenero(dto.getGenero());
            fisicaRepository.save(f);
        } else if (p instanceof Juridica) {
            Juridica j = (Juridica) p;
            if (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty()) {
                String cnpjLimpo = dto.getCnpj().trim();
                if (juridicaRepository.existsByCnpjAndIdNot(cnpjLimpo, j.getId())) {
                    throw new BusinessException("Já existe outra empresa cadastrada com o CNPJ: " + cnpjLimpo);
                }
                j.setCnpj(cnpjLimpo);
            }
            j.setFantasia(dto.getFantasia());
            j.setIe(dto.getIe());
            j.setIm(dto.getIm());
            juridicaRepository.save(j);
        }

        if (dto.getStatus() != null) {
            cliente.setStatus(dto.getStatus());
        }
        if (dto.getLimite() != null) {
            cliente.setLimite(dto.getLimite());
        }
        if (dto.getSaldo() != null) {
            cliente.setSaldo(dto.getSaldo());
        }

        cliente = clienteRepository.save(cliente);
        return new ClienteDTO(cliente);
    }

    @Transactional
    public void inativar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));
        cliente.setStatus(ClienteStatus.INATIVO);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void ativar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));
        cliente.setStatus(ClienteStatus.ATIVO);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para o ID: " + id);
        }
        try {
            clienteRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir o cliente pois ele possui histórico de vendas ou títulos financeiros. Considere inativá-lo.");
        }
    }

    private void validarRegrasBasicas(ClienteDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new BusinessException("O nome ou razão social é obrigatório.");
        }
    }

    private Fisica criarPessoaFisica(ClienteDTO dto) {
        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            throw new BusinessException("O CPF é obrigatório para Pessoa Física.");
        }
        String cpfLimpo = dto.getCpf().trim();
        if (fisicaRepository.existsByCpf(cpfLimpo)) {
            throw new BusinessException("Já existe um cliente cadastrado com o CPF: " + cpfLimpo);
        }

        Fisica f = new Fisica();
        f.setNome(dto.getNome().trim());
        f.setTipo('F');
        f.setCpf(cpfLimpo);
        f.setRg(dto.getRg());
        f.setGenero(dto.getGenero());
        f.setEmail(dto.getEmail());
        f.setSegmento(dto.getSegmento());
        f.setSite(dto.getSite());
        f.setObservacao(dto.getObservacao());
        f.setCadastro(LocalDate.now());

        return fisicaRepository.save(f);
    }

    private Juridica criarPessoaJuridica(ClienteDTO dto) {
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new BusinessException("O CNPJ é obrigatório para Pessoa Jurídica.");
        }
        String cnpjLimpo = dto.getCnpj().trim();
        if (juridicaRepository.existsByCnpj(cnpjLimpo)) {
            throw new BusinessException("Já existe um cliente cadastrado com o CNPJ: " + cnpjLimpo);
        }

        Juridica j = new Juridica();
        j.setNome(dto.getNome().trim());
        j.setTipo('J');
        j.setCnpj(cnpjLimpo);
        j.setFantasia(dto.getFantasia() != null ? dto.getFantasia().trim() : dto.getNome().trim());
        j.setIe(dto.getIe());
        j.setIm(dto.getIm());
        j.setEmail(dto.getEmail());
        j.setSegmento(dto.getSegmento());
        j.setSite(dto.getSite());
        j.setObservacao(dto.getObservacao());
        j.setCadastro(LocalDate.now());

        return juridicaRepository.save(j);
    }
}
