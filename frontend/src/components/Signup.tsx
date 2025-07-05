import { useState } from 'react';
import { Box, TextField, Button, Typography, Paper } from '@mui/material';
import axios from 'axios';

interface SignupProps {
  onSignup: () => void;
}

const Signup = ({ onSignup }: SignupProps) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8081/api/auth/signup', { username, password });
      setSuccess('Signup successful! You can now log in.');
      setError('');
      onSignup();
    } catch (err: any) {
      setError(err.response?.data || 'Signup failed');
      setSuccess('');
    }
  };

  return (
    <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
      <Typography variant="h5" gutterBottom>Sign Up</Typography>
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
        {success && <Typography color="primary">{success}</Typography>}
        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>Sign Up</Button>
      </Box>
    </Paper>
  );
};

export default Signup;
