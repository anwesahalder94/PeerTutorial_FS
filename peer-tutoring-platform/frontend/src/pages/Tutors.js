import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { tutorService, bookingService, sessionService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import Dialog from '../components/Dialog';

const Tutors = () => {
  const [tutors, setTutors] = useState([]);
  const [subject, setSubject] = useState('');
  const [minRating, setMinRating] = useState('');
  const [loading, setLoading] = useState(true);
  const [viewMode, setViewMode] = useState('grid');
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


  const loadTutors = async () => {
    try {
      const response = await tutorService.getAll();
      setTutors(response.data);
    } catch (err) {
      console.error('Failed to load tutors', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const params = {};
      if (subject && subject !== 'All Subjects') {
        params.subject = subject;
      }
      if (minRating) {
        params.minRating = parseFloat(minRating);
      }
      const response = await tutorService.search(params);
      setTutors(response.data);
    } catch (err) {
      console.error('Search failed', err);
    } finally {
      setLoading(false);
    }
  };

  const subjects = ['All Subjects', 'Mathematics', 'Physics', 'Chemistry', 'Biology', 'Computer Science', 'English', 'History'];

  const getInitials = (firstName, lastName) => {
    return `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();
  };

  const getAvatarColor = (name) => {
    const colors = [
      'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
      'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
      'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
      'linear-gradient(135deg, #30cfd0 0%, #330867 100%)',
    ];
    const index = name?.charCodeAt(0) % colors.length || 0;
    return colors[index];
  };

  const [bookingLoading, setBookingLoading] = useState(null);
  const [tutorSessions, setTutorSessions] = useState({});

  const loadTutorSessions = async () => {
    try {
      // Load all available sessions
      const response = await sessionService.getAvailable();
      const sessions = response.data;

      // Group sessions by tutorId (backend returns tutorId, not tutor object)
      const sessionsByTutor = {};
      sessions.forEach(session => {
        const tutorId = session.tutorId;
        if (tutorId) {
          if (!sessionsByTutor[tutorId]) {
            sessionsByTutor[tutorId] = [];
          }
          sessionsByTutor[tutorId].push(session);
        }
      });
      setTutorSessions(sessionsByTutor);
    } catch (err) {
      console.error('Failed to load sessions', err);
    }
  };

  useEffect(() => {
    loadTutors();
    loadTutorSessions();
  }, []);

  const handleBookSession = async (tutorId) => {
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

    // Find a session from this tutor
    try {
      setBookingLoading(tutorId);
      const sessions = tutorSessions[tutorId];

      if (sessions && sessions.length > 0) {
        const availableSession = sessions[0]; // Take the first available session
        const bookingResponse = await bookingService.create({
          sessionId: availableSession.id,
          notes: `Booked from Tutors page`
        });
        showDialog(
          'Booking Successful',
          `Session booked successfully! Status: ${bookingResponse.data.status}`,
          'success'
        );
        loadTutorSessions(); // Refresh sessions
      } else {
        showDialog(
          'No Sessions Available',
          'This tutor has no available sessions at the moment.',
          'warning'
        );
      }
    } catch (err) {
      console.error('Booking error:', err);
      const errorMessage = err.response?.data?.message || 'Failed to book session. Please try again.';
      showDialog('Booking Failed', errorMessage, 'error');
    } finally {
      setBookingLoading(null);
    }
  };

  // Show restricted view for tutors
  if (user?.role === 'TUTOR') {
    return (
      <div className="tutors-page">
        <section className="tutors-hero">
          <div className="tutors-hero-content">
            <span className="section-badge">Tutor Access</span>
            <h1>Tutor Dashboard</h1>
            <p>As a tutor, you can manage your profile and sessions from your dashboard.</p>
          </div>
        </section>

        <section className="restricted-section">
          <div className="restricted-content">
            <div className="restricted-icon">👨‍🏫</div>
            <h2>Welcome, {user?.firstName}!</h2>
            <p>Tutors cannot view other tutor profiles for privacy reasons.</p>
            <p>Please visit your dashboard to manage your sessions and profile.</p>
            <div className="restricted-actions">
              <button onClick={() => navigate('/dashboard')} className="btn btn-primary">
                Go to My Dashboard
              </button>
            </div>
          </div>
        </section>

        <style>{`
          .restricted-section {
            padding: 4rem 2rem;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 50vh;
          }

          .restricted-content {
            text-align: center;
            max-width: 500px;
            background: var(--bg-card);
            border: 1px solid var(--border-glass);
            border-radius: var(--radius-lg);
            padding: 3rem;
          }

          .restricted-icon {
            font-size: 4rem;
            margin-bottom: 1.5rem;
          }

          .restricted-content h2 {
            font-family: 'Poppins', sans-serif;
            font-size: 1.8rem;
            margin-bottom: 1rem;
            color: var(--text-primary);
          }

          .restricted-content p {
            color: var(--text-secondary);
            margin-bottom: 0.75rem;
            line-height: 1.6;
          }

          .restricted-actions {
            margin-top: 2rem;
          }

          .restricted-actions button {
            padding: 0.875rem 2rem;
            font-size: 1rem;
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
  }

  return (
    <div className="tutors-page">
      {/* Hero Banner */}
      <section className="tutors-hero">
        <div className="tutors-hero-content">
          <span className="section-badge">Find Your Perfect Tutor</span>
          <h1>Expert Tutors Ready to Help</h1>
          <p>Browse through our verified tutors and find the perfect match for your learning needs</p>
        </div>
      </section>

      {/* Search Section */}
      <section className="search-section">
        <form className="search-form" onSubmit={handleSearch}>
          <div className="search-input-wrapper">
            <span className="search-icon">🔍</span>
            <input
              type="text"
              placeholder="Search by subject (e.g., Mathematics, Physics...)"
              value={subject}
              onChange={(e) => setSubject(e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label>Minimum Rating:</label>
            <select value={minRating} onChange={(e) => setMinRating(e.target.value)}>
              <option value="">All Ratings</option>
              <option value="4.5">⭐ 4.5+ Stars</option>
              <option value="4">⭐ 4+ Stars</option>
              <option value="3">⭐ 3+ Stars</option>
            </select>
          </div>

          <button type="submit" className="search-btn">
            🔍 Search
          </button>
        </form>

        {/* Quick Filters */}
        <div className="quick-filters">
          {subjects.map((subj) => (
            <button
              key={subj}
              className={`filter-chip ${subject === subj || (subj === 'All Subjects' && !subject) ? 'active' : ''}`}
              onClick={() => {
                setSubject(subj === 'All Subjects' ? '' : subj);
                setTimeout(() => handleSearch({ preventDefault: () => {} }), 0);
              }}
            >
              {subj}
            </button>
          ))}
        </div>

        {/* View Toggle */}
        <div className="view-toggle">
          <button
            className={viewMode === 'grid' ? 'active' : ''}
            onClick={() => setViewMode('grid')}
          >
            ⊞ Grid
          </button>
          <button
            className={viewMode === 'list' ? 'active' : ''}
            onClick={() => setViewMode('list')}
          >
            ☰ List
          </button>
        </div>
      </section>

      {/* Results Section */}
      <section className="results-section">
        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner-large"></div>
            <p>Finding the best tutors for you...</p>
          </div>
        ) : tutors.length === 0 ? (
          <div className="empty-state">
            <span className="empty-icon">🔍</span>
            <h3>No tutors found</h3>
            <p>Try adjusting your search criteria or browse all subjects</p>
            <button className="btn btn-primary" onClick={() => { setSubject(''); setMinRating(''); loadTutors(); }}>
              View All Tutors
            </button>
          </div>
        ) : (
          <>
            <div className="results-header">
              <span className="results-count">{tutors.length} tutor{tutors.length !== 1 ? 's' : ''} found</span>
            </div>

            <div className={`tutors-${viewMode}`}>
              {tutors.map((tutor, index) => (
                <div
                  key={tutor.id}
                  className="tutor-card"
                  style={{ animationDelay: `${index * 0.05}s` }}
                >
                  <div className="tutor-avatar" style={{ background: getAvatarColor(tutor.user?.firstName) }}>
                    {getInitials(tutor.user?.firstName, tutor.user?.lastName)}
                  </div>

                  <div className="tutor-content">
                    <div className="tutor-header">
                      <div>
                        <h3>{tutor.user?.firstName} {tutor.user?.lastName}</h3>
                        <span className="tutor-subject">{tutor.subject}</span>
                      </div>
                      <div className="rating-badge">
                        <span className="star">⭐</span>
                        <span className="rating-value">{tutor.averageRating?.toFixed(1) || 'New'}</span>
                      </div>
                    </div>

                    <p className="tutor-bio">{tutor.bio?.substring(0, 120)}...</p>

                    <div className="tutor-stats">
                      <div className="stat">
                        <span className="stat-icon">🎓</span>
                        <span>{tutor.experience || 0}+ years exp.</span>
                      </div>
                      <div className="stat">
                        <span className="stat-icon">📚</span>
                        <span>{tutor.totalSessions || 0} sessions</span>
                      </div>
                    </div>

                    <div className="tutor-footer">
                      <div className="tutor-price">
                        <span className="price">${tutor.hourlyRate}</span>
                        <span className="per-hour">/hour</span>
                      </div>
                      <button
                        className="btn btn-primary view-profile-btn"
                        onClick={() => handleBookSession(tutor.id)}
                        disabled={bookingLoading === tutor.id}
                      >
                        {bookingLoading === tutor.id ? 'Booking...' : 'Book Session →'}
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </>
        )}
      </section>

      {/* CTA Section */}
      <section className="become-tutor-section">
        <div className="become-tutor-content">
          <div className="become-tutor-text">
            <h2>Are you an expert?</h2>
            <p>Join our platform as a tutor and start earning while helping others learn</p>
          </div>
          <Link to="/register" className="btn btn-secondary become-tutor-btn">
            Become a Tutor
          </Link>
        </div>
      </section>

      <style>{`
        .tutors-page {
          min-height: 100vh;
        }

        .tutors-hero {
          background: var(--bg-secondary);
          padding: 120px 20px 60px;
          text-align: center;
          border-bottom: 1px solid var(--border-glass);
        }

        .tutors-hero-content {
          max-width: 700px;
          margin: 0 auto;
        }

        .tutors-hero h1 {
          font-family: 'Poppins', sans-serif;
          font-size: clamp(2rem, 4vw, 3rem);
          font-weight: 700;
          margin: 1rem 0;
          background: var(--primary-gradient);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }

        .tutors-hero p {
          color: var(--text-secondary);
          font-size: 1.1rem;
        }

        .search-section {
          background: var(--bg-card);
          border-bottom: 1px solid var(--border-glass);
          padding: 2rem;
          position: sticky;
          top: 70px;
          z-index: 100;
          backdrop-filter: blur(20px);
        }

        .search-form {
          display: flex;
          gap: 1rem;
          max-width: 1200px;
          margin: 0 auto;
          align-items: center;
          flex-wrap: wrap;
        }

        .search-input-wrapper {
          flex: 1;
          min-width: 300px;
          position: relative;
        }

        .search-icon {
          position: absolute;
          left: 1rem;
          top: 50%;
          transform: translateY(-50%);
          font-size: 1.2rem;
        }

        .search-input-wrapper input {
          width: 100%;
          padding: 1rem 1rem 1rem 3rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          color: var(--text-primary);
          font-size: 1rem;
          transition: all var(--transition-normal);
        }

        .search-input-wrapper input:focus {
          outline: none;
          border-color: var(--primary);
          box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15);
        }

        .filter-group {
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .filter-group label {
          color: var(--text-secondary);
          font-size: 0.9rem;
        }

        .filter-group select {
          padding: 0.9rem 1rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          color: var(--text-primary);
          font-size: 0.95rem;
          cursor: pointer;
        }

        .search-btn {
          padding: 1rem 2rem;
          background: var(--primary-gradient);
          color: white;
          border: none;
          border-radius: var(--radius-md);
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .search-btn:hover {
          transform: translateY(-2px);
          box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
        }

        .quick-filters {
          display: flex;
          gap: 0.5rem;
          flex-wrap: wrap;
          max-width: 1200px;
          margin: 1rem auto 0;
          justify-content: center;
        }

        .filter-chip {
          padding: 0.5rem 1rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-xl);
          color: var(--text-secondary);
          font-size: 0.9rem;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .filter-chip:hover,
        .filter-chip.active {
          background: var(--primary-gradient);
          color: white;
          border-color: transparent;
        }

        .view-toggle {
          display: flex;
          gap: 0.5rem;
          justify-content: center;
          margin-top: 1rem;
        }

        .view-toggle button {
          padding: 0.5rem 1rem;
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-sm);
          color: var(--text-secondary);
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .view-toggle button.active {
          background: var(--primary-gradient);
          color: white;
        }

        .results-section {
          padding: 2rem;
          max-width: 1400px;
          margin: 0 auto;
        }

        .loading-container {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          padding: 4rem;
        }

        .loading-spinner-large {
          width: 60px;
          height: 60px;
          border: 3px solid var(--border-glass);
          border-top-color: var(--primary);
          border-radius: 50%;
          animation: spin 1s linear infinite;
          margin-bottom: 1rem;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }

        .empty-state {
          text-align: center;
          padding: 4rem;
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
        }

        .empty-icon {
          font-size: 4rem;
          margin-bottom: 1rem;
          display: block;
        }

        .empty-state h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.5rem;
          margin-bottom: 0.5rem;
        }

        .empty-state p {
          color: var(--text-secondary);
          margin-bottom: 1.5rem;
        }

        .results-header {
          margin-bottom: 1.5rem;
        }

        .results-count {
          color: var(--text-secondary);
          font-size: 0.95rem;
        }

        /* Grid View */
        .tutors-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
          gap: 1.5rem;
        }

        .tutors-grid .tutor-card {
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 1.5rem;
          transition: all var(--transition-normal);
          animation: fadeInUp 0.5s ease-out forwards;
          opacity: 0;
        }

        .tutors-grid .tutor-card:hover {
          transform: translateY(-5px);
          box-shadow: var(--shadow-glow);
          border-color: rgba(102, 126, 234, 0.3);
        }

        @keyframes fadeInUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }

        .tutor-avatar {
          width: 70px;
          height: 70px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.5rem;
          font-weight: 700;
          color: white;
          margin-bottom: 1rem;
        }

        .tutor-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 0.5rem;
        }

        .tutor-header h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.2rem;
          font-weight: 600;
          margin: 0;
        }

        .tutor-subject {
          color: var(--primary);
          font-size: 0.9rem;
          font-weight: 500;
        }

        .rating-badge {
          display: flex;
          align-items: center;
          gap: 0.3rem;
          background: var(--bg-glass);
          padding: 0.3rem 0.6rem;
          border-radius: var(--radius-sm);
        }

        .star {
          font-size: 0.9rem;
        }

        .rating-value {
          font-weight: 600;
          color: var(--text-primary);
        }

        .tutor-bio {
          color: var(--text-secondary);
          margin: 1rem 0;
          line-height: 1.6;
        }

        .tutor-stats {
          display: flex;
          gap: 1rem;
          margin-bottom: 1rem;
        }

        .tutor-stats .stat {
          display: flex;
          align-items: center;
          gap: 0.3rem;
          font-size: 0.9rem;
          color: var(--text-muted);
        }

        .tutor-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-top: 1rem;
          border-top: 1px solid var(--border-glass);
        }

        .tutor-price {
          display: flex;
          align-items: baseline;
          gap: 0.2rem;
        }

        .tutor-price .price {
          font-family: 'Poppins', sans-serif;
          font-size: 1.5rem;
          font-weight: 700;
          color: var(--accent);
        }

        .tutor-price .per-hour {
          color: var(--text-muted);
          font-size: 0.9rem;
        }

        .view-profile-btn {
          padding: 0.6rem 1.2rem;
          font-size: 0.9rem;
        }

        /* List View */
        .tutors-list {
          display: flex;
          flex-direction: column;
          gap: 1rem;
        }

        .tutors-list .tutor-card {
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 1.5rem;
          display: flex;
          gap: 1.5rem;
          align-items: center;
          transition: all var(--transition-normal);
        }

        .tutors-list .tutor-card:hover {
          border-color: rgba(102, 126, 234, 0.3);
        }

        .tutors-list .tutor-avatar {
          width: 80px;
          height: 80px;
          flex-shrink: 0;
          margin: 0;
        }

        .tutors-list .tutor-content {
          flex: 1;
          display: flex;
          align-items: center;
          gap: 2rem;
        }

        .tutors-list .tutor-header {
          flex: 1;
          margin: 0;
        }

        .tutors-list .tutor-bio {
          display: none;
        }

        .tutors-list .tutor-stats {
          flex-direction: column;
          gap: 0.3rem;
          margin: 0;
        }

        .tutors-list .tutor-footer {
          border: none;
          padding: 0;
          flex-direction: column;
          gap: 0.5rem;
        }

        /* Become Tutor Section */
        .become-tutor-section {
          padding: 4rem 2rem;
          margin-top: 2rem;
        }

        .become-tutor-content {
          max-width: 1000px;
          margin: 0 auto;
          background: var(--success-gradient);
          border-radius: var(--radius-lg);
          padding: 3rem;
          display: flex;
          justify-content: space-between;
          align-items: center;
          gap: 2rem;
          flex-wrap: wrap;
        }

        .become-tutor-text h2 {
          font-family: 'Poppins', sans-serif;
          font-size: 2rem;
          font-weight: 700;
          margin-bottom: 0.5rem;
        }

        .become-tutor-text p {
          opacity: 0.9;
        }

        .become-tutor-btn {
          background: white !important;
          color: #333 !important;
          border: none !important;
        }

        .become-tutor-btn:hover {
          background: var(--text-primary) !important;
        }

        @media (max-width: 768px) {
          .tutors-grid {
            grid-template-columns: 1fr;
          }

          .tutors-list .tutor-card {
            flex-direction: column;
            text-align: center;
          }

          .tutors-list .tutor-content {
            flex-direction: column;
            gap: 1rem;
          }

          .tutors-list .tutor-stats {
            flex-direction: row;
            justify-content: center;
          }

          .become-tutor-content {
            text-align: center;
            justify-content: center;
          }

          .search-form {
            flex-direction: column;
          }

          .search-input-wrapper {
            min-width: 100%;
          }

          .filter-group {
            width: 100%;
            justify-content: space-between;
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

export default Tutors;
