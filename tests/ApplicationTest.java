import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test class for my application class
 */
public class ApplicationTest {
  // Input arrayLists
  public
  ArrayList<String> inputArrayList = new ArrayList<>();
  ArrayList<String> Ext = new ArrayList<>();
  ArrayList<String> cParent = new ArrayList<>();
  ArrayList<String> cChild = new ArrayList<>();
  ArrayList<String> cChild2 = new ArrayList<>();
  ArrayList<String> cToken = new ArrayList<>();
  ArrayList<String> pStatus = new ArrayList<>();
  ArrayList<String> cItem = new ArrayList<>();
  ArrayList<String> pRedeem = new ArrayList<>();
  ArrayList<String> eChildMode = new ArrayList<>();
  ArrayList<String> scheduleToken = new ArrayList<>();
  ArrayList<String> cRedeem = new ArrayList<>();
  String Menu = "Please enter the number of the mainMenu item you would like to select:\n1. Create New User\n2. Exit \n";


  // initialize Inputs before each test
  @Before
  public void defineInputs(){
    inputArrayList.clear();//clears input arraylist
    Ext.add("2"); //exits
    // Create a Parent
    cParent.add("1"); //select create user
    cParent.add("1"); //select parent
    cParent.add("Bob"); //enter parent name
    // Create a Child
    cChild.addAll(cParent); // a parent must be created before a child
    cChild.add("1"); //select create user
    cChild.add("2"); //select Child
    cChild.add("1"); //select first parent(bob)
    cChild.add("Jack"); //enter child name
    cChild.add("2"); //mode (Negative)
    // Add another child
    cChild2.addAll(cChild); // create parent and first child
    cChild2.add("3"); //select Bob
    cChild2.add("1"); //select Add Child
    cChild2.add("Sally"); //enter child name
    cChild2.add("1"); //mode (Positive)
    // Give child 2 tokens
    cToken.addAll(cChild); //a child must exist to be given tokens
    cToken.add("3"); //Select Bob
    cToken.add("3"); //Select Jack
    cToken.add("3"); //Select Give Token
    cToken.add("2"); //enter number of tokens
    cToken.add("token note");//note for tokens
    // Add Item (reward/punishment) *Pass to ExecuteInput
    cItem.add("3"); //Select Bob
    cItem.add("3"); //Select Jack
    cItem.add("5"); //Select Add Item
    cItem.add("Time Out"); //set title for item
    cItem.add("2"); //set cost to redeem item
    // Parent Redeem Item
    pRedeem.add("3"); //Select Bob
    pRedeem.add("3"); //Select Jack
    pRedeem.add("7"); //Select Redeem Item
    // Edit Child's Mode
    eChildMode.addAll(cToken); // Creates a parent and a child who is in negative mode
    eChildMode.add("3"); //Select Bob
    eChildMode.add("3"); //Select Jack
    eChildMode.add("2"); //Select Edit
    eChildMode.add("2"); //Select Change Mode
    eChildMode.add("1"); //Select Positive
    // Set Child mode to positive so reward can be redeemed
    cRedeem.add("3"); //Select Jack
    cRedeem.add("2"); //Select Edit
    cRedeem.add("2"); //Select Change Mode
    cRedeem.add("1"); //Select Positive
    // Set up Schedule Token
    scheduleToken.addAll(cChild); //Select Positive
    scheduleToken.add("3"); // Select Bob
    scheduleToken.add("3"); // Select Jack
    scheduleToken.add("6"); // Select Schedule Token Menu
    scheduleToken.add("1"); // Select Schedule Token
    scheduleToken.add("1"); // set interval to 1 second (ain't nobody got time for that)
    scheduleToken.add("1"); // set number of tokens per interva

  }

  // Set System.In buffer to spoof user input
  public void setInput(ArrayList<String> inputArray){
    String input= "";
    for (String x : inputArray){
      input = input + x + "\r";
    }
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
  }

  @org.junit.Test
  public void createParentTest(){
    inputArrayList.addAll(cParent);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    assertEquals("Bob", app.userList.get(0).user.name);
  }

  @org.junit.Test // Parent should be able to add children
  public void createChildTest(){
    inputArrayList.addAll(cChild);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    assertEquals("Jack", app.userList.get(1).user.name);
  }

