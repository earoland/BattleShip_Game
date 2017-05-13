package server;

import java.io.IOException;

public class BattleShipDriver {
	
	protected static final int DEFAULT_SIZE = 10;
	
	public static void main(String[] args) {
		
		int boardSize = DEFAULT_SIZE;
		
		int portNo = 1066;
		
		switch(args.length){
			case 2:
				try{
					boardSize = Integer.parseInt(args[1]);
				}catch(NumberFormatException nfe){
					System.out.println("Error, expected usage is <Port Number>(int) [BoardSize] (int)");
					System.exit(0);}
			case 1:
				try{
					portNo = Integer.parseInt(args[0]);
				}catch(NumberFormatException nfe){
					System.out.println("Error, expected usage is <Port Number>(int) [BoardSize] (int)");
					System.exit(0);}
				break;
			case 0:
				System.out.println("Error, expected usage is <Port Number>(int) [BoardSize] (int)");
				System.exit(0);
				break;
		}
		BattleServer server = null;
		try {
			server = new BattleServer(portNo, boardSize);
		} catch (IOException e) {
			System.out.println("Error, portNo was invalid");
			System.exit(0);
		}
		try {
			server.listen();
		} catch (IOException e) {
			System.out.println("Error");
			System.exit(0);
		}
	
	}
}
