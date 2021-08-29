import java.util.ArrayList;
import java.util.Date;

public class EventEntry extends DataEntry{

    private String frName;
    private String[] type;
    private Date startDate;
    private Date endDate;
    private String notes;
    private boolean[] display;
    private boolean english;
    private ArrayList<String[]> themes;

    public EventEntry(String name, String id) {
        super(name, id);
        this.english=true;
    }

    public EventEntry(String name, Date date, String id, String frName, String[] type, Date endDate, String notes, ArrayList<String[]> themes) {
        super(name, id);
        this.startDate=date;
        this.frName = frName;
        this.type = type;
        this.endDate = endDate;
        this.notes = notes;
        this.english = true;
        this.themes=themes;
        this.display = new boolean[] {false,false,false,false,false,false};

    }

    public String getFrName() {
        return frName;
    }

    public String[] getType() {
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

    public void toggleEnglish(){english =!english;}

    //todo experiment with putting this in the toggleDate function
    public void displayEnd(){
        display[2] =!display[2];
    }
    public void displayNotes(){
        display[3] =!display[3];
    }
    public void toggleThemes(){display[4] =!display[4];}

    public String[] themes(ArrayList<String[]> stakes){
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

    //by default shows both english name and french name
    public String toString(){
        String result = name;
        String evtType = type[0];
        if(!english){
            result = frName;
            evtType= type[1];
        }
        String themeList;
        if(themes.size() > 0){
            String[] tmp = themes(themes);
            themeList = tmp[0];
            for(String curr: tmp){
                if(curr.equals(tmp[0])){
                    continue;
                }
                themeList = themeList + "\n"+ curr;
            }
        }
        else{
            themeList = null;
        }
        String[] options= {"S: "+ startDate.toString(),evtType,"E: "+endDate.toString(), notes,themeList};
        for (int i =0; i< display.length; i++) {
            if(display[i] && options[i]!=null){
                result = result + "\n" + options[i];
            }
        }
        return result;
    }
}
