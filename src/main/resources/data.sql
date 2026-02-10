-- Insert Users
INSERT INTO users (id, user_id, username, password, role, full_name, email) VALUES 
(1, 'USR-001', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'Admin User', 'admin@hotel.com'),
(2, 'USR-002', 'frontdesk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FRONT_DESK_STAFF', 'Front Desk Staff', 'frontdesk@hotel.com'),
(3, 'USR-003', 'manager', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MANAGER', 'Hotel Manager', 'manager@hotel.com');

-- Insert Rooms
INSERT INTO rooms (id, room_id, room_number, room_type, status, price_per_night, floor, max_occupancy) VALUES 
(1, 'RM-101', '101', 'SINGLE', 'AVAILABLE', 2000.00, 1, 1),
(2, 'RM-102', '102', 'SINGLE', 'AVAILABLE', 2000.00, 1, 1),
(3, 'RM-103', '103', 'DOUBLE', 'AVAILABLE', 3500.00, 1, 2),
(4, 'RM-104', '104', 'DOUBLE', 'AVAILABLE', 3500.00, 1, 2),
(5, 'RM-201', '201', 'SUITE', 'AVAILABLE', 6000.00, 2, 3),
(6, 'RM-202', '202', 'SUITE', 'AVAILABLE', 6000.00, 2, 3),
(7, 'RM-301', '301', 'DELUXE', 'AVAILABLE', 8000.00, 3, 4),
(8, 'RM-302', '302', 'DELUXE', 'AVAILABLE', 8000.00, 3, 4),
(9, 'RM-303', '303', 'DELUXE', 'MAINTENANCE', 8000.00, 3, 4),
(10, 'RM-304', '304', 'SUITE', 'AVAILABLE', 6500.00, 3, 3);

-- Insert Room Amenities
INSERT INTO room_amenities (room_id, amenity) VALUES 
(1, 'AC'), (1, 'TV'), (1, 'WiFi'),
(2, 'AC'), (2, 'TV'), (2, 'WiFi'),
(3, 'AC'), (3, 'TV'), (3, 'WiFi'), (3, 'Minibar'),
(4, 'AC'), (4, 'TV'), (4, 'WiFi'), (4, 'Minibar'),
(5, 'AC'), (5, 'TV'), (5, 'WiFi'), (5, 'Minibar'), (5, 'Jacuzzi'),
(6, 'AC'), (6, 'TV'), (6, 'WiFi'), (6, 'Minibar'), (6, 'Jacuzzi'),
(7, 'AC'), (7, 'TV'), (7, 'WiFi'), (7, 'Minibar'), (7, 'Jacuzzi'), (7, 'Balcony'),
(8, 'AC'), (8, 'TV'), (8, 'WiFi'), (8, 'Minibar'), (8, 'Jacuzzi'), (8, 'Balcony'),
(9, 'AC'), (9, 'TV'), (9, 'WiFi'), (9, 'Minibar'), (9, 'Jacuzzi'), (9, 'Balcony'),
(10, 'AC'), (10, 'TV'), (10, 'WiFi'), (10, 'Minibar'), (10, 'Jacuzzi');

-- Insert Sample Guests
INSERT INTO guests (id, guest_id, name, contact_number, email, id_proof_type, id_proof_number, address) VALUES 
(1, 'GST-001', 'John Doe', '+1-555-0101', 'john.doe@email.com', 'PASSPORT', 'P1234567', '123 Main St, New York, NY'),
(2, 'GST-002', 'Jane Smith', '+1-555-0102', 'jane.smith@email.com', 'DRIVERS_LICENSE', 'DL987654', '456 Oak Ave, Los Angeles, CA'),
(3, 'GST-003', 'Robert Johnson', '+1-555-0103', 'robert.j@email.com', 'PASSPORT', 'P7654321', '789 Pine Rd, Chicago, IL');

-- Note: Password for all users is 'password123' (BCrypt hashed)