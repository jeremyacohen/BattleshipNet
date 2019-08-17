import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args){
		try {
			String hostname = "localhost"; // can put your ip address here
			int port = 8999;
			boolean myTurn = true;
			System.out.println("Connecting to game server on port " + port);
			Socket connectionSock = new Socket(hostname, port);
			
			DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());

			System.out.println("Connection made.");

			// Start a thread to listen and display data sent by the server
			Listener listener = new Listener(connectionSock);
			Thread theThread = new Thread(listener);
			theThread.start();
			
			Scanner keyboard = new Scanner(System.in);
			while (serverOutput != null) {
				String data = keyboard.nextLine();
				if (!myTurn) {
					System.out.println("Please wait for your turn.");
				}
				else {
					serverOutput.writeBytes(data + "\n");
				}
			}
			System.out.println("Connection lost.");
			keyboard.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}