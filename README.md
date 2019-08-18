# BattleshipNet

This project uses threads and sockets to implement the game Battleship in Java. The game can be ran across two different computers, with a server and client running on one computer, and a client running on the other. 

To input a ship placement, you first put the row and column, and then you put the direction (U, D, L, R). So to place a ship in the top left corner going right, you type "A1 R". When attacking, you only type the row and column.

The client consists of Client.java and Listener.java. Client.java sends the user input to the server while Listener receives information from the Server. The server consists of Server.java and Handler.java. Server.java accepts new players and then starts a Handler thread for each client. The Handler sends and receives information from the Client. 
