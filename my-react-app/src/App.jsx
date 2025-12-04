import React, { useState, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Box, CircularProgress } from '@mui/material';
import { LogOut, Home } from 'lucide-react';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProductDashboard from './pages/ProductDashboard';
import './App.css';

const API_BASE = 'http://localhost:8080';

export default function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [currentPage, setCurrentPage] = useState('login');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch(`${API_BASE}/users/login`, {
          credentials: 'include',
          method: 'POST',
        });
        if (response.ok) {
          const user = await response.json();
          setCurrentUser(user);
          setCurrentPage('dashboard');
        }
      } catch (err) {
        console.log('No active session');
      } finally {
        setLoading(false);
      }
    };
    checkSession();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  const handleLoginSuccess = (user) => {
    console.log('User logged in:', user);
    setCurrentUser(user);
    setCurrentPage('dashboard');
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setCurrentPage('login');
  };

  const navigateTo = (page) => {
    setCurrentPage(page);
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', width: '100%', bgcolor: '#f5f5f5' }}>
      {currentUser && currentPage !== 'login' && currentPage !== 'register' && (
        <AppBar position="static" sx={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
          <Toolbar>
            <Button
              onClick={() => navigateTo('dashboard')}
              sx={{
                display: 'flex',
                alignItems: 'center',
                gap: 1,
                color: 'white',
                textTransform: 'none',
                fontSize: '1.25rem',
                fontWeight: 'bold',
                '&:hover': { opacity: 0.8 },
              }}
            >
              <Home size={28} /> Product Manager
            </Button>
            <Box sx={{ marginLeft: 'auto', display: 'flex', alignItems: 'center', gap: 2 }}>
              <Typography sx={{ color: 'white' }}>
                Welcome, {currentUser.name || currentUser.email}
              </Typography>
              <Button
                onClick={handleLogout}
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 0.5,
                  color: 'white',
                  backgroundColor: 'rgba(255,255,255,0.1)',
                  '&:hover': { backgroundColor: 'rgba(255,255,255,0.2)' },
                  textTransform: 'none',
                }}
              >
                <LogOut size={18} /> Logout
              </Button>
            </Box>
          </Toolbar>
        </AppBar>
      )}

      <Box sx={{ flex: 1 }}>
        {currentPage === 'login' && (
          <LoginPage
            onLoginSuccess={handleLoginSuccess}
            onSwitchToRegister={() => navigateTo('register')}
          />
        )}
        {currentPage === 'register' && (
          <RegisterPage
            onRegisterSuccess={() => navigateTo('login')}
            onSwitchToLogin={() => navigateTo('login')}
          />
        )}
        {currentPage === 'dashboard' && currentUser && (
          <ProductDashboard user={currentUser} />
        )}
      </Box>
    </Box>
  );
}