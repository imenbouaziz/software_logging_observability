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
    const savedUser = localStorage.getItem("currentUser");

    if (savedUser) {
      const user = JSON.parse(savedUser);
      logInfo("Restored user from localStorage", user);
      setCurrentUser(user);
      setCurrentPage("dashboard");
    }

    setLoading(false);
  }, []);

  if (loading) {
    logDebug('App loading UI displayed');

    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  //Save user in localStorage on login
  const handleLoginSuccess = (user) => {
    logInfo('User logged in', { id: user.id, email: user.email });

    localStorage.setItem("currentUser", JSON.stringify(user));

    setCurrentUser(user);
    setCurrentPage('dashboard');
  };

  //Clear localStorage on logout
  const handleLogout = () => {
    logInfo('User logout', {
      id: currentUser?.id,
      email: currentUser?.email,
    });

    localStorage.removeItem("currentUser");

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
