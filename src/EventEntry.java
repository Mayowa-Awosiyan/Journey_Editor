import java.util.Date;

public class EventEntry extends DataEntry{

    private String frName;
    private int type;
    private Date startDate;
    private Date endDate;
    private String notes;
    private boolean[] display;



    public EventEntry(String name, String id) {
        super(name, id);
    }

    public EventEntry(String name, Date date, String id, String frName, int type, Date endDate, String notes) {
        super(name, id);
        this.startDate=date;
        this.frName = frName;
        this.type = type;
        this.endDate = endDate;
        this.notes = notes;
        this.display = new boolean[] {false,false,false,false,false};

    }

    public String getFrName() {
        return frName;
    }

    public int getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }


    public boolean equals(Object comp){

        if(comp == null){

            return false;
        }
        else if(comp.getClass() != EventEntry.class){

            return false;
        }
        else if(comp.getClass() == EventEntry.class){
            EventEntry thing = (EventEntry) comp;
            //ids are unique between event entries so only id needs to be compared
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

    public void toggleDate(){
        display[0]= !display[0];
    }
    public void toggleType(){
        display[1]=!display[1];
    }

    //todo experiment with putting this in the toggleDate function
    public void displayEnd(){
        display[2] =!display[2];
    }
    public void displayNotes(){
        display[3] =!display[3];
    }

    public String toString(){
        String result = name + "\n"+ frName;
        String[] options= {startDate.toString(),String.valueOf(type),endDate.toString(), notes};
        for (int i =0; i< display.length; i++) {
            if(display[i] && options[i]!=null){
                result = result+ "\n" + options[i];
            }
        }
        return result;
    }
}
