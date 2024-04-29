FROM python:3.8-slim

# Set the working directory
WORKDIR /app

COPY . .

RUN pip install -r requirements.txt --no-cache-dir

CMD ["python", "main.py"]