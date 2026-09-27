import api from './api';
import type { FornecedorDTO, FornecedorStatus } from '../types/pessoa';

export const fornecedorService = {
  async listar(search?: string, status?: FornecedorStatus): Promise<FornecedorDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (status) params.status = status;

    const response = await api.get<FornecedorDTO[]>('/fornecedores', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<FornecedorDTO> {
    const response = await api.get<FornecedorDTO>(`/fornecedores/${id}`);
    return response.data;
  },

  async criar(fornecedor: FornecedorDTO): Promise<FornecedorDTO> {
    const response = await api.post<FornecedorDTO>('/fornecedores', fornecedor);
    return response.data;
  },

  async atualizar(id: number, fornecedor: FornecedorDTO): Promise<FornecedorDTO> {
    const response = await api.put<FornecedorDTO>(`/fornecedores/${id}`, fornecedor);
    return response.data;
  },

  async inativar(id: number): Promise<void> {
    await api.patch(`/fornecedores/${id}/inativar`);
  },

  async ativar(id: number): Promise<void> {
    await api.patch(`/fornecedores/${id}/ativar`);
  },

  async excluir(id: number): Promise<void> {
    await api.delete(`/fornecedores/${id}`);
  },
};

export default fornecedorService;
