package org.bibblan.bookcatalog.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@EqualsAndHashCode
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
