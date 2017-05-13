package client;

import java.io.IOException;
import java.util.Scanner;

import common.MessageSource;

/**
 * The BattleDriver is in charge with taking input from the command line
 * in order to connect a client to the server, and then passes commands to
 * the client that the user sent.   
 * @author Ethan Roland and Nick Sprinkle
 * @version 12/7/16
 */
public class BattleDriver {
	
    /**
     * The main method for the BattleDriver which parse the commandline
     * arguments, and then runs the runCommands method to parse game
     * commands that the user inputs.  Correct input is host name, port number,
     * and a nick name for the user.
     * @param args
     */
	public static void main(String[] args){
		BattleClient ship = null;
		if(args.length >= 3){
			try {
				ship = new BattleClient(args[0],Integer.valueOf(args[1]));
                ship.send("/join "+ args[2]);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			runCommands(ship);
		}else {
			System.out.println("Usage: [<Host Name>] [<Port Number>] "
                                +"[<Nick Name>]");
		}	
	}
	
    /**
     * Askes the user for the game commands and passes them off to the
     * client to pass to the server.
     *@param ship which is the client the user is connected to.
     */
	public static void runCommands(BattleClient ship){
		Scanner scan = new Scanner(System.in);
		String input = "";
		while(!input.equals("/quit")){
			String s = scan.nextLine();
			String[] command = s.split(" ");
			input = command[0];
            ship.send(s);
		}
		scan.close();
        System.exit(1);
	}
}
