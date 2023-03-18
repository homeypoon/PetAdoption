import java.util.Scanner;
import java.io.*;

/*
    This program is a pet adoption system that allows users to view available pets, file
    adoption applications, and check their adoption application. It also allows staff members to
    input adoption data to add pets to the system.

    Programmer: Homey Poon
 */

public class Main {
    static final int INPUT_TYPE_NAME = 0, INPUT_TYPE_AGE = 1,
            INPUT_TYPE_EMAIL = 2, INPUT_TYPE_PET_TYPE = 3;
    static final String AVAILABLE_PETS_FILE = "availablePets.txt",
            APPLICATION_DATA_FILE = "applicationData.txt";

    static PetType[] petTypes = PetType.values();

    public static void main(String[] args) throws InterruptedException {

        Scanner keyboard = new Scanner(System.in);
        String menuSelection;
        final String CLEAR_CONSOLE = "\033[H\033[2J";

        System.out.print(CLEAR_CONSOLE); // Clear console

        // Display welcoming message
        System.out.println(
                "\nWelcome to my pet adoption system! " +
                        "I hope that you can find a pet to bring home and care " +
                        "for! If you want to file an adoption application, please " +
                        "check out the available pets beforehand and remember your pet's name.");

        do {
            // Display menu and receive user selection
            System.out.println("\nMenu" +
                    "\n\n1) View Available Pets" +
                    "\n2) File Adoption Application" +
                    "\n3) Check Adoption Application" +
                    "\n4) Input Adoption Data (Reserved for Staff)" +
                    "\n5) Exit\n");
            System.out.print("Please enter your selection: ");

            menuSelection = keyboard.nextLine().strip();

            switch (menuSelection) {
                case "1":
                    // View Available Pets

                    System.out.println("Pet Profiles");
                    printPetProfiles();


                    break;
                case "2":
                    // Submit Adoption Application
                    User user = new User();

                    // Display introduction message
                    System.out.println(
                            "\nI'm glad you want to file for pet adoption and provide a home to a pet!" +
                                    " Before we begin the adoption application filing process, " +
                                    "I have to ensure that you are of legal age to adopt a pet.");
                    String userAge = getApplicationData("\nPlease input your age: ", INPUT_TYPE_AGE);

                    if (Integer.parseInt(userAge) >= 18) {
                        user.setAge(userAge);
                    } else {
                        System.out.println("\nUnfortunately, you are required to be at " +
                                "least 18 years old to adopt a pet. Feel free to explore the pet options, though!");
                        System.out.println("You will now return to the main menu...");
                        continue;
                    }

                    System.out.println("Great! You meet the age requirements for pet adoption. You can" +
                            " now proceed to fill out information about yourself and the pet");

                    user.setName(getApplicationData("\nPlease input your name: ", INPUT_TYPE_NAME));
                    user.setEmail(getApplicationData("\nPlease input your email address: ", INPUT_TYPE_EMAIL));

                    do {
                        String petName = getApplicationData("\nPlease input the name of the pet you would like to adopt: ", INPUT_TYPE_NAME);

                        Pet pet = getPetData(petName);

                        if (pet != null) {

                            saveApplicationData(pet, user);

                            System.out.println("\nYou have completed the pet adoption application. " +
                                    "Thank you so much for submitting it! We will review the application" +
                                    " and email you the results within 5 business days. " +
                                    "\nYou will now return to the main menu...");
                            Thread.sleep(2000);
                            break;
                        } else {
                            System.out.println("\nUnfortunately, the pet doesn't exist.");
                            System.out.println("If you would like to try another pet's name, input 1");
                            System.out.println("If you would like to return to the main menu, input 2.");

                            System.out.print("Please input your selection: ");
                            String userSelection = keyboard.nextLine();

                            if (userSelection.equals("1")) {
                                continue;
                            } else if (userSelection.equals("2")) {
                                break;
                            } else {
                                System.out.println("Invalid input! Please try again!");
                            }
                        }

                    } while (true);

                    break;
                case "3":
                    // Check Adoption Application


                    break;
                case "4":
                    // Input Adoption Data (Reserved for Staff)

                    Pet currentPet = new Pet();

                    System.out.println(
                            "\nPlease DO NOT proceed if you are NOT a staff member!");
                    System.out.println(
                            "\nPlease input information about " +
                                    "the pet so that we can input it into our system.");

                    // Prompt staff to input the pet's data
                    currentPet.setName(getApplicationData("\nPlease input the pet's name: ", INPUT_TYPE_NAME));
                    currentPet.setAge(getApplicationData("\nPlease input the pet's age: ", INPUT_TYPE_AGE));

                    String petTypeInput =
                            getApplicationData("\nPlease input the letter " +
                                    "corresponding to the pet's type: ", INPUT_TYPE_PET_TYPE);
                    currentPet.setType(PetType.valueOf(petTypeInput));

                    saveAvailablePet(currentPet); // save current pet to availablePets txt file


                    System.out.println("\nYou have successfully inputted "
                            + currentPet.getName() +
                            "'s data into the system. Thank you for your work!");
                    System.out.println("You will now return to the main menu...");
                    Thread.sleep(1500);

                    break;
                case "5":
                    printConcludingMessage();
                    break;
                default:
                    invalidMenuSelection();
                    break;
            }
        } while (true);
    }

