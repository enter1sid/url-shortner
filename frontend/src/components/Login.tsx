import { useState } from 'react';
import { Box, TextField, Button, Typography, Paper } from '@mui/material';
import axios from 'axios';

interface LoginProps {
  onLogin: (token: string) => void;
}

const Login = ({ onLogin }: LoginProps) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8081/api/auth/login', { username, password });
      onLogin(response.data.token);
    } catch (err) {
      setError('Invalid username or password');
    }
  };

  return (
    <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
      <Typography variant="h5" gutterBottom>Login</Typography>
      <Box component="form" onSubmit={handleSubmit}>
        <TextField
          fullWidth
          label="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          margin="normal"
          required
        />
        <TextField
          fullWidth
          label="Password"
          type="password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          margin="normal"
          required
        />
        {error && <Typography color="error">{error}</Typography>}
        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>Login</Button>
      </Box>
    </Paper>
  );
};

export default Login;
