import java.nio.file.Path;
import java.util.Objects;

/**
 * Add command that adds new book entries to the library from a file's data.
 * The file is located by the user given argument.
 */
public class AddCmd extends LibraryCommand {
    /** The file extension for the required file type - csv - including the preceding full stop.*/
    private final String FILE_EXTENSION = ".csv";

    /** Path provided by user input. It points to the file the book data will be loaded from. */
    private Path pathName;

    /**
     * Create an add command.
     *
     * @param argumentInput the file path of the file with data to be loaded from.
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException if the given argumentInput is null.
     */
    public AddCmd(String argumentInput) {
        super(CommandType.ADD, argumentInput);
    }

    /**
     * Execute the add command. This calls the library data's
     * loadData function to add book entries from the specified file.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if the book data passed is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, CommandFactory.NULL_LIB_MSG);
        data.loadData(pathName);
    }

    /**
     * Parses the given user input to create a file path.
     *
     * A file path with the required file ending is expected.
     *
     * @param argumentInput the file path.
     * @return true if the argument input ends with the required file extension.
     *         Otherwise returns false, indicating invalid input.
     * @throws NullPointerException if the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        super.parseArguments(argumentInput);
        if (argumentInput.length() < FILE_EXTENSION.length()) {
            return argumentInput.length() >= FILE_EXTENSION.length();
        }

        String pathEnding = argumentInput.substring(argumentInput.length() - FILE_EXTENSION.length());
        pathName = Path.of(argumentInput);
        return pathEnding.equals(FILE_EXTENSION);
    }
}