    /**
     * This method prints out each pet's formatted profile by using data from the availablePets txt file
     */
    public static void printPetProfiles() {

        File file = new File(AVAILABLE_PETS_FILE);

        try {
            Scanner scanner = new Scanner(file);

            int petCount = 1;

            while (scanner.hasNextLine()) {
                // Split each line into an array to retrieve each type of peta data
                String[] petData = scanner.nextLine().split(", ");
                String name = petData[0];
                int age = Integer.parseInt(petData[1]);
                PetType type = PetType.valueOf(petData[2]);

                System.out.println("Pet " + petCount++);
                System.out.println("name: " + name);
                System.out.println("age: " + age);
                System.out.println("type: " + type.name().toLowerCase());
                System.out.println();
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method saves the user and pet's data to the progress file.
     *
     * @param pet  The pet whose data is to be saved in the file.
     * @param user The user whose data is to be saved in the file.
     */
    public static void saveApplicationData(Pet pet, User user) {

        try {
            FileWriter fw = new FileWriter(APPLICATION_DATA_FILE, true);
            PrintWriter printWriter = new PrintWriter(fw);

            // Append the user and pet data to the txt file
            printWriter.print(user.getName());
            printWriter.print(", ");
            printWriter.print(user.getEmail());
            printWriter.print(", ");
            printWriter.print(user.getAge());
            printWriter.print(", ");
            printWriter.print(pet.getName());
            printWriter.print(", ");
            printWriter.print(pet.getAge());
            printWriter.print(", ");
            printWriter.print(pet.getType());
            printWriter.println(); // Add line break

            // Close the file
            printWriter.close();

        } catch (IOException e) {
            // Print the stack trace if there is an IOException
            e.printStackTrace();
        }
    }

    /**
     * This method saves the pet's data to the availablePets txt file
     *
     * @param pet The pet whose data is to be saved in the file.
     */
    public static void saveAvailablePet(Pet pet) {

        try {
            FileWriter fw = new FileWriter(AVAILABLE_PETS_FILE, true);
            PrintWriter printWriter = new PrintWriter(fw);

            // Append the pet data to the txt file
            printWriter.println(pet.getName() + ", " + pet.getAge() + ", " + pet.getType().name());

            // Close the file
            printWriter.close();

        } catch (IOException e) {
            // Print the stack trace if there is an IOException
            e.printStackTrace();
        }

    }

    /**
     * This method returns a Pet object based on the pet's name by reading from the availablePets txt file.
     *
     * @param petName The name of the pet whose data should be returned.
     * @return Returns a Pet object based on the pet's name if the pet exists, and returns null if it doesn't.
     */
    public static Pet getPetData(String petName) {

        try {
            File file = new File("pets.txt");
            Scanner scanner = new Scanner(file);

            // Loop through the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");

                // Check if the pet name matches
                if (parts[0].equals(petName)) {
                    Pet pet = new Pet();
                    pet.setName(parts[0]);
                    pet.setAge(parts[1]);
                    pet.setType(PetType.valueOf(parts[2]));

                    scanner.close();
                    return pet;
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method prompts the user form inputted information and returns the inputted data.
     *
     * @param promptMessage The message that prompts the user to input the desired data.
     * @param inputType     The type of input for the desired data.
     * @return The user's inputted data.
     */
    private static String getApplicationData(String promptMessage, int inputType) {
        Scanner scanner = new Scanner(System.in);

        do {

            if (inputType == INPUT_TYPE_PET_TYPE) {
                // Print out menu for pet types
                System.out.println("\nHere is the pet types menu:");
                for (int i = 0; i < petTypes.length; i++) {
                    char optionLetter = (char) ('a' + i);
                    System.out.println("\t" + optionLetter + ") " + petTypes[i]);
                }
            }
            System.out.print(promptMessage);

            String userInput = scanner.nextLine().strip();

            if (userInput.equals("")) {
                System.out.println("Invalid input! Please try again and ensure that your input is not empty.");
            } else if (inputType == INPUT_TYPE_NAME) {
                return userInput;
            } else if (inputType == INPUT_TYPE_AGE) {
                // Check if it is an int
                try {
                    int intAge = Integer.parseInt(userInput);

                    // Check if the age value is 0 or greater
                    if (intAge >= 0) {
                        return userInput;
                    } else {
                        System.out.println("Invalid input! Please try again and ensure that your age input is positive.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please try again and ensure that your age input is an integer.");
                }
            } else if (inputType == INPUT_TYPE_EMAIL) {
                for (int i = 0; i < userInput.length(); i++) {
                    if (userInput.charAt(i) == '@') {
                        return userInput;
                    }
                }
                System.out.println("Invalid input! Please try again and ensure that your input is an email address.");

            } else if (inputType == INPUT_TYPE_PET_TYPE) {
                switch (userInput) {
                    case "a":
                        return PetType.DOG.name();
                    case "b":
                        return PetType.CAT.name();
                    case "c":
                        return PetType.BIRD.name();
                    case "d":
                        return PetType.HAMSTER.name();
                    case "e":
                        return PetType.FERRET.name();
                    default:
                        System.out.println("Invalid input! Please try again and ensure that your input is one of the choices.");

                }
            }
        } while (true);

    }

    /**
     * This method prints a concluding message when the user selects to exit the program.
     */
    private static void printConcludingMessage() {
        // Print concluding message
        System.out.println("\n\nThank you for using my pet adoption system. You will now exit the program. Have a great day!");

        // Save progress and exit program
//            currentPet.savePetProgress();
        System.exit(0);
    }

    /**
     * This method prints an error message when the user enters an invalid menu selection.
     */
    private static void invalidMenuSelection() throws InterruptedException {
        // Print error message
        System.out.println("\n\nYour menu selection was invalid. Please try again!");
        Thread.sleep(1500);
    }


}