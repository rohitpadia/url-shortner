import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ShortUrlResponse.css';

function ShortUrlResponse() {
  const baseUrl = 'https://baseUrl';
  const location = useLocation();
  const navigate = useNavigate();
  const { shortUrl} = location.state;

  const copyToClipboard = () => {
    navigator.clipboard.writeText(fullShortUrl);
    alert('Short URL copied to clipboard!');
  };
  const fullShortUrl = `${baseUrl}${shortUrl}`;
  return (
    <div className="short-url-response">
      <h2>Short URL Created</h2>
      <p>Short URL: <a href={fullShortUrl} target="_blank" rel="noopener noreferrer">{shortUrl}</a></p>
      <button onClick={copyToClipboard}>Copy</button>
      <button onClick={() => navigate('/analytics', { state: { shortUrl } })}>View Analytics</button>
      <button onClick={() => navigate('/')}>Home</button>
    </div>
  );
}

export default ShortUrlResponse;
