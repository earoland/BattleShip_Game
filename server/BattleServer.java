package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import common.*;

public class BattleServer implements MessageListener{

	protected Game game;

	HashMap<String, ConnectionInterface> map;

	HashMap<ConnectionInterface, String> reverseMap;
	
	ArrayList<String> turnOrder;
	
	ArrayList<Thread> threads;

	private ServerSocket sSocket; 

	protected boolean gameInProgress = false;

	private String activePlayer = "";
	
	private int pointer = 0;
	
	private int boardSize;

	public BattleServer(int portNo, int boardSize) throws IOException {
		game = new Game(boardSize);
		sSocket = new ServerSocket(portNo);
		threads = new ArrayList<Thread>();
		turnOrder = new ArrayList<String>();
		map = new HashMap<String, ConnectionInterface>();
		reverseMap = new HashMap<ConnectionInterface, String>();
		this.boardSize = boardSize;
	}

	public void listen() throws IOException {
		int index = 0;
		while(!gameInProgress){
			Socket clientSocket = sSocket.accept();
			ConnectionInterface ci = new ConnectionInterface(clientSocket);
			ci.addMessageListener(this);
			threads.add(index, new Thread(ci));
			threads.get(index).start();
			index++;
		}
	}

	@Override
	public void messageReceived(String message, MessageSource source) {
		String messageParts[] = message.split(" ");

		switch(messageParts[0]){
		case "/join":
			join(messageParts, source);
			break;
		case "/play":
			play(messageParts, source);
			break;
		case "/attack":
			attack(messageParts, source);
			break;
		case "/quit":
			sourceClosed(source);
			break;
		case "/show":
			show(messageParts, source);
			break;
		default:
			((ConnectionInterface) source).send("Error, Invalid Command \n"
					+ "Accepted Commands are : /play, /show, /quit, /attack");
			break;
		}
		
			
	}

	private void broadcast(String message) {
		for(String key: map.keySet()){
			map.get(key).send(message);
		}
	}


	@Override
	public void sourceClosed(MessageSource source) {
		source.removeMessageListener(this);
		String name = reverseMap.get(source);
		reverseMap.remove(source);
		map.remove(name);
		game.players.remove(name);
		name = name + " Has Surrendered";
		broadcast(name);
	

	}

	private void play(String[] parts, MessageSource source){
		if(gameInProgress){
			//we don't want it to do anything if the game is in progress
		}
		else if(map.size() < 2){		
			broadcast("Not Enough Players to Begin the Game");
		}
		else{
			for(String key: map.keySet()){
				game.addPlayer(key);
			}
			setActivePlayer();
			gameInProgress = true;
		}
	}
	
	private void show(String[] parts, MessageSource source){
		String name = reverseMap.get(source);
		if(!gameInProgress){
			name = "Game has not begun";
		}
		else if(parts.length != 2){
			if(parts[1].equals(name)){
				name = game.showMap(parts[1], true);
			}
			else{
				name = game.showMap(parts[1], false);
			}
		}
		else{
			name ="Error, Syntax is /show <player>";
		}
		((ConnectionInterface) source).send(name);
	}
	
	private void join(String[] parts, MessageSource source){
	
	if(gameInProgress){
		//we don't want it to do anything if the game is in progress
	}else{
		if(parts.length != 2){
			((ConnectionInterface) source).send("Error, Syntax is /join Username");
		}
		else if(!map.containsKey(parts[1])){
			turnOrder.add(parts[1]);
			map.put(parts[1], ((ConnectionInterface) source));
			reverseMap.put(((ConnectionInterface) source), parts[1]);
			broadcast(parts[1] + " Has Joined");
		}
		else{
			((ConnectionInterface) source).send("Player Name Already Exists");
		}
	}
	}

	private void setActivePlayer() {
			activePlayer = turnOrder.get(pointer);
			if(pointer++ > turnOrder.size()){
				pointer = 0;
			}
	}

	private void attack(String[] parts, MessageSource source) {
		String name = reverseMap.get(source);
		if(!gameInProgress){
			((ConnectionInterface) source).send("Game has not Begun");
		}else{
			if(isActivePlayer(name)){
				if(parts.length == 4){
					if(name.equals(parts[3])){
						((ConnectionInterface) source).send("For Safety Reasons, "
								+ "You are Unable to Attack Yourself");
					}
					else{
						int x = 0;
						int y = 0;
						try{
							x = Integer.parseInt(parts[1]);
							y = Integer.parseInt(parts[2]);
						}catch(NumberFormatException nfe){
							((ConnectionInterface) source).send("Error, Syntax is "
									+ "/attack <int> <int> <string>");
						}
						if(game.getPlayer(name).getDefeated()){
							((ConnectionInterface) source).send("You Attempted to Attack"
									+ " a Defeated Player, Try Again");
						}else{
							if(x > boardSize || y > boardSize){
								((ConnectionInterface) source).send("Error:"
										+" you have attempted to attack"
										+ " outside the game board"
										+ "\n the Board Size is " 
										+ boardSize);
							}else{
								game.shoot(parts[3], x, y);
								setActivePlayer();
							}
						}
						broadcast(name + " Has Attacked" + parts[3]);
						if(!game.checkGameStatus(parts[3])){
							broadcast(parts[3] + " Was Defeated");
							game.getPlayer(name).setDefeated();
						}
					}
				}else{
					((ConnectionInterface) source).send("Error, Syntax is "
							+ "/Attack <X> <Y> <player>");
				}

			}else{
				((ConnectionInterface) source).send("You are not the active player, \n " +
						"It is " + getActivePlayer() +"'s turn");
			}
		}
	}

	private String getActivePlayer() {
		return activePlayer;
	}

	private boolean isActivePlayer(String name) {
		boolean bool = false;
		if(name.equals(activePlayer)){
			bool = true;
		}
		return bool;
	}

}
