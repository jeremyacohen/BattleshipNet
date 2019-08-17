import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Listener implements Runnable {
	private Socket connectionSock = null;
	Listener(Socket sock){
		this.connectionSock = sock;
	}
	public void run() {
		try {
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			while (true) {
				if (serverInput.equals(null)) {
					// Connection was lost
					System.out.println("Closing connection for socket " + connectionSock);
					connectionSock.close();
					break;
				}
				String serverText = serverInput.readLine();
				if (serverText.equals(null)) {
					connectionSock.close();
					break;
				}
				System.out.println(serverText);
			}
		}
		catch (Exception e){
			System.out.println("Error: Server has disconnected");
		}
		
	}
}
