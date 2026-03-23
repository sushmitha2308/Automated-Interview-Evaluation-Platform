import React, { useState, useEffect } from 'react';
import { codingAPI } from '../services/api';

const CodingPage = () => {
  const [questions, setQuestions] = useState([]);
  const [selected, setSelected] = useState(null);
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('JAVA');
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    codingAPI.getQuestions()
      .then(res => setQuestions(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const templates = {
    JAVA: `public class Solution {
    public static void main(String[] args) {
        // Your solution here
        
    }
}`,
    PYTHON: `def solution():
    # Your solution here
    pass

if __name__ == "__main__":
    solution()`,
    JAVASCRIPT: `function solution() {
    // Your solution here
    
}

solution();`,
    CPP: `#include <iostream>
using namespace std;

int main() {
    // Your solution here
    
    return 0;
}`,
    C: `#include <stdio.h>

int main() {
    // Your solution here
    
    return 0;
}`,
  };

  const selectQuestion = (q) => {
    setSelected(q);
    setCode(templates[language]);
    setResult(null);
  };

  const handleLanguageChange = (lang) => {
    setLanguage(lang);
    setCode(templates[lang]);
  };

  const handleSubmit = async () => {
    if (!selected || !code.trim()) return;
    setSubmitting(true);
    setResult(null);
    try {
      const res = await codingAPI.submitSolution({
        questionId: selected.id,
        code,
        language,
      });
      setResult(res.data);
      setQuestions(prev => prev.map(q =>
        q.id === selected.id ? { ...q, submitted: true } : q
      ));
    } catch (err) {
      setResult({ error: err.response?.data?.message || 'Submission failed' });
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="loading-spinner"><div className="spinner" /></div>;

  return (
    <div style={{ display: 'flex', height: 'calc(100vh - 64px)', overflow: 'hidden' }}>
      {/* Question List Sidebar */}
      <div style={styles.sidebar}>
        <div style={styles.sidebarHeader}>
          <h2 style={{ fontSize: '15px', fontWeight: '700' }}>Coding Challenges</h2>
          <span style={{ fontSize: '12px', color: '#64748b' }}>{questions.length} problems</span>
        </div>
        <div style={{ overflowY: 'auto', flex: 1 }}>
          {questions.map((q) => (
            <div
              key={q.id}
              onClick={() => selectQuestion(q)}
              style={{
                ...styles.questionItem,
                ...(selected?.id === q.id ? styles.questionItemActive : {}),
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '6px' }}>
                <span style={{ fontWeight: '600', fontSize: '13px' }}>{q.title}</span>
                {q.submitted && <span style={{ fontSize: '18px' }}>✅</span>}
              </div>
              <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                <span className={`badge badge-${q.difficulty?.toLowerCase()}`}>{q.difficulty}</span>
                <span style={{ fontSize: '11px', color: '#64748b' }}>{q.totalMarks} pts</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Main Area */}
      {selected ? (
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
          {/* Problem Description */}
          <div style={styles.problemPanel}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
              <h2 style={{ fontSize: '18px', fontWeight: '700' }}>{selected.title}</h2>
              <span className={`badge badge-${selected.difficulty?.toLowerCase()}`}>{selected.difficulty}</span>
            </div>
            <p style={{ color: '#94a3b8', fontSize: '14px', lineHeight: '1.7', whiteSpace: 'pre-line' }}>
              {selected.description}
            </p>
            {selected.sampleInput && (
              <div style={{ marginTop: '16px', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
                <div>
                  <div style={styles.ioLabel}>Input:</div>
                  <pre style={styles.codeBlock}>{selected.sampleInput}</pre>
                </div>
                <div>
                  <div style={styles.ioLabel}>Output:</div>
                  <pre style={styles.codeBlock}>{selected.sampleOutput}</pre>
                </div>
              </div>
            )}
            {selected.constraints && (
              <div style={{ marginTop: '12px' }}>
                <div style={styles.ioLabel}>Constraints:</div>
                <pre style={{ ...styles.codeBlock, color: '#f59e0b', fontSize: '12px' }}>{selected.constraints}</pre>
              </div>
            )}
          </div>

          {/* Code Editor */}
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', background: '#111827', borderTop: '1px solid #2a3a5c' }}>
            <div style={styles.editorHeader}>
              <select
                value={language}
                onChange={(e) => handleLanguageChange(e.target.value)}
                style={styles.langSelect}
              >
                {['JAVA', 'PYTHON', 'JAVASCRIPT', 'CPP', 'C'].map(l => (
                  <option key={l} value={l}>{l}</option>
                ))}
              </select>
              <button onClick={handleSubmit} disabled={submitting} className="btn btn-primary" style={{ padding: '8px 20px' }}>
                {submitting ? '⏳ Evaluating...' : '▶ Submit Solution'}
              </button>
            </div>
            <textarea
              value={code}
              onChange={(e) => setCode(e.target.value)}
              style={styles.codeEditor}
              placeholder="Write your solution here..."
              spellCheck={false}
            />
          </div>

          {/* Result Panel */}
          {result && (
            <div style={{
              ...styles.resultPanel,
              borderTopColor: result.error ? '#ef4444' : result.status === 'PASSED' ? '#10b981' : '#f59e0b'
            }}>
              {result.error ? (
                <div style={{ color: '#ef4444' }}>❌ Error: {result.error}</div>
              ) : (
                <div style={{ display: 'flex', gap: '24px', alignItems: 'center', flexWrap: 'wrap' }}>
                  <span className={`badge badge-${result.status?.toLowerCase()}`}>{result.status}</span>
                  <span style={{ color: '#94a3b8', fontSize: '14px' }}>
                    Score: <strong style={{ color: result.score >= 70 ? '#10b981' : '#f59e0b' }}>{result.score}%</strong>
                  </span>
                  <span style={{ color: '#94a3b8', fontSize: '14px' }}>
                    Tests: <strong>{result.testCasesPassed}/{result.totalTestCases}</strong>
                  </span>
                  {result.executionTimeMs && (
                    <span style={{ color: '#94a3b8', fontSize: '14px' }}>
                      Time: <strong>{result.executionTimeMs}ms</strong>
                    </span>
                  )}
                  {result.feedback && (
                    <span style={{ color: '#94a3b8', fontSize: '13px' }}>💬 {result.feedback}</span>
                  )}
                </div>
              )}
            </div>
          )}
        </div>
      ) : (
        <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <div className="empty-state">
            <div className="empty-state-icon">💻</div>
            <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '8px' }}>Select a problem to begin</h3>
            <p>Choose a coding challenge from the left panel</p>
          </div>
        </div>
      )}
    </div>
  );
};

const styles = {
  sidebar: {
    width: '280px',
    background: '#111827',
    borderRight: '1px solid #2a3a5c',
    display: 'flex',
    flexDirection: 'column',
    overflow: 'hidden',
  },
  sidebarHeader: {
    padding: '16px',
    borderBottom: '1px solid #2a3a5c',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  questionItem: {
    padding: '14px 16px',
    cursor: 'pointer',
    borderBottom: '1px solid #1a2236',
    transition: 'background 0.2s',
  },
  questionItemActive: { background: 'rgba(59,130,246,0.1)', borderLeft: '3px solid #3b82f6' },
  problemPanel: {
    padding: '20px',
    background: '#0a0e1a',
    borderBottom: '1px solid #2a3a5c',
    maxHeight: '45%',
    overflowY: 'auto',
  },
  ioLabel: { fontSize: '11px', color: '#64748b', textTransform: 'uppercase', letterSpacing: '1px', marginBottom: '6px', fontWeight: '600' },
  codeBlock: {
    background: '#1a2236',
    border: '1px solid #2a3a5c',
    borderRadius: '8px',
    padding: '10px 12px',
    fontSize: '13px',
    fontFamily: 'JetBrains Mono, monospace',
    color: '#a5f3fc',
    overflowX: 'auto',
  },
  editorHeader: {
    padding: '10px 16px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderBottom: '1px solid #2a3a5c',
  },
  langSelect: {
    background: '#1a2236',
    border: '1px solid #2a3a5c',
    color: '#f1f5f9',
    padding: '6px 12px',
    borderRadius: '8px',
    fontFamily: 'inherit',
    fontSize: '13px',
  },
  codeEditor: {
    flex: 1,
    background: '#111827',
    color: '#e2e8f0',
    border: 'none',
    padding: '16px',
    fontFamily: 'JetBrains Mono, monospace',
    fontSize: '13px',
    lineHeight: '1.6',
    resize: 'none',
    outline: 'none',
    tabSize: 4,
  },
  resultPanel: {
    padding: '14px 20px',
    background: '#0a0e1a',
    borderTop: '2px solid',
    transition: 'border-color 0.3s',
  },
};

export default CodingPage;
