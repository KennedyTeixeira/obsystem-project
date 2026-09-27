import api from './api';
import type { VendaDTO, VendaStatus } from '../types/venda';

export const vendaService = {
  async listar(search?: string, status?: VendaStatus): Promise<VendaDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (status) params.status = status;

    const response = await api.get<VendaDTO[]>('/vendas', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<VendaDTO> {
    const response = await api.get<VendaDTO>(`/vendas/${id}`);
    return response.data;
  },

  async criar(venda: VendaDTO): Promise<VendaDTO> {
    const response = await api.post<VendaDTO>('/vendas', venda);
    return response.data;
  },

  async alterarStatus(id: number, status: VendaStatus): Promise<VendaDTO> {
    const response = await api.patch<VendaDTO>(`/vendas/${id}/status`, null, {
      params: { status },
    });
    return response.data;
  },
};

export default vendaService;
