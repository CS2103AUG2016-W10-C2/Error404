package seedu.addressbook.revision_control;

import seedu.addressbook.data.AddressBook;

public class Originator {

    private AddressBook state;

    public void setState(AddressBook state) {
        this.state = state;
    }

    public AddressBook getState() {
        return state;
    }

    public Memento saveStateToMemento() {
        return new Memento(state);
    }

    public void getStateFromMemento(Memento Memento) {
        state = Memento.getState();
    }

}
