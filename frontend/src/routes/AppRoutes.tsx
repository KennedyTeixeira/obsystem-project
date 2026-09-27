import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AppLayout from '../components/layout/AppLayout';
import DashboardPage from '../pages/dashboard/DashboardPage';
import ProdutosPage from '../pages/produtos/ProdutosPage';
import PessoasPage from '../pages/pessoas/PessoasPage';
import VendasPage from '../pages/vendas/VendasPage';
import ComprasPage from '../pages/compras/ComprasPage';
import FinanceiroPage from '../pages/financeiro/FinanceiroPage';

export const AppRoutes: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="produtos" element={<ProdutosPage />} />
          <Route path="pessoas" element={<PessoasPage />} />
          <Route path="vendas" element={<VendasPage />} />
          <Route path="compras" element={<ComprasPage />} />
          <Route path="financeiro" element={<FinanceiroPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
