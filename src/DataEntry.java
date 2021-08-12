

//custom created class to hold information based on various
public class DataEntry extends Object {

    protected String name;
    protected String id;


    public DataEntry(String name, String id){
        this.name= name;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return name;
    }

    public boolean equals(Object object){
        if(object == null){
            return false;
        }
        else if(object.getClass() != DataEntry.class){
            return false;
        }
        else if(this.getId()== ((DataEntry) object).getId()
            && this.getName() == ((DataEntry) object).getName()){
            return true;
        }
        else {
            return false;
        }
    }


    //placeholder functions to be overridden
    public void toggleDate(){
        ;
    }
    public void toggleBusiness(){
        ;
    }
    public void toggleEmail(){
        ;
    }
    public void togglePhone(){
        ;
    }
    public void toggleCity(){
        ;
    }
    public void toggleFaculty(){
        ;
    }
    public void toggleTheme(){
        ;
    }
    public void toggleType(){
        ;
    }
    public void toggleAmount(){
        ;
    }
}
