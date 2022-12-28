import java.util.Objects;

/**
 * Static factory for creating library commands.
 */
public final class CommandFactory { // the final keyword prevents subclassing of this utility class
    /**
     * These error messages were moved from LibraryCommand class where each class would've automatically had them.
     * They were moved here then made public and static because the LibraryCommand class can't be submitted.
     */
    public static final String NULL_LIB_MSG = "The library cannot be null.";
    public static final String NULL_BOOK_LIST_MSG = "The list of books in the library cannot be null";

    /** Not to be used. */
    private CommandFactory() {
        // Utility classes do not need constructors and this is a way of preventing them 
        // from being instantiated accidentally.
        throw new UnsupportedOperationException("This constructor should never be used.");
    }

    /**
     * Create library command for the given type and argument.
     * 
     * @param cmdType Type of the command to be created
     * @param argumentInput command argument to be used during initialisation of the command
     * @return Command of the given type initialised for the given argument. If command creation 
     * failed due to an illegal argument, null will be returned.
     * @throws NullPointerException If one of the given parameters is null.
     */
    public static LibraryCommand createCommand(CommandType cmdType, String argumentInput) {
        Objects.requireNonNull(cmdType, "Given command type must not be null.");
        Objects.requireNonNull(argumentInput, "Given argument input must not be null.");

        try {
            switch(cmdType) {
                case HELP: return new HelpCmd(argumentInput);
                case EXIT: return new ExitCmd(argumentInput);
                case ADD: return new AddCmd(argumentInput);
                case LIST: return new ListCmd(argumentInput);
                case SEARCH: return new SearchCmd(argumentInput);
                case REMOVE: return new RemoveCmd(argumentInput);
                case GROUP: return new GroupCmd(argumentInput);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
        }

        return null;
    }
}
