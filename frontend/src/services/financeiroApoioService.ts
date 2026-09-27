import api from './api';
import type {
  CentroCustoDTO,
  PlanoContaDTO,
  ClassificacaoDTO,
} from '../types/financeiro';

export const financeiroApoioService = {
  async listarCentrosCusto(): Promise<CentroCustoDTO[]> {
    const response = await api.get<CentroCustoDTO[]>('/financeiro/centros-custo');
    return response.data;
  },

  async listarPlanosConta(): Promise<PlanoContaDTO[]> {
    const response = await api.get<PlanoContaDTO[]>('/financeiro/planos-conta');
    return response.data;
  },

  async listarClassificacoes(): Promise<ClassificacaoDTO[]> {
    const response = await api.get<ClassificacaoDTO[]>('/financeiro/classificacoes');
    return response.data;
  },
};

export default financeiroApoioService;
