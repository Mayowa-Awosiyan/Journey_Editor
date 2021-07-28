import java.util.Date;

public class EventEntry extends DataEntry{

    private String frName;
    private int type;
    private Date date;


    public EventEntry(String name, Date date, String id) {
        super(name, date, id);
    }
}
