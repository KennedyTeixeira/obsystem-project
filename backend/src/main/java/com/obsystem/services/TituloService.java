package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.ResumoFinanceiroDTO;
import com.obsystem.dto.TituloDTO;
import com.obsystem.entities.CentroCusto;
import com.obsystem.entities.Classificacao;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.PlanoConta;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.repositories.CentroCustoRepository;
import com.obsystem.repositories.ClassificacaoRepository;
import com.obsystem.repositories.PessoaRepository;
import com.obsystem.repositories.PlanoContaRepository;
import com.obsystem.repositories.TituloRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class TituloService {

    @Autowired
    private TituloRepository tituloRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private PlanoContaRepository planoContaRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Transactional(readOnly = true)
    public List<TituloDTO> findAll(String search, TipoTitulo tipo, TituloStatus status, LocalDate dataInicio, LocalDate dataFim) {
        List<Titulo> list;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasFilters = tipo != null || status != null || dataInicio != null || dataFim != null;

        if (hasSearch) {
            String s = "%" + search.trim().toLowerCase() + "%";
            list = tituloRepository.searchTitulosWithText(s, tipo, status, dataInicio, dataFim);
        } else if (hasFilters) {
            list = tituloRepository.searchTitulosWithoutText(tipo, status, dataInicio, dataFim);
        } else {
            list = tituloRepository.findAll(Sort.by(Sort.Direction.ASC, "vencimento"));
        }

        return list.stream().map(TituloDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TituloDTO findById(Integer id) {
        Titulo entity = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado para o ID: " + id));
        return new TituloDTO(entity);
    }

    @Transactional(readOnly = true)
    public ResumoFinanceiroDTO obterResumo() {
        BigDecimal totalReceber = tituloRepository.sumByTipoAndStatus(TipoTitulo.RECEITA, TituloStatus.PENDENTE);
        BigDecimal totalPagar = tituloRepository.sumByTipoAndStatus(TipoTitulo.DESPESA, TituloStatus.PENDENTE);
        BigDecimal totalVencido = tituloRepository.sumVencidos(LocalDate.now());

        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal totalRecebidoMes = tituloRepository.sumPagoByTipoAndPeriodo(TipoTitulo.RECEITA, TituloStatus.PAGO, inicioMes, fimMes);
        BigDecimal totalPagoMes = tituloRepository.sumPagoByTipoAndPeriodo(TipoTitulo.DESPESA, TituloStatus.PAGO, inicioMes, fimMes);

        return new ResumoFinanceiroDTO(totalReceber, totalPagar, totalRecebidoMes, totalPagoMes, totalVencido);
    }

    @Transactional
    public TituloDTO create(TituloDTO dto) {
        if (dto.getIdPessoa() == null) {
            throw new BusinessException("A pessoa (cliente ou fornecedor) é obrigatória para o título.");
        }
        if (dto.getTipo() == null) {
            throw new BusinessException("O tipo de título (Receita ou Despesa) é obrigatório.");
        }
        if (dto.getDescricao() == null || dto.getDescricao().trim().isEmpty()) {
            throw new BusinessException("A descrição do título é obrigatória.");
        }
        if (dto.getValorTotal() == null || dto.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor total do título deve ser maior que zero.");
        }
        if (dto.getVencimento() == null) {
            throw new BusinessException("A data de vencimento é obrigatória.");
        }

        Pessoa pessoa = pessoaRepository.findById(dto.getIdPessoa())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada para o ID: " + dto.getIdPessoa()));

        Titulo entity = new Titulo();
        entity.setPessoa(pessoa);
        entity.setTipo(dto.getTipo());
        entity.setDescricao(dto.getDescricao().trim());
        entity.setEmissao(dto.getEmissao() != null ? dto.getEmissao() : LocalDate.now());
        entity.setVencimento(dto.getVencimento());
        entity.setValorTotal(dto.getValorTotal());
        entity.setStatus(TituloStatus.PENDENTE);

        if (dto.getIdCentroCusto() != null) {
            CentroCusto cc = centroCustoRepository.findById(dto.getIdCentroCusto()).orElse(null);
            entity.setCentroCusto(cc);
        }

        if (dto.getIdPlanoConta() != null) {
            PlanoConta pc = planoContaRepository.findById(dto.getIdPlanoConta()).orElse(null);
            entity.setPlanoConta(pc);
        }

        if (dto.getIdClassificacao() != null) {
            Classificacao cl = classificacaoRepository.findById(dto.getIdClassificacao()).orElse(null);
            entity.setClassificacao(cl);
        }

        entity = tituloRepository.save(entity);
        return new TituloDTO(entity);
    }

    @Transactional
    public TituloDTO update(Integer id, TituloDTO dto) {
        Titulo entity = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado para o ID: " + id));

        if (entity.getStatus() == TituloStatus.PAGO) {
            throw new BusinessException("Não é possível alterar um título já liquidado. Realize o estorno primeiro.");
        }

        if (dto.getDescricao() != null && !dto.getDescricao().trim().isEmpty()) {
            entity.setDescricao(dto.getDescricao().trim());
        }
        if (dto.getVencimento() != null) {
            entity.setVencimento(dto.getVencimento());
        }
        if (dto.getValorTotal() != null && dto.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
            entity.setValorTotal(dto.getValorTotal());
        }

        if (dto.getIdCentroCusto() != null) {
            CentroCusto cc = centroCustoRepository.findById(dto.getIdCentroCusto()).orElse(null);
            entity.setCentroCusto(cc);
        }

        if (dto.getIdPlanoConta() != null) {
            PlanoConta pc = planoContaRepository.findById(dto.getIdPlanoConta()).orElse(null);
            entity.setPlanoConta(pc);
        }

        if (dto.getIdClassificacao() != null) {
            Classificacao cl = classificacaoRepository.findById(dto.getIdClassificacao()).orElse(null);
            entity.setClassificacao(cl);
        }

        entity = tituloRepository.save(entity);
        return new TituloDTO(entity);
    }

    @Transactional
    public TituloDTO liquidar(Integer id, LocalDate dataPagamento) {
        Titulo entity = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado para o ID: " + id));

        if (entity.getStatus() == TituloStatus.CANCELADO) {
            throw new BusinessException("Não é possível liquidar um título cancelado.");
        }

        entity.setStatus(TituloStatus.PAGO);
        entity.setPagamento(dataPagamento != null ? dataPagamento : LocalDate.now());

        entity = tituloRepository.save(entity);
        return new TituloDTO(entity);
    }

    @Transactional
    public TituloDTO cancelar(Integer id) {
        Titulo entity = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado para o ID: " + id));

        if (entity.getStatus() == TituloStatus.PAGO) {
            throw new BusinessException("Não é possível cancelar um título já pago. Faça o estorno antes.");
        }

        entity.setStatus(TituloStatus.CANCELADO);
        entity = tituloRepository.save(entity);
        return new TituloDTO(entity);
    }

    @Transactional
    public TituloDTO estornar(Integer id) {
        Titulo entity = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Título não encontrado para o ID: " + id));

        entity.setStatus(TituloStatus.PENDENTE);
        entity.setPagamento(null);

        entity = tituloRepository.save(entity);
        return new TituloDTO(entity);
    }
}
