import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import './Analytics.css';

function Analytics() {
  const location = useLocation();
  const navigate = useNavigate();
  const { shortUrl } = location.state;
  const [analytics, setAnalytics] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [copySuccess, setCopySuccess] = useState('');

  const baseApiUrl = 'https://baseUrl';

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        setError('');
        setLoading(true);
        const response = await axios.get(`${baseApiUrl}/analytics/${shortUrl}`);
        setAnalytics(response.data);
      } catch (error) {
        setError(error.response?.data?.message || 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    fetchAnalytics();
  }, [shortUrl]);

  const copyToClipboard = () => {
    if (analytics?.longUrl) {
      navigator.clipboard.writeText(analytics.longUrl)
      alert('Long URL copied to clipboard!');
    }
  };

  if (loading) {
    return <p className="noData">Loading...</p>;
  }

  return (
    <div className="analytics">
      <h2>URL Analytics</h2>
      {analytics ? (
        <div>
          <p>Click Count: {analytics.clickCount}</p>
          <p>Last Accessed At: {new Date(analytics.lastAccessedAt).toLocaleString()}</p>
          <p>Created At: {new Date(analytics.createdAt).toLocaleString()}</p>
          <div className="url-container">
            <input
              type="text"
              value={analytics.longUrl}
              readOnly
              className="long-url-input"
            />
            <button onClick={copyToClipboard} className="copy-button">
              Copy
            </button>
          </div>
          {copySuccess && <p className="copy-success">{copySuccess}</p>}
        </div>
      ) : (
        <p className="error-message">{error}</p>
      )}
      <button onClick={() => navigate('/')} className="home-button">Home</button>
    </div>
  );
}

export default Analytics;
