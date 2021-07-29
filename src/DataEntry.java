import java.util.Date;

//custom created class to hold information based on various
public class DataEntry extends Object {

    protected String name;
    protected Date date;
    protected String id;


    public DataEntry(String name, Date date, String id){
        this.name= name;
        this.date=date;
        this.id=id;
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

    public String toString(){
        return name + "\n" + date;
    }

    public boolean equals(Object object){
        if(object == null){
            return false;
        }
        else if(object.getClass() != DataEntry.class){
            return false;
        }
        else if(((DataEntry) object).getDate().equals(this.getDate()) && this.getId()== ((DataEntry) object).getId()
            && this.getName() == ((DataEntry) object).getName()){
            return true;
        }
        else {
            return false;
        }
    }
}
