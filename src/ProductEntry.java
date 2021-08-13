import java.util.Date;

public class ProductEntry extends DataEntry{

    private Date date;
    private boolean ongoing;
    private boolean peerReviewed;
    private String doi;
    private String notes;
    private int type;
    private boolean[] display;


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
        this.display = new boolean[] {false,false,false,false,false,false};
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

    public void toggleDate(){
        display[0] = !display[0];
    }

    public void toggleOngoing(){
        display[1]= !display[1];
    }

    public void togglePeerReviewed(){
        display[2] = !display[2];
    }
    public void toggleDoi(){display[3] = !display[3];}

    public void toggleNotes(){display[4] = !display[4];}

    public void toggleType(){
        display[5] = !display[5];
    }

    public String toString(){
        String result = name;
        String[] options= {date.toString(),String.valueOf(ongoing),String.valueOf(peerReviewed), doi,notes, String.valueOf(type)};
        for (int i =0; i< display.length; i++) {
            if(display[i] && options[i]!=null){
                result = result+ "\n" + options[i];
            }
        }
        return result;
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
