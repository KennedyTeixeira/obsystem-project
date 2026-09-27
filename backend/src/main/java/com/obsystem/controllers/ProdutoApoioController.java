package com.obsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obsystem.dto.CategoriaDTO;
import com.obsystem.dto.ClasseDTO;
import com.obsystem.dto.MarcaDTO;
import com.obsystem.dto.MedidaDTO;
import com.obsystem.dto.MilimetroDTO;
import com.obsystem.dto.ModeloDTO;
import com.obsystem.dto.ProdutoApoioCatalogoDTO;
import com.obsystem.dto.SubCategoriaDTO;
import com.obsystem.dto.TipoProdutoDTO;
import com.obsystem.dto.UnidadeMedidaDTO;
import com.obsystem.services.ProdutoApoioService;

@RestController
@RequestMapping("/api/produtos-apoio")
public class ProdutoApoioController {

    @Autowired
    private ProdutoApoioService service;

    @GetMapping("/catalogo")
    public ResponseEntity<ProdutoApoioCatalogoDTO> obterCatalogo() {
        return ResponseEntity.ok(service.obterCatalogoCompleto());
    }

    // --- Tipos de Produto ---
    @GetMapping("/tipos-produto")
    public ResponseEntity<List<TipoProdutoDTO>> listarTiposProduto() {
        return ResponseEntity.ok(service.listarTiposProduto());
    }

    @PostMapping("/tipos-produto")
    public ResponseEntity<TipoProdutoDTO> salvarTipoProduto(@RequestBody TipoProdutoDTO dto) {
        return ResponseEntity.ok(service.salvarTipoProduto(dto));
    }

    @DeleteMapping("/tipos-produto/{id}")
    public ResponseEntity<Void> excluirTipoProduto(@PathVariable Integer id) {
        service.excluirTipoProduto(id);
        return ResponseEntity.noContent().build();
    }

    // --- Unidades de Medida ---
    @GetMapping("/unidades-medida")
    public ResponseEntity<List<UnidadeMedidaDTO>> listarUnidadesMedida() {
        return ResponseEntity.ok(service.listarUnidadesMedida());
    }

    @PostMapping("/unidades-medida")
    public ResponseEntity<UnidadeMedidaDTO> salvarUnidadeMedida(@RequestBody UnidadeMedidaDTO dto) {
        return ResponseEntity.ok(service.salvarUnidadeMedida(dto));
    }

    @DeleteMapping("/unidades-medida/{id}")
    public ResponseEntity<Void> excluirUnidadeMedida(@PathVariable Integer id) {
        service.excluirUnidadeMedida(id);
        return ResponseEntity.noContent().build();
    }

    // --- Categorias ---
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> salvarCategoria(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(service.salvarCategoria(dto));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Integer id) {
        service.excluirCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // --- SubCategorias ---
    @GetMapping("/subcategorias")
    public ResponseEntity<List<SubCategoriaDTO>> listarSubCategorias(@RequestParam(required = false) Integer idCategoria) {
        return ResponseEntity.ok(service.listarSubCategorias(idCategoria));
    }

    @PostMapping("/subcategorias")
    public ResponseEntity<SubCategoriaDTO> salvarSubCategoria(@RequestBody SubCategoriaDTO dto) {
        return ResponseEntity.ok(service.salvarSubCategoria(dto));
    }

    @DeleteMapping("/subcategorias/{id}")
    public ResponseEntity<Void> excluirSubCategoria(@PathVariable Integer id) {
        service.excluirSubCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // --- Classes ---
    @GetMapping("/classes")
    public ResponseEntity<List<ClasseDTO>> listarClasses() {
        return ResponseEntity.ok(service.listarClasses());
    }

    @PostMapping("/classes")
    public ResponseEntity<ClasseDTO> salvarClasse(@RequestBody ClasseDTO dto) {
        return ResponseEntity.ok(service.salvarClasse(dto));
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<Void> excluirClasse(@PathVariable Integer id) {
        service.excluirClasse(id);
        return ResponseEntity.noContent().build();
    }

    // --- Modelos ---
    @GetMapping("/modelos")
    public ResponseEntity<List<ModeloDTO>> listarModelos() {
        return ResponseEntity.ok(service.listarModelos());
    }

    @PostMapping("/modelos")
    public ResponseEntity<ModeloDTO> salvarModelo(@RequestBody ModeloDTO dto) {
        return ResponseEntity.ok(service.salvarModelo(dto));
    }

    @DeleteMapping("/modelos/{id}")
    public ResponseEntity<Void> excluirModelo(@PathVariable Integer id) {
        service.excluirModelo(id);
        return ResponseEntity.noContent().build();
    }

    // --- Marcas ---
    @GetMapping("/marcas")
    public ResponseEntity<List<MarcaDTO>> listarMarcas() {
        return ResponseEntity.ok(service.listarMarcas());
    }

    @PostMapping("/marcas")
    public ResponseEntity<MarcaDTO> salvarMarca(@RequestBody MarcaDTO dto) {
        return ResponseEntity.ok(service.salvarMarca(dto));
    }

    @DeleteMapping("/marcas/{id}")
    public ResponseEntity<Void> excluirMarca(@PathVariable Integer id) {
        service.excluirMarca(id);
        return ResponseEntity.noContent().build();
    }

    // --- Milimetros ---
    @GetMapping("/milimetros")
    public ResponseEntity<List<MilimetroDTO>> listarMilimetros() {
        return ResponseEntity.ok(service.listarMilimetros());
    }

    @PostMapping("/milimetros")
    public ResponseEntity<MilimetroDTO> salvarMilimetro(@RequestBody MilimetroDTO dto) {
        return ResponseEntity.ok(service.salvarMilimetro(dto));
    }

    @DeleteMapping("/milimetros/{id}")
    public ResponseEntity<Void> excluirMilimetro(@PathVariable Integer id) {
        service.excluirMilimetro(id);
        return ResponseEntity.noContent().build();
    }

    // --- Medidas ---
    @GetMapping("/medidas")
    public ResponseEntity<List<MedidaDTO>> listarMedidas() {
        return ResponseEntity.ok(service.listarMedidas());
    }

    @PostMapping("/medidas")
    public ResponseEntity<MedidaDTO> salvarMedida(@RequestBody MedidaDTO dto) {
        return ResponseEntity.ok(service.salvarMedida(dto));
    }

    @DeleteMapping("/medidas/{id}")
    public ResponseEntity<Void> excluirMedida(@PathVariable Integer id) {
        service.excluirMedida(id);
        return ResponseEntity.noContent().build();
    }
}
