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
  String alias;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String alias) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    this.alias = alias;
  }

  
  //Instance methods ************************************************
  
  /****Changed for E49 RT
   * prints the server shutdown message
   */
  public void connectionClosed() {
    System.out.println("Server has shut down."); }
  
  /****Changed for E49 RT
   * stops the client from trying to reach the server
   */
  public void connectionException(Exception exception) {
    quit(); }
  
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }
  
  public int getClientCount() {
    return 0; }
  
  /****Changed for E50 RT
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if (message.charAt(0) == '#') {
      String command = "";
      String param = "";
      if (message.indexOf(" ") != -1) {
        command = message.substring(1,message.indexOf(" "));
        param = message.substring(message.indexOf(" "));
      }
      else {
        command = message.substring(1); }
      if (command.equals("quit")) {
        quit(); }
      else if (command.equals("alias")) {
        alias = param; }
      else if (command.equals("logoff")) {
         try {
           closeConnection(); }
         catch(IOException e) {}
      }
      else if (command.equals("sethost")) {
        if (isConnected()) {
          System.out.println("Already Connected"); }
        else {
          this.setHost(param); }
      }
      else if (command.equals("setport")) {
        if (isConnected()) {
          System.out.println("Already Connected"); }
        else {
          this.setPort(Integer.parseInt(param)); }
      }
      else if (command.equals("login")) {
        if (isConnected()) {
          System.out.println("Already Connected"); }
        else {
          try {
            openConnection(); }
          catch (IOException e) {
            System.out.println("Error: failed to login"); }
        }
      }
      else if (command.equals("gethost")) {
        System.out.println(this.getHost()); }
      else if (command.equals("getport")) {
        System.out.println(this.getPort()); }
      else if (command.equals("number")) {
        System.out.println(getClientCount()); }
      else {
        System.out.println("Invalid command!"); }
    }
    else {
      try { 
        sendToServer(message); }
      catch(IOException e) {
        clientUI.display("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }
  
  /**
   * method to get alias of a user
   * or set it
   */
  public String getAlias() {
    return alias; }
  public void setAlias(String a) {
    this.alias = a; }
  
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
    System.exit(0);
  }
}
//End of ChatClient class
