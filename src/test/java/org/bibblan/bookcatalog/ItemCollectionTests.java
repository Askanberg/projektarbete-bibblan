package org.bibblan.bookcatalog;


import org.bibblan.bookcatalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTests {
    private ItemCollection itemCollection;
    private ItemCollection searchCollection;


    @BeforeEach
    public void initializeGeneralTestUtilities() {
        itemCollection = new ItemCollection();
    }
    @Test
    void testAddSingleItem() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);

        itemCollection.addItem(book);

        assertEquals(book, itemCollection.getItemCopies("Educated").get(0));
    }
    @Test
    void testAddMultipleItems() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());
        List<Item> items = itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        itemCollection.addItems(items);

        for (Item i : items) {
            assertTrue(itemCollection.getItemMap().containsKey(i.getTitle()));
        }
    }
    @Test
    void testThatAddNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> itemCollection.addItem(null));
    }
    @Test
    void testThatAddingTwoOfTheSameBooksIncreasesCount() {
        Book bookA = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);

        itemCollection.addItem(bookA);
        int expectedAmountOfCopies = itemCollection.getItemMap().get(bookA.getTitle()).size();

        itemCollection.addItem(bookA);

        assertEquals(expectedAmountOfCopies + 1, itemCollection.getItemCopies(bookA.getTitle()).size());
    }
    @Test
    void testThatAddingTwoOfTheSameBooksButDifferentTypesIncreasesCount() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        EBook eBook = new EBook("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "http://example.com/educated", "EPUB");

        itemCollection.addItem(book);
        int expectedAmountOfCopies = itemCollection.getItemMap().get(book.getTitle()).size();

        itemCollection.addItem(eBook);

        assertEquals(expectedAmountOfCopies + 1, itemCollection.getItemCopies(book.getTitle()).size());
    }
    @Test
    void testThatGetCopiesReturnsBothBooksAfterAddingTwoOfTheSameBooksButDifferentTypes() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        EBook eBook = new EBook("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "http://example.com/educated", "EPUB");

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(book);
        expectedItems.add(eBook);

        itemCollection.addItems(expectedItems);

        assertEquals(expectedItems, itemCollection.getItemCopies("Educated"));
    }
    @Test
    void testAddingDifferentItemTypes() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());
        List<Item> ebooks =  itemFileReader.readItemsFromCsv("src/test/resources/testEBooks.csv");
        List<Item> books =  itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        EBook ebook = (EBook) ebooks.get(0);
        Book book = (Book) books.get(0);

        itemCollection.addItems(ebooks);
        itemCollection.addItems(books);


        assertTrue(itemCollection.getItemMap().containsKey(ebook.getTitle()), "Ebook");
        assertTrue(itemCollection.getItemMap().containsKey(book.getTitle()), "Book");

    }

    //******************************************
    //************** Sorting Tests**************
    //******************************************
    @Test
    void testSortingListWithZeroElements() {
        assertThrows(IllegalArgumentException.class, () -> itemCollection.sortItems());
    }
    @Test
    void testTryingToSortListWithOneElement() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(book);

        itemCollection.addItem(book);

        assertEquals(expectedList, itemCollection.sortItems());
    }
    @Test
    void testThatGetAllItemsMethodReturnsAllItemsUnsorted() {
        Book bookA = new Book("aaa", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        Book bookB = new Book("bbb", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590503", CoverType.POCKET);

        List<Item> expectedList = new ArrayList<>();
        expectedList.add(bookB);
        expectedList.add(bookA);

        itemCollection.addItem(bookB);
        itemCollection.addItem(bookA);
        assertArrayEquals(expectedList.toArray(), itemCollection.getAllItems().toArray());
    }
    @Test
    void testSortingListWithTwoUnsortedElementsByTitle() {
        Book bookA = new Book("aaa", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        Book bookB = new Book("bbb", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);

        List<Item> expectedList = new ArrayList<>();
        expectedList.add(bookA);
        expectedList.add(bookB);

        itemCollection.addItem(bookB);
        itemCollection.addItem(bookA);
        assertEquals(expectedList, itemCollection.sortItems("title"));
    }
    @Test
    void testSortingListWithMultipleUnsortedElementsByTitle() {
        Book bookA = new Book("TitleA", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookB = new Book("TitleB", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookC = new Book("TitleC", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookD = new Book("TitleD", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookE = new Book("TitleE", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);

        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookB, bookC, bookD, bookE));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, bookD, bookE, bookA, bookC));
        itemCollection.addItems(unsortedList);
        assertEquals(expectedList, itemCollection.sortItems("title"));
    }
    @Test
    void testSortingListWithSomeIdenticalElementsByTitle() {
        Book bookA = new Book("TitleA", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookB = new Book("TitleB", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookC = new Book("TitleC", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);
        Book bookD = new Book("TitleD", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "158", CoverType.POCKET);

        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookA, bookB, bookC, bookC, bookD));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, bookD, bookC, bookA, bookC, bookA));
        itemCollection.addItems(unsortedList);
        assertEquals(expectedList, itemCollection.sortItems("title"));
    }
    @Test
    void testSortingListWithMultipleUnsortedElementsByIsbn() {
        Book bookA = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookB = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "222", CoverType.POCKET);
        Book bookC = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "333", CoverType.POCKET);
        Book bookD = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "444", CoverType.POCKET);
        Book bookE = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "555", CoverType.POCKET);
        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookB, bookC, bookD, bookE));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, bookD, bookE, bookA, bookC));
        itemCollection.addItems(unsortedList);

        assertEquals(expectedList, itemCollection.sortItems("isbn"));
    }
    @Test
    void testThatSortingItemsWithoutIsbnIsPutLastIfSortingByIsbn() {
        Book bookA = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookB = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "222", CoverType.POCKET);
        Book bookC = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "333", CoverType.POCKET);
        Book bookD = new Book("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "444", CoverType.POCKET);
        EBook eBook = new EBook("Title", new Author("Author", new ArrayList<>()), "Genre", "Publisher", "http://example.com/example", "FileFormat");
        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookB, bookC, bookD, eBook));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, bookD, eBook, bookA, bookC));
        itemCollection.addItems(unsortedList);

        assertEquals(expectedList, itemCollection.sortItems("isbn"));
    }
    @Test
    void testSortingListWithMultipleUnsortedElementsByAuthor() {
        Book bookA = new Book("Title", new Author("AuthorA", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookB = new Book("Title", new Author("AuthorB", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookC = new Book("Title", new Author("AuthorC", new ArrayList<>()), "Genre", "Publisher", "222", CoverType.POCKET);
        Book bookD = new Book("Title", new Author("AuthorD", new ArrayList<>()), "Genre", "Publisher", "333", CoverType.POCKET);
        Book bookE = new Book("Title", new Author("AuthorE", new ArrayList<>()), "Genre", "Publisher", "444", CoverType.POCKET);
        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookB, bookC, bookD, bookE));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, bookD, bookE, bookA, bookC));
        itemCollection.addItems(unsortedList);

        assertEquals(expectedList, itemCollection.sortItems("author"));
    }
    @Test
    void testSortingListWithMultipleDifferentItemTypesByTitleAndAuthor() {
        Book bookA = new Book("BookA", new Author("AuthorA", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookB = new Book("BookB", new Author("AuthorB", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        EBook eBookA = new EBook("EbookA", new Author("AuthorC", new ArrayList<>()), "Genre", "Publisher", "http://example.com/example", "FileFormat");
        EBook eBookB = new EBook("EbookB", new Author("AuthorD", new ArrayList<>()), "Genre", "Publisher", "http://example.com/example", "FileFormat");
        Reference referenceA = new Reference("ReferenceA", new Author("AuthorE", new ArrayList<>()), "Genre", "Publisher", "111");
        Reference referenceB = new Reference("ReferenceB", new Author("AuthorF", new ArrayList<>()), "Genre", "Publisher", "111");
        List<Item> expectedList = new ArrayList<>(Arrays.asList(bookA, bookB, eBookA, eBookB, referenceA, referenceB));

        List<Item> unsortedList = new ArrayList<>(Arrays.asList(bookB, referenceB, eBookA, bookA, referenceA, eBookB));
        itemCollection.addItems(unsortedList);
        assertEquals(expectedList, itemCollection.sortItems("title"));
        assertEquals(expectedList, itemCollection.sortItems("author"));
    }
    @Test
    void testThatCallingSortItemsWithInvalidSortStringThrowsException() {
        Book bookA = new Book("BookA", new Author("AuthorA", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        Book bookB = new Book("BookB", new Author("AuthorB", new ArrayList<>()), "Genre", "Publisher", "111", CoverType.POCKET);
        itemCollection.addItems(Arrays.asList(bookA, bookB));

        assertThrows(IllegalArgumentException.class, () -> itemCollection.sortItems("Invalid"));
    }

    //********************************************
    //************** Searching Tests**************
    //********************************************
    @BeforeEach
    public void InitializeSearchUtilities() throws IOException {
        searchCollection = new ItemCollection();
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());
        searchCollection.addItems(itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv"));
        searchCollection.addItems(itemFileReader.readItemsFromCsv("src/test/resources/testEBooks.csv"));
        searchCollection.addItems(itemFileReader.readItemsFromCsv("src/test/resources/testReferences.csv"));
    }
    @Test
    void testThatSearchByTitleReturnsListOfItemsWithExactMatch() throws IOException {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByTitle("educated"));
    }
    @Test
    void testThatSearchByTitleReturnsListOfItemsWithDistanceAtThreshold() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByTitle("educates"));
    }
    @Test
    void testThatSearchByTitleReturnsEmptyListWithDistanceAtOneOverThreshold() {
        assertEquals(new ArrayList<>(), searchCollection.searchByTitle("educatre"));
    }
    @Test
    void testThatSearchByTitleReturnsListOfItemIfQueryPartOfTitle() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByTitle("Edu"));
    }

    @Test
    void testThatSearchByIsbnReturnsListOfItemsWithExactMatch() throws IOException {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByIsbn("9780399590504"));
    }
    @Test
    void testThatSearchByIsbnReturnsListOfItemsWithDistanceAtThreshold() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByIsbn("9780399590502"));
    }
    @Test
    void testThatSearchByIsbnReturnsEmptyListWithDistanceAtOneOverThreshold() {
        assertEquals(new ArrayList<>(), searchCollection.searchByIsbn("9780399590542"));
    }
    @Test
    void testThatSearchByIsbnReturnsListOfItemIfQueryPartOfIsbn() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByIsbn("978039"));
    }

    @Test
    void testThatSearchByAuthorReturnsListOfItemsWithExactMatch() throws IOException {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByAuthor("tara westover"));
    }
    @Test
    void testThatSearchByAuthorReturnsListOfItemsWithDistanceAtThreshold() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByAuthor("tara westovet"));
    }
    @Test
    void testThatSearchByAuthorReturnsEmptyListWithDistanceAtOneOverThreshold() {
        assertEquals(new ArrayList<>(), searchCollection.searchByAuthor("tara westodae"));
    }
    @Test
    void testThatSearchByAuthorReturnsListOfItemIfQueryPartOfAuthor() {
        List<Item> expectedList = searchCollection.getItemCopies("Educated");
        assertEquals(expectedList, searchCollection.searchByAuthor("Tar"));
    }

    @Test
    void testThatCalculateDistanceGivesDistanceZeroForIdenticalStrings() {
        assertEquals(0, searchCollection.calculateDistance("Test", "Test"));
    }
    @Test
    void testThatCalculateDistanceGivesDistance1ForStringsThatDifferOneLetter() {
        assertEquals(1, searchCollection.calculateDistance("Test", "Teft"));
    }
    @Test
    void testThatCalculateDistanceGivesDistance1ForStringThatIsOneLetterShorter() {
        assertEquals(1, searchCollection.calculateDistance("Test", "Tes"));
    }
    @Test
    void testThatCalculateDistanceGivesDistanceOfStringSizeWhenComparingEmptyString() {
        assertEquals(4, searchCollection.calculateDistance("Test", ""));
    }
    @Test
    void testThatCalculateDistanceGivesDistanceOfStringSizeWhenComparingCompletelyDifferentStrings() {
        assertEquals(4, searchCollection.calculateDistance("abcd", "efgh"));
    }


}
