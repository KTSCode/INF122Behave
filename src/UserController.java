import java.util.*;

/**
 * Created by x on 1/31/17.
 */
public abstract class UserController
{
  public User user;

  public abstract String updateView();

  public abstract String executeAction(Scanner keyboard);
}
