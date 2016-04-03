package de.htw_berlin.movation;

/**
 * Created by root on 02.01.2016.
 */
public final class Constants {

    private Constants(){}

    public static final int NOTIFICATION_ID = 6822;

    public enum SensorTypes {
        HEART_RATE,
        PEDOMETER,
        CALORIES,
        CONTACT,
        DISTANCE,
        NONE
    }

    public enum SkinColor {
        BLACK,
        BROWN,
        ASIAN,
        CAUCASIAN
    }

    public enum Sex {
        FEMALE,
        MALE
    }

    public enum Fitness {
        FIT,
        AVERAGE,
        FAT
    }

    public enum ClothType {
        TOP,
        BOTTOM
    }

    public enum Haircut{
        CUT1, CUT2, CUT3, CUT4, CUT5, CUT6, CUT7, CUT8
    }

    public enum Face {
        FACE1, FACE2, FACE3, FACE4, FACE5, FACE6
    }
}
