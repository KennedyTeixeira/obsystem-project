import api from './api';
import type {
  TituloDTO,
  TipoTitulo,
  TituloStatus,
  ResumoFinanceiroDTO,
  LiquidarTituloDTO,
} from '../types/financeiro';

export const tituloService = {
  async listar(
    search?: string,
    tipo?: TipoTitulo,
    status?: TituloStatus,
    dataInicio?: string,
    dataFim?: string
  ): Promise<TituloDTO[]> {
    const params: Record<string, string> = {};
    if (search) params.search = search;
    if (tipo) params.tipo = tipo;
    if (status) params.status = status;
    if (dataInicio) params.dataInicio = dataInicio;
    if (dataFim) params.dataFim = dataFim;

    const response = await api.get<TituloDTO[]>('/titulos', { params });
    return response.data;
  },

  async buscarPorId(id: number): Promise<TituloDTO> {
    const response = await api.get<TituloDTO>(`/titulos/${id}`);
    return response.data;
  },

  async obterResumo(): Promise<ResumoFinanceiroDTO> {
    const response = await api.get<ResumoFinanceiroDTO>('/titulos/resumo');
    return response.data;
  },

  async criar(titulo: TituloDTO): Promise<TituloDTO> {
    const response = await api.post<TituloDTO>('/titulos', titulo);
    return response.data;
  },

  async atualizar(id: number, titulo: TituloDTO): Promise<TituloDTO> {
    const response = await api.put<TituloDTO>(`/titulos/${id}`, titulo);
    return response.data;
  },

  async liquidar(id: number, dto?: LiquidarTituloDTO): Promise<TituloDTO> {
    const response = await api.patch<TituloDTO>(`/titulos/${id}/liquidar`, dto || {});
    return response.data;
  },

  async cancelar(id: number): Promise<TituloDTO> {
    const response = await api.patch<TituloDTO>(`/titulos/${id}/cancelar`);
    return response.data;
  },

  async estornar(id: number): Promise<TituloDTO> {
    const response = await api.patch<TituloDTO>(`/titulos/${id}/estornar`);
    return response.data;
  },
};

export default tituloService;
