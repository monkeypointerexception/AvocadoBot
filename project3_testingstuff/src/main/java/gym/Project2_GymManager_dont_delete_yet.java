package gym;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * A class used to help run the gym manager
 * @author Sriyaank Vadlamani, Paul Manayath
 */
public class Project2_GymManager_dont_delete_yet {

    private MemberDatabase database;
    private Scanner keyboard;
    private ClassSchedule classes;

    /**
     * Default Constructor to initialize the Scanner, MemberDatabase, and FitnessClass objects
     */
    public Project2_GymManager_dont_delete_yet() {
        this.keyboard = new Scanner(System.in);
        this.database = new MemberDatabase();
        this.classes = new ClassSchedule();
    }

    /**
     * Executes the Gym Manager
     */
    public void run() {
        String operation = "";
        String command = "";

        System.out.println("Gym Manager running...");

        do {
            String input = this.keyboard.nextLine();
            if (input.isBlank())
                continue;
            String fields[] = input.split(" ", 2);
            operation = fields[0];
            if (fields.length > 1)
                command = fields[1];
            this.performOperation(operation, command);

        } while(!operation.equals("Q"));
    }

    /**
     * Executes a specific operation (add member, drop class etc) based on user input
     * @param operation the operation to perform
     * @param command the string containing information about member, class etc
     */
    private void performOperation(String operation, String command) {
        if (operation.charAt(0) == ('P')) {
            command = operation;
            operation = "P";
        }

        char plan = 'S';
        if (operation.charAt(0) == ('A')) {
            if (operation.length() > 1)
                plan = operation.charAt(1);
            operation = "A";
        }

        switch (operation) {
            case "A" -> this.addMember(command, plan);
            case "R" -> this.removeMember(command);
            case "P" -> this.printDatabase(command);
            case "S" -> this.classes.printClasses();
            case "C" -> this.checkInOrDropClass(command, "Check In", false);
            case "D" -> this.checkInOrDropClass(command, "Drop", false);
            case "CG" -> this.checkInOrDropClass(command, "Check In", true);
            case "DG" -> this.checkInOrDropClass(command, "Drop", true);
            case "LS" -> this.loadFitnessClasses();
            case "LM" -> this.historyMembers();
            case "Q" -> System.out.println("Gym Manager terminated.");
            default -> System.out.println(operation + " is an invalid command!");
        }
    }

    /**
     * Used to either check in or drop a class
     * @param command the string containing information about the class and member
     * @param checkInOrDrop tells whether to check in to or drop a class
     * @param guest check if guest pass was used
     */
    private void checkInOrDropClass(String command, String checkInOrDrop, Boolean guest) {
        String[] fields = command.split(" ");
        //validate fitness class inputs
        FitnessClass fitnessClass = this.createAndValidateClass(fields[0], fields[1], fields[2]);
        if (fitnessClass == null)
            return;
        Member member = new Member(fields[3], fields[4], new Date(fields[5]),
                null, null);
        if (!member.getDob().isValid()) {
            System.out.println("DOB " + member.getDob().toString() + ": invalid calendar date!");
            return;
        }
        if (!this.database.isMember(member)) {
            System.out.println(fields[3] + " " + fields[4] + " " + fields[5] + " is not in the database.");
            return;
        }
        Member officialMember = this.database.findPerson(member);
        String fullname = fields[3] + " " + fields[4] + " " + officialMember.getDob().toString();
        if (officialMember.getExpire().dateHasPassed()) {
            System.out.println(fullname + " membership expired.");
            return;
        }
        if (checkInOrDrop.equals("Check In")) {
            this.addToClass(fitnessClass, officialMember, guest);
        } else if (checkInOrDrop.equals("Drop")) {
            this.dropClass(fitnessClass, officialMember, guest);
        }
    }

    /**
     * Attempt to add member into database
     * @param command The string input to be parsed
     */
    private void addMember(String command, char plan) {
        Member newMember;
        if (plan == 'S')
            newMember = new Member(command);
        else if (plan == 'F')
            newMember = new Family(command);
        else if(plan == 'P')
            newMember = new Premium(command);
        else {
            System.out.println("A" + plan + " is an invalid command!");
            return;
        }

        String fullname = newMember.getFname() + " " + newMember.getLname();
        if (!this.checkDateValidity(newMember))
            return;
        if (!this.locationValidity(command.split(" ")[3]))
            return;
        if(this.database.add(newMember))
            System.out.println(fullname + " added.");
        else
            System.out.println(fullname + " is already in the database.");
    }

