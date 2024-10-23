package org.bibblan.bookcatalog.domain;



import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Author {

    private String name;

    private List<Item> items;

    public Author(String name, List<Item> items) {
        this.name = name;
        this.items = Objects.requireNonNullElseGet(items, ArrayList::new);
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Author other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
