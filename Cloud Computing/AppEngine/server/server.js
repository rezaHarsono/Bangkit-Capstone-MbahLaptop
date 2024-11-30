const express = require('express');
const downloadRoute = require('./route');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(express.json());

// Routes
app.use('/api', downloadRoute);

// Health check
app.get('/', (req, res) => {
  res.send('ML Model API is running!');
});

// Start server
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});