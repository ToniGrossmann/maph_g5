package de.htw_berlin.movation;

/**
 * Created by root on 02.01.2016.
 */
public final class Constants {

    private Constants(){}

    public enum SensorTypes {
        HEART_RATE,
        PEDOMETER,
        CALORIES,
        CONTACT,
        DISTANCE,
        NONE
    }

    public static final String LOGINPREFS_USERNAME = "loginprefs_username";
    public static final String LOGINPREFS_PASSWORD = "loginprefs_password";
    public static final String LOGINPREFS_AUTOLOGIN = "loginprefs_autologin";
}
