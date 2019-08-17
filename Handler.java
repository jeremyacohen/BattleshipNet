import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Handler implements Runnable {
	public Socket connectionSock;
	public Socket[] socketList;
	public BattleShip game;
	public int playerID;
	
	public Handler(Socket sock, Socket[] socketList, BattleShip game, int playerID)
	{
		this.connectionSock = sock;
		this.socketList = socketList;
		this.game = game;
		this.playerID = playerID;
	}
	public void run() {
		try {
			BufferedReader playerInput = new BufferedReader(new InputStreamReader(this.connectionSock.getInputStream())); // this is recieving data from the client
			int [] shipLengths = new int [] {5, 4};
			
			switch (this.playerID) {
			case 2:
				sendMessage("\nYou are player '2', you will go second." + "\r\n");
				int ship = 0;
				while (game.player2placements != shipLengths.length) {
					sendMessage(this.game.toString(2));
					sendMessage("Place a ship of size " + shipLengths[ship] + ": " + "\r\n");
					String inp = playerInput.readLine().trim(); // waits here
					if (game.validShipPlace(inp, 2, shipLengths[ship])) {
						game.placeShips(inp, 2, shipLengths[ship]); // second is the size, will come from the array, should increment placement count
						ship++;
					}
					else {
						sendMessage("Invalid placement." + "\r\n");
					}
				}
				if (this.game.player1placements != shipLengths.length) { // check if the other player is done
					sendMessage(this.game.toString(2));
					sendMessage("Waiting for other player to finish..." + "\r\n");
					while (this.game.player1placements != shipLengths.length) {
						Thread.sleep(500);
					}
				}
				break;
			case 1:
				sendMessage("\nYou are player '1', you will go first." + "\r\n");
				ship = 0;
				while (game.player1placements != shipLengths.length) {
					sendMessage(this.game.toString(1));
					sendMessage("Place a ship of size " + shipLengths[ship] + ": " + "\r\n");
					String inp = playerInput.readLine().trim(); // waits here
					if (game.validShipPlace(inp, 1, shipLengths[ship])) {
						game.placeShips(inp, 1, shipLengths[ship]); // second is the size, will come from the array, should increment placement count
						ship++;
					}
					else {
						sendMessage("Invalid placement." + "\r\n");
					}
				}
				if (this.game.player2placements != shipLengths.length) {
					sendMessage(this.game.toString(1));
					sendMessage("Waiting for other player to finish..." + "\r\n");
					while (this.game.player2placements != shipLengths.length) {
						Thread.sleep(500);
					}
				}
				break;
			default:
				break;
			}
			while (!this.game.gameOver()) {
				sendMessage(this.game.toString(playerID)); 
				if (this.game.playerMove == this.playerID) { //player's turn
					sendMessage("Please enter attack position: " + "\r\n");
					String input = playerInput.readLine().trim(); // waits here
					if (!game.validAttack(input)) {
						sendMessage("Invalid move." + "\r\n");
					}
					else {
						
						if (game.isHit(input, this.playerID)) {
							game.attackPlayer(input, this.playerID);
							sendMessage("Nice shot. HIT" + "\r\n");
							if (game.checkSunkShip(input, this.playerID)) {
								game.sinkShip(input, this.playerID);
								sendMessage("Congrats, you sunk their ship." + "\r\n");
							}
						}
						else {
							game.attackPlayer(input, this.playerID);
							sendMessage("Sorry, you missed." + "\r\n");
						}
					}
				}
				else {
					sendMessage("Please wait for opponent's move." + "\r\n");
					while (this.game.playerMove != this.playerID) {
						Thread.sleep(500);
					}
				}
			}
			sendMessage(this.game.toString(this.playerID));
			if (game.player1lives == 0) { // If player 2 won
				sendMessage("GAME OVER! Player 2 wins!" + "\r\n");
			}
			else if (game.player2lives == 0){
				sendMessage("GAME OVER! Player 1 wins!" + "\r\n");
			}
			
		}catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException z) {
			System.out.println(z.getMessage());
		}
	}
	private void sendMessage(String message) { 
		try {
			DataOutputStream clientOutput = new DataOutputStream(this.connectionSock.getOutputStream());
			clientOutput.writeBytes(message);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}