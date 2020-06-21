// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import client.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @author Richard Xiong
 * @version June 2020
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
    if (message.charAt(0) == '#') {
      handleCommandFromServerUI(message.substring(1));
    } else {
      String msg = "SERVER MSG > " + message;
      System.out.println(msg);
      this.sendToAllClients(msg);
    }

  }

 /**
   * This method handles all commands coming from the serverUI            
   *
   * @param command A command from the serverUI.    
   */
  public void handleCommandFromServerUI(String command)
  {
    String[] cmd = command.split(" ");

    switch(cmd[0]) {
      case "quit":
        quit();
        break;
      case "stop":
        stopListening();
        break;
      case "close":
        try
        {
          close();
        }
        catch (IOException e)
        {
          System.out.println("Unable to close connection.");
        }
        break;        
      case "setport":
        if (isListening()) {
          System.out.println("Error message: cannot set port unless server is closed.");
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
      case "start":
        try
        {
          listen();
        }
        catch (IOException e)
        {
          System.out.println("Unable to start server.");
        }
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
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {
      System.out.println("Server was unable to quit.");
    }
    finally 
    {
      System.out.println("Server is quitting.");
      System.exit(0);
    }
  }  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass.
   * It is called each time a new client connection
   * is accepted. It prints a message indicating that
   * a new client has connected to the server.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) 
  {
    System.out.println("A new client has connected.");
  }

  /**
   * This method overrides the one in the superclass.
   * It is called each time a client disconnects from
   * the server. It prints a message indicating that
   * a client has disconnected from the server.
   * @param client the connection connected to the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client)
  {
    System.out.println("A client has disconnected.");
  }
  
  /**
   * This method overrides the one in the superclass.
   * It is called each time an exception is thrown in a
   * ConnectionToClient thread.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(ConnectionToClient client, Throwable exception)
  {
    System.out.println("A client has unexpectedly disconnected.");
  }

  /**
   * This method overrides the one in the superclass.
   * It is called when the server has closed.
   * When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed()
  {
    System.out.println("Server has closed.");
  }  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    ServerConsole serverConsole = new ServerConsole(sv);

    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    serverConsole.accept();
  }
}
//End of EchoServer class