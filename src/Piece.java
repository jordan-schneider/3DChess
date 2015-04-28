import java.util.ArrayList;

public abstract class Piece {
	int[] location;
	int owner;
	Board board;
	public final char cCode;
	int photoID;
	
	public Piece(int x, int y, int z, int owner, Board board,char cCode){
		location = new int[3];
		location[0] = x;
		location[1] = y;
		location[2] = z;
		this.owner = owner;
		this.cCode=cCode;
		this.board=board;
	}
	public char getSymbol(){
		return owner==Board.BLACK?Character.toLowerCase(cCode):cCode;
	}
	public void move(int[] to){
		location = to;
	}
	abstract ArrayList<int[]> getMoves();
}
