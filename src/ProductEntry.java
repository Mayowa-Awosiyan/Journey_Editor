import java.util.ArrayList;
import java.util.Date;

public class ProductEntry extends DataEntry{

    private Date date;
    private boolean ongoing;
    private boolean peerReviewed;
    private String doi;
    private String notes;
    private String[] type;
    private boolean[] display;
    private ArrayList<String[]> stakeholder;
    private boolean english;


    public ProductEntry(String name, String id) {
        super(name, id);
    }

    //todo consider moving the english boolean to be a global variable in journeyeditor
    public ProductEntry(String name, String id, Date date, boolean ongoing, boolean peerReviewed, String doi, String notes, String[] type, ArrayList<String[]> stakeholder) {
        super(name, id);
        this.date = date;
        this.ongoing = ongoing;
        this.peerReviewed = peerReviewed;
        this.doi = doi;
        this.notes = notes;
        this.type = type;
        this.stakeholder=stakeholder;
        this.display = new boolean[] {false,false,false,false,false,false,false};
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

    public String[] getType() {
        return type;
    }

    public void toogleFrench(){english=!english;}

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
    public void toggleStakeHolder(){
        display[6] = !display[6];
    }

    public String[] stakeHolders(ArrayList<String[]> stakes){
        String[] holders = new String[stakes.size()];

        for(int i = 0;i<stakes.size();i++){
            if(english){
                holders[i] = stakes.get(i)[0];
            }
            else {
                holders[i] = stakes.get(i)[1];
            }
        }
        return holders;
    }

    public String toString(){
        String result = name;
        String stakes;
        if(stakeholder.size() > 0){
            String[] tmp = stakeHolders(stakeholder);
            stakes = tmp[0];
            for(String curr: tmp){
                if(curr.equals(tmp[0])){
                    continue;
                }
                stakes = stakes + "\n"+ curr;
            }
        }
        else{
            stakes = null;
        }
        String prodType;
        if(english){
            prodType= type[0];
        }
        else{
            prodType = type[1];
        }
        String tmpdate;
        if (date== null){
            tmpdate = null;
        }
        else {
            tmpdate = date.toString();
        }
        String[] options= {tmpdate,String.valueOf(ongoing),String.valueOf(peerReviewed), doi,notes, prodType,stakes};

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