    /**
     * Removes the member specified
     * @param command The string input to be parsed
     */
    private void removeMember(String command) {
        Member newMember = new Member(command);
        if(!this.database.remove(newMember))
            System.out.println(newMember.getFname() + " " + newMember.getLname() + " is not in the database.");
        else
            System.out.println(newMember.getFname() + " " + newMember.getLname() + " removed.");
    }

    /**
     * Print database specified by operation
     * @param operation the specific operation
     */
    private void printDatabase(String operation) {
        if (this.database.isEmpty()) {
            System.out.println("Member database is empty!");
            return;
        }
        switch (operation) {
            case "P" -> {
                System.out.println("-list of members-");
                this.database.print();
            }
            case "PF" -> {
                System.out.println("-list of members with membership fees-");
                this.database.printWithFees();
            }
            case "PC" -> {
                System.out.println("-list of members sorted by county and zipcode-");
                this.database.printByCounty();
            }
            case "PN" -> {
                System.out.println("-list of members sorted by last name, and first name-");
                this.database.printByName();
            }
            case "PD" -> {
                System.out.println("-list of members sorted by membership expiration date-");
                this.database.printByExpirationDate();
            }
            default -> System.out.println(operation + " is an invalid command!");
        }
    }
    /**
     * Check validity of a member's DOB and expiration date
     * @param newMember The member we will use to check validity of DOB and expiration date
     * @return true if both dates are valid, false otherwise
     */
    private boolean checkDateValidity(Member newMember) {
        String validDOB = newMember.getDob().isValidDOB();
        if (!validDOB.equals("valid")) {
            System.out.println(validDOB);
            return false;
        }
        if (!newMember.getExpire().isValid()) {
            System.out.println("Expiration date " + newMember.getExpire().toString() + ": invalid calendar date!");
            return false;
        }
        return true;
    }

    /**
     * Check if the given location exists
     * @param locale The member's location
     * @return true if the location exists, false otherwise
     */
    private boolean locationValidity(String locale) {
        if (!Location.isLocationValid(locale)) {
            System.out.println(locale + ": invalid location!");
            return false;
        }
        return true;
    }

