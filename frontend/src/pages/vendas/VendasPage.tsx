import React, { useEffect, useState, useId } from 'react';
import {
  ShoppingCart,
  Plus,
  Search,
  X,
  Trash2,
  CheckCircle,
  Eye,
  AlertCircle,
  Loader2,
  Package,
} from 'lucide-react';
import vendaService from '../../services/vendaService';
import clienteService from '../../services/clienteService';
import produtoService from '../../services/produtoService';
import type { VendaDTO, VendaStatus, ItemVendaDTO } from '../../types/venda';
import type { ClienteDTO } from '../../types/pessoa';
import type { ProdutoDTO } from '../../types/produto';
import styles from './VendasPage.module.css';

export const VendasPage: React.FC = () => {
  const [vendas, setVendas] = useState<VendaDTO[]>([]);
  const [clientes, setClientes] = useState<ClienteDTO[]>([]);
  const [produtos, setProdutos] = useState<ProdutoDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<VendaStatus | ''>('');
  const [actionLoadingId, setActionLoadingId] = useState<number | null>(null);

  // Modal Novo Pedido
  const [modalNovoOpen, setModalNovoOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Form State Novo Pedido
  const [clienteId, setClienteId] = useState<number | ''>('');
  const [emissao, setEmissao] = useState(new Date().toISOString().split('T')[0]);
  const [previsao, setPrevisao] = useState(new Date().toISOString().split('T')[0]);
  const [statusVenda, setStatusVenda] = useState<VendaStatus>('ABERTO');
  const [gerarTituloFinanceiro, setGerarTituloFinanceiro] = useState(true);
  const [itens, setItens] = useState<ItemVendaDTO[]>([]);

  // Item Form State
  const [selectedProdutoId, setSelectedProdutoId] = useState<number | ''>('');
  const [quantidadeItem, setQuantidadeItem] = useState<number>(1);
  const [precoUnitario, setPrecoUnitario] = useState<number>(0);
  const [valorDesconto, setValorDesconto] = useState<number>(0);
  const [valorAcrescimo, setValorAcrescimo] = useState<number>(0);
  const [itemError, setItemError] = useState<string | null>(null);

  // Modal Detalhes do Pedido
  const [modalDetalhesOpen, setModalDetalhesOpen] = useState(false);
  const [vendaDetalhes, setVendaDetalhes] = useState<VendaDTO | null>(null);

  const searchInputId = useId();
  const statusFilterId = useId();
  const formClienteId = useId();
  const formEmissaoId = useId();
  const formPrevisaoId = useId();
  const formStatusId = useId();
  const formItemProdutoId = useId();
  const formItemQtdId = useId();
  const formItemPrecoId = useId();
  const formItemDescId = useId();
  const formItemAcrescId = useId();

  const carregarVendas = async () => {
    try {
      setLoading(true);
      const data = await vendaService.listar(
        search,
        statusFilter ? statusFilter : undefined
      );
      setVendas(data);
    } catch (err) {
      console.error('Erro ao carregar vendas:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarApoio = async () => {
    try {
      const [listaClientes, listaProdutos] = await Promise.all([
        clienteService.listar('', 'ATIVO'),
        produtoService.listar('', 'ATIVO'),
      ]);
      setClientes(listaClientes);
      setProdutos(listaProdutos);
    } catch (err) {
      console.error('Erro ao carregar dados de clientes e produtos:', err);
    }
  };

  useEffect(() => {
    carregarVendas();
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
    setClienteId(clientes.length > 0 ? (clientes[0].id || '') : '');
    setEmissao(new Date().toISOString().split('T')[0]);
    setPrevisao(new Date().toISOString().split('T')[0]);
    setStatusVenda('ABERTO');
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
    setValorAcrescimo(0);
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
      setPrecoUnitario(prod.precoVenda || 0);
    }
  };

  const handleAdicionarItem = () => {
    if (!selectedProdutoId) {
      setItemError('Selecione um produto para adicionar.');
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

    // Validação de estoque em tela para conforto do usuário
    if (statusVenda !== 'ORCAMENTO' && prod.controlaEstoque === 'SIM') {
      const disp = prod.estoqueDisponivel ?? prod.estoqueAtual;
      if (quantidadeItem > disp) {
        setItemError(
          `Estoque insuficiente para ${prod.nomeProduto}. Disponível: ${disp} ${prod.unidadeMedida || 'UN'}`
        );
        return;
      }
    }

    const subtotal =
      quantidadeItem * precoUnitario - valorDesconto + valorAcrescimo;

    const novoItem: ItemVendaDTO = {
      idProduto: prod.id!,
      nomeProduto: prod.nomeProduto,
      codigoProduto: prod.codigoProduto,
      unidadeMedida: prod.unidadeMedida || 'UN',
      numeroItem: itens.length + 1,
      quantidadeItem: Number(quantidadeItem),
      precoUnitario: Number(precoUnitario),
      valorDesconto: Number(valorDesconto) || 0,
      valorAcrescimo: Number(valorAcrescimo) || 0,
      valorTotal: subtotal > 0 ? subtotal : 0,
    };

    setItens([...itens, novoItem]);
    resetItemInputs();
  };

  const handleRemoverItem = (index: number) => {
    setItens(itens.filter((_, idx) => idx !== index));
  };

  // Totais do pedido atual
  const subtotalBruto = itens.reduce(
    (acc, it) => acc + it.quantidadeItem * (it.precoUnitario || 0),
    0
  );
  const totalDescontos = itens.reduce((acc, it) => acc + (it.valorDesconto || 0), 0);
  const totalAcrescimos = itens.reduce((acc, it) => acc + (it.valorAcrescimo || 0), 0);
  const totalLiquido = subtotalBruto - totalDescontos + totalAcrescimos;

  const handleSalvarVenda = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!clienteId) {
      setErrorMessage('Por favor, selecione um cliente para a venda.');
      return;
    }
    if (itens.length === 0) {
      setErrorMessage('O pedido precisa ter pelo menos um item.');
      return;
    }

    try {
      setSaving(true);
      setErrorMessage(null);

      const payload: VendaDTO = {
        idCliente: Number(clienteId),
        emissao,
        previsao,
        statusVenda,
        gerarTituloFinanceiro,
        itens: itens.map((it, idx) => ({
          ...it,
          numeroItem: idx + 1,
        })),
      };

      await vendaService.criar(payload);
      setModalNovoOpen(false);
      await carregarVendas();
      await carregarApoio(); // atualiza saldos de estoque disponíveis
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Erro ao registrar venda.';
      setErrorMessage(msg);
    } finally {
      setSaving(false);
    }
  };

  const handleAlterarStatus = async (id: number, novoStatus: VendaStatus) => {
    try {
      setActionLoadingId(id);
      await vendaService.alterarStatus(id, novoStatus);
      await carregarVendas();
      await carregarApoio();
    } catch (err: any) {
      alert(
        err?.response?.data?.message ||
          err?.message ||
          'Erro ao atualizar status do pedido.'
      );
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleVerDetalhes = (venda: VendaDTO) => {
    setVendaDetalhes(venda);
    setModalDetalhesOpen(true);
  };

  const renderStatusBadge = (status: VendaStatus) => {
    switch (status) {
      case 'ORCAMENTO':
        return <span className={styles.statusOrcamento}>Orçamento</span>;
      case 'ABERTO':
        return <span className={styles.statusAberto}>Aberto</span>;
      case 'SEPARACAO':
        return <span className={styles.statusSeparacao}>Em Separação</span>;
      case 'FINALIZADO':
        return <span className={styles.statusFinalizado}>Finalizado</span>;
      default:
        return <span>{status}</span>;
    }
  };

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <h1>Vendas & Pedidos</h1>
          <p>Gestão comercial, reservas de estoque e faturamento</p>
        </div>
        <button
          type="button"
          className={styles.btnPrimary}
          onClick={abrirModalNovo}
        >
          <Plus size={18} /> Novo Pedido de Venda
        </button>
      </div>

      {/* Filter Card */}
      <div className={styles.filterCard}>
        <div className={styles.searchBox}>
          <Search size={18} color="var(--text-subtle)" />
          <input
            id={searchInputId}
            type="text"
            placeholder="Buscar por cliente ou documento..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className={styles.statusFilter}>
          <label htmlFor={statusFilterId}>Status:</label>
          <select
            id={statusFilterId}
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value as VendaStatus | '')}
          >
            <option value="">Todos os status</option>
            <option value="ORCAMENTO">Orçamento</option>
            <option value="ABERTO">Aberto</option>
            <option value="SEPARACAO">Em Separação</option>
            <option value="FINALIZADO">Finalizado</option>
          </select>
        </div>
      </div>

      {/* Sales Table */}
      <div className={styles.tableCard}>
        {loading ? (
          <div className={styles.emptyState}>
            <Loader2 className="animate-spin" size={32} color="var(--color-primary)" />
            <p>Carregando pedidos de venda...</p>
          </div>
        ) : vendas.length === 0 ? (
          <div className={styles.emptyState}>
            <ShoppingCart size={48} color="var(--text-subtle)" />
            <h3>Nenhum pedido encontrado</h3>
            <p>Clique no botão acima para cadastrar a primeira venda.</p>
          </div>
        ) : (
          <div className={styles.tableResponsive}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Pedido</th>
                  <th>Cliente</th>
                  <th>Emissão</th>
                  <th>Previsão</th>
                  <th>Qtd Itens</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th style={{ textAlign: 'right' }}>Ações</th>
                </tr>
              </thead>
              <tbody>
                {vendas.map((v) => {
                  const isActionLoading = actionLoadingId === v.id;
                  return (
                    <tr key={v.id}>
                      <td>
                        <span className={styles.badgeVendaId}>
                          #{String(v.id).padStart(5, '0')}
                        </span>
                      </td>
                      <td>
                        <div style={{ fontWeight: 600 }}>{v.nomeCliente}</div>
                        {v.documentoCliente && (
                          <div
                            style={{
                              fontSize: '0.78rem',
                              color: 'var(--text-muted)',
                            }}
                          >
                            {v.documentoCliente}
                          </div>
                        )}
                      </td>
                      <td>{formatDate(v.emissao)}</td>
                      <td>{formatDate(v.previsao)}</td>
                      <td>
                        <span
                          style={{
                            fontWeight: 600,
                            backgroundColor: 'var(--bg-subtle)',
                            padding: '0.2rem 0.5rem',
                            borderRadius: 'var(--radius-sm)',
                          }}
                        >
                          {v.itens?.length || 0}
                        </span>
                      </td>
                      <td style={{ fontWeight: 700, color: 'var(--text-main)' }}>
                        {formatCurrency(v.totalVenda)}
                      </td>
                      <td>{renderStatusBadge(v.statusVenda)}</td>
                      <td>
                        <div
                          className={styles.actionsCell}
                          style={{ justifyContent: 'flex-end' }}
                        >
                          {/* Botão Ver Detalhes */}
                          <button
                            type="button"
                            className={styles.actionBtnOutline}
                            title="Ver Itens e Detalhes"
                            onClick={() => handleVerDetalhes(v)}
                          >
                            <Eye size={14} /> Detalhes
                          </button>

                          {/* Ações de Workflow */}
                          {v.statusVenda === 'ORCAMENTO' && (
                            <button
                              type="button"
                              className={styles.actionBtnWarning}
                              disabled={isActionLoading}
                              title="Aprovar Orçamento e Reservar Estoque"
                              onClick={() => handleAlterarStatus(v.id!, 'ABERTO')}
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <CheckCircle size={14} />
                              )}
                              Aprovar
                            </button>
                          )}

                          {v.statusVenda === 'ABERTO' && (
                            <>
                              <button
                                type="button"
                                className={styles.actionBtnWarning}
                                disabled={isActionLoading}
                                title="Enviar para Separação de Mercadorias"
                                onClick={() =>
                                  handleAlterarStatus(v.id!, 'SEPARACAO')
                                }
                              >
                                {isActionLoading ? (
                                  <Loader2 size={14} className="animate-spin" />
                                ) : (
                                  <Package size={14} />
                                )}
                                Separação
                              </button>
                              <button
                                type="button"
                                className={styles.actionBtnSuccess}
                                disabled={isActionLoading}
                                title="Finalizar Venda, Baixar Estoque e Faturar"
                                onClick={() =>
                                  handleAlterarStatus(v.id!, 'FINALIZADO')
                                }
                              >
                                {isActionLoading ? (
                                  <Loader2 size={14} className="animate-spin" />
                                ) : (
                                  <CheckCircle size={14} />
                                )}
                                Faturar
                              </button>
                            </>
                          )}

                          {v.statusVenda === 'SEPARACAO' && (
                            <button
                              type="button"
                              className={styles.actionBtnSuccess}
                              disabled={isActionLoading}
                              title="Concluir Separação, Baixar Estoque e Faturar"
                              onClick={() =>
                                handleAlterarStatus(v.id!, 'FINALIZADO')
                              }
                            >
                              {isActionLoading ? (
                                <Loader2 size={14} className="animate-spin" />
                              ) : (
                                <CheckCircle size={14} />
                              )}
                              Concluir & Faturar
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

      {/* MODAL NOVO PEDIDO DE VENDA */}
      {modalNovoOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <ShoppingCart size={22} color="var(--color-primary)" />
                <h2>Novo Pedido Comercial</h2>
              </div>
              <button
                type="button"
                className={styles.closeBtn}
                onClick={() => setModalNovoOpen(false)}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSalvarVenda} className={styles.modalBody}>
              {errorMessage && (
                <div className={styles.alertBox}>
                  <AlertCircle size={18} />
                  <span>{errorMessage}</span>
                </div>
              )}

              {/* Seção Dados Principais */}
              <div>
                <h4 className={styles.formSectionTitle}>1. Informações do Pedido</h4>
                <div className={styles.formGrid3}>
                  <div className={styles.formGroup} style={{ gridColumn: 'span 2' }}>
                    <label htmlFor={formClienteId}>Cliente Ativo *</label>
                    <select
                      id={formClienteId}
                      value={clienteId}
                      onChange={(e) => setClienteId(Number(e.target.value))}
                      required
                    >
                      <option value="">Selecione um cliente...</option>
                      {clientes.map((c) => (
                        <option key={c.id} value={c.id}>
                          {c.nome} {c.cpf ? `(CPF: ${c.cpf})` : c.cnpj ? `(CNPJ: ${c.cnpj})` : ''}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formStatusId}>Status Inicial *</label>
                    <select
                      id={formStatusId}
                      value={statusVenda}
                      onChange={(e) => setStatusVenda(e.target.value as VendaStatus)}
                      required
                    >
                      <option value="ABERTO">Aberto (Reserva Estoque)</option>
                      <option value="ORCAMENTO">Orçamento (Não Reserva)</option>
                      <option value="FINALIZADO">Finalizado (Baixa Imediata)</option>
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

              {/* Seção Adicionar Itens */}
              <div>
                <h4 className={styles.formSectionTitle}>2. Composição de Itens</h4>
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
                    <label htmlFor={formItemProdutoId}>Produto / Mercadoria *</label>
                    <select
                      id={formItemProdutoId}
                      value={selectedProdutoId}
                      onChange={(e) =>
                        handleProdutoSelectChange(
                          e.target.value ? Number(e.target.value) : ''
                        )
                      }
                    >
                      <option value="">Selecione o produto no catálogo...</option>
                      {produtos.map((p) => {
                        const disp = p.estoqueDisponivel ?? p.estoqueAtual;
                        return (
                          <option key={p.id} value={p.id}>
                            {p.codigoProduto} - {p.nomeProduto} | Preço:{' '}
                            {formatCurrency(p.precoVenda)} | Estoque:{' '}
                            {disp} {p.unidadeMedida || 'UN'}
                          </option>
                        );
                      })}
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
                      <label htmlFor={formItemPrecoId}>Preço Unit. (R$)</label>
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
                      <label htmlFor={formItemAcrescId}>Acréscimo (R$)</label>
                      <input
                        id={formItemAcrescId}
                        type="number"
                        min="0"
                        step="0.01"
                        value={valorAcrescimo}
                        onChange={(e) => setValorAcrescimo(Number(e.target.value))}
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
                              valorAcrescimo
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
                        <th>Preço Unit.</th>
                        <th>Desc.</th>
                        <th>Acrésc.</th>
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
                          <td style={{ color: it.valorAcrescimo ? 'var(--color-primary)' : 'inherit' }}>
                            {it.valorAcrescimo ? `+ ${formatCurrency(it.valorAcrescimo)}` : '-'}
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
                    Nenhum item adicionado ao pedido ainda.
                  </div>
                )}
              </div>

              {/* Seção Totais e Integração Financeira */}
              <div>
                <h4 className={styles.formSectionTitle}>3. Totalização & Financeiro</h4>
                <div className={styles.totalsBox}>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem' }}>
                    <div style={{ fontSize: '0.88rem', color: 'var(--text-muted)' }}>
                      Itens Brutos: <strong>{formatCurrency(subtotalBruto)}</strong>
                      {totalDescontos > 0 && ` | Descontos: - ${formatCurrency(totalDescontos)}`}
                      {totalAcrescimos > 0 && ` | Acréscimos: + ${formatCurrency(totalAcrescimos)}`}
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
                      Gerar Título a Receber no Financeiro ao Finalizar
                    </label>
                  </div>

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 600 }}>
                      TOTAL GERAL DO PEDIDO
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
                      <Loader2 size={16} className="animate-spin" /> Registrando Pedido...
                    </>
                  ) : (
                    <>
                      <CheckCircle size={16} /> Concluir Pedido
                    </>
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL DETALHES DO PEDIDO */}
      {modalDetalhesOpen && vendaDetalhes && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent} style={{ maxWidth: '850px' }}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <Eye size={22} color="var(--color-primary)" />
                <h2>Pedido #{String(vendaDetalhes.id).padStart(5, '0')}</h2>
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
              {/* Resumo da Venda */}
              <div className={styles.detailGrid}>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Cliente</span>
                  <span className={styles.detailValue}>
                    {vendaDetalhes.nomeCliente}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Documento</span>
                  <span className={styles.detailValue}>
                    {vendaDetalhes.documentoCliente || 'Não informado'}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Data de Emissão</span>
                  <span className={styles.detailValue}>
                    {formatDate(vendaDetalhes.emissao)}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Previsão / Vencimento</span>
                  <span className={styles.detailValue}>
                    {formatDate(vendaDetalhes.previsao)}
                  </span>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Status Atual</span>
                  <div>{renderStatusBadge(vendaDetalhes.statusVenda)}</div>
                </div>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Valor Total</span>
                  <span
                    className={styles.detailValue}
                    style={{ color: 'var(--color-primary)', fontSize: '1.1rem' }}
                  >
                    {formatCurrency(vendaDetalhes.totalVenda)}
                  </span>
                </div>
              </div>

              {/* Tabela de Itens do Pedido */}
              <div>
                <h4 className={styles.formSectionTitle}>Itens e Produtos</h4>
                <table className={styles.itemsTable}>
                  <thead>
                    <tr>
                      <th style={{ width: '40px' }}>#</th>
                      <th>Código</th>
                      <th>Produto</th>
                      <th>Quantidade</th>
                      <th>Preço Unit.</th>
                      <th>Desconto</th>
                      <th>Acréscimo</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    {vendaDetalhes.itens && vendaDetalhes.itens.length > 0 ? (
                      vendaDetalhes.itens.map((it, idx) => (
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
                          <td style={{ color: it.valorAcrescimo ? 'var(--color-primary)' : 'inherit' }}>
                            {it.valorAcrescimo ? `+ ${formatCurrency(it.valorAcrescimo)}` : '-'}
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

export default VendasPage;
