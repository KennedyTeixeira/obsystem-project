import api from './api';
import type { DashboardDTO } from '../types/dashboard';

export const dashboardService = {
  async obterResumo(): Promise<DashboardDTO> {
    const response = await api.get<DashboardDTO>('/dashboard/resumo');
    return response.data;
  },
};

export default dashboardService;
