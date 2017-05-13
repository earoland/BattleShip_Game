package common;

import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.Socket;

/**
 *
 *
 *@author Ethan Roland and Nick Sprinkle
 *@version 12/7/2016 
 */
public class ConnectionInterface extends MessageSource implements Runnable{
	/** Holds the socket for the client */
	private Socket socks;
	/** holds the output stream as a PrintStream */
    private PrintStream ps;
    /** holds the input stream as a BufferedReader*/
    private BufferedReader buff;
    /** holds an ArrayList of Strings to be sent across the connection*/
    ArrayList<String> toSend;
    
    /**
     *
     *
     *@param socket
     */	
	public ConnectionInterface(Socket socket){
		this.socks = socket; 
		toSend = new ArrayList<String>();
        try{
            this.ps = new PrintStream(socks.getOutputStream());
            this.buff = new BufferedReader(new InputStreamReader(socks.getInputStream()));
        }catch(IOException e){ System.out.println(e.getMessage());}
	}

    /**
     *
     */
	@Override
	public void run() { boolean isRunning = true; 
		while(isRunning){
			try{
				if(!toSend.isEmpty()){ 
					ps.print(toSend.get(0)); 
					toSend.remove(0);}
				if(buff.ready()){ 
					notifyReceipt(buff.readLine()); }
			}catch(IOException e){ 
				isRunning = false; 
			System.out.println(e.getMessage());}
		}}

	/**
	 * 
	 * @param write
	 */
    public void send(String write){toSend.add(write);}
}