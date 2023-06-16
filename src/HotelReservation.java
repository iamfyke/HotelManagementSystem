import java.util.*;
import java.sql.*;
import java.util.regex.*;

// main class
public class HotelReservation {
    private static final Scanner sc = new Scanner(System.in);
    private static String customerName, phoneNumber;
    private static int customerID;
    private static Timestamp createdAt;
    private static Connection connection;
    // url or link for the database
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hotelReservationOfficial";

    // username (user) and password (master password) of the database
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Iamthestormthatisapproaching!";


    // constructor for the connection
    public HotelReservation(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("---Welcome to STI Hotel!---");

        // establishing the connection for the database
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // calling the user registration method
        userRegistration();

    }

    // generating unique ID method
    private static int generateUniqueUserID () throws SQLException{
        try {
            // this is the set of the following numbers in a string type
            String existingIDs = "0123456789";
            // maximum id length is 7
            int idLength = 7;

            // accessing the customer ID
            String sql = "SELECT MAX (customer_user_id) FROM \"hotelReservationOfficial\".\"hotelSchema\".users";

            // establishing the statement connection
            Statement statement = connection.createStatement();

            // // the resultset will be used to execute the following SQL statement to access the following variables
            ResultSet resultSet = statement.executeQuery(sql);

            // 0 is the default value
            int maxUserID = 0;
            // if there's an available slot for the customer id then the statement below will be executed
            if (resultSet.next()) {
                maxUserID = resultSet.getInt(1);
            }

            // increment it to 1 to grant the system to generate a unique ID
            int newUserID = maxUserID + 1;

            statement.close();
            resultSet.close();

            // creating a StringBuilder object
            StringBuilder sb = new StringBuilder();
            // random object
            Random random = new Random();

            // generating a unique ID until it reaches the limit
            for (int i = 0; i < idLength; i++) {
                int index = random.nextInt(existingIDs.length());
                sb.append(existingIDs.charAt(index));
            }

            // the following unique ID that is an integer before will now be converted into a String
            String uniqueIDString = sb.toString();
            int uniqueID = Integer.parseInt(uniqueIDString);

            return uniqueID;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // return 0 if an error occurs
        return 0;
    }

    // user registration method
    private static void userRegistration() throws SQLException {

        boolean userRegistered = false;

        while (!userRegistered) {
            try {
                System.out.print("Please enter your name: ");
                customerName = sc.nextLine();

                System.out.print("Please enter your phone number: ");
                phoneNumber = sc.nextLine();

                // generate a unique customer ID
                customerID = generateUniqueUserID();

                // get the current timestamp as the creation date
                createdAt = new Timestamp(System.currentTimeMillis());

                // insert the information into the database
                String sql = "INSERT INTO \"hotelReservationOfficial\".\"hotelSchema\".users (customer_user_id, customer_name, phone_number, created_at)" + "VALUES (?, ?, ?,?)";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, customerID);
                statement.setString(2, customerName);
                statement.setString(3, phoneNumber);
                statement.setTimestamp(4, createdAt);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("You have been registered!");
                    System.out.println("Your Customer User ID is: " + customerID + "\nCreated at: " + createdAt);
                    userRegistered = true;
                }
                else {
                    System.out.println("Failed to register user. Please try again");
                    userRegistration();
                }

                statement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            catch (NullPointerException ex1) {
                System.out.println("Error accessing a certain column for user registration");
            }
            catch (InputMismatchException ex2) {
                System.out.println("Please enter the correct format");
                sc.nextLine();
            }
        }
    }

    private static void displayServices() {

    }

    private static void searchAvailableRooms() {
        System.out.println("Available Room Search");
    }
}