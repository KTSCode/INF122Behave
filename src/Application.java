import java.util.ArrayList;
import java.util.Scanner;

/**
 * Command Line Interface and User Manager for Behave!
 */
public class Application{
  public
  ArrayList<UserController> userList;
  private Scanner keyboard;

  //Constructor
  public Application(){
    userList = new ArrayList<>();
    keyboard = new Scanner(System.in);
    String output = "";
    clearConsole();
    while (output != "exit") {
      output = mainMenu(keyboard);
      clearConsole();
      if (output.length() > 0) {
        System.out.println("************************ Last Action ************************");
        System.out.println(output);
        System.out.println("*************************************************************");
      }
    }
  }
  public final static void clearConsole()
  {
    try{
      final String os = System.getProperty("os.name");
      if (os.contains("Windows")){
        for (int i = 0; i < 1000 ; i++) {
          System.out.println();
        }
        // FIXME figure out how to clear screen on windows
        Runtime.getRuntime().exec("cls");
      }
      else{
        System.out.print("\033[H\033[2J");
        Runtime.getRuntime().exec("clear");
      }
    }
    catch (final Exception e){
      for (int i = 0; i < 1000 ; i++) {
        System.out.println();
      }
    }
  }

  // Gets next line from user and parses it as an integer
  private String getString(Scanner kb) {
    return getString(kb, true);
  }
  private String getString(Scanner kb, boolean cantBeEmpty) {
    String input = kb.nextLine();
    if (input.length() == 0 && cantBeEmpty) {
      System.out.println("Input cannot be blank Please Try again");
      return getString(kb, true);
    }
    return input;
  }
  private String getString(Scanner kb, String msg){
    System.out.println(msg + ":");
    return getString(kb);
  }

  // Gets next line from user and parses it as an integer
  private int getInt(Scanner kb) {
    try {
      return Integer.parseInt(getString(kb));
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid input, please enter a number:");
      return getInt(kb);
    }
  }
  private int getInt(Scanner kb, String msg){
    System.out.println(msg + ":");
    return getInt(kb);
  }

  // Gets menu input for more than two options
  private int intMenu(Scanner kb, String message, ArrayList<String> options){
    while (true) {
      System.out.println(message + ":");
      for (int i = 0; i < options.size(); i++) {
        System.out.println((i + 1) + ". " + options.get(i));
      }
      int input = getInt(kb);
      if (input > 0 && input <= options.size()){
        return input;
      }else {
        System.out.println("!!!Invalid Option Please Try Again!!!");
      }
    }
  }
  // Gets menu input for two option menu
  private boolean boolMenu(Scanner kb, String msg, String optT, String optF){
    while (true) {
      System.out.println(msg + ":");
      System.out.println("1. " + optT);
      System.out.println("2. " + optF);
      int input = getInt(kb);
      if (input == 1) {
        return true;
      }else if (input == 2) {
        return false;
      }else {
        System.out.println("!!!Invalid Option Please Try Again!!!");
      }
    }
  }

  private String userStatus(){
    String toPrint = "";
    // Status update
    if (!userList.isEmpty()) {
      toPrint = "Current User Status:\n";
      for (int i = 0; i < userList.size() && userList.get(i).user.getClass().getName() == "Parent" ; i++) {
        toPrint = toPrint + userList.get(i).updateView();
      }
    }
    return toPrint + "\n";
  }

  public String mainMenu(Scanner kb){
    System.out.print(userStatus());
    // Get all the menu options
    ArrayList<String> menuOptions = new ArrayList<>();
    menuOptions.add("Create New User");
    menuOptions.add("Exit");
    if (!userList.isEmpty()) {
      for (int i = 0; i < userList.size(); i++) {
        menuOptions.add(userList.get(i).menuString());
      }
    }
    int menuOpt = intMenu(kb,
        "Please enter the number of the mainMenu item you would like to select",
        menuOptions);
    switch (menuOpt){
      case 1 :
        boolean uType = boolMenu(kb, "What kind of user would you like to create?",
            "Parent", "Child");
        return createNewUser(uType, kb);
      case 2 :
        return "exit";
      default:
        UserController uc = userList.get(menuOpt - 3);
        return uc.type ? parentMenu(kb, (ParentController) uc) :
            childMenu(kb, (ChildController) uc);
    }
  }

  private String createNewUser(boolean userType, Scanner kb) {
    if (userType) {
      System.out.println("What is the name of your new parent?");
      String name = getString(kb);
      userList.add(new ParentController(name));
      return "New Parent named " + name + " created";
    }
    else {
      if(userList.size() > 0){
        ArrayList<String> pNames = new ArrayList<>();
        for (int i = 0; i < userList.size() &&
            userList.get(i).user.getClass().getName() == "Parent" ; i++) {
          pNames.add(userList.get(i).user.name);
        }
        int pIndex = intMenu(kb, "Who's child is this?", pNames);
        ParentController PC = (ParentController) userList.get(pIndex - 1);
        return createNewChild(kb, PC);
      }
      return "!Cannot create child with no parent!";
    }
  }

