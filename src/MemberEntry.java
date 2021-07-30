import java.util.Date;

public class MemberEntry extends DataEntry{
//todo add boolean for each variable that needs to toggle being seen
    private String lname;
    private String name;
    private Date date;
    private String id;
    private String email;
    private String business;
    private String faculty;
    private String phone;
    private String city;

    public MemberEntry(String name, String lname,Date date, String id, String email, String business, String faculty, String phone, String city) {

        super(name, date, id);
        this.lname = lname;
        this.email = email;
        this.business = business;
        this.faculty = faculty;
        this.phone = phone;
        this.city = city;
    }

    public MemberEntry(String name, Date date, String id, String lname) {
        super(name, date, id);
        this.name=name;
        this.id=id;
        this.date=date;
        this.lname = lname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String toString(){
        return name+ " " + lname +"\n"+  date;
    }
}
