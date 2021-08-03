import java.util.Date;

public class ProductEntry extends DataEntry{

    private Date date;
    private boolean ongoing;
    private boolean peerReviewed;
    private String doi;
    private String notes;
    private int type;


    public ProductEntry(String name, String id) {
        super(name, id);
    }

    public ProductEntry(String name, String id, Date date, boolean ongoing, boolean peerReviewed, String doi, String notes, int type) {
        super(name, id);
        this.date = date;
        this.ongoing = ongoing;
        this.peerReviewed = peerReviewed;
        this.doi = doi;
        this.notes = notes;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public boolean isPeerReviewed() {
        return peerReviewed;
    }

    public String getDoi() {
        return doi;
    }

    public String getNotes() {
        return notes;
    }

    public int getType() {
        return type;
    }

    public String toString(){
        return name + "\n" + date;
    }

    public boolean equals(Object comp){
        if(comp == null){

            return false;
        }
        else if(comp.getClass() != ProductEntry.class){

            return false;
        }
        else if(comp.getClass() == ProductEntry.class){
            ProductEntry thing = (ProductEntry) comp;
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
