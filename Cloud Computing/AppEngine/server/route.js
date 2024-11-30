const express = require('express');
const path = require('path');
const { checkFileExists, downloadFile, cleanupTempFile } = require('../utils/loadModel');

const router = express.Router();

router.get('/download/:fileName', async (req, res) => {
  const { fileName } = req.params;
  const tempFilePath = path.join(__dirname, `../${fileName}`);

  try {
    const fileExists = await checkFileExists(fileName);
    if (!fileExists) {
      return res.status(404).send('File not found');
    }

    await downloadFile(fileName, tempFilePath);

    res.download(tempFilePath, fileName, async (err) => {
      if (err) {
        console.error('Error sending the file:', err);
        return res.status(500).send('Error downloading the file');
      }

      await cleanupTempFile(tempFilePath);
    });
  } catch (error) {
    console.error('Error handling download request:', error);
    res.status(500).send('Failed to download the file');
  }
});

module.exports = router;