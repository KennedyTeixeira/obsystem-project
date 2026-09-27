import React, { useEffect } from 'react';
import { X } from 'lucide-react';
import styles from './BottomSheetDrawer.module.css';

interface BottomSheetDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  icon?: React.ReactNode;
  children: React.ReactNode;
  footer?: React.ReactNode;
}

export const BottomSheetDrawer: React.FC<BottomSheetDrawerProps> = ({
  isOpen,
  onClose,
  title,
  icon,
  children,
  footer,
}) => {
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };
    if (isOpen) {
      document.body.style.overflow = 'hidden';
      window.addEventListener('keydown', handleKeyDown);
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [isOpen, onClose]);

  return (
    <div
      className={`${styles.overlay} ${isOpen ? styles.overlayOpen : ''}`}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
      role="dialog"
      aria-modal="true"
    >
      <div className={`${styles.bottomSheet} ${isOpen ? styles.bottomSheetOpen : ''}`}>
        <div className={styles.header}>
          <div className={styles.titleArea}>
            {icon}
            <h2 className={styles.title}>{title}</h2>
          </div>
          <button
            type="button"
            className={styles.btnClose}
            onClick={onClose}
            aria-label="Fechar painel"
          >
            <X size={18} />
          </button>
        </div>

        <div className={styles.body}>{children}</div>

        {footer && <div className={styles.footer}>{footer}</div>}
      </div>
    </div>
  );
};

export default BottomSheetDrawer;
