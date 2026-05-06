import React, { useState, useEffect } from 'react';
import { bookingService, sessionService, tutorService } from '../services/api';

const TutorDashboard = () => {
  const [bookings, setBookings] = useState([]);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [activeTab, setActiveTab] = useState('overview');
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    subject: '',
    startTime: '',
    endTime: '',
    price: '',
    maxStudents: 1,
    type: 'ONE_ON_ONE'
  });
  const [formErrors, setFormErrors] = useState({});

  // Profile form state
  const [profileFormData, setProfileFormData] = useState({
    bio: '',
    subject: '',
    subjects: [],
    hourlyRate: '',
    yearsOfExperience: ''
  });
  const [profileFormErrors, setProfileFormErrors] = useState({});

  useEffect(() => {
    loadData();
  }, []);

  // Auto-clear success/error messages after 5 seconds
  useEffect(() => {
    if (success || error) {
      const timer = setTimeout(() => {
        setSuccess('');
        setError('');
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [success, error]);

  const loadData = async () => {
    setLoading(true);
    setError('');
    try {
      // Load bookings
      const bookingsRes = await bookingService.getTutorBookings();
      setBookings(bookingsRes.data || []);

      // Load profile separately - it's okay if it doesn't exist yet
      try {
        const profileRes = await tutorService.getProfile();
        if (profileRes.data && Object.keys(profileRes.data).length > 0) {
          setProfile(profileRes.data);
        } else {
          setProfile(null);
        }
      } catch (profileErr) {
        // Profile doesn't exist yet - this is normal for new tutors
        console.log('No tutor profile found:', profileErr.response?.status);
        setProfile(null);
      }
    } catch (err) {
      console.error('Failed to load data', err);
      setError('Failed to load dashboard data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.title.trim()) errors.title = 'Title is required';
    if (!formData.subject.trim()) errors.subject = 'Subject is required';
    if (!formData.startTime) errors.startTime = 'Start time is required';
    if (!formData.endTime) errors.endTime = 'End time is required';
    if (!formData.price || formData.price <= 0) errors.price = 'Valid price is required';
    if (formData.startTime && formData.endTime && new Date(formData.startTime) >= new Date(formData.endTime)) {
      errors.endTime = 'End time must be after start time';
    }
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleCreateSession = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!validateForm()) return;

    try {
      // Convert price to number before sending
      const sessionData = {
        ...formData,
        price: parseFloat(formData.price),
        maxStudents: parseInt(formData.maxStudents)
      };
      console.log('Creating session with data:', sessionData);
      const response = await sessionService.create(sessionData);
      console.log('Session created:', response.data);
      setSuccess('✅ Session created successfully!');
      setShowForm(false);
      setFormData({
        title: '',
        description: '',
        subject: '',
        startTime: '',
        endTime: '',
        price: '',
        maxStudents: 1,
        type: 'ONE_ON_ONE'
      });
      setFormErrors({});
      await loadData();
    } catch (err) {
      console.error('Failed to create session', err);
      const errorMessage = err.response?.data?.message || err.message || 'Failed to create session. Please check all fields.';
      setError(`❌ ${errorMessage}`);
    }
  };

  // Profile creation handler
  const validateProfileForm = () => {
    const errors = {};
    if (!profileFormData.bio.trim()) errors.bio = 'Bio is required';
    if (!profileFormData.subject.trim()) errors.subject = 'Subject is required';
    if (!profileFormData.hourlyRate || profileFormData.hourlyRate <= 0) errors.hourlyRate = 'Valid hourly rate is required';
    if (!profileFormData.yearsOfExperience || profileFormData.yearsOfExperience < 0) errors.yearsOfExperience = 'Valid experience is required';
    setProfileFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleCreateProfile = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!validateProfileForm()) return;

    try {
      // Convert numeric fields and prepare data
      const profileData = {
        bio: profileFormData.bio,
        subject: profileFormData.subject,
        subjects: profileFormData.subject ? [profileFormData.subject] : [],
        hourlyRate: parseFloat(profileFormData.hourlyRate),
        yearsOfExperience: parseInt(profileFormData.yearsOfExperience)
      };
      console.log('Creating profile with data:', profileData);
      const response = await tutorService.createProfile(profileData);
      console.log('Profile created:', response.data);
      setSuccess('✅ Profile created successfully!');
      setProfileFormData({
        bio: '',
        subject: '',
        subjects: [],
        hourlyRate: '',
        yearsOfExperience: ''
      });
      setProfileFormErrors({});
      await loadData();
      setActiveTab('overview');
    } catch (err) {
      console.error('Failed to create profile', err);
      // Handle specific error cases
      if (err.response?.status === 403) {
        setError('❌ You already have a profile. Refreshing data...');
        // Try to reload profile data
        await loadData();
      } else {
        const errorMessage = err.response?.data?.message || err.message || 'Failed to create profile. Please check all fields.';
        setError(`❌ ${errorMessage}`);
      }
    }
  };

  const handleConfirm = async (bookingId) => {
    try {
      await bookingService.confirm(bookingId);
      setSuccess('✅ Booking confirmed!');
      loadData();
    } catch (err) {
      setError('Failed to confirm booking');
    }
  };

  const handleReject = async (bookingId) => {
    try {
      await bookingService.reject(bookingId);
      setSuccess('❌ Booking rejected');
      loadData();
    } catch (err) {
      setError('Failed to reject booking');
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

  if (loading) {
    return (
      <div className="tutor-dashboard loading-container">
        <div className="loading-spinner-large"></div>
        <p>Loading dashboard...</p>
      </div>
    );
  }

  return (
    <div className="tutor-dashboard">
      {/* Alerts */}
      {error && (
        <div className="alert alert-error">
          <span className="alert-icon">⚠️</span>
          {error}
          <button className="alert-close" onClick={() => setError('')}>×</button>
        </div>
      )}
      {success && (
        <div className="alert alert-success">
          <span className="alert-icon">✅</span>
          {success}
          <button className="alert-close" onClick={() => setSuccess('')}>×</button>
        </div>
      )}

      {/* Stats Overview */}
      <div className="stats-row">
        <div className="stat-mini">
          <span className="stat-icon">📊</span>
          <div>
            <span className="stat-value">{profile?.totalSessions || 0}</span>
            <span className="stat-label">Total Sessions</span>
          </div>
        </div>
        <div className="stat-mini">
          <span className="stat-icon">⭐</span>
          <div>
            <span className="stat-value">{profile?.averageRating?.toFixed(1) || 'New'}</span>
            <span className="stat-label">Rating</span>
          </div>
        </div>
        <div className="stat-mini">
          <span className="stat-icon">⏳</span>
          <div>
            <span className="stat-value">{bookings.filter(b => b.status === 'PENDING').length}</span>
            <span className="stat-label">Pending Bookings</span>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="dashboard-tabs">
        <button className={activeTab === 'overview' ? 'active' : ''} onClick={() => setActiveTab('overview')}>
          📊 Overview
        </button>
        <button className={activeTab === 'bookings' ? 'active' : ''} onClick={() => setActiveTab('bookings')}>
          📅 Bookings ({bookings.length})
        </button>
        <button className={activeTab === 'sessions' ? 'active' : ''} onClick={() => setActiveTab('sessions')}>
          📝 Create Session
        </button>
        {!profile && (
          <button className={activeTab === 'profile' ? 'active' : ''} onClick={() => setActiveTab('profile')}>
            ➕ Create Profile
          </button>
        )}
      </div>

      {/* Overview Tab */}
      {activeTab === 'overview' && (
        <div className="tab-content">
          <div className="profile-section">
            <h3>My Profile</h3>
            {profile ? (
              <div className="profile-card-modern">
                <div className="profile-header">
                  <div className="profile-avatar" style={{ background: 'var(--primary-gradient)' }}>
                    {profile.user?.firstName?.charAt(0)}
                  </div>
                  <div className="profile-info">
                    <h4>{profile.user?.firstName} {profile.user?.lastName}</h4>
                    <span className="profile-status" style={{ background: profile.status === 'APPROVED' ? 'var(--success)' : 'var(--warning)' }}>
                      {profile.status}
                    </span>
                  </div>
                </div>
                <div className="profile-details">
                  <div className="detail-row">
                    <span className="detail-label">Subject</span>
                    <span className="detail-value">{profile.subject}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Hourly Rate</span>
                    <span className="detail-value price">${profile.hourlyRate}/hr</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Experience</span>
                    <span className="detail-value">{profile.yearsOfExperience} years</span>
                  </div>
                  <div className="detail-row full-width">
                    <span className="detail-label">Bio</span>
                    <p className="bio-text">{profile.bio}</p>
                  </div>
                </div>
              </div>
            ) : (
              <div className="empty-state">
                <span className="empty-icon">👤</span>
                <h4>No profile created yet</h4>
                <p>Create your tutor profile to start offering sessions and earning money!</p>
                <button className="btn-create-profile" onClick={() => setActiveTab('profile')}>
                  ➕ Create Profile
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Bookings Tab */}
      {activeTab === 'bookings' && (
        <div className="tab-content">
          <h3>All Bookings</h3>
          {bookings.length === 0 ? (
            <div className="empty-state">
              <span className="empty-icon">📭</span>
              <h4>No bookings yet</h4>
              <p>Create sessions to start receiving bookings!</p>
            </div>
          ) : (
            <div className="bookings-modern">
              {bookings.map((booking) => (
                <div key={booking.id} className="booking-card-modern">
                  <div className="booking-header">
                    <div>
                      <h4>{booking.session?.title}</h4>
                      <p className="booking-meta">
                        <span>👤 {booking.student?.firstName} {booking.student?.lastName}</span>
                        <span>💰 ${booking.amount}</span>
                      </p>
                    </div>
                    {getStatusBadge(booking.status)}
                  </div>
                  {booking.status === 'PENDING' && (
                    <div className="booking-actions-modern">
                      <button onClick={() => handleConfirm(booking.id)} className="btn-confirm">
                        ✅ Confirm
                      </button>
                      <button onClick={() => handleReject(booking.id)} className="btn-reject">
                        ❌ Reject
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Create Session Tab */}
      {activeTab === 'sessions' && (
        <div className="tab-content">
          <div className="create-session-section">
            <h3>Create New Session</h3>
            {!profile ? (
              <div className="create-session-intro">
                <span className="intro-icon">⚠️</span>
                <h4>Profile Required</h4>
                <p>You need to create a tutor profile before you can create sessions.</p>
                <button className="btn-create-session" onClick={() => setActiveTab('profile')}>
                  <span>➕</span> Create Profile First
                </button>
              </div>
            ) : !showForm ? (
              <div className="create-session-intro">
                <span className="intro-icon">✨</span>
                <h4>Ready to teach?</h4>
                <p>Create a new session and start earning by sharing your knowledge.</p>
                <button className="btn-create-session" onClick={() => { setShowForm(true); setError(''); setSuccess(''); }}>
                  <span>➕</span> Create New Session
                </button>
              </div>
            ) : (
              <div className="session-form-container">
                <form onSubmit={handleCreateSession} className="session-form">
                  <div className="form-row">
                    <div className="form-group">
                      <label>
                        <span className="input-icon">📚</span>
                        Session Title *
                      </label>
                      <input
                        type="text"
                        placeholder="e.g., Advanced Calculus - Part 1"
                        value={formData.title}
                        onChange={(e) => setFormData({...formData, title: e.target.value})}
                        className={formErrors.title ? 'error' : ''}
                      />
                      {formErrors.title && <span className="field-error">{formErrors.title}</span>}
                    </div>
                    <div className="form-group">
                      <label>
                        <span className="input-icon">🎓</span>
                        Subject *
                      </label>
                      <input
                        type="text"
                        placeholder="e.g., Mathematics"
                        value={formData.subject}
                        onChange={(e) => setFormData({...formData, subject: e.target.value})}
                        className={formErrors.subject ? 'error' : ''}
                      />
                      {formErrors.subject && <span className="field-error">{formErrors.subject}</span>}
                    </div>
                  </div>

                  <div className="form-group">
                    <label>
                      <span className="input-icon">📝</span>
                      Description
                    </label>
                    <textarea
                      rows="3"
                      placeholder="Describe what students will learn in this session..."
                      value={formData.description}
                      onChange={(e) => setFormData({...formData, description: e.target.value})}
                    />
                  </div>

                  <div className="form-row">
                    <div className="form-group">
                      <label>
                        <span className="input-icon">📅</span>
                        Start Time *
                      </label>
                      <input
                        type="datetime-local"
                        value={formData.startTime}
                        onChange={(e) => setFormData({...formData, startTime: e.target.value})}
                        className={formErrors.startTime ? 'error' : ''}
                      />
                      {formErrors.startTime && <span className="field-error">{formErrors.startTime}</span>}
                    </div>
                    <div className="form-group">
                      <label>
                        <span className="input-icon">📅</span>
                        End Time *
                      </label>
                      <input
                        type="datetime-local"
                        value={formData.endTime}
                        onChange={(e) => setFormData({...formData, endTime: e.target.value})}
                        className={formErrors.endTime ? 'error' : ''}
                      />
                      {formErrors.endTime && <span className="field-error">{formErrors.endTime}</span>}
                    </div>
                  </div>

                  <div className="form-row">
                    <div className="form-group">
                      <label>
                        <span className="input-icon">💰</span>
                        Price ($) *
                      </label>
                      <input
                        type="number"
                        min="1"
                        placeholder="50"
                        value={formData.price}
                        onChange={(e) => setFormData({...formData, price: e.target.value})}
                        className={formErrors.price ? 'error' : ''}
                      />
                      {formErrors.price && <span className="field-error">{formErrors.price}</span>}
                    </div>
                    <div className="form-group">
                      <label>
                        <span className="input-icon">👥</span>
                        Max Students
                      </label>
                      <input
                        type="number"
                        min="1"
                        max="100"
                        value={formData.maxStudents}
                        onChange={(e) => setFormData({...formData, maxStudents: parseInt(e.target.value)})}
                      />
                    </div>
                    <div className="form-group">
                      <label>
                        <span className="input-icon">📌</span>
                        Type
                      </label>
                      <select
                        value={formData.type}
                        onChange={(e) => setFormData({...formData, type: e.target.value})}
                      >
                        <option value="ONE_ON_ONE">One on One</option>
                        <option value="GROUP">Group Session</option>
                      </select>
                    </div>
                  </div>

                  <div className="form-actions">
                    <button type="button" className="btn-secondary" onClick={() => { setShowForm(false); setFormErrors({}); setError(''); setSuccess(''); }}>
                      Cancel
                    </button>
                    <button type="submit" className="btn-primary">
                      <span>✨</span> Create Session
                    </button>
                  </div>
                </form>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Create Profile Tab */}
      {activeTab === 'profile' && (
        <div className="tab-content">
          <div className="create-profile-section">
            <h3>Create Your Tutor Profile</h3>
            <div className="profile-form-container">
              <form onSubmit={handleCreateProfile} className="profile-form">
                <div className="form-row">
                  <div className="form-group">
                    <label>
                      <span className="input-icon">🎓</span>
                      Subject *
                    </label>
                    <input
                      type="text"
                      placeholder="e.g., Mathematics"
                      value={profileFormData.subject}
                      onChange={(e) => setProfileFormData({...profileFormData, subject: e.target.value})}
                      className={profileFormErrors.subject ? 'error' : ''}
                    />
                    {profileFormErrors.subject && <span className="field-error">{profileFormErrors.subject}</span>}
                  </div>
                  <div className="form-group">
                    <label>
                      <span className="input-icon">💰</span>
                      Hourly Rate ($) *
                    </label>
                    <input
                      type="number"
                      min="1"
                      placeholder="50"
                      value={profileFormData.hourlyRate}
                      onChange={(e) => setProfileFormData({...profileFormData, hourlyRate: e.target.value})}
                      className={profileFormErrors.hourlyRate ? 'error' : ''}
                    />
                    {profileFormErrors.hourlyRate && <span className="field-error">{profileFormErrors.hourlyRate}</span>}
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>
                      <span className="input-icon">📅</span>
                      Years of Experience *
                    </label>
                    <input
                      type="number"
                      min="0"
                      placeholder="5"
                      value={profileFormData.yearsOfExperience}
                      onChange={(e) => setProfileFormData({...profileFormData, yearsOfExperience: e.target.value})}
                      className={profileFormErrors.yearsOfExperience ? 'error' : ''}
                    />
                    {profileFormErrors.yearsOfExperience && <span className="field-error">{profileFormErrors.yearsOfExperience}</span>}
                  </div>
                </div>

                <div className="form-group">
                  <label>
                    <span className="input-icon">📝</span>
                    Bio *
                  </label>
                  <textarea
                    rows="4"
                    placeholder="Tell students about yourself, your teaching style, and experience..."
                    value={profileFormData.bio}
                    onChange={(e) => setProfileFormData({...profileFormData, bio: e.target.value})}
                    className={profileFormErrors.bio ? 'error' : ''}
                  />
                  {profileFormErrors.bio && <span className="field-error">{profileFormErrors.bio}</span>}
                </div>

                <div className="form-actions">
                  <button type="button" className="btn-secondary" onClick={() => { setActiveTab('overview'); setProfileFormErrors({}); }}>
                    Cancel
                  </button>
                  <button type="submit" className="btn-primary">
                    <span>✨</span> Create Profile
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      <style>{`
        .tutor-dashboard {
          animation: fadeIn 0.5s ease-out;
        }

        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }

        /* Alerts */
        .alert {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          padding: 1rem;
          border-radius: var(--radius-md);
          margin-bottom: 1rem;
          position: relative;
        }

        .alert-error {
          background: rgba(245, 87, 108, 0.1);
          border: 1px solid rgba(245, 87, 108, 0.3);
          color: #ff6b8a;
        }

        .alert-success {
          background: rgba(67, 233, 123, 0.1);
          border: 1px solid rgba(67, 233, 123, 0.3);
          color: #43e97b;
        }

        .alert-close {
          margin-left: auto;
          background: none;
          border: none;
          color: inherit;
          font-size: 1.5rem;
          cursor: pointer;
          opacity: 0.7;
        }

        .alert-close:hover {
          opacity: 1;
        }

        /* Stats */
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

        /* Tabs */
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
        }

        .tab-content {
          min-height: 200px;
        }

        /* Profile Section */
        .profile-section h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.3rem;
          margin-bottom: 1rem;
        }

        .profile-card-modern {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 1.5rem;
        }

        .profile-header {
          display: flex;
          align-items: center;
          gap: 1rem;
          margin-bottom: 1.5rem;
          padding-bottom: 1rem;
          border-bottom: 1px solid var(--border-glass);
        }

        .profile-avatar {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.5rem;
          font-weight: 700;
          color: white;
        }

        .profile-info h4 {
          font-family: 'Poppins', sans-serif;
          margin: 0 0 0.25rem 0;
        }

        .profile-status {
          display: inline-block;
          padding: 0.2rem 0.6rem;
          border-radius: var(--radius-sm);
          font-size: 0.75rem;
          font-weight: 600;
          color: white;
          text-transform: uppercase;
        }

        .profile-details {
          display: grid;
          grid-template-columns: repeat(2, 1fr);
          gap: 1rem;
        }

        .detail-row {
          display: flex;
          flex-direction: column;
          gap: 0.25rem;
        }

        .detail-row.full-width {
          grid-column: span 2;
        }

        .detail-label {
          font-size: 0.85rem;
          color: var(--text-muted);
        }

        .detail-value {
          font-weight: 600;
          color: var(--text-primary);
        }

        .detail-value.price {
          color: var(--accent);
        }

        .bio-text {
          color: var(--text-secondary);
          line-height: 1.6;
          margin: 0;
        }

        /* Bookings */
        .bookings-modern {
          display: flex;
          flex-direction: column;
          gap: 1rem;
        }

        .booking-card-modern {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          padding: 1.25rem;
          transition: all var(--transition-normal);
        }

        .booking-card-modern:hover {
          border-color: rgba(102, 126, 234, 0.3);
        }

        .booking-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 0.75rem;
        }

        .booking-header h4 {
          font-family: 'Poppins', sans-serif;
          margin: 0 0 0.5rem 0;
        }

        .booking-meta {
          display: flex;
          gap: 1rem;
          color: var(--text-secondary);
          font-size: 0.9rem;
        }

        .booking-actions-modern {
          display: flex;
          gap: 0.75rem;
          margin-top: 1rem;
          padding-top: 1rem;
          border-top: 1px solid var(--border-glass);
        }

        .btn-confirm, .btn-reject {
          padding: 0.6rem 1.2rem;
          border: none;
          border-radius: var(--radius-md);
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .btn-confirm {
          background: var(--success-gradient);
          color: white;
        }

        .btn-confirm:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 20px rgba(67, 233, 123, 0.4);
        }

        .btn-reject {
          background: var(--bg-glass);
          color: var(--text-secondary);
          border: 1px solid var(--border-glass);
        }

        .btn-reject:hover {
          background: rgba(245, 87, 108, 0.2);
          color: #ff6b8a;
          border-color: rgba(245, 87, 108, 0.3);
        }

        /* Create Session */
        .create-session-section h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.3rem;
          margin-bottom: 1rem;
        }

        .create-session-intro {
          text-align: center;
          padding: 3rem;
          background: var(--bg-glass);
          border: 1px dashed var(--border-glass);
          border-radius: var(--radius-lg);
        }

        .intro-icon {
          font-size: 3rem;
          display: block;
          margin-bottom: 1rem;
        }

        .create-session-intro h4 {
          font-family: 'Poppins', sans-serif;
          margin: 0 0 0.5rem 0;
        }

        .create-session-intro p {
          color: var(--text-secondary);
          margin-bottom: 1.5rem;
        }

        .btn-create-session {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          padding: 1rem 2rem;
          background: var(--success-gradient);
          color: white;
          border: none;
          border-radius: var(--radius-xl);
          font-size: 1.1rem;
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .btn-create-session:hover {
          transform: translateY(-3px);
          box-shadow: 0 15px 30px rgba(67, 233, 123, 0.4);
        }

        /* Session Form */
        .session-form-container {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 2rem;
        }

        .session-form {
          display: flex;
          flex-direction: column;
          gap: 1.25rem;
        }

        .session-form .form-row {
          display: grid;
          grid-template-columns: repeat(2, 1fr);
          gap: 1rem;
        }

        .session-form .form-row:last-child {
          grid-template-columns: repeat(3, 1fr);
        }

        .session-form .form-group {
          display: flex;
          flex-direction: column;
          gap: 0.5rem;
        }

        .session-form label {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          font-size: 0.95rem;
          color: var(--text-secondary);
        }

        .input-icon {
          font-size: 1.1rem;
        }

        .session-form input,
        .session-form textarea,
        .session-form select {
          padding: 0.9rem 1rem;
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          color: var(--text-primary);
          font-size: 1rem;
          transition: all var(--transition-normal);
        }

        .session-form input:focus,
        .session-form textarea:focus,
        .session-form select:focus {
          outline: none;
          border-color: var(--primary);
          box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15);
        }

        .session-form input.error,
        .session-form textarea.error {
          border-color: #f5576c;
          background: rgba(245, 87, 108, 0.05);
        }

        .field-error {
          color: #ff6b8a;
          font-size: 0.85rem;
        }

        .form-actions {
          display: flex;
          justify-content: flex-end;
          gap: 1rem;
          margin-top: 1rem;
          padding-top: 1.5rem;
          border-top: 1px solid var(--border-glass);
        }

        .form-actions button {
          padding: 0.9rem 2rem;
          border-radius: var(--radius-md);
          font-size: 1rem;
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
        }

        .form-actions .btn-secondary {
          background: transparent;
          color: var(--text-secondary);
          border: 1px solid var(--border-glass);
        }

        .form-actions .btn-secondary:hover {
          background: var(--bg-glass);
          color: var(--text-primary);
        }

        .form-actions .btn-primary {
          background: var(--success-gradient);
          color: white;
          border: none;
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .form-actions .btn-primary:hover {
          transform: translateY(-2px);
          box-shadow: 0 10px 25px rgba(67, 233, 123, 0.4);
        }

        /* Empty State */
        .empty-state {
          text-align: center;
          padding: 3rem;
          background: var(--bg-glass);
          border: 1px dashed var(--border-glass);
          border-radius: var(--radius-lg);
        }

        .empty-icon {
          font-size: 3rem;
          display: block;
          margin-bottom: 1rem;
        }

        .empty-state h4 {
          font-family: 'Poppins', sans-serif;
          margin: 0 0 0.5rem 0;
        }

        .empty-state p {
          color: var(--text-secondary);
          margin: 0;
        }

        /* Create Profile Button in Empty State */
        .btn-create-profile {
          display: inline-flex;
          align-items: center;
          gap: 0.5rem;
          padding: 0.9rem 1.8rem;
          background: var(--primary-gradient);
          color: white;
          border: none;
          border-radius: var(--radius-xl);
          font-size: 1rem;
          font-weight: 600;
          cursor: pointer;
          transition: all var(--transition-normal);
          margin-top: 1rem;
        }

        .btn-create-profile:hover {
          transform: translateY(-3px);
          box-shadow: var(--shadow-glow);
        }

        /* Create Profile Section */
        .create-profile-section h3 {
          font-family: 'Poppins', sans-serif;
          font-size: 1.3rem;
          margin-bottom: 1rem;
        }

        .profile-form-container {
          background: var(--bg-glass);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-lg);
          padding: 2rem;
        }

        .profile-form {
          display: flex;
          flex-direction: column;
          gap: 1.25rem;
        }

        .profile-form .form-row {
          display: grid;
          grid-template-columns: repeat(2, 1fr);
          gap: 1rem;
        }

        .profile-form .form-group {
          display: flex;
          flex-direction: column;
          gap: 0.5rem;
        }

        .profile-form label {
          display: flex;
          align-items: center;
          gap: 0.5rem;
          font-size: 0.95rem;
          color: var(--text-secondary);
        }

        .profile-form input,
        .profile-form textarea {
          padding: 0.9rem 1rem;
          background: var(--bg-card);
          border: 1px solid var(--border-glass);
          border-radius: var(--radius-md);
          color: var(--text-primary);
          font-size: 1rem;
          transition: all var(--transition-normal);
        }

        .profile-form input:focus,
        .profile-form textarea:focus {
          outline: none;
          border-color: var(--primary);
          box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15);
        }

        .profile-form input.error,
        .profile-form textarea.error {
          border-color: #f5576c;
          background: rgba(245, 87, 108, 0.05);
        }

        /* Loading */
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
          border: 4px solid var(--border-glass);
          border-top-color: var(--primary);
          border-radius: 50%;
          animation: spin 1s linear infinite;
          margin-bottom: 1rem;
        }

        @keyframes spin {
          to { transform: rotate(360deg); }
        }

        @media (max-width: 768px) {
          .stats-row {
            grid-template-columns: 1fr;
          }

          .profile-details {
            grid-template-columns: 1fr;
          }

          .detail-row.full-width {
            grid-column: span 1;
          }

          .session-form .form-row,
          .session-form .form-row:last-child {
            grid-template-columns: 1fr;
          }

          .dashboard-tabs {
            flex-direction: column;
          }

          .form-actions {
            flex-direction: column;
          }

          .form-actions button {
            width: 100%;
          }
        }
      `}</style>
    </div>
  );
};

export default TutorDashboard;
