package org.example.app.general.common.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LikePatternSyntaxTest {
    @Test
    void testConvertGlobToSql() {
        String pattern = "*abc?def*";
        String expected = "%abc_def%";
        String result = LikePatternSyntax.SQL.convert(pattern, LikePatternSyntax.GLOB, false);
        assertEquals(expected, result);
    }

    @Test
    void testConvertSqlToGlob() {
        String pattern = "%abc_def%";
        String expected = "*abc?def*";
        String result = LikePatternSyntax.GLOB.convert(pattern, LikePatternSyntax.SQL, false);
        assertEquals(expected, result);
    }

    @Test
    void testConvertWithMatchSubstringTrue() {
        String pattern = "abc";
        String expected = "%abc%";
        String result = LikePatternSyntax.SQL.convert(pattern, LikePatternSyntax.SQL, true);
        assertEquals(expected, result);
    }

    @Test
    void testConvertNullPattern() {
        String result = LikePatternSyntax.SQL.convert(null, LikePatternSyntax.SQL, false);
        assertNull(result);
    }

    @Test
    void testConvertEmptyPatternWithMatchSubstringTrue() {
        String result = LikePatternSyntax.SQL.convert("", LikePatternSyntax.SQL, true);
        assertEquals("%", result);
    }

    @Test
    void testAutoDetectGlob() {
        assertEquals(LikePatternSyntax.GLOB, LikePatternSyntax.autoDetect("file-*.txt"));
    }

    @Test
    void testAutoDetectSql() {
        assertEquals(LikePatternSyntax.SQL, LikePatternSyntax.autoDetect("user_%_2024"));
    }

    @Test
    void testAutoDetectNoWildcards() {
        assertNull(LikePatternSyntax.autoDetect("plainText"));
    }

    @Test
    void testEscapedCharactersInPattern() {
        String pattern = "*abc\\*def*";
        String result = LikePatternSyntax.SQL.convert(pattern, LikePatternSyntax.GLOB, false);
        assertTrue(result.contains("\\%"));
    }
}
