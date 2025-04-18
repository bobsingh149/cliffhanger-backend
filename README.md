# Barter - Book Budddy & Social Platform

Barter is a modern web application that enables users to trade books and connect with fellow readers. Built with Spring Boot and WebFlux, it provides a reactive and scalable platform for book enthusiasts.

## 🚀 Features

- **Book Trading System**
  - Post books for trading
  - Search books by title, author, or subjects
  - Barter filtering system
  - Image upload support for book covers

- **Social Features**
  - User profiles with bio and preferences
  - Book buddy system based on common interests
  - Real-time chat functionality
  - Comments and likes on book posts
  - Group creation and management

- **Technical Features**
  - Reactive programming with WebFlux
  - Real-time WebSocket communication
  - Cloudinary integration for image management
  - PostgreSQL with R2DBC for reactive database operations
  - Flyway for database migrations

## 🛠️ Technology Stack

- **Backend**
  - Java 17
  - Spring Boot 3.2.5
  - Spring WebFlux
  - R2DBC (Reactive Relational Database Connectivity)
  - PostgreSQL
  - Flyway Migration
  - WebSocket
  - Cloudinary

- **Tools & Platforms**
  - Maven
  - Docker
  - Swagger/OpenAPI
  - Git

## 🏗️ Architecture

The application follows a reactive architecture pattern with:
- Reactive REST APIs
- Event-driven communication
- Non-blocking I/O operations
- Custom database converters for complex data types
- Separation of concerns (Controllers, Services, Repositories)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- PostgreSQL
- Cloudinary account

### Environment Variables

```properties
DB_USER=your_db_user
DB_PASSWORD=your_db_password
APP_PORT=7000
CLOUDINARY_API_KEY=your_cloudinary_key
CLOUDINARY_API_SECRET=your_cloudinary_secret
BOOKS_API_KEY=your_google_books_api_key
```

### Running Locally

1. Clone the repository:
```bash
git clone https://github.com/yourusername/barter-backend.git
cd barter-backend
```

2. Start PostgreSQL using Docker Compose:
```bash
docker-compose up -d
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:7000`

### API Documentation

Swagger UI is available at: `http://localhost:7000/swagger-ui.html`

## 📝 Database Schema

The application uses three main tables:
- `users`: Stores user information and preferences
- `product`: Stores book listings and trading information
- `conversations`: Manages chat and messaging data

Database migrations are handled by Flyway and can be found in the `src/main/resources/db/migration` directory.

## 🔄 Deployment

The application can be deployed using Docker:

```bash
docker build -t barter-backend .
docker run -p 7000:7000 barter-backend
```

## 🚧 Upcoming Features

Based on the TODO list in the application:
- Enhanced route logging with status codes
- Spring Security implementation
- Parallel processing for multiple image uploads
- Additional filtering options for book discovery
- Enhanced comment system

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.