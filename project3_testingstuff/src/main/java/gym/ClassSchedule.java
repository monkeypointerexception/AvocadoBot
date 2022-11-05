package gym;

/**
 A class used to keep track of the all fitness classes
 @author Sriyaank Vadlamani, Paul Manayath
 */
public class ClassSchedule {
    private FitnessClass [] classes;
    private int numClasses;

    /**
     * Constructor to create ClassSchedule Object
     */
    public ClassSchedule() {
        this.numClasses = 0;
        this.classes = new FitnessClass [15];

    }

    /**
     * Adds a fitness class into the fitness class array
     * @param fitnessClass the class to add into the array
     */
    public void addClass(FitnessClass fitnessClass) {
        classes[numClasses] =  fitnessClass;
        this.numClasses += 1;
    }

    /**
     * Prints all available fitness classses as well as its members
     */
    public void printClasses() {
        if (this.numClasses == 0) {
            System.out.println("Fitness class schedule is empty.");
            return;
        }
        System.out.println("-Fitness classes-");
        for(FitnessClass fitnessClass: classes) {
            System.out.println(fitnessClass.classDetails());
            printMembers(fitnessClass);
            printGuests(fitnessClass);
        }
        System.out.println("-end of class list.");
    }
    /**
     * Prints guests from a fitness class
     * @param fitnessClass the guests of class to be printed
     */
    public static void printGuests(FitnessClass fitnessClass) {
        if(fitnessClass.guests.isEmpty())
            return;
        System.out.println("- Guests -");
        for (Member member: fitnessClass.guests) {
            System.out.println("\t" + member);
        }
    }
    /**
     * Prints members from a fitness class
     * @param fitnessClass the members of class to be printed
     */
    public static void printMembers(FitnessClass fitnessClass) {
        if(fitnessClass.members.isEmpty())
            return;
        System.out.println("- Participants -");
        for (Member member: fitnessClass.members) {
            System.out.println("\t" + member);
        }
    }

    /**
     * Returns the class schedule
     * @return class schedule
     */
    public FitnessClass[] getClasses() {return this.classes;}

    /**
     * Gets the number of fitness classes
     * @return numClasses
     */
    public int getNumClasses() {return this.numClasses;}

    /** Checks if input given is a valid class
     * @param fitnessClass the class to be validated
     * @return true if instructor and class exists
     */
    public boolean validateInstructorOrClass(FitnessClass fitnessClass) {
        boolean classExists = false;
        boolean instructorExists = false;
        for (FitnessClass classCheck: this.classes) {
            if (classCheck.getInstructor().equalsIgnoreCase(fitnessClass.getInstructor())) {
                instructorExists = true;
            }
            if (classCheck.getClassName().equalsIgnoreCase(fitnessClass.getClassName())) {
                classExists = true;
            }
        }
        if (classExists && instructorExists)
            return true;
        if (!classExists)
            System.out.println(fitnessClass.getClassName() + " - class does not exist.");
        if (!instructorExists)
            System.out.println(fitnessClass.getInstructor() + " - instructor does not exist.");
        return false;
    }

    /**
     * Searches for an instance of a specific fitness class
     * @param classToFind the class to Find in the array
     * @return the specific fitness class
     */
    public FitnessClass searchForClass(FitnessClass classToFind) {
        boolean foundClass;
        boolean foundInstructor;
        boolean foundLocation;

        for (FitnessClass fitnessclass: this.classes) {
            foundClass = false; foundInstructor = false; foundLocation = false;
            if (fitnessclass == null)
                continue;
            if ((fitnessclass.getClassName().equalsIgnoreCase(classToFind.getClassName())))
                foundClass = true;
            if (fitnessclass.getInstructor().equalsIgnoreCase(classToFind.getInstructor()))
                foundInstructor = true;
            if (fitnessclass.getLocation().getName().equalsIgnoreCase(classToFind.getLocation().getName()))
                foundLocation = true;
            if (foundClass && foundInstructor && foundLocation)
                return fitnessclass;
        }
        return null;
    }
}
