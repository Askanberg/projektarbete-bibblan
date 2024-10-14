package org.bibblan.bookcatalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EBook extends Article{

    private String fileFormat;

    private String url;

    @Override
    public String getArticleType() {
        return "E-Book";
    }
}
