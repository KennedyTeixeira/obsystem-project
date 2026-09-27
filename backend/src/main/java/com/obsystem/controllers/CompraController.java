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

import com.obsystem.dto.CompraDTO;
import com.obsystem.entities.enums.CompraStatus;
import com.obsystem.services.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService service;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) CompraStatus status) {
        List<CompraDTO> list = service.findAll(search, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> findById(@PathVariable Integer id) {
        CompraDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CompraDTO> create(@RequestBody CompraDTO dto) {
        CompraDTO novoDto = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(novoDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CompraDTO> alterarStatus(
            @PathVariable Integer id,
            @RequestParam(value = "status") CompraStatus status) {
        CompraDTO atualizado = service.alterarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }
}
