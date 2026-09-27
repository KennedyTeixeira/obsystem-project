import React, { useEffect, useState, useId } from 'react';
import {
  Users,
  Truck,
  Plus,
  Search,
  Edit2,
  Power,
  X,
  AlertTriangle,
  Loader2,
  Building2,
  User,
} from 'lucide-react';
import clienteService from '../../services/clienteService';
import fornecedorService from '../../services/fornecedorService';
import type {
  ClienteDTO,
  FornecedorDTO,
  ClienteStatus,
  FornecedorStatus,
  TipoPagamento,
  TipoContato,
  Genero,
} from '../../types/pessoa';
import styles from './PessoasPage.module.css';

type ActiveTab = 'CLIENTES' | 'FORNECEDORES';

export const PessoasPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<ActiveTab>('CLIENTES');
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');

  // Listas
  const [clientes, setClientes] = useState<ClienteDTO[]>([]);
  const [fornecedores, setFornecedores] = useState<FornecedorDTO[]>([]);

  // Modal State
  const [modalOpen, setModalOpen] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [saving, setSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  // Form State Unificado
  const [tipoPessoa, setTipoPessoa] = useState<'F' | 'J'>('J');
  const [formNome, setFormNome] = useState('');
  const [formEmail, setFormEmail] = useState('');
  const [formSegmento, setFormSegmento] = useState('');
  const [formSite, setFormSite] = useState('');
  const [formObservacao, setFormObservacao] = useState('');

  // PF
  const [formCpf, setFormCpf] = useState('');
  const [formRg, setFormRg] = useState('');
  const [formGenero, setFormGenero] = useState<Genero>('MASCULINO');

  // PJ
  const [formCnpj, setFormCnpj] = useState('');
  const [formFantasia, setFormFantasia] = useState('');
  const [formIe, setFormIe] = useState('');
  const [formIm, setFormIm] = useState('');

  // Cliente params
  const [formStatusCliente, setFormStatusCliente] = useState<ClienteStatus>('ATIVO');
  const [formLimite, setFormLimite] = useState<number>(0);
  const [formSaldo, setFormSaldo] = useState<number>(0);

  // Fornecedor params
  const [formStatusFornecedor, setFormStatusFornecedor] = useState<FornecedorStatus>('ATIVO');
  const [formCredito, setFormCredito] = useState<number>(0);
  const [formPagamento, setFormPagamento] = useState<TipoPagamento>('PIX');

  // Endereço e Contato
  const [formCep, setFormCep] = useState('');
  const [formEndereco, setFormEndereco] = useState('');
  const [formNumero, setFormNumero] = useState('');
  const [formComplemento, setFormComplemento] = useState('');
  const [formBairro, setFormBairro] = useState('');
  const [formCidade, setFormCidade] = useState('');
  const [formUf, setFormUf] = useState('SP');
  const [formTelefone, setFormTelefone] = useState('');
  const [formTipoContato, setFormTipoContato] = useState<TipoContato>('WHATSAPP');

  // IDs para acessibilidade
  const searchInputId = useId();
  const statusFilterId = useId();
  const formNomeId = useId();
  const formEmailId = useId();
  const formSegmentoId = useId();
  const formCpfId = useId();
  const formRgId = useId();
  const formGeneroId = useId();
  const formCnpjId = useId();
  const formFantasiaId = useId();
  const formIeId = useId();
  const formImId = useId();
  const formLimiteId = useId();
  const formStatusClienteId = useId();
  const formPagamentoId = useId();
  const formCreditoId = useId();
  const formStatusFornecedorId = useId();
  const formCepId = useId();
  const formEnderecoId = useId();
  const formNumeroId = useId();
  const formBairroId = useId();
  const formCidadeId = useId();
  const formUfId = useId();
  const formTelefoneId = useId();
  const formTipoContatoId = useId();

  const carregarDados = async () => {
    try {
      setLoading(true);
      if (activeTab === 'CLIENTES') {
        const data = await clienteService.listar(
          search,
          statusFilter ? (statusFilter as ClienteStatus) : undefined
        );
        setClientes(data);
      } else {
        const data = await fornecedorService.listar(
          search,
          statusFilter ? (statusFilter as FornecedorStatus) : undefined
        );
        setFornecedores(data);
      }
    } catch (err) {
      console.error('Erro ao carregar dados:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregarDados();
  }, [activeTab, search, statusFilter]);

  const limparFormulario = () => {
    setEditingId(null);
    setErrorMessage(null);
    setTipoPessoa('J');
    setFormNome('');
    setFormEmail('');
    setFormSegmento('');
    setFormSite('');
    setFormObservacao('');
    setFormCpf('');
    setFormRg('');
    setFormGenero('MASCULINO');
    setFormCnpj('');
    setFormFantasia('');
    setFormIe('');
    setFormIm('');
    setFormStatusCliente('ATIVO');
    setFormLimite(0);
    setFormSaldo(0);
    setFormStatusFornecedor('ATIVO');
    setFormCredito(0);
    setFormPagamento('PIX');
    setFormCep('');
    setFormEndereco('');
    setFormNumero('');
    setFormComplemento('');
    setFormBairro('');
    setFormCidade('');
    setFormUf('SP');
    setFormTelefone('');
    setFormTipoContato('WHATSAPP');
  };

  const abrirModalNovo = () => {
    limparFormulario();
    setModalOpen(true);
  };

  const abrirModalEditarCliente = (c: ClienteDTO) => {
    limparFormulario();
    setEditingId(c.id || null);
    setTipoPessoa(c.tipoPessoa || 'F');
    setFormNome(c.nome || '');
    setFormEmail(c.email || '');
    setFormSegmento(c.segmento || '');
    setFormSite(c.site || '');
    setFormObservacao(c.observacao || '');
    setFormCpf(c.cpf || '');
    setFormRg(c.rg || '');
    setFormGenero(c.genero || 'MASCULINO');
    setFormCnpj(c.cnpj || '');
    setFormFantasia(c.fantasia || '');
    setFormIe(c.ie || '');
    setFormIm(c.im || '');
    setFormStatusCliente(c.status || 'ATIVO');
    setFormLimite(c.limite || 0);
    setFormSaldo(c.saldo || 0);
    setModalOpen(true);
  };

  const abrirModalEditarFornecedor = (f: FornecedorDTO) => {
    limparFormulario();
    setEditingId(f.id || null);
    setTipoPessoa(f.tipoPessoa || 'J');
    setFormNome(f.nome || '');
    setFormEmail(f.email || '');
    setFormSegmento(f.segmento || '');
    setFormSite(f.site || '');
    setFormObservacao(f.observacao || '');
    setFormCpf(f.cpf || '');
    setFormRg(f.rg || '');
    setFormGenero(f.genero || 'MASCULINO');
    setFormCnpj(f.cnpj || '');
    setFormFantasia(f.fantasia || '');
    setFormIe(f.ie || '');
    setFormIm(f.im || '');
    setFormStatusFornecedor(f.status || 'ATIVO');
    setFormCredito(f.credito || 0);
    setFormPagamento(f.pagamento || 'PIX');
    setModalOpen(true);
  };

  const fecharModal = () => {
    setModalOpen(false);
    limparFormulario();
  };

  const handleSalvar = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setErrorMessage(null);

    try {
      if (activeTab === 'CLIENTES') {
        const payload: ClienteDTO = {
          nome: formNome,
          tipoPessoa: tipoPessoa,
          email: formEmail,
          segmento: formSegmento,
          site: formSite,
          observacao: formObservacao,
          cpf: tipoPessoa === 'F' ? formCpf : undefined,
          rg: tipoPessoa === 'F' ? formRg : undefined,
          genero: tipoPessoa === 'F' ? formGenero : undefined,
          cnpj: tipoPessoa === 'J' ? formCnpj : undefined,
          fantasia: tipoPessoa === 'J' ? formFantasia : undefined,
          ie: tipoPessoa === 'J' ? formIe : undefined,
          im: tipoPessoa === 'J' ? formIm : undefined,
          status: formStatusCliente,
          limite: Number(formLimite) || 0,
          saldo: Number(formSaldo) || 0,
          cep: formCep,
          endereco: formEndereco,
          numero: formNumero,
          complemento: formComplemento,
          bairro: formBairro,
          cidade: formCidade,
          uf: formUf,
          telefone: formTelefone,
          tipoContato: formTipoContato,
        };

        if (editingId) {
          await clienteService.atualizar(editingId, payload);
        } else {
          await clienteService.criar(payload);
        }
      } else {
        const payload: FornecedorDTO = {
          nome: formNome,
          tipoPessoa: tipoPessoa,
          email: formEmail,
          segmento: formSegmento,
          site: formSite,
          observacao: formObservacao,
          cpf: tipoPessoa === 'F' ? formCpf : undefined,
          rg: tipoPessoa === 'F' ? formRg : undefined,
          genero: tipoPessoa === 'F' ? formGenero : undefined,
          cnpj: tipoPessoa === 'J' ? formCnpj : undefined,
          fantasia: tipoPessoa === 'J' ? formFantasia : undefined,
          ie: tipoPessoa === 'J' ? formIe : undefined,
          im: tipoPessoa === 'J' ? formIm : undefined,
          status: formStatusFornecedor,
          credito: Number(formCredito) || 0,
          pagamento: formPagamento,
          cep: formCep,
          endereco: formEndereco,
          numero: formNumero,
          complemento: formComplemento,
          bairro: formBairro,
          cidade: formCidade,
          uf: formUf,
          telefone: formTelefone,
          tipoContato: formTipoContato,
        };

        if (editingId) {
          await fornecedorService.atualizar(editingId, payload);
        } else {
          await fornecedorService.criar(payload);
        }
      }

      fecharModal();
      await carregarDados();
    } catch (err: unknown) {
      const errorObj = err as { response?: { data?: { message?: string } } };
      const msg = errorObj.response?.data?.message || 'Erro ao salvar cadastro. Verifique os dados.';
      setErrorMessage(msg);
    } finally {
      setSaving(false);
    }
  };

  const alternarStatusCliente = async (c: ClienteDTO) => {
    if (!c.id) return;
    try {
      if (c.status === 'ATIVO') {
        await clienteService.inativar(c.id);
      } else {
        await clienteService.ativar(c.id);
      }
      await carregarDados();
    } catch (err) {
      console.error('Erro ao alterar status:', err);
    }
  };

  const alternarStatusFornecedor = async (f: FornecedorDTO) => {
    if (!f.id) return;
    try {
      if (f.status === 'ATIVO') {
        await fornecedorService.inativar(f.id);
      } else {
        await fornecedorService.ativar(f.id);
      }
      await carregarDados();
    } catch (err) {
      console.error('Erro ao alterar status:', err);
    }
  };

  const formatarMoeda = (valor?: number) => {
    return (valor ?? 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  };

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.titleArea}>
          <h1>Pessoas & Atores</h1>
          <p>Gestão unificada de Clientes e Fornecedores (PF e PJ)</p>
        </div>
        <button type="button" onClick={abrirModalNovo} className={styles.btnPrimary}>
          <Plus size={18} /> {activeTab === 'CLIENTES' ? 'Novo Cliente' : 'Novo Fornecedor'}
        </button>
      </div>

      {/* Abas */}
      <div className={styles.tabsContainer}>
        <button
          type="button"
          onClick={() => { setActiveTab('CLIENTES'); setSearch(''); setStatusFilter(''); }}
          className={`${styles.tabBtn} ${activeTab === 'CLIENTES' ? styles.active : ''}`}
        >
          <Users size={18} /> Clientes
        </button>
        <button
          type="button"
          onClick={() => { setActiveTab('FORNECEDORES'); setSearch(''); setStatusFilter(''); }}
          className={`${styles.tabBtn} ${activeTab === 'FORNECEDORES' ? styles.active : ''}`}
        >
          <Truck size={18} /> Fornecedores
        </button>
      </div>

      {/* Filtros */}
      <div className={styles.filterCard}>
        <div className={styles.searchBox}>
          <Search size={18} color="var(--text-muted)" />
          <label htmlFor={searchInputId} style={{ display: 'none' }}>Buscar por nome ou razão social...</label>
          <input
            id={searchInputId}
            type="text"
            placeholder={activeTab === 'CLIENTES' ? 'Buscar cliente por nome...' : 'Buscar fornecedor por razão social...'}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className={styles.statusFilter}>
          <label htmlFor={statusFilterId} style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-muted)' }}>Status:</label>
          <select
            id={statusFilterId}
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">Todos</option>
            <option value="ATIVO">Somente Ativos</option>
            <option value="INATIVO">Somente Inativos</option>
            <option value="BLOQUEADO">Bloqueados</option>
          </select>
        </div>
      </div>

      {/* Tabela */}
      <div className={styles.tableCard}>
        {loading ? (
          <div className={styles.emptyState}>
            <Loader2 size={36} style={{ animation: 'spin 1s linear infinite' }} />
            <p>Carregando {activeTab === 'CLIENTES' ? 'clientes' : 'fornecedores'} do Supabase...</p>
          </div>
        ) : activeTab === 'CLIENTES' ? (
          clientes.length === 0 ? (
            <div className={styles.emptyState}>
              <Users size={48} style={{ color: 'var(--text-subtle)' }} />
              <h3>Nenhum cliente cadastrado</h3>
              <p>Clique no botão "+ Novo Cliente" acima para cadastrar seu primeiro cliente.</p>
            </div>
          ) : (
            <div className={styles.tableResponsive}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Tipo</th>
                    <th>Nome / Razão Social</th>
                    <th>Documento</th>
                    <th>E-mail</th>
                    <th>Segmento</th>
                    <th>Limite de Crédito</th>
                    <th>Saldo</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {clientes.map((c) => (
                    <tr key={c.id}>
                      <td>
                        {c.tipoPessoa === 'F' ? (
                          <span className={styles.badgePf}>PF</span>
                        ) : (
                          <span className={styles.badgePj}>PJ</span>
                        )}
                      </td>
                      <td>
                        <strong>{c.nome}</strong>
                        {c.fantasia && c.fantasia !== c.nome && (
                          <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{c.fantasia}</div>
                        )}
                      </td>
                      <td>{c.tipoPessoa === 'F' ? c.cpf || '-' : c.cnpj || '-'}</td>
                      <td>{c.email || '-'}</td>
                      <td>{c.segmento || '-'}</td>
                      <td><strong>{formatarMoeda(c.limite)}</strong></td>
                      <td>{formatarMoeda(c.saldo)}</td>
                      <td>
                        {c.status === 'ATIVO' && <span className={styles.badgeStatusAtivo}>● Ativo</span>}
                        {c.status === 'INATIVO' && <span className={styles.badgeStatusInativo}>○ Inativo</span>}
                        {c.status === 'BLOQUEADO' && <span className={styles.badgeStatusBloqueado}>✕ Bloqueado</span>}
                      </td>
                      <td>
                        <div className={styles.actionsCell}>
                          <button
                            type="button"
                            onClick={() => abrirModalEditarCliente(c)}
                            className={styles.actionBtn}
                            title="Editar Cliente"
                          >
                            <Edit2 size={16} />
                          </button>
                          <button
                            type="button"
                            onClick={() => alternarStatusCliente(c)}
                            className={styles.actionBtn}
                            title={c.status === 'ATIVO' ? 'Inativar Cliente' : 'Ativar Cliente'}
                          >
                            <Power
                              size={16}
                              color={c.status === 'ATIVO' ? 'var(--color-danger)' : 'var(--color-success)'}
                            />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )
        ) : (
          fornecedores.length === 0 ? (
            <div className={styles.emptyState}>
              <Truck size={48} style={{ color: 'var(--text-subtle)' }} />
              <h3>Nenhum fornecedor cadastrado</h3>
              <p>Clique no botão "+ Novo Fornecedor" acima para cadastrar seu primeiro parceiro.</p>
            </div>
          ) : (
            <div className={styles.tableResponsive}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Tipo</th>
                    <th>Razão Social / Nome</th>
                    <th>Documento</th>
                    <th>E-mail</th>
                    <th>Forma Pagamento</th>
                    <th>Crédito Disponível</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {fornecedores.map((f) => (
                    <tr key={f.id}>
                      <td>
                        {f.tipoPessoa === 'F' ? (
                          <span className={styles.badgePf}>PF</span>
                        ) : (
                          <span className={styles.badgePj}>PJ</span>
                        )}
                      </td>
                      <td>
                        <strong>{f.nome}</strong>
                        {f.fantasia && f.fantasia !== f.nome && (
                          <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{f.fantasia}</div>
                        )}
                      </td>
                      <td>{f.tipoPessoa === 'F' ? f.cpf || '-' : f.cnpj || '-'}</td>
                      <td>{f.email || '-'}</td>
                      <td><span style={{ fontWeight: 600 }}>{f.pagamento || 'PIX'}</span></td>
                      <td><strong>{formatarMoeda(f.credito)}</strong></td>
                      <td>
                        {f.status === 'ATIVO' && <span className={styles.badgeStatusAtivo}>● Ativo</span>}
                        {f.status === 'INATIVO' && <span className={styles.badgeStatusInativo}>○ Inativo</span>}
                        {f.status === 'BLOQUEADO' && <span className={styles.badgeStatusBloqueado}>✕ Bloqueado</span>}
                      </td>
                      <td>
                        <div className={styles.actionsCell}>
                          <button
                            type="button"
                            onClick={() => abrirModalEditarFornecedor(f)}
                            className={styles.actionBtn}
                            title="Editar Fornecedor"
                          >
                            <Edit2 size={16} />
                          </button>
                          <button
                            type="button"
                            onClick={() => alternarStatusFornecedor(f)}
                            className={styles.actionBtn}
                            title={f.status === 'ATIVO' ? 'Inativar Fornecedor' : 'Ativar Fornecedor'}
                          >
                            <Power
                              size={16}
                              color={f.status === 'ATIVO' ? 'var(--color-danger)' : 'var(--color-success)'}
                            />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )
        )}
      </div>

      {/* Modal Unificado de Cadastro e Edição */}
      {modalOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <div className={styles.modalHeader}>
              <h2>
                {editingId
                  ? `Editar ${activeTab === 'CLIENTES' ? 'Cliente' : 'Fornecedor'}`
                  : `Novo ${activeTab === 'CLIENTES' ? 'Cliente' : 'Fornecedor'}`}
              </h2>
              <button type="button" onClick={fecharModal} className={styles.closeBtn}>
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSalvar} style={{ display: 'flex', flexDirection: 'column', flex: 1, minHeight: 0 }}>
              <div className={styles.modalBody}>
                {errorMessage && (
                  <div className={styles.alertBox}>
                    <AlertTriangle size={18} style={{ display: 'inline', marginRight: '6px', verticalAlign: 'text-bottom' }} />
                    {errorMessage}
                  </div>
                )}

                {/* Seletor de Tipo PF / PJ */}
                <div className={styles.typeSelector}>
                  <button
                    type="button"
                    onClick={() => setTipoPessoa('J')}
                    className={`${styles.typeBtn} ${tipoPessoa === 'J' ? styles.active : ''}`}
                  >
                    <Building2 size={18} style={{ display: 'inline', marginRight: '6px', verticalAlign: 'middle' }} />
                    Pessoa Jurídica (CNPJ)
                  </button>
                  <button
                    type="button"
                    onClick={() => setTipoPessoa('F')}
                    className={`${styles.typeBtn} ${tipoPessoa === 'F' ? styles.active : ''}`}
                  >
                    <User size={18} style={{ display: 'inline', marginRight: '6px', verticalAlign: 'middle' }} />
                    Pessoa Física (CPF)
                  </button>
                </div>

                {/* Seção 1: Identificação */}
                <div>
                  <h3 className={styles.formSectionTitle}>1. Identificação Principal</h3>
                  <div className={styles.formGrid2}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formNomeId}>{tipoPessoa === 'J' ? 'Razão Social *' : 'Nome Completo *'}</label>
                      <input
                        id={formNomeId}
                        type="text"
                        required
                        placeholder={tipoPessoa === 'J' ? 'Ex: Vidraçaria Cristalina Ltda' : 'Ex: Kennedy Teixeira'}
                        value={formNome}
                        onChange={(e) => setFormNome(e.target.value)}
                      />
                    </div>

                    {tipoPessoa === 'J' ? (
                      <div className={styles.formGroup}>
                        <label htmlFor={formCnpjId}>CNPJ *</label>
                        <input
                          id={formCnpjId}
                          type="text"
                          required
                          placeholder="Ex: 00.000.000/0001-00"
                          value={formCnpj}
                          onChange={(e) => setFormCnpj(e.target.value)}
                        />
                      </div>
                    ) : (
                      <div className={styles.formGroup}>
                        <label htmlFor={formCpfId}>CPF *</label>
                        <input
                          id={formCpfId}
                          type="text"
                          required
                          placeholder="Ex: 000.000.000-00"
                          value={formCpf}
                          onChange={(e) => setFormCpf(e.target.value)}
                        />
                      </div>
                    )}
                  </div>

                  {tipoPessoa === 'J' ? (
                    <div className={styles.formGrid3} style={{ marginTop: '0.85rem' }}>
                      <div className={styles.formGroup}>
                        <label htmlFor={formFantasiaId}>Nome Fantasia</label>
                        <input
                          id={formFantasiaId}
                          type="text"
                          placeholder="Ex: Vidros Cristal"
                          value={formFantasia}
                          onChange={(e) => setFormFantasia(e.target.value)}
                        />
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formIeId}>Inscrição Estadual (IE)</label>
                        <input
                          id={formIeId}
                          type="text"
                          placeholder="Ex: 123.456.789.000"
                          value={formIe}
                          onChange={(e) => setFormIe(e.target.value)}
                        />
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formImId}>Inscrição Municipal (IM)</label>
                        <input
                          id={formImId}
                          type="text"
                          placeholder="Ex: 98765"
                          value={formIm}
                          onChange={(e) => setFormIm(e.target.value)}
                        />
                      </div>
                    </div>
                  ) : (
                    <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                      <div className={styles.formGroup}>
                        <label htmlFor={formRgId}>RG</label>
                        <input
                          id={formRgId}
                          type="text"
                          placeholder="Ex: 12.345.678-9"
                          value={formRg}
                          onChange={(e) => setFormRg(e.target.value)}
                        />
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formGeneroId}>Gênero</label>
                        <select
                          id={formGeneroId}
                          value={formGenero}
                          onChange={(e) => setFormGenero(e.target.value as Genero)}
                        >
                          <option value="MASCULINO">Masculino</option>
                          <option value="FEMININO">Feminino</option>
                          <option value="OUTRO">Outro</option>
                        </select>
                      </div>
                    </div>
                  )}

                  <div className={styles.formGrid2} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formEmailId}>E-mail</label>
                      <input
                        id={formEmailId}
                        type="email"
                        placeholder="Ex: contato@empresa.com.br"
                        value={formEmail}
                        onChange={(e) => setFormEmail(e.target.value)}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formSegmentoId}>Segmento</label>
                      <input
                        id={formSegmentoId}
                        type="text"
                        placeholder="Ex: Construção Civil, Vidraçaria, Arquitetura"
                        value={formSegmento}
                        onChange={(e) => setFormSegmento(e.target.value)}
                      />
                    </div>
                  </div>
                </div>

                {/* Seção 2: Contato e Endereço */}
                <div>
                  <h3 className={styles.formSectionTitle}>2. Contato & Localização</h3>
                  <div className={styles.formGrid2}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formTelefoneId}>Telefone / WhatsApp</label>
                      <input
                        id={formTelefoneId}
                        type="text"
                        placeholder="Ex: (11) 98765-4321"
                        value={formTelefone}
                        onChange={(e) => setFormTelefone(e.target.value)}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formTipoContatoId}>Tipo de Contato</label>
                      <select
                        id={formTipoContatoId}
                        value={formTipoContato}
                        onChange={(e) => setFormTipoContato(e.target.value as TipoContato)}
                      >
                        <option value="WHATSAPP">WhatsApp</option>
                        <option value="CELULAR">Celular</option>
                        <option value="TELEFONE_FIXO">Telefone Fixo</option>
                      </select>
                    </div>
                  </div>

                  <div className={styles.formGrid3} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formCepId}>CEP</label>
                      <input
                        id={formCepId}
                        type="text"
                        placeholder="Ex: 01001-000"
                        value={formCep}
                        onChange={(e) => setFormCep(e.target.value)}
                      />
                    </div>
                    <div className={styles.formGroup} style={{ gridColumn: 'span 2' }}>
                      <label htmlFor={formEnderecoId}>Logradouro / Endereço</label>
                      <input
                        id={formEnderecoId}
                        type="text"
                        placeholder="Ex: Av. Paulista"
                        value={formEndereco}
                        onChange={(e) => setFormEndereco(e.target.value)}
                      />
                    </div>
                  </div>

                  <div className={styles.formGrid3} style={{ marginTop: '0.85rem' }}>
                    <div className={styles.formGroup}>
                      <label htmlFor={formNumeroId}>Número</label>
                      <input
                        id={formNumeroId}
                        type="text"
                        placeholder="Ex: 1000"
                        value={formNumero}
                        onChange={(e) => setFormNumero(e.target.value)}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formBairroId}>Bairro</label>
                      <input
                        id={formBairroId}
                        type="text"
                        placeholder="Ex: Bela Vista"
                        value={formBairro}
                        onChange={(e) => setFormBairro(e.target.value)}
                      />
                    </div>
                    <div className={styles.formGroup}>
                      <label htmlFor={formCidadeId}>Cidade / UF</label>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <input
                          id={formCidadeId}
                          type="text"
                          placeholder="Cidade"
                          style={{ flex: 1 }}
                          value={formCidade}
                          onChange={(e) => setFormCidade(e.target.value)}
                        />
                        <input
                          id={formUfId}
                          type="text"
                          maxLength={2}
                          placeholder="UF"
                          style={{ width: '55px', textTransform: 'uppercase' }}
                          value={formUf}
                          onChange={(e) => setFormUf(e.target.value.toUpperCase())}
                        />
                      </div>
                    </div>
                  </div>
                </div>

                {/* Seção 3: Parâmetros Comerciais e Financeiros */}
                <div>
                  <h3 className={styles.formSectionTitle}>
                    3. Parâmetros {activeTab === 'CLIENTES' ? 'do Cliente' : 'do Fornecedor'}
                  </h3>

                  {activeTab === 'CLIENTES' ? (
                    <div className={styles.formGrid2}>
                      <div className={styles.formGroup}>
                        <label htmlFor={formLimiteId}>Limite de Crédito (R$)</label>
                        <input
                          id={formLimiteId}
                          type="number"
                          step="0.01"
                          min="0"
                          value={formLimite}
                          onChange={(e) => setFormLimite(parseFloat(e.target.value) || 0)}
                        />
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formStatusClienteId}>Status do Cliente</label>
                        <select
                          id={formStatusClienteId}
                          value={formStatusCliente}
                          onChange={(e) => setFormStatusCliente(e.target.value as ClienteStatus)}
                        >
                          <option value="ATIVO">Ativo</option>
                          <option value="INATIVO">Inativo</option>
                          <option value="BLOQUEADO">Bloqueado</option>
                          <option value="PENDENTE_PAGAMENTO">Pendente Pagamento</option>
                        </select>
                      </div>
                    </div>
                  ) : (
                    <div className={styles.formGrid3}>
                      <div className={styles.formGroup}>
                        <label htmlFor={formPagamentoId}>Forma de Pagamento Padrão</label>
                        <select
                          id={formPagamentoId}
                          value={formPagamento}
                          onChange={(e) => setFormPagamento(e.target.value as TipoPagamento)}
                        >
                          <option value="PIX">PIX</option>
                          <option value="BOLETO">Boleto Bancário</option>
                          <option value="CARTAO">Cartão de Crédito</option>
                          <option value="DEPOSITO">Depósito / Transferência</option>
                          <option value="DINHEIRO">Dinheiro</option>
                        </select>
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formCreditoId}>Crédito Pré-aprovado (R$)</label>
                        <input
                          id={formCreditoId}
                          type="number"
                          step="0.01"
                          min="0"
                          value={formCredito}
                          onChange={(e) => setFormCredito(parseFloat(e.target.value) || 0)}
                        />
                      </div>
                      <div className={styles.formGroup}>
                        <label htmlFor={formStatusFornecedorId}>Status do Fornecedor</label>
                        <select
                          id={formStatusFornecedorId}
                          value={formStatusFornecedor}
                          onChange={(e) => setFormStatusFornecedor(e.target.value as FornecedorStatus)}
                        >
                          <option value="ATIVO">Ativo</option>
                          <option value="INATIVO">Inativo</option>
                          <option value="BLOQUEADO">Bloqueado</option>
                        </select>
                      </div>
                    </div>
                  )}
                </div>
              </div>

              <div className={styles.modalFooter}>
                <button type="button" onClick={fecharModal} className={styles.btnSecondary} disabled={saving}>
                  Cancelar
                </button>
                <button type="submit" className={styles.btnPrimary} disabled={saving}>
                  {saving ? 'Gravando...' : editingId ? 'Salvar Alterações' : 'Concluir Cadastro'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PessoasPage;
