import java.util.Date;

public class GrantEntry extends DataEntry{
    private Date date;
    private Date recDate;
    private Date finDate;
    private int source;
    private String notes;
    //todo figure what data type investigator is and implement it
    //private String investigator;


    public GrantEntry(String name, Date date, String id) {
        super(name,id);
    }

    public GrantEntry(String name, Date date, String id, Date recDate, Date finDate, int source,String notes) {
        super(name,id);
        this.date=date;
        this.recDate = recDate;
        this.finDate = finDate;
        this.source = source;
        this.notes= notes;
    }

    public String getNotes() {
        return notes;
    }

    public Date getRecDate() {
        return recDate;
    }

    public Date getFinDate() {
        return finDate;
    }

    public Date getDate() {
        return date;
    }

    public int getSource() {
        return source;
    }

    public boolean equals(Object comp){

        if(comp == null){

            return false;
        }
        else if(comp.getClass() != GrantEntry.class){

            return false;
        }
        else if(comp.getClass() == GrantEntry.class){
            GrantEntry thing = (GrantEntry) comp;
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
