import api from './api';
import type { ProdutoDTO, ProdutoStatus } from '../types/produto';

export const produtoService = {
  async listar(search?: string, status?: ProdutoStatus): Promise<ProdutoDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (status) params.status = status;

    const response = await api.get<ProdutoDTO[]>('/produtos', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<ProdutoDTO> {
    const response = await api.get<ProdutoDTO>(`/produtos/${id}`);
    return response.data;
  },

  async criar(produto: ProdutoDTO): Promise<ProdutoDTO> {
    const response = await api.post<ProdutoDTO>('/produtos', produto);
    return response.data;
  },

  async atualizar(id: number, produto: ProdutoDTO): Promise<ProdutoDTO> {
    const response = await api.put<ProdutoDTO>(`/produtos/${id}`, produto);
    return response.data;
  },

  async inativar(id: number): Promise<void> {
    await api.patch(`/produtos/${id}/inativar`);
  },

  async ativar(id: number): Promise<void> {
    await api.patch(`/produtos/${id}/ativar`);
  },

  async excluir(id: number): Promise<void> {
    await api.delete(`/produtos/${id}`);
  },
};

export default produtoService;
