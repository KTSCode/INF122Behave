import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by x on 1/31/17.
 */
public class Child extends User{

  public enum Mode {Positive, Negative}

  public
    Mode mode;
    ArrayList<Token> tokenlist;
    Pair<String, Integer> item;
    int scheduleTokenAmt;
    long scheduleTokenInterval;
    Date lastUpdated;
    TokenHistory history;

  public Child(String name, String mode) {
    super(name);
    switch (mode){
      case "Positive":
        this.mode = Mode.Positive;
        break;
      case "Negative":
        this.mode = Mode.Negative;
        break;
      default: //defaults to positive
        this.mode = Mode.Positive;
    }
    tokenlist = new ArrayList<>();
    item = null;
    lastUpdated = new Date();
  }

  public String addTokens(int amt, String note){
    for (int i = 0; i < amt ; i++) {
      tokenlist.add(new Token(note));
    }
    return amt + " new tokens given to " + name;
  }

  public String deleteToken(int tokenIndex){
    tokenlist.remove(tokenIndex - 1);
    return "Token #" + (tokenIndex + 1) + " deleted";
  }

  public void updateTokens(){
    if(scheduleTokenInterval > 0) {
      Date curDate = new Date();
      while (Math.floor((curDate.getTime() - lastUpdated.getTime()) / 1000) >= scheduleTokenInterval && scheduleTokenAmt > 0) {
        lastUpdated.setTime(lastUpdated.getTime() + (scheduleTokenInterval * 1000));
        tokenlist.add(new Token(lastUpdated));
      }
    }
  }

  public String getStatus(){
    updateTokens();
    return this.name + "(Mode: " + mode + ", Tokens: " + tokenlist.size() + ")";
  }

  public String addItem(String name, int amt){
    item = new Pair<>(name, amt);
    return "New Item: " + name + "\nCost: " + amt;
  }

  public String redeem(){
    if(item == null) return "Error: You have no item (reward/punishment)!";
    if(mode.name() == "Positive"){
      if(tokenlist.size() >= item.getValue()) {
        String out = "Reward '" + item.getKey() + "' redeemed for " +
            item.getValue() + " tokens";
        for (int i = 0; i < item.getValue(); i++) {
          tokenlist.remove(0);
        }
        item = null;
        return out;
      }else return "Error: Insufficient Tokens To Redeem!";
    }else return "Children cannot redeem punishments";
  }

  public String editSchduledTokens(long interval, int amt){
    scheduleTokenInterval = (interval > 0) ? interval : 0;
    scheduleTokenAmt = (amt > 0) ? amt : 0;
     return scheduleTokenAmt + " Tokens will now be added every " +
         scheduleTokenInterval + " seconds";
  }

}

