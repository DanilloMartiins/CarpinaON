package com.carpinaon.controller;

import com.carpinaon.dto.dashboard.DashboardDTO;
import com.carpinaon.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller do dashboard - visão rápida pros servidor (só ADMIN)
@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    // GET /api/v1/admin/dashboard - resumo com os números principais
    @GetMapping
    public ResponseEntity<DashboardDTO> resumo() {
        return ResponseEntity.ok(dashboardService.obterResumo());
    }
}