package com.obsystem.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.obsystem.dto.LiquidarTituloDTO;
import com.obsystem.dto.ResumoFinanceiroDTO;
import com.obsystem.dto.TituloDTO;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.services.TituloService;

@RestController
@RequestMapping("/api/titulos")
public class TituloController {

    @Autowired
    private TituloService service;

    @GetMapping
    public ResponseEntity<List<TituloDTO>> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "tipo", required = false) TipoTitulo tipo,
            @RequestParam(value = "status", required = false) TituloStatus status,
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<TituloDTO> list = service.findAll(search, tipo, status, dataInicio, dataFim);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TituloDTO> findById(@PathVariable Integer id) {
        TituloDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroDTO> obterResumo() {
        ResumoFinanceiroDTO resumo = service.obterResumo();
        return ResponseEntity.ok(resumo);
    }

    @PostMapping
    public ResponseEntity<TituloDTO> create(@RequestBody TituloDTO dto) {
        TituloDTO novoDto = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(novoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TituloDTO> update(@PathVariable Integer id, @RequestBody TituloDTO dto) {
        TituloDTO atualizado = service.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/liquidar")
    public ResponseEntity<TituloDTO> liquidar(
            @PathVariable Integer id,
            @RequestBody(required = false) LiquidarTituloDTO dto) {
        LocalDate dataPagamento = (dto != null && dto.getDataPagamento() != null) ? dto.getDataPagamento() : LocalDate.now();
        TituloDTO atualizado = service.liquidar(id, dataPagamento);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<TituloDTO> cancelar(@PathVariable Integer id) {
        TituloDTO atualizado = service.cancelar(id);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/estornar")
    public ResponseEntity<TituloDTO> estornar(@PathVariable Integer id) {
        TituloDTO atualizado = service.estornar(id);
        return ResponseEntity.ok(atualizado);
    }
}
