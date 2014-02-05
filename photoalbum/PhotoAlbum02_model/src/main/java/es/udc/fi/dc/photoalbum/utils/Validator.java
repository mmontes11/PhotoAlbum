package es.udc.fi.dc.photoalbum.utils;

import java.util.regex.Pattern;

/**
 */
public class Validator {

    private static final String USERNAME_PATTERN = "[a-z0-9]+";
    private final static Pattern usernamePattern = Pattern
            .compile(USERNAME_PATTERN);

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final static Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Method isEmailValid.
     * 
     * @param hex
     *            String
     * @return boolean
     */
    public static boolean isEmailValid(final String hex) {
        return emailPattern.matcher(hex).matches();
    }

    /**
     * Method isUsernameValid.
     * 
     * @param hex
     *            String
     * @return boolean
     */
    public static boolean isUsernameValid(final String hex) {
        return usernamePattern.matcher(hex).matches();
    }
}
