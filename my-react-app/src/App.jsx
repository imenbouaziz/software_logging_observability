import React, { useState, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Box, CircularProgress } from '@mui/material';
import { LogOut, Home } from 'lucide-react';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProductDashboard from './pages/ProductDashboard';
import './App.css';

import './OpenTelemetry';
import { logInfo, logError, logDebug } from './loggers';

const API_BASE = 'http://localhost:8080';

export default function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [currentPage, setCurrentPage] = useState('login');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkSession = async () => {
      logInfo('Checking user session');

      try {
        const response = await fetch(`${API_BASE}/users/login`, {
          credentials: 'include',
          method: 'POST',
        });

        logDebug('Session check status', { status: response.status });

        if (response.ok) {
          const user = await response.json();
          logInfo('Session active', { id: user.id, email: user.email });
          setCurrentUser(user);
          setCurrentPage('dashboard');
        } else {
          logInfo('No active session');
        }
      } catch (err) {
        logError('ERROR checking session', err);
      } finally {
        setLoading(false);
        logDebug('Session check completed');
      }
    };

    checkSession();
  }, []);

  if (loading) {
    logDebug('App loading UI displayed');

    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  const handleLoginSuccess = (user) => {
    logInfo('User logged in', { id: user.id, email: user.email });
    setCurrentUser(user);
    setCurrentPage('dashboard');
  };

  const handleLogout = () => {
    logInfo('User logout', {
      id: currentUser?.id,
      email: currentUser?.email,
    });
    setCurrentUser(null);
    setCurrentPage('login');
  };

  const navigateTo = (page) => {
    logInfo('Navigate page', { page });
    setCurrentPage(page);
  };

  return (
    <Box sx={{ height: "100%", width: "100%", bgcolor: "#f5f5f5", display: "flex", flexDirection: "column" }}>

      {currentUser && currentPage !== 'login' && currentPage !== 'register' && (
        <AppBar position="static" sx={{ background: "linear-gradient(135deg,#667eea,#764ba2)" }}>
          <Toolbar>

            <Button
              onClick={() => navigateTo('dashboard')}
              sx={{ color: "white", gap: 1, fontSize: "1.25rem", fontWeight: "bold" }}
            >
              <Home size={26} /> Product Manager
            </Button>

            <Box sx={{ ml: "auto", display: "flex", gap: 2, alignItems: "center" }}>
              <Typography sx={{ color: "white" }}>
                Welcome, {currentUser.name || currentUser.email}
              </Typography>

              <Button
                onClick={handleLogout}
                sx={{
                  color: "white",
                  gap: 1,
                  textTransform: "none",
                  backgroundColor: "rgba(255,255,255,0.15)",
                  "&:hover": { background: "rgba(255,255,255,0.25)" }
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
