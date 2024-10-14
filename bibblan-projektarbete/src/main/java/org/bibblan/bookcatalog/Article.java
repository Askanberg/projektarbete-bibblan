package org.bibblan.bookcatalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Article {

    private String title;

    private String publishedDate;

    private String Publisher;

    private ArrayList<Author> authors = new ArrayList<Author>();

    //private ArrayList<Review> reviews = new ArrayList<Review>();

    public abstract String getArticleType();


}
