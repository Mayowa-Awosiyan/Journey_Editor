import java.util.Date;

public class GrantEntry extends DataEntry{
    private Date date;
    private Date recDate;
    private Date finDate;
    private int source;
    private String notes;
    private boolean[] display;
    //todo figure what data type investigator is and implement it
    //private String investigator;


    public GrantEntry(String name, Date date, String id) {
        super(name,id);
        this.display = new boolean[] {false,false,false, false,false};
    }

    public GrantEntry(String name, Date date, String id, Date recDate, Date finDate, int source,String notes) {
        super(name,id);
        this.date=date;
        this.recDate = recDate;
        this.finDate = finDate;
        this.source = source;
        this.notes= notes;
        this.display = new boolean[] {false,false,false, false,false};
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

    public String toString(){
        String result = name;
        String[] options= {date.toString(),recDate.toString(),finDate.toString(),String.valueOf(source),notes};
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
