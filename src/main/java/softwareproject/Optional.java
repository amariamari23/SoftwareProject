package softwareproject;

    /**
     * The Argument class extends the Argument class. 
     *
     * @author CS XXY Team
     * @version Fall 2015
     */
public class Optional extends Argument{
    
    /**
     * Creates a new Optional argument. 
     *
     */
    public Optional(){
        
    }
    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue(){
        /*if(type == Argument.Type.INT) return (T)(Integer)value;
        else if(type == Argument.Type.BOOLEAN) return (T)(Boolean)value;
        else if(type == Argument.Type.FLOAT) return (T)(Float)value;
        else return (T)(String)value;*/
        return value;
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
    public void setShortHand(String shorthand){
        this.shorthand = shorthand;
    }
    
    public String getShortHand() {
        return shorthand;
    }
    
    public void setRequired(boolean required){
        this.required = required;
    }
    
    public boolean getRequired() {
        return required;
    }
    
    public void setDefault(Object defaultValue){
        value = defaultValue;
    }
    
    public Object getDefault(){
        return value;
    }
}