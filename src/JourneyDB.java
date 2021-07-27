import java.sql.*;
import java.util.ArrayList;

public class JourneyDB {

    //the url points to the database in question
    private String url;
    //the connection to the database that the url points to
    private Connection connection;

    public JourneyDB(String point) throws SQLException {
        this.url = point;
        connection = DriverManager.getConnection(url);
    }

    public JourneyDB() throws SQLException {
        this.url = "jdbc:ucanaccess://SummerCOOPv2.accdb";
        connection = DriverManager.getConnection("jdbc:ucanaccess://SummerCOOPv2.accdb");
    }
    //todo make more flexible
    public ArrayList<MemberEntry> getMembers(String query){
        ArrayList<MemberEntry> cells = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String id =resultSet.getString(1);
                String fname= resultSet.getString(2);
                String lname=resultSet.getString(3);
                String dateJ = resultSet.getString(5);
                MemberEntry entry = new MemberEntry(fname,dateJ,id,lname);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }


    public static void main(String[] args) {
        String url = "jdbc:ucanaccess://SummerCOOPv2.accdb";

        try{
            Connection connection = DriverManager.getConnection(url);
            System.out.println("Connection made");
            Statement query = connection.createStatement();
            ResultSet answer = query.executeQuery("Select * From main_members where id <= 15");
            while (answer.next()){
                String id = answer.getString("id");
                String name = answer.getString("first_name");
                String type = answer.getString("last_name");
                String scope = answer.getString("business_name");
                String notes = answer.getString("date_joined");
                String blank = answer.getString("id");
                System.out.println(id + " " +name + " "+ type + " "+ scope + " "+ notes);
            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