  @org.junit.Test // Parent should be able to add children
  public void addChildTest(){
    inputArrayList.addAll(cChild2);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    assertEquals("Sally", app.userList.get(2).user.name);
  }

  @org.junit.Test // Parent should be able to edit children
  public void editChildTest(){
    inputArrayList.addAll(cChild); // create parent and child
    inputArrayList.add("3"); //select Bob
    inputArrayList.add("3"); //select Jack
    inputArrayList.add("2"); //select Edit
    inputArrayList.add("1"); //select Change Name
    inputArrayList.add("Jackson"); // enter new child name
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    assertEquals("Jackson", app.userList.get(1).user.name);
  }

  @org.junit.Test // Parent should be able to delete children
  public void deleteChildTest(){
    // Set up test input to create child
    inputArrayList.addAll(cChild);
    inputArrayList.add("3"); // Select Bob
    inputArrayList.add("3"); // Select Jack
    inputArrayList.add("2"); // Select Edit
    inputArrayList.add("3"); // Select Delete
    inputArrayList.addAll(Ext); // Exit Program
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    Parent p = (Parent) app.userList.get(0).user;
    assertEquals(0, p.childList.size());
  }

  @org.junit.Test // Parent should be able to define one mode per child
  public void setChildMode(){
    inputArrayList.addAll(eChildMode);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    Child jack = (Child) app.userList.get(1).user;
    assertEquals("Positive", jack.mode.toString());
  }

  @org.junit.Test // Parent should be able to add tokens
  public void addTokenTest(){
    // Set up test input
    inputArrayList.addAll(cToken);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    // get the time stamp from when the program was run
    String curTimeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
    Child jack = (Child) app.userList.get(1).user;
    assertEquals("Jack", jack.name);
    assertEquals(2, jack.tokenlist.size());
    assertEquals("token note", jack.tokenlist.get(0).note);
    assertEquals(curTimeStamp, jack.tokenlist.get(0).timeStamp);
  }

  @org.junit.Test // Parent should be able to edit tokens
  public void editTokenTest(){
    // Set up test input to edit child's token
    inputArrayList.addAll(cToken); // adds user input to create token
    inputArrayList.add("3"); //Select Bob
    inputArrayList.add("3"); //Select Jack
    inputArrayList.add("4"); //Select Edit Token
    inputArrayList.add("1"); //Select first token
    inputArrayList.add("1"); //Select Change Note
    inputArrayList.add("different token note");//note for tokens
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    Child jack = (Child) app.userList.get(1).user;
    assertEquals("different token note", jack.tokenlist.get(0).note);
  }

  @org.junit.Test // Parent should be able to delete tokens
  public void deleteTokenTest(){
    // Set up test input
    // Delete Child's Token
    inputArrayList.addAll(cToken); //tokens must exist to delete
    inputArrayList.add("3"); //Select Bob
    inputArrayList.add("3"); //Select Jack
    inputArrayList.add("4"); //Select Edit Token
    inputArrayList.add("1"); //Select first token
    inputArrayList.add("2"); //Select Delete Token
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    Child jack = (Child) app.userList.get(1).user;
    assertEquals(1, jack.tokenlist.size());
  }

  @org.junit.Test // Parent should be able to define, per child, the number of tokens required for redemption and
  // (optionally) what the tokens will be redeemed for
  public void addItem(){
    // Set up test input
    inputArrayList.addAll(cToken);
    inputArrayList.addAll(cItem);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    // Create a new application object
    Application app = new Application();
    Child jack = (Child) app.userList.get(1).user;
    assertNotEquals(null, jack.item);
    assertEquals("Time Out", jack.item.getKey());
    int cost = jack.item.getValue(); // had to put this here, because itelliJ wouldn't let me directly compare
    assertEquals(2, cost);
  }

