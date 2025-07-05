import { CssBaseline, ThemeProvider, createTheme, Button, Box } from '@mui/material'
import { useState, useEffect } from 'react'
import UrlShortener from './components/UrlShortener'
import Login from './components/Login'
import Signup from './components/Signup'
import Navbar from './components/Navbar'

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1976d2',
    },
  },
})

function getUsernameFromToken(token: string | null): string | null {
  if (!token) return null
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.sub || null
  } catch {
    return null
  }
}

function getEmailFromToken(token: string | null): string | null {
  if (!token) return null
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.email || null
  } catch {
    return null
  }
}

function App() {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'))
  const [showSignup, setShowSignup] = useState(false)
  const [username, setUsername] = useState<string | null>(getUsernameFromToken(localStorage.getItem('token')))
  const [email, setEmail] = useState<string | null>(getEmailFromToken(localStorage.getItem('token')))

  useEffect(() => {
    setUsername(getUsernameFromToken(token))
    setEmail(getEmailFromToken(token))
  }, [token])

  const handleLogin = (jwt: string) => {
    setToken(jwt)
    localStorage.setItem('token', jwt)
    setUsername(getUsernameFromToken(jwt))
  }
  const handleLogout = () => {
    setToken(null)
    setUsername(null)
    localStorage.removeItem('token')
  }

  if (!token) {
    return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Box>
          {showSignup ? (
            <Signup onSignup={() => setShowSignup(false)} />
          ) : (
            <Login onLogin={handleLogin} />
          )}
          <Button onClick={() => setShowSignup(!showSignup)} sx={{ mt: 2 }}>
            {showSignup ? 'Already have an account? Login' : "Don't have an account? Sign Up"}
          </Button>
        </Box>
      </ThemeProvider>
    )
  }

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Navbar
        user={username && email ? { username, email } : null}
        onEditProfile={() => alert('Edit Profile clicked!')}
        onLogout={handleLogout}
      />
      <Box>
        <UrlShortener token={token} />
      </Box>
    </ThemeProvider>
  )
}

export default App
