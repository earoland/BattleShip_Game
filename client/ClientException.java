/**
 *An Exception Class for a BattleClient
 *@author  Ethan Roland
 *@version CS465 Project 3
 */
package client;

/**
 *The ClientException class that wraps most other exceptions thrown in the client
 *
 */
public class ClientException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *A constructor that accepts no parameters
	 */
	public ClientException() {this("");}

	/**
	 *A constructor that accepts two parameters
	 *
	 *@param message, the message to be displayed
	 *@param ex, the Throwable exception this is wrapping
	 */
	public ClientException(String message, Throwable ex){
        super(message, ex);
	}
	/**
	 *A constructor that accepts one parameter
	 *
	 *@param message, the message to be displayed
	 */
	public ClientException(String message) { super(message); }
}


