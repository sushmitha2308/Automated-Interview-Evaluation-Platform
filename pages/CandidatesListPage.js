import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { recruiterAPI } from '../services/api';

const CandidatesListPage = () => {
  const [candidates, setCandidates] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [sortBy, setSortBy] = useState('overallScore');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    recruiterAPI.getAllCandidates()
      .then(res => { setCandidates(res.data); setFiltered(res.data); })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    let result = [...candidates];
    if (search) {
      result = result.filter(c =>
        c.fullName.toLowerCase().includes(search.toLowerCase()) ||
        c.email.toLowerCase().includes(search.toLowerCase())
      );
    }
    if (statusFilter !== 'ALL') {
      result = result.filter(c => c.status === statusFilter);
    }
    result.sort((a, b) => b[sortBy] - a[sortBy]);
    setFiltered(result);
  }, [candidates, search, statusFilter, sortBy]);

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  return (
    <div className="page-container">
      <h1 className="section-title">All Candidates</h1>

      {/* Filters */}
      <div style={styles.filters}>
        <input
          className="form-input"
          placeholder="🔍 Search by name or email..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{ maxWidth: '300px' }}
        />
        <select className="form-select" value={statusFilter} onChange={e => setStatusFilter(e.target.value)} style={{ maxWidth: '180px' }}>
          <option value="ALL">All Status</option>
          {['PENDING', 'IN_PROGRESS', 'COMPLETED', 'SHORTLISTED', 'REJECTED'].map(s => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
        <select className="form-select" value={sortBy} onChange={e => setSortBy(e.target.value)} style={{ maxWidth: '200px' }}>
          <option value="overallScore">Sort: Overall Score</option>
          <option value="codingScore">Sort: Coding Score</option>
          <option value="videoScore">Sort: Video Score</option>
          <option value="codingSubmissions">Sort: Submissions</option>
        </select>
        <span style={{ color: '#64748b', fontSize: '13px', marginLeft: 'auto' }}>
          {filtered.length} candidate{filtered.length !== 1 ? 's' : ''}
        </span>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        {filtered.map((c, i) => (
          <div key={c.candidateId} className="card" style={{ padding: '16px 20px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flexWrap: 'wrap' }}>
              {/* Avatar + Info */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', flex: '1', minWidth: '200px' }}>
                <div style={{ ...styles.avatar, background: `hsl(${i * 47 % 360}, 55%, 40%)` }}>
                  {c.fullName?.charAt(0)}
                </div>
                <div>
                  <div style={{ fontWeight: '600' }}>{c.fullName}</div>
                  <div style={{ fontSize: '12px', color: '#64748b' }}>{c.email}</div>
                </div>
              </div>

              {/* Scores */}
              <div style={styles.scoreBlock}>
                <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '2px' }}>CODING</div>
                <div style={{ fontWeight: '700', color: c.codingScore >= 70 ? '#10b981' : '#f59e0b' }}>
                  {c.codingScore > 0 ? `${c.codingScore}%` : '—'}
                </div>
              </div>

              <div style={styles.scoreBlock}>
                <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '2px' }}>VIDEO</div>
                <div style={{ fontWeight: '700', color: c.videoScore >= 70 ? '#10b981' : '#94a3b8' }}>
                  {c.videoScore > 0 ? `${c.videoScore}%` : '—'}
                </div>
              </div>

              <div style={styles.scoreBlock}>
                <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '2px' }}>OVERALL</div>
                <div style={{ fontWeight: '800', fontSize: '16px', color: c.overallScore >= 70 ? '#10b981' : c.overallScore >= 40 ? '#f59e0b' : '#ef4444' }}>
                  {c.overallScore > 0 ? `${c.overallScore}%` : '—'}
                </div>
              </div>

              <div style={styles.scoreBlock}>
                <div style={{ fontSize: '11px', color: '#64748b', marginBottom: '2px' }}>ACTIVITY</div>
                <div style={{ fontSize: '12px', color: '#94a3b8' }}>
                  {c.codingSubmissions}💻 {c.videoResponses}🎥
                </div>
              </div>

              <span className={`badge badge-${c.status?.toLowerCase()}`}>{c.status}</span>

              <Link to={`/recruiter/candidates/${c.candidateId}`} className="btn btn-secondary" style={{ padding: '7px 14px', fontSize: '13px' }}>
                Review →
              </Link>
            </div>
          </div>
        ))}
        {filtered.length === 0 && (
          <div className="empty-state card">
            <div className="empty-state-icon">👥</div>
            <p>No candidates found</p>
          </div>
        )}
      </div>
    </div>
  );
};

const styles = {
  filters: {
    display: 'flex', gap: '12px', marginBottom: '20px', alignItems: 'center', flexWrap: 'wrap',
  },
  avatar: {
    width: '40px', height: '40px', borderRadius: '50%',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    fontWeight: '700', fontSize: '16px', color: 'white', flexShrink: 0,
  },
  scoreBlock: { minWidth: '70px', textAlign: 'center' },
};

export default CandidatesListPage;
