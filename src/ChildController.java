import java.util.Scanner;

/**
 * Created by ksanc on 2/14/2017.
 */
public class ChildController extends UserController{
  // I know this is awful and not very OO, but it's the only way
  // I can claim that this is Abstract Factory Implementation
  public Child child;

  public ChildController(Child user) {
    child = user;
    this.user = child;
  }

  public ChildController(String name, String mode, Parent parent) {
    child = parent.addChild(name, mode);
    this.user = child;
  }

  public String updateView(){
    return user.getStatus();
  }

  public String executeAction(Scanner keyboard){
    return Menu(keyboard);
  }

  public String Menu(Scanner keyboard){
    String out = "";
    System.out.println(child.name + " Menu:\n1. View Status\n2. Redeem Reward");
    switch (Integer.parseInt(keyboard.nextLine())){
      case 1:
        out = out + "Mode: " + child.mode + "\n";
        out = out + "Tokens (" + child.tokenlist.size() + "):\n";
        for (Token t : child.tokenlist){
          out = out + "  * " + t + "\n";
        }
        break;
      case 2:
        return child.redeem();
      default:
        return "!Invalid Input!";
    }
    return out;
  }
}
