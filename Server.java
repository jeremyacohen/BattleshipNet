import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
	// Maintain list of all client sockets for broadcast
	private Socket[] socketList;

	public Server(){
		socketList = new Socket[2];
	}
	private void getConnection()
	{
		// Wait for a connection from the client
		try
		{
			System.out.println("Waiting for player connections on port 8999.");
			ServerSocket serverSock = new ServerSocket(8999);

			BattleShip game = new BattleShip();
			int playerID = 1;
			for (int i = 0; i < 2; ++i) {
				Socket connectionSock = serverSock.accept();
				// Add this socket to the list
				this.socketList[i] = connectionSock;
				// Send to ClientHandler the socket and arraylist of all sockets

				System.out.println("Player " + Integer.toString(i+1) + " connected successfully.");

				Handler handler = new Handler(connectionSock, this.socketList, game, playerID);
				Thread theThread = new Thread(handler);
				theThread.start();
				playerID = 2;
			}

			System.out.println("Game running...");

			Socket connectionSock = serverSock.accept();

			for (int i = 0; i < this.socketList.length; ++i){
				socketList[i].close();
			}
			serverSock.close();
			connectionSock.close();
			// Will never get here, but if the above loop is given
			// an exit condition then we'll go ahead and close the socket
			//serverSock.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args)
	{
		Server server = new Server();
		server.getConnection();
	}
}
