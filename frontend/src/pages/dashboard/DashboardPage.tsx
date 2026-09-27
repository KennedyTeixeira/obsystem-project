import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  ShoppingCart,
  ShoppingBag,
  ArrowUpRight,
  ArrowDownLeft,
  Wallet,
  AlertTriangle,
  Users,
  Clock,
  Plus,
  RefreshCw,
  Loader2,
  Package,
  Layers,
  CircleDollarSign,
  ChevronRight,
} from 'lucide-react';
import dashboardService from '../../services/dashboardService';
import type { DashboardDTO } from '../../types/dashboard';
import styles from './DashboardPage.module.css';

export const DashboardPage: React.FC = () => {
  const [data, setData] = useState<DashboardDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [tabOperacoes, setTabOperacoes] = useState<'VENDAS' | 'COMPRAS'>('VENDAS');

  const carregarDashboard = async () => {
    try {
      setRefreshing(true);
      const resumo = await dashboardService.obterResumo();
      setData(resumo);
    } catch (err) {
      console.error('Erro ao carregar dados do dashboard:', err);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    carregarDashboard();
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

  const getHojeFormatado = () => {
    const agora = new Date();
    const opcoes: Intl.DateTimeFormatOptions = {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    };
    const str = agora.toLocaleDateString('pt-BR', opcoes);
    return str.charAt(0).toUpperCase() + str.slice(1);
  };

  if (loading && !data) {
    return (
      <div className={styles.emptyState} style={{ minHeight: '60vh', justifyContent: 'center' }}>
        <Loader2 className="animate-spin" size={40} color="var(--color-primary)" />
        <p style={{ marginTop: '0.75rem', fontWeight: 600 }}>Carregando dados do painel executivo...</p>
      </div>
    );
  }

  const d = data || {
    totalVendasMes: 0,
    qtdVendasMes: 0,
    totalComprasMes: 0,
    qtdComprasMes: 0,
    totalReceberPendente: 0,
    totalRecebidoMes: 0,
    totalPagarPendente: 0,
    totalPagoMes: 0,
    saldoPrevisto: 0,
    totalVencidos: 0,
    qtdVencidos: 0,
    qtdProdutosEstoqueBaixo: 0,
    qtdClientesAtivos: 0,
    qtdFornecedoresAtivos: 0,
    ultimasVendas: [],
    ultimasCompras: [],
    proximosTitulos: [],
  };

  return (
    <div className={styles.dashboard}>
      {/* 1. Header com Saudação e Data */}
      <div className={styles.welcomeHeader}>
        <div>
          <h1>Painel Geral</h1>
          <p>{getHojeFormatado()} • Visão consolidada das operações do Obsystem ERP</p>
        </div>

        <div className={styles.headerActions}>
          <button
            type="button"
            className={styles.btnRefresh}
            onClick={carregarDashboard}
            disabled={refreshing}
            title="Atualizar dados do painel"
          >
            <RefreshCw size={16} className={refreshing ? 'animate-spin' : ''} />
            {refreshing ? 'Atualizando...' : 'Atualizar'}
          </button>
        </div>
      </div>

      {/* 2. Barra de Ações Rápidas (Quick Actions) */}
      <div className={styles.quickActionsBar}>
        <div className={styles.quickActionsTitle}>
          <Layers size={18} color="var(--color-primary)" />
          <span>Ações Rápidas</span>
        </div>
        <div className={styles.quickActionButtons}>
          <Link to="/vendas" className={`${styles.btnAction} ${styles.btnActionPrimary}`}>
            <Plus size={15} /> Nova Venda
          </Link>
          <Link to="/compras" className={`${styles.btnAction} ${styles.btnActionOutline}`}>
            <Plus size={15} /> Nova Compra
          </Link>
          <Link to="/produtos" className={`${styles.btnAction} ${styles.btnActionOutline}`}>
            <Plus size={15} /> Novo Produto
          </Link>
          <Link to="/financeiro" className={`${styles.btnAction} ${styles.btnActionOutline}`}>
            <Plus size={15} /> Lançamento Financeiro
          </Link>
        </div>
      </div>

      {/* 3. Grade dos 5 KPIs Principais */}
      <div className={styles.metricsGrid}>
        {/* Vendas do Mês */}
        <Link to="/vendas" className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconSales}`}>
            <ShoppingCart size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Vendas do Mês</span>
            <span className={styles.metricValue}>{formatCurrency(d.totalVendasMes)}</span>
            <span className={styles.metricSubtext}>
              {d.qtdVendasMes} {d.qtdVendasMes === 1 ? 'pedido faturado' : 'pedidos faturados'}
            </span>
          </div>
        </Link>

        {/* Compras do Mês */}
        <Link to="/compras" className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconPurchases}`}>
            <ShoppingBag size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Compras do Mês</span>
            <span className={styles.metricValue}>{formatCurrency(d.totalComprasMes)}</span>
            <span className={styles.metricSubtext}>
              {d.qtdComprasMes} {d.qtdComprasMes === 1 ? 'compra realizada' : 'compras realizadas'}
            </span>
          </div>
        </Link>

        {/* Contas a Receber */}
        <Link to="/financeiro" className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconReceive}`}>
            <ArrowUpRight size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Contas a Receber</span>
            <span className={styles.metricValue} style={{ color: 'var(--color-success-text)' }}>
              {formatCurrency(d.totalReceberPendente)}
            </span>
            <span className={styles.metricSubtext}>
              Recebido no mês: {formatCurrency(d.totalRecebidoMes)}
            </span>
          </div>
        </Link>

        {/* Contas a Pagar */}
        <Link to="/financeiro" className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconPay}`}>
            <ArrowDownLeft size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Contas a Pagar</span>
            <span className={styles.metricValue} style={{ color: 'var(--color-danger-text)' }}>
              {formatCurrency(d.totalPagarPendente)}
            </span>
            <span className={styles.metricSubtext}>
              Pago no mês: {formatCurrency(d.totalPagoMes)}
            </span>
          </div>
        </Link>

        {/* Saldo Previsto de Caixa */}
        <Link to="/financeiro" className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconBalance}`}>
            <Wallet size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Saldo Previsto</span>
            <span
              className={styles.metricValue}
              style={{
                color: d.saldoPrevisto >= 0 ? '#6b21a8' : 'var(--color-danger-text)',
              }}
            >
              {formatCurrency(d.saldoPrevisto)}
            </span>
            <span className={styles.metricSubtext}>Receber menos Pagar</span>
          </div>
        </Link>
      </div>

      {/* 4. Banner de Alertas e Saúde Operacional */}
      <div className={styles.alertsGrid}>
        {/* Estoque Baixo */}
        <Link to="/produtos" className={`${styles.alertCard} ${styles.alertStock}`}>
          <div className={`${styles.alertIcon}`} style={{ backgroundColor: '#fee2e2', color: '#dc2626' }}>
            <AlertTriangle size={22} />
          </div>
          <div className={styles.alertContent}>
            <h4>
              {d.qtdProdutosEstoqueBaixo > 0
                ? `${d.qtdProdutosEstoqueBaixo} ${d.qtdProdutosEstoqueBaixo === 1 ? 'produto com estoque baixo' : 'produtos com estoque baixo'}`
                : 'Estoque Sob Controle'}
            </h4>
            <p>
              {d.qtdProdutosEstoqueBaixo > 0
                ? 'Itens atingiram o nível mínimo de reposição'
                : 'Nenhum item abaixo do estoque mínimo'}
            </p>
          </div>
        </Link>

        {/* Títulos Vencidos */}
        <Link to="/financeiro" className={`${styles.alertCard} ${styles.alertOverdue}`}>
          <div className={`${styles.alertIcon}`} style={{ backgroundColor: '#fef3c7', color: '#d97706' }}>
            <Clock size={22} />
          </div>
          <div className={styles.alertContent}>
            <h4>
              {d.qtdVencidos > 0
                ? `${d.qtdVencidos} ${d.qtdVencidos === 1 ? 'título vencido' : 'títulos vencidos'} (${formatCurrency(d.totalVencidos)})`
                : 'Nenhum Título Vencido'}
            </h4>
            <p>
              {d.qtdVencidos > 0
                ? 'Requer atenção imediata no Contas a Receber/Pagar'
                : 'Todas as obrigações e recebimentos em dia'}
            </p>
          </div>
        </Link>

        {/* Clientes e Fornecedores Ativos */}
        <Link to="/pessoas" className={`${styles.alertCard} ${styles.alertPeople}`}>
          <div className={`${styles.alertIcon}`} style={{ backgroundColor: '#dcfce7', color: '#16a34a' }}>
            <Users size={22} />
          </div>
          <div className={styles.alertContent}>
            <h4>{d.qtdClientesAtivos + d.qtdFornecedoresAtivos} Parceiros Cadastrados</h4>
            <p>
              {d.qtdClientesAtivos} clientes e {d.qtdFornecedoresAtivos} fornecedores ativos
            </p>
          </div>
        </Link>
      </div>

      {/* 5. Painéis de Operações Recentes (Grade de 2 Colunas) */}
      <div className={styles.columnsGrid}>
        {/* Coluna 1: Movimentações Comerciais (Vendas / Compras Recentes) */}
        <div className={styles.sectionCard}>
          <div className={styles.sectionHeader}>
            <div className={styles.sectionTitle}>
              <Package size={18} color="var(--color-primary)" />
              <span>Movimentações Recentes</span>
            </div>
            <div className={styles.tabButtons}>
              <button
                type="button"
                className={`${styles.tabBtn} ${tabOperacoes === 'VENDAS' ? styles.tabBtnActive : ''}`}
                onClick={() => setTabOperacoes('VENDAS')}
              >
                Últimas Vendas
              </button>
              <button
                type="button"
                className={`${styles.tabBtn} ${tabOperacoes === 'COMPRAS' ? styles.tabBtnActive : ''}`}
                onClick={() => setTabOperacoes('COMPRAS')}
              >
                Últimas Compras
              </button>
            </div>
          </div>

          <div className={styles.tableResponsive}>
            {tabOperacoes === 'VENDAS' ? (
              d.ultimasVendas.length === 0 ? (
                <div className={styles.emptyState}>
                  <ShoppingCart size={32} color="var(--text-subtle)" />
                  <p>Nenhuma venda registrada ainda.</p>
                </div>
              ) : (
                <table className={styles.table}>
                  <thead>
                    <tr>
                      <th>Pedido</th>
                      <th>Cliente</th>
                      <th>Data</th>
                      <th>Status</th>
                      <th style={{ textAlign: 'right' }}>Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {d.ultimasVendas.map((v) => (
                      <tr key={v.id}>
                        <td>
                          <span className={styles.cellPrimary}>#{v.id}</span>
                        </td>
                        <td>
                          <div className={styles.cellPrimary}>{v.nomeCliente}</div>
                        </td>
                        <td>{formatDate(v.emissao)}</td>
                        <td>
                          <span
                            className={`${styles.statusBadge} ${
                              v.statusVenda === 'FINALIZADO'
                                ? styles.badgeSuccess
                                : v.statusVenda === 'SEPARACAO'
                                ? styles.badgePrimary
                                : styles.badgeWarning
                            }`}
                          >
                            {v.statusVenda}
                          </span>
                        </td>
                        <td style={{ textAlign: 'right', fontWeight: 700 }}>
                          {formatCurrency(v.totalVenda)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )
            ) : (
              d.ultimasCompras.length === 0 ? (
                <div className={styles.emptyState}>
                  <ShoppingBag size={32} color="var(--text-subtle)" />
                  <p>Nenhuma compra registrada ainda.</p>
                </div>
              ) : (
                <table className={styles.table}>
                  <thead>
                    <tr>
                      <th>Pedido</th>
                      <th>Fornecedor</th>
                      <th>Data</th>
                      <th>Status</th>
                      <th style={{ textAlign: 'right' }}>Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {d.ultimasCompras.map((c) => (
                      <tr key={c.id}>
                        <td>
                          <span className={styles.cellPrimary}>#{c.id}</span>
                        </td>
                        <td>
                          <div className={styles.cellPrimary}>{c.nomeFornecedor}</div>
                        </td>
                        <td>{formatDate(c.emissao)}</td>
                        <td>
                          <span
                            className={`${styles.statusBadge} ${
                              c.statusCompra === 'PAGAMENTO'
                                ? styles.badgeSuccess
                                : c.statusCompra === 'RECEBIMENTO'
                                ? styles.badgePrimary
                                : styles.badgeWarning
                            }`}
                          >
                            {c.statusCompra}
                          </span>
                        </td>
                        <td style={{ textAlign: 'right', fontWeight: 700 }}>
                          {formatCurrency(c.totalCompra)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )
            )}
          </div>

          <div className={styles.sectionFooter}>
            <Link to={tabOperacoes === 'VENDAS' ? '/vendas' : '/compras'}>
              Ver todas as {tabOperacoes === 'VENDAS' ? 'vendas' : 'compras'} &rarr;
            </Link>
          </div>
        </div>

        {/* Coluna 2: Próximos Vencimentos Financeiros */}
        <div className={styles.sectionCard}>
          <div className={styles.sectionHeader}>
            <div className={styles.sectionTitle}>
              <CircleDollarSign size={18} color="var(--color-primary)" />
              <span>Próximos Vencimentos Financeiros</span>
            </div>
            <Link to="/financeiro" className={styles.btnAction} style={{ fontSize: '0.78rem', color: 'var(--color-primary)' }}>
              Ver todos <ChevronRight size={14} />
            </Link>
          </div>

          <div className={styles.tableResponsive}>
            {d.proximosTitulos.length === 0 ? (
              <div className={styles.emptyState}>
                <CircleDollarSign size={32} color="var(--text-subtle)" />
                <p>Nenhum título pendente no momento.</p>
              </div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Tipo</th>
                    <th>Descrição</th>
                    <th>Vencimento</th>
                    <th style={{ textAlign: 'right' }}>Valor</th>
                  </tr>
                </thead>
                <tbody>
                  {d.proximosTitulos.map((t) => {
                    const isReceita = t.tipo === 'RECEITA';
                    return (
                      <tr key={t.id}>
                        <td>
                          {isReceita ? (
                            <span className={styles.tipoReceita}>
                              <ArrowUpRight size={14} /> Receita
                            </span>
                          ) : (
                            <span className={styles.tipoDespesa}>
                              <ArrowDownLeft size={14} /> Despesa
                            </span>
                          )}
                        </td>
                        <td>
                          <div className={styles.cellPrimary}>{t.descricao}</div>
                          <div className={styles.cellSubtext}>{t.nomePessoa}</div>
                        </td>
                        <td>
                          <span style={{ fontWeight: 600 }}>{formatDate(t.vencimento)}</span>
                          {t.vencido && (
                            <span
                              className={styles.statusBadge}
                              style={{
                                backgroundColor: '#fee2e2',
                                color: '#991b1b',
                                marginLeft: '0.4rem',
                                fontSize: '0.65rem',
                              }}
                            >
                              Vencido
                            </span>
                          )}
                        </td>
                        <td
                          style={{
                            textAlign: 'right',
                            fontWeight: 700,
                            color: isReceita ? 'var(--color-success-text)' : 'var(--color-danger-text)',
                          }}
                        >
                          {formatCurrency(t.valorTotal)}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            )}
          </div>

          <div className={styles.sectionFooter}>
            <Link to="/financeiro">Acessar módulo Financeiro &rarr;</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
