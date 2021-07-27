//custom created class to hold information based on various
public class DataEntry extends Object {

    protected String name;
    protected String date;
    protected String id;

    public DataEntry(String name, String date, String id){
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
