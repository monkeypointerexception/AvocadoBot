package gym;

import java.util.ArrayList;

/**
 A class used to keep track of the members of a specific fitness class
 Also contains data on the fitness class including name, instructor,
    time it takes place, and location
 @author Sriyaank Vadlamani, Paul Manayath
 */
public class FitnessClass {

    public ArrayList<Member> members;
    public ArrayList<Member> guests;
    private String className;
    private String instructor;
    private Time time;
    private Location location;

    /** Constructor for FitnessClass object
     * Initialize arraylist to hold members
     */
    public FitnessClass() {
        this.members = new ArrayList<Member>();
        this.guests = new ArrayList<Member>();
    }
    /** Add member into the fitness class
     @param newMember the member object to be checked into a class
     @return true if member has been added
     */
    public boolean addMember(Member newMember) {
        if (members.contains(newMember)) {
            return false;
        }
        members.add(newMember);
        return true;
    }
    /**
     * Add guest into fitness class
     * @param newMember
     * @return boolean
     */
    public boolean addGuest(Member newMember) {
        if (!((newMember instanceof Family) || (newMember instanceof Premium)))
            return false;

        int pass = ((Family) newMember).getGuestPasses() - 1;
        ((Family) newMember).setGuestPasses(pass);
        guests.add(newMember);
        return true;
    }
    /** remove member from a fitness class
     @param newMember the member object to be removed from a class
     @return boolean if removed
     */
    public boolean removeMember(Member newMember) {
        if (!members.contains(newMember)) {
            return false;
        }
        members.remove(newMember);
        return true;
    }
    /** remove guest from a fitness class
     @param newMember the guest to be removed from a class
     @return boolean if removed
     */
    public boolean removeGuest(Member newMember) {
        if(!guests.contains(newMember) || (!(newMember instanceof Family) && !(newMember instanceof Premium))) {
            return false;
        }
        int pass = ((Family) newMember).getGuestPasses() + 1;
        ((Family) newMember).setGuestPasses(pass);
        guests.remove(newMember);
        return true;
    }
    /**
     * Checks if a member is part of a fitness Class
     * @param member the member to be checked
     * @return true if the member is in the fitness class
     */
    public boolean isInClass(Member member) {return members.contains(member);}
    /** Returns a string of the details of the fitness class
     @return a string containing class name, instructor name, time, and location
     */
    public String classDetails() {
        return this.className+ " - " + this.instructor + ", " + this.time.toString()
                + ", " + this.location.name();
    }

    /**
     * Checks to see if two classes are equal
     * @param e the fitness class to compare to
     * @return true if the classes are equal, false otherwise
     */
    @Override
    public boolean equals(Object e) {
        if (e == null)
            return false;
        FitnessClass classToCompare = (FitnessClass) e;

        return ((this.className == classToCompare.className)
        && (this.instructor == classToCompare.instructor)
        && (this.location == classToCompare.location));
    }
    /**
     * Gets time the fitness class
     * @return time of class
     */
    public Time getTime() {return this.time;}

    /**
     * Sets time for a fitness Class
     * @param time the time to be set
     */
    public void setTime(Time time) {this.time = time;}

    /**
     * Return name of a fitness class
     * @return this.className
     */
    public String getClassName() {return this.className;}

    /**
     * Sets fitness class name
     * @param className the name of the fitness class
     */
    public void setClassName(String className) {this.className = className;}

    /**
     * Return fitness class location
     * @return the location of the fitness class
     */
    public Location getLocation() {return this.location;}

    /**
     * Sets location of a fitness class
     * @param location the location
     */
    public void setLocation(Location location) {this.location = location;}

    /**
     * Get name of the instructor of a fitness class
     * @return name of the instructor
     */
    public String getInstructor() {return this.instructor;}

    /**
     * The instructor to be set for a fitness class
     * @param instructor the name of the instructor
     */
    public void setInstructor(String instructor) {this.instructor = instructor;}
}