  private String createNewChild(Scanner kb, ParentController PC) {
    String output;
    String cName = getString(kb, "What is the child name?");
    boolean mode = boolMenu(kb, "What mode is the child starting in?",
        "Positive", "Negative");
    Child newChild = PC.addChild(cName, mode ? "Positive" : "Negative" );
    if (newChild == null) {
      output = "Child with that name already exists";
    } else {
      userList.add(new ChildController(newChild));
      output = cName + " was created as a child of " + PC.parent.name;
    }
    return output;
  }

  // Menu parent sees
  private String parentMenu(Scanner kb, ParentController PC){
    ArrayList<String> menuStrings = new ArrayList<>();
    menuStrings.add("Add Child");
    menuStrings.add("Exit Program");
    menuStrings.addAll(PC.childrenNames());

    int menuOpt = intMenu(kb, "Parent Menu", menuStrings);
    switch (menuOpt){
      case 1:
        return createNewChild(kb, PC);
      case 2:
        return "exit";
      default:
        return parentChildMenu(kb, PC, menuStrings.get(menuOpt - 1));
    }
  }
  // Menu parent sees after selecting a child
  private String parentChildMenu(Scanner kb, ParentController PC, String cName){
    ArrayList<String> menuStrs = new ArrayList<>();
    menuStrs.add("Status");
    menuStrs.add("Edit");
    menuStrs.add("Give Token");
    menuStrs.add("Edit Token");
    menuStrs.add("Add Item (Reward/Punishment)");
    menuStrs.add("Schedule Token Menu");
    menuStrs.add("Redeem Item (Punishment/Reward)");
    int menuOpt = intMenu(kb, "Parent Menu", menuStrs);
    switch (menuOpt){
      case 1: //("Status")
        return PC.childStatus(cName);
      case 2: //("Edit")
        return editChildMenu(kb, PC, cName);
      case 3: //("Give Token")
        return PC.giveChildTokens(cName, getInt(kb, "Enter number of tokens to give"),
             getString(kb,"Enter a note for the token or hit enter to skip"));
      case 4: //("Edit Token")
        return editTokensMenu(kb, PC, cName);
      case 5: //("Add Item (Reward/Punishment)")
        return PC.addChildItem(cName, getString(kb,
            "Enter the title of the new Item (Reward/Punishment)"),
            getInt(kb, "Enter the cost to redeem this Item (Reward/Punishment):"));
      case 6: //("Schedule Token Menu")
        return scheduleTokenMenu(kb, PC, cName);
      case 7: //("Redeem Item (Punishment/Reward)")
        return PC.parent.redeem(cName);
      default:
        return "";
    }
  }

  private String editChildMenu(Scanner kb, ParentController PC, String cName){
    ArrayList<String> menuStrs = new ArrayList<>();
    menuStrs.add("Change Name");
    menuStrs.add("Change Mode");
    menuStrs.add("Delete");
    int menuOpt = intMenu(kb, "Edit " + cName, menuStrs);
    switch (menuOpt) {
      case 1: // Change Name
        return PC.changeChildName(cName, getString(kb, "Enter new name for " + cName));
      case 2: // Change Mode
        return PC.changeChildMode(cName, boolMenu(kb, "Select new mode", "Positive",
            "Negative"));
      case 3: // Delete
        return PC.deleteChild(cName);
      default:
        return "editChildMenu switch statement defaulted";
    }
  }

  private String editTokensMenu(Scanner kb, ParentController PC, String cName){
    int tokenIndex = intMenu(kb, "Edit " + cName, PC.getChildTokens(cName));
    if (boolMenu(kb, "Token Menu", "Change Note", "Delete Token")){
      return PC.editChildToken(cName, tokenIndex,
          getString(kb, "Enter new note for token"));
    } else {
      return PC.deleteChildToken(cName, tokenIndex);
    }
  }

  private String scheduleTokenMenu(Scanner kb, ParentController PC, String cName){
    ArrayList<String> menuStrs = new ArrayList<>();
    menuStrs.add("Schedule Token Interval");
    menuStrs.add("Edit Token Schedule");
    menuStrs.add("Delete Token Schedule");
    int menuOpt = intMenu(kb, "Schedule Token Menu " + cName, menuStrs);
    switch (menuOpt) {
      case 2: // Print current token interval
        PC.getChildInterval(cName);
      case 1: // Schedule Token
        System.out.println("Enter the automatic token interval in seconds " +
            "(1 day = 86400, 1 week = 604800");
        return PC.setChildInterval(cName, Long.parseLong(kb.nextLine()),
            getInt(kb, "Enter the number of tokens you want automatically created " +
                "every interval"));
      case 3: // Delete Token Schedule
        PC.setChildInterval(cName, 0, 0);
        return "Automatic Token Schedule Deleted!";
      default:
        return "scheduleTokenMenu switch statement defaulted";
    }
  }

  // Menu child sees
  private String childMenu(Scanner kb, ChildController CC){
    if (boolMenu(kb, CC.child.name + " Menu", "View Status", "Redeem Reward")){
      return CC.fullStatus();
    } else {
      return CC.child.redeem();
    }
  }

}
