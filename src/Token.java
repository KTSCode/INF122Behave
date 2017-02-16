import java.text.SimpleDateFormat;
import java.util.Date;

public class Token
{
  public
    String note;
    String timeStamp;
  private
    // define a nice date format
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

  public Token(String note){
    this.note = note;
    timeStamp = sdf.format(new Date());
  }

  public Token(Date fakeDate){
    this.note = "Automatically added by token schedule";
    timeStamp = sdf.format(fakeDate);
  }

  public String toString(){
    return "Token Note: " + note + ", Timestamp: " + timeStamp;
  }
}
