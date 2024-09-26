# Smart Parking System

## Overview

The *Smart Parking System* is a Java-based application designed to help users find available parking spots in urban areas. The system checks for available spaces in public parking lots, nearby locations within a 700-meter radius, and private homeowner parking (if the homeowner grants permission). The system ensures dynamic management of parking spaces and helps optimize the use of available space.

### Features

- *Primary Parking Check*: Finds and reserves a parking space in the current location.
- *Nearby Parking*: If no parking is available at the current location, it searches for available parking within a 700-meter radius.
- *Homeowner Parking*: Homeowners can offer parking spaces in front of or near their homes. Availability depends on the owner's permission and specified time frames.
- *Distance-Based Search*: The system prioritizes finding the nearest available space.
- *Owner Permissions*: Homeowners can enable or disable their parking space availability.
- *Email Notification*: Homeowners receive notifications when their parking space is reserved.
- *Potential for Payment Integration*: Support for homeowners to charge for parking.

---

## Technologies Used

- *Java*: Core programming language used for backend logic.
- *JDBC (Java Database Connectivity)*: Used to connect and interact with the relational database.
- *MySQL/PostgreSQL*: A relational database system for managing parking spaces, reservations, and homeowner permissions.
- *Email API (Optional)*: Used to notify homeowners about parking reservations.

---

## Database Schema

### Tables

1. *PublicParkingSpaces*
    - Manages parking spaces available to the public.
    - Columns: space_id, location, is_available, reserved_by.

2. *NearbyParkingLocations*
    - Stores parking spots within a 700-meter radius of the current location.
    - Columns: location_id, location_name, distance_from_current, is_available, reserved_by.

3. *Homeowners*
    - Stores information about homeowners offering parking spaces.
    - Columns: owner_id, owner_name, contact_info, is_available.

4. *HomeownerParkingSpaces*
    - Manages parking spots offered by homeowners.
    - Columns: space_id, owner_id, distance_from_house, is_available, available_from, available_until, parking_fee.

---
