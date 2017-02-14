import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by x on 2/2/17.
 */
public class Application{
  public
  ArrayList<UserController> userList;
  Scanner keyboard;

  //Constructor
  public Application(){
    userList = new ArrayList<UserController>();
    keyboard = new Scanner(System.in);
    String output = "";
    while (output != "exit") {
      output = mainMenu(keyboard);
      //TODO add a clear screen function here
      System.out.println(output);
    }
  }

  public int intMenu(Scanner keyboard, String message, String[] options){
    while (true) {
      System.out.println(message + ":");
      for (int i = 0; i < options.length; i++) {
        System.out.println((i + 1) + ". " + options[i]);
      }
      int input = Integer.parseInt(keyboard.nextLine());
      if (input > 0 && input <= options.length){
        return input;
      }else {
        System.out.println("!!!Invalid Option Please Try Again!!!");
      }
    }
  }

  public String mainMenu(Scanner keyboard){
    System.out.println(userStatus());
    String[] menuOptions = new String[userList.size() + 2];
    menuOptions[0] = "Create New User";
    menuOptions[0] = "Exit";
    if (!userList.isEmpty()) {
      for (int i = 2; i < userList.size() + 2 ; i++) {
        menuOptions[i] = userList.get(i).user.name + "("
            + userList.get(i).user.getClass().getName() + ")";
      }
    }
    int menuOpt = intMenu(keyboard,
        "Please enter the number of the mainMenu item you would like to select", menuOptions);
    switch (menuOpt){
      case 1 :
        System.out.println("What kind of user would you like to create?");
        System.out.println("1.Parent");
        System.out.println("2.Child");
        createNewUser(Integer.parseInt(keyboard.nextLine()));
        break;
      case 2 :
        return "exit";
      default:
        if (menuOpt > 2 && menuOpt <= (userList.size() + 2)){
          return executeInput(userList.get(menuOpt - 3), keyboard);
        }else {return "!Invalid Input!!!!\n";}
    }
    return "";
  }

  public String getInput(){
    return keyboard.nextLine();
  }


  public String userStatus(){
    String toPrint = "";
    // Status update
    if (!userList.isEmpty()) {
      toPrint = "Status:\n";
      for (int i = 0; i < userList.size() && userList.get(i).user.getClass().getName() == "Parent" ; i++) {
        toPrint = toPrint + userList.get(i).updateView();
      }
    }
    //Initial User Menu: Create new User adopt existing User
    toPrint = toPrint + "Please enter the number of the mainMenu item you would like to select:\n";
    toPrint = toPrint + ("1. Create New User\n");
    toPrint = toPrint + ("2. Exit \n");
    // list existing users
    if (!userList.isEmpty()) {
      //print out
      for (int i = 0; i < userList.size() ; i++) {
        toPrint = toPrint + (i + 3) + ". " + userList.get(i).user.name + "("
            + userList.get(i).user.getClass().getName() + ")\n";
      }
    }
    //Prints user info if any
    return toPrint;
  }

  //Interfaces with UserController, and handles the logic for performing user actions
  public String executeInput(UserController uc, Scanner kb){
    return uc.executeAction(kb);
  }

  private void createNewUser(int userType) {
    User newUser;
    if (userType == 1) {
      System.out.println("What is the name of your new user?");
      newUser = new Parent(getInput());
    } else if (userType == 2) {
      if(userList.size() > 0){
        System.out.println("Who's child is this?");
        for (int i = 0; i < userList.size() && userList.get(i).user.getClass().getName() == "Parent" ; i++) {
          System.out.println( (i + 1) + ". " + userList.get(i).user.name + "\n");
        }
        Parent rent = (Parent) userList.get(Integer.parseInt(getInput()) - 1).user;
        System.out.println("What is the child name?");
        String cname = getInput();
        if(!rent.childList.containsKey(cname)) {
          System.out.println("What mode is the child starting in? \n1.Positive\n2.Negative");
          switch (Integer.parseInt(getInput())) {
            case 1:
              newUser = rent.addChild(cname, "Positive");
              break;
            case 2:
              newUser = rent.addChild(cname, "Negative");
              break;
            default:
              System.out.println("!Invalid Input!");
              return;
          }
        }else {
          System.out.println(cname + "added as user");
          newUser = rent.childList.get(cname);
        }
      }else {
        System.out.println("!Invalid Input!");
        return;
      }
    } else {
      System.out.println("!Invalid Input!");
      return;
    }
    UserController uc = new UserController(newUser);
    userList.add(uc);
  }
}
