import React from 'react';
import { ShoppingCart, Plus } from 'lucide-react';

export const VendasPage: React.FC = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 700 }}>Vendas & Pedidos</h1>
          <p style={{ color: 'var(--text-muted)' }}>Orçamentos, pedidos comerciais e faturamento</p>
        </div>
        <button
          type="button"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            backgroundColor: 'var(--color-primary)',
            color: '#fff',
            padding: '0.65rem 1.15rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            cursor: 'pointer',
          }}
        >
          <Plus size={18} /> Nova Venda
        </button>
      </div>

      <div
        style={{
          backgroundColor: 'var(--bg-surface)',
          border: '1px solid var(--border-light)',
          borderRadius: 'var(--radius-lg)',
          padding: '2.5rem',
          textAlign: 'center',
          color: 'var(--text-muted)',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: '1rem',
        }}
      >
        <ShoppingCart size={48} style={{ color: 'var(--text-subtle)' }} />
        <h3>Operações de Venda</h3>
        <p style={{ maxWidth: '420px', fontSize: '0.9rem' }}>
          Geração de pedidos com controle de itens, descontos, acréscimos e integração financeira automática.
        </p>
      </div>
    </div>
  );
};

export default VendasPage;
