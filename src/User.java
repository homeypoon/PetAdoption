/*
 * This file contains the Pet class, which keeps track
 * of the information about the pet
 *
 * Programmer: Homey Poon
 *
 */

public class User {
    private String name;
    private String email;
    private String age;


    /**
     * This method stores the user's name in the name field.
     *
     * @param name The user's name to be stored in the name field.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the user's name.
     *
     * @return The value of name field.
     */
    public String getName() {
        return name;
    }

    /**
     * This method stores the user's email in the email
     * field.
     *
     * @param email The user's email to be stored in the email field.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method returns the user's email in the email field.
     *
     * @return The value of email field.
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method stores the user's age in the email
     * field.
     *
     * @param age The pet name to be stored in the age field.
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * This method returns the user's age.
     *
     * @return The value of age field.
     */
    public String getAge() {
        return age;
    }


}
