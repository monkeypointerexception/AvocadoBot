package gym;

/**
 * A class to store the member of gym members with family memberships as comparable objects
 * @author Sriyaank Vadlamani, Paul Manayath
 */

public class Family extends Member {

    private final double STARTING_FEE = 29.99;
    private final double MONTHLY_FEE = 59.99;
    private final double MONTHS_IN_MEMBERSHIP = 3;

    private int guestPasses;

    protected String className;

    /**
     * Constructor to create Member object with family plan
     * @param fname    the first name of the member
     * @param lname    the last name of the member
     * @param dob      the date of birth of the member
     * @param expire   the expiration date of the membership
     * @param location the fitness center location
     */
    public Family(String fname, String lname, Date dob, Date expire, String location) {
        super(fname, lname, dob, expire, location);

        this.guestPasses = 1;
        this.className = "Family";
    }

    /**
     * Constructor to create Member object from single string with family plan
     * @param member the string containing all the member's info
     */
    public Family(String member) {
        super(member);

        this.guestPasses = 1;
        this.className = "Family";
    }

    /**
     * Calculates and returns the membership fee for the family membership plan
     * @return The membership fee
     */
    @Override
    public double membershipFee() {
        return this.STARTING_FEE + this.MONTHLY_FEE * this.MONTHS_IN_MEMBERSHIP;
    }

    /**
     * Prints the string used when calling the member object, including membership plan and guest passes.
     * @return "{super.toString}, (Family) guest-pass remaining: {guest passes}"
     */
    @Override
    public String toString() {
        return super.toString() + ", (" + this.className + ") guest-pass remaining: " + this.getGuestPasses();
    }

    /**
     * Gets the number of guest passes remaining
     * @return the number of guest passes remaining
     */
    public int getGuestPasses() {
        return this.guestPasses;
    }

    /**
     * Sets the number of guest passes for the member
     * @param guestPasses the number of guest passes for the member
     */
    public void setGuestPasses(int guestPasses) {
        this.guestPasses = guestPasses;
    }

}
