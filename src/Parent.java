import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by x on 1/31/17.
 */
public class Parent extends Guardian{

  public Parent(String name) {
    super(name);
    childList = new HashMap<String, Child>();
  }

  public String getStatus(){
    String returnMe =  this.name + "'s Children:";
    if (childList.size() == 0) {
      returnMe = returnMe + " <none>";
    } else {
      for (Child c : childList.values()){
       returnMe = returnMe + "\n  " + c.getStatus() + " ";
      }
    }
    return returnMe + "\n";
  }

  public Child addChild(String name, String mode){
    if(childList.get(name) == null){
      childList.put(name, new Child(name, mode));
      return childList.get(name);
    }else return null;
  }

  public Child deleteChild(String name){
    /* Because of how this program was designed, there is no way to delete
     * the child from the application's userList
     */
    return childList.remove(name);
  }

  public String redeem(String name){
    if(childList.containsKey(name)){
      Child child = childList.get(name);
      if(child.item == null) return "Error: Child has no item (reward/punishment)!";
      if(child.tokenlist.size() >= child.item.getValue()) {
        String itemType = (child.mode.name() == "Negative") ? "Punishment" : "Reward";
        String out = itemType + " '" + child.item.getKey() + "' has been redeemed for " +
            child.item.getValue() + " tokens";
        for (int i = 0; i < child.item.getValue(); i++) {
          child.tokenlist.remove(0);
        }
        child.item = null;
        return out;
      }else return "Error: Insufficient Tokens To Redeem!";
    }else return "Child does not exist!";
  }
}
