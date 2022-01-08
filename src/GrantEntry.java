import java.util.Date;

public class GrantEntry extends DataEntry{
    private Date date;
    private Date recDate;
    private Date finDate;
    private String[] source;
    private String notes;
    private boolean[] display;
    private Float amount;
    private boolean lri;
    private String status;

    private boolean english;
    //todo figure what data type investigator is and implement it
    //private String investigator;


    public GrantEntry(String name, Date date, String id) {
        super(name,id);
        this.date = date;
        this.display = new boolean[] {false,false,false, false,false,false, false,false};
    }

    public GrantEntry(String name, Date date, String id, Date recDate, Date finDate, String[] source,String notes, Float amount, boolean lri, String status) {
        super(name,id);
        this.date=date;
        this.recDate = recDate;
        this.finDate = finDate;
        this.source = source;
        this.amount = amount;
        this.notes= notes;
        this.lri = lri;
        this.status = status;
        this.english= true;
        this.display = new boolean[] {false,false,false, false,false,false, false,false};
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

    public String[] getSource() {
        return source;
    }

    public void toggleDate(){
        display[0] = !display[0];
    }

    public void toggleRecDate(){
        display[1]= !display[1];
    }

    public void toggleFinDate(){
        display[2] = !display[2];
    }
    public void toggleSource(){display[3] = !display[3];}

    public void toggleNotes(){display[4] = !display[4];}

    public void toggleAmount(){
        display[5]= !display[5];
    }

    public void toggleLRI(){
        display[6] =!display[6];
    }
    public void toggleStatus(){
        display[7]= !display[7];
    }
    public void toggleFrench(){english= !english;}

    public String toString(){
        String result = name;

        String[] options;
        if(english) {
            options = new String[]{(date != null) ? "Submitted:" + date.toString() : "Submission date unknown",
                    (recDate != null) ? "Received: " + recDate.toString() : "Not yet received",
                    (finDate != null) ? "Finish: " + finDate.toString() : "Not yet finished",
                    String.valueOf(source[0]),
                    notes,
                    "$"+String.valueOf(amount),
                    lri?"From Institute":"",
                    status};
        }
        else {
            options= new String[]{(date!=null) ? "Soumise:"+date.toString() : "Date de soumission inconnue",
                    (recDate!=null) ? "Reçue: "+recDate.toString() : "Pas encore reçue",
                    (finDate!=null) ? "Terminée: "+finDate.toString() : "Pas encore terminée",
                    String.valueOf(source[1]),
                    notes,
                    "$"+String.valueOf(amount),
                    lri?"De l'Institut":"",
                    status};
        }
        for (int i =0; i< options.length; i++) {
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
