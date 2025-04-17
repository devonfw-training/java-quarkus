package org.example.app.general.common.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SortOrderByTest {
    @Test
    void testGetDirectionDefaultsToAsc() {
        SortOrderBy sortOrderBy = new SortOrderBy();
        assertEquals(SortOrderDirection.ASC, sortOrderBy.getDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        SortOrderBy a = new SortOrderBy("status", SortOrderDirection.DESC);
        SortOrderBy b = new SortOrderBy("status", SortOrderDirection.DESC);
        SortOrderBy c = new SortOrderBy("priority", SortOrderDirection.ASC);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
    }

    @Test
    void testToStringFormat() {
        SortOrderBy orderBy = new SortOrderBy("priority", SortOrderDirection.ASC);
        assertEquals("priority ASC", orderBy.toString());
    }
}
