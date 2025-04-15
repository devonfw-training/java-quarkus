package org.example.app.general.dataaccess;

import com.querydsl.core.FilteredClause;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.example.app.general.common.search.LikePatternSyntax;
import org.example.app.general.common.search.SearchCriteria;
import org.example.app.general.common.search.SortOrderBy;
import org.example.app.general.common.search.SortOrderDirection;
import org.example.app.general.common.search.StringSearchOperator;
import org.example.app.general.common.search.StringSearchOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Abstract base class for query fragment of spring-data repository.
 */
public abstract class ApplicationQueryFragment {

  private static final Logger LOG = LoggerFactory.getLogger(ApplicationQueryFragment.class);

  /** Database limit of values in an IN expression. */
  private static final int MAX_IN_EXPRESSIONS = 1000;

  /** The {@link EntityManager}. */
  @Inject
  protected EntityManager em;

  /**
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param options the {@link StringSearchOptions} to configure the search. May be {@code null} for regular equals
   *        search as default fallback.
   * @return the new {@link BooleanExpression} for the specified string comparison clause.
   */
  protected BooleanExpression newStringClause(StringExpression expression, String value, StringSearchOptions options) {

    StringSearchOperator operator = StringSearchOperator.EQ;
    LikePatternSyntax syntax = null;
    boolean ignoreCase = false;
    boolean matchSubstring = false;
    if (options != null) {
      operator = options.getOperator();
      syntax = options.getLikeSyntax();
      ignoreCase = options.isIgnoreCase();
      matchSubstring = options.isMatchSubstring();
    }
    return newStringClause(expression, value, operator, syntax, ignoreCase, matchSubstring);
  }

