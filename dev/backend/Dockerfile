FROM python:3.8-slim

# Set the working directory
WORKDIR /app

RUN pip install flask dotenv --no-cache-dir

COPY . .

CMD ["python", "main.py"]