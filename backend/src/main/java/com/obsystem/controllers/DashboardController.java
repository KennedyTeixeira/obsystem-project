package com.obsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obsystem.dto.DashboardDTO;
import com.obsystem.services.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/resumo")
    public ResponseEntity<DashboardDTO> obterResumo() {
        DashboardDTO dto = service.obterResumoDashboard();
        return ResponseEntity.ok(dto);
    }
}
