package org.bibblan.bookcatalog;


import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Data
public class ItemCollection {
    private Map<String, Item> itemMap = new HashMap<>();

    public void addItem(Item item) {
        itemMap.put(item.getTitle(), item);
    }

    public void addItems(Collection<Item> item) {
        for (Item i : item) {
            itemMap.put(i.getTitle(), i);
        }
    }

    public Item getItem(String key) {
        return itemMap.get(key);
    }
}
