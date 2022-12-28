import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable class encapsulating data for a single book entry.
 */
public class BookEntry {
    /**
     * Last part of each error message passed when a field is passed as null
     * when creating an instance of the Book entry class
     */
    private final String NULL_ERROR = " cannot be a null field.";

    /** Delimiter between authors names in a string */
    private static final String AUTHOR_LINE_DELIMITER = "-";

    /**
     * The following constants are for when the string array construction option is used
     * to create a new instance of a BookEntry
     * The integers indicate the index position of the corresponding instance variable
     */
    private static final int TITLE_INDEX = 0;
    private static final int AUTHORS_INDEX = 1;
    private static final int RATING_INDEX = 2;
    private static final int ISBN_INDEX = 3;
    private static final int PAGES_INDEX = 4;
    /** The number of instance variables in a book entry */
    private static final int NUM_OF_VARIABLES = 5;

    /** The minimum allowed valued for a book rating */
    private static final int MIN_RATING = 0;
    /** The maximum allowed valued for a book rating */
    private static final int MAX_RATING = 5;

    /** The title of the book */
    private final String title;
    /** The authors of the book, where each author's name is stored as an element in a string array */
    private final String[] authors;
    /** The rating given to the book out of 5 */
    private final float rating;
    /** The 13-digit International Standard Book Number */
    private final String ISBN;
    /** The number of pages in the book */
    private final int pages;

    /**
     * Create a new book entry where each instance variable is entered as 5 separate values
     * The constructor also checks for the legality of the book entry created via its helper method.
     *
     * @param title the full name of the book.
     * @param authors a list of the authors listed as the book's writers.
     * @param rating the rating of the book, out of 5.
     * @param ISBN 13-digit International Standard Book Number of the book.
     * @param pages the number of pages in the book.
     * @throws NullPointerException if any parameters are left as null
     */
    public BookEntry(String title, String[] authors, float rating, String ISBN, int pages) {
        this.title = Objects.requireNonNull(title, "title" + NULL_ERROR);
        this.authors = Objects.requireNonNull(authors, "authors" + NULL_ERROR);
        this.rating = Objects.requireNonNull(rating, "rating" + NULL_ERROR);
        this.ISBN = Objects.requireNonNull(ISBN, "ISBN" + NULL_ERROR);
        this.pages = Objects.requireNonNull(pages, "pages" + NULL_ERROR);

        isLegal(rating, pages);
    }

    /**
     * Create a new book entry where each instance variable is entered as a string in a 5-element array
     * The constructor also checks for the legality of the book entry created via its helper method.
     *
     * @param bookVars a 5-element array c
     * @throws NullPointerException if any parameters i.e. elements in the string array are left as null
     *                              or if the array passed is, itself, null
     * @throws IllegalArgumentException if the string array passed doesn't have 5 elements
     */
    public BookEntry(String[] bookVars) {//future versions of program could find this overload useful
        Objects.requireNonNull(bookVars, "The array containing the books variable" + NULL_ERROR);
        if (bookVars.length != NUM_OF_VARIABLES) {
            throw new IllegalArgumentException("The array provided to create a new book with must have exactly " +
                    NUM_OF_VARIABLES + " elements");
        }

        this.title = Objects.requireNonNull(bookVars[TITLE_INDEX], "title" + NULL_ERROR);
        this.authors = Objects.requireNonNull(bookVars[AUTHORS_INDEX].split(AUTHOR_LINE_DELIMITER), "authors" + NULL_ERROR);
        this.rating = Objects.requireNonNull(Float.parseFloat(bookVars[RATING_INDEX]), "rating" + NULL_ERROR);
        this.ISBN = Objects.requireNonNull(bookVars[ISBN_INDEX], "ISBN" + NULL_ERROR);
        this.pages = Objects.requireNonNull(Integer.parseInt(bookVars[PAGES_INDEX]), "pages" + NULL_ERROR);

        isLegal(rating, pages);
    }

    /**
     * Helper method used by the class constructor to determine the legality of the values
     * assigned to the instance variables
     *
     * @param rating the rating, out of 5, given to the book
     * @param pages the number of pages in the book
     * @throws IllegalArgumentException if the rating or pages values are out of their permitted ranges
     */
    private static void isLegal(float rating, int pages) {
        if (!(rating >= MIN_RATING && rating <= MAX_RATING)) {
            throw new IllegalArgumentException("The rating must be between 0 and 5.");
        }
        if (pages < 0) {
            throw new IllegalArgumentException("The number of pages cannot be negative.");
        }
    }

    /**
     * Returns the title of the book.
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the list of authors of the book.
     * @return the list of authors of the book.
     */
    public String[] getAuthors() {
        return authors;
    }

    /**
     * Returns the rating of the book.
     * @return the rating of the book.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Returns the International Standard Book Number (ISBN) of the book.
     * @return the International Standard Book Number (ISBN) of the book.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Returns the number of pages in the book.
     * @return the number of pages in the book.
     */
    public int getPages() {
        return pages;
    }

    /**
     * Returns a String representation of the book entry.
     * @return String representation of the book's entry.
     */
    @Override
    public String toString() {
        StringBuilder authorsString = new StringBuilder(authors[0]);
        for (int i = 1; i < authors.length; i++) {
            authorsString.append(", ").append(authors[i]);
        }

        return title + '\n' +
                "by " + authorsString + '\n' +
                "Rating: " + String.format("%.2f", rating) + '\n' +
                "ISBN: " + ISBN + '\n' +
                pages + " pages";
    }

    /**
     * Test equality of the book entry state to the given
     * object instance.
     *
     * @param aBookEntry the instance to compare to.
     * @return true if object states are equal.
     */
    @Override
    public boolean equals(Object aBookEntry) {
        if (this == aBookEntry) {
            return true;
        }
        if (aBookEntry == null || getClass() != aBookEntry.getClass()) {
            return false;
        }
        BookEntry bookEntry = (BookEntry) aBookEntry;
        return Float.compare(bookEntry.getRating(), rating) == 0 &&
                pages == bookEntry.getPages() &&
                title.equals(bookEntry.getTitle()) &&
                Arrays.equals(authors, bookEntry.getAuthors()) &&
                ISBN.equals(bookEntry.getISBN());
    }

    /**
     * Generate a hash code, a number which is always the same for two equivalent instances of this class,
     * based on the current state of this object.
     *
     * @return this object's hashcode.
     */
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(title, rating, ISBN, pages);
        /**used to add the authors array state to the hashcode value in a way
         * that doesn't interfere with the value calculated in the first line by multiplying that value by 31*/
        hashCode = 31 * hashCode + Arrays.hashCode(authors);
        return hashCode;
    }
}
