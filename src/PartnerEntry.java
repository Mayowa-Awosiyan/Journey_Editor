

public class PartnerEntry extends DataEntry{

    private int scope;
    private String notes;
    private int type;




    public PartnerEntry(String name,String id) {
        super(name,id);
    }

    public PartnerEntry(String name, String id, int scope, String notes, int type) {
        super(name, id);
        this.scope = scope;
        this.notes = notes;
        this.type = type;
    }

    public int getScope() {
        return scope;
    }

    public String getNotes() {
        return notes;
    }

    public int getType() {
        return type;
    }


}
