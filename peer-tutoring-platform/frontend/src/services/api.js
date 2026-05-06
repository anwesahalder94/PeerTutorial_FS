import axios from 'axios';

// Use relative URL to leverage the proxy (configured in package.json)
// This avoids CORS issues during development
const API_URL = process.env.NODE_ENV === 'production'
  ? 'http://localhost:8080/api'
  : '/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('🔑 Adding auth token to request:', config.url);
    } else {
      console.warn('⚠️ No auth token found for request:', config.url);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    // Handle both 401 (Unauthorized) and 403 (Forbidden) as auth failures
    if (status === 401 || status === 403) {
      console.error(`Authentication error (${status}):`, error.response?.data);

      // Don't redirect if already on login page
      const currentPath = window.location.pathname;
      if (currentPath !== '/login' && currentPath !== '/register') {
        // Clear auth data
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        // Redirect to login
        window.location.href = '/login?error=session_expired';
      }
    }

    return Promise.reject(error);
  }
);

export const authService = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (userData) => api.post('/auth/register', userData),
  getCurrentUser: () => api.get('/auth/me'),
};

export const sessionService = {
  getAll: () => api.get('/sessions'),
  getAvailable: () => api.get('/sessions/available'),
  getById: (id) => api.get(`/sessions/${id}`),
  getByTutor: (tutorId) => api.get(`/sessions/tutor/${tutorId}`),
  create: (data) => api.post('/sessions', data),
  update: (id, data) => api.put(`/sessions/${id}`, data),
  delete: (id) => api.delete(`/sessions/${id}`),
};

export const bookingService = {
  getStudentBookings: () => api.get('/student/bookings'),
  getTutorBookings: () => api.get('/tutor/bookings'),
  create: (data) => api.post('/student/bookings', data),
  confirm: (bookingId) => api.post(`/tutor/bookings/${bookingId}/confirm`),
  reject: (bookingId) => api.post(`/tutor/bookings/${bookingId}/reject`),
  complete: (bookingId) => api.post(`/tutor/bookings/${bookingId}/complete`),
};

export const tutorService = {
  getAll: () => api.get('/tutors'),
  getById: (id) => api.get(`/tutors/${id}`),
  search: (params) => api.get('/tutors/search', { params }),
  getProfile: () => api.get('/tutor/profile'),
  createProfile: (data) => api.post('/tutor/profile', data),
  updateProfile: (data) => api.put('/tutor/profile', data),
  getRatings: (tutorId) => api.get(`/tutors/${tutorId}/ratings`),
  createRating: (data) => api.post('/student/ratings', data),
};

export const adminService = {
  getDashboard: () => api.get('/admin/dashboard'),
  getUsers: () => api.get('/admin/users'),
  deactivateUser: (userId) => api.post(`/admin/users/${userId}/deactivate`),
  getPendingTutors: () => api.get('/admin/tutors/pending'),
  approveTutor: (profileId) => api.post(`/admin/tutors/${profileId}/approve`),
  rejectTutor: (profileId) => api.post(`/admin/tutors/${profileId}/reject`),
  getPayouts: () => api.get('/admin/payouts'),
  createPayout: (data) => api.post('/admin/payouts', data),
  processPayout: (payoutId) => api.post(`/admin/payouts/${payoutId}/process`),
};

export default api;
