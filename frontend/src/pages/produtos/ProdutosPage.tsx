import React, { useEffect, useState, useId, useMemo } from 'react';
import {
  Package,
  Plus,
  Search,
  Edit2,
  Power,
  X,
  AlertTriangle,
  Loader2,
  Tag,
  FolderTree,
  GitBranch,
  Shield,
  Box,
  Award,
  Ruler,
  Maximize2,
  Disc,
  DollarSign,
  ChevronRight,
  ChevronLeft,
} from 'lucide-react';
import produtoService from '../../services/produtoService';
import produtoApoioService from '../../services/produtoApoioService';
import type { ProdutoDTO, ProdutoStatus } from '../../types/produto';
import type { ProdutoApoioCatalogoDTO, TipoEntidadeApoio } from '../../types/produtoApoio';
import ProdutoApoioDrawer from './components/ProdutoApoioDrawer';
import ReajustePrecosDrawer from './components/ReajustePrecosDrawer';
import styles from './ProdutosPage.module.css';

export const ProdutosPage: React.FC = () => {
  const [produtos, setProdutos] = useState<ProdutoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<ProdutoStatus | ''>('');
  const [categoriaFilter, setCategoriaFilter] = useState('');
  const [subcategoriaFilter, setSubcategoriaFilter] = useState('');
  const [classeFilter, setClasseFilter] = useState('');
  const [milimetroFilter, setMilimetroFilter] = useState<number | ''>('');

  // Drawers de Apoio e Ações (Estilo Bling ERP)
  const [drawerTipo, setDrawerTipo] = useState<TipoEntidadeApoio | null>(null);
  const [reajusteOpen, setReajusteOpen] = useState(false);
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [catalogo, setCatalogo] = useState<ProdutoApoioCatalogoDTO | null>(null);

  // Modal State
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [saving, setSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Form State
  const [formData, setFormData] = useState<ProdutoDTO>({
    codigoProduto: '',
    nomeProduto: '',
    tipoProduto: 'PRODUTO',
    unidadeMedida: 'UN',
    categoria: '',
    subcategoria: '',
    marca: '',
    classe: '',
    modelo: '',
    milimetro: undefined,
    medida: undefined,
    precoCusto: 0,
    precoVenda: 0,
    estoqueMinimo: 0,
    estoqueMaximo: 0,
    estoqueAtual: 0,
    controlaEstoque: 'SIM',
    statusProduto: 'ATIVO',
    fracionar: 'N',
  });

  const searchInputId = useId();
  const statusFilterId = useId();
  const categoriaFilterId = useId();
  const subcategoriaFilterId = useId();
  const classeFilterId = useId();
  const milimetroFilterId = useId();
  const formCodigoId = useId();
  const formNomeId = useId();
  const formTipoProdutoId = useId();
  const formUnidadeId = useId();
  const formCategoriaId = useId();
  const formSubcategoriaId = useId();
  const formMarcaId = useId();
  const formClasseId = useId();
  const formModeloId = useId();
  const formMilimetroId = useId();
  const formMedidaId = useId();
  const formPrecoCustoId = useId();
  const formPrecoVendaId = useId();
  const formEstoqueMinId = useId();
  const formEstoqueMaxId = useId();
  const formEstoqueAtualId = useId();
  const formControlaEstoqueId = useId();
  const formStatusId = useId();

  const carregarCatalogo = async () => {
    try {
      const data = await produtoApoioService.obterCatalogo();
      setCatalogo(data);
    } catch (err) {
      console.error('Erro ao carregar catálogo de apoio:', err);
    }
  };

  const carregarProdutos = async () => {
    try {
      setLoading(true);
      const data = await produtoService.listar(
        search,
        statusFilter ? statusFilter : undefined
      );
      setProdutos(data);
    } catch (err) {
      console.error('Erro ao carregar produtos:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregarProdutos();
  }, [search, statusFilter]);

  useEffect(() => {
    carregarCatalogo();
  }, []);

  // Subcategorias filtradas dinamicamente pela categoria selecionada
  const subcategoriasDisponiveis = useMemo(() => {
    if (!catalogo) return [];
    if (!formData.categoria) return catalogo.subcategorias;
    const catEncontrada = catalogo.categorias.find(
      (c) => c.descricao.toLowerCase() === formData.categoria?.toLowerCase()
    );
    if (!catEncontrada) return catalogo.subcategorias;
    const filtradas = catalogo.subcategorias.filter(
      (s) =>
        s.idCategoria === catEncontrada.id ||
        s.nomeCategoria?.toLowerCase() === formData.categoria?.toLowerCase()
    );
    return filtradas.length > 0 ? filtradas : catalogo.subcategorias;
  }, [catalogo, formData.categoria]);

  // Lista combinada de categorias (catálogo + produtos)
  const categoriasFiltro = useMemo(() => {
    const set = new Set<string>();
    catalogo?.categorias.forEach((c) => {
      if (c.descricao?.trim()) set.add(c.descricao.trim());
    });
    produtos.forEach((p) => {
      if (p.categoria?.trim()) set.add(p.categoria.trim());
    });
    return Array.from(set).sort((a, b) => a.localeCompare(b));
  }, [catalogo, produtos]);

  // Lista combinada de classes (catálogo + produtos)
  const classesFiltro = useMemo(() => {
    const set = new Set<string>();
    catalogo?.classes.forEach((cl) => {
      if (cl.descricao?.trim()) set.add(cl.descricao.trim());
    });
    produtos.forEach((p) => {
      if (p.classe?.trim()) set.add(p.classe.trim());
    });
    return Array.from(set).sort((a, b) => a.localeCompare(b));
  }, [catalogo, produtos]);

  // Lista combinada de milímetros (catálogo + produtos)
  const milimetrosFiltro = useMemo(() => {
    const map = new Map<number, string>();
    catalogo?.milimetros.forEach((m) => {
      if (m.espessura !== undefined && m.espessura !== null) {
        map.set(Number(m.espessura), m.descricao || `${m.espessura}mm`);
      }
    });
    produtos.forEach((p) => {
      if (p.milimetro !== undefined && p.milimetro !== null) {
        const val = Number(p.milimetro);
        if (!map.has(val)) {
          map.set(val, `${val}mm`);
        }
      }
    });
    return Array.from(map.entries())
      .map(([espessura, descricao]) => ({ espessura, descricao }))
      .sort((a, b) => a.espessura - b.espessura);
  }, [catalogo, produtos]);

  // Subcategorias disponíveis para a barra de filtros
  const subcategoriasFiltro = useMemo(() => {
    if (!catalogo) return [];
    if (!categoriaFilter) {
      // Se nenhuma categoria selecionada, reúne todas do catálogo e produtos
      const map = new Map<string, { id?: number; descricao: string }>();
      catalogo.subcategorias.forEach((s) => map.set(s.descricao.toLowerCase(), s));
      produtos.forEach((p) => {
        if (p.subcategoria?.trim() && !map.has(p.subcategoria.toLowerCase())) {
          map.set(p.subcategoria.toLowerCase(), { descricao: p.subcategoria.trim() });
        }
      });
      return Array.from(map.values());
    }
    const catEncontrada = catalogo.categorias.find(
      (c) => c.descricao.toLowerCase() === categoriaFilter.toLowerCase()
    );
    if (!catEncontrada) return catalogo.subcategorias;
    return catalogo.subcategorias.filter(
      (s) =>
        s.idCategoria === catEncontrada.id ||
        s.nomeCategoria?.toLowerCase() === categoriaFilter.toLowerCase()
    );
  }, [catalogo, categoriaFilter, produtos]);

  // Produtos filtrados por categoria, subcategoria, classe e milímetro
  const produtosFiltrados = useMemo(() => {
    return produtos.filter((p) => {
      if (categoriaFilter && p.categoria?.toLowerCase() !== categoriaFilter.toLowerCase()) {
        return false;
      }
      if (subcategoriaFilter && p.subcategoria?.toLowerCase() !== subcategoriaFilter.toLowerCase()) {
        return false;
      }
      if (classeFilter && p.classe?.toLowerCase() !== classeFilter.toLowerCase()) {
        return false;
      }
      if (milimetroFilter !== '' && p.milimetro !== Number(milimetroFilter)) {
        return false;
      }
      return true;
    });
  }, [produtos, categoriaFilter, subcategoriaFilter, classeFilter, milimetroFilter]);

  const hasActiveFilters = Boolean(
    categoriaFilter || subcategoriaFilter || classeFilter || milimetroFilter !== '' || statusFilter || search
  );

  const limparFiltros = () => {
    setSearch('');
    setStatusFilter('');
    setCategoriaFilter('');
    setSubcategoriaFilter('');
    setClasseFilter('');
    setMilimetroFilter('');
  };

  const abrirModalNovo = () => {
    setEditingId(null);
    setErrorMessage(null);
    const defaultTipo = catalogo?.tiposProduto[0]?.nome || 'Mercadoria para Revenda';
    const defaultUnidade = catalogo?.unidadesMedida[0]?.sigla || 'UN';
    setFormData({
      codigoProduto: '',
      nomeProduto: '',
      tipoProduto: defaultTipo,
      unidadeMedida: defaultUnidade,
      categoria: '',
      subcategoria: '',
      marca: '',
      classe: '',
      modelo: '',
      milimetro: undefined,
      medida: undefined,
      precoCusto: 0,
      precoVenda: 0,
      estoqueMinimo: 0,
      estoqueMaximo: 0,
      estoqueAtual: 0,
      controlaEstoque: 'SIM',
      statusProduto: 'ATIVO',
      fracionar: 'N',
    });
    setModalOpen(true);
  };

  const abrirModalEditar = (produto: ProdutoDTO) => {
    setEditingId(produto.id || null);
    setErrorMessage(null);
    setFormData({
      ...produto,
      tipoProduto: produto.tipoProduto || 'PRODUTO',
      unidadeMedida: produto.unidadeMedida || 'UN',
      categoria: produto.categoria || '',
      subcategoria: produto.subcategoria || '',
      classe: produto.classe || '',
      modelo: produto.modelo || '',
      marca: produto.marca || '',
      milimetro: produto.milimetro,
      medida: produto.medida,
      precoCusto: produto.precoCusto ?? 0,
      precoVenda: produto.precoVenda ?? 0,
      estoqueMinimo: produto.estoqueMinimo ?? 0,
      estoqueMaximo: produto.estoqueMaximo ?? 0,
      estoqueAtual: produto.estoqueAtual ?? 0,
    });
    setModalOpen(true);
  };

  const fecharModal = () => {
    setModalOpen(false);
    setEditingId(null);
    setErrorMessage(null);
  };

  const handleSalvar = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setErrorMessage(null);

    try {
      if (editingId) {
        await produtoService.atualizar(editingId, formData);
      } else {
        await produtoService.criar(formData);
      }
      fecharModal();
      await carregarProdutos();
    } catch (err: unknown) {
      const errorObj = err as { response?: { data?: { message?: string } } };
      const msg = errorObj.response?.data?.message || 'Erro ao salvar produto. Verifique os dados.';
      setErrorMessage(msg);
    } finally {
      setSaving(false);
    }
  };

  const alternarStatus = async (produto: ProdutoDTO) => {
    if (!produto.id) return;
    try {
      if (produto.statusProduto === 'ATIVO') {
        await produtoService.inativar(produto.id);
      } else {
        await produtoService.ativar(produto.id);
      }
      await carregarProdutos();
    } catch (err) {
      console.error('Erro ao alterar status:', err);
    }
  };

  // Cálculo de Margem em tempo real no formulário
  const calcularMargemForm = () => {
    const custo = Number(formData.precoCusto) || 0;
    const venda = Number(formData.precoVenda) || 0;
    if (custo > 0) {
      return (((venda - custo) / custo) * 100).toFixed(1);
    }
    return '0.0';
  };

  const formatarMoeda = (valor?: number) => {
    return (valor ?? 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  };

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <h1>Produtos & Estoque</h1>
          <p>Catálogo centralizado de mercadorias, preços e regras de estoque</p>
        </div>
        <div className={styles.headerActions}>
          <button
            type="button"
            onClick={abrirModalNovo}
            className={styles.btnNovoProdutoHeader}
          >
            <Plus size={18} /> Novo Produto
          </button>
        </div>
      </div>

      {/* Layout com Painel Principal e Menu Lateral Direito (Bling ERP) */}
      <div className={styles.pageLayout}>
        <div className={styles.mainContent}>
          {/* Filtros */}
          <div className={styles.filterCard}>
            <div className={styles.searchBox}>
              <Search size={18} color="var(--text-muted)" />
              <label htmlFor={searchInputId} style={{ display: 'none' }}>
                Buscar por nome ou código (SKU)...
              </label>
              <input
                id={searchInputId}
                type="text"
                placeholder="Buscar por nome ou código (SKU)..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>

            <div className={styles.filtersGroup}>
              {/* Filtro: Categoria */}
              <div className={styles.filterItem}>
                <label htmlFor={categoriaFilterId}>Categoria:</label>
                <select
                  id={categoriaFilterId}
                  value={categoriaFilter}
                  onChange={(e) => {
                    setCategoriaFilter(e.target.value);
                    setSubcategoriaFilter('');
                  }}
                >
                  <option value="">Todas</option>
                  {categoriasFiltro.map((catNome) => (
                    <option key={catNome} value={catNome}>
                      {catNome}
                    </option>
                  ))}
                </select>
              </div>

              {/* Filtro: Subcategoria */}
              <div className={styles.filterItem}>
                <label htmlFor={subcategoriaFilterId}>Subcategoria:</label>
                <select
                  id={subcategoriaFilterId}
                  value={subcategoriaFilter}
                  onChange={(e) => setSubcategoriaFilter(e.target.value)}
                >
                  <option value="">Todas</option>
                  {subcategoriasFiltro.map((s, idx) => (
                    <option key={s.id ?? idx} value={s.descricao}>
                      {s.descricao}
                    </option>
                  ))}
                </select>
              </div>

              {/* Filtro: Classe */}
              <div className={styles.filterItem}>
                <label htmlFor={classeFilterId}>Classe:</label>
                <select
                  id={classeFilterId}
                  value={classeFilter}
                  onChange={(e) => setClasseFilter(e.target.value)}
                >
                  <option value="">Todas</option>
                  {classesFiltro.map((classeNome) => (
                    <option key={classeNome} value={classeNome}>
                      {classeNome}
                    </option>
                  ))}
                </select>
              </div>

              {/* Filtro: Milímetro */}
              <div className={styles.filterItem}>
                <label htmlFor={milimetroFilterId}>Milímetro:</label>
                <select
                  id={milimetroFilterId}
                  value={milimetroFilter}
                  onChange={(e) =>
                    setMilimetroFilter(e.target.value ? Number(e.target.value) : '')
                  }
                >
                  <option value="">Todos</option>
                  {milimetrosFiltro.map((mi) => (
                    <option key={mi.espessura} value={mi.espessura}>
                      {mi.descricao}
                    </option>
                  ))}
                </select>
              </div>

              {/* Filtro: Status */}
              <div className={styles.filterItem}>
                <label htmlFor={statusFilterId}>Status:</label>
                <select
                  id={statusFilterId}
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value as ProdutoStatus | '')}
                >
                  <option value="">Todos</option>
                  <option value="ATIVO">Ativos</option>
                  <option value="INATIVO">Inativos</option>
                </select>
              </div>

              {/* Botão Limpar Filtros */}
              {hasActiveFilters && (
                <button
                  type="button"
                  className={styles.btnClearFilters}
                  onClick={limparFiltros}
                  title="Limpar todos os filtros"
                >
                  <X size={14} /> Limpar
                </button>
              )}
            </div>
          </div>

          {/* Tabela de Produtos */}
          <div className={styles.tableCard}>
            {loading ? (
              <div className={styles.emptyState}>
                <Loader2 size={36} className="spin" style={{ animation: 'spin 1s linear infinite' }} />
                <p>Carregando catálogo do Supabase...</p>
              </div>
            ) : produtosFiltrados.length === 0 ? (
              <div className={styles.emptyState}>
                <Package size={48} style={{ color: 'var(--text-subtle)' }} />
                <h3>Nenhum produto encontrado</h3>
                <p>
                  {hasActiveFilters
                    ? 'Nenhum item corresponde aos filtros selecionados. Tente limpar os filtros.'
                    : 'Clique no botão "+ Novo Produto" acima para cadastrar seu primeiro item.'}
                </p>
                {hasActiveFilters && (
                  <button
                    type="button"
                    className={styles.btnClearFilters}
                    onClick={limparFiltros}
                    style={{ marginTop: '0.75rem' }}
                  >
                    <X size={14} /> Limpar filtros
                  </button>
                )}
              </div>
            ) : (
              <div className={styles.tableResponsive}>
                <table className={styles.table}>
                  <thead>
                    <tr>
                      <th>Código</th>
                      <th>Produto</th>
                      <th>Categoria</th>
                      <th>Subcategoria</th>
                      <th>Classe</th>
                      <th>Milímetro</th>
                      <th>Unidade Medida</th>
                      <th>Estoque</th>
                      <th>Preço Venda</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    {produtosFiltrados.map((p) => (
                      <tr key={p.id} style={p.statusProduto === 'INATIVO' ? { opacity: 0.6 } : undefined}>
                        {/* Código */}
                        <td>
                          <span className={styles.skuBadge}>{p.codigoProduto}</span>
                        </td>

                        {/* Produto */}
                        <td>
                          <strong className={styles.productName}>{p.nomeProduto}</strong>
                          {p.statusProduto === 'INATIVO' && (
                            <span style={{ marginLeft: '6px', fontSize: '0.72rem', color: 'var(--text-muted)' }}>
                              (Inativo)
                            </span>
                          )}
                        </td>

                        {/* Categoria */}
                        <td>
                          <span>{p.categoria || '-'}</span>
                        </td>

                        {/* Subcategoria */}
                        <td>
                          <span style={{ color: 'var(--text-muted)' }}>{p.subcategoria || '-'}</span>
                        </td>

                        {/* Classe */}
                        <td>
                          {p.classe ? (
                            <span className={styles.badgeClasse}>{p.classe}</span>
                          ) : (
                            <span style={{ color: 'var(--text-subtle)' }}>-</span>
                          )}
                        </td>

                        {/* Milímetro */}
                        <td>
                          {p.milimetro !== undefined && p.milimetro !== null ? (
                            <span className={styles.badgeMilimetro}>{p.milimetro}mm</span>
                          ) : (
                            <span style={{ color: 'var(--text-subtle)' }}>-</span>
                          )}
                        </td>

                        {/* Unidade Medida */}
                        <td>
                          <span>{p.unidadeMedida || 'UN'}</span>
                        </td>

                        {/* Estoque */}
                        <td>
                          <strong>{p.estoqueAtual ?? 0}</strong>
                        </td>

                        {/* Preço Venda */}
                        <td>
                          <strong style={{ color: 'var(--color-primary)' }}>{formatarMoeda(p.precoVenda)}</strong>
                        </td>

                        {/* Ações */}
                        <td>
                          <div className={styles.actionsCell}>
                            <button
                              type="button"
                              onClick={() => abrirModalEditar(p)}
                              className={styles.actionBtn}
                              title="Editar Produto"
                            >
                              <Edit2 size={16} />
                            </button>
                            <button
                              type="button"
                              onClick={() => alternarStatus(p)}
                              className={styles.actionBtn}
                              title={p.statusProduto === 'ATIVO' ? 'Inativar Produto' : 'Ativar Produto'}
                            >
                              <Power
                                size={16}
                                color={p.statusProduto === 'ATIVO' ? 'var(--color-danger)' : 'var(--color-success)'}
                              />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>

        {/* Menu Lateral à Direita (Bling ERP) */}
    <aside
      className={`${styles.rightSidebar} ${
        sidebarCollapsed ? styles.rightSidebarCollapsed : ''
      }`}
    >
      {!sidebarCollapsed ? (
        <>
          {/* Ferramentas de apoio */}
          <div className={styles.sidebarSection}>
            <span className={styles.sidebarSectionTitle}>Ferramentas de apoio</span>
            <div className={styles.sidebarNavList}>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('tipo_produto')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Tag size={16} />
                </span>
                <span>Tipo de Produtos</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('categoria')}
              >
                <span className={styles.sidebarItemIcon}>
                  <FolderTree size={16} />
                </span>
                <span>Categoria</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('subcategoria')}
              >
                <span className={styles.sidebarItemIcon}>
                  <GitBranch size={16} />
                </span>
                <span>SubCategoria</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('classe')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Shield size={16} />
                </span>
                <span>Classe</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('modelo')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Box size={16} />
                </span>
                <span>Modelo</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('marca')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Award size={16} />
                </span>
                <span>Marca</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('unidade_medida')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Ruler size={16} />
                </span>
                <span>Unidade de Medida</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('medida')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Maximize2 size={16} />
                </span>
                <span>Medida</span>
              </button>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setDrawerTipo('milimetro')}
              >
                <span className={styles.sidebarItemIcon}>
                  <Disc size={16} />
                </span>
                <span>Milímetro</span>
              </button>
            </div>
          </div>

          {/* Mais ações */}
          <div className={styles.sidebarSection}>
            <span className={styles.sidebarSectionTitle}>Mais Ações</span>
            <div className={styles.sidebarNavList}>
              <button
                type="button"
                className={styles.sidebarNavItem}
                onClick={() => setReajusteOpen(true)}
              >
                <span className={styles.sidebarItemIcon}>
                  <DollarSign size={16} />
                </span>
                <span>Reajuste de Preços</span>
              </button>
            </div>
          </div>

          {/* Informações */}
          <div className={styles.sidebarSection}>
            <span className={styles.sidebarSectionTitle}>Informações</span>
            <div className={styles.sidebarInfoCard}>
              <div className={styles.sidebarInfoItem}>
                <span className={styles.sidebarInfoLabel}>Total de Produtos:</span>
                <span className={styles.sidebarInfoValue}>{produtos.length}</span>
              </div>
              {hasActiveFilters && (
                <div className={styles.sidebarInfoItem}>
                  <span className={styles.sidebarInfoLabel}>Filtrados:</span>
                  <span className={`${styles.sidebarInfoValue} ${styles.sidebarInfoHighlight}`}>
                    {produtosFiltrados.length}
                  </span>
                </div>
              )}
              <div className={styles.sidebarInfoItem}>
                <span className={styles.sidebarInfoLabel}>Ativos:</span>
                <span className={`${styles.sidebarInfoValue} ${styles.sidebarInfoHighlight}`}>
                  {produtos.filter((p) => p.statusProduto === 'ATIVO').length}
                </span>
              </div>
              <div className={styles.sidebarInfoItem}>
                <span className={styles.sidebarInfoLabel}>Estoque Baixo:</span>
                <span className={styles.sidebarInfoValue}>
                  {
                    produtos.filter(
                      (p) =>
                        (p.estoqueAtual ?? 0) <= (p.estoqueMinimo ?? 0) &&
                        (p.estoqueMinimo ?? 0) > 0
                    ).length
                  }
                </span>
              </div>
            </div>
          </div>

          <button
            type="button"
            className={styles.btnSidebarToggle}
            onClick={() => setSidebarCollapsed(true)}
            title="Recolher menu lateral"
          >
            <ChevronRight size={18} />
          </button>
        </>
      ) : (
        <>
          <button
            type="button"
            className={styles.btnSidebarToggle}
            onClick={() => setSidebarCollapsed(false)}
            title="Expandir menu lateral"
          >
            <ChevronLeft size={18} />
          </button>
        </>
      )}
    </aside>
  </div>

      {/* Modal de Cadastro / Edição */}
      {modalOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <h2>{editingId ? 'Editar Produto' : 'Novo Produto'}</h2>
              <button type="button" onClick={fecharModal} className={styles.closeBtn}>
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSalvar} style={{ display: 'flex', flexDirection: 'column', flex: 1, minHeight: 0 }}>
              <div className={styles.modalBody}>
                {errorMessage && (
                  <div className={styles.alertBox}>
                    <AlertTriangle size={18} style={{ display: 'inline', marginRight: '6px', verticalAlign: 'text-bottom' }} />
                    {errorMessage}
                  </div>
                )}

                {/* Seção 1: Identificação do Produto */}
                <div>
                  <h3 className={styles.formSectionTitle}>1. Identificação do Produto</h3>
                  <div className={styles.formGrid2}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formCodigoId}>Código / SKU *</label>
                      <input
                        id={formCodigoId}
                        type="text"
                        required
                        placeholder="Ex: PROD-001"
                        value={formData.codigoProduto}
                        onChange={(e) => setFormData({ ...formData, codigoProduto: e.target.value })}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formNomeId}>Nome do Produto *</label>
                      <input
                        id={formNomeId}
                        type="text"
                        required
                        placeholder="Ex: Vidro Temperado Incolor 8mm"
                        value={formData.nomeProduto}
                        onChange={(e) => setFormData({ ...formData, nomeProduto: e.target.value })}
                      />
                    </div>
                  </div>

                  <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formTipoProdutoId}>Tipo de Produto *</label>
                      <select
                        id={formTipoProdutoId}
                        value={formData.tipoProduto}
                        onChange={(e) => setFormData({ ...formData, tipoProduto: e.target.value })}
                        required
                      >
                        <option value="">Selecione o tipo de produto...</option>
                        {catalogo?.tiposProduto.map((t) => (
                          <option key={t.id} value={t.nome}>
                            {t.nome}
                          </option>
                        ))}
                        {(!catalogo?.tiposProduto || catalogo.tiposProduto.length === 0) && (
                          <>
                            <option value="PRODUTO">Produto (Revenda / Acabado)</option>
                            <option value="INSUMO">Insumo (Matéria-prima)</option>
                            <option value="SERVICO">Serviço</option>
                          </>
                        )}
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formUnidadeId}>Unidade de Medida</label>
                      <select
                        id={formUnidadeId}
                        value={formData.unidadeMedida || ''}
                        onChange={(e) => setFormData({ ...formData, unidadeMedida: e.target.value })}
                      >
                        <option value="">Selecione a unidade...</option>
                        {catalogo?.unidadesMedida.map((u) => (
                          <option key={u.id} value={u.sigla}>
                            {u.sigla} - {u.descricao}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </div>

                {/* Seção 2: Classificação & Categoria */}
                <div>
                  <h3 className={styles.formSectionTitle}>2. Classificação & Categorização</h3>
                  <div className={styles.formGrid2}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formCategoriaId}>Categoria</label>
                      <select
                        id={formCategoriaId}
                        value={formData.categoria || ''}
                        onChange={(e) => {
                          const cat = e.target.value;
                          setFormData({ ...formData, categoria: cat, subcategoria: '' });
                        }}
                      >
                        <option value="">Selecione a categoria...</option>
                        {catalogo?.categorias.map((c) => (
                          <option key={c.id} value={c.descricao}>
                            {c.descricao}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formSubcategoriaId}>Subcategoria</label>
                      <select
                        id={formSubcategoriaId}
                        value={formData.subcategoria || ''}
                        onChange={(e) => setFormData({ ...formData, subcategoria: e.target.value })}
                      >
                        <option value="">Selecione a subcategoria...</option>
                        {subcategoriasDisponiveis.map((s) => (
                          <option key={s.id} value={s.descricao}>
                            {s.descricao} {s.nomeCategoria && !formData.categoria ? `(${s.nomeCategoria})` : ''}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>

                  <div className={styles.formGrid3} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formMarcaId}>Marca / Fabricante</label>
                      <select
                        id={formMarcaId}
                        value={formData.marca || ''}
                        onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
                      >
                        <option value="">Selecione a marca...</option>
                        {catalogo?.marcas.map((m) => (
                          <option key={m.id} value={m.descricao}>
                            {m.descricao}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formClasseId}>Classe do Material</label>
                      <select
                        id={formClasseId}
                        value={formData.classe || ''}
                        onChange={(e) => setFormData({ ...formData, classe: e.target.value })}
                      >
                        <option value="">Selecione a classe...</option>
                        {catalogo?.classes.map((cl) => (
                          <option key={cl.id} value={cl.descricao}>
                            {cl.descricao}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formModeloId}>Modelo Construtivo</label>
                      <select
                        id={formModeloId}
                        value={formData.modelo || ''}
                        onChange={(e) => setFormData({ ...formData, modelo: e.target.value })}
                      >
                        <option value="">Selecione o modelo...</option>
                        {catalogo?.modelos.map((mo) => (
                          <option key={mo.id} value={mo.descricao}>
                            {mo.descricao}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </div>

                {/* Seção 3: Dimensões & Especificações Técnicas */}
                <div>
                  <h3 className={styles.formSectionTitle}>3. Dimensões & Especificações Técnicas</h3>
                  <div className={styles.formGrid2}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formMilimetroId}>Milímetro / Espessura</label>
                      <select
                        id={formMilimetroId}
                        value={formData.milimetro !== undefined && formData.milimetro !== null ? formData.milimetro : ''}
                        onChange={(e) =>
                          setFormData({
                            ...formData,
                            milimetro: e.target.value ? Number(e.target.value) : undefined,
                          })
                        }
                      >
                        <option value="">Selecione a espessura...</option>
                        {catalogo?.milimetros.map((mi) => (
                          <option key={mi.id} value={mi.espessura}>
                            {mi.descricao || `${mi.espessura}mm`}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formMedidaId}>Medida Padrão</label>
                      <select
                        id={formMedidaId}
                        value={formData.medida !== undefined && formData.medida !== null ? formData.medida : ''}
                        onChange={(e) =>
                          setFormData({
                            ...formData,
                            medida: e.target.value ? Number(e.target.value) : undefined,
                          })
                        }
                      >
                        <option value="">Selecione a medida padrão...</option>
                        {catalogo?.medidas.map((me) => (
                          <option key={me.id} value={me.id}>
                            {me.descricao} {me.largura && me.altura ? `(${me.largura} x ${me.altura} mm)` : ''}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </div>

                {/* Seção 4: Preços e Margem */}
                <div>
                  <h3 className={styles.formSectionTitle}>4. Precificação & Lucro Comercial</h3>
                  <div className={styles.formGrid3}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formPrecoCustoId}>Preço de Custo (R$)</label>
                      <input
                        id={formPrecoCustoId}
                        type="number"
                        step="0.01"
                        min="0"
                        value={formData.precoCusto}
                        onChange={(e) => setFormData({ ...formData, precoCusto: parseFloat(e.target.value) || 0 })}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formPrecoVendaId}>Preço de Venda (R$)</label>
                      <input
                        id={formPrecoVendaId}
                        type="number"
                        step="0.01"
                        min="0"
                        value={formData.precoVenda}
                        onChange={(e) => setFormData({ ...formData, precoVenda: parseFloat(e.target.value) || 0 })}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label>Margem Prevista</label>
                      <div
                        style={{
                          padding: '0.6rem 0.85rem',
                          backgroundColor: 'var(--color-success-light)',
                          color: 'var(--color-success-text)',
                          borderRadius: 'var(--radius-md)',
                          fontWeight: 700,
                          fontSize: '0.95rem',
                          textAlign: 'center',
                          border: '1px solid #bbf7d0',
                        }}
                      >
                        +{calcularMargemForm()}%
                      </div>
                    </div>
                  </div>
                </div>

                {/* Seção 5: Estoque */}
                <div>
                  <h3 className={styles.formSectionTitle}>5. Parâmetros de Estoque</h3>
                  <div className={styles.formGrid3}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formEstoqueMinId}>Estoque Mínimo</label>
                      <input
                        id={formEstoqueMinId}
                        type="number"
                        step="0.001"
                        min="0"
                        value={formData.estoqueMinimo}
                        onChange={(e) => setFormData({ ...formData, estoqueMinimo: parseFloat(e.target.value) || 0 })}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formEstoqueMaxId}>Estoque Máximo</label>
                      <input
                        id={formEstoqueMaxId}
                        type="number"
                        step="0.001"
                        min="0"
                        value={formData.estoqueMaximo}
                        onChange={(e) => setFormData({ ...formData, estoqueMaximo: parseFloat(e.target.value) || 0 })}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formEstoqueAtualId}>Estoque Atual</label>
                      <input
                        id={formEstoqueAtualId}
                        type="number"
                        step="0.001"
                        min="0"
                        value={formData.estoqueAtual}
                        onChange={(e) => setFormData({ ...formData, estoqueAtual: parseFloat(e.target.value) || 0 })}
                      />
                    </div>
                  </div>

                  <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formControlaEstoqueId}>Controla Estoque?</label>
                      <select
                        id={formControlaEstoqueId}
                        value={formData.controlaEstoque}
                        onChange={(e) => setFormData({ ...formData, controlaEstoque: e.target.value as 'SIM' | 'NAO' })}
                      >
                        <option value="SIM">Sim</option>
                        <option value="NAO">Não</option>
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formStatusId}>Status Inicial</label>
                      <select
                        id={formStatusId}
                        value={formData.statusProduto}
                        onChange={(e) => setFormData({ ...formData, statusProduto: e.target.value as ProdutoStatus })}
                      >
                        <option value="ATIVO">Ativo</option>
                        <option value="INATIVO">Inativo</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>

              <div className={styles.modalFooter}>
                <button type="button" onClick={fecharModal} className={styles.btnSecondary} disabled={saving}>
                  Cancelar
                </button>
                <button type="submit" className={styles.btnPrimary} disabled={saving}>
                  {saving ? 'Gravando...' : editingId ? 'Salvar Alterações' : 'Cadastrar Produto'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Drawer Deslizante da Direita para Esquerda (Cadastros de Apoio) */}
      <ProdutoApoioDrawer
        isOpen={drawerTipo !== null}
        onClose={() => setDrawerTipo(null)}
        tipoEntidade={drawerTipo}
        onCatalogoUpdated={() => {
          carregarCatalogo();
          carregarProdutos();
        }}
      />

      {/* Drawer Deslizante Inferior (Bottom Sheet - Reajuste de Preços) */}
      <ReajustePrecosDrawer
        isOpen={reajusteOpen}
        onClose={() => setReajusteOpen(false)}
        produtos={produtos}
        onPrecosAtualizados={carregarProdutos}
      />
    </div>

  );
};

export default ProdutosPage;
