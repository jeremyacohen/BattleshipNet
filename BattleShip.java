import java.util.Arrays; // this is BattleShipNet now changed
import java.util.ArrayList;
public class BattleShip {
	String[][] player1board;
	String[][] player2board;
	String[][] player1attacks;
	String[][] player2attacks;
	int player1lives;
	int player2lives;
	String[] rows;
	String[] cols;
	int player1placements;
	int player2placements;
	ArrayList<Ship> player1Ships; // is a list of each boat, each boat is a list of coordinates, and each coordinate is a pair with row and col
	ArrayList<Ship> player2Ships;
	public volatile int playerMove;
	public BattleShip() {
		player1placements = 0;
		player2placements = 0;
		player1lives = 0;
		player2lives = 0;
		playerMove = 1;
		player1Ships = new ArrayList<>();
		player2Ships = new ArrayList<>();
		
		player1board = new String[10][10];
		for (int i = 0; i < 10; i++) {
			Arrays.fill(player1board[i], " ");
		}
		
		player2board = new String[10][10];
		for (int i = 0; i < 10; i++) {
			Arrays.fill(player2board[i], " ");
		}
		player1attacks = new String[10][10];
		for (int i = 0; i < 10; i++) {
			Arrays.fill(player1attacks[i], " ");
		}
		player2attacks = new String[10][10];
		for (int i = 0; i < 10; i++) {
			Arrays.fill(player2attacks[i], " ");
		}
		rows = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		cols = new String[] {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	}
	public int parseInputRow(String s) {
		return  s.charAt(0)- 'A';
	}
	public int parseInputCol(String s) {
		if (s.length() == 3) {
			return 9;
		}
		return Integer.parseInt(s.charAt(1) + "")- 1;
	}
	public String toString(int player) {
		String[][]board;
		String[][]attackboard;
		if (player == 1) {
			board = player1board;
			attackboard = player1attacks;
		}
		else {
			board = player2board;
			attackboard = player2attacks;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Your Attacks\n");
		sb.append(cols[0]);
		for (int i = 1; i < cols.length; i++) {
			sb.append("|");
			sb.append(cols[i]);
		}
		sb.append("\n");
		for (int r = 0; r < 10; r++) {
			sb.append("---------------------");
			sb.append("\n");
			sb.append(rows[r]);
			for (int c = 0; c < 10; c++) {
				sb.append("|");
				sb.append(attackboard[r][c]);
			}
			sb.append("\n");
			
		}
		sb.append("\n");
		sb.append("Your Board\n");
		sb.append(cols[0]);
		for (int i = 1; i < cols.length; i++) {
			sb.append("|");
			sb.append(cols[i]);
		}
		sb.append("\n");
		for (int r = 0; r < 10; r++) {
			sb.append("---------------------");
			sb.append("\n");
			sb.append(rows[r]);
			for (int c = 0; c < 10; c++) {
				sb.append("|");
				sb.append(board[r][c]);
			}
			sb.append("\n");
			
		}
		return sb.toString();
	}
	public boolean checkSunkShip(String input,  int player) { // player is the one who is doing the sinking
		int row = parseInputRow(input);
		int col = parseInputCol(input);
		if (player == 2) {
			for (Ship s : player1Ships) {
				if(s.containCell(row, col)){
					for (Cell c : s.cells) {
						if (!player1board[c.row][c.col].equals("X")) {
							return false;
						}
					}
				}
			}
		}
		else {
			for (Ship s : player2Ships) {
				if(s.containCell(row, col)) {
					for (Cell c : s.cells) {
						if (!player2board[c.row][c.col].equals("X")) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	public void sinkShip(String input, int player) { // player refers to the player who is doing the attacks 
		int row = parseInputRow(input);
		int col = parseInputCol(input);
		if (player == 2) {
			for (Ship s : player1Ships) {
				if(s.containCell(row, col)) {
					for (Cell c : s.cells) {
						player1board[c.row][c.col] = "D";
						player2attacks[c.row][c.col] = "D";
					}
				}
			}
		}
		else {
			for (Ship s : player2Ships) {
				if(s.containCell(row, col)) {
					for (Cell c : s.cells) {
						player2board[c.row][c.col] = "D";
						player1attacks[c.row][c.col] = "D";
					}
				}
			}
		}
	}
	public void placeShips(String input, int player, int size) {
		String location = input.substring(0, input.indexOf(' '));
		int row = parseInputRow(location);
		int col = parseInputCol(location);
		String direction = input.charAt(input.indexOf(' ') + 1) + "";
		if (player == 1) {
			player1placements++;
		}
		if (player == 2) {
			player2placements++;
		}
		Ship sh = new Ship(size);
		if (direction.equals("U")) { // row decreases, col stays the same
			if (player == 1) {
				for (int i = row; i > row - size; i--) {
					player1board[i][col] = "S";
					player1lives++;
					Cell c = new Cell(i, col);
					sh.addCell(c);
				}
				player1Ships.add(sh);
			}
			if (player == 2) {
				for (int i = row; i > row - size; i--) {
					player2board[i][col] = "S";
					player2lives++;
					Cell c = new Cell(i, col);
					sh.addCell(c);
				}
				player2Ships.add(sh);
			}
		}
		if (direction.equals("D")) { // row increases, col stays the same
			if (player == 1) {
				for (int i = row; i < row + size; i++) {
					player1board[i][col] = "S";
					player1lives++;
					Cell c = new Cell(i, col);
					sh.addCell(c);
				}
				player1Ships.add(sh);
			}
			if (player == 2) {
				for (int i = row; i < row + size; i++) {
					player2board[i][col] = "S";
					player2lives++;
					Cell c = new Cell(i, col);
					sh.addCell(c);
				}
				player2Ships.add(sh);
				
			}
		}
		if (direction.equals("L")) { // row stays the same, col decreases
			if (player == 1) {
				for (int i = col; i > col - size; i--) {
					player1board[row][i] = "S";
					player1lives++;
					Cell c = new Cell(row, i);
					sh.addCell(c);
				}
				player1Ships.add(sh);
			}
			if (player == 2) {
				for (int i = col; i > col - size; i--) {
					player2board[row][i] = "S";
					player2lives++;
					Cell c = new Cell(row, i);
					sh.addCell(c);
				}
				player2Ships.add(sh);
			}
		}
		if (direction.equals("R")) { // row stays the same, col increases
			if (player == 1) {
				for (int i = col; i < col + size; i++) {
					player1board[row][i] = "S";
					player1lives++;
					Cell c = new Cell(row, i);
					sh.addCell(c);
				}
				player1Ships.add(sh);
			}
			if (player == 2) {
				for (int i = col; i < col + size; i++) {
					player2board[row][i] = "S";
					player2lives++;
					Cell c = new Cell(row, i);
					sh.addCell(c);
				}
				player2Ships.add(sh);
				
			}
		}
	}
	public boolean validShipPlace(String input, int player, int size) {
		String location = input.substring(0, input.indexOf(' '));
		int row = parseInputRow(location);
		int col = parseInputCol(location);
		
		String direction = input.charAt(input.indexOf(' ') + 1) + "";
		if ((row + size >= 11 &&  direction.equals("S")) || (col + size >= 11 && direction.equals("E")) || (row - size < -1 && direction.equals("N")) || (col - size < -1 && direction.equals("W"))) {
			return false;
		}
		if (direction.equals("N")) { // row decreases, col stays the same
			if (player == 1) {
				for (int i = row; i > row - size; i--) {
					if (player1board[i][col].equals("S")){
						return false;
					}
				}
			}
			if (player == 2) {
				for (int i = row; i > row - size; i--) {
					player2board[i][col] = "S";
					if (player2board[i][col].equals("S")){
						return false;
					}
					
				}
			}
		}
		if (direction.equals("S")) { // row increases, col stays the same
			if (player == 1) {
				for (int i = row; i < row + size; i++) {
					if (player1board[i][col].equals("S")){
						return false;
					}
				}
			}
			if (player == 2) {
				for (int i = row; i < row + size; i++) {
					if (player2board[i][col].equals("S")){
						return false;
					}
				}
			}
		}
		
		if (direction.equals("W")) { // row stays the same, col decreases
			if (player == 1) {
				for (int i = col; i > col - size; i--) {
					if (player1board[row][i].equals("S")){
						return false;
					}
				}
			}
			if (player == 2) {
				for (int i = col; i > col - size; i--) {
					if (player2board[row][i].equals("S")){
						return false;
					}
				}
			}
		}
		if (direction.equals("E")) { // row stays the same, col increases
			if (player == 1) {
				for (int i = col; i < col + size; i++) {
					if (player1board[row][i].equals("S")){
						return false;
					}
				}
			}
			if (player == 2) {
				for (int i = col; i < col + size; i++) {
					if (player2board[row][i].equals("S")){
						return false;
					}
				}
			}
		}
		return true;
	}
	public boolean isHit(String input, int player) {
		int row = parseInputRow(input);
		int col = parseInputCol(input);
		if (player == 1) {
			return (player2board[row][col] == "S");
		}
		else if (player == 2) {
			return (player1board[row][col] == "S");
		}
		return false;
	}
	public void attackPlayer(String input, int player) { // player is the player that is doing the attacking
		int row = parseInputRow(input);
		int col = parseInputCol(input);
		if (player == 1) {
			if (isHit(input, player)) {
				player2board[row][col] = "X";
				player1attacks[row][col] = "X";
				player2lives--;
			}
			else {
				player1attacks[row][col] = "M";
				
			}
			playerMove = 2;
		}
		else if (player == 2) {
			if (isHit(input, player)) {
				player1board[row][col] = "X";
				player2attacks[row][col] = "X";
				player1lives--;
			}
			else {
				player2attacks[row][col] = "M";
			}
			playerMove = 1;
		}
	}
	public boolean gameOver() {
		return (player1lives == 0) || (player2lives == 0);
	}
	public boolean validAttack(String input) { // need to improve this and other error checkers
		int row = parseInputRow(input);
		int col = parseInputCol(input);
		if (row < 0 || row >= player1board.length) {
				return false;
		}
		if (col < 0 || col >= player1board[0].length) {
			return false;
		}
		return true;
	}
}
