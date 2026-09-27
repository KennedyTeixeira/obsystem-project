export type CompraStatus =
  | 'REQUISICAO'
  | 'COTACAO'
  | 'NEGOCIACAO'
  | 'PEDIDO'
  | 'RECEBIMENTO'
  | 'PAGAMENTO';

export type ItemCompraStatus = 'INCLUSO' | 'CANCELADO' | 'FATURADO';

export interface ItemCompraDTO {
  id?: number;
  idProduto: number;
  nomeProduto?: string;
  codigoProduto?: string;
  unidadeMedida?: string;
  numeroItem?: number;
  quantidadeItem: number;
  precoUnitario?: number;
  valorDesconto?: number;
  valorDespesa?: number;
  valorTotal?: number;
  statusItem?: ItemCompraStatus;
}

export interface CompraDTO {
  id?: number;
  idFornecedor: number;
  nomeFornecedor?: string;
  documentoFornecedor?: string;
  emissao: string;
  previsao: string;
  statusCompra: CompraStatus;
  totalCompra?: number;
  itens: ItemCompraDTO[];
  gerarTituloFinanceiro?: boolean;
}
