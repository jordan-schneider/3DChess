import java.util.ArrayList;


public abstract class Piece {
	int[] location;
	int owner;
	Board board;
	
	public Piece(int x, int y, int z, int owner, Board board){
		location = new int[3];
		location[0] = x;
		location[1] = y;
		location[2] = z;
		this.owner = owner;
		this.board = board;
	}
	
	public void move(int[] to){
		location = to;
	}
	public ArrayList<int[]> getMoves(){
		return null;
	}
}
