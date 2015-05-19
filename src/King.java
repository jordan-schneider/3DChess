import java.util.ArrayList;


/**
 * Raumschach King can move to any open square in either a cardinal direction or a pair of cardinal directions.
 * Alternatively, 
 * @author Jordan
 *
 */
public class King extends Piece {
	boolean hasMoved;
	public static final int[][] moves={
		{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}
	};
	
	/**
	 * Creates a new King Piece
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @param owner of piece, either 0 or 1
	 * @param board this piece is on
	 */
	public King(int x, int y, int z, int owner, Board board){
		super(x,y,z,owner,board,'K');
		hasMoved = false;
	}

	/**
	 * Moves the piece
	 * @param to 
	 * 			triplet of coordinates to move to
	 */
	@Override
	public void move(int[] to) {
		super.move(to);
		hasMoved = true;
	}

	/**
	 * Returns a list of the valid moves for this piece.
	 * A move is valid if its distance is no greater than 1 diagonal away from the current position AND
	 * the move would not put the king in check
	 */
	@Override
	public ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid = new ArrayList<int[]>();
		for(int[] m:moves){
			int[] loc={m[0]+location[0],m[1]+location[1],m[2]+location[2]};
			
			if((loc[0]|loc[1]|loc[2])>=0&&loc[0]<5&&loc[1]<5&&loc[2]<5&&(board.getAt(loc)==null||board.getAt(loc).owner!=owner))
				valid.add(loc);
		}
		return valid;
	}
}
