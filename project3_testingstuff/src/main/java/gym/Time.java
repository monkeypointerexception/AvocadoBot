package gym;

/**
 An implementation of a class to keep track of the times of various
 fitness classes.
 Records time (hh:mm)
 @author Sriyaank Vadlamani, Paul Manayath
 */

public enum Time {

    morning("9","30"),
    afternoon("14","00"),
    evening("18","30");

    private final String hour;
    private final String minute;

    /**
     * Default constructor for the enum;
     *
     * @param hour         hour as hh for the hour of the class
     * @param minute       minute as mm for the min of the class
     */
    Time(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /** Returns time of the class as a string
     @return string of the time of the class
     */
    @Override
    public String toString(){
        return this.hour + ":" + this.minute;
    }
}