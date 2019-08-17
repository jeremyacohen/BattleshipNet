import java.util.ArrayList;

public class Ship {
	ArrayList<Cell> cells;
	int length;
	public Ship(int size) {
		length = size;
		cells = new ArrayList<>();
	}
	public void addCell(Cell c) {
		cells.add(c);
	}
	public boolean containCell(int row, int col) {
		for (Cell s : cells) {
			if (row == s.row && col == s.col) {
				return true;
			}
		}
		return false;
	}
}
