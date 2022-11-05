package gym;

/**
 * A class to store the member of gym members with premium memberships as comparable objects
 * @author Sriyaank Vadlamani, Paul Manayath
 */

public class Premium extends Family {

    private final double MONTHLY_FEE = 59.99;
    private final double MONTHS_IN_MEMBERSHIP = 12;

    /**
     * Constructor to create Member object with premium plan
     * @param fname    the first name of the member
     * @param lname    the last name of the member
     * @param dob      the date of birth of the member
     * @param expire   the expiration date of the membership
     * @param location the fitness center location
     */
    public Premium(String fname, String lname, Date dob, Date expire, String location) {
        super(fname, lname, dob, expire, location);

        super.setGuestPasses(3);
        super.className = "Premium";
    }

    /**
     * Constructor to create Member object from single string with premium plan
     * @param member the string containing all the member's info
     */
    public Premium(String member) {
        super(member);

        super.setExpire(12); // sets the expiration date 12 months from current date
        super.setGuestPasses(3);
        super.className = "Premium";
    }

    /**
     * Calculates and returns the membership fee for the premium membership plan
     * @return The membership fee
     */
    @Override
    public double membershipFee() {
        return this.MONTHLY_FEE * (this.MONTHS_IN_MEMBERSHIP - 1);
    }

}
