public class CustomEntry extends DataEntry{

    private String content;

    public CustomEntry(String name, String id) {
        super(name, id);
        this.content = null;
    }

    public CustomEntry(String name, String id, String content){
        super(name,id);
        this.content= content;
    }

    public void setContent(String thing){
        this.content = thing;
    }

    public String toString(){
        return content;
    }

}
