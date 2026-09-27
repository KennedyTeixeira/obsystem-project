import React from 'react';
import { Menu, User } from 'lucide-react';
import styles from './Header.module.css';

interface HeaderProps {
  onToggleSidebar: () => void;
}

export const Header: React.FC<HeaderProps> = ({ onToggleSidebar }) => {
  return (
    <header className={styles.header}>
      <div className={styles.leftSection}>
        <button
          type="button"
          onClick={onToggleSidebar}
          className={styles.toggleBtn}
          title="Alternar menu lateral"
          aria-label="Alternar menu lateral"
        >
          <Menu size={20} />
        </button>
        <span className={styles.systemBadge}>Obsystem Enterprise</span>
      </div>

      <div className={styles.rightSection}>
        <div className={styles.statusIndicator}>
          <span className={styles.statusDot}></span>
          <span>API Spring Boot: Online</span>
        </div>

        <div className={styles.userProfile}>
          <div className={styles.avatar}>
            <User size={18} />
          </div>
          <span className={styles.userName}>Administrador</span>
        </div>
      </div>
    </header>
  );
};

export default Header;
