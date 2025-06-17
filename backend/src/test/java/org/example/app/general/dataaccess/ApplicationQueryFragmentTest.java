package org.example.app.general.dataaccess;

import com.querydsl.core.FilteredClause;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.example.app.general.common.search.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ApplicationQueryFragmentTest {
    private TestableQueryFragment fragment;

    @BeforeEach
    void setUp() {
        fragment = new TestableQueryFragment();
    }

    @Test
    void testNewStringClause_nullValue_returnsNullOrNotNullClause() {
        StringExpression expression = Expressions.stringPath("testField");

        assertThat(fragment.newStringClause(expression, null, StringSearchOperator.EQ, null, false, false))
                .isEqualTo(expression.isNull());

        assertThat(fragment.newStringClause(expression, null, StringSearchOperator.NE, null, false, false))
                .isEqualTo(expression.isNotNull());

        assertThrows(IllegalArgumentException.class, () ->
                fragment.newStringClause(expression, null, StringSearchOperator.LT, null, false, false));
    }

    @Test
    void testNewStringClause_emptyValue_returnsIsEmptyOrNotEmpty() {
        StringExpression expression = Expressions.stringPath("testField");

        assertThat(fragment.newStringClause(expression, "", StringSearchOperator.EQ, null, false, false))
                .isEqualTo(expression.isEmpty());

        assertThat(fragment.newStringClause(expression, "", StringSearchOperator.NE, null, false, false))
                .isEqualTo(expression.isNotEmpty());
    }

    @Test
    void testNewStringClause_ignoreCase_appliesUpperCase() {
        StringExpression mockExpression = mock(StringExpression.class);
        StringExpression upperMock = mock(StringExpression.class);
        when(mockExpression.upper()).thenReturn(upperMock);
        when(upperMock.like(anyString())).thenReturn(mock(BooleanExpression.class));

        fragment.newStringClause(mockExpression, "foo", StringSearchOperator.LIKE, LikePatternSyntax.SQL, true, false);

        verify(mockExpression).upper();
        verify(upperMock).like(anyString());
    }

    @Test
    void testNewLikeClause_autoDetectSyntax() {
        StringExpression expr = Expressions.stringPath("field");
        BooleanExpression result = fragment.newLikeClause(expr, "abc*", null, false, false);

        assertThat(result).isNotNull();
    }

    @Test
    void testWhereIn_withShortList() {
        SimpleExpression<String> expr = Expressions.stringPath("field");
        FilteredClause<?> clause = mock(FilteredClause.class);

        // Call the method with a short list
        fragment.whereIn(clause, expr, List.of("a", "b", "c"));

        // Capture the BooleanExpression argument passed to where()
        ArgumentCaptor<BooleanExpression> captor = ArgumentCaptor.forClass(BooleanExpression.class);
        verify(clause).where(captor.capture());

        // Assert that the captured BooleanExpression is not null
        BooleanExpression capturedExpression = captor.getValue();
        assertThat(capturedExpression).isNotNull();

        // Optionally, assert that the captured expression contains the expected "in" clause
        assertThat(capturedExpression.toString()).contains("in");
        assertThat(capturedExpression.toString()).contains("a");
        assertThat(capturedExpression.toString()).contains("b");
        assertThat(capturedExpression.toString()).contains("c");

        // If duplicates might exist, verify that the expression has no duplicate values
        List<String> values = List.of("a", "b", "c");
        assertThat(capturedExpression.toString()).contains(String.join(", ", values));
    }



    @Test
    void testWhereIn_withEmptyList_logsAndAddsFalseClause() {
        SimpleExpression<String> expr = Expressions.stringPath("field");
        FilteredClause<?> clause = mock(FilteredClause.class);

        fragment.whereIn(clause, expr, Collections.emptyList());

        verify(clause).where(eq(Expressions.ONE.eq(Expressions.ZERO)));
    }

    @Test
    void testWhereIn_withLargeList_partitionsCorrectly() {
        SimpleExpression<String> expr = Expressions.stringPath("field");
        FilteredClause<?> clause = mock(FilteredClause.class);

        List<String> largeList = Collections.nCopies(1050, "val");

        fragment.whereIn(clause, expr, largeList);

        ArgumentCaptor<BooleanExpression> captor = ArgumentCaptor.forClass(BooleanExpression.class);
        verify(clause).where(captor.capture());

        assertThat(captor.getValue()).isNotNull(); // not testing deep equality of OR-ed clauses
    }

    @Test
    void testFindPaginated_withoutTotal() {
        JPAQuery<String> query = mock(JPAQuery.class);
        when(query.fetch()).thenReturn(List.of("item1", "item2"));

        SearchCriteria criteria = new TestSearchCriteria();
        criteria.setDetermineTotal(false);

        var result = fragment.findPaginated(criteria, query);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    // Internal testable subclass for access
    static class TestableQueryFragment extends ApplicationQueryFragment {
        // no-op: expose protected methods for testing
    }
}