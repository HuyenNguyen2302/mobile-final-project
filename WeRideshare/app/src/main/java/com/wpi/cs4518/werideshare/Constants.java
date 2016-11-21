package com.wpi.cs4518.werideshare;

import java.util.regex.Pattern;

/**
 * Created by mrampiah on 11/13/16.
 */
public class Constants {
    private static Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public static final int REQUEST_READ_CONTACTS = 0;

    public static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    public static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    public static final int TRUE = 1;

    public static final int FALSE = 0;


    /**
     * A dummy authentication store containing known currentUser names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    public static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:Hello1", "bar@example.com:world"
    };

    private Constants() {

    }

    public static Pattern getEmailPattern(){
        return Pattern.compile(EMAIL_PATTERN);
    }

    public static Pattern getPasswordPattern(){
        return Pattern.compile(PASSWORD_PATTERN);
    }
}
