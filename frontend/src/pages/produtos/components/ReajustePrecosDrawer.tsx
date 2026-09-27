import React, { useState } from 'react';
import { DollarSign, Check, Percent } from 'lucide-react';
import BottomSheetDrawer from '../../../components/common/BottomSheetDrawer';
import type { ProdutoDTO } from '../../../types/produto';
import produtoService from '../../../services/produtoService';
import styles from './ReajustePrecosDrawer.module.css';

interface ReajustePrecosDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  produtos: ProdutoDTO[];
  onPrecosAtualizados: () => void;
}

export const ReajustePrecosDrawer: React.FC<ReajustePrecosDrawerProps> = ({
  isOpen,
  onClose,
  produtos,
  onPrecosAtualizados,
}) => {
  const [tipoOperacao, setTipoOperacao] = useState<'ACRESCIMO' | 'DESCONTO'>('ACRESCIMO');
  const [tipoUnidade, setTipoUnidade] = useState<'PERCENTUAL' | 'VALOR'>('PERCENTUAL');
  const [valor, setValor] = useState<number | ''>(5);
  const [saving, setSaving] = useState(false);

  const handleAplicar = async () => {
    if (!valor || Number(valor) <= 0) {
      alert('Informe um valor de reajuste maior que zero.');
      return;
    }

    const confirmMsg = `Deseja aplicar o ${
      tipoOperacao === 'ACRESCIMO' ? 'acréscimo' : 'desconto'
    } de ${valor}${tipoUnidade === 'PERCENTUAL' ? '%' : ' R$'} para os ${
      produtos.length
    } produtos listados?`;

    if (!window.confirm(confirmMsg)) return;

    try {
      setSaving(true);
      const val = Number(valor);

      for (const p of produtos) {
        if (!p.id) continue;
        const precoAtual = Number(p.precoVenda) || 0;
        let novoPreco = precoAtual;

        if (tipoUnidade === 'PERCENTUAL') {
          const fator = val / 100;
          novoPreco =
            tipoOperacao === 'ACRESCIMO'
              ? precoAtual * (1 + fator)
              : Math.max(0, precoAtual * (1 - fator));
        } else {
          novoPreco =
            tipoOperacao === 'ACRESCIMO'
              ? precoAtual + val
              : Math.max(0, precoAtual - val);
        }

        novoPreco = Math.round(novoPreco * 100) / 100;

        await produtoService.atualizar(p.id, {
          ...p,
          precoVenda: novoPreco,
        });
      }

      alert('Reajuste de preços aplicado com sucesso a todos os produtos!');
      onPrecosAtualizados();
      onClose();
    } catch (err) {
      console.error('Erro ao reajustar preços:', err);
      alert('Ocorreu um erro ao atualizar os preços dos produtos.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <BottomSheetDrawer
      isOpen={isOpen}
      onClose={onClose}
      title="Reajuste de Preços em Lote"
      icon={<DollarSign size={20} color="var(--primary, #10b981)" />}
    >
      <div className={styles.container}>
        <div className={styles.row}>
          <div className={styles.controlGroup}>
            <label className={styles.label}>Ação</label>
            <select
              className={styles.select}
              value={tipoOperacao}
              onChange={(e) => setTipoOperacao(e.target.value as 'ACRESCIMO' | 'DESCONTO')}
            >
              <option value="ACRESCIMO">Acréscimo</option>
              <option value="DESCONTO">Desconto</option>
            </select>
          </div>

          <div className={styles.controlGroup}>
            <label className={styles.label}>Valor do Reajuste</label>
            <div className={styles.valueInputGroup}>
              <input
                type="number"
                step="0.01"
                min="0"
                className={`${styles.input} ${styles.valueInput}`}
                value={valor}
                onChange={(e) => setValor(e.target.value ? Number(e.target.value) : '')}
                placeholder="0,00"
              />
              <button
                type="button"
                className={styles.unitToggle}
                onClick={() =>
                  setTipoUnidade((prev) => (prev === 'PERCENTUAL' ? 'VALOR' : 'PERCENTUAL'))
                }
                title="Clique para alternar entre % e R$"
              >
                {tipoUnidade === 'PERCENTUAL' ? <Percent size={14} /> : 'R$'}
              </button>
            </div>
          </div>

          <div className={styles.radioArea}>
            <span className={styles.radioTitle}>Aplicar para:</span>
            <label className={styles.radioOption}>
              <input type="radio" checked readOnly name="aplicarPara" />
              Todos os produtos visíveis na tela ({produtos.length})
            </label>
          </div>
        </div>

        <div className={styles.actions}>
          <button type="button" className={styles.btnSecondary} onClick={onClose} disabled={saving}>
            Cancelar
          </button>
          <button
            type="button"
            className={styles.btnPrimary}
            onClick={handleAplicar}
            disabled={saving}
          >
            <Check size={16} />
            {saving ? 'Aplicando Reajuste...' : 'Atualizar Preços dos Produtos'}
          </button>
        </div>
      </div>
    </BottomSheetDrawer>
  );
};

export default ReajustePrecosDrawer;
