package gym;

/**
 * An array-based database to store all members at a fitness center chain
 * @author Sriyaank Vadlamani, Paul Manayath
 */

public class MemberDatabase {

    private Member[] mlist;
    private int size;

    private final int NOT_FOUND = -1;
    private final int MINIMUM_AGE = 18;

    /**
     * Initializes the database to have an empty database of size 4
     */
    public MemberDatabase() {
        this.size = 4;
        this.mlist = new Member[size];
    }

    /**
     * Locates the location of a specific member in the database
     * @param member the Member object of the person the user is trying to find
     * @return the index of member in mlist, NOT_FOUND if member is not in mlist
     */
    private int find(Member member) {
        for (int index = 0; index < this.size; index++) {
            if (this.mlist[index] == null)
                continue;
            if (this.mlist[index].equals(member))
                return index;
        }
        return NOT_FOUND;
    }

    /**
     * Locates the location of a specific member in the database
     * @param member the Member object of the person the user is trying to find
     * @return the specific member object, null if unable to be found
     */
    public Member findPerson(Member member) {
        for (int index = 0; index < this.size; index++) {
            if (this.mlist[index] == null)
                continue;
            if (this.mlist[index].equals(member))
                return mlist[index];
        }
        return null;
    }

    /**
     * Checks to see if a member is in the database
     * @param member the Member object of the person the user wants to know if the member is a member or not
     * @return false if member is not in database or membership is expired, true otherwise
     */
    public boolean isMember(Member member) {
        return this.find(member) != NOT_FOUND;
    }

    /**
     * Makes mlist increase in size by 4 when it is full
     */
    private void grow() {
        Member[] copy = new Member[this.size + 4];
        for (int index = 0; index < this.size; index++) {
            copy[index] = this.mlist[index];
        }
        this.size += 4;
        this.mlist = copy;
        copy = null;
    }

    /**
     * Adds a new member to the database if they are not already in it.
     * Calls upon this.grow() if adding the member makes the database full.
     *
     * @param member the member to add to the database
     * @return true if the member is successfully added, false otherwise
     */
    public boolean add(Member member) {
        member.setFname(member.getFname().substring(0,1).toUpperCase() +
                member.getFname().substring(1).toLowerCase());
        member.setLname(member.getLname().substring(0,1).toUpperCase() +
                member.getLname().substring(1).toLowerCase());

        int index = 0;
        if (!member.getDob().isValid() || this.getAge(member) < MINIMUM_AGE) {
            return false;
        }
        while (index < this.size) {
            if (this.mlist[index] == null) {
                this.mlist[index] = member;
                break;
            } else if (this.mlist[index].equals(member)) {
                return false;
            }
            index++;
        }

        if (index == this.size - 1) {
            this.grow();
        }
        return true;
    }

    /**
     * Helper for add method used to find member's age
     * @param member the member whose age needs to be checked
     * @return the member's age
     */
    private int getAge(Member member) {
        Date currentDate = new Date();
        int age = 0;
        if (currentDate.getMonth() > member.getDob().getMonth()) {
            age -= 1;
        } else if (currentDate.getDay() > member.getDob().getDay()) {
            age -= 1;
        }

        age = currentDate.getYear() - member.getDob().getYear();
        return age;
    }

    /**
     * Removes a member from the database while preserving the order of the database
     * @param member the member to remove from the database
     * @return true if the member is removed successfully, false otherwise
     */
    public boolean remove(Member member) {
        int index = this.find(member);
        if (index == NOT_FOUND) {
            return false;
        }

        while (this.mlist[index] != null && index < this.size - 1) {
            this.mlist[index] = this.mlist[index + 1];
            index++;
        }
        return this.mlist[index] == null;
    }

    /**
     * Prints out the database without any sorting
     */
    public void print() {
        for (Member member : this.mlist) {
            if (member != null)
                System.out.println(member);
        }
        System.out.println("-end of list-");
    }

    /**
     * Prints out the database without any sorting, but includes membership fees
     */
    public void printWithFees() {
        for (Member member : this.mlist) {
            if (member != null)
                System.out.println(member.toStringWithMembershipFees());

        }
        System.out.println("-end of list-");
    }

    /**
     * Searches database for a non-null value to check emptiness
     * @return true if the database is empty, false otherwise
     */
    public boolean isEmpty() {
        for (Member member : this.mlist) {
            if (member != null)
                return false;
        }
        return true;
    }

    /**
     * Prints out the database after sorting it by country.
     * If the counties are the same, it sorts by zipcode instead
     */
    public void printByCounty() {
        // sort array first
        boolean swapped;
        for (int index = 0; index < this.size - 1; index++) {
            swapped = false;
            for (int otherIndex = 0; otherIndex < this.size - index - 1; otherIndex++) {
                if(this.mlist[otherIndex+1] != null) {
                    if (this.mlist[otherIndex] == null || (this.mlist[otherIndex].getLocation().getCounty().compareTo(
                            this.mlist[otherIndex + 1].getLocation().getCounty()
                    ) > 0 || (this.mlist[otherIndex].getLocation().getCounty().equals(
                            this.mlist[otherIndex + 1].getLocation().getCounty()
                    ) && this.mlist[otherIndex].getLocation().getZipCode().compareTo(
                            this.mlist[otherIndex + 1].getLocation().getZipCode()
                    ) > 0))) {
                        Member temp = this.mlist[otherIndex];
                        this.mlist[otherIndex] = this.mlist[otherIndex + 1];
                        this.mlist[otherIndex + 1] = temp;
                        swapped = true;
                    }
                }
            }

            if (!swapped)
                break;
        }

        // print array
        this.print();
    }

    /**
     * Prints out the database after sorting it by expiration date
     */
    public void printByExpirationDate() {
        // sort array first
        boolean swapped;
        for (int index = 0; index < this.size - 1; index++) {
            swapped = false;
            for (int otherIndex = 0; otherIndex < this.size - index - 1; otherIndex++) {
                if(this.mlist[otherIndex+1] != null) {
                    if (this.mlist[otherIndex] == null || (this.mlist[otherIndex].getExpire().compareTo(
                            this.mlist[otherIndex + 1].getExpire()) > 0)) {
                        Member temp = this.mlist[otherIndex];
                        this.mlist[otherIndex] = this.mlist[otherIndex + 1];
                        this.mlist[otherIndex + 1] = temp;
                        swapped = true;
                    }
                }
            }

            if (!swapped)
                break;
        }

        // print array
        this.print();
    }

    /**
     * Prints out the database after sorting it by name
     */
    public void printByName() {
        // sort array first
        boolean swapped;
        for (int index = 0; index < this.size - 1; index++) {
            swapped = false;
            for (int otherIndex = 0; otherIndex < this.size - index - 1; otherIndex++) {
                if(this.mlist[otherIndex+1] != null) {
                    if (this.mlist[otherIndex] == null || (this.mlist[otherIndex].getLname().compareTo(
                            this.mlist[otherIndex + 1].getLname()
                    ) > 0 || (this.mlist[otherIndex].getLname().equals(
                            this.mlist[otherIndex + 1].getLname()
                    ) && this.mlist[otherIndex].getFname().compareTo(
                            this.mlist[otherIndex + 1].getFname()
                    ) > 0))) {
                        Member temp = this.mlist[otherIndex];
                        this.mlist[otherIndex] = this.mlist[otherIndex + 1];
                        this.mlist[otherIndex + 1] = temp;
                        swapped = true;
                    }
                }

            }
            if (!swapped)
                break;
        }

        // print array
        this.print();
    }
}
