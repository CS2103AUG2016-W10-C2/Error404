package seedu.addressbook.commands;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.util.List;
import java.util.Optional;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    /** The feedback message to be shown to the user. Contains a description of the execution result */
    public final String feedbackToUser;

    /** The list of persons that was produced by the command */
    private final List<? extends ReadOnlyPerson> relevantPersons;
    
    private final AddressBook state;
    private boolean isUndone;

    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = feedbackToUser;
        relevantPersons = null;
        state = null;
    }

    public CommandResult(String feedbackToUser, List<? extends ReadOnlyPerson> relevantPersons) {
        this.feedbackToUser = feedbackToUser;
        this.relevantPersons = relevantPersons;
        state = null;
    }
    
    public CommandResult(String feedbackToUser, AddressBook state, boolean undoneStatus) {
        this.feedbackToUser = feedbackToUser;
        this.relevantPersons = null;
        this.state = state;
        isUndone = undoneStatus;
        System.out.println("isUndone " + isUndone);
    }

    /**
     * Returns list of persons relevant to the command command result, if any.
     */
    public Optional<List<? extends ReadOnlyPerson>> getRelevantPersons() {
        return Optional.ofNullable(relevantPersons);
    }
    
    /**
     *  Returns previous state of addressBook to command command result, if succeeded
     */
    public AddressBook getRevertState() {
        return state;
    }
    
    public boolean getUndoStatus() {
        return isUndone;
    }
}
