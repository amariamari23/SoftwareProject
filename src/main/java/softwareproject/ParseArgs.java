
package softwareproject;

import java.util.*;
import java.lang.*;

import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
*The ParseArgs class allows the creation of a program 
*with arguments that can be required or not. This involves 
*set the program name, description of the program, and the 
*arguments that the main programmer would like for the 
*program. It also saves an .xml file for the main that the 
*programmer creates. It also takes in an .xml file to read 
*it in. It uses the Argument, Positional, and Optional 
*classes.
*
*@see Argument Class
*@see Positional Class
*@see Optional Class
*@author CS 310 XXY Team
*@version Fall 2015
*/
public class ParseArgs{
    protected static Map<String, Argument> map;
    protected static Map<String, String> shmap;
    
    protected static List<String> positionalKeys;
    protected static List<String> optionalKeys;
    
    private boolean messageTrue;
    
    private String programName;
    private String programDescription;
    private String helpMessage;
        
    protected XMLParse x;
    
    /**
    *Constructs a new ParseArgs.
    *<p>
    *This starts the new maps needed to keep up with the 
    *arguments given by the programmer, the new ArrayLists 
    *for keeping the lists of positional and optional keys, 
    *and begins the help message.
    */
    public ParseArgs() {
        map = new HashMap<String, Argument>();
        shmap = new HashMap<String, String>();
        
        positionalKeys = new ArrayList<String>();
        optionalKeys = new ArrayList<String>();
        
        messageTrue = false;
        
        helpMessage = "usage: java ";    
    }
    /**
     * addPos adds a positional argument, an argument that is always required. 
     *
     * @param  description  what it is
     * @param  name the name of the key
     * @param  type enum from class Argument that tells the program the datatype of the argument
     * @see         Positional Class
     */
    public void addPos(String name, String description, Argument.Type type)
    {
        Positional temp = new Positional();
        positionalKeys.add(name);
        temp.setDescription(description);
        temp.setType(type);
        map.put(name, temp);
    }
    
    /**
    * addOpt adds an optional argument, an argument that is not required.
    *
    *@param   name            is the name of the optional argument.
    *@param   defaultValue    is a default value for the optional argument.
    *@param   type            is the enum from class Argument that tells the program whether the argument is a string, integer, float, or boolean.
    *@see     Optional Class
    */
    public void addOpt(String name, Object defaultValue, Argument.Type type){
        Optional temp = new Optional();
        optionalKeys.add(name);
        temp.setType(type);
        temp.setDefault(defaultValue);
        
        String shorthand = "";
        if(("-" + name.charAt(0)) != "-h") 
            shorthand = "-" + name.charAt(0);
        else
            shorthand = "-";
        temp.setShortHand(shorthand);
        map.put(name, temp);
        shmap.put(shorthand, name);
    }
    
    /**
    *readXML calls XMLParse.java to read a .xml file
    *
    *@param   file     the name of the .xml file
    *@see     XMLParse Class
    */
    public void readXML(String file){
        x = new XMLParse(); 
        x.readXML(file);
    }
    
