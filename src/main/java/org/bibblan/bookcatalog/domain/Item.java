package org.bibblan.bookcatalog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item {

    private String title;

    private String genre;

    private Author author;

    private String Publisher;



    //private ArrayList<Review> reviews = new ArrayList<Review>();

    public abstract String getArticleType();


}
