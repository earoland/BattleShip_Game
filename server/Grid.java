package server;
import java.util.Random;
/**
 * Class for a single board of battleship, has all needed methods for displaying
 * the board to both the board's player and to enemy players, 
 * can determine if the player still has ships remaining,
 * and allows for being shot at by enemy players. 
 *  
 * @author Ethan Roland, Nick Sprinkle
 * @version project 3 CS467
 */
public class Grid {
	
	//The battleship board 
	//Note to Dr. K, yes, I realize this should be a 2d array of strings
	//but nick decided to use ints for some godforsaken reason and I dont feel like changing it because it does work
	//in a very convoluted way (Ethan)
	int[][] board;
	// the boardSize given, should default to 10
	private  int boardSize;
	//the size of an aircraft carrier in battleship
	public static final int AIRCRAFTCARRIER = 5;
	//the size of a submarine in battleship
	public static final int SUBMARINE = 3;
	//the size of a battleship in battleship
	public static final int BATTLESHIP = 4;
	//the size of a destroyer in battleship
	public static final int DESTROYER = 2;
	//the size of a cruiser in battleship
	public static final int CRUISER = 3;
	
	private boolean defeated;
	
	/**
	 * Constructor, takes one parameter of type int and 
	 * builds a board based upon it, no guarantee all ships will fit
	 * @param boardSize, the board size to 
	 */
	public Grid(int boardSize){
		this.boardSize = boardSize;
		board = new int[boardSize][boardSize];
		this.placeShip(AIRCRAFTCARRIER, "C");
		this.placeShip(BATTLESHIP, "B" );
		this.placeShip(DESTROYER, "D");
		this.placeShip(CRUISER, "R");
		this.placeShip(SUBMARINE, "S");
		defeated = false;
	}
	
	
	/**
	 * tester method for use with custom board, allows variable 
	 * ship placement based on parameters received
	 * 
	 * @param boardSize, the size of the square board to build
	 * @param ships, the number of ships to place on the board
	 */
	public Grid(int boardSize, int ships){
		board = new int[boardSize][boardSize];
		switch(ships){
		case 5:
			this.placeShip(AIRCRAFTCARRIER, "C");
		case 4:
			this.placeShip(BATTLESHIP, "B" );
		case 3:
			this.placeShip(CRUISER, "R");		
		case 2:
			this.placeShip(SUBMARINE, "S");
		case 1:
			this.placeShip(DESTROYER, "D");
			break;
		}
		defeated = false;
	}
	/**
	 * given a ship type and a ship size, attempts to place that ship upon the board
	 * retries until it succeeds
	 * 
	 * @param shipSize, the size of the ship to be placed
	 * @param character, the encoding character to place in the location.
	 */
	public void placeShip(int shipSize, String character){ 
		
		boolean shipDoesNotOverLap = true;
		boolean upOrDown;
		int range;
		int position;
		int randomNum;
		do{
		Random randy = new Random();
		//says if ship will be place vertical or horizontal
		upOrDown = randy.nextBoolean();
		
		//range that the ship can be placed such as if the ship is
		//5 long then it will be stop from having place outside the board
		range = boardSize - shipSize;
		position = randy.nextInt(range + 1);
		
		//makes it so one side can be 0 to BOARDSIZE
		randomNum = randy.nextInt(boardSize);
		
		shipDoesNotOverLap = detectShip(upOrDown, position, randomNum, shipSize);
		}while(shipDoesNotOverLap);
		

		if(upOrDown){
			for(int i = 0; i < shipSize; i ++){
				placeHelper(character, randomNum, position + i);
			}
		}else{
			for(int i = 0; i < shipSize; i ++){
				placeHelper(character, position + i, randomNum);
			}
		}//end of else
	}//end of method
	
	/**
	 * Helper method that places the appropriate ship character within the board space
	 * at the provided coordinates
	 * 
	 * @param type, the type of ship, determines what character to place
	 * @param x, the first coordinate to place that character at
	 * @param y, the second coordinate to place that character at
	 */
	public void placeHelper(String type, int x, int y){
		
		int p =0;
		switch(type){
			case "C":
				p = 1;
				break;
			case "B":
				p = 2;
				break;
			case "D":
				p = 3;
				break;
			case "R":
				p = 4;
				break;
			case "S":
				p = 5;
				break;
		}
		board[x][y] = p;
		

	}
	/**
	 * This method tells if there is another ship at the position that this ship want placed.
	 * 
	 * @return boolean, True if there is a non " " within the path; false otherwise 
	 */
	public boolean detectShip(boolean upOrDown,int position, int randomNum, int shipSize){
		boolean retry = false;
		if(upOrDown){
			for(int i = 0; i < shipSize; i ++){
				if(board[randomNum][position + i] != 0){
					retry = true;
				}
			}
		}else{
			for(int i = 0; i < shipSize; i ++){
				if(board[position + i][randomNum] != 0){
					retry = true;
				}
			}
		}//end of else
		return retry;
	}
	
	public String showMap(boolean thisPlayer){
		String s = "    0   1   2   3   4   5   6   7   8   9\n";
		String line = "  +---+---+---+---+---+---+---+---+---+---+\n";
		s += line;
		for(int i = 0;i <boardSize;i++){
			s += i;
			for(int j = 0; j< boardSize;j++){
				s += " | " + getPositionType(i,j,thisPlayer);
			}
			s += "\n";
			s += line;
		}
		return s;
	}
	
	
	/**
	 * Determines what the user will see for that position as enemy.
	 * Board Key
	 * 0 = nothing = " "
	 * 1 = Carrier = C
	 * 2 = Battleship = B
	 * 3 = Destroyer = D
	 * 4 = Cruiser = R
	 * 5 = Submarine = S
	 * 6 = miss = X
	 * 7 = hit = !
	 * 
	 * @param x the first coordinate
	 * @param y the second coordinate
	 * @param thisPlayer true if viewing own board
	 */
	public String getPositionType(int x, int y,boolean thisPlayer){
		
		if(board[x][y] == 6){
			return "X";
		}else if(board[x][y] == 7){
			return "!";
		}else if((board[x][y] == 1) && thisPlayer){
			return "C";
		}else if((board[x][y] == 2) && thisPlayer){
			return "B";
		}else if((board[x][y] == 3) && thisPlayer){
			return "D";
		}else if((board[x][y] == 4) && thisPlayer){
			return "R";
		}else if((board[x][y] == 5) && thisPlayer){
			return "S";
		}else{
			return " ";
		}
	}
	
	public boolean checkGameStatus(){
		boolean check = false;
		for(int i =0; i <boardSize;i++){
			for(int j = 0;j < boardSize;j++){
				if(board[i][j] != 0 || board[i][j] != 6 || board[i][j] != 7){
					check = true;
				}
			}
		}
		return check;
	}


	public void setDefeated() {
		defeated = true;
		
	}
	public boolean getDefeated(){
		return defeated;
	}
	
}
