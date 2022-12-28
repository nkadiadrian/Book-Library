import java.util.List;
import java.util.Objects;

/**
 * List command that prints all book entries to the console.
 * The way the entries are displayed is decided by a user given argument.
 */
public class ListCmd extends LibraryCommand {
    /** Command argument for displaying all book entries in full */
    private final String LONG_ARG = "long";
    /** Command argument for displaying all book entries by just their title */
    private final String SHORT_ARG = "short";

    /** The type of display to be used. It is provided by user input */
    private String listType;

    /**
     * Create a list command.
     *
     * @param argumentInput the command argument.
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException if the given argumentInput is null.
     */
    public ListCmd(String argumentInput) {
        super(CommandType.LIST, argumentInput);
    }

    /**
     * Execute the list command. This prints a header and then
     * all books in the library in the specified listType format.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if the book data passed is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, CommandFactory.NULL_LIB_MSG);
        List<BookEntry> bookList = data.getBookData();

        headerPrinter(bookList);
        listingPrinter(bookList);
    }

    /**
     * Parses the given user input to create a listing type from the command argument.
     *
     * A command argument, "long", "short" or a blank argument in this case, is expected.
     *
     * @param argumentInput the command argument.
     * @return true if the argument input is a valid command argument.
     *         Otherwise returns false, indicating invalid input.
     * @throws NullPointerException if the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        super.parseArguments(argumentInput);

        switch (argumentInput) {
            case LONG_ARG:
            case SHORT_ARG:
                listType = argumentInput;
                break;
            case "":
                listType = SHORT_ARG;
        }
        return (argumentInput.equals(LONG_ARG) || argumentInput.equals(SHORT_ARG) || argumentInput.isBlank());
    }

    /**
     * Helper method used by the execute method.
     * Prints the header line for the listing based on the size of the book list.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     */
    private void headerPrinter(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        if (bookList.isEmpty()) {
            System.out.println("The library has no book entries.");
        } else {
            System.out.println(bookList.size() + " books in library:");
        }
    }

    /**
     * Helper method used by the execute method.
     * Prints the contents of the library based on the list type specified.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     * @throws IllegalStateException if listType somehow has a value that isn't a command argument option.
     */
    private void listingPrinter(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        if (listType.equals(SHORT_ARG)) {
            shortListingPrinter(bookList);
        } else if (listType.equals(LONG_ARG)) {
            longListingPrinter(bookList);
        } else {
            throw new IllegalStateException("The execute method has been called with an illegal or null list type.");
        }
    }

    /**
     * Helper method used by the execute method when the list command argument is "short".
     * Prints the title of all the books in the library.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     */
    private void shortListingPrinter(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        for (BookEntry book : bookList) {
            System.out.println(book.getTitle());
        }
    }

    /**
     * Helper method used by the execute method when the list command argument is "long".
     * Prints the full details of all the books in the library.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     */
    private void longListingPrinter(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        for (BookEntry book : bookList) {
            System.out.println(book.toString());
            System.out.println();
        }
    }
}

