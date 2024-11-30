const path = require('path');
const fs = require('fs').promises;
const { Storage } = require('@google-cloud/storage');

// Load environment variables
require('dotenv').config();

// Initialize Google Cloud Storage
const storage = new Storage({
  keyFilename: path.join(__dirname, `../${process.env.GCS_KEYFILE_PATH}`), // Path to service account key
});
const bucketName = process.env.GCS_BUCKET_NAME; // Bucket name from .env
const subFolder = process.env.GCS_SUBFOLDER; // Subfolder path from .env

/**
 * Check if the file exists in the Google Cloud Storage bucket
 * @param {string} fileName - Name of the file to check
 * @returns {Promise<boolean>} - True if the file exists, false otherwise
 */
async function checkFileExists(fileName) {
  try {
    const filePath = `${subFolder}/${fileName}`;
    const file = storage.bucket(bucketName).file(filePath);
    const [exists] = await file.exists();
    return exists;
  } catch (error) {
    console.error('Error checking file existence in GCS:', error);
    throw new Error('Failed to check file existence');
  }
}

/**
 * Download the file from Google Cloud Storage to a temporary location
 * @param {string} fileName - Name of the file to download
 * @param {string} tempFilePath - Path to save the temporary file
 * @returns {Promise<void>}
 */
async function downloadFile(fileName, tempFilePath) {
  try {
    const filePath = `${subFolder}/${fileName}`;
    const file = storage.bucket(bucketName).file(filePath);
    await file.download({ destination: tempFilePath });
    console.log(`File downloaded from GCS to ${tempFilePath}`);
  } catch (error) {
    console.error('Error downloading file from GCS:', error);
    throw new Error('Failed to download the file');
  }
}

/**
 * Clean up (delete) the temporary file
 * @param {string} tempFilePath - Path of the file to delete
 * @returns {Promise<void>}
 */
async function cleanupTempFile(tempFilePath) {
  try {
    await fs.unlink(tempFilePath);
    console.log(`Temporary file deleted: ${tempFilePath}`);
  } catch (error) {
    console.error('Error deleting temporary file:', error);
  }
}

module.exports = {
  checkFileExists,
  downloadFile,
  cleanupTempFile,
};