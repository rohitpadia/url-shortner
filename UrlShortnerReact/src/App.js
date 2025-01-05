import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import ShortUrlResponse from './pages/ShortUrlResponse';
import Analytics from './pages/Analytics';
import './App.css';
import BackgroundAnimate from './animations/BackgroundAnimate';

function App() {
  return (
    <Router>  
      <div className="App">
      <BackgroundAnimate />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/short-url-response" element={<ShortUrlResponse />} />
          <Route path="/analytics" element={<Analytics />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
