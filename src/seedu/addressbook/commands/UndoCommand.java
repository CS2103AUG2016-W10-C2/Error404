package seedu.addressbook.commands;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.person.UniquePersonList.DuplicatePersonException;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.data.tag.UniqueTagList.DuplicateTagException;
import seedu.addressbook.logic.Logic;
import seedu.addressbook.revision_control.CareTaker;
import seedu.addressbook.revision_control.Originator;

/**
 * Undo most recently successful command which mutates the data of AddressBook.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Undo your most recent command which mutates the data such as add,delete,edit,clear" + "Example: "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Your AddressBook action has been reverted successfully";

    private Originator originator;
    private CareTaker careTaker;


    /**
     * To request logic to give additional input for executing Undo.
     */
    public UndoCommand(Logic logic) {

    }

    public UndoCommand(Originator originator, CareTaker careTaker, int numCmd, int numUndo) {
        this.originator = originator;
        this.careTaker = careTaker;

    }

    @Override
    public CommandResult execute() {

        /*
         * new AddressBook(new UniquePersonList(personList), new UniqueTagList(tagList))
         * 
         */
        originator.getStateFromMemento(careTaker.get(0)); // does not work when ported here
        return new CommandResult(String.format(MESSAGE_SUCCESS), 
                new AddressBook(new UniquePersonList( originator.getState().getAllPersons()),        
                        new UniqueTagList(originator.getState().getAllTags())),// does not work.
                                                 true); 
    }

}
