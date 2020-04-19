package cn.edu.cuit.fatcat.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FastHttpDateFormatTest {

    @Test
    public void getCurrentDate() {
    }

    @Test
    public void formatDate() {
    }

    @Test
    public void testFormatDate() {
    }

    @Test
    public void parseDate() {
    }

    @Test
    public void testParseDate() {
        String value = "Sun, 19-Apr-2020 08:01:46 GMT";
        System.out.println(FastHttpDateFormat.parseDate(value));
    }
}