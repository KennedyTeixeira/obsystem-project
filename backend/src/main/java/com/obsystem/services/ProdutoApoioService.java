package com.obsystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.obsystem.entities.Categoria;
import com.obsystem.entities.Classe;
import com.obsystem.entities.Marca;
import com.obsystem.entities.Medida;
import com.obsystem.entities.Milimetro;
import com.obsystem.entities.Modelo;
import com.obsystem.entities.SubCategoria;
import com.obsystem.entities.TipoProduto;
import com.obsystem.entities.UnidadeMedida;
import com.obsystem.entities.enums.ProdutoStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import com.obsystem.repositories.CategoriaRepository;
import com.obsystem.repositories.ClasseRepository;
import com.obsystem.repositories.MarcaRepository;
import com.obsystem.repositories.MedidaRepository;
import com.obsystem.repositories.MilimetroRepository;
import com.obsystem.repositories.ModeloRepository;
import com.obsystem.repositories.SubCategoriaRepository;
import com.obsystem.repositories.TipoProdutoRepository;
import com.obsystem.repositories.UnidadeMedidaRepository;

@Service
public class ProdutoApoioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TipoProdutoRepository tipoProdutoRepository;

    @PostConstruct
    public void ajustarConstraints() {
        try {
            jdbcTemplate.execute("ALTER TABLE obs_produto DROP CONSTRAINT IF EXISTS obs_produto_tp_produto_check");
        } catch (Exception e) {
            // Ignora se não existir
        }

        try {
            // Cria tabela associativa para ManyToMany se ainda não existir
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS obs_categoria_sub_categoria (" +
                "    id_sub_categoria INT4 NOT NULL, " +
                "    id_categoria INT4 NOT NULL, " +
                "    CONSTRAINT pk_categoria_sub_categoria PRIMARY KEY (id_sub_categoria, id_categoria)" +
                ")"
            );

            // Migra dados existentes de obs_sub_categoria.id_categoria
            jdbcTemplate.execute(
                "INSERT INTO obs_categoria_sub_categoria (id_sub_categoria, id_categoria) " +
                "SELECT id_sub_categoria, id_categoria " +
                "FROM obs_sub_categoria " +
                "WHERE id_categoria IS NOT NULL " +
                "ON CONFLICT DO NOTHING"
            );
        } catch (Exception e) {
            // Ignora se já estiver ajustado
        }
    }

    @Autowired
    private UnidadeMedidaRepository unidadeMedidaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubCategoriaRepository subCategoriaRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private MilimetroRepository milimetroRepository;

    @Autowired
    private MedidaRepository medidaRepository;

    @Transactional(readOnly = true)
    public ProdutoApoioCatalogoDTO obterCatalogoCompleto() {
        ProdutoApoioCatalogoDTO catalogo = new ProdutoApoioCatalogoDTO();
        catalogo.setTiposProduto(listarTiposProduto());
        catalogo.setUnidadesMedida(listarUnidadesMedida());
        catalogo.setCategorias(listarCategorias());
        catalogo.setSubcategorias(listarSubCategorias(null));
        catalogo.setClasses(listarClasses());
        catalogo.setModelos(listarModelos());
        catalogo.setMarcas(listarMarcas());
        catalogo.setMilimetros(listarMilimetros());
        catalogo.setMedidas(listarMedidas());
        return catalogo;
    }

    // --- TipoProduto ---
    @Transactional(readOnly = true)
    public List<TipoProdutoDTO> listarTiposProduto() {
        return tipoProdutoRepository.findAllByOrderByNomeAsc().stream()
                .map(TipoProdutoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TipoProdutoDTO salvarTipoProduto(TipoProdutoDTO dto) {
        TipoProduto entity = (dto.getId() != null)
                ? tipoProdutoRepository.findById(dto.getId()).orElse(new TipoProduto())
                : new TipoProduto();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new TipoProdutoDTO(tipoProdutoRepository.save(entity));
    }

    @Transactional
    public void excluirTipoProduto(Integer id) {
        tipoProdutoRepository.deleteById(id);
    }

    // --- UnidadeMedida ---
    @Transactional(readOnly = true)
    public List<UnidadeMedidaDTO> listarUnidadesMedida() {
        return unidadeMedidaRepository.findAllByOrderBySiglaAsc().stream()
                .map(UnidadeMedidaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UnidadeMedidaDTO salvarUnidadeMedida(UnidadeMedidaDTO dto) {
        UnidadeMedida entity = (dto.getId() != null)
                ? unidadeMedidaRepository.findById(dto.getId()).orElse(new UnidadeMedida())
                : new UnidadeMedida();
        entity.setSigla(dto.getSigla().toUpperCase().trim());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new UnidadeMedidaDTO(unidadeMedidaRepository.save(entity));
    }

    @Transactional
    public void excluirUnidadeMedida(Integer id) {
        unidadeMedidaRepository.deleteById(id);
    }

    // --- Categoria ---
    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAllByOrderByDescricaoAsc().stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaDTO salvarCategoria(CategoriaDTO dto) {
        Categoria entity = (dto.getId() != null)
                ? categoriaRepository.findById(dto.getId()).orElse(new Categoria())
                : new Categoria();
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new CategoriaDTO(categoriaRepository.save(entity));
    }

    @Transactional
    public void excluirCategoria(Integer id) {
        try {
            jdbcTemplate.update("DELETE FROM obs_categoria_sub_categoria WHERE id_categoria = ?", id);
        } catch (Exception e) {
            // Ignora se não existir
        }
        categoriaRepository.deleteById(id);
    }

    // --- SubCategoria ---
    @Transactional(readOnly = true)
    public List<SubCategoriaDTO> listarSubCategorias(Integer idCategoria) {
        List<SubCategoria> lista = (idCategoria != null)
                ? subCategoriaRepository.findByCategoriaId(idCategoria)
                : subCategoriaRepository.findAllWithCategorias();
        return lista.stream().map(SubCategoriaDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public SubCategoriaDTO salvarSubCategoria(SubCategoriaDTO dto) {
        SubCategoria entity = (dto.getId() != null)
                ? subCategoriaRepository.findById(dto.getId()).orElse(new SubCategoria())
                : new SubCategoria();
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);

        java.util.Set<Integer> idsCats = new java.util.HashSet<>();
        if (dto.getIdsCategorias() != null && !dto.getIdsCategorias().isEmpty()) {
            idsCats.addAll(dto.getIdsCategorias());
        } else if (dto.getIdCategoria() != null) {
            idsCats.add(dto.getIdCategoria());
        }

        entity.getCategorias().clear();
        if (!idsCats.isEmpty()) {
            List<Categoria> cats = categoriaRepository.findAllById(idsCats);
            entity.getCategorias().addAll(cats);
        }

        return new SubCategoriaDTO(subCategoriaRepository.save(entity));
    }

    @Transactional
    public void excluirSubCategoria(Integer id) {
        try {
            jdbcTemplate.update("DELETE FROM obs_categoria_sub_categoria WHERE id_sub_categoria = ?", id);
        } catch (Exception e) {
            // Ignora se não existir
        }
        subCategoriaRepository.deleteById(id);
    }

    // --- Classe ---
    @Transactional(readOnly = true)
    public List<ClasseDTO> listarClasses() {
        return classeRepository.findAllByOrderByDescricaoAsc().stream()
                .map(ClasseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClasseDTO salvarClasse(ClasseDTO dto) {
        Classe entity = (dto.getId() != null)
                ? classeRepository.findById(dto.getId()).orElse(new Classe())
                : new Classe();
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new ClasseDTO(classeRepository.save(entity));
    }

    @Transactional
    public void excluirClasse(Integer id) {
        classeRepository.deleteById(id);
    }

    // --- Modelo ---
    @Transactional(readOnly = true)
    public List<ModeloDTO> listarModelos() {
        return modeloRepository.findAllByOrderByDescricaoAsc().stream()
                .map(ModeloDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ModeloDTO salvarModelo(ModeloDTO dto) {
        Modelo entity = (dto.getId() != null)
                ? modeloRepository.findById(dto.getId()).orElse(new Modelo())
                : new Modelo();
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new ModeloDTO(modeloRepository.save(entity));
    }

    @Transactional
    public void excluirModelo(Integer id) {
        modeloRepository.deleteById(id);
    }

    // --- Marca ---
    @Transactional(readOnly = true)
    public List<MarcaDTO> listarMarcas() {
        return marcaRepository.findAllByOrderByDescricaoAsc().stream()
                .map(MarcaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MarcaDTO salvarMarca(MarcaDTO dto) {
        Marca entity = (dto.getId() != null)
                ? marcaRepository.findById(dto.getId()).orElse(new Marca())
                : new Marca();
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new MarcaDTO(marcaRepository.save(entity));
    }

    @Transactional
    public void excluirMarca(Integer id) {
        marcaRepository.deleteById(id);
    }

    // --- Milimetro ---
    @Transactional(readOnly = true)
    public List<MilimetroDTO> listarMilimetros() {
        return milimetroRepository.findAllByOrderByEspessuraAsc().stream()
                .map(MilimetroDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MilimetroDTO salvarMilimetro(MilimetroDTO dto) {
        Milimetro entity = (dto.getId() != null)
                ? milimetroRepository.findById(dto.getId()).orElse(new Milimetro())
                : new Milimetro();
        entity.setEspessura(dto.getEspessura());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new MilimetroDTO(milimetroRepository.save(entity));
    }

    @Transactional
    public void excluirMilimetro(Integer id) {
        milimetroRepository.deleteById(id);
    }

    // --- Medida ---
    @Transactional(readOnly = true)
    public List<MedidaDTO> listarMedidas() {
        return medidaRepository.findAllByOrderByDescricaoAsc().stream()
                .map(MedidaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedidaDTO salvarMedida(MedidaDTO dto) {
        Medida entity = (dto.getId() != null)
                ? medidaRepository.findById(dto.getId()).orElse(new Medida())
                : new Medida();
        entity.setDescricao(dto.getDescricao());
        entity.setLargura(dto.getLargura());
        entity.setAltura(dto.getAltura());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ProdutoStatus.ATIVO);
        return new MedidaDTO(medidaRepository.save(entity));
    }

    @Transactional
    public void excluirMedida(Integer id) {
        medidaRepository.deleteById(id);
    }
}
