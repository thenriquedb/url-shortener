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
  "url": "https://example.com"
}
```
- **Response**:
```json
{
  "shortUrl": "http://localhost:8080/abc123"
}
```

### Redirect Shortened URL
- **URL**: `/{id}`
- **Method**: `GET`
- **Response**: Redirects to the original URL.