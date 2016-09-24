package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.data.tag.Tag;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.data.tag.UniqueTagList.TagNotFoundException;

import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.Set;

/**
 * Adds a person to the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Edits a person identified by the index number used in the last person listing. "
            + "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
            + "Parameters: INDEX [NAME] [p]p/PHONE [p]e/EMAIL [p]a/ADDRESS  [t/TAG]...\n\t"
            + "Example: " + COMMAND_WORD
            + " 0 John Doe p/98765432 e/johnd@gmail.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney";

    public static final String MESSAGE_SUCCESS = "Person edited: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private Person toAdd;
    private ReadOnlyPerson target;
    private String name;
    private String phone;
    private String email;
    private String address;
    private boolean isPhonePrivate;
    private boolean isEmailPrivate;
    private boolean isAddressPrivate;
    private Set<String> tags;

    /**
     * Convenience constructor using raw values.
     *
     */
    public EditCommand(int targetVisibleIndex, String name,
                      String phone, boolean isPhonePrivate,
                      String email, boolean isEmailPrivate,
                      String address, boolean isAddressPrivate,
                      Set<String> tags) throws IllegalValueException {
        super(targetVisibleIndex);
        this.name = name;
        this.phone = phone;
        this.isPhonePrivate = isPhonePrivate;
        this.email = email;
        this.isEmailPrivate = isEmailPrivate;
        this.address = address;
        this.isAddressPrivate = isAddressPrivate;
        this.tags = tags;
        
    }


    public ReadOnlyPerson getPerson() {
        return toAdd;
    }

    
    @Override
    public CommandResult execute() {
        try {
            target = getTargetPerson();
            if(name==null){
                name = target.getName().toString();
            }
            if (phone == null){
                phone = target.getPhone().toString();
                isPhonePrivate = target.getPhone().isPrivate();
            }
            if (email == null){
                email = target.getEmail().toString();
                isEmailPrivate = target.getEmail().isPrivate();
            }
            if (address == null){
                address = target.getAddress().toString();
                isAddressPrivate = target.getAddress().isPrivate();
            }
            Set<Tag> tagSet = new HashSet<>();
            for (String tagName : tags) {
                tagSet.add(new Tag(tagName));
            }
            if(tagSet.isEmpty()){
                tagSet = target.getTags().toSet();
            }
            this.toAdd = new Person(
                    new Name(name),
                    new Phone(phone, isPhonePrivate),
                    new Email(email, isEmailPrivate),
                    new Address(address, isAddressPrivate),
                    new UniqueTagList(tagSet)
            );
            for(Tag tag: target.getTags().toSet()){
                addressBook.removeTag(tag);
            }
            addressBook.removePerson(target);
            addressBook.addPerson(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniquePersonList.DuplicatePersonException dpe) {
            return new CommandResult(MESSAGE_DUPLICATE_PERSON);
        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        } catch (IllegalValueException e) {
            return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        } catch (TagNotFoundException e) {
            return new CommandResult(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
    }

}
