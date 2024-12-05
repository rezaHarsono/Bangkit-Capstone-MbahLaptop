# Use a lightweight Python image as the base
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy only necessary files for installation first to leverage Docker caching
COPY requirements.txt /app/

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the application code into the container
COPY . /app

# Expose port 8080 for Cloud Run
EXPOSE 8080

# Set environment variables for Flask
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV PORT=8080

# Command to run the application
CMD ["python", "app.py"]