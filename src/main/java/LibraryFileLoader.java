import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 
 * Class responsible for loading
 * book data from file.
 */
public class LibraryFileLoader {
    /** The header line in the csv files */
    private static final String FILE_HEADER = "title,authors,average_rating,ISBN,# num_pages";
    /** Delimiter between book entry variables in a file line string */
    private static final String BOOK_LINE_DELIMITER = ",";

    /**
     * Contains all lines read from a book data file using
     * the loadFileContent method.
     * 
     * This field can be null if loadFileContent was not called
     * for a valid Path yet.
     * 
     * NOTE: Individual line entries do not include line breaks at the 
     * end of each line.
     */
    private List<String> fileContent;

    /** Create a new loader. No file content has been loaded yet. */
    public LibraryFileLoader() { 
        fileContent = null;
    }

    /**
     * Load all lines from the specified book data file and
     * save them for later parsing with the parseFileContent method.
     * 
     * This method has to be called before the parseFileContent method
     * can be executed successfully.
     * 
     * @param fileName file path with book data
     * @return true if book data could be loaded successfully, false otherwise
     * @throws NullPointerException if the given file name is null
     */
    public boolean loadFileContent(Path fileName) {
        Objects.requireNonNull(fileName, "Given filename must not be null.");
        boolean success = false;

        try {
            fileContent = Files.readAllLines(fileName);
            success = true;
        } catch (IOException | SecurityException e) {
            System.err.println("ERROR: Reading file content failed: " + e);
        }

        return success;
    }

    /**
     * Has file content been loaded already?
     * @return true if file content has been loaded already.
     */
    public boolean contentLoaded() {
        return fileContent != null;
    }

    /**
     * Parse file content loaded previously with the loadFileContent method.
     * If there is no content to be parsed it first prints an error message.
     * 
     * @return books parsed from the previously loaded book data or an empty list
     * if no book data has been loaded yet.
     */
    public List<BookEntry> parseFileContent() {
        List<BookEntry> newBooks = new ArrayList<BookEntry>();

        if (contentLoaded()) {
            addBooksToList(newBooks);
        } else {
            System.err.println("ERROR: No content loaded before parsing.");
        }

        return newBooks;
    }

    /**
     * Helper method used by parseFileContent.
     * Converts and appends the previously loaded file contents to a list of book entries.
     *
     * The try-catch block means any lines in the file that don't fit the BookEntry format are skipped.
     * While not required it was added anyway since its inclusion shouldn't make any tests fail.
     *
     * @param newBooks the list of books that the converted file contents are to be appended to.
     * @throws NullPointerException if the list of books to be appended to is null.
     */
    private void addBooksToList(List<BookEntry> newBooks) {
        Objects.requireNonNull(newBooks, "The list of new books to be loaded cannot be null.");

        int currentLineNum = 0;
        for (String fileLine : fileContent) {
            currentLineNum++;
            try {
                bookAdder(fileLine, newBooks);
            } catch (IllegalArgumentException err) {
                System.err.println("Potential book on line " + (currentLineNum) + " could not be added because of a " + err);
            }
        }
    }

    /**
     * Helper method used by addBooksToList.
     * Attempts to create a BookEntry instance from a file line then appends it to a list of book entries.
     *
     * It recognises blank and header lines, only attempting to execute the book adding sequence
     * if a line isn't one so such lines don't cause unnecessary error messages.
     *
     * @param fileLine a string from the file being read to be converted to a book entry.
     * @param newBooks the list of books that the converted file contents are to be appended to.
     * @throws NullPointerException if the list of books to be appended to is null or
     *                              if the file line passed to the method is null.
     */
    private void bookAdder(String fileLine, List<BookEntry> newBooks) {
        Objects.requireNonNull(fileLine, "The line to be converted to a book entry cannot be null.");
        Objects.requireNonNull(newBooks, "The list of new books to be loaded cannot be null.");

        if (!(fileLine.isBlank() || fileLine.equals(FILE_HEADER))) {//means nothing's done for the title and blank lines
            String[] bookVars = fileLine.split(BOOK_LINE_DELIMITER);
            newBooks.add(new BookEntry(bookVars));
            }
        }
}
