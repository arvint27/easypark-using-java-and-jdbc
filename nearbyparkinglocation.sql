CREATE TABLE NearbyParkingLocations (
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    location_name VARCHAR(100),
    distance_from_current DECIMAL(5,2),  -- Distance from the current parking lot
    is_available BOOLEAN DEFAULT true  -- Whether parking is available
);