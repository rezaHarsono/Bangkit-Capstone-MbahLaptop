# Use an official Python runtime as a parent image
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app

# Install dependencies
RUN pip install --no-cache-dir flask tensorflow

# Expose port 8080 for Cloud Run
EXPOSE 8080

# Run the application
CMD ["python", "app.py"]