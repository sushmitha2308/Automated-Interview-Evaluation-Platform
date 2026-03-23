import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:6060/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor - add JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - handle 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

// Coding API
export const codingAPI = {
  getQuestions: () => api.get('/coding/questions'),
  getQuestion: (id) => api.get(`/coding/questions/${id}`),
  submitSolution: (data) => api.post('/coding/submit', data),
  getMySubmissions: () => api.get('/coding/submissions'),
};

// Video API
export const videoAPI = {
  uploadVideo: (questionId, formData) => api.post(`/videos/upload/${questionId}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }),
  getMyVideos: () => api.get('/videos/my-videos'),
  reviewVideo: (videoId, data) => api.put(`/videos/${videoId}/review`, data),
  getStreamUrl: (videoId) => `${API_BASE_URL}/videos/stream/${videoId}`,
};

// Interview Questions API
export const interviewAPI = {
  getQuestions: () => api.get('/interview-questions'),
};

// Recruiter API
export const recruiterAPI = {
  getDashboardStats: () => api.get('/recruiter/dashboard/stats'),
  getAllCandidates: () => api.get('/recruiter/candidates'),
  getCandidateDetail: (id) => api.get(`/recruiter/candidates/${id}`),
  updateCandidateStatus: (id, data) => api.put(`/recruiter/candidates/${id}/status`, data),
};

export default api;
