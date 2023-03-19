import java.util.*;
import java.io.*;

/*
    This program is a pet adoption system that allows users to view available pets, file
    adoption applications, and check their adoption application. It also allows staff members to
    input adoption data to add pets to the system.

    Programmer: Homey Poon
 */

public class Main {
    static final int INPUT_TYPE_DUPLICABLE_NAME = 0, INPUT_TYPE_NOT_DUPLICABLE_NAME = 1, INPUT_TYPE_AGE = 2,
            INPUT_TYPE_EMAIL = 3, INPUT_TYPE_PET_TYPE = 4;
    static final String AVAILABLE_PETS_FILE = "availablePets.txt",
            APPLICATION_DATA_FILE = "applicationData.txt";
    static PetType[] petTypes = PetType.values();

    public static void main(String[] args) throws InterruptedException {

        Scanner keyboard = new Scanner(System.in);
        String menuSelection;
        final String CLEAR_CONSOLE = "\033[H\033[2J";

        System.out.print(CLEAR_CONSOLE); // Clear console

        File availablePetsFile = new File(AVAILABLE_PETS_FILE);
        if (!availablePetsFile.exists()) {
            try {
                availablePetsFile.createNewFile();
            } catch (IOException e) {
                System.out.println("There was an error creating a necessary file. Please contact a staff member for tech support.");
                System.out.println("The program will exit now.");

                System.exit(0);
            }
        }

        File applicationDataFile = new File(APPLICATION_DATA_FILE);
        if (!applicationDataFile.exists()) {
            try {
                applicationDataFile.createNewFile();
            } catch (IOException e) {
                System.out.println("There was an error creating a necessary file. Please contact a staff member for tech support.");
                System.out.println("The program will exit now.");

                System.exit(0);
            }
        }

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
                    "\n3) Check Adoption Application Receipt" +
                    "\n4) Input Adoption Data (Reserved For Staff ONLY)" +
                    "\n5) Exit\n");
            System.out.print("Please enter your selection: ");

            menuSelection = keyboard.nextLine().strip();

