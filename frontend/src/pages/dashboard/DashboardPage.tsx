import React from 'react';
import { ShoppingCart, ShoppingBag, ArrowUpRight, ArrowDownRight } from 'lucide-react';
import styles from './DashboardPage.module.css';

export const DashboardPage: React.FC = () => {
  return (
    <div className={styles.dashboard}>
      <div className={styles.welcomeHeader}>
        <h1>Painel Geral</h1>
        <p>Visão consolidada das operações do Obsystem ERP</p>
      </div>

      <div className={styles.metricsGrid}>
        <div className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconSales}`}>
            <ShoppingCart size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Vendas do Mês</span>
            <span className={styles.metricValue}>R$ 0,00</span>
          </div>
        </div>

        <div className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconPurchases}`}>
            <ShoppingBag size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Compras do Mês</span>
            <span className={styles.metricValue}>R$ 0,00</span>
          </div>
        </div>

        <div className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconReceive}`}>
            <ArrowUpRight size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Contas a Receber</span>
            <span className={styles.metricValue}>R$ 0,00</span>
          </div>
        </div>

        <div className={styles.metricCard}>
          <div className={`${styles.iconWrapper} ${styles.iconPay}`}>
            <ArrowDownRight size={24} />
          </div>
          <div className={styles.metricInfo}>
            <span className={styles.metricLabel}>Contas a Pagar</span>
            <span className={styles.metricValue}>R$ 0,00</span>
          </div>
        </div>
      </div>

      <div className={styles.statusCard}>
        <div className={styles.statusCardHeader}>
          <h2>Status do Ecossistema</h2>
        </div>

        <div className={styles.architectureList}>
          <div className={styles.archItem}>
            <span className={styles.archItemTitle}>⚡ Front-end SPA</span>
            <span className={styles.archItemDesc}>React 19 + TypeScript + Vite + CSS Modules</span>
          </div>

          <div className={styles.archItem}>
            <span className={styles.archItemTitle}>☕ Backend API</span>
            <span className={styles.archItemDesc}>Spring Boot 4 + Spring Data JPA (porta 8080)</span>
          </div>

          <div className={styles.archItem}>
            <span className={styles.archItemTitle}>☁️ Banco de Dados</span>
            <span className={styles.archItemDesc}>PostgreSQL 17.6 no Supabase (aws-sa-east-1)</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
