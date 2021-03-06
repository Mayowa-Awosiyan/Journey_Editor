import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class JourneyDB {

    //the url points to the database in question
    private String url;
    //the connection to the database that the url points to
    private Connection connection;

    public JourneyDB(String point) throws SQLException {
        this.url = point;
        connection = DriverManager.getConnection("jdbc:ucanaccess://"+url);
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
                String bui = resultSet.getString(4);
                Boolean isGuest = resultSet.getBoolean("is_guest");
                Date dateJ;
                if (!isGuest)
                    dateJ = (Date) resultSet.getDate(5);
                else
                    dateJ = (Date) resultSet.getDate(7);
                if (dateJ==null)
                    dateJ=new Date();
                String email = resultSet.getString("email");
                String facNum = resultSet.getString("faculty");
                String[] fac = getEngAndFrench("Select name_en, name_fr from types_faculty where id ="+ facNum);
                String phone = resultSet.getString("business_phone");
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
                ArrayList<String[]> themes = getThemes("Select theme_id from relp_event_topic where event_id =" + id, id);
                String[] types = getEngAndFrench("Select type_en, type_fr from types_event where id ="+type);

                EventEntry entry = new EventEntry(fname,dateJ,id,lname,types,endDate,notes,themes);
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
                String[] source2 = getEngAndFrench("Select type_en, type_fr from types_grantsource where id = " +source);
                Date fin = resultSet.getDate(8);
                boolean lri =resultSet.getBoolean(4);
                String status = resultSet.getString(5);

                GrantEntry entry = new GrantEntry(fname,dateJ,id,endDate,fin,source2,notes, amount,lri,status);
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
                int scope =resultSet.getInt(4);
                String[] scopes = getEngAndFrench("select scope_en, scope_fr from types_partnershipscope where id =" + scope);
                String notes = resultSet.getString(5);
                int type = resultSet.getInt(3);
                String[] type2 = getEngAndFrench("Select type_en, type_fr from types_partnershiptype  where id = " + type);
                PartnerEntry entry = new PartnerEntry(fname,id,type2,notes,scopes);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<String> getNames(String selection){
        ArrayList<String> names = new ArrayList<>();
        String query;
        if(selection.equals("Member")){
            query = "Select first_name from main_Members";
        }
        else if(selection.equals("Event")){
            query = "Select name_en from main_Events";
        }
        else if(selection.equals("Grant")){
            query = "select title from main_Grants";
        }
        else if(selection.equals("Partner")){
            query = "Select name from main_Partners";
        }
        else{
            query = "Select title from main_Products";
        }
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                names.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return names;
    }

    public String[] getEngAndFrench(String query){
        String[] strings = new String[2];
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                strings[0] = resultSet.getString(1);
                strings[1] = resultSet.getString(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return strings;
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
                String[] type2 = getEngAndFrench("Select type_en, type_fr from types_product where id = " + type);
                ArrayList<String[]> stake = getStakeHolders("Select target_stakeholder_id from relp_product_TargetStakeholder where " +id
                        + " = relp_product_targetstakeholder.product_id", id);
                ProductEntry entry = new ProductEntry(fname,id,date,ongoing,reviewed,doi,notes,type2,stake);
                cells.add(entry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cells;
    }

    public ArrayList<String[]> getStakeHolders(String query, String id){
        ArrayList<String[]> stakes = new ArrayList<>();
        ArrayList<String> selected = new ArrayList<String>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                selected.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(String currId: selected){
            try{
                query = "select name_en, name_fr from types_targetstakeholder where id ="+ currId;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String[] holders = new String[2];
                    holders[0] = resultSet.getString(1);
                    holders[1] = resultSet.getString(2);
                    stakes.add(holders);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return stakes;
    }

    public ArrayList<String[]> getThemes(String query, String id){
        ArrayList<String[]> stakes = new ArrayList<>();
        ArrayList<String> selected = new ArrayList<String>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                selected.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(String currId: selected){
            try{
                query = "select name_en, name_fr from types_topic where theme_id ="+ currId;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String[] holders = new String[2];
                    holders[0] = resultSet.getString(1);
                    holders[1] = resultSet.getString(2);
                    stakes.add(holders);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return stakes;
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
