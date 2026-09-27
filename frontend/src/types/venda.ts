export type VendaStatus = 'ORCAMENTO' | 'ABERTO' | 'SEPARACAO' | 'FINALIZADO';
export type ItemStatus = 'INCLUSO' | 'CANCELADO' | 'FATURADO';

export interface ItemVendaDTO {
  id?: number;
  idProduto: number;
  nomeProduto?: string;
  codigoProduto?: string;
  unidadeMedida?: string;
  numeroItem?: number;
  quantidadeItem: number;
  precoUnitario?: number;
  valorDesconto?: number;
  valorAcrescimo?: number;
  valorTotal?: number;
  valorCusto?: number;
  statusItem?: ItemStatus;
}

export interface VendaDTO {
  id?: number;
  idCliente: number;
  nomeCliente?: string;
  documentoCliente?: string;
  emissao: string;
  previsao: string;
  statusVenda: VendaStatus;
  totalVenda?: number;
  itens: ItemVendaDTO[];
  gerarTituloFinanceiro?: boolean;
}
