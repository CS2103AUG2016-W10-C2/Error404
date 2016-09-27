package seedu.addressbook.logic;

import seedu.addressbook.commands.Command;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.commands.UndoCommand;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.parser.Parser;
import seedu.addressbook.revision_control.CareTaker;
import seedu.addressbook.revision_control.Originator;
import seedu.addressbook.storage.StorageFile;
import seedu.addressbook.storage.jaxb.AdaptedAddressBook;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents the main Logic of the AddressBook.
 */
public class Logic {


    private StorageFile storage;
    private AddressBook addressBook;
    private Originator originator;
    private CareTaker careTaker;
    private int numCmd = 0;
    private int numUndo = 0;

    /** The list of person shown to the user most recently.  */
    private List<? extends ReadOnlyPerson> lastShownList = Collections.emptyList();

    public Logic() throws Exception{
        setStorage(initializeStorage());
        setAddressBook(storage.load());
        originator = new Originator();
        setCareTaker();
 
    }

    Logic(StorageFile storageFile, AddressBook addressBook){
        setStorage(storageFile);
        setAddressBook(addressBook);
        originator = new Originator();
        setCareTaker();
    }    

    void setStorage(StorageFile storage){
        this.storage = storage;
    }

    void setAddressBook(AddressBook addressBook){
        this.addressBook = addressBook;
    }    
    
    /**
     * Sets the careTaker to be the initial state upon starting the program.
     * CareTaker and originator will reset on every startup.
     */
    void setCareTaker() {
        careTaker = new CareTaker();
        originator.setState(addressBook);
        careTaker.add(originator.saveStateToMemento());
    }

    /**
     * Creates the StorageFile object based on the user specified path (if any) or the default storage path.
     * @throws StorageFile.InvalidStorageFilePathException if the target file path is incorrect.
     */
    private StorageFile initializeStorage() throws StorageFile.InvalidStorageFilePathException {
        return new StorageFile();
    }

    public String getStorageFilePath() {
        return storage.getPath();
    }
    
    public Originator getOriginator() {
        return originator;
    }
    
    public CareTaker getCareTaker() {
        return careTaker;
    }

    /**
     * Unmodifiable view of the current last shown list.
     */
    public List<ReadOnlyPerson> getLastShownList() {
        return Collections.unmodifiableList(lastShownList);
    }

    protected void setLastShownList(List<? extends ReadOnlyPerson> newList) {
        lastShownList = newList;
    }

    /**
     * Parses the user command, executes it, and returns the result.
     * @throws Exception if there was any problem during command execution.
     */
    public CommandResult execute(String userCommandText) throws Exception {
        Command command = new Parser().parseCommand(userCommandText);
        if (isUndo(command)) {            
            command = new UndoCommand(originator, careTaker, numCmd, numUndo);
        }
        CommandResult result = execute(command);
        recordResult(result);
        return result;
    }

    /**
     * Executes the command, updates storage, and returns the result.
     *
     * @param command user command
     * @return result of the command
     * @throws Exception if there was any problem during command execution.
     */
    private CommandResult execute(Command command) throws Exception {
        command.setData(addressBook, lastShownList);
        numCmd++;
        CommandResult result = command.execute();
        //TODO: save only if execute successfully mutates the data.
        storage.save(addressBook);
        if (!result.getUndoStatus()) {
            originator.setState(addressBook);
            System.out.println(originator.getState() + " 1");
            careTaker.add(originator.saveStateToMemento());
            System.out.println(result.getUndoStatus());
        }
        else  {
            // logic issues with num counter
            numUndo++;           
            System.out.println(careTaker.mementoList.size());
            System.out.println(originator.getState() + " 2");
           
            /*
             originator.getStateFromMemento(careTaker.get(0));
             addressBook = new AddressBook(new UniquePersonList( originator.getState().getAllPersons()),        
                                           new UniqueTagList(originator.getState().getAllTags()));// works for delete, To convert to command
             */
             
            //AddressBook(UniquePersonList persons, UniqueTagList tags)
        }
        
        
        return result;
    }

    /** Updates the {@link #lastShownList} if the result contains a list of Persons. */
    private void recordResult(CommandResult result) {
        final Optional<List<? extends ReadOnlyPerson>> personList = result.getRelevantPersons();
        if (personList.isPresent()) {
            lastShownList = personList.get();
        }
    }
    
    private boolean isUndo(Command command) {
        return (command.getClass().equals( new UndoCommand(null).getClass()) ? true : false);
    }
}
