CREATE TABLE PublicParkingSpaces (
    space_id INT PRIMARY KEY AUTO_INCREMENT,
    is_available BOOLEAN DEFAULT true,  -- Whether the space is available
    location VARCHAR(100),  -- Location description or coordinates
    distance_from_current DECIMAL(5,2)  -- Distance from the current location
);