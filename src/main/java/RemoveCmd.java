import java.util.List;
import java.util.Objects;

/**
 * Remove command that finds and removes all books matching a term,
 * either by author or book title.
 *
 * The term and type of search is decided by user given input.
 */
public class RemoveCmd extends LibraryCommand {
    /** Delimiter between type of remove command and the parameter for the term to be searched for */
    private static final String INPUT_ARGUMENT_DELIMITER = " ";
    /** Command argument for a removal by title */
    private final String TITLE_ARG = "TITLE";
    /** Command argument for a removal by author */
    private final String AUTHOR_ARG = "AUTHOR";

    /** What the search for terms to be removed will be by. It is provided by user input */
    private String removeType;
    /** Term, provided by user input, sought for in the book titles or authors as the case may be. */
    private String removeTerm;

    /**
     * Create a remove command.
     *
     * @param argumentInput the command argument and parameter.
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException if the given argumentInput is null.
     */
    public RemoveCmd(String argumentInput) {
        super(CommandType.REMOVE, argumentInput);
    }


    /**
     * Execute remove command. This finds and removes
     * all book entries fitting the required criteria.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if the book data passed is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, CommandFactory.NULL_LIB_MSG);
        List<BookEntry> bookList = data.getBookData();

        removeBooks(bookList);
    }

    /**
     * Parses the given user input to create a remove type and term
     * from the command argument and parameter respectively.
     *
     * A command argument followed by a space then a parameter is expected.
     *
     * @param argumentInput the command argument and parameter.
     * @return true if both helper functions indicate valid input.
     *         Otherwise returns false, indicating invalid input.
     * @throws NullPointerException if the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        super.parseArguments(argumentInput);

        return parseType(argumentInput) && parseTerm(argumentInput);
    }

    /**
     * Helper method used by parseArguments method.
     * Parses the given user input to assign a remove type.
     *
     * @param argumentInput argument input for this command
     * @return true if the value parsed into removeType matches that of the title or author command arguments.
     *         Otherwise returns false, indicating invalid input.
     * @throws NullPointerException if the given argumentInput is null.
     */
    private boolean parseType(String argumentInput) {
        super.parseArguments(argumentInput);

        if (argumentInput.startsWith(TITLE_ARG)) {
            removeType = TITLE_ARG;
        } else if (argumentInput.startsWith(AUTHOR_ARG)) {
            removeType = AUTHOR_ARG;
        }

        return (removeType.equals(TITLE_ARG) || removeType.equals(AUTHOR_ARG));
    }

    /**
     * Helper method used by parseArguments method.
     * Parses the given user input to assign a remove term.
     *
     * @param argumentInput argument input for this command
     * @return true if the value parsed into removeTerm isn't blank and there was a delimiter found in the input.
     *         Otherwise returns false, indicating invalid input.
     * @throws NullPointerException if the given argumentInput is null.
     */
    private boolean parseTerm(String argumentInput) {
        super.parseArguments(argumentInput);

        int firstSpaceIdx = argumentInput.indexOf(INPUT_ARGUMENT_DELIMITER);
        //+1 means everything after the space, not including the space is taken as the remove term.
        removeTerm = argumentInput.substring(firstSpaceIdx + 1);

        return !removeTerm.isBlank() && firstSpaceIdx > 0; //if delimiter isn't found indexOf will return -1.
    }

    /**
     * Helper method used by the execute method.
     * Removes book entries based on the values parsed into the remove term and type.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the given list of book entries is null.
     * @throws IllegalStateException if removeType somehow has a value that isn't a command argument option.
     */
    private void removeBooks(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        if (removeType.equals(TITLE_ARG)) {
            removeByTitle(bookList);
        } else if (removeType.equals(AUTHOR_ARG)) {
            removeByAuthor(bookList);
        } else {
            throw new IllegalStateException("The execute method has been called with an illegal or null remove type.");
        }
    }

    /**
     * Helper method used by removeBooks when the removeType is TITLE.
     * Removes book entry with a title matching the remove term.
     *
     * @param bookList list of book entries to be searched through
     * @throws NullPointerException if the given list of book entries is null.
     */
    private void removeByTitle(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        for (BookEntry book : bookList) {
            if (book.getTitle().equals(removeTerm)) {
                bookList.remove(book);
                System.out.println(removeTerm + ": removed successfully.");
                return;
            }
        }
        System.out.println(removeTerm + ": not found."); //prints this line if no book is found as for loop isn't exhausted before breaking via return
    }

    /**
     * Helper method used by removeBooks when the removeType is AUTHOR.
     * Removes all book entries that have one of their authors matching the remove term.
     *
     * @param bookList list of book entries to be searched through
     * @throws NullPointerException if the given list of book entries is null.
     */
    private void removeByAuthor(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        int counter = 0;
        for (BookEntry book : bookList) {
            for (String author: book.getAuthors()) {
                if (author.equals(removeTerm)) {
                    bookList.remove(book);
                    counter++;
                }
            }
        }
        System.out.println(counter + " books removed for author: " + removeTerm);
    }
}
