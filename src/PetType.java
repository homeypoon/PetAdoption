/*
 * This file contains the PetType enum classes that represents
 * the available pet types in the game.
 *
 * Programmer: Homey Poon
 */

public enum PetType {

    DOG("Dog"),
    CAT("Cat"),
    BIRD("Bird"),
    HAMSTER("Hamster"),
    FERRET("Ferret");


    // String representation of the pet type
    private final String PET_TYPE_STRING;

    /**
     * Constructor for the PetType enum.
     *
     * @param string The String representation of the pet type.
     */
    PetType(String string) {
        PET_TYPE_STRING = string;
    }

    /**
     * This method overrides the toString method to return the string representation
     * of the pet type.
     *
     * @return The string representation of the pet type.
     */
    @Override
    public String toString() {
        return PET_TYPE_STRING;
    }


}
