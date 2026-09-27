import type { VendaDTO } from './venda';
import type { CompraDTO } from './compra';
import type { TituloDTO } from './financeiro';

export interface DashboardDTO {
  totalVendasMes: number;
  qtdVendasMes: number;

  totalComprasMes: number;
  qtdComprasMes: number;

  totalReceberPendente: number;
  totalRecebidoMes: number;
  totalPagarPendente: number;
  totalPagoMes: number;
  saldoPrevisto: number;

  totalVencidos: number;
  qtdVencidos: number;

  qtdProdutosEstoqueBaixo: number;
  qtdClientesAtivos: number;
  qtdFornecedoresAtivos: number;

  ultimasVendas: VendaDTO[];
  ultimasCompras: CompraDTO[];
  proximosTitulos: TituloDTO[];
}
