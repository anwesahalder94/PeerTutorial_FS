import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { sessionService, bookingService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import Dialog from '../components/Dialog';

const Home = () => {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [bookingLoading, setBookingLoading] = useState(null);
  const { user } = useAuth();
  const navigate = useNavigate();

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
  const [stats, setStats] = useState({
    tutors: 150,
    students: 2500,
    sessions: 8500,
    rating: 4.8
  });

  useEffect(() => {
    loadSessions();
  }, []);

  const loadSessions = async () => {
    try {
      const response = await sessionService.getAvailable();
      setSessions(response.data.slice(0, 6)); // Show only first 6 sessions
    } catch (err) {
      console.error('Failed to load sessions', err);
    } finally {
      setLoading(false);
    }
  };

  const handleBookSession = async (sessionId) => {
    if (!user) {
      navigate('/login');
      return;
    }

    if (user.role !== 'STUDENT') {
      showDialog(
        'Booking Restricted',
        'Only students can book sessions. Please login as a student to make a booking.',
        'warning'
      );
      return;
    }

    setBookingLoading(sessionId);
    try {
      const response = await bookingService.create({
        sessionId,
        notes: 'Booked from home page'
      });
      showDialog(
        'Booking Successful',
        `Your booking request has been sent! Status: ${response.data.status}`,
        'success'
      );
      loadSessions(); // Refresh the sessions
    } catch (err) {
      console.error('Booking error:', err);
      const errorMessage = err.response?.data?.message || 'Failed to book session. Please try again.';
      showDialog('Booking Failed', errorMessage, 'error');
    } finally {
      setBookingLoading(null);
    }
  };

  const features = [
    {
      icon: '🎓',
      title: 'Expert Tutors',
      description: 'Learn from verified experts in various subjects with proven teaching experience.'
    },
    {
      icon: '⏰',
      title: 'Flexible Scheduling',
      description: 'Book sessions that fit your schedule. Learn anytime, anywhere.'
    },
    {
      icon: '💰',
      title: 'Affordable Pricing',
      description: 'Competitive rates starting from $10/hour. Quality education within reach.'
    },
    {
      icon: '⭐',
      title: 'Rated & Reviewed',
      description: 'Choose tutors based on real student reviews and ratings.'
    }
  ];

  return (
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-badge animate-in">
          <span className="badge-icon">🚀</span>
          <span>Trusted by 2,500+ students worldwide</span>
        </div>
        <h1 className="animate-in-delay-1">
          Master Any Subject<br />
          <span className="gradient-text">With Expert Tutors</span>
        </h1>
        <p className="animate-in-delay-2">
          Connect with thousands of verified tutors. Get personalized 1-on-1 sessions
          or join group classes. Your learning journey starts here.
        </p>
        <div className="hero-buttons animate-in-delay-3">
          <Link to={user ? '/dashboard' : '/register'} className="btn btn-primary">
            <span>{user ? 'Go to Dashboard' : 'Start Learning'}</span>
            <span className="btn-arrow">→</span>
          </Link>
          <Link to="/tutors" className="btn btn-secondary">
            <span>Explore Tutors</span>
          </Link>
        </div>

        {/* Stats */}
        <div className="hero-stats animate-in-delay-3">
          <div className="stat-item">
            <span className="stat-number">{stats.tutors}+</span>
            <span className="stat-label">Expert Tutors</span>
          </div>
          <div className="stat-divider"></div>
          <div className="stat-item">
            <span className="stat-number">{stats.students.toLocaleString()}+</span>
            <span className="stat-label">Happy Students</span>
          </div>
          <div className="stat-divider"></div>
          <div className="stat-item">
            <span className="stat-number">{stats.sessions.toLocaleString()}+</span>
            <span className="stat-label">Sessions Completed</span>
          </div>
          <div className="stat-divider"></div>
          <div className="stat-item">
            <span className="stat-number">{stats.rating}</span>
            <span className="stat-label">Average Rating</span>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="section-header">
          <span className="section-badge">Why Choose Us</span>
          <h2>Everything You Need to Succeed</h2>
          <p>We provide all the tools and resources for effective learning</p>
        </div>
        <div className="features-grid">
          {features.map((feature, index) => (
            <div key={index} className="feature-card" style={{animationDelay: `${index * 0.1}s`}}>
              <div className="feature-icon">{feature.icon}</div>
              <h3>{feature.title}</h3>
              <p>{feature.description}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Available Sessions */}
      <section className="available-sessions">
        <div className="section-header">
          <span className="section-badge">Live Sessions</span>
          <h2>Available Sessions</h2>
          <p>Book your spot in upcoming sessions with top tutors</p>
        </div>

        {loading ? (
          <div className="loading">Loading sessions...</div>
        ) : sessions.length === 0 ? (
          <div className="empty-state">
            <span className="empty-icon">📚</span>
            <h3>No sessions available</h3>
            <p>Check back soon for new sessions or browse our tutors.</p>
            <Link to="/tutors" className="btn btn-primary">Browse Tutors</Link>
          </div>
        ) : (
          <>
            <div className="sessions-grid">
              {sessions.map((session, index) => (
                <div
                  key={session.id}
                  className="session-card"
                  style={{animationDelay: `${index * 0.1}s`}}
                >
                  <div className="session-header">
                    <span className="session-subject">{session.subject}</span>
                    <span className="session-price">${session.price}</span>
                  </div>
                  <h3>{session.title}</h3>
                  <p>{session.description?.substring(0, 100)}...</p>
                  <div className="session-meta">
                    <span className="meta-item">
                      <span className="meta-icon">📅</span>
                      {new Date(session.startTime).toLocaleDateString()}
                    </span>
                    <span className="meta-item">
                      <span className="meta-icon">👥</span>
                      {session.type}
                    </span>
                    <span className="meta-item">
                      <span className="meta-icon">⏱️</span>
                      {session.duration || 60} min
                    </span>
                  </div>
                  <button
                    className="btn btn-primary session-btn"
                    onClick={() => handleBookSession(session.id)}
                    disabled={bookingLoading === session.id}
                  >
                    {bookingLoading === session.id ? (
                      'Booking...'
                    ) : user ? (
                      user.role === 'STUDENT' ? 'Book Now' : 'View Session'
                    ) : (
                      'Login to Book'
                    )}
                  </button>
                </div>
              ))}
            </div>
            <div className="view-all-container">
              <Link to="/tutors" className="view-all-link">
                View All Sessions <span>→</span>
              </Link>
            </div>
          </>
        )}
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="cta-content">
          <h2>Ready to Start Learning?</h2>
          <p>Join thousands of students achieving their goals with expert tutors.</p>
          <div className="cta-buttons">
            <Link to="/register" className="btn btn-primary">
              Create Free Account
            </Link>
            <Link to="/tutors" className="btn btn-secondary">
              Browse Tutors
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="footer-content">
          <div className="footer-brand">
            <h3>PeerTutor</h3>
            <p>Empowering learners worldwide with personalized education.</p>
          </div>
          <div className="footer-links">
            <div className="footer-column">
              <h4>Platform</h4>
              <Link to="/tutors">Find Tutors</Link>
              <Link to="/register">Become a Tutor</Link>
              <Link to="/">How it Works</Link>
            </div>
            <div className="footer-column">
              <h4>Support</h4>
              <Link to="/">Help Center</Link>
              <Link to="/">Contact Us</Link>
              <Link to="/">FAQs</Link>
            </div>
            <div className="footer-column">
              <h4>Legal</h4>
              <Link to="/">Privacy Policy</Link>
              <Link to="/">Terms of Service</Link>
            </div>
          </div>
        </div>
        <div className="footer-bottom">
          <p>© 2024 PeerTutor. All rights reserved.</p>
        </div>
      </footer>

      <style>{`
        /* Additional Home Styles */
        .hero-badge {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          padding: 0.5rem 1rem;
          border-radius: var(--radius-xl);
          font-size: 0.9rem;
          color: var(--text-secondary);
          margin-bottom: var(--spacing-md);
        }

        .badge-icon {
          font-size: 1.2rem;
        }

        .gradient-text {
          background: var(--accent-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }

        .btn-arrow {
          transition: transform var(--transition-normal);
        }

        .btn-primary:hover .btn-arrow {
          transform: translateX(5px);
        }

        .hero-stats {
          display: flex;
          align-items: center;
          gap: var(--spacing-xl);
          margin-top: var(--spacing-xxl);
          flex-wrap: wrap;
          justify-content: center;
        }

        .stat-item {
          text-align: center;
        }

        .stat-number {
          display: block;
          font-family: 'Poppins', sans-serif;
          font-size: 2.5rem;
          font-weight: 700;
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }

        .stat-label {
          font-size: 0.9rem;
          color: var(--text-secondary);
        }

        .stat-divider {
          width: 1px;
          height: 50px;
          background: var(--border-glass);
        }

        @media (max-width: 768px) {
          .hero-stats {
            gap: var(--spacing-md);
          }

          .stat-divider {
            display: none;
          }

          .stat-number {
            font-size: 1.8rem;
          }
        }

        .section-header {
          text-align: center;
          margin-bottom: var(--spacing-xl);
        }

        .section-badge {
          display: inline-block;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          padding: 0.4rem 1rem;
          border-radius: var(--radius-sm);
          font-size: 0.85rem;
          color: var(--primary);
          text-transform: uppercase;
          letter-spacing: 1px;
          font-weight: 600;
          margin-bottom: var(--spacing-sm);
        }

        .section-header h2 {
          font-family: 'Poppins', sans-serif;
          font-size: 2.5rem;
          font-weight: 700;
          margin-bottom: var(--spacing-sm);
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }

        .section-header p {
          color: var(--text-secondary);
          font-size: 1.1rem;
          max-width: 500px;
          margin: 0 auto;
        }

        .feature-card {
          animation: fadeInUp 0.6s ease-out forwards;
          opacity: 0;
        }

        .session-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: var(--spacing-sm);
        }

        .session-subject {
          background: var(--primary-gradient);
          color: white;
          padding: 0.3rem 0.8rem;
          border-radius: var(--radius-sm);
          font-size: 0.8rem;
          font-weight: 600;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .session-price {
          font-family: 'Poppins', sans-serif;
          font-size: 1.5rem;
          font-weight: 700;
          color: var(--accent);
        }

        .meta-item {
          display: flex;
          align-items: center;
          gap: 0.3rem;
        }

        .meta-icon {
          font-size: 0.9rem;
        }

        .session-btn {
          width: 100%;
          margin-top: var(--spacing-sm);
          text-align: center;
          justify-content: center;
          cursor: pointer;
          border: none;
          font-family: inherit;
          font-size: inherit;
        }

        .session-btn:disabled {
          opacity: 0.7;
          cursor: not-allowed;
        }

        .view-all-container {
          text-align: center;
          margin-top: var(--spacing-xl);
        }

        .view-all-link {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          color: var(--primary);
          font-weight: 600;
          text-decoration: none;
          transition: gap var(--transition-normal);
        }

        .view-all-link:hover {
          gap: 1rem;
        }

        .empty-state {
          text-align: center;
          padding: var(--spacing-xxl);
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
        }

        .empty-icon {
          font-size: 4rem;
          margin-bottom: var(--spacing-md);
          display: block;
        }

        .empty-state h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.5rem;
          margin-bottom: var(--spacing-sm);
        }

        .empty-state p {
          color: var(--text-secondary);
          margin-bottom: var(--spacing-md);
        }

        /* CTA Section */
        .cta-section {
          padding: var(--spacing-xxl) var(--spacing-lg);
          margin: var(--spacing-xl) auto;
          max-width: 1000px;
        }

        .cta-content {
          background: var(--primary-gradient);
          border-radius: var(--radius-lg);
          padding: var(--spacing-xxl);
          text-align: center;
          position: relative;
          overflow: hidden;
        }

        .cta-content::before {
          content: '';
          position: absolute;
          top: -50%;
          right: -20%;
          width: 400px;
          height: 400px;
          background: rgba(255, 255, 255, 0.1);
          border-radius: 50%;
          filter: blur(50px);
        }

        .cta-content h2 {
          font-family: 'Poppins', sans-serif;
          font-size: 2.5rem;
          font-weight: 700;
          margin-bottom: var(--spacing-sm);
          position: relative;
        }

        .cta-content p {
          font-size: 1.1rem;
          margin-bottom: var(--spacing-lg);
          opacity: 0.9;
          position: relative;
        }

        .cta-buttons {
          display: flex;
          gap: var(--spacing-md);
          justify-content: center;
          flex-wrap: wrap;
          position: relative;
        }

        .cta-buttons .btn-secondary {
          border-color: rgba(255, 255, 255, 0.3);
          color: white;
        }

        .cta-buttons .btn-secondary:hover {
          background: rgba(255, 255, 255, 0.1);
        }

        /* Footer */
        .footer {
          background: var(--bg-secondary);
          border-top: 1px solid var(--border-glass);
          padding: var(--spacing-xxl) var(--spacing-lg);
          margin-top: var(--spacing-xxl);
        }

        .footer-content {
          max-width: 1200px;
          margin: 0 auto;
          display: grid;
          grid-template-columns: 1fr 2fr;
          gap: var(--spacing-xl);
        }

        .footer-brand h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.8rem;
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          margin-bottom: var(--spacing-sm);
        }

        .footer-brand p {
          color: var(--text-secondary);
          max-width: 300px;
        }

        .footer-links {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: var(--spacing-lg);
        }

        .footer-column h4 {
          font-family: 'Poppins', sans-serif;
          font-size: 1rem;
          font-weight: 600;
          margin-bottom: var(--spacing-md);
          color: var(--text-primary);
        }

        .footer-column a {
          display: block;
          color: var(--text-secondary);
          text-decoration: none;
          margin-bottom: 0.5rem;
          transition: color var(--transition-fast);
        }

        .footer-column a:hover {
          color: var(--primary);
        }

        .footer-bottom {
          max-width: 1200px;
          margin: var(--spacing-xl) auto 0;
          padding-top: var(--spacing-lg);
          border-top: 1px solid var(--border-glass);
          text-align: center;
          color: var(--text-muted);
        }

        @media (max-width: 768px) {
          .footer-content {
            grid-template-columns: 1fr;
            text-align: center;
          }

          .footer-brand p {
            margin: 0 auto;
          }

          .footer-links {
            grid-template-columns: repeat(2, 1fr);
          }

          .cta-content {
            padding: var(--spacing-xl);
          }

          .cta-content h2 {
            font-size: 1.8rem;
          }
        }

        @media (max-width: 480px) {
          .footer-links {
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

export default Home;
