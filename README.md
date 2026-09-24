# Store API (E-commerce App)

A Spring Boot REST API for an e-commerce store with user authentication, product management, shopping cart, order processing, and Stripe payment integration.

## Features

- **Authentication & Authorization**: JWT-based authentication with access and refresh tokens
- **User Management**: User registration, profile management, and password changes
- **Product Catalog**: Product and category management
- **Shopping Cart**: Add/remove items, view cart contents
- **Order Processing**: Create orders from cart, view order history
- **Admin Panel**: Admin-specific endpoints for store management
- **Payment Integration**: Stripe integration for secure payments
- **API Documentation**: Swagger/OpenAPI UI for API exploration

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Flyway** - Database migrations
- **MySQL** - Database
- **MapStruct** - Object mapping
- **Lombok** - Reduce boilerplate code
- **JWT (jjwt)** - Token-based authentication
- **Stripe** - Payment processing
- **SpringDoc OpenAPI** - API documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Stripe account (for payment features)

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd store-api
   ```

2. **Configure environment variables**
   
   Copy `.env.example` to `.env` and fill in the required values:
   ```bash
   cp .env.example .env
   ```
   
   Required environment variables:
   - `JWT_SECRET` - Secret key for JWT token signing
   - `JWT_ACCESS_TOKEN_EXPIRATION_IN_SEC` - Access token expiration time in seconds
   - `JWT_REFRESH_TOKEN_EXPIRATION_IN_SEC` - Refresh token expiration time in seconds
   - `STRIPE_SECRET_KEY` - Your Stripe secret key
   - `STRIPE_WEBHOOK_SECRET_KEY` - Your Stripe webhook secret
   - `WEBSITE_URL` - Your website URL for Stripe redirects

3. **Configure database**
   
   Update database credentials in `pom.xml` (Flyway plugin configuration) or use the default:
   - URL: `jdbc:mysql://localhost:3306/store_api`
   - Username: `root`
   - Password: `1234`

4. **Run database migrations**
   ```bash
   ./mvnw flyway:migrate
   ```

## Running the Application

**Using Maven wrapper:**
```bash
./mvnw spring-boot:run
```

**Or package and run:**
```bash
./mvnw clean package
java -jar target/store-api-1.0.0.jar
```

The application will start on `http://localhost:8081`

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8081/swagger-ui.html
```

## Project Structure

```
src/main/java/com/storeapi/
├── admin/          # Admin controllers and security
├── auth/           # Authentication services and JWT handling
├── carts/          # Shopping cart functionality
├── common/         # Global exception handling and shared components
├── orders/         # Order processing and management
├── products/       # Product and category management
├── users/          # User profiles and addresses
└── validations/    # Custom validation annotations
```

## API Endpoints

### Authentication (`/api/auth`)
- `POST /api/auth/login` - User login (returns access token, sets refresh token cookie)
- `POST /api/auth/validate` - Validate access token
- `GET /api/auth/me` - Get current logged-in user details
- `POST /api/auth/refresh` - Refresh access token using refresh token cookie
- `POST /api/auth/logout` - Logout user (invalidates tokens)

### Products (`/api/products`)
- `GET /api/products` - Get all products (optional filter by `categoryId`)
- `GET /api/products/{productId}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{productId}` - Update existing product
- `DELETE /api/products/{productId}` - Delete product

### Shopping Cart (`/api/carts`)
- `GET /api/carts` - Get all carts
- `GET /api/carts/{cartId}` - Get cart by UUID with items
- `POST /api/carts` - Create new empty cart
- `POST /api/carts/{cartId}/items` - Add item to cart (or increase quantity)
- `PUT /api/carts/{cartId}/items/{productId}` - Update cart item quantity
- `DELETE /api/carts/{cartId}/items/{productId}` - Remove item from cart
- `DELETE /api/carts/{cartId}/items` - Clear all items from cart

### Orders (`/api/orders`)
- `GET /api/orders` - Get all orders
- `GET /api/orders/{orderId}` - Get order by ID with items

### Admin (`/api/admin`)
- `GET /api/admin/greetings` - Admin greeting endpoint (admin-only access)

## Default Profiles

- **dev**: Development environment (default)
- **prod**: Production environment

Switch profiles using:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
