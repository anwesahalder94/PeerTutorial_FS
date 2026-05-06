import React, { useState, useEffect } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login, initiateOAuthLogin } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Check for session expired error in URL
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    if (params.get('error') === 'session_expired') {
      setError('⚠️ Your session has expired. Please log in again.');
    }
  }, [location]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleLogin = () => {
    initiateOAuthLogin('google');
  };

  const handleGithubLogin = () => {
    initiateOAuthLogin('github');
  };

  return (
    <div className="auth-page">
      {/* Decorative elements */}
      <div className="auth-decoration auth-decoration-1"></div>
      <div className="auth-decoration auth-decoration-2"></div>

      <div className="auth-container">
        <div className="auth-header">
          <div className="auth-icon">🔐</div>
          <h2>Welcome Back</h2>
          <p>Sign in to continue your learning journey</p>
        </div>

        {error && (
          <div className="error">
            <span className="error-icon">⚠️</span>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>
              <span className="input-icon">✉️</span>
              Email Address
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter your email"
              required
            />
          </div>

          <div className="form-group">
            <label>
              <span className="input-icon">🔒</span>
              Password
            </label>
            <div className="password-input-wrapper">
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? (
                  <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" />
                    <line x1="1" y1="1" x2="23" y2="23" />
                  </svg>
                ) : (
                  <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                )}
              </button>
            </div>
          </div>

          <div className="form-options">
            <label className="remember-me">
              <input type="checkbox" />
              <span>Remember me</span>
            </label>
            <Link to="/" className="forgot-password">
              Forgot password?
            </Link>
          </div>

          <button type="submit" disabled={isLoading}>
            {isLoading ? (
              <span className="loading-spinner"></span>
            ) : (
              <>
                Sign In <span className="btn-icon">→</span>
              </>
            )}
          </button>
        </form>

        <div className="auth-divider">
          <span>or continue with</span>
        </div>

        <div className="social-login">
          <button
            type="button"
            className="social-btn google"
            onClick={handleGoogleLogin}
          >
            <svg className="social-icon-svg" viewBox="0 0 24 24" width="20" height="20">
              <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
              <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
            </svg>
            Google
          </button>
          <button
            type="button"
            className="social-btn github"
            onClick={handleGithubLogin}
          >
            <svg className="social-icon-svg" viewBox="0 0 24 24" width="20" height="20">
              <path fill="currentColor" d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
            </svg>
            GitHub
          </button>
        </div>

        <p className="auth-switch">
          Don't have an account?{' '}
          <Link to="/register" className="signup-link">Create one now</Link>
        </p>
      </div>

      <style>{`
        .auth-page {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 100px 20px 40px;
          position: relative;
          overflow: hidden;
        }

        .auth-decoration {
          position: absolute;
          border-radius: 50%;
          filter: blur(80px);
          opacity: 0.4;
        }

        .auth-decoration-1 {
          width: 400px;
          height: 400px;
          background: var(--primary-gradient);
          top: -100px;
          right: -100px;
          animation: float 8s ease-in-out infinite;
        }

        .auth-decoration-2 {
          width: 300px;
          height: 300px;
          background: var(--secondary-gradient);
          bottom: -50px;
          left: -50px;
          animation: float 8s ease-in-out infinite reverse;
        }

        @keyframes float {
          0%, 100% { transform: translate(0, 0) scale(1); }
          50% { transform: translate(30px, -30px) scale(1.1); }
        }

        .auth-header {
          text-align: center;
          margin-bottom: var(--spacing-lg);
        }

        .auth-icon {
          font-size: 3rem;
          margin-bottom: var(--spacing-sm);
          display: block;
        }

        .auth-header h2 {
          font-family: 'Poppins', sans-serif;
          font-size: 2rem;
          font-weight: 700;
          margin-bottom: var(--spacing-xs);
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }

        .auth-header p {
          color: var(--text-secondary);
        }

        .form-group label {
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .input-icon {
          font-size: 1.1rem;
        }

        .password-input-wrapper {
          position: relative;
          display: flex;
          align-items: center;
        }

        .password-input-wrapper input {
          width: 100%;
          padding-right: 45px;
        }

        .password-toggle {
          position: absolute;
          right: 12px;
          background: none;
          border: none;
          padding: 0;
          cursor: pointer;
          color: var(--text-muted);
          display: flex;
          align-items: center;
          justify-content: center;
          transition: color var(--transition-fast);
        }

        .password-toggle:hover {
          color: var(--primary);
        }

        .password-toggle:focus {
          outline: none;
        }

        .form-options {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: var(--spacing-md);
          font-size: 0.9rem;
        }

        .remember-me {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          cursor: pointer;
          color: var(--text-secondary);
        }

        .remember-me input {
          width: auto;
          accent-color: var(--primary);
        }

        .forgot-password {
          color: var(--primary);
          text-decoration: none;
          font-weight: 500;
          transition: color var(--transition-fast);
        }

        .forgot-password:hover {
          color: var(--accent);
        }

        .btn-icon {
          margin-left: 0.5rem;
          transition: transform var(--transition-fast);
        }

        button[type="submit"]:hover .btn-icon {
          transform: translateX(5px);
        }

        .loading-spinner {
          display: inline-block;
          width: 20px;
          height: 20px;
          border: 2px solid rgba(255, 255, 255, 0.3);
          border-top-color: white;
          border-radius: 50%;
          animation: spin 0.8s linear infinite;
        }

        button[type="submit"]:disabled {
          opacity: 0.7;
          cursor: not-allowed;
        }

        .auth-divider {
          position: relative;
          text-align: center;
          margin: var(--spacing-lg) 0;
        }

        .auth-divider::before {
          content: '';
          position: absolute;
          top: 50%;
          left: 0;
          right: 0;
          height: 1px;
          background: var(--border-glass);
        }

        .auth-divider span {
          background: var(--bg-card);
          padding: 0 var(--spacing-sm);
          color: var(--text-muted);
          font-size: 0.9rem;
          position: relative;
        }

        .social-login {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: var(--spacing-sm);
          margin-bottom: var(--spacing-md);
        }

        .social-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 0.5rem;
          padding: 0.8rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          color: var(--text-primary);
          font-weight: 500;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .social-btn:hover {
          background: var(--bg-secondary);
          border-color: var(--primary);
          transform: translateY(-2px);
        }

        .social-btn.google:hover {
          background: rgba(66, 133, 244, 0.1);
          border-color: #4285F4;
        }

        .social-btn.github:hover {
          background: rgba(36, 41, 46, 0.1);
          border-color: #24292e;
        }

        .social-icon-svg {
          flex-shrink: 0;
        }

        .auth-switch {
          text-align: center;
          color: var(--text-secondary);
          font-size: 0.95rem;
        }

        .auth-switch a,
        .signup-link {
          background: var(--accent-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          font-weight: 600;
          cursor: pointer;
          text-decoration: none;
          position: relative;
        }

        .auth-switch a:hover,
        .signup-link:hover {
          opacity: 0.8;
          text-decoration: underline;
        }

        .error {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          background: rgba(245, 87, 108, 0.1);
          border: 1px solid rgba(245, 87, 108, 0.3);
          color: #ff6b8a;
          padding: var(--spacing-sm);
          border-radius: var(--radius-md);
          margin-bottom: var(--spacing-md);
        }

        .error-icon {
          font-size: 1.2rem;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }

        @media (max-width: 480px) {
          .form-options {
            flex-direction: column;
            gap: var(--spacing-sm);
            align-items: flex-start;
          }

          .social-login {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </div>
  );
};

export default Login;
