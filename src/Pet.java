/*
 * This file contains the Pet class, which keeps track
 * of the information about the pet
 *
 * Programmer: Homey Poon
 *
 */

public class Pet {
    private String name;
    private PetType type;
    private String age;


    /**
     * This method stores the pet's name in the name
     * field.
     *
     * @param name The pet name to be stored in the name field.
     */
    public void setName(String name) {
        this.name = name;
    } // end setName

    /**
     * This method returns the pet's name.
     *
     * @return The value of name field.
     */
    public String getName() {
        return name;
    }

    /**
     * This method stores the pet's age in the age
     * field.
     *
     * @param age The pet name to be stored in the age field.
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * This method returns the pet's name.
     *
     * @return The value of age field.
     */
    public String getAge() {
        return age;
    }

    /**
     * This method stores the pet's type in the type
     * field.
     *
     * @param type The pet type to be stored in the type field.
     */
    public void setType(PetType type) {
        this.type = type;
    }

    /**
     * This method returns the pet's type in the type field.
     *
     * @return The value of type field.
     */
    public PetType getType() {
        return type;
    }

}
