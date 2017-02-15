import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ksanc on 2/14/2017.
 */
public class ParentController extends UserController{

  public Parent parent;

  public ParentController(Parent user) {
    parent = (Parent) user;
    this.user = parent;
  }

  public ParentController(String name) {
    parent = new Parent(name);
    this.user = parent;
  }

  public String updateView(){
    return user.getStatus();
  }

  public String executeAction(Scanner keyboard){
    String out = "";
    // Check type of User
    ArrayList<Child> children = new ArrayList<>(parent.childList.values());
    System.out.println("Parent Menu:\n1. Add Child\n2. Exit");
    for (int i = 1; i < (children.size() + 1); i++) {
      System.out.println((i + 2) + ". " + children.get(i-1).name);
    }
    System.out.println(""); //new line
    int menuOpt = Integer.parseInt(keyboard.nextLine());
    switch (menuOpt){
      case 1: // Add Child FIXME Wet code copied from Create user method
        System.out.println("What is the child name?");
        String cname = keyboard.nextLine();
        System.out.println("What mode is the child starting in? \n1.Positive\n2.Negative");
        switch (Integer.parseInt(keyboard.nextLine())){
          case 1:
            parent.addChild(cname, "Positive");
            break;
          case 2:
            parent.addChild(cname, "Negative");
            break;
          default:
            return "!Invalid Input!";
        }
        break;
      case 2: //Exit
        break;
      default:
        if(menuOpt > 0 && menuOpt < (children.size() + 3)){
          out = out + parentMenu(parent, children.get(menuOpt - 3), keyboard);
        }else return "!Invalid Input!";
    }
    return out;
  }
  public String parentMenu(Parent rent, Child child, Scanner keyboard){
    String out = "";
    System.out.println(child.name + " Menu:\n1. Status\n2. Edit\n3. Give Token(s)\n" +
        "4. Edit Token\n5. Add Item (Reward/Punishment)\n6. Schedule Token Menu\n" +
        "7. Redeem Item (Punishment/Reward)");
    int menuOpt = Integer.parseInt(keyboard.nextLine());
    switch (menuOpt){
      case 1: //Status
        out = out + child.getStatus() + "\n";
        if(child.item != null){
          out = out + "Item: " + child.item.getKey() +
              "\nCost: " + child.item.getValue() + "\n";
        }
        out = out + "Tokens:\n";
        for (Token t : child.tokenlist){
          out = out + "  * " + t + "\n";
        }
        break;
      case 2: //Edit
        System.out.println("Edit " + child.name + ":\n1. Change Name\n2. Change Mode\n" +
            "3. Delete");
        switch (Integer.parseInt(keyboard.nextLine())){
          case 1: //Change Name
            System.out.println("Enter new name for" + child.name + ":\n");
            child.name = keyboard.nextLine();
            out = out + " Child name changed to " + child.name;
            break;
          case 2: //Change Mode
            System.out.println("Select New Mode:\n1. Positive\n2. Negative");
            switch (Integer.parseInt(keyboard.nextLine())){
              case 1:
                child.mode = Child.Mode.Positive;
                break;
              case 2:
                child.mode = Child.Mode.Negative;
                break;
              default:
                return "!Invalid Input!";
            }
            break;
          case 3: //Delete
            out = out + " Deleted " + rent.deleteChild(child.name).name +
                " from " + rent.name;
            break;
          default:
            return "!Invalid Input!";
        }
        break;
      case 3:  //Give Token(s)
        System.out.println("Enter number of tokens to give:\n");
        int tokenAmt = Integer.parseInt(keyboard.nextLine());
        System.out.println("Enter a note for the token or hit enter to skip:\n");
        String note = keyboard.nextLine();
        out = out + rent.addTokens(child.name, tokenAmt, note);
        break;
      case 4: //Edit Token
        for (int i = 0; i < child.tokenlist.size(); i++) {
          System.out.println((i + 1) + ". " + child.tokenlist.get(i).note);
        }
        int tokeNum = Integer.parseInt(keyboard.nextLine()) - 1;
        System.out.println("\nToken Menu:\n1. Change Note\n 2. Delete Token");
        switch (Integer.parseInt(keyboard.nextLine())){
          case 1: //Change Note
            System.out.println("Enter new note for Token");
            child.tokenlist.get(tokeNum).note = keyboard.nextLine();
            out = out + " Token note updated";
            break;
          case 2: //Delete Token
            child.tokenlist.remove(tokeNum);
            out = out + " Token deleted";
            break;
          default:
            return "!Invalid Input!";
        }
        break;
      case 5: //Add Item
        System.out.println("Enter the title of the new Item (Reward/Punishment):");
        String title = keyboard.nextLine();
        System.out.println("Enter the cost to redeem this Item (Reward/Punishment):");
        out = out + child.addItem(title, Integer.parseInt(keyboard.nextLine()));
        break;
      case 6: //Schedule Token Menu
        System.out.println("Schedule Token Menu:\n1. Schedule Token\n2. Edit Token " +
            "Schedule\n3. Delete Token Schedule");
        switch (Integer.parseInt(keyboard.nextLine())){
          case 2:
            System.out.println("Current Token Interval: " + child.scheduleTokenInterval +
                " Seconds\nTokens Added Every Interval:" + child.scheduleTokenAmt);
          case 1:
            System.out.println("Enter the automatic token interval in seconds " +
                "(1 day = 86400, 1 week = 604800");
            long inteval = Long.parseLong(keyboard.nextLine());
            System.out.println("Enter the number of tokens you want automatically created " +
                "every interval");
            out = out + child.editSchduledTokens(inteval, Integer.parseInt(keyboard.nextLine()));
            break;
          case 3:
            child.editSchduledTokens(0, 0);
            out = out + "Automatic Token Schedule Deleted!";
            break;
          default:
        }
        break;
      case 7:
        out = out + rent.redeem(child.name);
        break;
      default:
        return "!Invalid Input!";
    }
    return out;
  }
}
