package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EBook extends Item{

    private String fileFormat;

    private String url;

    @Override
    public String getArticleType() {
        return "E-Book";
    }
}
