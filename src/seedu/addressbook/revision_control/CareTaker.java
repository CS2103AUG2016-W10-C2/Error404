package seedu.addressbook.revision_control;

import java.util.ArrayList;

public class CareTaker {

    public ArrayList<Memento> mementoList;
    
    public CareTaker() {
        mementoList = new ArrayList<Memento>();
    }

    public void add(Memento state) {
        mementoList.add(state);
    }

    public Memento get(int index) {
        return mementoList.get(index);
    }
}
