CREATE TABLE Homeowners (
    owner_id INT PRIMARY KEY AUTO_INCREMENT,
    owner_name VARCHAR(100),
    is_available BOOLEAN DEFAULT false,  -- Whether the owner is available
    contact_info VARCHAR(100)  -- Email or phone
);