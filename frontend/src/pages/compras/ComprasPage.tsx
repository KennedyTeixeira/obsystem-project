import React, { useEffect, useState, useId } from 'react';
import {
  ShoppingBag,
  Plus,
  Search,
  X,
  Trash2,
  CheckCircle,
  Eye,
  AlertCircle,
  Loader2,
  PackageCheck,
  Send,
  ArrowRight,
  TrendingUp,
} from 'lucide-react';
import compraService from '../../services/compraService';
import fornecedorService from '../../services/fornecedorService';
import produtoService from '../../services/produtoService';
import type { CompraDTO, CompraStatus, ItemCompraDTO } from '../../types/compra';
import type { FornecedorDTO } from '../../types/pessoa';
import type { ProdutoDTO } from '../../types/produto';
import styles from './ComprasPage.module.css';

export const ComprasPage: React.FC = () => {
  const [compras, setCompras] = useState<CompraDTO[]>([]);
  const [fornecedores, setFornecedores] = useState<FornecedorDTO[]>([]);
  const [produtos, setProdutos] = useState<ProdutoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<CompraStatus | ''>('');
  const [actionLoadingId, setActionLoadingId] = useState<number | null>(null);

  // Modal Nova Compra / Cotação
  const [modalNovoOpen, setModalNovoOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Form State Nova Compra
  const [fornecedorId, setFornecedorId] = useState<number | ''>('');
  const [emissao, setEmissao] = useState(new Date().toISOString().split('T')[0]);
  const [previsao, setPrevisao] = useState(new Date().toISOString().split('T')[0]);
  const [statusCompra, setStatusCompra] = useState<CompraStatus>('COTACAO');
  const [gerarTituloFinanceiro, setGerarTituloFinanceiro] = useState(true);
  const [itens, setItens] = useState<ItemCompraDTO[]>([]);

  // Item Form State
  const [selectedProdutoId, setSelectedProdutoId] = useState<number | ''>('');
  const [quantidadeItem, setQuantidadeItem] = useState<number>(1);
  const [precoUnitario, setPrecoUnitario] = useState<number>(0);
  const [valorDesconto, setValorDesconto] = useState<number>(0);
  const [valorDespesa, setValorDespesa] = useState<number>(0);
  const [itemError, setItemError] = useState<string | null>(null);

  // Modal Detalhes da Compra
  const [modalDetalhesOpen, setModalDetalhesOpen] = useState(false);
  const [compraDetalhes, setCompraDetalhes] = useState<CompraDTO | null>(null);

  const searchInputId = useId();
  const statusFilterId = useId();
  const formFornecedorId = useId();
  const formEmissaoId = useId();
  const formPrevisaoId = useId();
  const formStatusId = useId();
  const formItemProdutoId = useId();
  const formItemQtdId = useId();
  const formItemPrecoId = useId();
  const formItemDescId = useId();
  const formItemDespId = useId();

  const carregarCompras = async () => {
    try {
      setLoading(true);
      const data = await compraService.listar(
        search,
        statusFilter ? statusFilter : undefined
      );
      setCompras(data);
    } catch (err) {
      console.error('Erro ao carregar compras:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarApoio = async () => {
    try {
      const [listaFornecedores, listaProdutos] = await Promise.all([
        fornecedorService.listar('', 'ATIVO'),
        produtoService.listar('', 'ATIVO'),
      ]);
      setFornecedores(listaFornecedores);
      setProdutos(listaProdutos);
    } catch (err) {
      console.error('Erro ao carregar fornecedores e produtos:', err);
    }
  };

  useEffect(() => {
    carregarCompras();
  }, [search, statusFilter]);

  useEffect(() => {
    carregarApoio();
  }, []);

  const formatCurrency = (val?: number) => {
    return (val || 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return '-';
    const parts = dateStr.split('-');
    if (parts.length === 3) {
      return `${parts[2]}/${parts[1]}/${parts[0]}`;
    }
    return dateStr;
  };

  const abrirModalNovo = () => {
    setFornecedorId(fornecedores.length > 0 ? (fornecedores[0].id || '') : '');
    setEmissao(new Date().toISOString().split('T')[0]);
    setPrevisao(new Date().toISOString().split('T')[0]);
    setStatusCompra('COTACAO');
    setGerarTituloFinanceiro(true);
    setItens([]);
    resetItemInputs();
    setErrorMessage(null);
    setItemError(null);
    setModalNovoOpen(true);
  };

  const resetItemInputs = () => {
    setSelectedProdutoId('');
    setQuantidadeItem(1);
    setPrecoUnitario(0);
    setValorDesconto(0);
    setValorDespesa(0);
    setItemError(null);
  };

  const handleProdutoSelectChange = (prodId: number | '') => {
    setSelectedProdutoId(prodId);
    setItemError(null);
    if (!prodId) {
      setPrecoUnitario(0);
      return;
    }
    const prod = produtos.find((p) => p.id === prodId);
    if (prod) {
      setPrecoUnitario(prod.precoCusto || 0);
    }
  };

  const handleAdicionarItem = () => {
    if (!selectedProdutoId) {
      setItemError('Selecione um produto/insumo para adicionar.');
      return;
    }
    if (quantidadeItem <= 0) {
      setItemError('A quantidade deve ser maior que zero.');
      return;
    }

    const prod = produtos.find((p) => p.id === selectedProdutoId);
    if (!prod) {
      setItemError('Produto não encontrado.');
      return;
    }

    const subtotal =
      quantidadeItem * precoUnitario - valorDesconto + valorDespesa;

    const novoItem: ItemCompraDTO = {
      idProduto: prod.id!,
      nomeProduto: prod.nomeProduto,
      codigoProduto: prod.codigoProduto,
      unidadeMedida: prod.unidadeMedida || 'UN',
      numeroItem: itens.length + 1,
      quantidadeItem: Number(quantidadeItem),
      precoUnitario: Number(precoUnitario),
      valorDesconto: Number(valorDesconto) || 0,
      valorDespesa: Number(valorDespesa) || 0,
      valorTotal: subtotal > 0 ? subtotal : 0,
    };

    setItens([...itens, novoItem]);
    resetItemInputs();
  };

  const handleRemoverItem = (index: number) => {
    setItens(itens.filter((_, idx) => idx !== index));
  };

  // Totais da compra atual
  const subtotalBruto = itens.reduce(
    (acc, it) => acc + it.quantidadeItem * (it.precoUnitario || 0),
    0
  );
  const totalDescontos = itens.reduce((acc, it) => acc + (it.valorDesconto || 0), 0);
  const totalDespesas = itens.reduce((acc, it) => acc + (it.valorDespesa || 0), 0);
  const totalLiquido = subtotalBruto - totalDescontos + totalDespesas;

  const handleSalvarCompra = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!fornecedorId) {
      setErrorMessage('Por favor, selecione um fornecedor para a compra.');
      return;
    }
    if (itens.length === 0) {
      setErrorMessage('A compra/cotação precisa ter pelo menos um item.');
      return;
    }

    try {
      setSaving(true);
      setErrorMessage(null);

      const payload: CompraDTO = {
        idFornecedor: Number(fornecedorId),
        emissao,
        previsao,
        statusCompra,
        gerarTituloFinanceiro,
        itens: itens.map((it, idx) => ({
          ...it,
          numeroItem: idx + 1,
        })),
      };

      await compraService.criar(payload);
      setModalNovoOpen(false);
      await carregarCompras();
      await carregarApoio();
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Erro ao registrar compra.';
      setErrorMessage(msg);
    } finally {
      setSaving(false);
    }
  };

  const handleAlterarStatus = async (id: number, novoStatus: CompraStatus) => {
    try {
      setActionLoadingId(id);
      await compraService.alterarStatus(id, novoStatus);
      await carregarCompras();
      await carregarApoio();
    } catch (err: any) {
      alert(
        err?.response?.data?.message ||
          err?.message ||
          'Erro ao atualizar status da compra.'
      );
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleVerDetalhes = (compra: CompraDTO) => {
    setCompraDetalhes(compra);
    setModalDetalhesOpen(true);
  };

  const renderStatusBadge = (status: CompraStatus) => {
    switch (status) {
      case 'REQUISICAO':
        return <span className={styles.statusRequisicao}>Requisição</span>;
      case 'COTACAO':
        return <span className={styles.statusCotacao}>Cotação</span>;
      case 'NEGOCIACAO':
        return <span className={styles.statusNegociacao}>Negociação</span>;
      case 'PEDIDO':
        return <span className={styles.statusPedido}>Pedido Emitido</span>;
      case 'RECEBIMENTO':
        return <span className={styles.statusRecebimento}>Recebido (Estoque)</span>;
      case 'PAGAMENTO':
        return <span className={styles.statusPagamento}>Faturado / Pago</span>;
      default:
        return <span>{status}</span>;
    }
  };

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <h1>Compras & Suprimentos</h1>
          <p>Cotações de fornecedores, pedidos de compra e reposição de estoque</p>
        </div>
        <button
          type="button"
          className={styles.btnPrimary}
          onClick={abrirModalNovo}
        >
          <Plus size={18} /> Nova Compra / Cotação
        </button>
      </div>

      {/* Filter Card */}
      <div className={styles.filterCard}>
        <div className={styles.searchBox}>
          <Search size={18} color="var(--text-subtle)" />
          <input
            id={searchInputId}
            type="text"
            placeholder="Buscar por fornecedor ou documento..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className={styles.statusFilter}>
          <label htmlFor={statusFilterId}>Status:</label>
          <select
            id={statusFilterId}
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as CompraStatus | '')}
          >
            <option value="">Todos os status</option>
            <option value="REQUISICAO">Requisição</option>
            <option value="COTACAO">Cotação</option>
            <option value="NEGOCIACAO">Negociação</option>
            <option value="PEDIDO">Pedido</option>
            <option value="RECEBIMENTO">Recebimento (Estoque)</option>
            <option value="PAGAMENTO">Faturado / Pago</option>
          </select>
        </div>
      </div>

      {/* Purchases Table */}
      <div className={styles.tableCard}>
        {loading ? (
          <div className={styles.emptyState}>
            <Loader2 className="animate-spin" size={32} color="var(--color-primary)" />
            <p>Carregando registros de compras e suprimentos...</p>
          </div>
        ) : compras.length === 0 ? (
          <div className={styles.emptyState}>
            <ShoppingBag size={48} color="var(--text-subtle)" />
            <h3>Nenhuma compra ou cotação encontrada</h3>
            <p>Clique no botão acima para cadastrar a primeira cotação de suprimentos.</p>
          </div>
        ) : (
          <div className={styles.tableResponsive}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Compra</th>
                  <th>Fornecedor</th>
                  <th>Emissão</th>
                  <th>Previsão</th>
                  <th>Qtd Itens</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th style={{ textAlign: 'right' }}>Ações</th>
                </tr>
              </thead>
              <tbody>
                {compras.map((c) => {
                  const isActionLoading = actionLoadingId === c.id;
                  return (
                    <tr key={c.id}>
                      <td>
                        <span className={styles.badgeCompraId}>
                          #{String(c.id).padStart(5, '0')}
                        </span>
                      </td>
                      <td>
                        <div style={{ fontWeight: 600 }}>{c.nomeFornecedor}</div>
                        {c.documentoFornecedor && (
                          <div
                            style={{
                              fontSize: '0.78rem',
                              color: 'var(--text-muted)',
                            }}
                          >
                            {c.documentoFornecedor}
                          </div>
                        )}
                      </td>
                      <td>{formatDate(c.emissao)}</td>
                      <td>{formatDate(c.previsao)}</td>
                      <td>
                        <span
                          style={{
                            fontWeight: 600,
                            backgroundColor: 'var(--bg-subtle)',
                            padding: '0.2rem 0.5rem',
                            borderRadius: 'var(--radius-sm)',
                          }}
                        >
                          {c.itens?.length || 0}
                        </span>
                      </td>
                      <td style={{ fontWeight: 700, color: 'var(--text-main)' }}>
                        {formatCurrency(c.totalCompra)}
                      </td>
                      <td>{renderStatusBadge(c.statusCompra)}</td>
                      <td>
                        <div
                          className={styles.actionsCell}
                          style={{ justifyContent: 'flex-end' }}
                        >
                          {/* Botão Ver Detalhes */}
                          <button
                            type="button"
                            className={styles.actionBtnOutline}
                            title="Ver Itens e Detalhes da Compra"
                            onClick={() => handleVerDetalhes(c)}
                          >
                            <Eye size={14} /> Detalhes
                          </button>

                          {/* Pipeline Actions */}
                          {c.statusCompra === 'REQUISICAO' && (
                            <button
                              type="button"
                              className={styles.actionBtnInfo}
                              disabled={isActionLoading}
                              title="Iniciar Cotação com Fornecedores"
                              onClick={() => handleAlterarStatus(c.id!, 'COTACAO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <Send size={14} />
                              )}
                              Cotar
                            </button>
                          )}

                          {c.statusCompra === 'COTACAO' && (
                            <button
                              type="button"
                              className={styles.actionBtnWarning}
                              disabled={isActionLoading}
                              title="Avançar para Negociação de Preços"
                              onClick={() => handleAlterarStatus(c.id!, 'NEGOCIACAO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <TrendingUp size={14} />
                              )}
                              Negociar
                            </button>
                          )}

                          {c.statusCompra === 'NEGOCIACAO' && (
                            <button
                              type="button"
                              className={styles.actionBtnInfo}
                              disabled={isActionLoading}
                              title="Aprovar e Emitir Pedido de Compra"
                              onClick={() => handleAlterarStatus(c.id!, 'PEDIDO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <ArrowRight size={14} />
                              )}
                              Emitir Pedido
                            </button>
                          )}

                          {c.statusCompra === 'PEDIDO' && (
                            <button
                              type="button"
                              className={styles.actionBtnWarning}
                              disabled={isActionLoading}
                              title="Confirmar Recebimento e Dar Entrada no Estoque"
                              onClick={() => handleAlterarStatus(c.id!, 'RECEBIMENTO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <PackageCheck size={14} />
                              )}
                              Receber Estoque
                            </button>
                          )}

                          {c.statusCompra === 'RECEBIMENTO' && (
                            <button
                              type="button"
                              className={styles.actionBtnSuccess}
                              disabled={isActionLoading}
                              title="Faturar e Gerar Contas a Pagar"
                              onClick={() => handleAlterarStatus(c.id!, 'PAGAMENTO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <CheckCircle size={14} />
                              )}
                              Faturar / Pagar
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* MODAL NOVA COMPRA / COTAÇÃO */}
      {modalNovoOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <ShoppingBag size={22} color="var(--color-primary)" />
                <h2>Nova Cotação / Pedido de Compra</h2>
              </div>
              <button
                type="button"
                className={styles.closeBtn}
                onClick={() => setModalNovoOpen(false)}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSalvarCompra} className={styles.modalBody}>
              {errorMessage && (
                <div className={styles.alertBox}>
                  <AlertCircle size={18} />
                  <span>{errorMessage}</span>
                </div>
              )}

              {/* Seção Dados Principais */}
              <div>
                <h4 className={styles.formSectionTitle}>1. Dados do Suprimento</h4>
                <div className={styles.formGrid3}>
                  <div className={styles.formGroup} style={{ gridColumn: 'span 2' }}>
                    <label htmlFor={formFornecedorId}>Fornecedor Ativo *</label>
                    <select
                      id={formFornecedorId}
                      value={fornecedorId}
                      onChange={(e) => setFornecedorId(Number(e.target.value))}
                      required
                    >
                      <option value="">Selecione um fornecedor...</option>
                      {fornecedores.map((f) => (
                        <option key={f.id} value={f.id}>
                          {f.nome} {f.cpf ? `(CPF: ${f.cpf})` : f.cnpj ? `(CNPJ: ${f.cnpj})` : ''}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formStatusId}>Status Inicial *</label>
                    <select
                      id={formStatusId}
                      value={statusCompra}
                      onChange={(e) => setStatusCompra(e.target.value as CompraStatus)}
                      required
                    >
                      <option value="COTACAO">Cotação (Pesquisa de Preços)</option>
                      <option value="REQUISICAO">Requisição Interna</option>
                      <option value="NEGOCIACAO">Negociação</option>
                      <option value="PEDIDO">Pedido Formal</option>
                      <option value="RECEBIMENTO">Recebimento (Entrada de Estoque)</option>
                      <option value="PAGAMENTO">Faturado (Estoque + Contas a Pagar)</option>
                    </select>
                  </div>
                </div>

                <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                  <div className={styles.formGroup}>
                    <label htmlFor={formEmissaoId}>Data de Emissão *</label>
                    <input
                      id={formEmissaoId}
                      type="date"
                      value={emissao}
                      onChange={(e) => setEmissao(e.target.value)}
                      required
                    />
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formPrevisaoId}>Previsão de Entrega *</label>
                    <input
                      id={formPrevisaoId}
                      type="date"
                      value={previsao}
                      onChange={(e) => setPrevisao(e.target.value)}
                      required
                    />
                  </div>
                </div>
              </div>

              {/* Seção Adicionar Itens / Insumos */}
              <div>
                <h4 className={styles.formSectionTitle}>2. Mercadorias & Insumos</h4>
                <div className={styles.addItemCard}>
                  {itemError && (
                    <div
                      style={{
                        backgroundColor: '#fee2e2',
                        color: '#991b1b',
                        padding: '0.5rem 0.75rem',
                        borderRadius: 'var(--radius-sm)',
                        fontSize: '0.82rem',
                        fontWeight: 600,
                        display: 'flex',
                        alignItems: 'center',
                        gap: '0.5rem',
                      }}
                    >
                      <AlertCircle size={16} />
                      {itemError}
                    </div>
                  )}

                  <div className={styles.formGroup}>
                    <label htmlFor={formItemProdutoId}>Produto / Matéria-Prima *</label>
                    <select
                      id={formItemProdutoId}
                      value={selectedProdutoId}
                      onChange={(e) =>
                        handleProdutoSelectChange(
                          e.target.value ? Number(e.target.value) : ''
                        )
                      }
                    >
                      <option value="">Selecione o produto ou insumo...</option>
                      {produtos.map((p) => (
                        <option key={p.id} value={p.id}>
                          {p.codigoProduto} - {p.nomeProduto} | Custo Atual:{' '}
                          {formatCurrency(p.precoCusto)} | Estoque:{' '}
                          {p.estoqueAtual} {p.unidadeMedida || 'UN'}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className={styles.formGrid3}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formItemQtdId}>Quantidade</label>
                      <input
                        id={formItemQtdId}
                        type="number"
                        min="0.01"
                        step="any"
                        value={quantidadeItem}
                        onChange={(e) => setQuantidadeItem(Number(e.target.value))}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor={formItemPrecoId}>Custo Unit. (R$)</label>
                      <input
                        id={formItemPrecoId}
                        type="number"
                        min="0.01"
                        step="0.01"
                        value={precoUnitario}
                        onChange={(e) => setPrecoUnitario(Number(e.target.value))}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor={formItemDescId}>Desconto (R$)</label>
                      <input
                        id={formItemDescId}
                        type="number"
                        min="0"
                        step="0.01"
                        value={valorDesconto}
                        onChange={(e) => setValorDesconto(Number(e.target.value))}
                      />
                    </div>

                    <div className={styles.formGroup}>
                      <label htmlFor={formItemDespId}>Frete / Despesa (R$)</label>
                      <input
                        id={formItemDespId}
                        type="number"
                        min="0"
                        step="0.01"
                        value={valorDespesa}
                        onChange={(e) => setValorDespesa(Number(e.target.value))}
                      />
                    </div>

                    <div
                      style={{
                        gridColumn: 'span 2',
                        display: 'flex',
                        alignItems: 'flex-end',
                        justifyContent: 'flex-end',
                        gap: '1rem',
                      }}
                    >
                      <div
                        style={{
                          fontSize: '0.88rem',
                          color: 'var(--text-muted)',
                          alignSelf: 'center',
                        }}
                      >
                        Subtotal Item:{' '}
                        <strong style={{ color: 'var(--color-primary)' }}>
                          {formatCurrency(
                            quantidadeItem * precoUnitario -
                              valorDesconto +
                              valorDespesa
                          )}
                        </strong>
                      </div>

                      <button
                        type="button"
                        className={styles.btnPrimary}
                        style={{ padding: '0.55rem 1rem', fontSize: '0.85rem' }}
                        onClick={handleAdicionarItem}
                      >
                        <Plus size={16} /> Adicionar Item
                      </button>
                    </div>
                  </div>
                </div>

                {/* Tabela de Itens Adicionados */}
                {itens.length > 0 ? (
                  <table className={styles.itemsTable}>
                    <thead>
                      <tr>
                        <th style={{ width: '40px' }}>#</th>
                        <th>Código</th>
                        <th>Produto</th>
                        <th>Qtd</th>
                        <th>Custo Unit.</th>
                        <th>Desc.</th>
                        <th>Desp./Frete</th>
                        <th>Total</th>
                        <th style={{ width: '40px' }}></th>
                      </tr>
                    </thead>
                    <tbody>
                      {itens.map((it, idx) => (
                        <tr key={idx}>
                          <td style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                            {idx + 1}
                          </td>
                          <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>
                            {it.codigoProduto}
                          </td>
                          <td>{it.nomeProduto}</td>
                          <td style={{ fontWeight: 600 }}>
                            {it.quantidadeItem} {it.unidadeMedida}
                          </td>
                          <td>{formatCurrency(it.precoUnitario)}</td>
                          <td style={{ color: it.valorDesconto ? 'var(--color-danger)' : 'inherit' }}>
                            {it.valorDesconto ? `- ${formatCurrency(it.valorDesconto)}` : '-'}
                          </td>
                          <td style={{ color: it.valorDespesa ? 'var(--color-primary)' : 'inherit' }}>
                            {it.valorDespesa ? `+ ${formatCurrency(it.valorDespesa)}` : '-'}
                          </td>
                          <td style={{ fontWeight: 700 }}>
                            {formatCurrency(it.valorTotal)}
                          </td>
                          <td>
                            <button
                              type="button"
                              className={styles.btnRemoveItem}
                              title="Remover Item"
                              onClick={() => handleRemoverItem(idx)}
                            >
                              <Trash2 size={16} />
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                ) : (
                  <div
                    style={{
                      textAlign: 'center',
                      padding: '1.5rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.88rem',
                      fontStyle: 'italic',
                    }}
                  >
                    Nenhum item adicionado à cotação/compra ainda.
                  </div>
                )}
              </div>

              {/* Seção Totais e Contas a Pagar */}
              <div>
                <h4 className={styles.formSectionTitle}>3. Totalização & Contas a Pagar</h4>
                <div className={styles.totalsBox}>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem' }}>
                    <div style={{ fontSize: '0.88rem', color: 'var(--text-muted)' }}>
                      Total Bruto: <strong>{formatCurrency(subtotalBruto)}</strong>
                      {totalDescontos > 0 && ` | Descontos: - ${formatCurrency(totalDescontos)}`}
                      {totalDespesas > 0 && ` | Despesas: + ${formatCurrency(totalDespesas)}`}
                    </div>
                    <label
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '0.5rem',
                        fontSize: '0.85rem',
                        cursor: 'pointer',
                        marginTop: '0.25rem',
                      }}
                    >
                      <input
                        type="checkbox"
                        checked={gerarTituloFinanceiro}
                        onChange={(e) => setGerarTituloFinanceiro(e.target.checked)}
                      />
                      Gerar Título de Contas a Pagar no Financeiro ao Finalizar
                    </label>
                  </div>

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 600 }}>
                      VALOR TOTAL DA COMPRA
                    </div>
                    <div className={styles.totalValueHighlight}>
                      {formatCurrency(totalLiquido)}
                    </div>
                  </div>
                </div>
              </div>

              {/* Footer */}
              <div className={styles.modalFooter}>
                <button
                  type="button"
                  className={styles.btnSecondary}
                  onClick={() => setModalNovoOpen(false)}
                  disabled={saving}
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className={styles.btnPrimary}
                  disabled={saving}
                >
                  {saving ? (
                    <>
                      <Loader2 size={16} className="animate-spin" /> Registrando...
                    </>
                  ) : (
                    <>
                      <CheckCircle size={16} /> Salvar Compra
                    </>
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL DETALHES DA COMPRA */}
      {modalDetalhesOpen && compraDetalhes && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent} style={{ maxWidth: '850px' }}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <Eye size={22} color="var(--color-primary)" />
                <h2>Compra #{String(compraDetalhes.id).padStart(5, '0')}</h2>
              </div>
              <button
                type="button"
                className={styles.closeBtn}
                onClick={() => setModalDetalhesOpen(false)}
              >
                <X size={20} />
              </button>
            </div>

            <div className={styles.modalBody}>
              {/* Resumo da Compra */}
              <div className={styles.detailGrid}>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Fornecedor</span>
                  <span className={styles.detailValue}>
                    {compraDetalhes.nomeFornecedor}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Documento</span>
                  <span className={styles.detailValue}>
                    {compraDetalhes.documentoFornecedor || 'Não informado'}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Data de Emissão</span>
                  <span className={styles.detailValue}>
                    {formatDate(compraDetalhes.emissao)}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Previsão de Entrega</span>
                  <span className={styles.detailValue}>
                    {formatDate(compraDetalhes.previsao)}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Status Atual</span>
                  <div>{renderStatusBadge(compraDetalhes.statusCompra)}</div>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Valor Total</span>
                  <span
                    className={styles.detailValue}
                    style={{ color: 'var(--color-primary)', fontSize: '1.1rem' }}
                  >
                    {formatCurrency(compraDetalhes.totalCompra)}
                  </span>
                </div>
              </div>

              {/* Tabela de Itens da Compra */}
              <div>
                <h4 className={styles.formSectionTitle}>Itens e Insumos Comprados</h4>
                <table className={styles.itemsTable}>
                  <thead>
                    <tr>
                      <th style={{ width: '40px' }}>#</th>
                      <th>Código</th>
                      <th>Produto</th>
                      <th>Quantidade</th>
                      <th>Custo Unit.</th>
                      <th>Desconto</th>
                      <th>Despesas/Frete</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    {compraDetalhes.itens && compraDetalhes.itens.length > 0 ? (
                      compraDetalhes.itens.map((it, idx) => (
                        <tr key={it.id || idx}>
                          <td style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                            {it.numeroItem || idx + 1}
                          </td>
                          <td style={{ fontFamily: 'monospace', fontWeight: 600 }}>
                            {it.codigoProduto}
                          </td>
                          <td>{it.nomeProduto}</td>
                          <td style={{ fontWeight: 600 }}>
                            {it.quantidadeItem} {it.unidadeMedida || 'UN'}
                          </td>
                          <td>{formatCurrency(it.precoUnitario)}</td>
                          <td style={{ color: it.valorDesconto ? 'var(--color-danger)' : 'inherit' }}>
                            {it.valorDesconto ? `- ${formatCurrency(it.valorDesconto)}` : '-'}
                          </td>
                          <td style={{ color: it.valorDespesa ? 'var(--color-primary)' : 'inherit' }}>
                            {it.valorDespesa ? `+ ${formatCurrency(it.valorDespesa)}` : '-'}
                          </td>
                          <td style={{ fontWeight: 700 }}>
                            {formatCurrency(it.valorTotal)}
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan={8} style={{ textAlign: 'center', padding: '1rem' }}>
                          Nenhum item vinculado.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>

            <div className={styles.modalFooter}>
              <button
                type="button"
                className={styles.btnSecondary}
                onClick={() => setModalDetalhesOpen(false)}
              >
                Fechar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ComprasPage;
