import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/common/Navbar';
import AuthPage from './pages/AuthPage';
import CandidateDashboard from './pages/CandidateDashboard';
import CodingPage from './pages/CodingPage';
import VideoInterviewPage from './pages/VideoInterviewPage';
import MySubmissionsPage from './pages/MySubmissionsPage';
import RecruiterDashboard from './pages/RecruiterDashboard';
import CandidatesListPage from './pages/CandidatesListPage';
import CandidateDetailPage from './pages/CandidateDetailPage';
import './index.css';

const ProtectedRoute = ({ children, requiredRole }) => {
  const { user, loading } = useAuth();
  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;
  if (!user) return <Navigate to="/login" replace />;
  if (requiredRole && user.role !== requiredRole && user.role !== 'ADMIN') {
    return <Navigate to={user.role === 'CANDIDATE' ? '/dashboard' : '/recruiter/dashboard'} replace />;
  }
  return children;
};

const AppRoutes = () => {
  const { user } = useAuth();

  return (
    <>
      {user && <Navbar />}
      <Routes>
        <Route path="/login" element={<AuthPage />} />
        <Route path="/register" element={<AuthPage />} />

        {/* Candidate Routes */}
        <Route path="/dashboard" element={
          <ProtectedRoute requiredRole="CANDIDATE"><CandidateDashboard /></ProtectedRoute>
        } />
        <Route path="/coding" element={
          <ProtectedRoute requiredRole="CANDIDATE"><CodingPage /></ProtectedRoute>
        } />
        <Route path="/video-interview" element={
          <ProtectedRoute requiredRole="CANDIDATE"><VideoInterviewPage /></ProtectedRoute>
        } />
        <Route path="/my-submissions" element={
          <ProtectedRoute requiredRole="CANDIDATE"><MySubmissionsPage /></ProtectedRoute>
        } />

        {/* Recruiter Routes */}
        <Route path="/recruiter/dashboard" element={
          <ProtectedRoute requiredRole="RECRUITER"><RecruiterDashboard /></ProtectedRoute>
        } />
        <Route path="/recruiter/candidates" element={
          <ProtectedRoute requiredRole="RECRUITER"><CandidatesListPage /></ProtectedRoute>
        } />
        <Route path="/recruiter/candidates/:candidateId" element={
          <ProtectedRoute requiredRole="RECRUITER"><CandidateDetailPage /></ProtectedRoute>
        } />

        {/* Default redirect */}
        <Route path="/" element={
          user
            ? <Navigate to={user.role === 'CANDIDATE' ? '/dashboard' : '/recruiter/dashboard'} replace />
            : <Navigate to="/login" replace />
        } />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
};

function App() {
  return (
    <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}

export default App;
