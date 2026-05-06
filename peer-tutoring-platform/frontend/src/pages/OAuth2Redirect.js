import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const OAuth2Redirect = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginWithToken } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    const error = params.get('error');

    if (token) {
      // Extract user data from URL params
      const userData = {
        id: params.get('userId'),
        email: params.get('email'),
        firstName: params.get('firstName'),
        lastName: params.get('lastName'),
        role: params.get('role'),
      };

      // Store auth data
      loginWithToken(token, userData);

      // Redirect to dashboard
      navigate('/dashboard', { replace: true });
    } else if (error) {
      console.error('OAuth2 Error:', error);
      navigate('/login?error=oauth_failed', { replace: true });
    } else {
      navigate('/login', { replace: true });
    }
  }, [location, navigate, loginWithToken]);

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-header">
          <div className="auth-icon">⏳</div>
          <h2>Authenticating...</h2>
          <p>Please wait while we complete your sign-in</p>
        </div>
        <div className="loading-spinner-container">
          <div className="loading-spinner-large"></div>
        </div>
      </div>

      <style>{`
        .auth-page {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 100px 20px 40px;
        }

        .loading-spinner-container {
          display: flex;
          justify-content: center;
          margin-top: 2rem;
        }

        .loading-spinner-large {
          width: 50px;
          height: 50px;
          border: 4px solid rgba(102, 126, 234, 0.2);
          border-top-color: #667eea;
          border-radius: 50%;
          animation: spin 1s linear infinite;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

export default OAuth2Redirect;
