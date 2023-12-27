# Spring-JWT-demo

This is a simple example of a REST API which secures the application and protects the endpoints by JSON Web Tokens.

# API documentation

## Authentication Endpoints

### Register User
Register a new user.

**URL:** `/api/v1/auth/register`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "firstName": "John",
        "lastName": "Doe",
        "email": "john@example.com",
        "password": "password123"
    }
    ```
- **Response:** 
    - Status: `200 OK`
    - Body:
        ```json
        {
            "token": "<JWT_TOKEN>"
        }
        ```

### Authenticate User
Authenticate and obtain a JWT token.

- **URL:** `/api/v1/auth/authenticate`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "email": "john@example.com",
        "password": "password123"
    }
    ```
- **Response:** 
    - Status: `200 OK`
    - Body:
        ```json
        {
            "token": "<JWT_TOKEN>"
        }
        ```

## Protected Endpoints

### Hello Endpoint
Get a greeting message from a secured endpoint.

- **URL:** `/api/v1/demo-controller`
- **Method:** `GET`
- **Headers:**
    - `Authorization: Bearer <JWT_TOKEN>`
- **Response:** 
    - Status: `200 OK`
    - Body:
        ```json
        {
            "message": "Hello from secured endpoint"
        }
        ```
        
**Note:** Replace `<JWT_TOKEN>` in the `Authorization` header with the JWT token obtained after successful authentication.
