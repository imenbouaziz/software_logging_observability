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
import { UserPlus } from 'lucide-react';

const API_BASE = 'http://localhost:8080';

export default function RegisterPage({ onRegisterSuccess, onSwitchToLogin }) {
  const [formData, setFormData] = useState({ email: '', password: '', name: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (!formData.name || !formData.email || !formData.password) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await fetch(`${API_BASE}/users/register`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error('Registration failed');
      }

      onRegisterSuccess();
    } catch (err) {
      setError(err.message || 'Registration failed');
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
            <UserPlus size={32} color="#667eea" />
            <Typography variant="h4" sx={{ fontWeight: 'bold', color: '#667eea' }}>
              Register
            </Typography>
          </Box>

          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              type="text"
              label="Name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              onKeyPress={handleKeyPress}
              fullWidth
              variant="outlined"
              placeholder="Your Name"
            />
            <TextField
              type="email"
              label="Email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              onKeyPress={handleKeyPress}
              fullWidth
              variant="outlined"
              placeholder="your@email.com"
            />
            <TextField
              type="password"
              label="Password"
              name="password"
              value={formData.password}
              onChange={handleChange}
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
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Register'}
            </Button>
          </Box>

          <Box sx={{ mt: 3, textAlign: 'center' }}>
            <Typography variant="body2" sx={{ color: 'gray' }}>
              Already have an account?{' '}
              <Button
                onClick={onSwitchToLogin}
                sx={{
                  color: '#667eea',
                  textTransform: 'none',
                  fontWeight: 'bold',
                  p: 0,
                  '&:hover': { textDecoration: 'underline' },
                }}
              >
                Login
              </Button>
            </Typography>
          </Box>
        </Card>
      </Container>
    </Box>
  );
}