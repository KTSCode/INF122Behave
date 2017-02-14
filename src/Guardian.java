import java.util.HashMap;
import java.util.Map;

/**
 * Created by x on 1/31/17.
 */
public class Guardian extends User{
  public
  HashMap<String, Child> childList;

  public Guardian(String name) {
    super(name);
  }

  public String addTokens(String cname, int amt, String note){
    return childList.get(cname).addTokens(amt, note);
  }
}
