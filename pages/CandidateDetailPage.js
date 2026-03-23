import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { recruiterAPI } from '../services/api';

const CandidateDetailPage = () => {
  const { candidateId } = useParams();
  const navigate = useNavigate();
  const [candidate, setCandidate] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [tab, setTab] = useState('coding');
  const [statusForm, setStatusForm] = useState({ status: '', summaryNotes: '' });
  const [showStatusModal, setShowStatusModal] = useState(false);

  useEffect(() => {
    recruiterAPI.getCandidateDetail(candidateId)
      .then(res => {
        setCandidate(res.data);
        setStatusForm({ status: res.data.status || 'PENDING', summaryNotes: res.data.summaryNotes || '' });
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [candidateId]);

  const handleStatusUpdate = async () => {
    setSaving(true);
    try {
      await recruiterAPI.updateCandidateStatus(candidateId, statusForm);
      setCandidate(prev => ({ ...prev, status: statusForm.status, summaryNotes: statusForm.summaryNotes }));
      setShowStatusModal(false);
    } catch (err) {
      alert('Failed to update status');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;
  if (!candidate) return <div className="page-container"><p>Candidate not found</p></div>;

  const statusOptions = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'SHORTLISTED', 'REJECTED'];

  return (
    <div className="page-container">
      <button onClick={() => navigate('/recruiter/candidates')} style={styles.backBtn}>← Back to Candidates</button>

      {/* Candidate Header */}
      <div className="card" style={{ marginBottom: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '16px' }}>
          <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
            <div style={styles.avatar}>{candidate.fullName?.charAt(0)}</div>
            <div>
              <h1 style={{ fontSize: '22px', fontWeight: '700' }}>{candidate.fullName}</h1>
              <div style={{ color: '#94a3b8', fontSize: '14px' }}>{candidate.email}</div>
              {candidate.phone && <div style={{ color: '#64748b', fontSize: '13px' }}>{candidate.phone}</div>}
            </div>
          </div>

          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: '28px', fontWeight: '800', color: candidate.overallScore >= 70 ? '#10b981' : '#f59e0b' }}>
                {Math.round(candidate.overallScore)}%
              </div>
              <div style={{ fontSize: '11px', color: '#64748b' }}>Overall Score</div>
            </div>
            <span className={`badge badge-${candidate.status?.toLowerCase()}`} style={{ fontSize: '13px', padding: '6px 14px' }}>
              {candidate.status}
            </span>
            <button onClick={() => setShowStatusModal(true)} className="btn btn-primary" style={{ padding: '8px 16px' }}>
              Update Status
            </button>
          </div>
        </div>

        {candidate.summaryNotes && (
          <div style={{ ...styles.notesBox, marginTop: '16px' }}>
            <strong>📋 Notes:</strong> {candidate.summaryNotes}
          </div>
        )}
      </div>

      {/* Stats */}
      <div className="grid-3" style={{ marginBottom: '24px' }}>
        <div className="card" style={{ textAlign: 'center' }}>
          <div style={{ fontSize: '24px', fontWeight: '700', color: '#3b82f6' }}>{candidate.codingSubmissions?.length || 0}</div>
          <div style={{ fontSize: '13px', color: '#64748b' }}>Coding Submissions</div>
        </div>
        <div className="card" style={{ textAlign: 'center' }}>
          <div style={{ fontSize: '24px', fontWeight: '700', color: '#8b5cf6' }}>{candidate.videoResponses?.length || 0}</div>
          <div style={{ fontSize: '13px', color: '#64748b' }}>Video Responses</div>
        </div>
        <div className="card" style={{ textAlign: 'center' }}>
          <div style={{ fontSize: '24px', fontWeight: '700', color: '#10b981' }}>
            {candidate.codingSubmissions?.filter(s => s.status === 'PASSED').length || 0}
          </div>
          <div style={{ fontSize: '13px', color: '#64748b' }}>Tests Passed</div>
        </div>
      </div>

      {/* Tabs */}
      <div style={styles.tabs}>
        <button style={{ ...styles.tab, ...(tab === 'coding' ? styles.tabActive : {}) }} onClick={() => setTab('coding')}>
          💻 Coding ({candidate.codingSubmissions?.length || 0})
        </button>
        <button style={{ ...styles.tab, ...(tab === 'video' ? styles.tabActive : {}) }} onClick={() => setTab('video')}>
          🎥 Videos ({candidate.videoResponses?.length || 0})
        </button>
      </div>

      {tab === 'coding' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {(candidate.codingSubmissions || []).map(sub => (
            <div key={sub.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                <div style={{ fontWeight: '600' }}>{sub.questionTitle}</div>
                <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
                  <span style={{ fontWeight: '700', color: sub.score >= 70 ? '#10b981' : '#f59e0b' }}>{sub.score}%</span>
                  <span className={`badge badge-${sub.status?.toLowerCase()}`}>{sub.status}</span>
                </div>
              </div>
              <div style={{ fontSize: '13px', color: '#64748b' }}>
                {sub.language} • {sub.testCasesPassed}/{sub.totalTestCases} tests passed •
                Submitted {new Date(sub.submittedAt).toLocaleString()}
              </div>
              {sub.feedback && <div style={{ ...styles.notesBox, marginTop: '8px', fontSize: '13px' }}>💬 {sub.feedback}</div>}
            </div>
          ))}
          {!candidate.codingSubmissions?.length && (
            <div className="empty-state card"><div className="empty-state-icon">💻</div><p>No coding submissions</p></div>
          )}
        </div>
      )}

      {tab === 'video' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {(candidate.videoResponses || []).map(video => (
            <div key={video.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div style={{ flex: 1 }}>
                  <div style={{ fontWeight: '600', marginBottom: '6px' }}>{video.questionText}</div>
                  <div style={{ fontSize: '13px', color: '#64748b' }}>
                    {video.originalFileName} • {(video.fileSizeBytes / 1024 / 1024).toFixed(1)} MB
                    {video.durationSeconds && ` • ${video.durationSeconds}s`} •
                    Uploaded {new Date(video.uploadedAt).toLocaleString()}
                  </div>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', alignItems: 'flex-end' }}>
                  <span className={`badge badge-${video.status?.toLowerCase() === 'uploaded' ? 'pending' : video.status?.toLowerCase()}`}>
                    {video.status}
                  </span>
                  {video.rating && (
                    <div style={{ color: '#f59e0b', fontSize: '16px' }}>{'★'.repeat(video.rating)}{'☆'.repeat(5 - video.rating)}</div>
                  )}
                  {video.streamUrl && (
                    <a href={`http://localhost:8080${video.streamUrl}`} target="_blank" rel="noreferrer" className="btn btn-secondary" style={{ padding: '5px 12px', fontSize: '12px' }}>
                      ▶ Play
                    </a>
                  )}
                </div>
              </div>
              {video.recruiterNotes && (
                <div style={{ ...styles.notesBox, marginTop: '8px', fontSize: '13px' }}>📝 {video.recruiterNotes}</div>
              )}
            </div>
          ))}
          {!candidate.videoResponses?.length && (
            <div className="empty-state card"><div className="empty-state-icon">🎥</div><p>No video responses</p></div>
          )}
        </div>
      )}

      {/* Status Modal */}
      {showStatusModal && (
        <div style={styles.modalOverlay}>
          <div style={styles.modal}>
            <h3 style={{ fontWeight: '700', marginBottom: '20px' }}>Update Candidate Status</h3>
            <div className="form-group">
              <label className="form-label">Status</label>
              <select className="form-select" value={statusForm.status} onChange={e => setStatusForm(p => ({ ...p, status: e.target.value }))}>
                {statusOptions.map(s => <option key={s} value={s}>{s}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Summary Notes</label>
              <textarea className="form-textarea" value={statusForm.summaryNotes}
                onChange={e => setStatusForm(p => ({ ...p, summaryNotes: e.target.value }))}
                placeholder="Add internal notes about this candidate..." rows={4} />
            </div>
            <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
              <button onClick={() => setShowStatusModal(false)} className="btn btn-secondary">Cancel</button>
              <button onClick={handleStatusUpdate} disabled={saving} className="btn btn-primary">
                {saving ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

const styles = {
  backBtn: {
    background: 'transparent', border: 'none', color: '#94a3b8', cursor: 'pointer',
    fontFamily: 'inherit', fontSize: '14px', marginBottom: '20px', padding: '0',
  },
  avatar: {
    width: '60px', height: '60px', borderRadius: '50%',
    background: 'linear-gradient(135deg, #3b82f6, #8b5cf6)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    fontSize: '24px', fontWeight: '800', color: 'white',
  },
  notesBox: {
    background: 'rgba(59,130,246,0.08)', border: '1px solid rgba(59,130,246,0.15)',
    borderRadius: '8px', padding: '10px 14px', color: '#94a3b8',
  },
  tabs: { display: 'flex', gap: '8px', marginBottom: '20px' },
  tab: {
    padding: '10px 20px', borderRadius: '8px', border: '1px solid #2a3a5c',
    background: 'transparent', color: '#94a3b8', cursor: 'pointer',
    fontFamily: 'inherit', fontSize: '14px', fontWeight: '500', transition: 'all 0.2s',
  },
  tabActive: { background: 'rgba(59,130,246,0.1)', border: '1px solid rgba(59,130,246,0.3)', color: '#3b82f6' },
  modalOverlay: {
    position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.7)',
    display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000,
  },
  modal: {
    background: '#1a2236', border: '1px solid #2a3a5c',
    borderRadius: '16px', padding: '32px', width: '100%', maxWidth: '480px',
  },
};

export default CandidateDetailPage;
