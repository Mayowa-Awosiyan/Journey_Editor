import java.util.Date;

public class MemberEntry extends DataEntry{
//todo add boolean for each variable that needs to toggle being seen
    private String lname;
    private String name;
    private Date date;
    private String email;
    private String business;
    private String[] faculty;
    private String phone;
    private String city;
    private boolean english;
    private boolean[] display;

    public MemberEntry(String name,Date date,String id,  String lname, String email, String business, String[] faculty, String phone, String city) {

        super(name, id);
        this.name= name;
        this.date = date;
        this.lname = lname;
        this.email = email;
        this.business = business;
        this.faculty = faculty;
        this.phone = phone;
        this.city = city;
        this.english = true;
        this.display= new boolean[]{false, false, false, false, false, false};
    }

    public MemberEntry(String name, Date date, String id, String lname) {
        super(name, id);
        this.name=name;
        this.id=id;
        this.date=date;
        this.lname = lname;
        this.display= new boolean[]{false, false, false, false, false, false};
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

    public String[] getFaculty() {
        return faculty;
    }

    public void setFaculty(String[] faculty) {
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


    public void toggleDate(){
        display[0] = !display[0];
    }

    public void toggleEmail(){
        display[1]= !display[1];
    }

    public void toggleBusiness(){
        display[2] = !display[2];
    }
    public void toggleFaculty(){display[3] = !display[3];}

    public void togglePhone(){display[4] = !display[4];}
    public void toggleCity(){
        display[5] = !display[5];
    }
    public void toggleEnglish(){english=!english;}

    //conditional toString method that by default prints name and last name
    public String toString(){
        String result = name + " " + lname;
        String[] options= {date.toString(),email,business,faculty[0],phone,city};
        if(!english){
            options= new String[]{date.toString(),email,business,faculty[1],phone,city};
        }
        for (int i =0; i< display.length; i++) {
            if(display[i] && options[i]!=null){
                result = result+ "\n" + options[i];
            }
        }
        return result;
    }

    public boolean equals(Object comp){
        if(comp == null){

            return false;
        }
        else if(comp.getClass() != MemberEntry.class){

            return false;
        }
        else if(comp.getClass() == MemberEntry.class){
            MemberEntry thing = (MemberEntry) comp;
            //ids are unique between grant entries so only id needs to be compared as
            // everything else can be identical for a different grant (tho rarely)
            if(thing.getId().equals(this.getId())){
                return true;
            }
            else{
                return false;
            }
        }
        else {
            return false;
        }

    }
}
