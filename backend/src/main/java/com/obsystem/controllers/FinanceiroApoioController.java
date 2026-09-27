package com.obsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obsystem.dto.CentroCustoDTO;
import com.obsystem.dto.ClassificacaoDTO;
import com.obsystem.dto.PlanoContaDTO;
import com.obsystem.services.FinanceiroApoioService;

@RestController
@RequestMapping("/api/financeiro")
public class FinanceiroApoioController {

    @Autowired
    private FinanceiroApoioService service;

    @GetMapping("/centros-custo")
    public ResponseEntity<List<CentroCustoDTO>> listarCentrosCusto() {
        return ResponseEntity.ok(service.listarCentrosCusto());
    }

    @GetMapping("/planos-conta")
    public ResponseEntity<List<PlanoContaDTO>> listarPlanosConta() {
        return ResponseEntity.ok(service.listarPlanosConta());
    }

    @GetMapping("/classificacoes")
    public ResponseEntity<List<ClassificacaoDTO>> listarClassificacoes() {
        return ResponseEntity.ok(service.listarClassificacoes());
    }
}
