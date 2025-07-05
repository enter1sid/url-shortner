import { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Paper, Container, List, ListItem, ListItemText, Divider } from '@mui/material';
import axios from 'axios';

const BACKEND_URL = 'http://localhost:8081';

interface UrlShortenerProps {
  token: string;
}

const UrlShortener = ({ token }: UrlShortenerProps) => {
  const [longUrl, setLongUrl] = useState('');
  const [title, setTitle] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [displayTitle, setDisplayTitle] = useState('');
  const [error, setError] = useState('');
  const [allUrls, setAllUrls] = useState<any[]>([]);

  useEffect(() => {
    fetchAllUrls();
    // eslint-disable-next-line
  }, []);

  const fetchAllUrls = async () => {
    try {
      const response = await axios.get(`${BACKEND_URL}/api/url/all`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setAllUrls(response.data);
    } catch (err) {
      // Optionally handle error
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `${BACKEND_URL}/api/url/shorten`,
        { longUrl, title },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setShortUrl(`${BACKEND_URL}/${response.data.shortUrl}`);
      setDisplayTitle(response.data.title);
      setError('');
      fetchAllUrls();
    } catch (err) {
      setError('Failed to create short URL');
      setShortUrl('');
      setDisplayTitle('');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await axios.delete(`${BACKEND_URL}/api/url/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchAllUrls();
    } catch (err) {
      // Optionally handle error
    }
  };

  const copyToClipboard = (url: string) => {
    navigator.clipboard.writeText(url);
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          URL Shortener
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <TextField
            fullWidth
            label="Enter Long URL"
            value={longUrl}
            onChange={(e) => setLongUrl(e.target.value)}
            margin="normal"
            variant="outlined"
            required
            error={!!error}
            helperText={error}
          />
          <TextField
            fullWidth
            label="Enter Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            margin="normal"
            variant="outlined"
            required
          />
          <Button 
            type="submit" 
            variant="contained" 
            fullWidth 
            sx={{ mt: 2 }}
          >
            Shorten URL
          </Button>
        </Box>

        {displayTitle && shortUrl && (
          <Box sx={{ mt: 3 }}>
            <Paper 
              variant="outlined" 
              sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
            >
              <Typography 
                variant="body1" 
                sx={{ wordBreak: 'break-all', mr: 2 }}
              >
                {displayTitle}
              </Typography>
              <Button 
                variant="outlined" 
                size="small"
                onClick={() => copyToClipboard(shortUrl)}
              >
                Copy Link
              </Button>
            </Paper>
          </Box>
        )}

        <Divider sx={{ my: 4 }} />
        <Typography variant="h6" gutterBottom>All Shortened URLs</Typography>
        <List>
          {allUrls.map((url) => (
            <ListItem key={url.id} sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <ListItemText
                primary={url.title}
                secondary={<a href={`${BACKEND_URL}/${url.shortUrl}`} target="_blank" rel="noopener noreferrer">{`${BACKEND_URL}/${url.shortUrl}`}</a>}
              />
              <Box sx={{ display: 'flex', gap: 1 }}>
                <Button size="small" onClick={() => copyToClipboard(`${BACKEND_URL}/${url.shortUrl}`)}>Copy Link</Button>
                <Button size="small" color="error" onClick={() => handleDelete(url.id)}>Delete</Button>
              </Box>
            </ListItem>
          ))}
        </List>
      </Paper>
    </Container>
  );
};

export default UrlShortener;
