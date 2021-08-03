

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


    public boolean equals(Object comp){
        if(comp == null){

            return false;
        }
        else if(comp.getClass() != PartnerEntry.class){

            return false;
        }
        else if(comp.getClass() == PartnerEntry.class){
            PartnerEntry thing = (PartnerEntry) comp;
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
