// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;

  /**
   * The integer variable loginid records the login id for the
   * current client's session.
   */
  int loginid;
  final static String loginidPrefix = "#login";

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(int loginid, String host, int port, ChatIF clientUI) throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.loginid = loginid;
    this.clientUI = clientUI;

    openConnection();

    try
    {
      sendToServer(loginidPrefix + " " + loginid);
    }
    catch(Exception e)
    {
      System.out.println(e.toString());
      clientUI.display("Could not send message to server. Terminating client.");
      quit();
    }
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if (message.charAt(0) == '#') {
      handleCommandFromClientUI(message.substring(1));
    } else {
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display("Could not send message to server. Terminating client.");
        quit();
      }
    }
  }

 /**
   * This method handles all commands coming from the clientUI            
   *
   * @param command A command from the clientUI.    
   */
  public void handleCommandFromClientUI(String command)
  {
    String[] cmd = command.split(" ");

    switch(cmd[0]) {
      case "quit":
        quit();
        break;
      case "logoff":
        try
        {
          closeConnection();
        }
        catch (IOException e)
        {
          System.out.println("Client was unable to logoff.");
        }
        break;
      case "sethost":
        if (isConnected()) {
          System.out.println("Error message: cannot set host unless client is logged off.");
        } else {
          try
          {
            setHost(cmd[1]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
            System.out.println("Host was not entered.");
          }
        }
        break;
      case "setport":
        if (isConnected()) {
          System.out.println("Error message: cannot set port unless client is logged off.");
        } else {
          try
          {
            setPort(Integer.parseInt(cmd[1]));
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
            System.out.println("Port number was not entered.");
          }
          catch (NumberFormatException e)
          {
            System.out.println("Invalid port number.");
          }
        }
        break;
      case "login":
        try
        {
          openConnection();
        }
        catch (IOException e)
        {
          System.out.println("Client was unable to login.");
        }
        break;
      case "gethost":
        System.out.println("Host: " + getHost());
        break;
      case "getport":
        System.out.println("Port: " + getPort());
        break;
      default:
        System.out.println("Command not recognized.");
        break;
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.out.println("The client has quit.");
    System.exit(0);
  }

	/**
	 * This method overrides the one in the superclass.
   * It is called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. 
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  protected void connectionException(Exception exception)
  {
    System.out.println("Server has unexpectedly shut down. Client is quitting.");
    System.exit(0);
  }

  /**
   * This method overrides the one in the superclass.
   * It is called after the connection has been closed
   * to perform special processing such as cleaning up 
   * and terminating, or attempting to reconnect.
   */
  protected void connectionClosed()
  {
    System.out.println("Connection to server has closed.");
  }

}
//End of ChatClient class
