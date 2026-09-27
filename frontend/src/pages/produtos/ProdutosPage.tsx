import React from 'react';
import { Package, Plus } from 'lucide-react';

export const ProdutosPage: React.FC = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 700 }}>Produtos & Estoque</h1>
          <p style={{ color: 'var(--text-muted)' }}>Gestão do catálogo, controle de estoque e preços</p>
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
          <Plus size={18} /> Novo Produto
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
        <Package size={48} style={{ color: 'var(--text-subtle)' }} />
        <h3>Nenhum produto cadastrado no momento</h3>
        <p style={{ maxWidth: '420px', fontSize: '0.9rem' }}>
          Em breve você poderá cadastrar seus itens, controlar estoque mínimo/máximo e consultar dados direto do Supabase.
        </p>
      </div>
    </div>
  );
};

export default ProdutosPage;
