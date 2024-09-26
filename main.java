import java.sql.*;

public class SmartParkingSystem {

    // Check if parking is available at the current location
    public boolean checkCurrentLocationParking(String currentLocation) throws SQLException {
        String query = "SELECT space_id FROM PublicParkingSpaces WHERE is_available = true AND location = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, currentLocation);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int spaceId = rs.getInt("space_id");
                System.out.println("Parking available at current location. Space ID: " + spaceId);
                reservePublicParking(spaceId);  // Reserve the space
                return true;
            } else {
                System.out.println("No parking available at the current location.");
                return false;
            }
        }
    }

    // Reserve public parking space
    public void reservePublicParking(int spaceId) throws SQLException {
        String query = "UPDATE PublicParkingSpaces SET is_available = false WHERE space_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, spaceId);
            ps.executeUpdate();
            System.out.println("Public parking space reserved. Space ID: " + spaceId);
        }
    }

    // Find nearby parking locations
    public void findNearbyParking(String currentLocation) throws SQLException {
        String query = "SELECT location_id, location_name, distance_from_current " +
                       "FROM NearbyParkingLocations WHERE is_available = true AND distance_from_current <= 700";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                System.out.println("Nearby parking locations with available slots:");
                do {
                    int locationId = rs.getInt("location_id");
                    String locationName = rs.getString("location_name");
                    double distance = rs.getDouble("distance_from_current");
                    System.out.println("Location: " + locationName + ", Distance: " + distance + " meters");

                    // Reserve parking if needed (Optional: Logic to reserve can be added)
                } while (rs.next());
            } else {
                System.out.println("No nearby parking available within 700 meters.");
            }
        }
    }

    // Find homeowner parking if no public parking is available
    public void findHomeownerParking(String carNumber) throws SQLException {
        String query = "SELECT h.owner_name, h.contact_info, p.space_id, p.distance_from_house, p.available_from, p.available_until " +
                       "FROM HomeownerParkingSpaces p " +
                       "JOIN Homeowners h ON p.owner_id = h.owner_id " +
                       "WHERE p.is_available = true AND h.is_available = true AND p.distance_from_house <= 700";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String ownerName = rs.getString("owner_name");
                String contactInfo = rs.getString("contact_info");
                int spaceId = rs.getInt("space_id");
                Time availableFrom = rs.getTime("available_from");
                Time availableUntil = rs.getTime("available_until");

                // Check if current time is within availability
                if (isWithinAvailableTime(availableFrom, availableUntil)) {
                    System.out.println("Parking available at " + ownerName + "'s house, " +
                                       rs.getDouble("distance_from_house") + " meters away. Contact: " + contactInfo);
                    notifyHomeowner(contactInfo, ownerName, carNumber);
                    updateHomeownerParkingAvailability(spaceId);
                } else {
                    System.out.println("Parking space is outside the available time range.");
                }
            } else {
                System.out.println("No homeowner parking available within 700 meters.");
            }
        }
    }

    // Check if current time is within the available time range
    public boolean isWithinAvailableTime(Time availableFrom, Time availableUntil) {
        Time currentTime = new Time(System.currentTimeMillis());
        return currentTime.after(availableFrom) && currentTime.before(availableUntil);
    }

    // Reserve homeowner parking space and mark it unavailable
    public void updateHomeownerParkingAvailability(int spaceId) throws SQLException {
        String query = "UPDATE HomeownerParkingSpaces SET is_available = false WHERE space_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, spaceId);
            ps.executeUpdate();
            System.out.println("Homeowner parking space reserved.");
        }
    }

    // Notify homeowner when their space is reserved
    public void notifyHomeowner(String email, String ownerName, String carNumber) {
        String subject = "Parking Space Reservation Notification";
        String message = "Dear " + ownerName + ",\n\n" +
                         "Your parking space has been reserved by the car with number: " + carNumber + ".\n" +
                         "Please contact the user if necessary.";

        EmailNotification.sendEmail(email, subject, message);
    }

    public static void main(String[] args) {
        SmartParkingSystem parkingSystem = new SmartParkingSystem();

        String currentLocation = "Main Street";  // Example location
        String carNumber = "AB123CD";  // Example car number

        try {
            // 1. Check parking at the current location
            boolean isParkingAvailable = parkingSystem.checkCurrentLocationParking(currentLocation);

            // 2. If no parking at current location, check for nearby parking locations
            if (!isParkingAvailable) {
                System.out.println("Searching for nearby parking locations...");
                parkingSystem.findNearbyParking(currentLocation);
                
                // 3. If still no parking, check for homeowner parking
                parkingSystem.findHomeownerParking(carNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
