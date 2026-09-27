import api from './api';
import type { ClienteDTO, ClienteStatus } from '../types/pessoa';

export const clienteService = {
  async listar(search?: string, status?: ClienteStatus): Promise<ClienteDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (status) params.status = status;

    const response = await api.get<ClienteDTO[]>('/clientes', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<ClienteDTO> {
    const response = await api.get<ClienteDTO>(`/clientes/${id}`);
    return response.data;
  },

  async criar(cliente: ClienteDTO): Promise<ClienteDTO> {
    const response = await api.post<ClienteDTO>('/clientes', cliente);
    return response.data;
  },

  async atualizar(id: number, cliente: ClienteDTO): Promise<ClienteDTO> {
    const response = await api.put<ClienteDTO>(`/clientes/${id}`, cliente);
    return response.data;
  },

  async inativar(id: number): Promise<void> {
    await api.patch(`/clientes/${id}/inativar`);
  },

  async ativar(id: number): Promise<void> {
    await api.patch(`/clientes/${id}/ativar`);
  },

  async excluir(id: number): Promise<void> {
    await api.delete(`/clientes/${id}`);
  },
};

export default clienteService;
