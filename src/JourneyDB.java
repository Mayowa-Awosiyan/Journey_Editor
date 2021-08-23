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
                Date dateJ = (Date) resultSet.getDate(5);
                String bui = resultSet.getString(4);
                String email = resultSet.getString("email");
                String fac = resultSet.getString("faculty");
                String phone = resultSet.getString("mobile_phone");
                String city = resultSet.getString("city");
                MemberEntry entry = new MemberEntry(fname,dateJ,id,lname,email,bui,fac,phone,city);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<EventEntry> getEvents(String query){
        ArrayList<EventEntry> cells = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String id =resultSet.getString(1);
                String fname= resultSet.getString(2);
                String lname=resultSet.getString(3);
                Date endDate = resultSet.getDate(5);
                Date dateJ = resultSet.getDate(4);
                String notes = resultSet.getString(6);
                int type = resultSet.getInt(7);
                EventEntry entry = new EventEntry(fname,dateJ,id,lname,type,endDate,notes);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<GrantEntry> getGrants(String query){
        ArrayList<GrantEntry> cells = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String id =resultSet.getString(1);
                String fname= resultSet.getString(2);
                Float amount=resultSet.getFloat(3);
                Date endDate = resultSet.getDate(7);
                Date dateJ = resultSet.getDate(6);
                String notes = resultSet.getString(10);
                int source = resultSet.getInt(9);
                Date fin = resultSet.getDate(8);
                boolean lri =resultSet.getBoolean(4);
                String status = resultSet.getString(5);

                GrantEntry entry = new GrantEntry(fname,dateJ,id,endDate,fin,source,notes, amount,lri,status);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<PartnerEntry> getPartners(String query){
        ArrayList<PartnerEntry> cells = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String id =resultSet.getString(1);
                String fname= resultSet.getString(2);
                int type =resultSet.getInt(5);
                String notes = resultSet.getString(4);
                int source = resultSet.getInt(3);

                PartnerEntry entry = new PartnerEntry(fname,id,source,notes,type);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<ProductEntry> getProducts(String query){
        ArrayList<ProductEntry> cells = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String id =resultSet.getString(1);
                String fname= resultSet.getString(2);
                Date date = resultSet.getDate(3);
                boolean ongoing = resultSet.getBoolean(4);
                boolean reviewed = resultSet.getBoolean(5);
                String doi =resultSet.getString(6);
                String notes = resultSet.getString(8);
                int type = resultSet.getInt(9);

                ProductEntry entry = new ProductEntry(fname,id,date,ongoing,reviewed,doi,notes,type);
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
