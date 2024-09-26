CREATE TABLE HomeownerParkingSpaces (
    space_id INT PRIMARY KEY AUTO_INCREMENT,
    owner_id INT,
    distance_from_house DECIMAL(5,2),  -- Distance from the homeowner's house
    is_available BOOLEAN DEFAULT true,  -- Whether the space is available
    available_from TIME,  -- Time from which space is available
    available_until TIME,  -- Time until space is available
    parking_fee DECIMAL(10,2),  -- Fee for parking
    FOREIGN KEY (owner_id) REFERENCES Homeowners(owner_id)
);