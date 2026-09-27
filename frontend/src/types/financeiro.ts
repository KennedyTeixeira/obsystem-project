export type TipoTitulo =
  | 'RECEITA'
  | 'DESPESA'
  | 'PREVISAO_RECEITA'
  | 'PREVISAO_DESPESA';

export type TituloStatus =
  | 'PAGO'
  | 'PENDENTE'
  | 'CANCELADO'
  | 'PREVISAO'
  | 'PARCIAL';

export interface TituloDTO {
  id?: number;
  tipo: TipoTitulo;
  descricao: string;
  idPessoa: number;
  nomePessoa?: string;
  documentoPessoa?: string;
  idVenda?: number;
  idCompra?: number;
  idClassificacao?: number;
  nomeClassificacao?: string;
  idPlanoConta?: number;
  nomePlanoConta?: string;
  idCentroCusto?: number;
  nomeCentroCusto?: string;
  emissao: string;
  vencimento: string;
  pagamento?: string;
  valorTotal: number;
  status: TituloStatus;
  vencido?: boolean;
}

export interface ResumoFinanceiroDTO {
  totalReceberPendente: number;
  totalPagarPendente: number;
  saldoPrevisto: number;
  totalRecebidoMes: number;
  totalPagoMes: number;
  totalVencido: number;
}

export interface LiquidarTituloDTO {
  dataPagamento: string;
}

export interface CentroCustoDTO {
  id?: number;
  descricao: string;
  tipo: 'SINTETICO' | 'ANALITICO';
  status: 'ATIVO' | 'INATIVO';
}

export interface PlanoContaDTO {
  id?: number;
  descricao: string;
  tipo: 'RECEITA' | 'DESPESA' | 'ATIVO' | 'PASSIVO';
  status: 'ATIVO' | 'INATIVO';
}

export interface ClassificacaoDTO {
  id?: number;
  descricao: string;
  tipo: string;
  status: 'ATIVO' | 'INATIVO';
}
