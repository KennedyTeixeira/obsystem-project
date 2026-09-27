import React from 'react';
import { ShoppingBag, Plus } from 'lucide-react';

export const ComprasPage: React.FC = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 700 }}>Compras & Suprimentos</h1>
          <p style={{ color: 'var(--text-muted)' }}>Requisições, cotações e pedidos de compra</p>
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
          <Plus size={18} /> Nova Compra
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
        <ShoppingBag size={48} style={{ color: 'var(--text-subtle)' }} />
        <h3>Gestão de Compras</h3>
        <p style={{ maxWidth: '420px', fontSize: '0.9rem' }}>
          Controle de cotações com fornecedores, entrada de notas e atualização automática do estoque.
        </p>
      </div>
    </div>
  );
};

export default ComprasPage;
