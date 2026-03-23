import React, { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const AuthPage = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    fullName: '', email: '', password: '', phone: '', role: 'CANDIDATE'
  });

  const { user, login, register } = useAuth();
  const navigate = useNavigate();

  if (user) {
    return <Navigate to={user.role === 'CANDIDATE' ? '/dashboard' : '/recruiter/dashboard'} replace />;
  }

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const userData = isLogin
        ? await login(form.email, form.password)
        : await register(form);
      navigate(userData.role === 'CANDIDATE' ? '/dashboard' : '/recruiter/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.bg1} />
      <div style={styles.bg2} />

      <div style={styles.container}>
        <div style={styles.header}>
          <div style={styles.logo}>⚡ InterviewAI</div>
          <p style={styles.tagline}>Automated Interview Evaluation Platform</p>
        </div>

        <div style={styles.card}>
          <div style={styles.tabs}>
            <button style={{ ...styles.tab, ...(isLogin ? styles.tabActive : {}) }} onClick={() => { setIsLogin(true); setError(''); }}>
              Sign In
            </button>
            <button style={{ ...styles.tab, ...(!isLogin ? styles.tabActive : {}) }} onClick={() => { setIsLogin(false); setError(''); }}>
              Register
            </button>
          </div>

          {error && <div style={styles.errorBox}>{error}</div>}

          <div style={styles.demo}>
            <strong>Demo Accounts:</strong><br />
            Candidate: candidate@demo.com / password123<br />
            Recruiter: recruiter@demo.com / password123
          </div>

          <form onSubmit={handleSubmit}>
            {!isLogin && (
              <>
                <div className="form-group">
                  <label className="form-label">Full Name</label>
                  <input className="form-input" name="fullName" value={form.fullName}
                    onChange={handleChange} placeholder="John Doe" required />
                </div>
                <div className="form-group">
                  <label className="form-label">Role</label>
                  <select className="form-select" name="role" value={form.role} onChange={handleChange}>
                    <option value="CANDIDATE">Candidate</option>
                    <option value="RECRUITER">Recruiter</option>
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Phone (optional)</label>
                  <input className="form-input" name="phone" value={form.phone}
                    onChange={handleChange} placeholder="+1 555 0000" />
                </div>
              </>
            )}
            <div className="form-group">
              <label className="form-label">Email Address</label>
              <input className="form-input" type="email" name="email" value={form.email}
                onChange={handleChange} placeholder="you@example.com" required />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input className="form-input" type="password" name="password" value={form.password}
                onChange={handleChange} placeholder="••••••••" required />
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '12px' }} disabled={loading}>
              {loading ? 'Please wait...' : isLogin ? 'Sign In' : 'Create Account'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

const styles = {
  page: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    background: '#0a0e1a',
    position: 'relative',
    overflow: 'hidden',
  },
  bg1: {
    position: 'absolute', top: '-200px', left: '-200px',
    width: '600px', height: '600px',
    background: 'radial-gradient(circle, rgba(59,130,246,0.12) 0%, transparent 70%)',
    borderRadius: '50%',
  },
  bg2: {
    position: 'absolute', bottom: '-200px', right: '-200px',
    width: '600px', height: '600px',
    background: 'radial-gradient(circle, rgba(139,92,246,0.10) 0%, transparent 70%)',
    borderRadius: '50%',
  },
  container: { position: 'relative', zIndex: 1, width: '100%', maxWidth: '420px', padding: '0 24px' },
  header: { textAlign: 'center', marginBottom: '32px' },
  logo: { fontSize: '28px', fontWeight: '800', color: '#f1f5f9', marginBottom: '8px' },
  tagline: { color: '#64748b', fontSize: '14px' },
  card: {
    background: '#1a2236',
    border: '1px solid #2a3a5c',
    borderRadius: '16px',
    padding: '32px',
  },
  tabs: {
    display: 'flex',
    background: '#111827',
    borderRadius: '10px',
    padding: '4px',
    marginBottom: '24px',
  },
  tab: {
    flex: 1, padding: '8px', border: 'none', borderRadius: '8px',
    cursor: 'pointer', fontFamily: 'inherit', fontSize: '14px',
    fontWeight: '500', color: '#64748b', background: 'transparent', transition: 'all 0.2s',
  },
  tabActive: { background: '#3b82f6', color: 'white' },
  errorBox: {
    background: 'rgba(239,68,68,0.1)',
    border: '1px solid rgba(239,68,68,0.2)',
    color: '#ef4444',
    padding: '12px',
    borderRadius: '8px',
    fontSize: '13px',
    marginBottom: '16px',
  },
  demo: {
    background: 'rgba(59,130,246,0.08)',
    border: '1px solid rgba(59,130,246,0.2)',
    color: '#94a3b8',
    padding: '12px',
    borderRadius: '8px',
    fontSize: '12px',
    marginBottom: '20px',
    lineHeight: '1.8',
  },
};

export default AuthPage;
