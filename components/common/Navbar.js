import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isCandidate = user?.role === 'CANDIDATE';
  const isRecruiter = user?.role === 'RECRUITER' || user?.role === 'ADMIN';

  const navLinks = isCandidate
    ? [
        { to: '/dashboard', label: 'Dashboard' },
        { to: '/coding', label: 'Coding Tests' },
        { to: '/video-interview', label: 'Video Interview' },
        { to: '/my-submissions', label: 'My Results' },
      ]
    : [
        { to: '/recruiter/dashboard', label: 'Dashboard' },
        { to: '/recruiter/candidates', label: 'Candidates' },
      ];

  return (
    <nav style={styles.nav}>
      <div style={styles.container}>
        <Link to="/" style={styles.brand}>
          <span style={styles.brandIcon}>⚡</span>
          <span>InterviewAI</span>
        </Link>

        <div style={styles.links}>
          {navLinks.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              style={{
                ...styles.link,
                ...(location.pathname === link.to ? styles.linkActive : {}),
              }}
            >
              {link.label}
            </Link>
          ))}
        </div>

        <div style={styles.userSection}>
          <div style={styles.userInfo}>
            <div style={styles.avatar}>{user?.fullName?.charAt(0) || 'U'}</div>
            <div>
              <div style={styles.userName}>{user?.fullName}</div>
              <div style={styles.userRole}>{user?.role}</div>
            </div>
          </div>
          <button onClick={handleLogout} style={styles.logoutBtn}>Logout</button>
        </div>
      </div>
    </nav>
  );
};

const styles = {
  nav: {
    background: 'rgba(17,24,39,0.95)',
    borderBottom: '1px solid #2a3a5c',
    backdropFilter: 'blur(10px)',
    position: 'sticky',
    top: 0,
    zIndex: 100,
  },
  container: {
    maxWidth: '1280px',
    margin: '0 auto',
    padding: '0 24px',
    height: '64px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  brand: {
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
    fontSize: '18px',
    fontWeight: '700',
    color: '#f1f5f9',
    textDecoration: 'none',
  },
  brandIcon: { fontSize: '20px' },
  links: { display: 'flex', gap: '4px' },
  link: {
    padding: '6px 14px',
    borderRadius: '8px',
    color: '#94a3b8',
    fontSize: '14px',
    fontWeight: '500',
    textDecoration: 'none',
    transition: 'all 0.2s',
  },
  linkActive: {
    background: 'rgba(59,130,246,0.15)',
    color: '#3b82f6',
  },
  userSection: {
    display: 'flex',
    alignItems: 'center',
    gap: '16px',
  },
  userInfo: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
  },
  avatar: {
    width: '36px',
    height: '36px',
    borderRadius: '50%',
    background: 'linear-gradient(135deg, #3b82f6, #8b5cf6)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: '700',
    fontSize: '14px',
    color: 'white',
  },
  userName: { fontSize: '13px', fontWeight: '600', color: '#f1f5f9' },
  userRole: { fontSize: '11px', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.5px' },
  logoutBtn: {
    background: 'rgba(239,68,68,0.1)',
    border: '1px solid rgba(239,68,68,0.2)',
    color: '#ef4444',
    padding: '6px 14px',
    borderRadius: '8px',
    cursor: 'pointer',
    fontSize: '13px',
    fontWeight: '500',
    fontFamily: 'inherit',
    transition: 'all 0.2s',
  },
};

export default Navbar;
