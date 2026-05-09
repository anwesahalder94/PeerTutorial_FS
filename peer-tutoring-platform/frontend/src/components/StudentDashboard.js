import React, { useState, useEffect } from 'react';
import { bookingService, sessionService, tutorService } from '../services/api';
import Dialog from './Dialog';

const StudentDashboard = () => {
  const [bookings, setBookings] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [tutors, setTutors] = useState({});
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('bookings');

  const [dialog, setDialog] = useState({
    isOpen: false,
    title: '',
    message: '',
    type: 'info'
  });

  const showDialog = (title, message, type = 'info') => {
    setDialog({ isOpen: true, title, message, type });
  };

  const closeDialog = () => {
    setDialog({ ...dialog, isOpen: false });
  };

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const bookingsRes = await bookingService.getStudentBookings();
      setBookings(bookingsRes.data);

      const sessionsRes = await sessionService.getAvailable();
      setSessions(sessionsRes.data);

      // Load tutor information for all sessions
      const tutorsRes = await tutorService.getAll();
      const tutorsMap = {};
      tutorsRes.data.forEach(tutor => {
        tutorsMap[tutor.userId] = tutor;
      });
      setTutors(tutorsMap);
    } catch (err) {
      console.error('Failed to load data', err);
    } finally {
      setLoading(false);
    }
  };

  const getTutorName = (tutorId) => {
    const tutor = tutors[tutorId];
    if (tutor) {
      return `${tutor.user?.firstName || ''} ${tutor.user?.lastName || ''}`.trim() || 'Unknown Tutor';
    }
    return 'Loading...';
  };

  const handleBook = async (sessionId) => {
    try {
      console.log('🚀 Booking session:', sessionId);
      const response = await bookingService.create({ sessionId, notes: 'Booking from dashboard' });
      console.log('✅ Booking successful:', response.data);
      showDialog(
        'Booking Successful',
        `Your booking request has been sent! Status: ${response.data.status}`,
        'success'
      );
      loadData();
    } catch (err) {
      console.error('❌ Booking error:', err);
      const errorMessage = err.response?.data?.message || err.message || 'Booking failed';
      showDialog('Booking Failed', errorMessage, 'error');
    }
  };

  const getStatusBadge = (status) => {
    const styles = {
      PENDING: { bg: 'rgba(254, 225, 64, 0.2)', color: '#fee140', icon: '⏳' },
      CONFIRMED: { bg: 'rgba(67, 233, 123, 0.2)', color: '#43e97b', icon: '✅' },
      REJECTED: { bg: 'rgba(245, 87, 108, 0.2)', color: '#f5576c', icon: '❌' },
      COMPLETED: { bg: 'rgba(102, 126, 234, 0.2)', color: '#667eea', icon: '🎉' },
    };
    const style = styles[status] || styles.PENDING;
    return (
      <span className="status-badge" style={{ background: style.bg, color: style.color }}>
        {style.icon} {status}
      </span>
    );
  };

  const confirmedBookings = bookings.filter(b => b.status === 'CONFIRMED');
  const pendingBookings = bookings.filter(b => b.status === 'PENDING');

  return (
    <div className="student-dashboard">
      {/* Stats Overview */}
      <div className="stats-row">
        <div className="stat-mini">
          <span className="stat-icon">📚</span>
          <div>
            <span className="stat-value">{bookings.length}</span>
            <span className="stat-label">Total Bookings</span>
          </div>
        </div>
        <div className="stat-mini">
          <span className="stat-icon">✅</span>
          <div>
            <span className="stat-value">{confirmedBookings.length}</span>
            <span className="stat-label">Confirmed</span>
          </div>
        </div>
        <div className="stat-mini">
          <span className="stat-icon">⏳</span>
          <div>
            <span className="stat-value">{pendingBookings.length}</span>
            <span className="stat-label">Pending</span>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="dashboard-tabs">
        <button
          className={activeTab === 'bookings' ? 'active' : ''}
          onClick={() => setActiveTab('bookings')}
        >
          📅 My Bookings ({bookings.length})
        </button>
        <button
          className={activeTab === 'sessions' ? 'active' : ''}
          onClick={() => setActiveTab('sessions')}
        >
          🔍 Available Sessions ({sessions.length})
        </button>
      </div>

      {/* Tab Content */}
      {activeTab === 'bookings' && (
        <div className="tab-content">
          {loading ? (
            <div className="loading">Loading your bookings...</div>
          ) : bookings.length === 0 ? (
            <div className="empty-state">
              <span className="empty-icon">📚</span>
              <h4>No bookings yet</h4>
              <p>Explore available sessions and start your learning journey!</p>
            </div>
          ) : (
            <div className="bookings-grid">
              {bookings.map((booking) => {
                // Look up session info from sessions list
                const session = sessions.find(s => s.id === booking.sessionId);
                return (
                  <div key={booking.id} className="booking-card-modern">
                    <div className="booking-header">
                      <h4>{session?.title || 'Session #' + booking.sessionId}</h4>
                      {getStatusBadge(booking.status)}
                    </div>
                    <div className="booking-details">
                      <div className="detail-item">
                        <span className="detail-icon">👨‍🏫</span>
                        <span>{getTutorName(session?.tutorId)}</span>
                      </div>
                      <div className="detail-item">
                        <span className="detail-icon">📖</span>
                        <span>{session?.subject || 'N/A'}</span>
                      </div>
                      <div className="detail-item">
                        <span className="detail-icon">💰</span>
                        <span className="price">${booking.amount}</span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      )}

      {activeTab === 'sessions' && (
        <div className="tab-content">
          {loading ? (
            <div className="loading">Loading available sessions...</div>
          ) : sessions.length === 0 ? (
            <div className="empty-state">
              <span className="empty-icon">🔍</span>
              <h4>No sessions available</h4>
              <p>Check back later for new sessions!</p>
            </div>
          ) : (
            <div className="sessions-grid-modern">
              {sessions.map((session) => (
                <div key={session.id} className="session-card-modern">
                  <div className="session-header-modern">
                    <span className="session-subject-badge">{session.subject}</span>
                    <span className="session-price">${session.price}</span>
                  </div>
                  <h4>{session.title}</h4>
                  <p className="tutor-name">👨‍🏫 {getTutorName(session.tutorId)}</p>
                  <p className="session-description">{session.description?.substring(0, 100)}...</p>
                  <div className="session-meta-modern">
                    <span>📅 {new Date(session.startTime).toLocaleDateString()}</span>
                    <span>👥 {session.enrolledStudents || 0}/{session.maxStudents} students</span>
                    <span>⏱️ {Math.round((new Date(session.endTime) - new Date(session.startTime)) / (1000 * 60))} min</span>
                  </div>
                  <button
                    className="book-btn"
                    onClick={() => handleBook(session.id)}
                  >
                    Book Now →
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      <style>{`
        .student-dashboard {
          animation: fadeIn 0.5s ease-out;
        }

        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }

        .stats-row {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 1rem;
          margin-bottom: 2rem;
        }

        .stat-mini {
          display: flex;
          align-items: center;
          gap: 1rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          padding: 1rem;
          transition: all var(--transition-normal);
        }

        .stat-mini:hover {
          border-color: var(--primary);
          transform: translateY(-2px);
        }

        .stat-mini .stat-icon {
          font-size: 1.5rem;
          width: 45px;
          height: 45px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: var(--primary-gradient);
          border-radius: var(--radius-md);
        }

        .stat-value {
          display: block;
          font-size: 1.5rem;
          font-weight: 700;
          color: var(--text-primary);
        }

        .stat-label {
          font-size: 0.85rem;
          color: var(--text-secondary);
        }

        .dashboard-tabs {
          display: flex;
          gap: 0.5rem;
          margin-bottom: 1.5rem;
          border-bottom: 1px solid var(--border-glass);
          padding-bottom: 1rem;
        }

        .dashboard-tabs button {
          padding: 0.75rem 1.5rem;
          background: transparent;
          border: 1px solid transparent;
          border-radius: var(--radius-md);
          color: var(--text-secondary);
          font-weight: 500;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .dashboard-tabs button:hover {
          color: var(--text-primary);
          background: var(--bg-glass);
        }

        .dashboard-tabs button.active {
          background: var(--primary-gradient);
          color: white;
          border-color: transparent;
        }

        .tab-content {
          min-height: 200px;
        }

        .empty-state {
          text-align: center;
          padding: 3rem;
          background: var(--bg-glass);
          border: 1px dashed var(--border-glass);
          border-radius: var(--radius-lg);
        }

        .empty-icon {
          font-size: 3rem;
          margin-bottom: 1rem;
          display: block;
        }

        .empty-state h4 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.2rem;
          margin-bottom: 0.5rem;
        }

        .empty-state p {
          color: var(--text-secondary);
        }

        .bookings-grid {
          display: grid;
          gap: 1rem;
        }

        .booking-card-modern {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          padding: 1.5rem;
          transition: all var(--transition-normal);
        }

        .booking-card-modern:hover {
          border-color: rgba(102, 126, 234, 0.3);
        }

        .booking-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 1rem;
        }

        .booking-header h4 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.1rem;
          margin: 0;
        }

        .status-badge {
          padding: 0.3rem 0.8rem;
          border-radius: var(--radius-xl);
          font-size: 0.85rem;
          font-weight: 600;
          display: flex;
          align-items: center;
          gap: 0.3rem;
        }

        .booking-details {
          display: flex;
          flex-wrap: wrap;
          gap: 1rem;
        }

        .detail-item {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          color: var(--text-secondary);
          font-size: 0.95rem;
        }

        .detail-icon {
          font-size: 1rem;
        }

        .detail-item .price {
          color: var(--accent);
          font-weight: 600;
        }

        .sessions-grid-modern {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
          gap: 1rem;
        }

        .session-card-modern {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          padding: 1.5rem;
          transition: all var(--transition-normal);
          display: flex;
          flex-direction: column;
        }

        .session-card-modern:hover {
          border-color: rgba(102, 126, 234, 0.3);
          transform: translateY(-3px);
        }

        .session-header-modern {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 0.75rem;
        }

        .session-subject-badge {
          background: var(--primary-gradient);
          color: white;
          padding: 0.3rem 0.8rem;
          border-radius: var(--radius-sm);
          font-size: 0.8rem;
          font-weight: 600;
        }

        .session-price {
          font-family: 'Poppins', sans-serif;
          font-size: 1.3rem;
          font-weight: 700;
          color: var(--accent);
        }

        .session-card-modern h4 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.1rem;
          margin-bottom: 0.5rem;
        }

        .session-description {
          color: var(--text-secondary);
          font-size: 0.9rem;
          margin-bottom: 1rem;
          flex-grow: 1;
        }

        .tutor-name {
          color: var(--primary);
          font-size: 0.9rem;
          font-weight: 500;
          margin-bottom: 0.5rem;
        }

        .session-meta-modern {
          display: flex;
          gap: 1rem;
          margin-bottom: 1rem;
          font-size: 0.85rem;
          color: var(--text-muted);
        }

        .book-btn {
          width: 100%;
          padding: 0.75rem;
          background: var(--success-gradient);
          color: white;
          border: none;
          border-radius: var(--radius-md);
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .book-btn:hover {
          transform: translateY(-2px);
          box-shadow: 0 10px 25px rgba(67, 233, 123, 0.4);
        }

        .loading {
          text-align: center;
          padding: 3rem;
          color: var(--text-secondary);
        }

        @media (max-width: 768px) {
          .stats-row {
            grid-template-columns: 1fr;
          }

          .dashboard-tabs {
            flex-direction: column;
          }

          .dashboard-tabs button {
            width: 100%;
          }

          .sessions-grid-modern {
            grid-template-columns: 1fr;
          }
        }
      `}</style>

      <Dialog
        isOpen={dialog.isOpen}
        onClose={closeDialog}
        title={dialog.title}
        message={dialog.message}
        type={dialog.type}
      />
    </div>
  );
};

export default StudentDashboard;
