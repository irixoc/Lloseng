import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class constructs the UI that enables chat functionality
 * for the server.  It implements the chat interface in order 
 * to activate the display() method.
 * 
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Richard Xiong
 * @version June 2020
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the server that created this ConsoleChat.
   */
  EchoServer server;
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param EchoServer the server to connect to.
   */
  public ServerConsole(EchoServer server) 
  {
    this.server = server;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
   */
  public void accept()
  {
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception e)
    {
      System.out.println(e.toString());
      System.out.println("Unexpected error while reading from console!!!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("SERVER MSG > " + message);
  }

  
  // //Class methods ***************************************************
  
  // /**
  //  * This method is responsible for the creation of the Client UI.
  //  *
  //  * @param args[0] The host to connect to.
  //  */
  // public static void main(String[] args) 
  // {
  //   int port = 0;  //The port number

  //   try
  //   {
  //     port = Integer.parseInt(args[0]);
  //   }
  //   catch(ArrayIndexOutOfBoundsException e)
  //   {
  //     port = DEFAULT_PORT;
  //   }

  //   ServerConsole chat = new ServerConsole(port);
  //   chat.accept();  //Wait for console data
  // }

}
//End of ConsoleChat class
