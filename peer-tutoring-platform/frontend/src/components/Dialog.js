import React from 'react';

const Dialog = ({ isOpen, onClose, title, message, type = 'info' }) => {
  if (!isOpen) return null;

  const config = {
    info: {
      icon: '💬',
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      bgGlow: 'rgba(102, 126, 234, 0.3)',
      buttonColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    warning: {
      icon: '⚠️',
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      bgGlow: 'rgba(245, 87, 108, 0.3)',
      buttonColor: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
    },
    error: {
      icon: '🚫',
      gradient: 'linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%)',
      bgGlow: 'rgba(238, 90, 90, 0.3)',
      buttonColor: 'linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%)'
    },
    success: {
      icon: '🎉',
      gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
      bgGlow: 'rgba(67, 233, 123, 0.3)',
      buttonColor: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
    }
  };

  const currentConfig = config[type] || config.info;

  return (
    <div className="dialog-overlay" onClick={onClose}>
      <div className="dialog-content" onClick={(e) => e.stopPropagation()}>
        <button className="dialog-close-x" onClick={onClose}>×</button>

        <div className="dialog-icon-wrapper" style={{ background: currentConfig.bgGlow }}>
          <div className="dialog-icon">{currentConfig.icon}</div>
        </div>

        <h3 className="dialog-title">{title}</h3>
        <p className="dialog-message">{message}</p>

        <button
          className="dialog-button"
          onClick={onClose}
          style={{ background: currentConfig.buttonColor }}
        >
          Got it
        </button>
      </div>

      <style>{`
        .dialog-overlay {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.7);
          backdrop-filter: blur(8px);
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 10000;
          animation: fadeIn 0.25s ease-out;
          padding: 20px;
        }

        @keyframes fadeIn {
          from { opacity: 0; }
          to { opacity: 1; }
        }

        .dialog-content {
          background: linear-gradient(145deg, #1e1e3a 0%, #1a1a2e 100%);
          border: 1px solid rgba(255, 255, 255, 0.1);
          border-radius: 24px;
          padding: 2.5rem;
          max-width: 420px;
          width: 100%;
          text-align: center;
          box-shadow:
            0 25px 50px rgba(0, 0, 0, 0.4),
            0 0 0 1px rgba(255, 255, 255, 0.05);
          animation: slideUp 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
          position: relative;
        }

        @keyframes slideUp {
          from {
            opacity: 0;
            transform: translateY(40px) scale(0.9);
          }
          to {
            opacity: 1;
            transform: translateY(0) scale(1);
          }
        }

        .dialog-close-x {
          position: absolute;
          top: 16px;
          right: 20px;
          background: rgba(255, 255, 255, 0.1);
          border: none;
          color: rgba(255, 255, 255, 0.6);
          font-size: 1.5rem;
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.2s ease;
          padding: 0;
          line-height: 1;
        }

        .dialog-close-x:hover {
          background: rgba(255, 255, 255, 0.2);
          color: white;
          transform: rotate(90deg);
        }

        .dialog-icon-wrapper {
          width: 90px;
          height: 90px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin: 0 auto 1.5rem;
          animation: pulse 2s ease-in-out infinite;
        }

        @keyframes pulse {
          0%, 100% { transform: scale(1); }
          50% { transform: scale(1.05); }
        }

        .dialog-icon {
          font-size: 3rem;
          filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
        }

        .dialog-title {
          font-family: 'Poppins', sans-serif;
          font-size: 1.6rem;
          font-weight: 700;
          margin-bottom: 0.75rem;
          color: #ffffff;
          letter-spacing: -0.5px;
        }

        .dialog-message {
          color: rgba(255, 255, 255, 0.7);
          font-size: 1.05rem;
          line-height: 1.7;
          margin-bottom: 2rem;
        }

        .dialog-button {
          color: white;
          border: none;
          padding: 1rem 3rem;
          border-radius: 14px;
          font-size: 1.05rem;
          font-weight: 600;
          cursor: pointer;
          transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
          font-family: inherit;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
          min-width: 140px;
        }

        .dialog-button:hover {
          transform: translateY(-3px) scale(1.02);
          box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
        }

        .dialog-button:active {
          transform: translateY(-1px) scale(0.98);
        }
      `}</style>
    </div>
  );
};

export default Dialog;
