package seedu.addressbook.history;

import java.util.Enumeration;
import java.util.Stack;

public class History {

    public static final String EXAMPLE = "123, some street";
    public static final String MESSAGE_HISTORY_CONSTRAINTS = "Only successfully executed commands are recorded";
    public static final String ADDRESS_VALIDATION_REGEX = ".+";
    
    public final Stack<String> historyStack;
    
    public History() {
        historyStack = new Stack<String>();
    }
    
    /**
     * Adds most recently successful executed command to the History Manager. 
     */
    public void addLastCommand(String cmd) {
        historyStack.push(cmd);
    }
    
    /**
     * Removes most recently added command to the History Manager.
     */
    public void removeLastCommand() {
        historyStack.pop();
    }   
    
    /**
     * Clear list of previously executed commands in current session 
     * added to the history manager.
     */
     public void clear() {
         historyStack.clear();
     }
     
     public void printStack() {
         Enumeration<String> e = historyStack.elements();
         while(e.hasMoreElements()) {
             System.out.println(e.nextElement());
         }
     }
}
