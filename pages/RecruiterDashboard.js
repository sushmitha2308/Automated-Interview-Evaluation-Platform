import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { recruiterAPI } from '../services/api';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

const RecruiterDashboard = () => {
  const [stats, setStats] = useState(null);
  const [candidates, setCandidates] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([recruiterAPI.getDashboardStats(), recruiterAPI.getAllCandidates()])
      .then(([sRes, cRes]) => {
        setStats(sRes.data);
        setCandidates(cRes.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  const statCards = [
    { label: 'Total Candidates', value: stats?.totalCandidates || 0, icon: '👥', color: '#3b82f6' },
    { label: 'Pending Review', value: stats?.pendingReviews || 0, icon: '⏳', color: '#f59e0b' },
    { label: 'Shortlisted', value: stats?.shortlisted || 0, icon: '⭐', color: '#10b981' },
    { label: 'Rejected', value: stats?.rejected || 0, icon: '❌', color: '#ef4444' },
  ];

  const scoreData = candidates.slice(0, 8).map(c => ({
    name: c.fullName.split(' ')[0],
    coding: Math.round(c.codingScore),
    video: Math.round(c.videoScore),
    overall: Math.round(c.overallScore),
  }));

  const statusData = [
    { name: 'Pending', value: candidates.filter(c => c.status === 'PENDING').length, color: '#64748b' },
    { name: 'In Progress', value: candidates.filter(c => c.status === 'IN_PROGRESS').length, color: '#3b82f6' },
    { name: 'Shortlisted', value: candidates.filter(c => c.status === 'SHORTLISTED').length, color: '#10b981' },
    { name: 'Rejected', value: candidates.filter(c => c.status === 'REJECTED').length, color: '#ef4444' },
    { name: 'Completed', value: candidates.filter(c => c.status === 'COMPLETED').length, color: '#8b5cf6' },
  ].filter(d => d.value > 0);

  return (
    <div className="page-container">
      <h1 className="section-title">Recruiter Dashboard</h1>

      <div className="grid-4" style={{ marginBottom: '28px' }}>
        {statCards.map(stat => (
          <div key={stat.label} className="card" style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '28px', marginBottom: '6px' }}>{stat.icon}</div>
            <div style={{ fontSize: '32px', fontWeight: '800', color: stat.color }}>{stat.value}</div>
            <div style={{ fontSize: '12px', color: '#64748b', marginTop: '4px' }}>{stat.label}</div>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '24px', marginBottom: '28px' }}>
        {/* Score Chart */}
        <div className="card">
          <h3 style={{ fontWeight: '600', marginBottom: '16px' }}>Candidate Score Comparison</h3>
          {scoreData.length > 0 ? (
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={scoreData} margin={{ top: 5, right: 5, bottom: 5, left: -20 }}>
                <XAxis dataKey="name" tick={{ fill: '#94a3b8', fontSize: 11 }} />
                <YAxis tick={{ fill: '#94a3b8', fontSize: 11 }} domain={[0, 100]} />
                <Tooltip contentStyle={{ background: '#1a2236', border: '1px solid #2a3a5c', borderRadius: '8px' }} />
                <Bar dataKey="coding" name="Coding" fill="#3b82f6" radius={[4,4,0,0]} />
                <Bar dataKey="video" name="Video" fill="#8b5cf6" radius={[4,4,0,0]} />
                <Bar dataKey="overall" name="Overall" fill="#10b981" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="empty-state" style={{ height: '180px' }}>No candidate data yet</div>
          )}
        </div>

        {/* Status Pie */}
        <div className="card">
          <h3 style={{ fontWeight: '600', marginBottom: '16px' }}>Status Distribution</h3>
          {statusData.length > 0 ? (
            <>
              <ResponsiveContainer width="100%" height={160}>
                <PieChart>
                  <Pie data={statusData} cx="50%" cy="50%" innerRadius={45} outerRadius={70} dataKey="value" paddingAngle={2}>
                    {statusData.map((entry, i) => <Cell key={i} fill={entry.color} />)}
                  </Pie>
                  <Tooltip contentStyle={{ background: '#1a2236', border: '1px solid #2a3a5c', borderRadius: '8px' }} />
                </PieChart>
              </ResponsiveContainer>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', marginTop: '8px' }}>
                {statusData.map(d => (
                  <div key={d.name} style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: '12px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: d.color }} />
                    <span style={{ color: '#94a3b8' }}>{d.name} ({d.value})</span>
                  </div>
                ))}
              </div>
            </>
          ) : (
            <div className="empty-state" style={{ height: '180px' }}>No data</div>
          )}
        </div>
      </div>

      {/* Candidates Table */}
      <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
        <div style={{ padding: '16px 20px', borderBottom: '1px solid #2a3a5c', display: 'flex', justifyContent: 'space-between' }}>
          <h3 style={{ fontWeight: '600' }}>All Candidates</h3>
          <Link to="/recruiter/candidates" style={{ fontSize: '13px', color: '#3b82f6' }}>View All →</Link>
        </div>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ background: '#111827' }}>
                {['Candidate', 'Coding Score', 'Video Score', 'Overall', 'Submissions', 'Status', 'Action'].map(h => (
                  <th key={h} style={{ padding: '12px 16px', textAlign: 'left', fontSize: '12px', color: '#64748b', fontWeight: '600', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {candidates.slice(0, 5).map((c, i) => (
                <tr key={c.candidateId} style={{ borderTop: '1px solid #1a2236', transition: 'background 0.15s' }}>
                  <td style={{ padding: '14px 16px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <div style={{ width: '32px', height: '32px', borderRadius: '50%', background: `hsl(${i * 60}, 60%, 40%)`, display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: '700', fontSize: '13px' }}>
                        {c.fullName.charAt(0)}
                      </div>
                      <div>
                        <div style={{ fontWeight: '500', fontSize: '14px' }}>{c.fullName}</div>
                        <div style={{ fontSize: '11px', color: '#64748b' }}>{c.email}</div>
                      </div>
                    </div>
                  </td>
                  <td style={{ padding: '14px 16px' }}>
                    <span style={{ color: c.codingScore >= 70 ? '#10b981' : c.codingScore >= 40 ? '#f59e0b' : '#ef4444', fontWeight: '600' }}>
                      {c.codingScore}%
                    </span>
                  </td>
                  <td style={{ padding: '14px 16px' }}>
                    <span style={{ color: c.videoScore >= 70 ? '#10b981' : c.videoScore >= 40 ? '#f59e0b' : '#94a3b8', fontWeight: '600' }}>
                      {c.videoScore > 0 ? `${c.videoScore}%` : '—'}
                    </span>
                  </td>
                  <td style={{ padding: '14px 16px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <div style={{ width: '60px', height: '6px', background: '#2a3a5c', borderRadius: '3px' }}>
                        <div style={{ height: '100%', width: `${c.overallScore}%`, background: c.overallScore >= 70 ? '#10b981' : '#3b82f6', borderRadius: '3px' }} />
                      </div>
                      <span style={{ fontSize: '13px', fontWeight: '600' }}>{c.overallScore}%</span>
                    </div>
                  </td>
                  <td style={{ padding: '14px 16px', color: '#94a3b8', fontSize: '13px' }}>
                    {c.codingSubmissions} coding, {c.videoResponses} video
                  </td>
                  <td style={{ padding: '14px 16px' }}>
                    <span className={`badge badge-${c.status?.toLowerCase()}`}>{c.status}</span>
                  </td>
                  <td style={{ padding: '14px 16px' }}>
                    <Link to={`/recruiter/candidates/${c.candidateId}`} className="btn btn-secondary" style={{ padding: '6px 12px', fontSize: '12px' }}>
                      Review
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {candidates.length === 0 && (
            <div className="empty-state" style={{ padding: '40px' }}>No candidates registered yet</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RecruiterDashboard;