  /**
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param operator the {@link StringSearchOperator} used to compare the search string {@code value}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the new {@link BooleanExpression} for the specified string comparison clause.
   */
  protected BooleanExpression newStringClause(StringExpression expression, String value, StringSearchOperator operator,
      LikePatternSyntax syntax, boolean ignoreCase, boolean matchSubstring) {

    if (operator == null) {
      if (value == null) {
        return null;
      }
      if (syntax == null) {
        syntax = LikePatternSyntax.autoDetect(value);
        if (syntax == null) {
          operator = StringSearchOperator.EQ;
        } else {
          operator = StringSearchOperator.LIKE;
        }
      } else {
        operator = StringSearchOperator.LIKE;
      }
    }
    if (matchSubstring && ((operator == StringSearchOperator.EQ) || (operator == StringSearchOperator.NE))) {
      if (syntax == null) {
        syntax = LikePatternSyntax.SQL;
      }
      if (operator == StringSearchOperator.EQ) {
        operator = StringSearchOperator.LIKE;
      } else {
        operator = StringSearchOperator.NOT_LIKE;
      }
    }
    String v = value;
    if (v == null) {
        return switch (operator) {
            case LIKE, EQ -> expression.isNull();
            case NE -> expression.isNotNull();
            default -> throw new IllegalArgumentException("Operator " + operator + " does not accept null!");
        };
    } else if (v.isEmpty()) {
      switch (operator) {
        case LIKE, EQ:
          return expression.isEmpty();
        case NOT_LIKE, NE:
          return expression.isNotEmpty();
        default:
          // continue
      }
    }
    StringExpression exp = expression;
    if (ignoreCase) {
      v = v.toUpperCase(Locale.US);
      exp = exp.upper();
    }
      return switch (operator) {
          case LIKE -> newLikeClause(exp, v, syntax, false, matchSubstring, false);
          case NOT_LIKE -> newLikeClause(exp, v, syntax, false, matchSubstring, true);
          case EQ -> exp.eq(v);
          case NE -> exp.ne(v);
          case LT -> exp.lt(v);
          case LE -> exp.loe(v);
          case GT -> exp.gt(v);
          case GE -> exp.goe(v);
          default -> throw new IllegalStateException("" + operator);
      };
  }

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   */
  protected void whereLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    BooleanExpression like = newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, false);
    if (like != null) {
      query.where(like);
    }
  }

  /**
   * @param query the {@link JPAQuery} to modify.
   * @param expression the {@link StringExpression} to {@link StringExpression#notLike(String) create the NOT
   *        LIKE-clause} from.
   * @param pattern the pattern for the NOT LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   */
  protected void whereNotLike(JPAQuery<?> query, StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    BooleanExpression like = newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, true);
    if (like != null) {
      query.where(like);
    }
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, false);
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#notLike(String) create the NOT
   *        LIKE-clause} from.
   * @param pattern the pattern for the NOT LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @return the NOT LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newNotLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring) {

    return newLikeClause(expression, pattern, syntax, ignoreCase, matchSubstring, true);
  }

  /**
   * @param expression the {@link StringExpression} to {@link StringExpression#like(String) create the LIKE-clause}
   *        from.
   * @param pattern the pattern for the LIKE-clause to create.
   * @param syntax the {@link LikePatternSyntax} of the given {@code pattern}.
   * @param ignoreCase - {@code true} to ignore the case, {@code false} otherwise (to search case-sensitive).
   * @param matchSubstring - {@code true} to match also if the given {@code pattern} shall also match substrings on the
   *        given {@link StringExpression}.
   * @param negate - {@code true} for {@link StringExpression#notLike(String) NOT LIKE}, {@code false} for
   *        {@link StringExpression#like(String) LIKE}.
   * @return the LIKE-clause as {@link BooleanExpression}.
   */
  protected BooleanExpression newLikeClause(StringExpression expression, String pattern, LikePatternSyntax syntax,
      boolean ignoreCase, boolean matchSubstring, boolean negate) {

    if (syntax == null) {
      syntax = LikePatternSyntax.autoDetect(pattern);
      if (syntax == null) {
        syntax = LikePatternSyntax.SQL;
      }
    }
    String likePattern = LikePatternSyntax.SQL.convert(pattern, syntax, matchSubstring);
    StringExpression exp = expression;
    if (ignoreCase) {
      likePattern = likePattern.toUpperCase(Locale.US);
      exp = exp.upper();
    }
    BooleanExpression clause;
    if (syntax != LikePatternSyntax.SQL) {
      clause = exp.like(likePattern, LikePatternSyntax.ESCAPE);
    } else {
      clause = exp.like(likePattern);
    }
    if (negate) {
      clause = clause.not();
    }
    return clause;
  }

  /**
   * @param statement the database statement as {@link FilteredClause} where to append a condition to the WHERE clause.
   * @param expression the {@link StringExpression} to search on.
   * @param value the string value or pattern to search for.
   * @param options the {@link StringSearchOptions} to configure the search. May be {@code null} for regular equals
   *        search.
   */
  protected void where(FilteredClause<?> statement, StringExpression expression, String value,
      StringSearchOptions options) {

    BooleanExpression clause = newStringClause(expression, value, options);
    if (clause != null) {
      statement.where(clause);
    }
  }

  /**
   * @param statement the database statement as {@link FilteredClause} where to append a condition to the WHERE clause.
   * @param expression the {@link StringExpression} to search on.
   * @param value the {@link Boolean} value to search. Will be {@code null} to ignore.
   */
  protected void where(FilteredClause<?> statement, BooleanExpression expression, Boolean value) {

    if (value != null) {
      BooleanExpression clause = expression.eq(value);
      statement.where(clause);
    }
  }

  /**
   * @param <V> type of the values for the IN expression (e.g. {@link Long} for IDs).
   * @param statement the database statement as {@link FilteredClause} (e.g. {@link JPAQuery}).
   * @param expression the {@link SimpleExpression} to build the IN-expression from.
   * @param values the {@link List} of values for the IN-expression.
   */
  public <V> void whereIn(FilteredClause<?> statement, SimpleExpression<V> expression, List<V> values) {

    BooleanExpression inExpression = null;
    int size = 0;
    if (values != null) {
      size = values.size();
    }
    if ((size == 0) || (values == null)) {
      LOG.info("Missing values for statement: {}", statement);
      inExpression = Expressions.ONE.eq(Expressions.ZERO);
    } else if (size <= MAX_IN_EXPRESSIONS) {
      inExpression = expression.in(padList(values));
    } else {
      LOG.info("Expression {} requires partiitoning as {} values shall be filled in IN-expression of statement: {}",
          expression, size, statement);
      int rest = size;
      int start = 0;
      while (rest > 0) {
        List<V> partition;
        if (rest >= MAX_IN_EXPRESSIONS) {
          int end = start + MAX_IN_EXPRESSIONS;
          partition = values.subList(start, end);
          start = end;
          rest -= MAX_IN_EXPRESSIONS;
        } else {
          partition = padList(values.subList(start, size));
          rest = 0;
        }
        BooleanExpression newInExpr = expression.in(partition);
        if (inExpression == null) {
          inExpression = newInExpr;
        } else {
          inExpression = inExpression.or(newInExpr);
        }
      }
    }
    statement.where(inExpression);
  }

  // prevent flooding the DB query cache with query variants causing hard parses
  private <V> List<V> padList(List<V> list) {

    int size = list.size();
    if (size <= 2) {
      return list;
    }
    int paddedSize = 4;
    while (paddedSize < size) {
      paddedSize = paddedSize * 2;
    }
    if (paddedSize > MAX_IN_EXPRESSIONS) {
      paddedSize = MAX_IN_EXPRESSIONS; // 1024 -> 1000
    }
    list = new ArrayList<>(list);
    V lastValue = list.get(size - 1);
    list.addAll(Collections.nCopies(paddedSize - size, lastValue));
    return list;
  }

  /**
   * @param statement the statement as {@link QueryBase}.
   * @param sort the {@link List} of {@link SortOrderBy}-items.
   * @param mapper the {@link Function} to map from {@link SortOrderBy#getName() sort order name} to
   *        {@link ComparableExpressionBase}.
   */
  protected void orderBy(QueryBase<?> statement, List<SortOrderBy> sort,
      Function<String, ComparableExpressionBase<?>> mapper) {

    if (sort == null) {
      return;
    }
    for (SortOrderBy order : sort) {
      ComparableExpressionBase<?> expression = mapper.apply(order.getName());
      OrderSpecifier<?> orderSpecifier;
      if (SortOrderDirection.ASC.equals(order.getDirection())) {
        orderSpecifier = expression.asc();
      } else {
        orderSpecifier = expression.desc();
      }
      statement.orderBy(orderSpecifier);
    }
  }

  /**
   * @param <E> type of the objects to find.
   * @param criteria the {@link SearchCriteria}.
   * @param query the {@link JPAQuery} to execute.
   * @return the resulting {@link Page} with the found hits.
   */
  protected <E> Page<E> findPaginated(SearchCriteria criteria, JPAQuery<E> query) {

    long total = -1;
    if (criteria.isDetermineTotal()) {
      total = query.clone().fetchCount();
    }
    long offset = 0;
    Pageable pageable = criteria.asPageable();
    if (pageable != null) {
      offset = pageable.getOffset();
      query.offset(offset);
      query.limit(pageable.getPageSize());
    }
    List<E> hits = query.fetch();
    if (total == -1) {
      total = offset + hits.size();
    }
    return new PageImpl<>(hits, pageable, total);
  }
}
