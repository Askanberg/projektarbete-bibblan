package org.bibblan.bookcatalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private String name;

    private ArrayList<Book> books = new ArrayList<>();
}
