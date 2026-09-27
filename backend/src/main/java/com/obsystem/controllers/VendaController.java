package com.obsystem.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.obsystem.dto.VendaDTO;
import com.obsystem.entities.enums.VendaStatus;
import com.obsystem.services.VendaService;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    public ResponseEntity<List<VendaDTO>> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) VendaStatus status) {
        List<VendaDTO> list = service.findAll(search, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> findById(@PathVariable Integer id) {
        VendaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VendaDTO> create(@RequestBody VendaDTO dto) {
        VendaDTO novoDto = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(novoDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VendaDTO> alterarStatus(
            @PathVariable Integer id,
            @RequestParam("status") VendaStatus status) {
        VendaDTO atualizado = service.alterarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }
}
