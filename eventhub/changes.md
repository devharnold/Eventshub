# Eventshub Changes

**Date:** 2026-07-16

## Overview

This update focused on stabilizing the backend by fixing entity type inconsistencies, correcting DAO implementations, improving authentication, and resolving build/runtime issues.

---

# Booking Module

## BookingService

- Refactored booking flow.
- Free events now bypass payment confirmation.
- Paid events require payment verification before ticket generation.
- Removed mismatched Integer/String ID usage.

## BookingController

- Returns `TicketDTO` instead of exposing the `Ticket` entity.
- Improved exception handling for failed payments.

---

# Ticket Module

## TicketService

- Standardized ticket creation.
- Retrieves event details before creating a ticket.
- Generates ticket metadata correctly.

## TicketDaoImpl

Reworked ticket persistence.

### Improvements

- Generates ticket numbers.
- Generates QR codes.
- Persists ticket metadata.
- Maps database rows correctly.
- Improved SQL handling.
- Improved logging.

---

# Events Module

## EventsService

Updated to consistently use String event IDs.

Added:

- Event lookup
- Free-event detection

## EventsController

Updated endpoints to match the service layer.

Fixed:

- Path variable types
- Request parameter handling
- Event retrieval

## EventsDaoImpl

Major cleanup.

### Fixed

- SQL parameter binding
- Event ID handling
- Row mapping
- Query execution
- ResultSet processing
- Object construction

---

# User Module

## UsersDaoImpl

Rewritten DAO implementation.

### Improvements

- Proper user insertion
- Password hashing before storage
- Fixed SQL execution
- Proper ResultSet mapping

---

# Authentication

## AuthService

Improved authentication flow.

### Changes

- Passwords are now verified using PBKDF2 hashes.
- Plain-text password comparison removed.
- JWT generation remains unchanged.

---

# Organization Module

## OrganizationDaoImpl

Fixed several SQL issues.

### Corrected

- INSERT statement
- Placeholder count
- Password storage
- executeUpdate() usage
- findAll() implementation
- Result mapping

---

# Models

Updated model classes for consistency.

## Events

- Standardized String IDs.
- Fixed setter implementation.
- Improved field mapping.

## Users

- Consistent getters/setters.
- Timestamp initialization retained.

## Organizations

- Password support retained.
- Cleaned model implementation.

---

# Security

Implemented secure password handling.

- PBKDF2 hashing
- Salt generation
- Constant-time password verification
- Authentication updated to use secure verification

---

# Build & Tooling

Resolved project build issues.

### Fixed

- Maven compilation failures
- Integer/String type mismatches
- DAO implementation errors
- IntelliJ SDK incompatibility
- `java.lang.ExceptionInInitializerError`
- `com.sun.tools.javac.code.TypeTag::UNKNOWN`

---

# Result

Application compiles successfully.

Spring Boot application starts successfully.

Authentication uses hashed passwords.

Booking flow is operational.

DAO implementations are consistent.

Entity IDs are standardized.

Project is now in a runnable state.