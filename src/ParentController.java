import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ksanc on 2/14/2017.
 */
public class ParentController extends UserController{

  public Parent parent;

  public ParentController(String name) {
    parent = new Parent(name);
    this.user = parent;
    type = true;
  }

  public String menuString(){
    return parent.name + " (Parent)";
  }

  public String updateView(){
    return user.getStatus();
  }

  public Child addChild(String name, String mode){
    return parent.addChild(name, mode);
  }

  public ArrayList<String> childrenNames(){
    ArrayList<String> menuOptions = new ArrayList<>();
    menuOptions.addAll(parent.childList.keySet());
    return menuOptions;
  }

  public String childStatus(String cName){
    String out = "";
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    out = out + child.getStatus() + "\n";
    if(child.item != null){
      out = out + "Item: " + child.item.getKey() +
          "\nCost: " + child.item.getValue() + "\n";
    }
    out = out + "Tokens:\n";
    for (Token t : child.tokenlist){
      out = out + "  * " + t + "\n";
    }
    return out;
  }

  public String changeChildName(String cName, String nName) {
    Child child = parent.childList.remove(cName);
    if (child == null) {return "Child does not exist";}
    child.name = nName;
    parent.childList.put(nName, child);
    return cName + " name was changed to " + child.name;
  }

  public String changeChildMode(String cName, boolean mode) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    child.mode = mode ? Child.Mode.Positive : Child.Mode.Negative;
    return cName + "'s mode was changed to " + child.mode;
  }

  public String deleteChild(String cName) {
    Child child = parent.deleteChild(cName);
    if (child == null) {return "Child does not exist";}
    return " Deleted " + child.name + " from " + parent.name;
  }

  public String giveChildTokens(String cName, int amt, String note) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return parent.addTokens(child.name, amt, note);
  }

  public ArrayList<String> getChildTokens(String cName){
    Child child = parent.childList.get(cName);
    ArrayList<String> tokens = new ArrayList<>();
    if (child == null) {return tokens;}
    for (int i = 0; i < child.tokenlist.size(); i++) {
      tokens.add(child.tokenlist.get(i).note);
    }
    return tokens;
  }

  public String editChildToken(String cName, int tokenIndex, String nNote) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    child.tokenlist.get(tokenIndex - 1).note = nNote;
    return "Token #" + tokenIndex + " note updated to " + nNote;
  }

  public String deleteChildToken(String cName, int tokenIndex) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return child.deleteToken(tokenIndex);
  }

  public String addChildItem(String cName, String iName, int itemVal) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return child.addItem(iName, itemVal);
  }

  public String getChildInterval(String cName) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return "Current Token Interval: " + child.scheduleTokenInterval +
                " Seconds\nTokens Added Every Interval:" + child.scheduleTokenAmt;
  }

  public String setChildInterval(String cName, long interval, int amt) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return child.editSchduledTokens(interval, amt);
  }

  public String redeemChildItem(String cName) {
    Child child = parent.childList.get(cName);
    if (child == null) {return "Child does not exist";}
    return parent.redeem(cName);
  }

}
