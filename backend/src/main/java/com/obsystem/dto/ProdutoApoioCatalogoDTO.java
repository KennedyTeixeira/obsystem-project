package com.obsystem.dto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoApoioCatalogoDTO {

    private List<TipoProdutoDTO> tiposProduto = new ArrayList<>();
    private List<UnidadeMedidaDTO> unidadesMedida = new ArrayList<>();
    private List<CategoriaDTO> categorias = new ArrayList<>();
    private List<SubCategoriaDTO> subcategorias = new ArrayList<>();
    private List<ClasseDTO> classes = new ArrayList<>();
    private List<ModeloDTO> modelos = new ArrayList<>();
    private List<MarcaDTO> marcas = new ArrayList<>();
    private List<MilimetroDTO> milimetros = new ArrayList<>();
    private List<MedidaDTO> medidas = new ArrayList<>();

    public ProdutoApoioCatalogoDTO() {
    }

    public List<TipoProdutoDTO> getTiposProduto() {
        return tiposProduto;
    }

    public void setTiposProduto(List<TipoProdutoDTO> tiposProduto) {
        this.tiposProduto = tiposProduto;
    }

    public List<UnidadeMedidaDTO> getUnidadesMedida() {
        return unidadesMedida;
    }

    public void setUnidadesMedida(List<UnidadeMedidaDTO> unidadesMedida) {
        this.unidadesMedida = unidadesMedida;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public List<SubCategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<SubCategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public List<ClasseDTO> getClasses() {
        return classes;
    }

    public void setClasses(List<ClasseDTO> classes) {
        this.classes = classes;
    }

    public List<ModeloDTO> getModelos() {
        return modelos;
    }

    public void setModelos(List<ModeloDTO> modelos) {
        this.modelos = modelos;
    }

    public List<MarcaDTO> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<MarcaDTO> marcas) {
        this.marcas = marcas;
    }

    public List<MilimetroDTO> getMilimetros() {
        return milimetros;
    }

    public void setMilimetros(List<MilimetroDTO> milimetros) {
        this.milimetros = milimetros;
    }

    public List<MedidaDTO> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<MedidaDTO> medidas) {
        this.medidas = medidas;
    }
}