            switch (menuSelection) {
                case "1":
                    // View Available Pets
                    String returnToMenuSelection;

                    // Print pet profiles
                    System.out.println("\nPet Profiles");
                    printPetProfiles();

                    // Return user to main menu when they press 0
                    do {
                        System.out.print("\nType 0 when you're ready to return to the main menu: ");
                        returnToMenuSelection = keyboard.nextLine().strip();

                        System.out.println("Invalid input! Please try again!");

                    } while (!returnToMenuSelection.equals("0"));

                    System.out.println("\nYou will now return to the main menu...");
                    Thread.sleep(800);

                    break;
                case "2":
                    // Submit Adoption Application
                    User user = new User();

                    // Display introduction message
                    System.out.println(
                            "\nI'm glad you want to file for pet adoption and provide a home to a pet!");
                    System.out.println("Before we begin the adoption application filing process, I have" +
                            " to ensure that you are of legal age to adopt a pet.");
                    String userAge = getApplicationData("\nPlease input your age: ", INPUT_TYPE_AGE);

                    if (Integer.parseInt(userAge) >= 18) {
                        user.setAge(userAge);
                    } else {
                        System.out.println("\nUnfortunately, you are required to be at " +
                                "least 18 years old to adopt a pet. Feel free to explore the pet profiles, though!");
                        System.out.println("You will now return to the main menu...");
                        Thread.sleep(1200);
                        continue;
                    }

                    System.out.println("Great! You meet the age requirements for pet adoption. You can" +
                            " now proceed to fill out information about yourself and the pet you would like to adopt.");

                    user.setName(getApplicationData("\nPlease input your name: ", INPUT_TYPE_DUPLICABLE_NAME));
                    user.setEmail(getApplicationData("\nPlease input your email address: ", INPUT_TYPE_EMAIL));

                    do {

                        String petName = getApplicationData("\nPlease input the name of the pet you would like to adopt: ", INPUT_TYPE_DUPLICABLE_NAME);

                        Pet pet = getPetData(petName);

                        System.out.println("pet: " + getPetData(petName));


                        if (pet != null) {

                            saveApplicationData(pet, user);
                            removePetFromAvailablePets(petName);

                            System.out.println("\nYou have completed the pet adoption application. " +
                                    "Thank you so much for submitting it! We will review the application" +
                                    " and email you the results within 5 business days. " +
                                    "\nYou will now return to the main menu...");
                            Thread.sleep(2000);
                            break;
                        } else {
                            System.out.println("\nUnfortunately, the pet doesn't exist.");
                            System.out.println("\nIf you would like to try another pet's name, input 1");
                            System.out.println("If you would like to return to the main menu, input 2.");

                            String userSelection;

                            do {
                                System.out.print("\nPlease input your selection: ");

                                userSelection = keyboard.nextLine();

                                if (userSelection.equals("1")) {
                                    break;
                                } else if (userSelection.equals("2")) {
                                    break;
                                } else {
                                    System.out.println("Invalid input! Please try again!");
                                }
                            } while (true);

                            // Return to main menu if user selects 2
                            if (userSelection.equals("2")) {
                                break;
                            }
                        }

                    } while (true);

                    break;
                case "3":
                    // Check Adoption Application Receipt
                    String returnToMenuSelection2;

                    System.out.println("To show you your application receipt, we will need your name.");

                    do {
                        String userName = getApplicationData("\nPlease input your name: ", INPUT_TYPE_DUPLICABLE_NAME);

                        User currentUser = getUserData(userName);

                        if (currentUser != null) {

                            // Print user profile and pet adoption application(s)
                            ApplicationReceipt applicationReceipt = getApplicationReceiptData(userName.strip().toLowerCase());
                            printApplicationReceipt(applicationReceipt);

                            // Return user to main menu when they press 0
                            do {
                                System.out.print("\nType 0 when you're ready to return to the main menu: ");
                                returnToMenuSelection2 = keyboard.nextLine().strip();

                                if (returnToMenuSelection2.equals("0")) {
                                    break;
                                }

                                System.out.println("Invalid input! Please try again!");

                            } while (true);

                            System.out.println("\nYou will now return to the main menu...");
                            Thread.sleep(800);
                            break;

                        } else {
                            System.out.println("\nUnfortunately, a user with the inputted name doesn't exist.");
                            System.out.println("\nIf you would like to try another name, input 1");
                            System.out.println("If you would like to return to the main menu, input 2.");

                            String userSelection;

                            do {
                                System.out.print("\nPlease input your selection: ");

                                userSelection = keyboard.nextLine();

                                if (userSelection.equals("1")) {
                                    break;
                                } else if (userSelection.equals("2")) {
                                    break;
                                } else {
                                    System.out.println("Invalid input! Please try again!");
                                }
                            } while (true);

                            // Return to main menu if user selects 2
                            if (userSelection.equals("2")) {
                                break;
                            }
                        }
                    } while (true);

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
                    currentPet.setName(getApplicationData("\nPlease input the pet's name: ", INPUT_TYPE_NOT_DUPLICABLE_NAME));
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
     * This method uses the receiptDataMap to prints out a formatted adoption
     * application receipt with the user's name and pet(s)
     *
     * @param applicationReceipt This is used to get data that should be included in the receipt.
     */
    public static void printApplicationReceipt(ApplicationReceipt applicationReceipt) {

        Map<User, List<Pet>> receiptMap = applicationReceipt.getReceiptMap();

        // Get the user and list of their pets
        User user = applicationReceipt.getUser();
        List<Pet> pets = receiptMap.get(applicationReceipt.getUser());

        System.out.println("\nAdoption Application Receipt for " + user.getName() + "");

        System.out.println("\nUser Information");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Age: " + user.getAge());

        System.out.println("\nPet Adoption Requests Submitted for Review");

        // Check if the user had submitted an applications for any pets
        if (pets.isEmpty()) {
            System.out.println("We have not received an adoption application from you.");
            System.out.println("Please contact a staff member if you believe there has been a processing error.");
        } else {
            // Print profile for every pet
            int petCount = 1;
            for (Pet pet : pets) {
                System.out.println("Pet Adoption Request" + petCount++);
                System.out.println("Pet Name: " + pet.getName());
                System.out.println("Pet Age: " + pet.getAge());
                System.out.println("Pet Type: " + pet.getType());
            }
        }

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
                System.out.println();

                String[] petData = scanner.nextLine().split(", ");
                String name = petData[0];
                int age = Integer.parseInt(petData[1]);
                PetType type = PetType.valueOf(petData[2]);

                System.out.println("Pet " + petCount++);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Type: " + type);
            }

            if (petCount == 1) {
                System.out.println("We do not have any animals currently in our system.");
                System.out.println("Please contact a staff member for more information.");
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
            printWriter.println(user.getName() + ", " + user.getEmail() + ", " + user.getAge()
                    + pet.getName() + ", " + pet.getAge() + ", " + pet.getType().name());

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
     * This method saves the pet's data to the availablePets txt file
     *
     * @param petName The name of the pet whose data should be removed.
     */
    public static void removePetFromAvailablePets(String petName) {

        try {
            // Read the pet data from the file and each pet to the ArrayList
            List<Pet> pets = new ArrayList<>();
            Scanner scanner = new Scanner(new File(AVAILABLE_PETS_FILE));

            while (scanner.hasNextLine()) {
                String[] petData = scanner.nextLine().split(", ");

                Pet currentPet = new Pet();
                currentPet.setName(petData[0]);
                currentPet.setAge(petData[1]);
                currentPet.setType(PetType.valueOf(petData[2]));
                pets.add(currentPet);
            }
            scanner.close();

            // Remove the Pet object with the petName
            pets.removeIf(pet -> pet.getName().equalsIgnoreCase(petName));

            // Write the remaining Pet data in the ArrayList back to the file
            FileWriter fw = new FileWriter(AVAILABLE_PETS_FILE);
            PrintWriter printWriter = new PrintWriter(fw);
            for (Pet pet : pets) {
                printWriter.println(pet.getName() + ", " + pet.getAge() + ", " + pet.getType().name());
            }
            printWriter.close();

        } catch (IOException e) {
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
            File file = new File(AVAILABLE_PETS_FILE);
            Scanner scanner = new Scanner(file);

            // Loop through the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] petData = line.split(", ");

                // Check if the pet name matches
                if (petData[0].equals(petName)) {
                    Pet pet = new Pet();
                    pet.setName(petData[0]);
                    pet.setAge(petData[1]);
                    pet.setType(PetType.valueOf(petData[2]));

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
     * This method returns a User object based on the user's name by reading from the applicationData txt file.
     *
     * @param userName The name of the user whose data should be returned.
     * @return Returns a User object based on the user's name if the user exists, and returns null if it doesn't.
     */
    public static User getUserData(String userName) {

        try {
            File file = new File(APPLICATION_DATA_FILE);
            Scanner scanner = new Scanner(file);

            // Loop through the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(", ");

                // Check if the pet name matches
                if (userData[0].equals(userName)) {
                    User user = new User();
                    user.setName(userData[0]);
                    user.setEmail(userData[1]);
                    user.setAge(userData[2]);

                    scanner.close();
                    return user;
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method returns the user's profile and pets they have
     * submitted applications for by reading from the applicationData txt file.
     *
     * @param userName The name of the user whose data should be returned.
     * @return Returns an ApplicationReceipt object receipt that holds data for the application receipt.
     */
    public static ApplicationReceipt getApplicationReceiptData(String userName) {

        Map<User, List<Pet>> receiptDataMap = new HashMap<>();
        User user = new User();
        List<Pet> pets = new ArrayList<>();


        try {
            File file = new File(APPLICATION_DATA_FILE);
            Scanner scanner = new Scanner(file);

            // Loop through the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] applicationData = line.split(", ");

                // Check if the user's name matches
                if (applicationData[0].equals(userName)) {
                    // Set the data
                    user.setName(applicationData[0]);
                    user.setEmail(applicationData[1]);
                    user.setAge(applicationData[2]);

                    Pet pet = new Pet();
                    pet.setName(applicationData[3]);
                    pet.setAge(applicationData[4]);
                    pet.setType(PetType.valueOf(applicationData[5]));
                    pets.add(pet);
                }
            }

            scanner.close();

            // Add the User and Pet objects to the map
            receiptDataMap.put(user, pets);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new ApplicationReceipt(user, receiptDataMap);

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
            } else if (inputType == INPUT_TYPE_DUPLICABLE_NAME) {
                return userInput;
            } else if (inputType == INPUT_TYPE_NOT_DUPLICABLE_NAME) {
                if (getPetData(userInput) == null) {
                    return userInput;
                } else {
                    System.out.println("Sorry, this pet name is taken. Please try another one!");
                }
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