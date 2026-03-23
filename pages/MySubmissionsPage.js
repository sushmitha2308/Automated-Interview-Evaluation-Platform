import React, { useState, useEffect } from 'react';
import { codingAPI, videoAPI } from '../services/api';

const MySubmissionsPage = () => {
  const [submissions, setSubmissions] = useState([]);
  const [videos, setVideos] = useState([]);
  const [tab, setTab] = useState('coding');
  const [loading, setLoading] = useState(true);
  const [expanded, setExpanded] = useState(null);

  useEffect(() => {
    Promise.all([codingAPI.getMySubmissions(), videoAPI.getMyVideos()])
      .then(([sRes, vRes]) => {
        setSubmissions(sRes.data);
        setVideos(vRes.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  const avgScore = submissions.length
    ? Math.round(submissions.reduce((a, s) => a + s.score, 0) / submissions.length)
    : 0;

  return (
    <div className="page-container">
      <h1 className="section-title">My Results</h1>

      <div className="grid-4" style={{ marginBottom: '28px' }}>
        {[
          { label: 'Total Submissions', value: submissions.length, color: '#3b82f6' },
          { label: 'Tests Passed', value: submissions.filter(s => s.status === 'PASSED').length, color: '#10b981' },
          { label: 'Average Score', value: `${avgScore}%`, color: '#8b5cf6' },
          { label: 'Videos Uploaded', value: videos.length, color: '#f59e0b' },
        ].map(stat => (
          <div key={stat.label} className="card" style={{ textAlign: 'center', padding: '20px' }}>
            <div style={{ fontSize: '26px', fontWeight: '800', color: stat.color }}>{stat.value}</div>
            <div style={{ fontSize: '12px', color: '#64748b', marginTop: '4px' }}>{stat.label}</div>
          </div>
        ))}
      </div>

      {/* Tabs */}
      <div style={styles.tabs}>
        <button style={{ ...styles.tab, ...(tab === 'coding' ? styles.tabActive : {}) }}
          onClick={() => setTab('coding')}>
          💻 Coding Submissions ({submissions.length})
        </button>
        <button style={{ ...styles.tab, ...(tab === 'video' ? styles.tabActive : {}) }}
          onClick={() => setTab('video')}>
          🎥 Video Responses ({videos.length})
        </button>
      </div>

      {tab === 'coding' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {submissions.length === 0 ? (
            <div className="empty-state card">
              <div className="empty-state-icon">💻</div>
              <p>No coding submissions yet</p>
            </div>
          ) : submissions.map(sub => (
            <div key={sub.id} className="card" style={{ padding: '0', overflow: 'hidden' }}>
              <div
                style={styles.submissionHeader}
                onClick={() => setExpanded(expanded === sub.id ? null : sub.id)}
              >
                <div>
                  <span style={{ fontWeight: '600', fontSize: '15px' }}>{sub.questionTitle}</span>
                  <span style={{ marginLeft: '12px', color: '#64748b', fontSize: '13px' }}>
                    {sub.language} • {new Date(sub.submittedAt).toLocaleDateString()}
                  </span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <span style={{ fontWeight: '700', fontSize: '16px',
                    color: sub.score >= 70 ? '#10b981' : sub.score >= 40 ? '#f59e0b' : '#ef4444' }}>
                    {sub.score}%
                  </span>
                  <span className={`badge badge-${sub.status?.toLowerCase()}`}>{sub.status}</span>
                  <span style={{ color: '#64748b' }}>{expanded === sub.id ? '▲' : '▼'}</span>
                </div>
              </div>

              {expanded === sub.id && (
                <div style={{ padding: '16px 20px', borderTop: '1px solid #2a3a5c' }}>
                  <div className="grid-3" style={{ marginBottom: '16px' }}>
                    <div style={styles.metaBox}>
                      <div style={styles.metaLabel}>Test Cases</div>
                      <div style={styles.metaValue}>{sub.testCasesPassed}/{sub.totalTestCases}</div>
                    </div>
                    <div style={styles.metaBox}>
                      <div style={styles.metaLabel}>Execution Time</div>
                      <div style={styles.metaValue}>{sub.executionTimeMs ? `${sub.executionTimeMs}ms` : 'N/A'}</div>
                    </div>
                    <div style={styles.metaBox}>
                      <div style={styles.metaLabel}>Score</div>
                      <div style={{ ...styles.metaValue, color: sub.score >= 70 ? '#10b981' : '#f59e0b' }}>{sub.score}/100</div>
                    </div>
                  </div>
                  {sub.feedback && (
                    <div style={styles.feedbackBox}>
                      💬 {sub.feedback}
                    </div>
                  )}
                  {sub.code && (
                    <details style={{ marginTop: '12px' }}>
                      <summary style={{ cursor: 'pointer', color: '#94a3b8', fontSize: '13px', marginBottom: '8px' }}>
                        View Code
                      </summary>
                      <pre style={{
                        background: '#111827', border: '1px solid #2a3a5c', borderRadius: '8px',
                        padding: '14px', fontSize: '12px', fontFamily: 'JetBrains Mono, monospace',
                        color: '#a5f3fc', overflowX: 'auto', maxHeight: '300px',
                      }}>{sub.code}</pre>
                    </details>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {tab === 'video' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {videos.length === 0 ? (
            <div className="empty-state card">
              <div className="empty-state-icon">🎥</div>
              <p>No video responses yet</p>
            </div>
          ) : videos.map(video => (
            <div key={video.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div style={{ flex: 1 }}>
                  <div style={{ fontWeight: '600', marginBottom: '6px' }}>{video.questionText}</div>
                  <div style={{ fontSize: '13px', color: '#64748b', marginBottom: '8px' }}>
                    📁 {video.originalFileName} •
                    📦 {(video.fileSizeBytes / 1024 / 1024).toFixed(1)} MB •
                    📅 {new Date(video.uploadedAt).toLocaleDateString()}
                    {video.durationSeconds && ` • ⏱ ${video.durationSeconds}s`}
                  </div>
                  {video.recruiterNotes && (
                    <div style={styles.feedbackBox}>
                      📝 Recruiter: {video.recruiterNotes}
                    </div>
                  )}
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                  <span className={`badge badge-${video.status?.toLowerCase() === 'uploaded' ? 'pending' : video.status?.toLowerCase()}`}>
                    {video.status}
                  </span>
                  {video.rating && (
                    <div style={{ color: '#f59e0b', fontSize: '14px' }}>
                      {'★'.repeat(video.rating)}{'☆'.repeat(5 - video.rating)}
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

const styles = {
  tabs: { display: 'flex', gap: '8px', marginBottom: '20px' },
  tab: {
    padding: '10px 20px', borderRadius: '8px', border: '1px solid #2a3a5c',
    background: 'transparent', color: '#94a3b8', cursor: 'pointer', fontFamily: 'inherit',
    fontSize: '14px', fontWeight: '500', transition: 'all 0.2s',
  },
  tabActive: { background: 'rgba(59,130,246,0.1)', border: '1px solid rgba(59,130,246,0.3)', color: '#3b82f6' },
  submissionHeader: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    padding: '16px 20px', cursor: 'pointer',
  },
  metaBox: {
    background: '#111827', borderRadius: '8px', padding: '12px',
    border: '1px solid #2a3a5c', textAlign: 'center',
  },
  metaLabel: { fontSize: '11px', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.5px', marginBottom: '4px' },
  metaValue: { fontSize: '16px', fontWeight: '700' },
  feedbackBox: {
    background: 'rgba(59,130,246,0.08)', border: '1px solid rgba(59,130,246,0.15)',
    borderRadius: '8px', padding: '10px 14px', fontSize: '13px', color: '#94a3b8',
  },
};

export default MySubmissionsPage;