    /**
    *saveToXML creates a new .xml file that can be read back in. 
    *This is used to create new positional or optional arguments.
    *
    * @param   file the file to be saved
    */
    public void saveToXML(String file){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuild = docFactory.newDocumentBuilder();
            
            Document doc = docBuild.newDocument();
                    
            Element root = doc.createElement("Argument");
            int posCount = 0, optCount = 0;
            for(int i = 0; i <  numberOfTotalKeys(); i++){
                if(posCount <  numberOfPositionalKeys()){
                    Element pos = doc.createElement("Positional");
                    
                    String key =  getPositionalKey(posCount);
                    Argument temp =  getArg(key);
                    
                    Element child1 = doc.createElement("name");
                    child1.appendChild(doc.createTextNode(key));
                    
                    Argument.Type type = temp.getType();
                    Element child2 = doc.createElement("type");
                    child2.appendChild(doc.createTextNode(type.name()));
                    
                    String descript = temp.getDescription();
                    Element child3 = doc.createElement("description");
                    child3.appendChild(doc.createTextNode(descript));
             
                    pos.appendChild(child1);
                    pos.appendChild(child2);
                    pos.appendChild(child3);
                    root.appendChild(pos);
                    
                    posCount ++;
                }
                else if(optCount <  numberOfOptionalKeys()){
                    Element opt = doc.createElement("Optional");
                    
                    String key =  getOptionalKey(optCount);
                    Argument temp =  getArg(key);
                    
                    Element child1 = doc.createElement("name");
                    child1.appendChild(doc.createTextNode(key));
                    
                    Element child2 = doc.createElement("shorthand");
                    String shorthand = temp.getShortHand().substring(1);
                    child2.appendChild(doc.createTextNode(shorthand));
                    
                    Element child3 = doc.createElement("type");
                    Argument.Type type = temp.getType();
                    child3.appendChild(doc.createTextNode(type.name()));
                    
                    Element child4 = doc.createElement("default");
                    Object def = temp.getValue();
                    child4.appendChild(doc.createTextNode(def.toString()));
                    
                    opt.appendChild(child1);
                    opt.appendChild(child2);
                    opt.appendChild(child3);
                    opt.appendChild(child4);
                    root.appendChild(opt);
                    
                    optCount ++;
                }
                
            }
            doc.appendChild(root);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file));
                
            transformer.transform(source, result);
            
        }
        catch(ParserConfigurationException | TransformerException e) {
            System.err.println(getUsage() + "\n" + programName + ".java SaveToXML() error: File " + file + " was not found");
        }
    }
    
    /**
    *setShortHand sets a shorthand name to the specified key except if -h is 
    *given as the shorthand name because it is reserved for help
    *
    *@param   key          the key to set the shorthand name to
    *@param   shorthand    the desired shorthand name
    */
    public void setShortHand(String key, String shorthand){
        if(shorthand == "h")
            throw new HelpMessageException("-h is used for only help");
        shorthand = "-" + shorthand;
        Argument temp = getArg(key);
        temp.setShortHand(shorthand);
        map.put(key,temp);
        shmap.put(shorthand, key);
    }
    
    /**
    *parse checks if help is called then sets and converts the user's input
    *
    *@param   args    the given arguments from command prompt
    */
    public void parse(String[] args)
    {
        queueToMap(args);
    }
    
    /**
    *getUsage creates and returns a message that states "usage: java " 
    *then the program name, all the positional argument names, and all 
    *the optional argument names.
    *
    *@return the message that was built
    */
    public String getUsage() {
        String message = "usage: java " + programName;
        for(int a = 0; a < positionalKeys.size(); a++) {
            message = message + " " + getPositionalKey(a);
        }
        for(int a = 0; a < optionalKeys.size(); a++) {
            message = message + " " + getOptionalKey(a);
        }
        return message;
    }
    
    private void queueToMap(String[] args){
        Queue<String> argsQueue = new LinkedList<String>();
        for(int i = 0; i < args.length; i++){
            argsQueue.add(args[i]);
        }
        int posCount = 0;
        while(argsQueue.peek() != null){
            String arg = argsQueue.remove();
            String key = "";
            if(arg.startsWith("--")){
                if(arg.equals("--help")){
                    messageTrue = true;
                    throw new HelpMessageException(getHelpMessage());
                }
                else if(map.containsKey(arg.substring(2))){
                    key = arg.substring(2);
                    arg = argsQueue.remove();
                }
                else
                    throw new WrongArgumentException(getUsage() + "\n" + programName + ".java: error: Argument: \"" + arg.substring(2) + "\" does not exist");
            }
            else if(arg.startsWith("-")){
                if(arg.equals("-h")){
                    messageTrue = true;
                    throw new HelpMessageException(getHelpMessage());
                }
                else if(shmap.containsKey(arg)){
                    key = shmap.get(arg);
                    arg = argsQueue.remove();
                }
                else
                    throw new WrongArgumentException(getUsage() + "\n" + programName + ".java: error: Shorthand Argument: \"" + arg.substring(1) + "\" does not correspond to any Argument");
            }
            else if(posCount >= positionalKeys.size()){
                posCount++;
                throw new TooManyArgumentsException(getUsage() + "\n" + programName + ".java: error: Unrecognized Argument: " + arg);
                }
            else{
                key = positionalKeys.get(posCount);
                posCount++;
            }
            
            Argument temp = getArg(key);
            Argument.Type type = temp.getType();
            try{
                if(type == Argument.Type.INT){
                    Integer.parseInt(arg);
                }
                else if(type == Argument.Type.FLOAT){
                    Float.valueOf(arg);
                }
                else if(type == Argument.Type.BOOLEAN){
                    if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("t"))
                    {
                        arg = "true";
                    }
                    else if (arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("f"))
                    {
                        arg = "false";
                    }
                    else
                    {
                        throw new NumberFormatException();
                    }
                }
                temp.setValue(arg);
                map.put(key, temp);
           }
           catch(NumberFormatException e){
                throw new NumberFormatException(getUsage() + "\n" + programName + ".java: error: argument " + key + ": invalid " + type + " value: " + arg); 
           }
        }
        if(!messageTrue)
        {
            if(posCount < positionalKeys.size()){
                String message = getUsage() + "\n" + programName + ".java: error: not enough positional arguments: missing:\n";
                for(int a = posCount; a < positionalKeys.size(); a++) 
                    message = message + " " + getPositionalKey(a);
                throw new TooLittleArgumentsException(message);
            }            
        } 
    }
    
    /**
    *numberOfPositionalKeys returns the total number of positional keys
    *
    *@return the total number of positional keys
    */
    public int numberOfPositionalKeys(){
        return positionalKeys.size();
    }
    
    /**
    *numberOfOptionalKeys returns the total number of optional keys
    *
    *@return the total number of optional keys
    */
    public int numberOfOptionalKeys(){
        return optionalKeys.size();
    }
    
    
    /**
    *numberOfTotalKeys returns the total number of keys
    *
    *@return the total number of keys
    */
    public int numberOfTotalKeys(){
        return optionalKeys.size() + positionalKeys.size();
    }
    
    /**
    *getHelpMessage returns the message to help the user to understand the program
    *
    *@return the message to help the user to understand the program
    */
    public String getHelpMessage() {
        return helpMessage;
    }
    
    /**
    *doesHelpWork returns a boolean to determine if help works
    *
    *@return a boolean to determine if help works
    */
    public boolean doesHelpWork(){
        return messageTrue;
    }
    
    /**
    *getArg returns the Argument associated with the given key
    *
    *@param key    the key associated with the desired Argument
    *@return the Argument associated with the given key
    *@see Argument Class
    */
    public Argument getArg(String key)
    {
        Argument temp = new Argument();
        temp = map.get(key);
        return temp;
    }
    
    /**
    *getValue calls getValue of Argument to return the user's input associated with the given key
    *
    *@param key    the key associated with the desired user input
    *@return the user's input
    */
    public <T> T getValue(String key)
    {
        Argument temp = getArg(key);
        return temp.getValue();
    }
    
    /**
    *getPositionalKey takes an integer to return the name of the Positional Key at that index
    *
    *@param where   the index of the desired Positional Key
    *@return the name of the Positional Key
    */
    public String getPositionalKey(int where)
    {
        String s = positionalKeys.get(where);
        return s;
    }
    
    /**
    *getOptionalKey takes an integer to return the name of the Optional Key at that index
    *
    *@param where   the index of the desired Optional Key
    *@return the name of the Optional Key
    */
    public String getOptionalKey(int where)
    {
        String s = optionalKeys.get(where);
        return s;
    }
    
    /**
    *getProgramName returns the name of the program
    *
    *@return the name of the Program
    */
    public String getProgramName(){
        return programName;
    }
    
    /**
    *programInfo sets the name of the program and uses the description 
    *of the program and description of the positional keys through getArg
    *to build the help message
    *
    *@param name           name of the program
    *@param description    description of the program
    */
    public void programInfo(String name, String description){
        String key = "";
        Argument temp = new Positional();
        this.programName = name;
        this.programDescription = description;
        for(String k : positionalKeys)
        {
            key = key + " " + k;
        }          
        helpMessage = helpMessage + name + key + "\n" + description;
        
        helpMessage = helpMessage + "\nPositional arguments:";
        
        for(String k : positionalKeys)
        {
            temp = getArg(k);
            helpMessage = helpMessage + "\n" + k + " " + temp.getDescription();
        }
    }
    

}
