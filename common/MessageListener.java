package common;

/**
 * This interface represents  observers of MessageSource.
 * 
 * @author Ethan Roland & Nick Sprinkle
 * @version 11/27/2016
 *
 */
public interface MessageListener {
	
	/**
	 * Used to notify observers that the subject has received a message.
	 * @param message The message received by the subject.
	 * @param source The source from which this message originated(if needed).
	 */
	public void messageReceived(String message, MessageSource source);
	
	/**
	 * Used to notify observers that the subject will not receive new messages; observers can
	 * deregister themselves.
	 * 
	 * @param source The MessageSource that does not expect more messages.
	 */
	public void sourceClosed(MessageSource source);

}
