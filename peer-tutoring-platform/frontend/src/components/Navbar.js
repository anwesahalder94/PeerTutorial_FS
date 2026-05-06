import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();
  const [scrolled, setScrolled] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 50);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
    setMobileMenuOpen(false);
  };

  const toggleMobileMenu = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  return (
    <nav className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <div className="nav-brand">
        <Link to="/">PeerTutor</Link>
      </div>

      {/* Mobile Menu Button */}
      <button
        className="mobile-menu-btn"
        onClick={toggleMobileMenu}
        aria-label="Toggle menu"
      >
        <span className={`hamburger ${mobileMenuOpen ? 'open' : ''}`}></span>
      </button>

      {/* Navigation Links */}
      <div className={`nav-links ${mobileMenuOpen ? 'mobile-open' : ''}`}>
        <Link to="/" onClick={() => setMobileMenuOpen(false)}>
          <span className="nav-icon">🏠</span> Home
        </Link>
        <Link to="/tutors" onClick={() => setMobileMenuOpen(false)}>
          <span className="nav-icon">👨‍🏫</span> Tutors
        </Link>

        {user ? (
          <>
            <Link to="/dashboard" onClick={() => setMobileMenuOpen(false)}>
              <span className="nav-icon">📊</span> Dashboard
            </Link>
            <div className="user-menu">
              <span className="user-avatar">
                {user.firstName?.charAt(0).toUpperCase()}
              </span>
              <span className="user-name">{user.firstName}</span>
            </div>
            <button onClick={handleLogout} className="btn-logout">
              <span className="nav-icon">🚪</span> Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-login" onClick={() => setMobileMenuOpen(false)}>
              <span className="nav-icon">🔑</span> Login
            </Link>
            <Link to="/register" className="nav-register" onClick={() => setMobileMenuOpen(false)}>
              <span className="nav-icon">✨</span> Get Started
            </Link>
          </>
        )}
      </div>

      <style>{`
        .mobile-menu-btn {
          display: none;
          background: transparent;
          border: none;
          cursor: pointer;
          padding: 0.5rem;
          z-index: 1001;
        }

        .hamburger {
          display: block;
          width: 25px;
          height: 2px;
          background: var(--text-primary);
          position: relative;
          transition: all 0.3s ease;
        }

        .hamburger::before,
        .hamburger::after {
          content: '';
          position: absolute;
          width: 25px;
          height: 2px;
          background: var(--text-primary);
          transition: all 0.3s ease;
        }

        .hamburger::before {
          top: -8px;
        }

        .hamburger::after {
          top: 8px;
        }

        .hamburger.open {
          background: transparent;
        }

        .hamburger.open::before {
          transform: rotate(45deg);
          top: 0;
        }

        .hamburger.open::after {
          transform: rotate(-45deg);
          top: 0;
        }

        .nav-icon {
          margin-right: 0.3rem;
          font-size: 1.1rem;
        }

        .user-menu {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          padding: 0.5rem 1rem;
          background: var(--bg-glass);
          border-radius: var(--radius-md);
          border: 1px solid var(--border-glass);
        }

        .user-avatar {
          width: 32px;
          height: 32px;
          background: var(--primary-gradient);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 700;
          font-size: 0.9rem;
          color: white;
        }

        .nav-login {
          color: var(--text-secondary) !important;
        }

        .nav-login:hover {
          color: var(--text-primary) !important;
        }

        .nav-register {
          background: var(--primary-gradient) !important;
          color: white !important;
          border-radius: var(--radius-md) !important;
        }

        .nav-register:hover {
          transform: translateY(-2px);
          box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
        }

        @media (max-width: 768px) {
          .mobile-menu-btn {
            display: block;
          }

          .nav-links {
            position: fixed;
            top: 70px;
            left: 0;
            right: 0;
            background: rgba(15, 23, 42, 0.98);
            backdrop-filter: blur(20px);
            flex-direction: column;
            padding: 2rem;
            gap: 1rem;
            transform: translateY(-150%);
            transition: transform 0.4s ease;
            border-bottom: 1px solid var(--border-glass);
          }

          .nav-links.mobile-open {
            transform: translateY(0);
          }

          .nav-links a,
          .nav-links button {
            width: 100%;
            justify-content: center;
          }

          .user-menu {
            justify-content: center;
          }
        }
      `}</style>
    </nav>
  );
};

export default Navbar;
