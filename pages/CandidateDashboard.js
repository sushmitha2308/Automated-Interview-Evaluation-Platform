import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { codingAPI, videoAPI } from '../services/api';

const CandidateDashboard = () => {
  const { user } = useAuth();
  const [submissions, setSubmissions] = useState([]);
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([codingAPI.getMySubmissions(), videoAPI.getMyVideos()])
      .then(([subRes, vidRes]) => {
        setSubmissions(subRes.data);
        setVideos(vidRes.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const avgScore = submissions.length
    ? Math.round(submissions.reduce((a, s) => a + s.score, 0) / submissions.length)
    : 0;
  const passedCount = submissions.filter(s => s.status === 'PASSED').length;

  const stats = [
    { label: 'Coding Submissions', value: submissions.length, icon: '💻', color: '#3b82f6' },
    { label: 'Tests Passed', value: passedCount, icon: '✅', color: '#10b981' },
    { label: 'Avg Coding Score', value: `${avgScore}%`, icon: '📊', color: '#8b5cf6' },
    { label: 'Videos Uploaded', value: videos.length, icon: '🎥', color: '#f59e0b' },
  ];

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  return (
    <div className="page-container">
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ fontSize: '28px', fontWeight: '700', marginBottom: '4px' }}>
          Welcome back, {user?.fullName?.split(' ')[0]} 👋
        </h1>
        <p style={{ color: '#64748b' }}>Track your interview progress and submissions</p>
      </div>

      <div className="grid-4" style={{ marginBottom: '32px' }}>
        {stats.map((stat) => (
          <div key={stat.label} className="card" style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '32px', marginBottom: '8px' }}>{stat.icon}</div>
            <div style={{ fontSize: '28px', fontWeight: '700', color: stat.color }}>{stat.value}</div>
            <div style={{ fontSize: '13px', color: '#64748b', marginTop: '4px' }}>{stat.label}</div>
          </div>
        ))}
      </div>

      <div className="grid-2" style={{ gap: '24px' }}>
        {/* Quick Actions */}
        <div className="card">
          <h2 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '16px' }}>Quick Actions</h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            <Link to="/coding" style={{ textDecoration: 'none' }}>
              <div style={actionStyle('#3b82f6')}>
                <span>💻</span>
                <div>
                  <div style={{ fontWeight: '600' }}>Start Coding Test</div>
                  <div style={{ fontSize: '12px', color: '#94a3b8' }}>View and solve coding challenges</div>
                </div>
                <span style={{ marginLeft: 'auto', color: '#64748b' }}>→</span>
              </div>
            </Link>
            <Link to="/video-interview" style={{ textDecoration: 'none' }}>
              <div style={actionStyle('#10b981')}>
                <span>🎥</span>
                <div>
                  <div style={{ fontWeight: '600' }}>Video Interview</div>
                  <div style={{ fontSize: '12px', color: '#94a3b8' }}>Record your video responses</div>
                </div>
                <span style={{ marginLeft: 'auto', color: '#64748b' }}>→</span>
              </div>
            </Link>
            <Link to="/my-submissions" style={{ textDecoration: 'none' }}>
              <div style={actionStyle('#8b5cf6')}>
                <span>📋</span>
                <div>
                  <div style={{ fontWeight: '600' }}>View My Results</div>
                  <div style={{ fontSize: '12px', color: '#94a3b8' }}>Check submission history and scores</div>
                </div>
                <span style={{ marginLeft: 'auto', color: '#64748b' }}>→</span>
              </div>
            </Link>
          </div>
        </div>

        {/* Recent Submissions */}
        <div className="card">
          <h2 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '16px' }}>Recent Submissions</h2>
          {submissions.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">📝</div>
              <p>No submissions yet.<br />Start with a coding challenge!</p>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {submissions.slice(0, 4).map((sub) => (
                <div key={sub.id} style={subItemStyle}>
                  <div>
                    <div style={{ fontWeight: '500', fontSize: '14px' }}>{sub.questionTitle}</div>
                    <div style={{ fontSize: '12px', color: '#64748b' }}>
                      {sub.testCasesPassed}/{sub.totalTestCases} tests • {sub.language}
                    </div>
                  </div>
                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontWeight: '700', color: sub.score >= 70 ? '#10b981' : sub.score >= 40 ? '#f59e0b' : '#ef4444' }}>
                      {sub.score}%
                    </div>
                    <span className={`badge badge-${sub.status.toLowerCase()}`}>{sub.status}</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

const actionStyle = (color) => ({
  display: 'flex',
  alignItems: 'center',
  gap: '12px',
  padding: '14px',
  background: '#111827',
  borderRadius: '10px',
  border: `1px solid ${color}22`,
  cursor: 'pointer',
  fontSize: '18px',
  transition: 'all 0.2s',
  color: '#f1f5f9',
});

const subItemStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: '12px',
  background: '#111827',
  borderRadius: '8px',
  border: '1px solid #2a3a5c',
};

export default CandidateDashboard;
