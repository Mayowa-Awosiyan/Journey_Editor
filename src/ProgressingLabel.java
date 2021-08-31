//helper class to make custom ids for nodes that don't conflict with the library generated ids
public class ProgressingLabel {
    private int id;
    private String name;
    private int remove;

    public ProgressingLabel(String name){
        this.name = name;
        this.id = 1;
        this.remove = name.length();
    }

    public int getId() {
        return id;
    }
    public void setId(int recieved){
        this.id= recieved;
    }
    public String getName() {
        return name;
    }

    public void progress(){
        this.id++;
    }

    public String toString(){
        return name + String.valueOf(id);
    }
    public int getTarget(String label){
        label = label.replaceFirst(name,"");
        return Integer.parseInt(label);
    }

    public int getTarget(String label, String role){
        label = label.replaceFirst(role,"");
        return Integer.parseInt(label);
    }

}
