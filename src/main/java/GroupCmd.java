import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Group command used to display the contents of a library sorted and grouped
 * The grouping is decided by a user given parameter, either TITLE or AUTHOR for now.
 */
public class GroupCmd extends LibraryCommand {
    /** Command argument for a grouping by title */
    private final String TITLE_ARG = "TITLE";
    /** Command argument for a grouping by author */
    private final String AUTHOR_ARG = "AUTHOR";

    /** String before the name of a grouping category is printed */
    private final String HEAD_BUFFER = "## ";
    /** Figure that converts an index position to its equivalent letter and vice versa */
    private final int CHAR_SHIFT = 65;
    /**
     * The number of groupings when the title command argument is active,
     *  one for each letter of the alphabet with an extra one added for digits
     */
    private final int NUM_TITLE_GROUPINGS = 27;

    /** The type of grouping to be used. It is provided by user input */
    private String groupType;

    /**
     * Create a group command.
     *
     * @param argumentInput the command argument.
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException if the given argumentInput is null.
     */
    public GroupCmd(String argumentInput) {
        super(CommandType.GROUP, argumentInput);
    }

    /**
     * Execute the group command. This prints a header and then
     * all books in the library in the specified groupType groupinga.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if the book data passed is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, CommandFactory.NULL_LIB_MSG);
        List<BookEntry> bookList = data.getBookData();

        mainHeaderPrinter(bookList);
        printGroupings(bookList);
    }

    /**
     * Parses the given user input to create a grouping type from the command argument.
     *
     * A command argument, "TITLE" or "AUTHOR" in this case, is expected.
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
            case TITLE_ARG:
            case AUTHOR_ARG:
                groupType = argumentInput;
        }
        return (argumentInput.equals(TITLE_ARG) || argumentInput.equals(AUTHOR_ARG));
    }

    /**
     * Helper method used by the execute method.
     * Prints the header line for the listing based on the size of the book list and the active group type.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     */
    private void mainHeaderPrinter(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        if (bookList.isEmpty()) {
            System.out.println("The library has no book entries.");
        } else {
            System.out.println("Grouped data by " + groupType);
        }
    }

    /**
     * Helper method used by the execute method.
     * Prints the contents of the library based on the group type specified.
     *
     * @param bookList list of book entries to be searched through.
     * @throws NullPointerException if the list of book entries is null.
     * @throws IllegalStateException if groupType somehow has a value that isn't a command argument option.
     */
    private void printGroupings(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        if (groupType.equals(TITLE_ARG)) {
            printTitleGroupings(getTitleGroupings(bookList));
        } else if (groupType.equals(AUTHOR_ARG)) {
            printAuthorGroupings(bookList, getAuthorGroupingNames(bookList));
        } else {
            throw new IllegalStateException("The execute method has been called with an illegal or null group type.");
        }
    }

    /**
     * Helper method used by the printGroupings method when the list command argument is "TITLE".
     * Prints all the books in the library grouped by their titles.
     *
     * @param titleGroupings array of lists of book entries' titles.
     *                       Sorted so each title is in an array position matching its first letter.
     * @throws NullPointerException if the array of lists of book entries's titles is null.
     */
    private void printTitleGroupings(ArrayList<String>[] titleGroupings) {
        Objects.requireNonNull(titleGroupings, "The title groupings array cannot be null");

        int groupingsIndex = 0;
        for (ArrayList<String> titleGrouping : titleGroupings) {
            if (!titleGrouping.isEmpty()) {//means group headers are only printed if entries in that group were found.
                System.out.println(HEAD_BUFFER + titleHeader(groupingsIndex));
                for (String book : titleGrouping) {
                    System.out.println("\t" + book);
                }
            }
            groupingsIndex++;
        }
    }

    /**
     * Helper method used by the printTitleGroupings method.
     * Forms the title of the grouping based on the current position within the array.
     *
     * @param index the index of the current position within the array
     * @return the rest of the header of the title grouping after the buffer.
     */
    private String titleHeader(int index) {
        Objects.requireNonNull(index, "The index from iterating through the array cannot be null.");

        if (index == NUM_TITLE_GROUPINGS - 1) {//-1 is to convert to index
            return "[0-9]";//uses 27th array slot for books beginning with numbers
        }
        return String.valueOf(((char) (index + CHAR_SHIFT)));
    }

    /**
     * Helper method used by the printGroupings method when the list command argument is "TITLE".
     * Forms a sorted array via radix method where each element is an arrayList
     * Each list's index represents the group of the title in it, done by alphabetical order.
     *
     * @param bookList list of book entries to be searched through.
     * @return a radix-sorted array of lists of book titles.
     */
    private ArrayList<String>[] getTitleGroupings(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        ArrayList<String>[] titleGroupings = new ArrayList[NUM_TITLE_GROUPINGS];
        Arrays.setAll(titleGroupings, titleGrouping -> new ArrayList<String>()); //initialises all arrayLists in array.
        for (BookEntry book : bookList) {
                titleGroupings[getLetterIndex(book)].add(book.getTitle());
        }

        return titleGroupings;
    }

    /**
     * Helper method used by the getTitleGroupings method.
     * Generates an index that maps a book entry to its appropriate arrayList in the larger array.
     *
     * @param book the book entry to be sorted into an alphabetical group.
     * @return the index in the array of the arrayList the book should be added to.
     */
    private int getLetterIndex(BookEntry book) {
        Objects.requireNonNull(book, "The book entry cannot be null.");

        if (Character.isDigit(book.getTitle().charAt(0))){
            return NUM_TITLE_GROUPINGS - 1; //-1 is to convert to index
        }
        return Character.toUpperCase(book.getTitle().charAt(0)) - CHAR_SHIFT;
    }

    /**
     * Helper method used by the printGroupings method when the list command argument is "AUTHOR".
     * Prints all the books in the library grouped by their authors.
     *
     * @param bookList list of book entries to be searched through.
     * @param authors alphabetically sorted list of authors who have books in the library.
     * @throws NullPointerException if the list of book entries is null or
     *                              if the list of authors is null.
     */
    private void printAuthorGroupings(List<BookEntry> bookList, ArrayList<String> authors) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);
        Objects.requireNonNull(authors, "The author groupings array list cannot be null");

        for (String author : authors) {
            System.out.println(HEAD_BUFFER + author);
            for (BookEntry book : bookList) {
                for (String bookAuthor : book.getAuthors()) {
                    if (bookAuthor.equals(author)) {
                        System.out.println("\t" + book.getTitle());
                    }
                }
            }
        }
    }

    /**
     * Helper method used by the printGroupings method when the list command argument is "AUTHOR".
     * Collates a list of all authors, with no double entries, whose books are in the library book list.
     *
     * @param bookList list of book entries to be searched through.
     * @return a sorted list of all authors whose books are in the library.
     * @throws NullPointerException if the list of book entries is null.
     */
    private ArrayList<String> getAuthorGroupingNames(List<BookEntry> bookList) {
        Objects.requireNonNull(bookList, CommandFactory.NULL_BOOK_LIST_MSG);

        ArrayList<String> authors = new ArrayList<>();
        for (BookEntry book : bookList) {
            for (String author : book.getAuthors()) {
                if (!authors.contains(author)) {
                    authors.add(author);
                }
            }
        }
        Collections.sort(authors);

        return authors;
    }
}
