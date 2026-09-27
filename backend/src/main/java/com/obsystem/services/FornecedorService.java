package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.FornecedorDTO;
import com.obsystem.entities.Contato;
import com.obsystem.entities.Endereco;
import com.obsystem.entities.Fisica;
import com.obsystem.entities.Fornecedor;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.FornecedorStatus;
import com.obsystem.entities.enums.TipoContato;
import com.obsystem.entities.enums.TipoPagamento;
import com.obsystem.repositories.ContatoRepository;
import com.obsystem.repositories.EnderecoRepository;
import com.obsystem.repositories.FisicaRepository;
import com.obsystem.repositories.FornecedorRepository;
import com.obsystem.repositories.JuridicaRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private FisicaRepository fisicaRepository;

    @Autowired
    private JuridicaRepository juridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Transactional(readOnly = true)
    public List<FornecedorDTO> findAll(String search, FornecedorStatus status) {
        List<Fornecedor> list;

        if (search != null && !search.trim().isEmpty()) {
            list = fornecedorRepository.searchByNome(search.trim());
        } else if (status != null) {
            list = fornecedorRepository.findByStatus(status);
        } else {
            list = fornecedorRepository.findAll();
        }

        return list.stream().map(FornecedorDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FornecedorDTO findById(Integer id) {
        Fornecedor entity = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + id));
        return new FornecedorDTO(entity);
    }

    @Transactional
    public FornecedorDTO create(FornecedorDTO dto) {
        validarRegrasBasicas(dto);

        Pessoa pessoaSalva;
        char tipo = dto.getTipoPessoa() != null ? Character.toUpperCase(dto.getTipoPessoa()) : 'J';

        if (tipo == 'F') {
            pessoaSalva = criarPessoaFisica(dto);
        } else {
            pessoaSalva = criarPessoaJuridica(dto);
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setPessoa(pessoaSalva);
        fornecedor.setStatus(dto.getStatus() != null ? dto.getStatus() : FornecedorStatus.ATIVO);
        fornecedor.setCredito(dto.getCredito() != null ? dto.getCredito() : BigDecimal.ZERO);
        fornecedor.setPagamento(dto.getPagamento() != null ? dto.getPagamento() : TipoPagamento.PIX);

        fornecedor = fornecedorRepository.save(fornecedor);

        // Endereço
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

        // Contato
        if (dto.getTelefone() != null && !dto.getTelefone().trim().isEmpty()) {
            Contato contato = new Contato();
            contato.setPessoa(pessoaSalva);
            contato.setTipo(dto.getTipoContato() != null ? dto.getTipoContato() : TipoContato.CELULAR);
            contato.setNumero(dto.getTelefone().trim());
            contatoRepository.save(contato);
        }

        return new FornecedorDTO(fornecedor);
    }

    @Transactional
    public FornecedorDTO update(Integer id, FornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + id));

        validarRegrasBasicas(dto);
        Pessoa p = fornecedor.getPessoa();

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
            fornecedor.setStatus(dto.getStatus());
        }
        if (dto.getCredito() != null) {
            fornecedor.setCredito(dto.getCredito());
        }
        if (dto.getPagamento() != null) {
            fornecedor.setPagamento(dto.getPagamento());
        }

        fornecedor = fornecedorRepository.save(fornecedor);
        return new FornecedorDTO(fornecedor);
    }

    @Transactional
    public void inativar(Integer id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + id));
        fornecedor.setStatus(FornecedorStatus.INATIVO);
        fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public void ativar(Integer id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + id));
        fornecedor.setStatus(FornecedorStatus.ATIVO);
        fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public void delete(Integer id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + id);
        }
        try {
            fornecedorRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir o fornecedor pois ele possui histórico de compras ou títulos financeiros. Considere inativá-lo.");
        }
    }

    private void validarRegrasBasicas(FornecedorDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new BusinessException("A razão social ou nome é obrigatório.");
        }
    }

    private Fisica criarPessoaFisica(FornecedorDTO dto) {
        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            throw new BusinessException("O CPF é obrigatório para Pessoa Física.");
        }
        String cpfLimpo = dto.getCpf().trim();
        if (fisicaRepository.existsByCpf(cpfLimpo)) {
            throw new BusinessException("Já existe um cadastro com o CPF: " + cpfLimpo);
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

    private Juridica criarPessoaJuridica(FornecedorDTO dto) {
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new BusinessException("O CNPJ é obrigatório para Pessoa Jurídica.");
        }
        String cnpjLimpo = dto.getCnpj().trim();
        if (juridicaRepository.existsByCnpj(cnpjLimpo)) {
            throw new BusinessException("Já existe um cadastro com o CNPJ: " + cnpjLimpo);
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
