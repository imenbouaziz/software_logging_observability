import React, { useState } from 'react';
import {
  Box,
  Card,
  TextField,
  Button,
  Typography,
  Container,
  Alert,
  CircularProgress,
} from '@mui/material';
import { LogIn } from 'lucide-react';

const API_BASE = 'http://localhost:8080';

export default function LoginPage({ onLoginSuccess, onSwitchToRegister }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    if (!email || !password) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await fetch(
        `${API_BASE}/users/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
        {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        }
      );

      if (!response.ok) {
        throw new Error('Invalid credentials');
      }

      const user = await response.json();
      onLoginSuccess(user);
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') handleSubmit();
  };

  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      }}
    >
      <Container maxWidth="xs">
        <Card
          sx={{
            padding: 4,
            boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.37)',
            borderRadius: 2,
          }}
        >
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 3 }}>
            <LogIn size={32} color="#667eea" />
            <Typography variant="h4" sx={{ fontWeight: 'bold', color: '#667eea' }}>
              Login
            </Typography>
          </Box>

          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              type="email"
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              onKeyPress={handleKeyPress}
              fullWidth
              variant="outlined"
              placeholder="your@email.com"
            />
            <TextField
              type="password"
              label="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyPress={handleKeyPress}
              fullWidth
              variant="outlined"
              placeholder="••••••••"
            />

            <Button
              onClick={handleSubmit}
              disabled={loading}
              fullWidth
              variant="contained"
              sx={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                color: 'white',
                py: 1.5,
                fontWeight: 'bold',
                mt: 2,
              }}
            >
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Login'}
            </Button>
          </Box>

          <Box sx={{ mt: 3, textAlign: 'center' }}>
            <Typography variant="body2" sx={{ color: 'gray' }}>
              Don't have an account?{' '}
              <Button
                onClick={onSwitchToRegister}
                sx={{
                  color: '#667eea',
                  textTransform: 'none',
                  fontWeight: 'bold',
                  p: 0,
                  '&:hover': { textDecoration: 'underline' },
                }}
              >
                Register
              </Button>
            </Typography>
          </Box>
        </Card>
      </Container>
    </Box>
  );
}