package org.bibblan.bookcatalog.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DomainTests {
    @Test
    void testThatAuthorsWithSameNameGetsSameHashCode() {
        Author authorA = new Author("Test", new ArrayList<>());
        Author authorB = new Author("Test", new ArrayList<>());

        assertEquals(authorA.hashCode(), authorB.hashCode());
    }
}
