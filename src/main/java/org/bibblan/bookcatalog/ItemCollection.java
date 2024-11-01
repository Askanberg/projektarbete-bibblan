package org.bibblan.bookcatalog;


import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class ItemCollection {
    private Map<String, List<Item>> itemMap = new LinkedHashMap<>();

    private final int SEARCH_DIFF_THRESHOLD = 1;

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        if (!itemMap.containsKey(item.getTitle())) {
            itemMap.put(item.getTitle(), new ArrayList<>());
        }
        itemMap.get(item.getTitle()).add(item);
    }

    public void addItems(Collection<Item> item) {
        for (Item i : item) {
            if (!itemMap.containsKey(i.getTitle())) {
                itemMap.put(i.getTitle(), new ArrayList<>());
            }
            itemMap.get(i.getTitle()).add(i);
        }
    }

    public List<Item> getItemCopies(String key) {
        if (itemMap.containsKey(key)) {
            return itemMap.get(key);
        } else {
            throw new NoSuchElementException("The item did not exist in the collection");
        }
    }

    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>();
        for (List<Item> list : itemMap.values()) {
            allItems.addAll(list);
        }
        return allItems;
    }

    //*************************************
    //************** Sorting **************
    //*************************************
    public List<Item> sortItems() {
        return sortItems("title");
    }

    public List<Item> sortItems(String sortingBy) {
        if (itemMap.isEmpty()) {
            throw new IllegalArgumentException("The list is empty and cannot be sorted");
        }

        List<Item> allItems = getAllItems();
        if (allItems.size() < 2) {
            return allItems;
        }
        Comparator<Item> itemComparator = createComparator(sortingBy);
        allItems.sort(itemComparator);
        return allItems;
    }

    private Comparator<Item> createComparator(String sortingBy) {
        switch (sortingBy.toLowerCase()) {
            case "title":
                return Comparator.comparing(Item::getTitle);
            case "isbn":
                return Comparator.comparing(item ->
                                (item instanceof Book || item instanceof Reference)
                                        ? Integer.parseInt(((Book) item).getIsbn())
                                        : null,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );
            case "author":
                return Comparator.comparing(item -> {
                    Author author = item.getAuthor();
                    return author.getName();
                });
            default:
                throw new IllegalArgumentException("Error: Trying to sort by invalid columnName");
        }
    }


    //***************************************
    //************** Searching **************
    //***************************************
    public List<Item> searchByTitle(String titleQuery) {
        List<Item> results = new ArrayList<>();
        for (Item item : getAllItems()) {
            int distance = calculateDistance(item.getTitle().toLowerCase(), titleQuery.toLowerCase());
            if (distance <= SEARCH_DIFF_THRESHOLD || item.getTitle().toLowerCase().contains(titleQuery.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> searchByIsbn(String isbnQuery) {
        List<Item> results = new ArrayList<>();
        for (Item item : getAllItems()) {
            String isbn = "";
            if (item instanceof Book) {
                isbn = ((Book) item).getIsbn();
            } else if (item instanceof Reference) {
                isbn = ((Reference) item).getIsbn();
            }
            int distance = calculateDistance(isbn, isbnQuery);
            if (distance <= SEARCH_DIFF_THRESHOLD || isbn.contains(isbnQuery)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> searchByAuthor(String authorQuery) {
        List<Item> results = new ArrayList<>();
        for (Item item : getAllItems()) {
            String authorName = item.getAuthor().getName();
            int distance = calculateDistance(authorName.toLowerCase(), authorQuery.toLowerCase());
            if (distance <= SEARCH_DIFF_THRESHOLD || authorName.toLowerCase().contains(authorQuery.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    private int calculateDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
