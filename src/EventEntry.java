import java.util.Date;

public class EventEntry extends DataEntry{

    private String frName;
    private int type;
    private Date startDate;
    private Date endDate;
    private String notes;



    public EventEntry(String name, Date date, String id) {
        super(name, date, id);
    }

    public EventEntry(String name, Date date, String id, String frName, int type, Date endDate, String notes) {
        super(name, date, id);

        this.startDate=date;
        this.frName = frName;
        this.type = type;
        this.endDate = endDate;
        this.notes = notes;

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
            if(thing.getId().equals(this.getId())){
                    System.out.println("ITS TRUE v");
                    return true;
            }
            else{
                System.out.println("USED EVENT");
                return false;
            }
        }
        else {
            return false;
        }
    }
}
