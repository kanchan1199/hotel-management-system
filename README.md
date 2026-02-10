# Hotel Management System - Spring Boot Application

## Overview
This is a production-ready Spring Boot application for Hotel Operations Digitalization - Room Booking & Reservation Module.

## Features
- ✅ User Authentication & Authorization (JWT-based)
- ✅ Room Availability Management
- ✅ Reservation Creation & Management
- ✅ Guest Check-In & Check-Out
- ✅ Billing & Payment Processing
- ✅ Reservation Cancellation with Policy-based Charges
- ✅ Real-time Room Status Updates
- ✅ Business Rule Enforcement
- ✅ Comprehensive Error Handling

## Technology Stack
- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Security**: JWT Authentication
- **Spring Data JPA**: Database operations
- **H2 Database**: In-memory database
- **Lombok**: Reduce boilerplate code
- **Maven**: Build tool

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## Build & Run

### 1. Build the project
```bash
mvn clean install
```

### 2. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Access H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:hoteldb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Authentication
- `POST /v1/auth/login` - User login
- `POST /v1/auth/logout` - User logout

### Rooms
- `GET /v1/rooms/availability` - Get room availability
- `GET /v1/rooms/{roomId}` - Get room details

### Reservations
- `POST /v1/reservations` - Create reservation
- `GET /v1/reservations` - Search reservations
- `GET /v1/reservations/{confirmationNumber}` - Get reservation details

### Check-In/Check-Out
- `POST /v1/check-in` - Guest check-in
- `POST /v1/check-out` - Guest check-out

### Billing
- `GET /v1/billing/{confirmationNumber}` - Get bill
- `POST /v1/billing/{confirmationNumber}/calculate` - Calculate bill
- `POST /v1/payments` - Record payment
- `GET /v1/payments/{confirmationNumber}` - Payment history

### Cancellations
- `POST /v1/cancellations` - Cancel reservation
- `GET /v1/cancellations/{cancellationId}` - Get cancellation details

## Default Users

| Username   | Password     | Role              |
|------------|--------------|-------------------|
| admin      | password123  | ADMIN             |
| frontdesk  | password123  | FRONT_DESK_STAFF  |
| manager    | password123  | MANAGER           |

## Sample API Usage

### 1. Login
```bash
curl -X POST http://localhost:8080/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

### 2. Get Room Availability
```bash
curl -X GET "http://localhost:8080/v1/rooms/availability?checkInDate=2024-12-25&checkOutDate=2024-12-28" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Create Reservation
```bash
curl -X POST http://localhost:8080/v1/reservations \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomType": "DELUXE",
    "checkInDate": "2024-12-25",
    "checkOutDate": "2024-12-28",
    "numberOfGuests": 2,
    "guest": {
      "name": "John Doe",
      "email": "john@example.com",
      "contactNumber": "+1234567890",
      "idProofType": "PASSPORT",
      "idProofNumber": "P123456"
    }
  }'
```

## Business Rules Implemented

1. **Early Check-In Prevention**: Guests cannot check in before the reservation start date
2. **Maintenance Room Blocking**: Rooms under maintenance are excluded from availability
3. **Double Booking Prevention**: System prevents overlapping reservations for the same room
4. **Standard Check-Out Time**: 12:00 PM with late checkout charges ($50/hour)
5. **Payment Enforcement**: Payment must be settled before check-out completion
6. **Cancellation Policy**:
   - Free cancellation: >48 hours before check-in
   - 20% charge: 24-48 hours before check-in
   - 50% charge: <24 hours before check-in
7. **Audit Trail**: All records retained with timestamps

## Testing

### Run Unit Tests
```bash
mvn test
```

### Test Coverage
- Reservation Service Tests
- Check-In/Out Service Tests
- Cancellation Service Tests

## Database Schema

The application uses H2 in-memory database with the following entities:
- Users
- Rooms
- Guests
- Reservations
- Charges
- Payments
- Cancellations

## Error Handling

The application provides comprehensive error responses:
- `400 Bad Request` - Validation errors
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Resource not found
- `409 Conflict` - Business rule violations
- `500 Internal Server Error` - Unexpected errors

## Security

- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption using BCrypt
- Session timeout: 30 minutes

## Project Structure

```
src/
├── main/
│   ├── java/com/hotel/management/
│   │   ├── config/          # Security & app configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Exception handling
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── security/        # JWT & security components
│   │   └── service/         # Business logic
│   └── resources/
│       ├── application.properties
│       └── data.sql         # Initial data
└── test/
    └── java/com/hotel/management/  # Unit tests
```

## Future Enhancements

- Integration with external payment gateways
- Email notifications for reservations
- Advanced reporting and analytics
- Multi-property support
- Mobile app integration
- Real-time availability updates via WebSocket

## License

This project is for demonstration purposes.

## Contact

For questions or support, contact: support@hotelmanagementsystem.com