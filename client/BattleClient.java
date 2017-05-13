package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import common.*;

/**
 * BattleClient is the client implementation of the program.
 * 
 * @author Ethan Roland and Nick Sprinkle
 * @version 12/2/2016
 */
public class BattleClient extends MessageSource implements MessageListener{
	
	/** Holds a reference to the connection interface */
	private ConnectionInterface face;
	/** a reference to the thread, not really necessary, just allows for referencing it */
	private Thread holder;
	
	/**
	 * Constructor for the BattleClient. Takes in a String which is the 
     * hostname, and a port number connect to.
	 * 
	 * @param url is the hostname to connect to
	 * @param port is the port number to connect to
	 * @throws UnknownHostException if an unknown host exception occurs
	 * @throws IOException if an I/O exception occurs it must be handled
	 */
	public BattleClient(String url,int port) throws UnknownHostException, 
                                                    IOException{
		
        this.face = new ConnectionInterface(new Socket(url, port));
        holder = new Thread(face);
        holder.start();

	}

	/**
	 * Used to notify observers that the subject has received a message.
	 * @param message The message received by the subject.
	 * @param source The source from which this message originated(if needed).
	 */
	@Override
	public void messageReceived(String message, MessageSource source) {
        System.out.print(message);
	}

    /**
     *
     * @param message 
     */
    public void send(String message){ face.send(message); }

	/**
	 * Used to notify observers that the subject will not receive new messages;
     * observers can deregister themselves.
	 * 
	 * @param source The MessageSource that does not expect more messages.
	 */
	@Override
	public void sourceClosed(MessageSource source) {
		source.removeMessageListener(this);		
	}

}
