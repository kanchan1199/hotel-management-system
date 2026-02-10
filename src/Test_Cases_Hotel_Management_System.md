# Test Case Document: Hotel Management System - Room Booking & Reservation Module

**Project:** Hotel Operations Digitalization  
**Feature ID:** REQ-HMS-2026  
**Document Version:** 1.0  
**Date:** Generated  
**Prepared By:** Test Case Generator Agent

---

## Table of Contents
1. [Introduction](#introduction)
2. [Test Strategy](#test-strategy)
3. [Test Cases](#test-cases)
   - [F-01: User Access](#f-01-user-access)
   - [F-02: Room Availability Management](#f-02-room-availability-management)
   - [F-03: Room Reservation](#f-03-room-reservation)
   - [F-04: Availability Validation](#f-04-availability-validation)
   - [F-05: Check-In & Check-Out](#f-05-check-in--check-out)
   - [F-06: Billing & Payment](#f-06-billing--payment)
   - [F-07: Reservation Cancellation](#f-07-reservation-cancellation)
4. [Business Rules Test Cases](#business-rules-test-cases)
5. [Traceability Matrix](#traceability-matrix)

---

## 1. Introduction

This document contains comprehensive test cases for the Hotel Management System's Room Booking & Reservation Module. The test cases are designed to verify all functional requirements (F-01 through F-07) and business rules specified in the BRD.

### Scope
- User authentication and authorization
- Room availability tracking
- Reservation creation and management
- Check-in/Check-out operations
- Billing and payment processing
- Reservation cancellation

---

## 2. Test Strategy

### Test Categories
- **Smoke Tests:** Critical path scenarios for basic functionality
- **Sanity Tests:** Verification of specific features after changes
- **Regression Tests:** Full suite to ensure no existing functionality breaks
- **Negative Tests:** Error handling and validation scenarios

### Priority Levels
- **P0 (Critical):** Core booking and reservation functionality
- **P1 (High):** Payment, check-in/check-out operations
- **P2 (Medium):** Reporting, edge cases
- **P3 (Low):** UI/UX enhancements

---

## 3. Test Cases

### F-01: User Access

#### TC-F01-001: Admin User Login - Valid Credentials
**Requirement ID:** F-01  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Hotel Management System is accessible
- Valid Admin user credentials exist in the system

**Test Steps:**
1. Navigate to the login page
2. Enter valid Admin username
3. Enter valid Admin password
4. Click "Login" button

**Expected Results:**
- User is successfully authenticated
- Admin dashboard is displayed
- All admin-level booking operations are accessible

**Post-conditions:**
- User session is created
- Activity is logged in audit trail

---

#### TC-F01-002: Front Desk Staff Login - Valid Credentials
**Requirement ID:** F-01  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Hotel Management System is accessible
- Valid Front Desk Staff credentials exist

**Test Steps:**
1. Navigate to the login page
2. Enter valid Front Desk Staff username
3. Enter valid Front Desk Staff password
4. Click "Login" button

**Expected Results:**
- User is successfully authenticated
- Front Desk dashboard is displayed
- Booking operations appropriate to Front Desk role are accessible
- Admin-only features are not visible/accessible

**Post-conditions:**
- User session is created with Front Desk permissions
- Activity is logged

---

#### TC-F01-003: Login - Invalid Credentials
**Requirement ID:** F-01  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Hotel Management System is accessible

**Test Steps:**
1. Navigate to the login page
2. Enter invalid username
3. Enter invalid password
4. Click "Login" button

**Expected Results:**
- Login fails
- Error message displayed: "Invalid username or password"
- User remains on login page
- No session is created

**Post-conditions:**
- Failed login attempt is logged

---

#### TC-F01-004: Role-Based Access Control - Admin Operations
**Requirement ID:** F-01  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- User is logged in as Admin

**Test Steps:**
1. Navigate to room management section
2. Attempt to create a reservation
3. Attempt to modify room status
4. Attempt to cancel a reservation
5. Attempt to access system configuration

**Expected Results:**
- All operations are permitted
- Admin can perform all booking-related operations
- Admin can access system configuration

**Post-conditions:**
- All actions are logged with Admin user ID

---

#### TC-F01-005: Role-Based Access Control - Front Desk Restrictions
**Requirement ID:** F-01  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in as Front Desk Staff

**Test Steps:**
1. Attempt to access system configuration
2. Attempt to modify user roles
3. Attempt to access admin-only reports

**Expected Results:**
- Access is denied for admin-only features
- Error message: "You do not have permission to access this feature"
- Front Desk user can still perform standard booking operations

**Post-conditions:**
- Unauthorized access attempts are logged

---

#### TC-F01-006: Session Timeout
**Requirement ID:** F-01  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Session timeout is configured (e.g., 30 minutes)

**Test Steps:**
1. Log in successfully
2. Leave the system idle for duration exceeding session timeout
3. Attempt to perform a booking operation

**Expected Results:**
- Session expires after configured timeout
- User is redirected to login page
- Message displayed: "Your session has expired. Please log in again."

**Post-conditions:**
- User session is terminated
- No operations can be performed without re-authentication

---

### F-02: Room Availability Management

#### TC-F02-001: Display Real-Time Room Availability - Available Rooms
**Requirement ID:** F-02  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- User is logged in
- Multiple rooms exist with status "Available"
- Check-in date: Tomorrow
- Check-out date: 3 days from tomorrow

**Test Steps:**
1. Navigate to room availability section
2. Select check-in date (tomorrow)
3. Select check-out date (3 days later)
4. Click "Check Availability" button

**Expected Results:**
- System displays all rooms with status "Available" for the selected date range
- Room details include: Room number, Room type, Price per night, Amenities
- Booked or unavailable rooms are not displayed

**Post-conditions:**
- Availability data is current and accurate

---

#### TC-F02-002: Room Availability - No Available Rooms
**Requirement ID:** F-02  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- All rooms are booked for the selected date range

**Test Steps:**
1. Navigate to room availability section
2. Select check-in date where all rooms are booked
3. Select check-out date
4. Click "Check Availability" button

**Expected Results:**
- System displays message: "No rooms available for the selected dates"
- Option to modify search criteria is provided
- No rooms are listed

**Post-conditions:**
- User can adjust dates and search again

---

#### TC-F02-003: Room Availability - Filter by Room Type
**Requirement ID:** F-02  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- User is logged in
- Multiple room types exist (Single, Double, Suite, etc.)
- Some rooms of each type are available

**Test Steps:**
1. Navigate to room availability section
2. Select check-in and check-out dates
3. Filter by room type (e.g., "Suite")
4. Click "Check Availability" button

**Expected Results:**
- System displays only available rooms of the selected type (Suite)
- Other room types are not displayed
- Room count reflects filtered results

**Post-conditions:**
- Filter can be cleared to show all room types

---

#### TC-F02-004: Room Availability - Real-Time Update After Booking
**Requirement ID:** F-02  
**Priority:** P0 (Critical)  
**Test Type:** Positive

**Pre-conditions:**
- User is logged in
- Room 101 is available for selected dates

**Test Steps:**
1. Check availability for specific dates
2. Note that Room 101 is available
3. Create a reservation for Room 101 for those dates
4. Immediately check availability for the same dates again

**Expected Results:**
- After booking, Room 101 no longer appears in available rooms list
- Availability count is updated in real-time
- System prevents double-booking

**Post-conditions:**
- Room status is updated to "Booked"

---

#### TC-F02-005: Room Availability - Invalid Date Range
**Requirement ID:** F-02  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in

**Test Steps:**
1. Navigate to room availability section
2. Select check-in date: Tomorrow
3. Select check-out date: Today (before check-in date)
4. Click "Check Availability" button

**Expected Results:**
- System displays validation error: "Check-out date must be after check-in date"
- Availability search is not performed
- User is prompted to correct the dates

**Post-conditions:**
- User can correct dates and retry

---

#### TC-F02-006: Room Availability - Past Dates
**Requirement ID:** F-02  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in

**Test Steps:**
1. Navigate to room availability section
2. Select check-in date in the past
3. Select check-out date
4. Click "Check Availability" button

**Expected Results:**
- System displays validation error: "Check-in date cannot be in the past"
- Availability search is not performed

**Post-conditions:**
- User must select current or future dates

---

### F-03: Room Reservation

#### TC-F03-001: Create Room Reservation - Valid Data
**Requirement ID:** F-03  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- User is logged in
- Room 101 (Deluxe Suite) is available for selected dates
- Check-in: Tomorrow
- Check-out: 3 days from tomorrow

**Test Steps:**
1. Navigate to room reservation section
2. Select Room Type: Deluxe Suite
3. Select Room Number: 101
4. Enter check-in date: Tomorrow
5. Enter check-out date: 3 days from tomorrow
6. Enter guest details:
   - Name: John Doe
   - Email: john.doe@email.com
   - Phone: +1234567890
   - ID Type: Passport
   - ID Number: AB123456
7. Click "Create Reservation" button

**Expected Results:**
- Reservation is created successfully
- Confirmation message displayed with reservation ID
- Room 101 status changes to "Reserved"
- Reservation details are stored in the system
- Confirmation email sent to guest (if configured)

**Post-conditions:**
- Reservation record exists in database
- Room is no longer available for the selected dates

---

#### TC-F03-002: Create Reservation - Missing Guest Details
**Requirement ID:** F-03  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Room is available

**Test Steps:**
1. Navigate to room reservation section
2. Select room type and dates
3. Leave guest name field empty
4. Fill other required fields
5. Click "Create Reservation" button

**Expected Results:**
- Validation error displayed: "Guest name is required"
- Reservation is not created
- User is prompted to complete all required fields

**Post-conditions:**
- No reservation record is created
- Room remains available

---

#### TC-F03-003: Create Reservation - Invalid Email Format
**Requirement ID:** F-03  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Room is available

**Test Steps:**
1. Navigate to room reservation section
2. Select room type and dates
3. Enter guest name
4. Enter invalid email: "invalidemail"
5. Fill other fields
6. Click "Create Reservation" button

**Expected Results:**
- Validation error: "Please enter a valid email address"
- Reservation is not created

**Post-conditions:**
- User must provide valid email format

---

#### TC-F03-004: Create Reservation - Invalid Phone Number
**Requirement ID:** F-03  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Room is available

**Test Steps:**
1. Navigate to room reservation section
2. Select room type and dates
3. Enter guest details with invalid phone: "abc123"
4. Click "Create Reservation" button

**Expected Results:**
- Validation error: "Please enter a valid phone number"
- Reservation is not created

**Post-conditions:**
- User must provide valid phone number

---

#### TC-F03-005: Create Reservation - Multiple Rooms
**Requirement ID:** F-03  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- User is logged in
- Multiple rooms are available for selected dates

**Test Steps:**
1. Navigate to room reservation section
2. Select multiple rooms (Room 101, Room 102)
3. Enter same check-in and check-out dates
4. Enter guest details
5. Click "Create Reservation" button

**Expected Results:**
- Reservation is created for all selected rooms
- Single reservation ID links multiple rooms
- All selected rooms are marked as "Reserved"
- Total cost reflects all rooms

**Post-conditions:**
- Multiple room reservation record exists
- All rooms are unavailable for selected dates

---

#### TC-F03-006: Create Reservation - Special Requests
**Requirement ID:** F-03  
**Priority:** P2 (Medium)  
**Test Type:** Positive

**Pre-conditions:**
- User is logged in
- Room is available

**Test Steps:**
1. Navigate to room reservation section
2. Select room and dates
3. Enter guest details
4. Add special requests: "Late check-in, Extra pillows"
5. Click "Create Reservation" button

**Expected Results:**
- Reservation is created with special requests noted
- Special requests are visible in reservation details
- Staff can view special requests during check-in

**Post-conditions:**
- Special requests are stored with reservation

---

### F-04: Availability Validation

#### TC-F04-001: Prevent Double Booking - Same Room, Overlapping Dates
**Requirement ID:** F-04  
**Priority:** P0 (Critical)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Room 101 is reserved from Jan 1 to Jan 5

**Test Steps:**
1. Attempt to create new reservation for Room 101
2. Select check-in: Jan 3
3. Select check-out: Jan 7
4. Enter guest details
5. Click "Create Reservation" button

**Expected Results:**
- System prevents reservation
- Error message: "Room 101 is not available for the selected dates"
- Reservation is not created
- Alternative available rooms are suggested

**Post-conditions:**
- Room 101 remains booked for original dates only
- No double-booking occurs

---

#### TC-F04-002: Prevent Booking - Room Under Maintenance
**Requirement ID:** F-04  
**Priority:** P0 (Critical)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in
- Room 102 status is "Under Maintenance"

**Test Steps:**
1. Attempt to create reservation for Room 102
2. Select valid dates
3. Enter guest details
4. Click "Create Reservation" button

**Expected Results:**
- System prevents reservation
- Error message: "Room 102 is under maintenance and cannot be booked"
- Room 102 does not appear in available rooms list

**Post-conditions:**
- Room remains under maintenance status
- No reservation is created

---

#### TC-F04-003: Availability Validation - Exact Date Match
**Requirement ID:** F-04  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Room 101 is booked from Jan 1 to Jan 5

**Test Steps:**
1. Attempt to book Room 101
2. Select check-in: Jan 1
3. Select check-out: Jan 5
4. Enter guest details
5. Click "Create Reservation" button

**Expected Results:**
- System prevents reservation
- Error message: "Room 101 is already booked for these dates"

**Post-conditions:**
- No double-booking

---

#### TC-F04-004: Availability Validation - Partial Overlap (Start)
**Requirement ID:** F-04  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Room 101 is booked from Jan 5 to Jan 10

**Test Steps:**
1. Attempt to book Room 101
2. Select check-in: Jan 3
3. Select check-out: Jan 7
4. Click "Create Reservation" button

**Expected Results:**
- System prevents reservation
- Error message indicates room is unavailable for selected dates

**Post-conditions:**
- No overlapping booking created

---

#### TC-F04-005: Availability Validation - Partial Overlap (End)
**Requirement ID:** F-04  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Room 101 is booked from Jan 1 to Jan 5

**Test Steps:**
1. Attempt to book Room 101
2. Select check-in: Jan 3
3. Select check-out: Jan 8
4. Click "Create Reservation" button

**Expected Results:**
- System prevents reservation
- Error message indicates room is unavailable

**Post-conditions:**
- No overlapping booking created

---

#### TC-F04-006: Availability Validation - Back-to-Back Bookings Allowed
**Requirement ID:** F-04  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Room 101 is booked from Jan 1 to Jan 5 (check-out on Jan 5)

**Test Steps:**
1. Attempt to book Room 101
2. Select check-in: Jan 5 (same as previous check-out)
3. Select check-out: Jan 8
4. Enter guest details
5. Click "Create Reservation" button

**Expected Results:**
- Reservation is created successfully
- System allows back-to-back bookings (check-out and check-in on same day)
- Both reservations exist without conflict

**Post-conditions:**
- Room 101 has consecutive bookings
- Housekeeping is notified for quick turnaround

---

### F-05: Check-In & Check-Out

#### TC-F05-001: Guest Check-In - Valid Reservation
**Requirement ID:** F-05  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Reservation exists for Guest John Doe
- Reservation ID: RES-001
- Check-in date: Today
- Room 101 is reserved

**Test Steps:**
1. Navigate to check-in section
2. Enter reservation ID: RES-001 or search by guest name
3. Verify guest details and ID
4. Click "Check-In" button

**Expected Results:**
- Guest is checked in successfully
- Room 101 status changes from "Reserved" to "Occupied"
- Check-in timestamp is recorded
- Room key/access is generated
- Confirmation message: "Guest checked in successfully"

**Post-conditions:**
- Guest can access room
- Room status is "Occupied"
- Check-in record is logged

---

#### TC-F05-002: Guest Check-Out - Standard Time
**Requirement ID:** F-05  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Guest is checked in to Room 101
- Current time: 11:00 AM (before 12:00 PM standard check-out)
- No pending charges

**Test Steps:**
1. Navigate to check-out section
2. Enter room number: 101
3. Review final bill
4. Confirm payment is complete
5. Click "Check-Out" button

**Expected Results:**
- Guest is checked out successfully
- Room 101 status changes from "Occupied" to "Available" (after housekeeping)
- Check-out timestamp is recorded
- Final bill is generated
- No late check-out charges applied

**Post-conditions:**
- Room is ready for housekeeping
- Guest access is revoked
- Billing is finalized

---

#### TC-F05-003: Check-In Before Reservation Date - Denied
**Requirement ID:** F-05  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Reservation exists with check-in date: Tomorrow
- Current date: Today

**Test Steps:**
1. Navigate to check-in section
2. Enter reservation ID
3. Attempt to check in guest
4. Click "Check-In" button

**Expected Results:**
- Check-in is denied
- Error message: "Guest cannot check in before the reservation start date"
- Room status remains "Reserved"
- Option to modify reservation dates is provided

**Post-conditions:**
- No check-in is recorded
- Reservation remains active for original dates

---

#### TC-F05-004: Late Check-Out - Additional Charges
**Requirement ID:** F-05  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Guest is checked in to Room 101
- Standard check-out time: 12:00 PM
- Current time: 2:00 PM (2 hours late)
- Late check-out charges: $50 per hour

**Test Steps:**
1. Navigate to check-out section
2. Enter room number: 101
3. System detects late check-out
4. Review bill with late charges
5. Confirm payment
6. Click "Check-Out" button

**Expected Results:**
- Late check-out charge of $100 (2 hours × $50) is added to bill
- Total bill reflects late charges
- Guest is checked out after payment confirmation
- Room status updates to "Available"

**Post-conditions:**
- Late charges are recorded in billing
- Room is released for next reservation

---

#### TC-F05-005: Check-Out with Pending Charges
**Requirement ID:** F-05  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Guest is checked in to Room 101
- Pending charges exist: Room service $50, Minibar $30
- Payment status: Unpaid

**Test Steps:**
1. Navigate to check-out section
2. Enter room number: 101
3. Attempt to check out
4. Click "Check-Out" button

**Expected Results:**
- System displays pending charges
- Warning message: "Guest has unpaid charges totaling $80"
- Check-out is blocked until payment is confirmed
- Option to settle charges is provided

**Post-conditions:**
- Guest cannot check out until charges are paid
- Room remains "Occupied"

---

#### TC-F05-006: Check-In - No Reservation Found
**Requirement ID:** F-05  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- User is logged in

**Test Steps:**
1. Navigate to check-in section
2. Enter non-existent reservation ID: RES-9999
3. Click "Search" button

**Expected Results:**
- Error message: "No reservation found with ID RES-9999"
- Option to create walk-in reservation is provided
- No check-in is processed

**Post-conditions:**
- User can create new reservation for walk-in guest

---

#### TC-F05-007: Room Status Update - After Check-Out
**Requirement ID:** F-05  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Guest has checked out from Room 101
- Room requires housekeeping

**Test Steps:**
1. Check-out guest from Room 101
2. Verify room status immediately after check-out
3. Housekeeping marks room as cleaned
4. Verify room status after cleaning

**Expected Results:**
- Immediately after check-out: Room status = "Needs Cleaning"
- After housekeeping: Room status = "Available"
- Room appears in availability search for future dates

**Post-conditions:**
- Room is ready for next guest

---

### F-06: Billing & Payment

#### TC-F06-001: Calculate Total Stay Cost - Standard Booking
**Requirement ID:** F-06  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Room 101 (Deluxe Suite) rate: $200 per night
- Check-in: Jan 1
- Check-out: Jan 4 (3 nights)
- No additional charges

**Test Steps:**
1. Create reservation for Room 101
2. Select check-in: Jan 1
3. Select check-out: Jan 4
4. Complete reservation
5. View billing details

**Expected Results:**
- System calculates: 3 nights × $200 = $600
- Total cost displayed: $600
- Breakdown shows: Room charges, Number of nights, Rate per night

**Post-conditions:**
- Billing record is created with accurate calculation

---

#### TC-F06-002: Calculate Cost with Additional Charges
**Requirement ID:** F-06  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Room rate: $200 per night
- Stay duration: 2 nights = $400
- Additional charges: Room service $50, Minibar $30, Laundry $20

**Test Steps:**
1. Guest is checked in
2. Add room service charge: $50
3. Add minibar charge: $30
4. Add laundry charge: $20
5. Generate final bill at check-out

**Expected Results:**
- Room charges: $400
- Additional charges: $100 ($50 + $30 + $20)
- Total bill: $500
- Itemized breakdown is displayed

**Post-conditions:**
- All charges are recorded in billing system

---

#### TC-F06-003: Record Payment - Credit Card
**Requirement ID:** F-06  
**Priority:** P0 (Critical)  
**Test Type:** Positive

**Pre-conditions:**
- Guest has final bill of $500
- Payment method: Credit Card

**Test Steps:**
1. Navigate to payment section
2. Select payment method: Credit Card
3. Enter card details (or process payment via POS)
4. Confirm payment amount: $500
5. Click "Process Payment" button

**Expected Results:**
- Payment is processed successfully
- Payment status: "Paid"
- Payment method recorded: "Credit Card"
- Receipt is generated
- Transaction ID is recorded

**Post-conditions:**
- Payment record exists in system
- Guest can check out

---

#### TC-F06-004: Record Payment - Cash
**Requirement ID:** F-06  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Guest has final bill of $500
- Payment method: Cash

**Test Steps:**
1. Navigate to payment section
2. Select payment method: Cash
3. Enter amount received: $500
4. Click "Process Payment" button

**Expected Results:**
- Payment is recorded successfully
- Payment status: "Paid"
- Payment method recorded: "Cash"
- Receipt is generated

**Post-conditions:**
- Cash payment is logged
- Guest can check out

---

#### TC-F06-005: Partial Payment
**Requirement ID:** F-06  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Total bill: $500
- Guest wants to pay in installments

**Test Steps:**
1. Navigate to payment section
2. Enter partial payment: $300
3. Select payment method: Credit Card
4. Process payment
5. Record remaining balance

**Expected Results:**
- Partial payment of $300 is recorded
- Payment status: "Partially Paid"
- Remaining balance: $200
- System tracks outstanding amount

**Post-conditions:**
- Guest cannot check out until full payment
- Partial payment is logged

---

#### TC-F06-006: Payment Failure - Declined Card
**Requirement ID:** F-06  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Guest attempts payment with credit card
- Card is declined by payment gateway

**Test Steps:**
1. Navigate to payment section
2. Select payment method: Credit Card
3. Enter card details
4. Click "Process Payment" button
5. Payment gateway declines transaction

**Expected Results:**
- Payment fails
- Error message: "Payment declined. Please try another payment method."
- Payment status remains: "Unpaid"
- Guest is prompted to use alternative payment method

**Post-conditions:**
- No payment record is created
- Guest must provide alternative payment

---

#### TC-F06-007: Billing Report - Audit Trail
**Requirement ID:** F-06  
**Priority:** P2 (Medium)  
**Test Type:** Positive

**Pre-conditions:**
- Multiple bookings and payments exist
- Reporting period: Last 30 days

**Test Steps:**
1. Navigate to billing reports section
2. Select date range: Last 30 days
3. Generate billing report
4. Review report details

**Expected Results:**
- Report displays all bookings and payments for selected period
- Details include: Reservation ID, Guest name, Room number, Total charges, Payment method, Payment status, Transaction date
- Report can be exported (PDF, Excel)
- All records are retained for audit purposes

**Post-conditions:**
- Audit trail is complete and accessible

---

#### TC-F06-008: Tax Calculation
**Requirement ID:** F-06  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Room rate: $200 per night
- Stay: 2 nights = $400
- Tax rate: 10%

**Test Steps:**
1. Create reservation
2. Generate bill at check-out
3. Review tax calculation

**Expected Results:**
- Subtotal: $400
- Tax (10%): $40
- Total: $440
- Tax is clearly itemized on bill

**Post-conditions:**
- Tax is included in payment processing

---

### F-07: Reservation Cancellation

#### TC-F07-001: Cancel Reservation - Before Check-In
**Requirement ID:** F-07  
**Priority:** P0 (Critical)  
**Test Type:** Smoke, Positive

**Pre-conditions:**
- Reservation exists: RES-001
- Check-in date: 5 days from now
- Cancellation policy: Free cancellation up to 48 hours before check-in
- Current time: 6 days before check-in

**Test Steps:**
1. Navigate to reservation management
2. Search for reservation: RES-001
3. Click "Cancel Reservation" button
4. Confirm cancellation

**Expected Results:**
- Reservation is cancelled successfully
- No cancellation charges applied (within free cancellation period)
- Room status changes from "Reserved" to "Available"
- Cancellation confirmation is sent to guest
- Refund is processed (if payment was made)

**Post-conditions:**
- Reservation status: "Cancelled"
- Room is available for new bookings

---

#### TC-F07-002: Cancel Reservation - With Cancellation Charges
**Requirement ID:** F-07  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Reservation exists: RES-002
- Check-in date: Tomorrow
- Cancellation policy: 50% charge if cancelled within 48 hours
- Total booking amount: $600
- Payment status: Paid

**Test Steps:**
1. Navigate to reservation management
2. Search for reservation: RES-002
3. Click "Cancel Reservation" button
4. System displays cancellation charges
5. Confirm cancellation

**Expected Results:**
- Cancellation charge: $300 (50% of $600)
- Refund amount: $300
- Reservation is cancelled
- Cancellation charges are applied per hotel policy
- Guest is notified of charges and refund amount

**Post-conditions:**
- Reservation status: "Cancelled"
- Partial refund is processed
- Room becomes available

---

#### TC-F07-003: Cancel Reservation - No Refund (Late Cancellation)
**Requirement ID:** F-07  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Reservation exists: RES-003
- Check-in date: Today
- Cancellation policy: No refund if cancelled on check-in day
- Total booking amount: $400
- Payment status: Paid

**Test Steps:**
1. Navigate to reservation management
2. Search for reservation: RES-003
3. Click "Cancel Reservation" button
4. System displays no-refund policy
5. Confirm cancellation

**Expected Results:**
- Cancellation charge: $400 (100%)
- Refund amount: $0
- Reservation is cancelled
- Guest is notified of no-refund policy
- Full payment is retained

**Post-conditions:**
- Reservation status: "Cancelled"
- No refund processed
- Room becomes available

---

#### TC-F07-004: Cancel Reservation - Already Checked In
**Requirement ID:** F-07  
**Priority:** P1 (High)  
**Test Type:** Negative

**Pre-conditions:**
- Reservation exists: RES-004
- Guest is already checked in
- Room status: "Occupied"

**Test Steps:**
1. Navigate to reservation management
2. Search for reservation: RES-004
3. Attempt to click "Cancel Reservation" button

**Expected Results:**
- Cancellation is blocked
- Error message: "Cannot cancel reservation. Guest is already checked in. Please process check-out instead."
- Reservation remains active

**Post-conditions:**
- Reservation status unchanged
- Guest remains checked in

---

#### TC-F07-005: Cancel Reservation - Already Cancelled
**Requirement ID:** F-07  
**Priority:** P2 (Medium)  
**Test Type:** Negative

**Pre-conditions:**
- Reservation RES-005 was previously cancelled
- Reservation status: "Cancelled"

**Test Steps:**
1. Navigate to reservation management
2. Search for reservation: RES-005
3. Attempt to cancel again

**Expected Results:**
- Error message: "This reservation has already been cancelled"
- No action is taken

**Post-conditions:**
- Reservation status remains "Cancelled"

---

#### TC-F07-006: Cancellation Policy Display
**Requirement ID:** F-07  
**Priority:** P2 (Medium)  
**Test Type:** Positive

**Pre-conditions:**
- User is creating a new reservation

**Test Steps:**
1. Navigate to reservation creation
2. Select room and dates
3. Review cancellation policy before confirming

**Expected Results:**
- Cancellation policy is clearly displayed
- Policy includes: Free cancellation period, Cancellation charges, Refund timeline
- Guest must acknowledge policy before completing reservation

**Post-conditions:**
- Guest is informed of cancellation terms

---

#### TC-F07-007: Refund Processing After Cancellation
**Requirement ID:** F-07  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Reservation cancelled with refund amount: $500
- Original payment method: Credit Card

**Test Steps:**
1. Cancel reservation
2. System calculates refund
3. Process refund to original payment method
4. Track refund status

**Expected Results:**
- Refund of $500 is initiated
- Refund is processed to original credit card
- Refund status: "Processing"
- Guest receives refund confirmation
- Refund timeline: 5-7 business days (as per policy)

**Post-conditions:**
- Refund record exists in system
- Guest is notified of refund timeline

---

## 4. Business Rules Test Cases

### BR-001: Guest Cannot Check In Before Reservation Date
**Requirement:** Business Rule #1  
**Priority:** P0 (Critical)  
**Test Type:** Negative

**Pre-conditions:**
- Reservation check-in date: Jan 10
- Current date: Jan 8

**Test Steps:**
1. Attempt to check in guest on Jan 8
2. Enter reservation ID
3. Click "Check-In"

**Expected Results:**
- Check-in is denied
- Error: "Guest cannot check in before the reservation start date (Jan 10)"

**Post-conditions:**
- No check-in recorded

---

### BR-002: Rooms Under Maintenance Cannot Be Booked
**Requirement:** Business Rule #2  
**Priority:** P0 (Critical)  
**Test Type:** Negative

**Pre-conditions:**
- Room 105 status: "Under Maintenance"

**Test Steps:**
1. Search for available rooms
2. Verify Room 105 is not listed
3. Attempt to manually book Room 105 (if system allows)

**Expected Results:**
- Room 105 does not appear in available rooms
- If manual booking attempted: Error message displayed
- Booking is prevented

**Post-conditions:**
- Room 105 remains under maintenance

---

### BR-003: Standard Check-Out Time is 12:00 PM
**Requirement:** Business Rule #3  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Guest is checked in
- Current time: 11:30 AM

**Test Steps:**
1. Process check-out at 11:30 AM
2. Verify no late charges

**Expected Results:**
- Check-out processed successfully
- No late check-out charges
- Standard check-out time (12:00 PM) is enforced

**Post-conditions:**
- Guest checked out on time

---

### BR-004: Late Check-Out Incurs Additional Charges
**Requirement:** Business Rule #3  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Standard check-out: 12:00 PM
- Current time: 2:30 PM
- Late charge: $50/hour

**Test Steps:**
1. Process check-out at 2:30 PM
2. Review bill

**Expected Results:**
- Late check-out charge: $150 (3 hours × $50)
- Charge is added to final bill

**Post-conditions:**
- Late charges recorded

---

### BR-005: Booking and Payment Records Retained for Audit
**Requirement:** Business Rule #4  
**Priority:** P1 (High)  
**Test Type:** Positive

**Pre-conditions:**
- Multiple bookings and payments exist
- Some records are 1+ years old

**Test Steps:**
1. Navigate to audit/reporting section
2. Search for historical records (1+ years old)
3. Verify data integrity

**Expected Results:**
- All booking records are retained
- All payment records are retained
- Data is accessible for reporting and audit
- No data loss or deletion

**Post-conditions:**
- Audit trail is complete

---

## 5. Traceability Matrix

| Test Case ID | Requirement ID | Requirement Description | Priority | Test Type |
|--------------|----------------|-------------------------|----------|----------|
| TC-F01-001 | F-01 | Admin User Login | P0 | Smoke, Positive |
| TC-F01-002 | F-01 | Front Desk Staff Login | P0 | Smoke, Positive |
| TC-F01-003 | F-01 | Invalid Login | P1 | Negative |
| TC-F01-004 | F-01 | Admin RBAC | P1 | Positive |
| TC-F01-005 | F-01 | Front Desk RBAC | P1 | Negative |
| TC-F01-006 | F-01 | Session Timeout | P2 | Negative |
| TC-F02-001 | F-02 | Display Available Rooms | P0 | Smoke, Positive |
| TC-F02-002 | F-02 | No Rooms Available | P1 | Negative |
| TC-F02-003 | F-02 | Filter by Room Type | P1 | Positive |
| TC-F02-004 | F-02 | Real-Time Update | P0 | Positive |
| TC-F02-005 | F-02 | Invalid Date Range | P2 | Negative |
| TC-F02-006 | F-02 | Past Dates | P2 | Negative |
| TC-F03-001 | F-03 | Create Reservation - Valid | P0 | Smoke, Positive |
| TC-F03-002 | F-03 | Missing Guest Details | P1 | Negative |
| TC-F03-003 | F-03 | Invalid Email | P2 | Negative |
| TC-F03-004 | F-03 | Invalid Phone | P2 | Negative |
| TC-F03-005 | F-03 | Multiple Rooms | P1 | Positive |
| TC-F03-006 | F-03 | Special Requests | P2 | Positive |
| TC-F04-001 | F-04 | Prevent Double Booking | P0 | Negative |
| TC-F04-002 | F-04 | Room Under Maintenance | P0 | Negative |
| TC-F04-003 | F-04 | Exact Date Match | P1 | Negative |
| TC-F04-004 | F-04 | Partial Overlap (Start) | P1 | Negative |
| TC-F04-005 | F-04 | Partial Overlap (End) | P1 | Negative |
| TC-F04-006 | F-04 | Back-to-Back Bookings | P1 | Positive |
| TC-F05-001 | F-05 | Check-In Valid | P0 | Smoke, Positive |
| TC-F05-002 | F-05 | Check-Out Standard | P0 | Smoke, Positive |
| TC-F05-003 | F-05 | Early Check-In Denied | P1 | Negative |
| TC-F05-004 | F-05 | Late Check-Out Charges | P1 | Positive |
| TC-F05-005 | F-05 | Pending Charges | P1 | Negative |
| TC-F05-006 | F-05 | No Reservation Found | P2 | Negative |
| TC-F05-007 | F-05 | Room Status Update | P1 | Positive |
| TC-F06-001 | F-06 | Calculate Stay Cost | P0 | Smoke, Positive |
| TC-F06-002 | F-06 | Additional Charges | P1 | Positive |
| TC-F06-003 | F-06 | Payment - Credit Card | P0 | Positive |
| TC-F06-004 | F-06 | Payment - Cash | P1 | Positive |
| TC-F06-005 | F-06 | Partial Payment | P1 | Positive |
| TC-F06-006 | F-06 | Payment Failure | P1 | Negative |
| TC-F06-007 | F-06 | Billing Report | P2 | Positive |
| TC-F06-008 | F-06 | Tax Calculation | P1 | Positive |
| TC-F07-001 | F-07 | Cancel - Free | P0 | Smoke, Positive |
| TC-F07-002 | F-07 | Cancel - With Charges | P1 | Positive |
| TC-F07-003 | F-07 | Cancel - No Refund | P1 | Positive |
| TC-F07-004 | F-07 | Cancel - Checked In | P1 | Negative |
| TC-F07-005 | F-07 | Cancel - Already Cancelled | P2 | Negative |
| TC-F07-006 | F-07 | Cancellation Policy Display | P2 | Positive |
| TC-F07-007 | F-07 | Refund Processing | P1 | Positive |
| BR-001 | Business Rule 1 | No Early Check-In | P0 | Negative |
| BR-002 | Business Rule 2 | Maintenance Rooms | P0 | Negative |
| BR-003 | Business Rule 3 | Standard Check-Out Time | P1 | Positive |
| BR-004 | Business Rule 3 | Late Check-Out Charges | P1 | Positive |
| BR-005 | Business Rule 4 | Audit Trail Retention | P1 | Positive |

---

## Summary

**Total Test Cases:** 52

**By Priority:**
- P0 (Critical): 15
- P1 (High): 29
- P2 (Medium): 8

**By Type:**
- Positive: 30
- Negative: 22
- Smoke: 7

**Coverage:**
- All 7 functional requirements (F-01 to F-07) covered
- All 4 business rules covered
- Edge cases and error scenarios included
- Traceability maintained for all test cases

---

**End of Test Case Document**