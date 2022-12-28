import java.util.List;
import java.util.Objects;

/**
 * Search command that finds and displays all books containing a term.
 * The term is decided by a user given argument.
 */
public class SearchCmd extends LibraryCommand {

    /** Term, provided by user input, sought for in the library titles. */
    private String searchTerm;

    /**
     * Create a search command.
     *
     * @param argumentInput the term the user is searching for.
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException if the given argumentInput is null.
     */
    public SearchCmd(String argumentInput) {
        super(CommandType.SEARCH, argumentInput);
    }

    /**
     * Execute the search command. This finds and prints
     * the title of all books containing the search term.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if the book data passed is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, CommandFactory.NULL_LIB_MSG);
        List<BookEntry> bookList = data.getBookData();

        searchBookList(bookList);
    }

    /**
     * Parses the given user input to create a search term.
     * An argument without spaces is expected.
     *
     * @param argumentInput the search term.
     * @return false if the value parse into searchTerm's is blank or contains a space, indicating invalid input.
     *         Otherwise returns true.
     * @throws NullPointerException if the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        super.parseArguments(argumentInput);
        searchTerm = argumentInput;

        return !(searchTerm.contains(" ") || searchTerm.isBlank());
    }

    /**
     * Helper method used by the execute method.
     * Builds and prints all book titles containing the search term.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     */
    private void searchBookList(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        StringBuilder foundBooks = new StringBuilder();
        String upperSearchTerm = searchTerm.toUpperCase();
        for (BookEntry book : bookList) {
            if (book.getTitle().toUpperCase().contains(upperSearchTerm)) {
                foundBooks.append(book.getTitle()).append('\n');
            }
        }
        printFoundBooks(foundBooks.toString());
    }

    /**
     * Helper method used by the searchBookList method.
     * Prints the list of books found to the console or,
     * if no books were found, a message to that effect.
     *
     * @param foundBooks the string of found books.
     * @throws NullPointerException if the string of found books is null.
     */
    private void printFoundBooks(String foundBooks) {
        Objects.requireNonNull(foundBooks, "The string of found books cannot be null.");

        if (foundBooks.isBlank()) {
            System.out.println("No hits found for search term: " + searchTerm);
        } else {
            System.out.println(foundBooks);
        }
    }
}
