# URL Shortener (Spring Boot + MongoDB)

A simple proof-of-concept (POC) URL shortener built with Spring Boot and MongoDB for learning purposes.

## Stack
- Java 21;
- Spring Boot;
- MongoDB;
- Docker compose;

## Endpoints

### Shorten URL
- **URL**: `/shorten`
- **Method**: `POST`
- **Request Body**:
```json
{
  "url": "https://example.com",
  "expiresAt": "2025-05-21T11:28:00" // Optional
}
```

- **Response**:
```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "expiresAt": "2025-05-21T11:28:00" // Or null if not set
}
```

### Redirect Shortened URL
- **URL**: `/{id}`
- **Method**: `GET`
- **Response**: Redirects to the original URL.

## Running with Docker Compose

```bash
docker compose --profile production up --build -d
```

## Runing with Docker Swarm

**1. Create Docker secrets**

```bash
echo 8081 | docker secret create APP_PORT -
echo mongo | docker secret create MONGO_HOST -
echo 27017 | docker secret create MONGO_PORT -
echo root | docker secret create MONGO_USER -
echo password | docker secret create MONGO_PASSWORD -
echo url_shortener | docker secret create MONG0_DB -
echo cache | docker secret create REDIS_HOST -
echo 6379 | docker secret create REDIS_PORT -
```

**2. Run with Docker Swarm**

```bash
docker swarm init
```
**3. Deploy the stack**

```bash
docker stack deploy -c docker-swarm.yml url_shortener
```
