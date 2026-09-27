import React, { useEffect, useState, useId } from 'react';
import {
  CircleDollarSign,
  Plus,
  Search,
  X,
  Eye,
  AlertCircle,
  Loader2,
  CheckCircle2,
  Undo2,
  Ban,
  ArrowUpRight,
  ArrowDownLeft,
  Wallet,
  AlertTriangle,
  Receipt,
} from 'lucide-react';
import tituloService from '../../services/tituloService';
import financeiroApoioService from '../../services/financeiroApoioService';
import clienteService from '../../services/clienteService';
import fornecedorService from '../../services/fornecedorService';
import type {
  TituloDTO,
  TipoTitulo,
  TituloStatus,
  ResumoFinanceiroDTO,
  CentroCustoDTO,
  PlanoContaDTO,
  ClassificacaoDTO,
} from '../../types/financeiro';
import type { ClienteDTO, FornecedorDTO } from '../../types/pessoa';
import styles from './FinanceiroPage.module.css';

export const FinanceiroPage: React.FC = () => {
  const [titulos, setTitulos] = useState<TituloDTO[]>([]);
  const [resumo, setResumo] = useState<ResumoFinanceiroDTO>({
    totalReceberPendente: 0,
    totalPagarPendente: 0,
    saldoPrevisto: 0,
    totalRecebidoMes: 0,
    totalPagoMes: 0,
    totalVencido: 0,
  });

  const [clientes, setClientes] = useState<ClienteDTO[]>([]);
  const [fornecedores, setFornecedores] = useState<FornecedorDTO[]>([]);
  const [centrosCusto, setCentrosCusto] = useState<CentroCustoDTO[]>([]);
  const [planosConta, setPlanosConta] = useState<PlanoContaDTO[]>([]);
  const [classificacoes, setClassificacoes] = useState<ClassificacaoDTO[]>([]);

  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [tipoTab, setTipoTab] = useState<'' | 'RECEITA' | 'DESPESA'>('');
  const [statusFilter, setStatusFilter] = useState<TituloStatus | ''>('');
  const [dataInicio, setDataInicio] = useState('');
  const [dataFim, setDataFim] = useState('');
  const [actionLoadingId, setActionLoadingId] = useState<number | null>(null);

  // Modal Novo Título Manual
  const [modalNovoOpen, setModalNovoOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Form Novo Título
  const [novoTipo, setNovoTipo] = useState<TipoTitulo>('DESPESA');
  const [novoIdPessoa, setNovoIdPessoa] = useState<number | ''>('');
  const [novaDescricao, setNovaDescricao] = useState('');
  const [novoValor, setNovoValor] = useState<string>('');
  const [novaEmissao, setNovaEmissao] = useState(new Date().toISOString().split('T')[0]);
  const [novoVencimento, setNovoVencimento] = useState(new Date().toISOString().split('T')[0]);
  const [novoCentroCustoId, setNovoCentroCustoId] = useState<number | ''>('');
  const [novoPlanoContaId, setNovoPlanoContaId] = useState<number | ''>('');
  const [novaClassificacaoId, setNovaClassificacaoId] = useState<number | ''>('');

  // Modal Baixa / Liquidação
  const [modalBaixaOpen, setModalBaixaOpen] = useState(false);
  const [tituloBaixa, setTituloBaixa] = useState<TituloDTO | null>(null);
  const [dataPagamentoBaixa, setDataPagamentoBaixa] = useState(new Date().toISOString().split('T')[0]);
  const [liquidando, setLiquidando] = useState(false);

  // Modal Detalhes
  const [modalDetalhesOpen, setModalDetalhesOpen] = useState(false);
  const [tituloDetalhes, setTituloDetalhes] = useState<TituloDTO | null>(null);

  const searchInputId = useId();
  const statusFilterId = useId();
  const dataInicioId = useId();
  const dataFimId = useId();

  const formTipoId = useId();
  const formPessoaId = useId();
  const formDescricaoId = useId();
  const formValorId = useId();
  const formEmissaoId = useId();
  const formVencimentoId = useId();
  const formCentroCustoId = useId();
  const formPlanoContaId = useId();
  const formClassificacaoId = useId();
  const formBaixaDataId = useId();

  const carregarDados = async () => {
    try {
      setLoading(true);
      const [listaTitulos, resumoData] = await Promise.all([
        tituloService.listar(
          search,
          tipoTab ? tipoTab : undefined,
          statusFilter ? statusFilter : undefined,
          dataInicio || undefined,
          dataFim || undefined
        ),
        tituloService.obterResumo(),
      ]);
      setTitulos(listaTitulos);
      setResumo(resumoData);
    } catch (err) {
      console.error('Erro ao carregar dados financeiros:', err);
    } finally {
      setLoading(false);
    }
  };

  const carregarApoio = async () => {
    try {
      const [c, f, cc, pc, cl] = await Promise.all([
        clienteService.listar('', 'ATIVO'),
        fornecedorService.listar('', 'ATIVO'),
        financeiroApoioService.listarCentrosCusto(),
        financeiroApoioService.listarPlanosConta(),
        financeiroApoioService.listarClassificacoes(),
      ]);
      setClientes(c);
      setFornecedores(f);
      setCentrosCusto(cc);
      setPlanosConta(pc);
      setClassificacoes(cl);
    } catch (err) {
      console.error('Erro ao carregar entidades de apoio financeiro:', err);
    }
  };

  useEffect(() => {
    carregarDados();
  }, [search, tipoTab, statusFilter, dataInicio, dataFim]);

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
    if (clientes.length === 0 || fornecedores.length === 0) {
      carregarApoio();
    }
    setNovoTipo('DESPESA');
    setNovoIdPessoa('');
    setNovaDescricao('');
    setNovoValor('');
    setNovaEmissao(new Date().toISOString().split('T')[0]);
    setNovoVencimento(new Date().toISOString().split('T')[0]);
    setNovoCentroCustoId('');
    setNovoPlanoContaId('');
    setNovaClassificacaoId('');
    setErrorMessage(null);
    setModalNovoOpen(true);
  };

  const handleSalvarTitulo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!novoIdPessoa) {
      setErrorMessage('Por favor, selecione uma pessoa favorecida ou pagadora.');
      return;
    }

    const valorLimpo = String(novoValor).trim().replace(/\./g, '').replace(',', '.');
    const valorNumerico = parseFloat(valorLimpo);
    if (isNaN(valorNumerico) || valorNumerico <= 0) {
      setErrorMessage('O valor do título deve ser maior que zero (ex: 150,00).');
      return;
    }

    try {
      setSaving(true);
      setErrorMessage(null);

      const payload: TituloDTO = {
        tipo: novoTipo,
        idPessoa: Number(novoIdPessoa),
        descricao: novaDescricao,
        valorTotal: valorNumerico,
        emissao: novaEmissao,
        vencimento: novoVencimento,
        status: 'PENDENTE',
        idCentroCusto: novoCentroCustoId ? Number(novoCentroCustoId) : undefined,
        idPlanoConta: novoPlanoContaId ? Number(novoPlanoContaId) : undefined,
        idClassificacao: novaClassificacaoId ? Number(novaClassificacaoId) : undefined,
      };

      await tituloService.criar(payload);
      setModalNovoOpen(false);
      await carregarDados();
    } catch (err: any) {
      setErrorMessage(err?.response?.data?.message || err?.message || 'Erro ao registrar título financeiro.');
    } finally {
      setSaving(false);
    }
  };

  const abrirModalBaixa = (titulo: TituloDTO) => {
    setTituloBaixa(titulo);
    setDataPagamentoBaixa(new Date().toISOString().split('T')[0]);
    setModalBaixaOpen(true);
  };

  const handleConfirmarBaixa = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!tituloBaixa?.id) return;

    try {
      setLiquidando(true);
      await tituloService.liquidar(tituloBaixa.id, {
        dataPagamento: dataPagamentoBaixa,
      });
      setModalBaixaOpen(false);
      setTituloBaixa(null);
      await carregarDados();
    } catch (err: any) {
      alert(err?.response?.data?.message || err?.message || 'Erro ao liquidar título.');
    } finally {
      setLiquidando(false);
    }
  };

  const handleCancelarTitulo = async (id: number) => {
    if (!confirm('Tem certeza de que deseja cancelar este título financeiro?')) return;
    try {
      setActionLoadingId(id);
      await tituloService.cancelar(id);
      await carregarDados();
    } catch (err: any) {
      alert(err?.response?.data?.message || err?.message || 'Erro ao cancelar título.');
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleEstornarTitulo = async (id: number) => {
    if (!confirm('Deseja estornar a quitação deste título e torná-lo PENDENTE novamente?')) return;
    try {
      setActionLoadingId(id);
      await tituloService.estornar(id);
      await carregarDados();
    } catch (err: any) {
      alert(err?.response?.data?.message || err?.message || 'Erro ao estornar título.');
    } finally {
      setActionLoadingId(null);
    }
  };

  const handleVerDetalhes = (titulo: TituloDTO) => {
    setTituloDetalhes(titulo);
    setModalDetalhesOpen(true);
  };

  // Monta lista de pessoas combinadas (Pessoa ID -> Nome)
  const listaPessoas: { id: number; nome: string; doc?: string; tipo: string }[] = [];
  const listaCli = clientes
    .filter((c) => c.idPessoa)
    .map((c) => ({
      id: c.idPessoa!,
      nome: c.nome,
      doc: c.cpf || c.cnpj,
      tipo: 'Cliente',
    }));
  const listaForn = fornecedores
    .filter((f) => f.idPessoa)
    .map((f) => ({
      id: f.idPessoa!,
      nome: f.nome,
      doc: f.cpf || f.cnpj,
      tipo: 'Fornecedor',
    }));

  if (novoTipo === 'DESPESA') {
    listaPessoas.push(...listaForn);
    listaCli.forEach((c) => {
      if (!listaPessoas.some((p) => p.id === c.id)) listaPessoas.push(c);
    });
  } else {
    listaPessoas.push(...listaCli);
    listaForn.forEach((f) => {
      if (!listaPessoas.some((p) => p.id === f.id)) listaPessoas.push(f);
    });
  }

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <h1>Financeiro & Títulos</h1>
          <p>Gestão de Contas a Pagar, Contas a Receber e Fluxo de Caixa</p>
        </div>
        <button
          type="button"
          className={styles.btnPrimary}
          onClick={abrirModalNovo}
        >
          <Plus size={18} /> Novo Lançamento Manual
        </button>
      </div>

      {/* KPI Cards de Fluxo de Caixa */}
      <div className={styles.kpiGrid}>
        <div className={`${styles.kpiCard} ${styles.kpiReceber}`}>
          <div className={styles.kpiHeader}>
            <span>A RECEBER (PENDENTE)</span>
            <ArrowUpRight size={18} color="var(--color-success)" />
          </div>
          <div className={styles.kpiValue} style={{ color: 'var(--color-success-text)' }}>
            {formatCurrency(resumo.totalReceberPendente)}
          </div>
          <div className={styles.kpiSubtext}>
            Realizado este mês: {formatCurrency(resumo.totalRecebidoMes)}
          </div>
        </div>

        <div className={`${styles.kpiCard} ${styles.kpiPagar}`}>
          <div className={styles.kpiHeader}>
            <span>A PAGAR (PENDENTE)</span>
            <ArrowDownLeft size={18} color="var(--color-danger)" />
          </div>
          <div className={styles.kpiValue} style={{ color: 'var(--color-danger-text)' }}>
            {formatCurrency(resumo.totalPagarPendente)}
          </div>
          <div className={styles.kpiSubtext}>
            Pago este mês: {formatCurrency(resumo.totalPagoMes)}
          </div>
        </div>

        <div className={`${styles.kpiCard} ${styles.kpiSaldo}`}>
          <div className={styles.kpiHeader}>
            <span>SALDO PREVISTO</span>
            <Wallet size={18} color="var(--color-primary)" />
          </div>
          <div
            className={styles.kpiValue}
            style={{
              color: resumo.saldoPrevisto >= 0 ? 'var(--color-primary)' : 'var(--color-danger)',
            }}
          >
            {formatCurrency(resumo.saldoPrevisto)}
          </div>
          <div className={styles.kpiSubtext}>
            Diferença: Receitas - Despesas
          </div>
        </div>

        <div className={`${styles.kpiCard} ${styles.kpiVencidos}`}>
          <div className={styles.kpiHeader}>
            <span>TÍTULOS VENCIDOS</span>
            <AlertTriangle size={18} color="#b91c1c" />
          </div>
          <div className={styles.kpiValue} style={{ color: '#b91c1c' }}>
            {formatCurrency(resumo.totalVencido)}
          </div>
          <div className={styles.kpiSubtext}>
            Exigem cobrança ou quitação urgente
          </div>
        </div>
      </div>

      {/* Tabs Navigation */}
      <div className={styles.tabsBar}>
        <button
          type="button"
          className={`${styles.tabItem} ${tipoTab === '' ? styles.tabItemActive : ''}`}
          onClick={() => setTipoTab('')}
        >
          <Receipt size={18} /> Todos os Títulos
        </button>
        <button
          type="button"
          className={`${styles.tabItem} ${tipoTab === 'RECEITA' ? styles.tabItemActive : ''}`}
          onClick={() => setTipoTab('RECEITA')}
        >
          <ArrowUpRight size={18} color="var(--color-success)" /> Contas a Receber
        </button>
        <button
          type="button"
          className={`${styles.tabItem} ${tipoTab === 'DESPESA' ? styles.tabItemActive : ''}`}
          onClick={() => setTipoTab('DESPESA')}
        >
          <ArrowDownLeft size={18} color="var(--color-danger)" /> Contas a Pagar
        </button>
      </div>

      {/* Filters Card */}
      <div className={styles.filterCard}>
        <div className={styles.searchBox}>
          <Search size={18} color="var(--text-subtle)" />
          <input
            id={searchInputId}
            type="text"
            placeholder="Buscar por descrição ou pessoa..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className={styles.filterControls}>
          <div className={styles.filterGroup}>
            <label htmlFor={statusFilterId}>Status:</label>
            <select
              id={statusFilterId}
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value as TituloStatus | '')}
            >
              <option value="">Todos</option>
              <option value="PENDENTE">Pendentes</option>
              <option value="PAGO">Pagos / Liquidados</option>
              <option value="CANCELADO">Cancelados</option>
            </select>
          </div>

          <div className={styles.filterGroup}>
            <label htmlFor={dataInicioId}>De:</label>
            <input
              id={dataInicioId}
              type="date"
              value={dataInicio}
              onChange={(e) => setDataInicio(e.target.value)}
            />
          </div>

          <div className={styles.filterGroup}>
            <label htmlFor={dataFimId}>Até:</label>
            <input
              id={dataFimId}
              type="date"
              value={dataFim}
              onChange={(e) => setDataFim(e.target.value)}
            />
          </div>
        </div>
      </div>

      {/* Financial Titles Table */}
      <div className={styles.tableCard}>
        {loading ? (
          <div className={styles.emptyState}>
            <Loader2 className="animate-spin" size={32} color="var(--color-primary)" />
            <p>Carregando títulos financeiros...</p>
          </div>
        ) : titulos.length === 0 ? (
          <div className={styles.emptyState}>
            <CircleDollarSign size={48} color="var(--text-subtle)" />
            <h3>Nenhum título encontrado</h3>
            <p>Os títulos de vendas e compras finalizadas aparecerão automaticamente aqui.</p>
          </div>
        ) : (
          <div className={styles.tableResponsive}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Tipo</th>
                  <th>Descrição</th>
                  <th>Pessoa / Favorecido</th>
                  <th>Emissão</th>
                  <th>Vencimento</th>
                  <th>Liquidação</th>
                  <th>Valor</th>
                  <th>Status</th>
                  <th style={{ textAlign: 'right' }}>Ações</th>
                </tr>
              </thead>
              <tbody>
                {titulos.map((t) => {
                  const isActionLoading = actionLoadingId === t.id;
                  const isReceita = t.tipo === 'RECEITA' || t.tipo === 'PREVISAO_RECEITA';

                  return (
                    <tr key={t.id}>
                      <td>
                        {isReceita ? (
                          <div className={styles.tipoReceita}>
                            <ArrowUpRight size={14} /> Receita
                          </div>
                        ) : (
                          <div className={styles.tipoDespesa}>
                            <ArrowDownLeft size={14} /> Despesa
                          </div>
                        )}
                      </td>
                      <td>
                        <div style={{ fontWeight: 600 }}>{t.descricao}</div>
                        <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>
                          {t.idVenda && `Pedido de Venda #${t.idVenda}`}
                          {t.idCompra && `Pedido de Compra #${t.idCompra}`}
                          {!t.idVenda && !t.idCompra && 'Lançamento Manual'}
                        </div>
                      </td>
                      <td>
                        <div style={{ fontWeight: 500 }}>{t.nomePessoa}</div>
                        {t.documentoPessoa && (
                          <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>
                            {t.documentoPessoa}
                          </div>
                        )}
                      </td>
                      <td>{formatDate(t.emissao)}</td>
                      <td>
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                          <span>{formatDate(t.vencimento)}</span>
                          {t.vencido && <span className={styles.badgeVencido}>Vencido</span>}
                        </div>
                      </td>
                      <td>{t.pagamento ? formatDate(t.pagamento) : '-'}</td>
                      <td
                        style={{
                          fontWeight: 700,
                          color: isReceita ? 'var(--color-success-text)' : 'var(--color-danger-text)',
                        }}
                      >
                        {formatCurrency(t.valorTotal)}
                      </td>
                      <td>
                        {t.status === 'PAGO' ? (
                          <span className={styles.statusPago}>Pago</span>
                        ) : t.status === 'PENDENTE' ? (
                          <span className={styles.statusPendente}>Pendente</span>
                        ) : (
                          <span className={styles.statusCancelado}>Cancelado</span>
                        )}
                      </td>
                      <td>
                        <div className={styles.actionsCell} style={{ justifyContent: 'flex-end' }}>
                          {/* Botão Ver Detalhes */}
                          <button
                            type="button"
                            className={styles.actionBtnOutline}
                            title="Ver Detalhes do Título"
                            onClick={() => handleVerDetalhes(t)}
                          >
                            <Eye size={14} /> Detalhes
                          </button>

                          {/* Se Pendente: Liquidar ou Cancelar */}
                          {t.status === 'PENDENTE' && (
                            <>
                              <button
                                type="button"
                                className={styles.actionBtnSuccess}
                                title={isReceita ? 'Registrar Recebimento' : 'Registrar Pagamento'}
                                onClick={() => abrirModalBaixa(t)}
                              >
                                <CheckCircle2 size={14} />
                                {isReceita ? 'Receber' : 'Pagar'}
                              </button>
                              <button
                                type="button"
                                className={styles.actionBtnDanger}
                                disabled={isActionLoading}
                                title="Cancelar Título"
                                onClick={() => handleCancelarTitulo(t.id!)}
                              >
                                <Ban size={14} />
                              </button>
                            </>
                          )}

                          {/* Se Pago: Estornar */}
                          {t.status === 'PAGO' && (
                            <button
                              type="button"
                              className={styles.actionBtnWarning}
                              disabled={isActionLoading}
                              title="Estornar Quitação (Voltar para Pendente)"
                              onClick={() => handleEstornarTitulo(t.id!)}
                            >
                              <Undo2 size={14} /> Estornar
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

      {/* MODAL NOVO TÍTULO MANUAL */}
      {modalNovoOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <CircleDollarSign size={22} color="var(--color-primary)" />
                <h2>Novo Lançamento Financeiro</h2>
              </div>
              <button
                type="button"
                className={styles.closeBtn}
                onClick={() => setModalNovoOpen(false)}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSalvarTitulo} className={styles.modalBody}>
              {errorMessage && (
                <div className={styles.alertBox}>
                  <AlertCircle size={18} />
                  <span>{errorMessage}</span>
                </div>
              )}

              <div>
                <h4 className={styles.formSectionTitle}>1. Informações Básicas</h4>
                <div className={styles.formGrid2}>
                  <div className={styles.formGroup}>
                    <label htmlFor={formTipoId}>Tipo de Título *</label>
                    <select
                      id={formTipoId}
                      value={novoTipo}
                      onChange={(e) => setNovoTipo(e.target.value as TipoTitulo)}
                      required
                    >
                      <option value="DESPESA">Contas a Pagar (Despesa)</option>
                      <option value="RECEITA">Contas a Receber (Receita)</option>
                    </select>
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formPessoaId}>Favorecido / Sacado *</label>
                    <select
                      id={formPessoaId}
                      value={novoIdPessoa}
                      onChange={(e) => setNovoIdPessoa(Number(e.target.value))}
                      required
                    >
                      <option value="">Selecione a pessoa...</option>
                      {listaPessoas.map((p) => (
                        <option key={p.id} value={p.id}>
                          {p.nome} {p.doc ? `(${p.doc})` : ''} [{p.tipo}]
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className={styles.formGroup} style={{ marginTop: '0.85rem' }}>
                  <label htmlFor={formDescricaoId}>Descrição do Lançamento *</label>
                  <input
                    id={formDescricaoId}
                    type="text"
                    placeholder="Ex: Aluguel do galpão, Conta de energia, Consultoria..."
                    value={novaDescricao}
                    onChange={(e) => setNovaDescricao(e.target.value)}
                    required
                  />
                </div>
              </div>

              <div>
                <h4 className={styles.formSectionTitle}>2. Prazos e Valores</h4>
                <div className={styles.formGrid3}>
                  <div className={styles.formGroup}>
                    <label htmlFor={formEmissaoId}>Data de Emissão *</label>
                    <input
                      id={formEmissaoId}
                      type="date"
                      value={novaEmissao}
                      onChange={(e) => setNovaEmissao(e.target.value)}
                      required
                    />
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formVencimentoId}>Data de Vencimento *</label>
                    <input
                      id={formVencimentoId}
                      type="date"
                      value={novoVencimento}
                      onChange={(e) => setNovoVencimento(e.target.value)}
                      required
                    />
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formValorId}>Valor Total (R$) *</label>
                    <input
                      id={formValorId}
                      type="text"
                      inputMode="decimal"
                      placeholder="0,00"
                      value={novoValor}
                      onChange={(e) => setNovoValor(e.target.value)}
                      required
                    />
                  </div>
                </div>
              </div>

              <div>
                <h4 className={styles.formSectionTitle}>3. Classificação Contábil (Opcional)</h4>
                <div className={styles.formGrid3}>
                  <div className={styles.formGroup}>
                    <label htmlFor={formCentroCustoId}>Centro de Custo</label>
                    <select
                      id={formCentroCustoId}
                      value={novoCentroCustoId}
                      onChange={(e) =>
                        setNovoCentroCustoId(e.target.value ? Number(e.target.value) : '')
                      }
                    >
                      <option value="">Não informado</option>
                      {centrosCusto.map((cc) => (
                        <option key={cc.id} value={cc.id}>
                          {cc.descricao}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formPlanoContaId}>Plano de Contas</label>
                    <select
                      id={formPlanoContaId}
                      value={novoPlanoContaId}
                      onChange={(e) =>
                        setNovoPlanoContaId(e.target.value ? Number(e.target.value) : '')
                      }
                    >
                      <option value="">Não informado</option>
                      {planosConta.map((pc) => (
                        <option key={pc.id} value={pc.id}>
                          {pc.descricao}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className={styles.formGroup}>
                    <label htmlFor={formClassificacaoId}>Classificação</label>
                    <select
                      id={formClassificacaoId}
                      value={novaClassificacaoId}
                      onChange={(e) =>
                        setNovaClassificacaoId(e.target.value ? Number(e.target.value) : '')
                      }
                    >
                      <option value="">Não informado</option>
                      {classificacoes.map((cl) => (
                        <option key={cl.id} value={cl.id}>
                          {cl.descricao}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>

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
                      <CheckCircle2 size={16} /> Salvar Lançamento
                    </>
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL BAIXA / LIQUIDAÇÃO */}
      {modalBaixaOpen && tituloBaixa && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent} style={{ maxWidth: '500px' }}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <CheckCircle2 size={22} color="var(--color-success)" />
                <h2>Liquidar / Baixar Título</h2>
              </div>
              <button
                type="button"
                className={styles.closeBtn}
                onClick={() => setModalBaixaOpen(false)}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleConfirmarBaixa} className={styles.modalBody}>
              <p style={{ fontSize: '0.92rem', color: 'var(--text-main)' }}>
                Confirme a quitação de <strong>{tituloBaixa.descricao}</strong> para{' '}
                <strong>{tituloBaixa.nomePessoa}</strong>:
              </p>

              <div
                style={{
                  backgroundColor: 'var(--bg-subtle)',
                  padding: '1rem',
                  borderRadius: 'var(--radius-md)',
                  textAlign: 'center',
                }}
              >
                <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>VALOR A LIQUIDAR</span>
                <div style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--color-primary)' }}>
                  {formatCurrency(tituloBaixa.valorTotal)}
                </div>
              </div>

              <div className={styles.formGroup}>
                <label htmlFor={formBaixaDataId}>Data Efetiva de Pagamento / Crédito *</label>
                <input
                  id={formBaixaDataId}
                  type="date"
                  value={dataPagamentoBaixa}
                  onChange={(e) => setDataPagamentoBaixa(e.target.value)}
                  required
                />
              </div>

              <div className={styles.modalFooter}>
                <button
                  type="button"
                  className={styles.btnSecondary}
                  onClick={() => setModalBaixaOpen(false)}
                  disabled={liquidando}
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className={styles.btnPrimary}
                  style={{ backgroundColor: 'var(--color-success)' }}
                  disabled={liquidando}
                >
                  {liquidando ? (
                    <>
                      <Loader2 size={16} className="animate-spin" /> Efetuando Baixa...
                    </>
                  ) : (
                    <>
                      <CheckCircle2 size={16} /> Confirmar Quitação
                    </>
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL DETALHES DO TÍTULO */}
      {modalDetalhesOpen && tituloDetalhes && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent} style={{ maxWidth: '650px' }}>
            <div className={styles.modalHeader}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                <Eye size={22} color="var(--color-primary)" />
                <h2>Título #{String(tituloDetalhes.id).padStart(5, '0')}</h2>
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
              <div className={styles.detailGrid}>
                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Tipo</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.tipo === 'RECEITA' ? 'Contas a Receber' : 'Contas a Pagar'}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Status</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.status}
                  </span>
                </div>

                <div className={styles.detailCard} style={{ gridColumn: 'span 2' }}>
                  <span className={styles.detailLabel}>Descrição</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.descricao}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Favorecido / Sacado</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.nomePessoa}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Documento</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.documentoPessoa || 'Não informado'}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Data de Emissão</span>
                  <span className={styles.detailValue}>
                    {formatDate(tituloDetalhes.emissao)}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Data de Vencimento</span>
                  <span className={styles.detailValue}>
                    {formatDate(tituloDetalhes.vencimento)}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Data de Quitação</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.pagamento ? formatDate(tituloDetalhes.pagamento) : 'Pendente'}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Valor do Título</span>
                  <span className={styles.detailValue} style={{ color: 'var(--color-primary)', fontSize: '1.2rem' }}>
                    {formatCurrency(tituloDetalhes.valorTotal)}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Centro de Custo</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.nomeCentroCusto || 'Geral'}
                  </span>
                </div>

                <div className={styles.detailCard}>
                  <span className={styles.detailLabel}>Plano de Contas</span>
                  <span className={styles.detailValue}>
                    {tituloDetalhes.nomePlanoConta || 'Operacional'}
                  </span>
                </div>
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

export default FinanceiroPage;
