package server;
import java.util.HashMap;

public class Game {
	
	private int boardSize;
	
	HashMap<String,Grid> players;
	
	public Game(int boardSize){	
		this.boardSize = boardSize;
		players = new HashMap<String, Grid>();
	}
	public void addPlayer(String playerName){
		
		players.put(playerName ,new Grid(boardSize));
	}
	
	/**
	 * Holds a 2d array to represent a board.
	 * 0 = nothing = " "
	 * 1 = Carrier = C
	 * 2 = Battleship = B
	 * 3 = Destroyer = D
	 * 4 = Cruiser = R
	 * 5 = Submarine = S
	 * 6 = miss = X
	 * 7 = hit = !
	 * 
	 * @param x, the first coordinate to shoot at
	 * @param y, the second coordinate to shoot at
	 */
	public void shoot(String player, int x, int y){
		Grid holder = players.get(player);
		int spot = holder.board[x][y];
		
		if(spot == 0 ){
			holder.board[x][y] = 6;
		}else if(spot == 1 ||spot == 2 ||spot == 3 ||spot == 4 ||spot == 5 ){
			holder.board[x][y] = 7;
		}
	}
	
	/**
	 * Checks to see if the player is still in the game.
	 * 
	 *  @return True if player still in, False if player is not.
	 */
	public boolean checkGameStatus(String player){
		Grid holder = players.get(player);
		return holder.checkGameStatus();
	}
	public String showMap(String player, boolean isCurrent){
		Grid holder = players.get(player);
		return holder.showMap(isCurrent);
	}
	public Grid getPlayer(String name) {
		return players.get(name);
	}

}
