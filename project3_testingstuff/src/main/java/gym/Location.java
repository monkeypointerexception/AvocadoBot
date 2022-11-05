package gym;

/**
 An enumeration of all possible fitness center locations
 @author Sriyaank Vadlamani, Paul Manayath
 */

public enum Location {
    BRIDGEWATER("08807", "SOMERSET", "BRIDGEWATER"),
    EDISON("08837", "MIDDLESEX", "EDISON"),
    FRANKLIN("08873", "SOMERSET", "FRANKLIN"),
    PISCATAWAY("08854", "MIDDLESEX", "PISCATAWAY"),
    SOMERVILLE("08876", "SOMERSET", "SOMERVILLE");

    private final String zipCode;
    private final String county;
    private final String name;

    /**
     The constructor of the Location enum to store the zipcode and county of
     the fitness center location
     @param zipCode the zip code of the fitness center location
     @param county the county of the fitness center location
     @param name the name of the fitness center location
     */
    Location(String zipCode, String county, String name) {
        this.zipCode = zipCode;
        this.county = county;
        this.name = name;
    }

    /**
     Returns the zip code of the fitness center
     @return this.zipCode (zip code of the fitness center)
     */
    String getZipCode() {
        return this.zipCode;
    }
    /**
     Returns the name of the location of the fitness center
     @return this.name (location of the fitness center)
     */
    String getName() {
        return this.name;
    }

    /**
     Returns the county of the fitness center
     @return this.county (county of the fitness center)
     */
    String getCounty() {
        return this.county;
    }

    /**
     Presents all the important details of the location as a string
     @return "{Location name}, {Zip Code}, {County}"
     */
    @Override
    public String toString() {
        return this.name() + ", " + this.getZipCode() + ", " + this.getCounty();
    }

    /**
     * Checks if the location name is valid
     * @param locale the location to be searched for
     * @return true in location is valid, false otherwise
     */
    public static boolean isLocationValid(String locale) {
        for (Location location: Location.values()) {
            if (location.name.equalsIgnoreCase(locale))
                return true;
        }
        return false;
    }
}
