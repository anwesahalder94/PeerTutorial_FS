import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import StudentDashboard from '../components/StudentDashboard';
import TutorDashboard from '../components/TutorDashboard';
import AdminDashboard from '../components/AdminDashboard';

const Dashboard = () => {
  const { user, hasRole } = useAuth();
  const [activeTab, setActiveTab] = useState('overview');

  const getRoleIcon = (role) => {
    switch (role) {
      case 'ADMIN': return '👑';
      case 'TUTOR': return '👨‍🏫';
      default: return '🎓';
    }
  };

  const getRoleColor = (role) => {
    switch (role) {
      case 'ADMIN': return 'linear-gradient(135deg, #f5576c 0%, #f093fb 100%)';
      case 'TUTOR': return 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)';
      default: return 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)';
    }
  };

  return (
    <div className="dashboard">
      {/* Dashboard Header */}
      <div className="dashboard-header">
        <div className="welcome-section">
          <div className="user-avatar-large" style={{ background: getRoleColor(user?.role) }}>
            {user?.firstName?.charAt(0).toUpperCase()}
          </div>
          <div className="welcome-text">
            <h2>Welcome back, {user?.firstName}! 👋</h2>
            <div className="user-meta">
              <span className="role-badge" style={{ background: getRoleColor(user?.role) }}>
                {getRoleIcon(user?.role)} {user?.role}
              </span>
              <span className="email-badge">{user?.email}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Dashboard Content */}
      <div className="dashboard-content">
        {hasRole('STUDENT') && (
          <div className="dashboard-section">
            <div className="section-header-with-icon">
              <span className="section-icon student-icon">🎓</span>
              <h3>Student Dashboard</h3>
            </div>
            <StudentDashboard />
          </div>
        )}

        {hasRole('TUTOR') && (
          <div className="dashboard-section">
            <div className="section-header-with-icon">
              <span className="section-icon tutor-icon">👨‍🏫</span>
              <h3>Tutor Dashboard</h3>
            </div>
            <TutorDashboard />
          </div>
        )}

        {hasRole('ADMIN') && (
          <div className="dashboard-section">
            <div className="section-header-with-icon">
              <span className="section-icon admin-icon">👑</span>
              <h3>Admin Dashboard</h3>
            </div>
            <AdminDashboard />
          </div>
        )}
      </div>

      <style>{`
        .dashboard {
          padding: 100px 20px 40px;
          max-width: 1400px;
          margin: 0 auto;
          min-height: 100vh;
        }

        .dashboard-header {
          margin-bottom: 2rem;
        }

        .welcome-section {
          display: flex;
          align-items: center;
          gap: 1.5rem;
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 2rem;
        }

        .user-avatar-large {
          width: 80px;
          height: 80px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 2rem;
          font-weight: 700;
          color: white;
          box-shadow: var(--shadow-glow);
        }

        .welcome-text h2 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.8rem;
          font-weight: 700;
          margin-bottom: 0.5rem;
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }

        .user-meta {
          display: flex;
          gap: 1rem;
          flex-wrap: wrap;
        }

        .role-badge {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          padding: 0.4rem 1rem;
          border-radius: var(--radius-xl);
          color: white;
          font-weight: 600;
          font-size: 0.9rem;
        }

        .email-badge {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          padding: 0.4rem 1rem;
          border-radius: var(--radius-xl);
          color: var(--text-secondary);
          font-size: 0.9rem;
        }

        .dashboard-content {
          display: flex;
          flex-direction: column;
          gap: 2rem;
        }

        .dashboard-section {
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 2rem;
          transition: all var(--transition-normal);
        }

        .dashboard-section:hover {
          border-color: rgba(102, 126, 234, 0.3);
        }

        .section-header-with-icon {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          margin-bottom: 1.5rem;
          padding-bottom: 1rem;
          border-bottom: 1px solid var(--border-glass);
        }

        .section-header-with-icon h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.4rem;
          font-weight: 600;
          margin: 0;
        }

        .section-icon {
          font-size: 1.5rem;
          width: 40px;
          height: 40px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: var(--bg-glass);
          border-radius: var(--radius-md);
        }

        .student-icon {
          background: linear-gradient(135deg, rgba(79, 172, 254, 0.2) 0%, rgba(0, 242, 254, 0.2) 100%);
        }

        .tutor-icon {
          background: linear-gradient(135deg, rgba(67, 233, 123, 0.2) 0%, rgba(56, 249, 215, 0.2) 100%);
        }

        .admin-icon {
          background: linear-gradient(135deg, rgba(245, 87, 108, 0.2) 0%, rgba(240, 147, 251, 0.2) 100%);
        }

        @media (max-width: 768px) {
          .welcome-section {
            flex-direction: column;
            text-align: center;
          }

          .user-meta {
            justify-content: center;
          }

          .dashboard-section {
            padding: 1.5rem;
          }
        }
      `}</style>
    </div>
  );
};

export default Dashboard;
