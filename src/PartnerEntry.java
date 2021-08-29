

public class PartnerEntry extends DataEntry{

    private String[] scope;
    private String notes;
    private String[] type;
    private boolean[] display;
    private boolean english;

    public PartnerEntry(String name,String id) {
        super(name,id);
    }

    public PartnerEntry(String name, String id, String[] type, String notes, String[] scope) {
        super(name, id);
        this.scope = scope;
        this.notes = notes;
        this.type = type;
        this.english = true;
        this.display = new boolean[] {false,false,false};
    }

    public String[] getScope() {
        return scope;
    }

    public String getNotes() {
        return notes;
    }

    public String[] getType() {
        return type;
    }

    public void toggleNotes(){
        display[0] = !display[0];
    }

    public void toggleScope(){
        display[1]= !display[1];
    }

    public void toggleType(){
        display[2] = !display[2];
    }
    public void toggleFrench(){english= !english;}

    public String toString(){
        String result = name;
        String[] options= {notes, String.valueOf(scope[0]), String.valueOf(type[0])};
        if(!english){
            options= new String[]{notes, String.valueOf(scope[1]), String.valueOf(type[1])};
        }
        for (int i =0; i< display.length; i++) {
            if(display[i] && (options[i]!=null || options[i].equals(""))){
                result = result+ "\n" + options[i];
            }
        }
        return result;
    }


    public boolean equals(Object comp){
        if(comp == null){

            return false;
        }
        else if(comp.getClass() != PartnerEntry.class){

            return false;
        }
        else if(comp.getClass() == PartnerEntry.class){
            PartnerEntry thing = (PartnerEntry) comp;
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
