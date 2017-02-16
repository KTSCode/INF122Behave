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
    type = false;
  }

  public ChildController(String name, String mode, Parent parent) {
    child = parent.addChild(name, mode);
    this.user = child;
  }

  public String updateView(){
    return user.getStatus();
  }

  public String fullStatus(){
    String out = "";
    out = out + "Mode: " + child.mode + "\n";
    out = out + "Tokens (" + child.tokenlist.size() + "):\n";
    for (Token t : child.tokenlist){
      out = out + "  * " + t + "\n";
    }
    return out;
  }

  public String menuString(){
    return child.name + " (Child)";
  }
}
