package com.wpi.cs4518.werideshare;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mrampiah on 11/13/16.
 */

public class LoginTests {

    @Test
    public void testPasswordMatch(){
        /* accepted pattern:
         * one lower case letter,
         * one upper case letter,
         * one digit
         * length >= 8
         *
         */

        assertTrue(Pattern.matches(Constants.PASSWORD_PATTERN, "TestPass1"));

        assertFalse(Pattern.matches(Constants.PASSWORD_PATTERN, "TESTPASS1"));//missing lowercase
        assertFalse(Pattern.matches(Constants.PASSWORD_PATTERN, "testpass1"));//missing uppercase
        assertFalse(Pattern.matches(Constants.PASSWORD_PATTERN, "TestPass"));//missing digit
        assertFalse(Pattern.matches(Constants.PASSWORD_PATTERN, "Test1"));//length too short
    }

    @Test
    public void testEmailMatch(){
        //regular email example
        assertTrue(Pattern.matches(Constants.EMAIL_PATTERN, "test@example.com"));

        //numbers allowed; minimum of 2 letters for domain (accepted: .com, .cm; not accepted: .c .l )
        assertTrue(Pattern.matches(Constants.EMAIL_PATTERN, "te23st@exple.cm"));

        //allow special characters eg. "."
        assertTrue(Pattern.matches(Constants.EMAIL_PATTERN, "t..3@example.com"));


        assertFalse(Pattern.matches(Constants.EMAIL_PATTERN, "te23st@exple.m"));//one letter domain

    }
}
