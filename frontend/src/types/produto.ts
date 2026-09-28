export type ProdutoStatus = 'ATIVO' | 'INATIVO';
export type ProdutoTipo = 'PRODUTO' | 'SERVICO' | 'INSUMO' | string;
export type ProdutoEstoque = 'SIM' | 'NAO';

export interface ProdutoDTO {
  id?: number;
  nomeProduto: string;
  codigoProduto: string;
  precoCusto: number;
  precoVenda: number;
  estoqueMinimo: number;
  estoqueMaximo: number;
  estoqueReservado?: number;
  estoqueAtual: number;
  estoqueDisponivel?: number;
  tipoProduto: ProdutoTipo;
  unidadeMedida?: string;
  categoria?: string;
  subcategoria?: string;
  classe?: string;
  modelo?: string;
  milimetro?: number;
  medida?: number;
  marca?: string;
  controlaEstoque?: ProdutoEstoque;
  statusProduto?: ProdutoStatus;
  fracionar?: string;
  margemLucro?: number;
  alertaEstoque?: 'CRITICO' | 'NORMAL' | 'EXCESSIVO';
}
