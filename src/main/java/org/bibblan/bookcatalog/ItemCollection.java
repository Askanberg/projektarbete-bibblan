package org.bibblan.bookcatalog;


import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.util.*;

@Data
public class ItemCollection {
    private Map<String, ItemCount> itemMap = new HashMap<>();

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        if (itemMap.containsKey(item.getTitle())) {
            itemMap.get(item.getTitle()).addCopy(item);
        } else {
            itemMap.put(item.getTitle(), new ItemCount(item, 1));
        }
    }

    public void addItems(Collection<Item> item) {
        for (Item i : item) {
            if (itemMap.containsKey(i.getTitle())) {
                itemMap.get(i.getTitle()).addCopy(i);
            } else {
                itemMap.put(i.getTitle(), new ItemCount(i, 1));
            }
        }
    }

    public ItemCount getItemCount(String key) {
        return itemMap.get(key);
    }

    @Data
    public static class ItemCount {
        private String title;
        private List<Item> totalCopies = new ArrayList<>();
        private int count;

        public ItemCount(Item item, int count) {
            this.title = item.getTitle();
            this.count = count;
            totalCopies.add(item);
        }

        public void addCopy(Item item) {
            totalCopies.add(item);
            this.count++;
        }
    }
}
