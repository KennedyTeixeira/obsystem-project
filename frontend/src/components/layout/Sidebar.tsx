import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Package,
  Users,
  ShoppingCart,
  ShoppingBag,
  CircleDollarSign,
  Layers,
} from 'lucide-react';
import styles from './Sidebar.module.css';

interface SidebarProps {
  collapsed: boolean;
}

export const Sidebar: React.FC<SidebarProps> = ({ collapsed }) => {
  const menuItems = [
    { to: '/', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/produtos', label: 'Produtos & Estoque', icon: Package },
    { to: '/pessoas', label: 'Pessoas & Atores', icon: Users },
    { to: '/vendas', label: 'Vendas & Pedidos', icon: ShoppingCart },
    { to: '/compras', label: 'Compras & Cotações', icon: ShoppingBag },
    { to: '/financeiro', label: 'Financeiro & Títulos', icon: CircleDollarSign },
  ];

  return (
    <aside className={`${styles.sidebar} ${collapsed ? styles.collapsed : ''}`}>
      <div className={styles.brand}>
        <Layers className={styles.brandIcon} size={28} />
        {!collapsed && <span className={styles.brandTitle}>OBSYSTEM</span>}
      </div>

      <nav className={styles.nav}>
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `${styles.navItem} ${isActive ? styles.active : ''}`
              }
              title={collapsed ? item.label : undefined}
            >
              <Icon className={styles.navIcon} size={20} />
              {!collapsed && <span className={styles.navLabel}>{item.label}</span>}
            </NavLink>
          );
        })}
      </nav>

      <div className={styles.footer}>
        {!collapsed ? 'Obsystem ERP v1.0' : 'v1.0'}
      </div>
    </aside>
  );
};

export default Sidebar;
