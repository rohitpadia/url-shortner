import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Home.css';

function Home() {
  const [longUrl, setLongUrl] = useState('');
  const [shortUrlInput, setShortUrlInput] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const baseApiUrl = 'https://baseUrl';

  const createShortUrl = async () => {
    if (!longUrl.trim()) {
      setError('Please enter a valid long URL.');
      return;
    }
    try {
      setError('');
      setLoading(true);
      const response = await axios.post(baseApiUrl, { longUrl });
      navigate('/short-url-response', { state: { shortUrl: response.data.shortUrl, longUrl } });
    } catch (error) {
      setError(error.response?.data?.message || 'An error occurred while creating the short URL.');
    } finally {
      setLoading(false);
    }
  };

  const viewAnalytics = () => {
    if (shortUrlInput.length !== 8) {
      setError('Short URL Code should be exactly 8 characters.');
      return;
    }
    setError('');
    navigate('/analytics', { state: { shortUrl: shortUrlInput } });
  };

  if (loading) {
    return <p className="noData">Loading...</p>;
  }

  return (
    <div className="home">
      <h1>URL Shortener</h1>
      <div className="form-group">
        <input
          type="text"
          placeholder="Enter long URL"
          value={longUrl}
          onChange={(e) => setLongUrl(e.target.value)}
        />
        <button onClick={createShortUrl}>Create Short URL</button>
      </div>
      <div className="form-group">
        <input
          type="text"
          placeholder="Enter short URL Code"
          value={shortUrlInput}
          onChange={(e) => setShortUrlInput(e.target.value)}
        />
        <button onClick={viewAnalytics}>View Analytics</button>
      </div>
      {error && <p className="error-message">{error}</p>}
    </div>
  );
}

export default Home;
