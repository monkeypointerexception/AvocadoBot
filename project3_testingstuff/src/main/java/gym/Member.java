package gym;

/**
 * A class to store the member of gym members with standard memberships as comparable objects
 * @author Sriyaank Vadlamani, Paul Manayath
 */

public class Member implements Comparable<Member>{
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;


    private final double STARTING_FEE = 29.99;
    private final double MONTHLY_FEE = 39.99;
    private final double MONTHS_IN_MEMBERSHIP = 3;

    /**
     * Constructor to create Member object with standard plan
     * @param fname the first name of the member
     * @param lname the last name of the member
     * @param dob the date of birth of the member
     * @param expire the expiration date of the membership
     * @param location the fitness center location
     */
    public Member(String fname, String lname, Date dob, Date expire, String location) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expire;

        if (location != null) {
            switch (location.toUpperCase()) {
                case "BRIDGEWATER" -> this.location = Location.BRIDGEWATER;
                case "EDISON" -> this.location = Location.EDISON;
                case "FRANKLIN" -> this.location = Location.FRANKLIN;
                case "PISCATAWAY" -> this.location = Location.PISCATAWAY;
                case "SOMERVILLE" -> this.location = Location.SOMERVILLE;
            }
        }

//        this.guestPasses = 0;
    }

    /**
     * Constructor to create Member object from single string with standard plan
     * @param member the string containing all the member's info
     */
    public Member(String member) {
        String[] fields = member.split(" ");
        this.fname = fields[0].substring(0, 1).toUpperCase()
                + fields[0].substring(1).toLowerCase();
        this.lname = fields[1].substring(0, 1).toUpperCase()
                + fields[1].substring(1).toLowerCase();
        this.dob = new Date(fields[2]);

        // sets the expiration date 3 months in the future
        // works for standard and family plan, but premium must recall it
        this.setExpire(3);

        if (fields.length > 3) {
            switch (fields[3].toUpperCase()) {
                case "BRIDGEWATER" -> this.location = Location.BRIDGEWATER;
                case "EDISON" -> this.location = Location.EDISON;
                case "FRANKLIN" -> this.location = Location.FRANKLIN;
                case "PISCATAWAY" -> this.location = Location.PISCATAWAY;
                case "SOMERVILLE" -> this.location = Location.SOMERVILLE;
            }
        }

//        this.guestPasses = 0;
    }

    /**
     * Prints the string used when calling the member object.
     * @return "{first name} {last name}, DOB: {dob}, Membership expires {expiration date}, Location: {location}"
     */
    @Override
    public String toString() {
        return this.fname + " " + this.lname +
                ", DOB: " + dob + ", Membership expires " + expire +
                ", Location: " + location;
    }

    /**
     * Prints out the string with the information in toString plus the membership Fee
     * @return "{toString}, Membership Fee: {membershipFee}"
     */
    public String toStringWithMembershipFees() {
        return this + ", Membership Fee: $" + this.membershipFee();
    }

    /**
     * Checks to see if two Member objects are the same
     * @param obj the member object to compared
     * @return true if the member objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return this.compareTo((Member) obj) == 0;
    }

    /**
     * Compares two member objects
     * The objects are compared by last name,
     * then first name if last names are the same,
     * then date of birth if first names are the same
     * @param member the member object to be compared.
     * @return negative number if member is greater, positive if less, 0 otherwise
     */
    @Override
    public int compareTo(Member member) {
        if (!this.lname.equalsIgnoreCase(member.lname)) {
            return this.lname.compareTo(member.lname);
        } else if (!this.fname.equalsIgnoreCase(member.fname)) {
            return this.fname.compareTo(member.fname);
        } else {
            return this.dob.compareTo(member.dob);
        }
    }

    /**
     * Calculates and returns the membership fee for the standard membership plan
     * @return The membership fee
     */
    public double membershipFee() {
        return this.STARTING_FEE + this.MONTHLY_FEE * this.MONTHS_IN_MEMBERSHIP;
    }

    /**
     * testbed main
     */
    public static void main(String[] args) {
        // testcase 1: Different Date of Birth
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // John Doe 12/2/2022 3/30/2023 BRIDGEWATER
        // expected output: negative
        Member one = new Member("John Doe 9/2/2022 3/30/2023 BRIDGEWATER");
        System.out.println("Testing case 1");
        int test = one.compareTo(new Member("John Doe 12/2/2022 3/30/2023 BRIDGEWATER"));
        if(test < 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();

        // testcase 2: Different First Name
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // Jane Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // expected output: positive
        System.out.println("Testing case 2");
        test = one.compareTo(new Member("Jane Doe 9/2/2022 3/30/2023 BRIDGEWATER"));
        if(test > 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();

        // testcase 3: Different Last Name
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // John Joe 9/2/2022 3/30/2023 BRIDGEWATER
        // expected output: negative
        System.out.println("Testing case 3");
        test = one.compareTo(new Member("John Joe 9/2/2022 3/30/2023 BRIDGEWATER"));
        if(test < 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();

        // testcase 4: Same Name and Date of Birth
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // John Doe 9/2/2022 3/29/2023 EDISON
        // expected output: 0
        System.out.println("Testing case 4");
        test = one.compareTo(new Member("John Doe 9/2/2022 3/29/2023 EDISON"));
        if(test == 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();

        // testcase 5: Different Names and Dates of Birth
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // April March 3/31/1990 6/30/2023 PISCATAWAY
        // expected output: negative (should be prioritizing last name in comparison)
        System.out.println("Testing case 5");
        test = one.compareTo(new Member("April March 3/31/1990 6/30/2023 PISCATAWAY"));
        if(test < 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();

        // testcase 6: Same Last Name Only
        // John Doe 9/2/2022 3/30/2023 BRIDGEWATER
        // April Doe 3/31/1990 6/30/2023 PISCATAWAY
        // expected output: positive (should be prioritizing first name in comparison)
        System.out.println("Testing case 6");
        test = one.compareTo(new Member("April Doe 3/31/1990 6/30/2023 PISCATAWAY"));
        if(test > 0)
            System.out.println("Success: " + test);
        else
            System.out.println("Fail: " + test);

        System.out.println();


    }

    /**
     Returns the first name of the member
     @return the first name of the member
     */
    public String getFname() {
        return this.fname;
    }

    /**
     Returns the last name of the member
     @return the last name of the member
     */
    public String getLname() {
        return this.lname;
    }

    /**
     Returns the date of birth of the member
     @return the date of birth of the member
     */
    public Date getDob() {
        return this.dob;
    }

    /**
     Returns the expiration date of the member's membership
     @return the expiration date of the member's membership
     */
    public Date getExpire() {
        return this.expire;
    }

    /**
     Returns the location of the member's fitness center
     @return the location of the member's fitness center
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Sets the first name of the member
     * @param fname the first name of the member
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * Sets the last name of the member
     * @param lname the last name of the member
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * Sets the date of birth of the member
     * @param dob the date of birth of the member
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Sets the expiration date of the membership
     * @param months the number of months the membership lasts
     */
    public void setExpire(int months) {
        Date currentDate = new Date();
        if (currentDate.getMonth() + months > 12) {
            this.expire = new Date("" + ((currentDate.getMonth() + months)%12)
                    + "/" + currentDate.getDay() + "/" + (currentDate.getYear() + 1));
        } else {
            this.expire = new Date("" + (currentDate.getMonth() + months)
                    + "/" + currentDate.getDay() + "/" + currentDate.getYear());
        }
    }

//    /**
//     * Gets the number of guest passes remaining
//     * @return the number of guest passes remaining
//     */
//    public int getGuestPasses() {
//        return this.guestPasses;
//    }
//
//    /**
//     * Sets the number of guest passes for the member
//     * @param guestPasses the number of guest passes for the member
//     */
//    public void setGuestPasses(int guestPasses) {
//        this.guestPasses = guestPasses;
//    }
}
