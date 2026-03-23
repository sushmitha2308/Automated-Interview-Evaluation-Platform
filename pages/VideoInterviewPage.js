import React, { useState, useEffect, useRef } from 'react';
import { videoAPI, interviewAPI } from '../services/api';

const VideoInterviewPage = () => {
  const [questions, setQuestions] = useState([]);
  const [currentIdx, setCurrentIdx] = useState(0);
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [uploadedVideos, setUploadedVideos] = useState([]);
  const [myVideos, setMyVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [phase, setPhase] = useState('browse'); // browse | upload | done
  const fileInputRef = useRef();

  useEffect(() => {
    Promise.all([interviewAPI.getQuestions(), videoAPI.getMyVideos()])
      .then(([qRes, vRes]) => {
        const questionsData = Array.isArray(qRes.data)
          ? qRes.data
          : Array.isArray(qRes.data?.questions)
            ? qRes.data.questions
            : [];
        const myVideosData = Array.isArray(vRes.data)
          ? vRes.data
          : Array.isArray(vRes.data?.videos)
            ? vRes.data.videos
            : [];
        setQuestions(questionsData);
        setMyVideos(myVideosData);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const currentQuestion = questions[currentIdx];

  const isAnswered = (questionId) =>
    myVideos.some(v => v.questionId === questionId) ||
    uploadedVideos.includes(questionId);

  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (file) setSelectedFile(file);
  };

  const handleUpload = async () => {
    if (!selectedFile || !currentQuestion) return;
    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', selectedFile);
      await videoAPI.uploadVideo(currentQuestion.id, formData);
      setUploadedVideos(prev => [...prev, currentQuestion.id]);
      setSelectedFile(null);
      setPhase('done');
    } catch (err) {
      alert(err.response?.data?.message || 'Upload failed. Please try again.');
    } finally {
      setUploading(false);
    }
  };

  const goToNext = () => {
    if (currentIdx < questions.length - 1) {
      setCurrentIdx(currentIdx + 1);
      setSelectedFile(null);
      setPhase('browse');
    }
  };

  const answeredCount = Array.isArray(questions)
    ? questions.filter(q => isAnswered(q.id)).length
    : 0;

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  return (
    <div className="page-container">
      <div style={{ marginBottom: '24px' }}>
        <h1 className="section-title">Video Interview</h1>
        <p style={{ color: '#64748b', marginBottom: '16px' }}>
          Record and upload video responses to interview questions. Each video should be 1-3 minutes.
        </p>

        {/* Progress Bar */}
        <div style={styles.progress}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
            <span style={{ fontSize: '13px', color: '#94a3b8' }}>Progress</span>
            <span style={{ fontSize: '13px', color: '#3b82f6', fontWeight: '600' }}>
              {answeredCount}/{questions.length} answered
            </span>
          </div>
          <div style={styles.progressBar}>
            <div style={{
              ...styles.progressFill,
              width: `${questions.length ? (answeredCount / questions.length) * 100 : 0}%`
            }} />
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '260px 1fr', gap: '24px' }}>
        {/* Question Nav */}
        <div>
          <div className="card" style={{ padding: '12px' }}>
            <h3 style={{ fontSize: '13px', color: '#64748b', fontWeight: '600', marginBottom: '10px', padding: '4px 8px' }}>
              QUESTIONS
            </h3>
            {questions.map((q, idx) => (
              <div
                key={q.id}
                onClick={() => { setCurrentIdx(idx); setSelectedFile(null); setPhase('browse'); }}
                style={{
                  ...styles.qNavItem,
                  ...(idx === currentIdx ? styles.qNavActive : {}),
                }}
              >
                <div style={styles.qNavNum}>{idx + 1}</div>
                <div style={{ flex: 1 }}>
                  <div style={{ fontSize: '12px', fontWeight: '500', lineHeight: '1.3' }}>
                    {q.questionText.substring(0, 50)}{q.questionText.length > 50 ? '...' : ''}
                  </div>
                </div>
                {isAnswered(q.id) && <span style={{ color: '#10b981', fontSize: '14px' }}>✓</span>}
              </div>
            ))}
          </div>
        </div>

        {/* Main Content */}
        <div>
          {currentQuestion ? (
            <div className="card">
              <div style={{ marginBottom: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                  <span style={{ fontSize: '12px', color: '#64748b', textTransform: 'uppercase', letterSpacing: '1px' }}>
                    Question {currentIdx + 1} of {questions.length}
                  </span>
                  <span style={{ fontSize: '12px', color: '#94a3b8' }}>
                    ⏱ Max {Math.floor(currentQuestion.maxDurationSeconds / 60)} minutes
                  </span>
                </div>
                <h2 style={{ fontSize: '20px', fontWeight: '600', lineHeight: '1.4', marginBottom: '12px' }}>
                  {currentQuestion.questionText}
                </h2>
                {currentQuestion.description && (
                  <p style={{ color: '#94a3b8', fontSize: '14px', lineHeight: '1.7' }}>
                    {currentQuestion.description}
                  </p>
                )}
              </div>

              {isAnswered(currentQuestion.id) ? (
                <div style={styles.answeredBox}>
                  <span style={{ fontSize: '24px' }}>✅</span>
                  <div>
                    <div style={{ fontWeight: '600', color: '#10b981' }}>Response Uploaded</div>
                    <div style={{ fontSize: '13px', color: '#64748b' }}>Your video response has been submitted for review</div>
                  </div>
                </div>
              ) : phase === 'done' ? (
                <div style={styles.answeredBox}>
                  <span style={{ fontSize: '24px' }}>🎉</span>
                  <div>
                    <div style={{ fontWeight: '600', color: '#10b981' }}>Successfully Uploaded!</div>
                    <div style={{ fontSize: '13px', color: '#64748b' }}>Your response has been submitted</div>
                  </div>
                </div>
              ) : (
                <div>
                  <div style={styles.uploadArea} onClick={() => fileInputRef.current?.click()}>
                    <div style={{ fontSize: '40px', marginBottom: '12px' }}>🎬</div>
                    <div style={{ fontSize: '16px', fontWeight: '600', marginBottom: '6px', color: '#f1f5f9' }}>
                      {selectedFile ? selectedFile.name : 'Click to select video file'}
                    </div>
                    <div style={{ fontSize: '13px', color: '#64748b' }}>
                      {selectedFile
                        ? `${(selectedFile.size / 1024 / 1024).toFixed(1)} MB`
                        : 'MP4, WebM, AVI, MOV • Max 100MB'}
                    </div>
                    <input
                      ref={fileInputRef}
                      type="file"
                      accept="video/*"
                      onChange={handleFileSelect}
                      style={{ display: 'none' }}
                    />
                  </div>

                  <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
                    <button
                      onClick={handleUpload}
                      disabled={!selectedFile || uploading}
                      className="btn btn-primary"
                      style={{ flex: 1, justifyContent: 'center' }}
                    >
                      {uploading ? '⏳ Uploading...' : '📤 Upload Response'}
                    </button>
                    {selectedFile && (
                      <button onClick={() => setSelectedFile(null)} className="btn btn-secondary">
                        Clear
                      </button>
                    )}
                  </div>
                </div>
              )}

              <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '24px', paddingTop: '20px', borderTop: '1px solid #2a3a5c' }}>
                <button
                  onClick={() => { setCurrentIdx(Math.max(0, currentIdx - 1)); setPhase('browse'); setSelectedFile(null); }}
                  disabled={currentIdx === 0}
                  className="btn btn-secondary"
                >
                  ← Previous
                </button>
                <button
                  onClick={goToNext}
                  disabled={currentIdx === questions.length - 1}
                  className="btn btn-primary"
                >
                  Next Question →
                </button>
              </div>
            </div>
          ) : (
            <div className="empty-state card">
              <div className="empty-state-icon">🎥</div>
              <p>No interview questions available</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

const styles = {
  progress: { background: '#1a2236', border: '1px solid #2a3a5c', borderRadius: '10px', padding: '16px' },
  progressBar: { height: '6px', background: '#2a3a5c', borderRadius: '3px', overflow: 'hidden' },
  progressFill: { height: '100%', background: 'linear-gradient(90deg, #3b82f6, #8b5cf6)', borderRadius: '3px', transition: 'width 0.5s ease' },
  qNavItem: {
    display: 'flex', alignItems: 'center', gap: '10px',
    padding: '10px', borderRadius: '8px', cursor: 'pointer', marginBottom: '4px',
    transition: 'background 0.2s',
  },
  qNavActive: { background: 'rgba(59,130,246,0.12)', border: '1px solid rgba(59,130,246,0.2)' },
  qNavNum: {
    width: '24px', height: '24px', borderRadius: '50%',
    background: '#2a3a5c', display: 'flex', alignItems: 'center', justifyContent: 'center',
    fontSize: '11px', fontWeight: '700', flexShrink: 0,
  },
  answeredBox: {
    display: 'flex', alignItems: 'center', gap: '16px',
    background: 'rgba(16,185,129,0.08)', border: '1px solid rgba(16,185,129,0.2)',
    borderRadius: '10px', padding: '20px',
  },
  uploadArea: {
    border: '2px dashed #2a3a5c', borderRadius: '12px', padding: '40px 20px',
    textAlign: 'center', cursor: 'pointer', transition: 'all 0.2s',
    background: '#111827',
  },
};

export default VideoInterviewPage;
