package com.obsystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.CentroCustoDTO;
import com.obsystem.dto.ClassificacaoDTO;
import com.obsystem.dto.PlanoContaDTO;
import com.obsystem.entities.enums.CentroCustoStatus;
import com.obsystem.entities.enums.ClassificacaoStatus;
import com.obsystem.entities.enums.PlanoContaStatus;
import com.obsystem.repositories.CentroCustoRepository;
import com.obsystem.repositories.ClassificacaoRepository;
import com.obsystem.repositories.PlanoContaRepository;

@Service
public class FinanceiroApoioService {

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private PlanoContaRepository planoContaRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Transactional(readOnly = true)
    public List<CentroCustoDTO> listarCentrosCusto() {
        return centroCustoRepository.findByStatus(CentroCustoStatus.ATIVO)
                .stream().map(CentroCustoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanoContaDTO> listarPlanosConta() {
        return planoContaRepository.findByStatus(PlanoContaStatus.ATIVO)
                .stream().map(PlanoContaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassificacaoDTO> listarClassificacoes() {
        return classificacaoRepository.findByStatus(ClassificacaoStatus.ATIVO)
                .stream().map(ClassificacaoDTO::new).collect(Collectors.toList());
    }
}
