package softwareproject;

    /**
     * The Positional class extends the Argument class. 
     *
     * @author CS XXY Team
     * @version Fall 2015
     */
public class Positional extends Argument {
    
    /**
     * Creates a new Positional argument. 
     *
     */
    public Positional(){
    }
    public void setValue(Object value){
        this.value = value;
    }
    public <T> T getValue(){
        if(type == Argument.Type.INT) return (T)(Integer.valueOf((String)value));
        else if(type == Argument.Type.BOOLEAN) return (T)(Boolean.valueOf((String)value));
        else if(type == Argument.Type.FLOAT) return (T)(Float.valueOf((String)value));
        else if(type == Argument.Type.STRING) return (T)(String)value;
        return (T)value;
    }
    public void setType(Type type){
        this.type = type;
    }
    public Type getType(){
        return type;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }  
}
