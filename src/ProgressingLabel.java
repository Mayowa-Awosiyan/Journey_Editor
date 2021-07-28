//helper class to make custom ids for nodes that don't conflict with the library generated ids
public class ProgressingLabel {
    private int index;
    private String name;

    public ProgressingLabel(String name){
        this.name = name;
        this.index= 0;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void progress(){
        this.index++;
    }

    public String toString(){
        return name + String.valueOf(index);
    }

}