    /**
     * Load fitnessClass Schedule from "classSchedule.txt" into a ClassSchedule object
     */
    private void loadFitnessClasses() {
        try {
            Scanner scan = new Scanner(new File("loadfiles/classSchedule.txt"));

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    FitnessClass newClass = new FitnessClass();
                    String[] parse = line.split(" ");
                    newClass.setClassName(parse[0].toUpperCase());
                    newClass.setInstructor(parse[1].toUpperCase());
                    newClass.setTime(selectTime(parse[2]));
                    newClass.setLocation(selectLocation(parse[3]));
                    this.classes.addClass(newClass);
                }
            }
            this.classes.printClasses();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Return correct location given string
     * @param location the location as a string
     * @return correct location
     */
    private Location selectLocation(String location) {
        return switch (location.toLowerCase()) {
            case "bridgewater" -> Location.BRIDGEWATER;
            case "piscataway" -> Location.PISCATAWAY;
            case "somerville" -> Location.SOMERVILLE;
            case "franklin" -> Location.FRANKLIN;
            case "edison" -> Location.EDISON;
            default -> null;
        };
    }
    /**
     * Return correct time given string
     * @param time the string interpretation of the time
     * @return correct time
     */
    private Time selectTime(String time) {
        return switch (time) {
            case "morning" -> Time.morning;
            case "afternoon" -> Time.afternoon;
            case "evening" -> Time.evening;
            default -> null;
        };
    }
    /**
     * Attempt to add Member to the fitness class
     * @param fitnessClass the class the member will be added to
     * @param newMember Member to be added
     * @param guest check if guest pass was used or not
     */
    private void addToClass(FitnessClass fitnessClass, Member newMember, Boolean guest) {
        String fullname = newMember.getFname() + " " + newMember.getLname();
        if (!this.validateMember(newMember, fullname, guest, fitnessClass)) {
            return;
        }
        if (this.timeConflict(newMember, fitnessClass.getTime(), fitnessClass))
            return;
        if (guest) {
            if (fitnessClass.addGuest(newMember)) {
                System.out.println(fullname + " (guest) checked in " + fitnessClass.classDetails().toUpperCase());
                ClassSchedule.printMembers(fitnessClass);
                ClassSchedule.printGuests(fitnessClass);}
        } else {
            if (fitnessClass.addMember(newMember)) {
                System.out.println(fullname + " checked in " + fitnessClass.classDetails().toUpperCase());
                ClassSchedule.printMembers(fitnessClass);
                ClassSchedule.printGuests(fitnessClass);
            } else {
                System.out.println(fullname + " already checked in.");
            }
        }
    }
    /**
     * Attempt to remove Member from a fitness class
     * @param fitnessClass the class to remove the member from
     * @param newMember Member to be removed
     * @param guest if it was a guest check in
     */
    private void dropClass(FitnessClass fitnessClass, Member newMember, Boolean guest) {
        String fullname = newMember.getFname() + " " + newMember.getLname();
        if (guest) {
            if(fitnessClass.removeGuest(newMember))
                System.out.println(fullname + " Guest done with the class.");
            else
                System.out.println(fullname + " Guest did not check in.");
        } else {
            if (fitnessClass.removeMember(newMember))
                System.out.println(fullname + " done with the class.");
            else
                System.out.println(fullname + " did not check in.");
        }
    }
    /**
     * Checks if a member abides by membership restrictions
     * @param newMember the member
     * @param fullname fullname of the member
     * @param guest check if it is a quest
     * @param fitnessClass the class it will be added in to
     * @return true if the member abides by membership restrictions
     */
    private boolean validateMember(Member newMember, String fullname, Boolean guest, FitnessClass fitnessClass) {
        //standard membership, no guest pass
        if(guest && !(newMember instanceof Family) && !(newMember instanceof Premium) ) {
            System.out.println("Standard membership - guest check-in is not allowed.");
            return false;
        }
        //check for membership restriction
        if (!(newMember instanceof Family) && !(newMember instanceof Premium)) {
            if (!newMember.getLocation().getName().equalsIgnoreCase(fitnessClass.getLocation().getName())) {
                System.out.println(fullname + " checking in " + fitnessClass.getLocation()
                        + " - standard membership location restriction.");
                return false;
            }
        }
        if (guest) { //guest -> membership location only
            if (!newMember.getLocation().getName().equalsIgnoreCase(fitnessClass.getLocation().getName())) {
                System.out.println(fullname + " Guest checking in " + fitnessClass.getLocation()
                        + " - guest location restriction.");
                return false;
            }
            if(((Family) newMember).getGuestPasses() == 0) {
                System.out.println(fullname + " ran out of guest pass.");
                return false;
            }
        }
        return true;
    }
    /**
     * Attempt to remove Member from a fitness class
     * @param className the name of the fitness Class
     * @param instructor the name of the instructor
     * @param location name of the location
     */
    private FitnessClass createAndValidateClass(String className, String instructor, String location) {
        FitnessClass classToSearch = new FitnessClass();
        classToSearch.setClassName(className);
        classToSearch.setInstructor(instructor);
        if (!this.classes.validateInstructorOrClass(classToSearch)) {
            return null;
        }
        if (!Location.isLocationValid(location)) {
            System.out.println(location + " - invalid location.");
            return null;
        }
        classToSearch.setLocation(selectLocation(location));
        return this.classes.searchForClass(classToSearch);
    }

    /**
     * Checks for a time conflict
     * @param member the member
     * @param time the time of class the member may be added to
     * @param fitnessClass the class the member may be added to
     * @return true if there is a time conflict
     */
    private boolean timeConflict(Member member, Time time, FitnessClass fitnessClass) {
        int length = this.classes.getNumClasses();
        for (int i = 0; i < length; i++) {
            FitnessClass conflictCheck = this.classes.getClasses()[i];
            if (conflictCheck.equals(fitnessClass))
                continue;
            if (!conflictCheck.isInClass(member))
                continue;
            if (conflictCheck.getTime().toString().equalsIgnoreCase(time.toString())) {
                String zip = fitnessClass.getLocation().getZipCode();
                String county = fitnessClass.getLocation().getCounty();
                System.out.println("Time Conflict - "
                + fitnessClass.classDetails() + ", " + zip + ", " + county);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds history members specified from memberList.txt
     */
    private void historyMembers() {
        try {
            Scanner file = new Scanner(new File("loadfiles/memberList.txt"));

            System.out.println("-list of members loaded-");

            while (file.hasNext()) {
                String line = file.nextLine();
                line = line.replaceAll("\\s+", " ");
                String[] fields = (line.trim()).split(" ");
                Member newMember = new Member(
                        fields[0], fields[1], new Date(fields[2]), new Date(fields[3]), fields[4]
                );
                if (this.database.add(newMember))
                    System.out.println(newMember);
            }

            System.out.println("-end of list-");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }
}
