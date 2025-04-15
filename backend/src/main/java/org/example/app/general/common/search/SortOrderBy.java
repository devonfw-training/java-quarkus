package org.example.app.general.common.search;

import java.util.Objects;

/**
 * Specifies an ORDER BY clause.
 */
public class SortOrderBy {

  private String name;

  private SortOrderDirection direction;

  /**
   * The constructor.
   */
  public SortOrderBy() {

    super();
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param direction the {@link #getDirection() direction}.
   */
  public SortOrderBy(String name, SortOrderDirection direction) {

    super();
    this.name = name;
    this.direction = direction;
  }

  /**
   * @return the name of the property to order by.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName()}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return the {@link SortOrderDirection}.
   */
  public SortOrderDirection getDirection() {

    if (this.direction == null) {
      return SortOrderDirection.ASC;
    }
    return this.direction;
  }

  /**
   * @param direction new value of {@link #getDirection()}.
   */
  public void setDirection(SortOrderDirection direction) {

    this.direction = direction;
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.name, this.direction);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SortOrderBy other = (SortOrderBy) obj;
    if (this.direction != other.direction) {
      return false;
    }
    return Objects.equals(this.name, other.name);
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append(this.name);
    sb.append(' ');
    sb.append(this.direction);
    return sb.toString();
  }

  /**
   * @param name the {@link #getName() name} to order by.
   * @return the new {@link SortOrderBy} for the given {@link #getName() name} with {@link SortOrderDirection#ASC
   *         ascending} order.
   */
  public static SortOrderBy ofAsc(String name) {

    return new SortOrderBy(name, SortOrderDirection.ASC);
  }

  /**
   * @param name the {@link #getName() name} to order by.
   * @return the new {@link SortOrderBy} for the given {@link #getName() name} with {@link SortOrderDirection#DESC
   *         descending} order.
   */
  public static SortOrderBy ofDesc(String name) {

    return new SortOrderBy(name, SortOrderDirection.DESC);
  }

}