  @org.junit.Test // Parent should be able to redeem punishments for their children
  public void redeemTokens(){
    // Set up test input test redemption with tokens
    inputArrayList.addAll(cToken);
    inputArrayList.addAll(cItem); //Add Item
    inputArrayList.addAll(Ext);  //Exit
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    inputArrayList.clear();//clears input arraylist
    inputArrayList.addAll(pRedeem);
    setInput(inputArrayList); //add input to buffer
    assertEquals("Punishment 'Time Out' has been redeemed for 2 tokens"
        , app.mainMenu(new Scanner(System.in)));

    // Set up test input to test redemption without tokens
    inputArrayList.clear();//clears input arraylist
    inputArrayList.addAll(cChild);
    inputArrayList.addAll(cItem); //Add Item
    inputArrayList.addAll(Ext);  //Exit
    setInput(inputArrayList); //add input to buffer
    app = new Application();
    inputArrayList.clear();//clears input arraylist
    inputArrayList.addAll(pRedeem);
    setInput(inputArrayList); //add input to buffer
    assertEquals("Error: Insufficient Tokens To Redeem!"
        , app.mainMenu(new Scanner(System.in)));
  }

  @org.junit.Test // Children should be able to redeem their tokens for a reward positive mode
  public void redeemTokensChild(){
    // Set up input to test child redeeming punishments
    inputArrayList.addAll(cToken); // create a child in negative mode with tokens
    inputArrayList.addAll(cItem); //Add Item
    inputArrayList.addAll(Ext);  //Exit
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    inputArrayList.clear();//clears input arraylist
    inputArrayList.add("4"); //select jack
    inputArrayList.add("2"); //select redeem
    setInput(inputArrayList); //add input to buffer
    assertEquals("Children cannot redeem punishments", app.mainMenu( new Scanner(System.in)));

    // Set up input to test child redeeming reward
    inputArrayList.clear();//clears input arraylist
    inputArrayList.addAll(eChildMode); //create child with tokens and put it in positive mode
    inputArrayList.addAll(cItem); //add item
    inputArrayList.addAll(Ext);  //Exit
    setInput(inputArrayList); //add input to buffer
    app = new Application();
    inputArrayList.clear();//clears input arraylist
    inputArrayList.add("4"); //select jack
    inputArrayList.add("2"); //select redeem
    setInput(inputArrayList); //add input to buffer
    assertEquals("Reward 'Time Out' redeemed for 2 tokens", app.mainMenu(new Scanner(System.in)));
  }

  /*********************
   * Menu output tests *
   ********************/
  @org.junit.Test // Parent should be able to view the status of their children
  public void viewStatusParent(){
    inputArrayList.addAll(cToken);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    String curTimeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
    inputArrayList.clear();//clears input arraylist
    inputArrayList.add("3"); //Select Bob
    inputArrayList.add("3"); //Select Jack
    inputArrayList.add("1"); //Select Status
    setInput(inputArrayList); //add input to buffer
    assertEquals("Jack(Mode: Negative, Tokens: 2)\nTokens:\n" +
            "  * Token Note: token note, Timestamp: " + curTimeStamp + "\n" +
            "  * Token Note: token note, Timestamp: " + curTimeStamp + "\n"
        , app.mainMenu(new Scanner(System.in)));
  }

  @org.junit.Test // Children should be able to view the status of their tokens
  public void viewStatusChild(){
    // Set up test input
    inputArrayList.addAll(cToken);
    inputArrayList.addAll(Ext);  //Exit
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    String curTimeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
    inputArrayList.clear();//clears input arraylist
    inputArrayList.add("4"); //Select Jack
    inputArrayList.add("1"); //Select view Status
    setInput(inputArrayList); //add input to buffer
    // Test output of creating Item
    assertEquals("Mode: Negative\nTokens (2):\n" +
            "  * Token Note: token note, Timestamp: " + curTimeStamp + "\n" +
            "  * Token Note: token note, Timestamp: " + curTimeStamp + "\n"
        , app.mainMenu(new Scanner(System.in)));
  }

  /******************
  * Time Bases Test *
  ******************/
  @org.junit.Test // Parent should be able to schedule tokens to be automatically added periodically (e.g., one per day)
  public void scheduleAutokens() throws InterruptedException {
    inputArrayList.addAll(scheduleToken);
    inputArrayList.addAll(Ext);
    setInput(inputArrayList); //add input to buffer
    Application app = new Application();
    TimeUnit.SECONDS.sleep(3);
    Child jack = (Child) app.userList.get(1).user;
    jack.getStatus(); //this updates the tokens based on the time
    assertEquals(3, jack.tokenlist.size());
  }

}