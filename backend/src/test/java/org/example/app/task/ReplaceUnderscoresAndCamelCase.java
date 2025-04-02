package org.example.app.task;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

/**
 * {@code DisplayNameGenerator} that replaces underscores with spaces and camel case to readable sentences.
 *
 * <p>
 * This generator extends the functionality of {@link ReplaceUnderscores} by inserting spaces ({@code ' '}) in front of
 * capital letters and sequences of numbers in all class and method names.
 */
public class ReplaceUnderscoresAndCamelCase extends ReplaceUnderscores {

  @Override
  public String generateDisplayNameForClass(Class<?> testClass) {

    return replaceDoubleWhitespace(replaceCapitals(super.generateDisplayNameForClass(testClass)));
  }

  @Override
  public String generateDisplayNameForNestedClass(Class<?> nestedClass) {

    return replaceDoubleWhitespace(replaceCapitals(super.generateDisplayNameForNestedClass(nestedClass)));
  }

  @Override
  public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {

    return replaceDoubleWhitespace(replaceCapitals(super.generateDisplayNameForMethod(testClass, testMethod)));
  }

  private String replaceCapitals(String name) {

    name = name.replaceAll("([A-Z])", " $1");
    name = name.replaceAll("([0-9]+)", " $1");
    return name;
  }

  private String replaceDoubleWhitespace(String name) {

    return name.replaceAll("  ", " ");
  }
}