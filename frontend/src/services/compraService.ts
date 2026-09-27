import api from './api';
import type { CompraDTO, CompraStatus } from '../types/compra';

export const compraService = {
  async listar(search?: string, status?: CompraStatus): Promise<CompraDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (status) params.status = status;

    const response = await api.get<CompraDTO[]>('/compras', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<CompraDTO> {
    const response = await api.get<CompraDTO>(`/compras/${id}`);
    return response.data;
  },

  async criar(compra: CompraDTO): Promise<CompraDTO> {
    const response = await api.post<CompraDTO>('/compras', compra);
    return response.data;
  },

  async alterarStatus(id: number, status: CompraStatus): Promise<CompraDTO> {
    const response = await api.patch<CompraDTO>(`/compras/${id}/status`, null, {
      params: { status },
    });
    return response.data;
  },
};

export default compraService;
