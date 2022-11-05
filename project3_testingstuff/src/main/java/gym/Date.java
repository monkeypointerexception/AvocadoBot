package gym;

import java.util.Calendar;

/**
 A custom implementation of the date class.
 It includes our own constructors and method to check if a date is valid.
 Comparable interface is used to define our own process for comparing date objects.
 @author Sriyaank Vadlamani, Paul Manayath
 */
public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;

    private static final int LEAP_YEAR_FEBRUARY_DAYS = 29;
    public static final int MAX_MONTHS = 12;

    private static final int LESS_THAN = -1;
    private static final int GREATER_THAN = 1;
    private static final int YEAR_INDEX = 2;
    private static final int AGE = 18;

    private static final int QUADRENNIAL = 4;
    private static final int CENTENNIAL = 100;
    private static final int QUATERCENTENNIAL = 400;

    /** Default constructor; create a date object using today's date via Calendar*/
    public Date() {
        //months start @ 0 in the Calendar class
        Calendar currentDate = Calendar.getInstance();
        this.year = currentDate.get(Calendar.YEAR);
        this.month = currentDate.get(Calendar.MONTH) + 1;
        this.day = currentDate.get(Calendar.DAY_OF_MONTH);


    }
    /** Constructor when given a string date; Create a date object from the given string
    @param date string used to create the date object
    */
    public Date(String date) {
        String[] parsedDate = date.split("/");

        this.year = Integer.parseInt(parsedDate[2]);
        this.month = Integer.parseInt(parsedDate[0]);
        this.day = Integer.parseInt(parsedDate[1]);
    }

    /** Compare this date object with given date object given as an argument
    @param date the date object to be compared with this date
    @return 1 if this object is greater than the given one,
            0 if both objects are equal, -1 if this date object is less
            than the given one
    */
    @Override
    public int compareTo(Date date) {
      if(this.year > date.year) {
        return GREATER_THAN;
      }
      if(this.year < date.year){
        return LESS_THAN;
      }
      if(this.month > date.month) {
        return GREATER_THAN;
      }
      if(this.month < date.month){
        return LESS_THAN;
      }
      return Integer.compare(this.day, date.day);
    }

    /** Check if this calendar date is valid
    @return boolean true if the date is valid, false otherwise
    */
    public boolean isValid() {
        //check if month is within 1 - 12 range
        boolean validRange = (this.month >= 1 && this.month <= MAX_MONTHS);
        if (!validRange) return false;

        //get maximum days in a month via calendar
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, this.month - 1);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        //we only need to check for a leap year if it is February
        if (this.month == Calendar.FEBRUARY + 1) {
            if (isLeapYear()) {max = LEAP_YEAR_FEBRUARY_DAYS;}
        }
        //check if number of days are within bounds
        return (this.day >= 1 && this.day <= max);
    }

    /** Check if the year from the date object is a leap year
    @return boolean true if it is a leap year, false if not
    */
    private boolean isLeapYear() {
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                return (this.year % QUATERCENTENNIAL == 0);
            }
            return true;
        }
        return false; 
    }
    /** Checks the validity of a member's DOB
     @return string stating that the member is not 18 or older,the DOB cannot be
        today or a future date, or is valid
     */
    public String isValidDOB() {
        if (!this.isValid()) {
            return "DOB " + this.toString() + ": invalid calendar date!";
        }
        Date current = new Date();
        if (this.compareTo(current) > LESS_THAN)
            return "DOB " + this.toString() + ": cannot be today or a future date!";
        //check if under 18
        if (current.year - this.year < AGE)
            return "DOB " + this.toString() + ": must be 18 or older to join!";
        //diff btw years may be 18, but check months and day to confirm
        if (current.year - this.year == AGE) {
            if (this.month > current.month)
                return "DOB " + this.toString() + ": must be 18 or older to join!";
            if ((this.month == current.month) && (this.day > current.day))
                return "DOB " + this.toString() + ": must be 18 or older to join!";
        }
        return "valid";
    }
    
    /** Returns string representation of the date object
    @return string representation of the date object
    */
    @Override
    public String toString() {
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * Returns if the date has already passed.
     * @return true if the date has already passed, false otherwise
     */
    public boolean dateHasPassed() {
        return this.compareTo(new Date()) == -1;
    }

    /** Returns the year
     @return the year
     */
    public int getYear() {
        return this.year;
    }

    /** Returns the month
     @return the month
     */
    public int getMonth() {
        return this.month;
    }

    /** Returns the date
     @return the date
     */
    public int getDay() {
        return this.day;
    }

}
