package seedu.addressbook.revision_control;

import seedu.addressbook.data.AddressBook;

public class Memento {

    private AddressBook state;
   
    
    public Memento(AddressBook state) {
        this.state = state;
    }

    public AddressBook getState() {
        return state;
    }

}
