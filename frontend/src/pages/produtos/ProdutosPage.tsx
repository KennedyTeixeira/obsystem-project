import React, { useEffect, useState, useId } from 'react';
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
import type { ProdutoDTO, ProdutoStatus, ProdutoTipo } from '../../types/produto';
import type { ProdutoApoioCatalogoDTO, TipoEntidadeApoio } from '../../types/produtoApoio';
import ProdutoApoioDrawer from './components/ProdutoApoioDrawer';
import ReajustePrecosDrawer from './components/ReajustePrecosDrawer';
import styles from './ProdutosPage.module.css';

export const ProdutosPage: React.FC = () => {
  const [produtos, setProdutos] = useState<ProdutoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<ProdutoStatus | ''>('');

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
    categoria: '',
    marca: '',
    unidadeMedida: 'UN',
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
  const formCodigoId = useId();
  const formNomeId = useId();
  const formTipoProdutoId = useId();
  const formCategoriaId = useId();
  const formMarcaId = useId();
  const formUnidadeId = useId();
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


  const abrirModalNovo = () => {
    setEditingId(null);
    setErrorMessage(null);
    setFormData({
      codigoProduto: '',
      nomeProduto: '',
      tipoProduto: 'PRODUTO',
      categoria: '',
      marca: '',
      unidadeMedida: 'UN',
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
          <label htmlFor={searchInputId} style={{ display: 'none' }}>Buscar por nome ou código (SKU)...</label>
          <input
            id={searchInputId}
            type="text"
            placeholder="Buscar por nome ou código (SKU)..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className={styles.statusFilter}>
          <label htmlFor={statusFilterId} style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-muted)' }}>Status:</label>
          <select
            id={statusFilterId}
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as ProdutoStatus | '')}
          >
            <option value="">Todos</option>
            <option value="ATIVO">Somente Ativos</option>
            <option value="INATIVO">Somente Inativos</option>
          </select>
        </div>
      </div>

      {/* Tabela de Produtos */}
      <div className={styles.tableCard}>
        {loading ? (
          <div className={styles.emptyState}>
            <Loader2 size={36} className="spin" style={{ animation: 'spin 1s linear infinite' }} />
            <p>Carregando catálogo do Supabase...</p>
          </div>
        ) : produtos.length === 0 ? (
          <div className={styles.emptyState}>
            <Package size={48} style={{ color: 'var(--text-subtle)' }} />
            <h3>Nenhum produto encontrado</h3>
            <p>Clique no botão "+ Novo Produto" acima para cadastrar seu primeiro item.</p>
          </div>
        ) : (
          <div className={styles.tableResponsive}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Código (SKU)</th>
                  <th>Produto</th>
                  <th>Categoria / Marca</th>
                  <th>Preço Custo</th>
                  <th>Preço Venda</th>
                  <th>Margem</th>
                  <th>Estoque</th>
                  <th>Alerta</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {produtos.map((p) => (
                  <tr key={p.id}>
                    <td>
                      <span className={styles.badgeSku}>{p.codigoProduto}</span>
                    </td>
                    <td>
                      <strong>{p.nomeProduto}</strong>
                    </td>
                    <td>
                      <span style={{ color: 'var(--text-muted)' }}>
                        {p.categoria || 'Geral'} {p.marca ? `• ${p.marca}` : ''}
                      </span>
                    </td>
                    <td>{formatarMoeda(p.precoCusto)}</td>
                    <td>
                      <strong>{formatarMoeda(p.precoVenda)}</strong>
                    </td>
                    <td>
                      <span className={styles.badgeMargin}>
                        +{p.margemLucro ?? 0}%
                      </span>
                    </td>
                    <td>
                      <span>
                        {p.estoqueAtual ?? 0} {p.unidadeMedida || 'UN'}
                      </span>
                    </td>
                    <td>
                      {p.alertaEstoque === 'CRITICO' && (
                        <span className={styles.badgeAlertCritico}>⚠️ Baixo</span>
                      )}
                      {p.alertaEstoque === 'EXCESSIVO' && (
                        <span className={styles.badgeAlertExcessivo}>📦 Excesso</span>
                      )}
                      {p.alertaEstoque === 'NORMAL' && (
                        <span className={styles.badgeAlertNormal}>✓ Normal</span>
                      )}
                    </td>
                    <td>
                      {p.statusProduto === 'ATIVO' ? (
                        <span className={styles.badgeStatusAtivo}>● Ativo</span>
                      ) : (
                        <span className={styles.badgeStatusInativo}>○ Inativo</span>
                      )}
                    </td>
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

                {/* Seção 1: Identificação */}
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
                        placeholder="Ex: Caneca Porcelana Branca"
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
                        onChange={(e) => setFormData({ ...formData, tipoProduto: e.target.value as ProdutoTipo })}
                      >
                        <option value="PRODUTO">Produto (Revenda / Acabado)</option>
                        <option value="INSUMO">Insumo (Matéria-prima)</option>
                        <option value="SERVICO">Serviço</option>
                      </select>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formUnidadeId}>Unidade de Medida</label>
                      <input
                        id={formUnidadeId}
                        type="text"
                        list="unidades-list"
                        placeholder="Ex: UN, M2, KG, CX"
                        value={formData.unidadeMedida || ''}
                        onChange={(e) => setFormData({ ...formData, unidadeMedida: e.target.value })}
                      />
                      <datalist id="unidades-list">
                        {catalogo?.unidadesMedida.map((u) => (
                          <option key={u.id} value={u.sigla} label={u.descricao} />
                        ))}
                      </datalist>
                    </div>
                  </div>

                  <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formCategoriaId}>Categoria</label>
                      <input
                        id={formCategoriaId}
                        type="text"
                        list="categorias-list"
                        placeholder="Ex: Vidros, Chapas, Ferragens"
                        value={formData.categoria || ''}
                        onChange={(e) => setFormData({ ...formData, categoria: e.target.value })}
                      />
                      <datalist id="categorias-list">
                        {catalogo?.categorias.map((c) => (
                          <option key={c.id} value={c.descricao} />
                        ))}
                      </datalist>
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formMarcaId}>Marca</label>
                      <input
                        id={formMarcaId}
                        type="text"
                        list="marcas-list"
                        placeholder="Ex: Vivix, Cebrace, Blindex"
                        value={formData.marca || ''}
                        onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
                      />
                      <datalist id="marcas-list">
                        {catalogo?.marcas.map((m) => (
                          <option key={m.id} value={m.descricao} />
                        ))}
                      </datalist>
                    </div>
                  </div>
                </div>

                {/* Seção 2: Preços e Margem */}
                <div>
                  <h3 className={styles.formSectionTitle}>2. Precificação & Lucro Comercial</h3>
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

                {/* Seção 3: Estoque */}
                <div>
                  <h3 className={styles.formSectionTitle}>3. Parâmetros de Estoque</h3>
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
