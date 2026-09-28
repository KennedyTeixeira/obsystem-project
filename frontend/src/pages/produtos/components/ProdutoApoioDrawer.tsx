import React, { useState, useEffect } from 'react';
import {
  Plus,
  Trash2,
  Edit2,
  Check,
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
} from 'lucide-react';
import SlideOverDrawer from '../../../components/common/SlideOverDrawer';
import produtoApoioService from '../../../services/produtoApoioService';
import type {
  CategoriaDTO,
  TipoEntidadeApoio,
} from '../../../types/produtoApoio';
import styles from './ProdutoApoioDrawer.module.css';

interface ProdutoApoioDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  tipoEntidade: TipoEntidadeApoio | null;
  onCatalogoUpdated?: () => void;
}

export const ProdutoApoioDrawer: React.FC<ProdutoApoioDrawerProps> = ({
  isOpen,
  onClose,
  tipoEntidade,
  onCatalogoUpdated,
}) => {
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [items, setItems] = useState<unknown[]>([]);
  const [categoriasList, setCategoriasList] = useState<CategoriaDTO[]>([]);

  // Campos de Formulário Genéricos
  const [descricao, setDescricao] = useState('');
  const [nome, setNome] = useState('');
  const [sigla, setSigla] = useState('');
  const [idCategoria, setIdCategoria] = useState<number | ''>('');
  const [espessura, setEspessura] = useState<number | ''>('');
  const [largura, setLargura] = useState<number | ''>('');
  const [altura, setAltura] = useState<number | ''>('');

  const limparFormulario = () => {
    setEditingId(null);
    setDescricao('');
    setNome('');
    setSigla('');
    setIdCategoria('');
    setEspessura('');
    setLargura('');
    setAltura('');
  };

  const handleEditar = (item: any) => {
    setEditingId(item.id);
    if (tipoEntidade === 'tipo_produto') {
      setNome(item.nome || '');
      setDescricao(item.descricao || '');
    } else if (tipoEntidade === 'unidade_medida') {
      setSigla(item.sigla || '');
      setDescricao(item.descricao || '');
    } else if (tipoEntidade === 'subcategoria') {
      setDescricao(item.descricao || '');
      setIdCategoria(item.idCategoria ? Number(item.idCategoria) : '');
    } else if (tipoEntidade === 'medida') {
      setDescricao(item.descricao || '');
      setLargura(item.largura !== undefined && item.largura !== null ? Number(item.largura) : '');
      setAltura(item.altura !== undefined && item.altura !== null ? Number(item.altura) : '');
    } else if (tipoEntidade === 'milimetro') {
      setEspessura(item.espessura !== undefined && item.espessura !== null ? Number(item.espessura) : '');
      setDescricao(item.descricao || '');
    } else {
      setDescricao(item.descricao || '');
    }
  };

  const carregarDados = async () => {
    if (!tipoEntidade) return;
    try {
      setLoading(true);
      if (tipoEntidade === 'tipo_produto') {
        const data = await produtoApoioService.listarTiposProduto();
        setItems(data);
      } else if (tipoEntidade === 'categoria') {
        const data = await produtoApoioService.listarCategorias();
        setItems(data);
      } else if (tipoEntidade === 'subcategoria') {
        const [subCats, cats] = await Promise.all([
          produtoApoioService.listarSubCategorias(),
          produtoApoioService.listarCategorias(),
        ]);
        setItems(subCats);
        setCategoriasList(cats);
      } else if (tipoEntidade === 'classe') {
        const data = await produtoApoioService.listarClasses();
        setItems(data);
      } else if (tipoEntidade === 'modelo') {
        const data = await produtoApoioService.listarModelos();
        setItems(data);
      } else if (tipoEntidade === 'marca') {
        const data = await produtoApoioService.listarMarcas();
        setItems(data);
      } else if (tipoEntidade === 'unidade_medida') {
        const data = await produtoApoioService.listarUnidadesMedida();
        setItems(data);
      } else if (tipoEntidade === 'medida') {
        const data = await produtoApoioService.listarMedidas();
        setItems(data);
      } else if (tipoEntidade === 'milimetro') {
        const data = await produtoApoioService.listarMilimetros();
        setItems(data);
      }
    } catch (err) {
      console.error('Erro ao carregar dados de apoio:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isOpen && tipoEntidade) {
      limparFormulario();
      carregarDados();
    }
  }, [isOpen, tipoEntidade]);

  const handleSalvar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!tipoEntidade) return;

    try {
      setSaving(true);

      if (tipoEntidade === 'tipo_produto') {
        if (!nome.trim()) return;
        await produtoApoioService.salvarTipoProduto({
          id: editingId || undefined,
          nome: nome.trim(),
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'categoria') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarCategoria({
          id: editingId || undefined,
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'subcategoria') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarSubCategoria({
          id: editingId || undefined,
          descricao: descricao.trim(),
          idCategoria: idCategoria ? Number(idCategoria) : undefined,
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'classe') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarClasse({
          id: editingId || undefined,
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'modelo') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarModelo({
          id: editingId || undefined,
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'marca') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarMarca({
          id: editingId || undefined,
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'unidade_medida') {
        if (!sigla.trim() || !descricao.trim()) return;
        await produtoApoioService.salvarUnidadeMedida({
          id: editingId || undefined,
          sigla: sigla.trim().toUpperCase(),
          descricao: descricao.trim(),
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'medida') {
        if (!descricao.trim()) return;
        await produtoApoioService.salvarMedida({
          id: editingId || undefined,
          descricao: descricao.trim(),
          largura: largura ? Number(largura) : undefined,
          altura: altura ? Number(altura) : undefined,
          status: 'ATIVO',
        });
      } else if (tipoEntidade === 'milimetro') {
        if (!espessura) return;
        await produtoApoioService.salvarMilimetro({
          id: editingId || undefined,
          espessura: Number(espessura),
          descricao: descricao.trim() || `${espessura}mm`,
          status: 'ATIVO',
        });
      }

      limparFormulario();
      await carregarDados();
      if (onCatalogoUpdated) {
        onCatalogoUpdated();
      }
    } catch (err) {
      console.error('Erro ao salvar item de apoio:', err);
    } finally {
      setSaving(false);
    }
  };

  const handleExcluir = async (id?: number) => {
    if (!id || !tipoEntidade) return;
    if (!window.confirm('Tem certeza que deseja remover este item?')) return;

    try {
      if (tipoEntidade === 'tipo_produto') await produtoApoioService.excluirTipoProduto(id);
      else if (tipoEntidade === 'categoria') await produtoApoioService.excluirCategoria(id);
      else if (tipoEntidade === 'subcategoria') await produtoApoioService.excluirSubCategoria(id);
      else if (tipoEntidade === 'classe') await produtoApoioService.excluirClasse(id);
      else if (tipoEntidade === 'modelo') await produtoApoioService.excluirModelo(id);
      else if (tipoEntidade === 'marca') await produtoApoioService.excluirMarca(id);
      else if (tipoEntidade === 'unidade_medida') await produtoApoioService.excluirUnidadeMedida(id);
      else if (tipoEntidade === 'medida') await produtoApoioService.excluirMedida(id);
      else if (tipoEntidade === 'milimetro') await produtoApoioService.excluirMilimetro(id);

      await carregarDados();
      if (onCatalogoUpdated) {
        onCatalogoUpdated();
      }
    } catch (err) {
      console.error('Erro ao excluir item de apoio:', err);
      alert('Não foi possível excluir o item pois ele pode estar em uso por outros cadastros.');
    }
  };

  // Informações de Cabeçalho do Tipo
  const obterInfoTipo = () => {
    switch (tipoEntidade) {
      case 'tipo_produto':
        return {
          title: 'Tipos de Produtos',
          subtitle: 'Classificação fiscal e operacional (Mercadoria, Matéria-prima, etc.)',
          icon: <Tag size={20} color="var(--primary, #10b981)" />,
        };
      case 'categoria':
        return {
          title: 'Categorias',
          subtitle: 'Grupos principais para organização do catálogo',
          icon: <FolderTree size={20} color="var(--primary, #10b981)" />,
        };
      case 'subcategoria':
        return {
          title: 'Subcategorias',
          subtitle: 'Subdivisão vinculada às categorias principais',
          icon: <GitBranch size={20} color="var(--primary, #10b981)" />,
        };
      case 'classe':
        return {
          title: 'Classes de Produtos',
          subtitle: 'Classificação de material (Temperado, Laminado, Comum, etc.)',
          icon: <Shield size={20} color="var(--primary, #10b981)" />,
        };
      case 'modelo':
        return {
          title: 'Modelos',
          subtitle: 'Padrões de fabricação e modelos construtivos',
          icon: <Box size={20} color="var(--primary, #10b981)" />,
        };
      case 'marca':
        return {
          title: 'Marcas / Fabricantes',
          subtitle: 'Fornecedores e indústrias fabricantes parceiras',
          icon: <Award size={20} color="var(--primary, #10b981)" />,
        };
      case 'unidade_medida':
        return {
          title: 'Unidades de Medida',
          subtitle: 'Padrões de comercialização e estoque (UN, M2, KG, CX, etc.)',
          icon: <Ruler size={20} color="var(--primary, #10b981)" />,
        };
      case 'medida':
        return {
          title: 'Medidas Padrão',
          subtitle: 'Dimensões nominais de chapas e cortes (Largura x Altura)',
          icon: <Maximize2 size={20} color="var(--primary, #10b981)" />,
        };
      case 'milimetro':
        return {
          title: 'Milímetros / Espessuras',
          subtitle: 'Espessuras nominais padronizadas (ex: 4mm, 6mm, 8mm, 10mm)',
          icon: <Disc size={20} color="var(--primary, #10b981)" />,
        };
      default:
        return {
          title: 'Cadastro de Apoio',
          subtitle: '',
          icon: null,
        };
    }
  };

  const info = obterInfoTipo();

  return (
    <SlideOverDrawer
      isOpen={isOpen}
      onClose={onClose}
      title={info.title}
      subtitle={info.subtitle}
      footer={
        <button type="button" className={styles.btnCancel} onClick={onClose}>
          Fechar
        </button>
      }
    >
      <div className={styles.container}>
        {/* Formulário de Inclusão Rápida / Edição */}
        <form onSubmit={handleSalvar} className={styles.addFormCard}>
          <h3 className={`${styles.addFormTitle} ${editingId ? styles.addFormTitleEditing : ''}`}>
            {editingId ? (
              <>
                <Edit2 size={16} /> Editando Registro #{editingId}
              </>
            ) : (
              <>
                <Plus size={16} /> Novo Registro
              </>
            )}
          </h3>

          <div className={styles.formGrid}>
            {tipoEntidade === 'tipo_produto' && (
              <>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Nome do Tipo *</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Ex: Mercadoria para Revenda"
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    required
                  />
                </div>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Descrição / Finalidade</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Breve descrição"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                  />
                </div>
              </>
            )}

            {tipoEntidade === 'unidade_medida' && (
              <>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Sigla *</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Ex: M2, UN, KG"
                    maxLength={10}
                    value={sigla}
                    onChange={(e) => setSigla(e.target.value.toUpperCase())}
                    required
                  />
                </div>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Descrição *</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Ex: Metro Quadrado"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                    required
                  />
                </div>
              </>
            )}

            {tipoEntidade === 'subcategoria' && (
              <div className={styles.formGroup}>
                <label className={styles.label}>Categoria Vinculada</label>
                <select
                  className={styles.select}
                  value={idCategoria}
                  onChange={(e) => setIdCategoria(e.target.value ? Number(e.target.value) : '')}
                >
                  <option value="">Selecione a categoria...</option>
                  {categoriasList.map((c) => (
                    <option key={c.id} value={c.id}>
                      {c.descricao}
                    </option>
                  ))}
                </select>
              </div>
            )}

            {['categoria', 'subcategoria', 'classe', 'modelo', 'marca'].includes(
              tipoEntidade || ''
            ) && (
              <div className={styles.formGroup} style={{ gridColumn: '1 / -1' }}>
                <label className={styles.label}>Descrição do Item *</label>
                <input
                  type="text"
                  className={styles.input}
                  placeholder="Nome ou descrição oficial"
                  value={descricao}
                  onChange={(e) => setDescricao(e.target.value)}
                  required
                />
              </div>
            )}

            {tipoEntidade === 'milimetro' && (
              <>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Espessura (mm) *</label>
                  <input
                    type="number"
                    step="0.1"
                    className={styles.input}
                    placeholder="Ex: 8.0"
                    value={espessura}
                    onChange={(e) => {
                      const v = e.target.value;
                      setEspessura(v ? Number(v) : '');
                      if (!descricao) setDescricao(v ? `${v}mm` : '');
                    }}
                    required
                  />
                </div>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Rótulo / Descrição</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Ex: 8mm"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                  />
                </div>
              </>
            )}

            {tipoEntidade === 'medida' && (
              <>
                <div className={styles.formGroup} style={{ gridColumn: '1 / -1' }}>
                  <label className={styles.label}>Descrição da Medida *</label>
                  <input
                    type="text"
                    className={styles.input}
                    placeholder="Ex: 3210 x 2400"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                    required
                  />
                </div>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Largura (mm)</label>
                  <input
                    type="number"
                    step="1"
                    className={styles.input}
                    placeholder="Ex: 3210"
                    value={largura}
                    onChange={(e) => setLargura(e.target.value ? Number(e.target.value) : '')}
                  />
                </div>
                <div className={styles.formGroup}>
                  <label className={styles.label}>Altura (mm)</label>
                  <input
                    type="number"
                    step="1"
                    className={styles.input}
                    placeholder="Ex: 2400"
                    value={altura}
                    onChange={(e) => setAltura(e.target.value ? Number(e.target.value) : '')}
                  />
                </div>
              </>
            )}
          </div>

          <div className={styles.formActions}>
            {editingId && (
              <button
                type="button"
                className={styles.btnCancelEdit}
                onClick={limparFormulario}
                disabled={saving}
              >
                Cancelar Edição
              </button>
            )}
            <button type="submit" className={styles.btnAdd} disabled={saving}>
              {saving ? (
                <Loader2 size={16} className="spin" />
              ) : editingId ? (
                <Check size={16} />
              ) : (
                <Plus size={16} />
              )}
              {editingId ? 'Salvar Alterações' : 'Salvar Registro'}
            </button>
          </div>
        </form>

        {/* Lista de Registros Cadastrados */}
        <div className={styles.listHeader}>
          <h4 className={styles.listTitle}>Registros Cadastrados</h4>
          <span className={styles.countBadge}>{items.length} itens</span>
        </div>

        {loading ? (
          <div className={styles.loadingArea}>
            <Loader2 size={24} className="spin" />
            <span>Carregando dados...</span>
          </div>
        ) : items.length === 0 ? (
          <div className={styles.emptyState}>Nenhum registro cadastrado até o momento.</div>
        ) : (
          <div className={styles.itemsList}>
            {items.map((item: any) => (
              <div
                key={item.id}
                className={`${styles.itemCard} ${editingId === item.id ? styles.itemCardEditing : ''}`}
              >
                <div className={styles.itemInfo}>
                  <div className={styles.itemMain}>
                    {item.sigla && <span className={styles.tagSigla}>{item.sigla}</span>}
                    {item.nome || item.descricao}
                  </div>
                  {(item.nomeCategoria || (item.espessura !== undefined && item.espessura !== null) || (item.largura && item.altura)) && (
                    <div className={styles.itemSub}>
                      {item.nomeCategoria && `Categoria: ${item.nomeCategoria}`}
                      {item.espessura !== undefined && item.espessura !== null && `Espessura: ${item.espessura}mm`}
                      {item.largura && item.altura && `Dimensões: ${item.largura} x ${item.altura} mm`}
                    </div>
                  )}
                </div>
                <div className={styles.itemActions}>
                  <button
                    type="button"
                    className={styles.btnEdit}
                    onClick={() => handleEditar(item)}
                    title="Editar registro"
                  >
                    <Edit2 size={15} />
                  </button>
                  <button
                    type="button"
                    className={styles.btnDelete}
                    onClick={() => handleExcluir(item.id)}
                    title="Excluir registro"
                  >
                    <Trash2 size={15} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </SlideOverDrawer>
  );
};

export default ProdutoApoioDrawer;
