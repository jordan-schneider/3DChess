import java.util.ArrayList;

/**
 * A Raumschach Knight
 * Moves using the "two in one direction, one in another" rule. With a third dimension, you now have two options for the "other direction"
 * @author Jordan
 *
 */
public class Knight extends Piece {
	public static final int[][] movePattern = {
		{0,1,2},{0,2,1},{1,0,2},{1,2,0},{2,0,1},{2,1,0},
		{0,-1,2},{0,2,-1},{-1,0,2},{-1,2,0},{2,0,-1},{2,-1,0},
		{0,1,-2},{0,-2,1},{1,0,-2},{1,-2,0},{-2,0,1},{-2,1,0},
		{0,-1,-2},{0,-2,-1},{-1,0,-2},{-1,-2,0},{-2,0,-1},{-2,-1,0}
	};
	
	/**
	 * Makes a new Knight
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @param owner of piece, either 0 or 1
	 * @param board piece is on
	 */
	public Knight(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'N');
	}

	/**
	 * Returns a list of valid moves.
	 * Moves are valid if they are 2 away in 1 axis and 1 away in another.
	 */
	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		for(int[] i:movePattern){
			int[] tent= {i[0]+this.location[0],i[1]+this.location[1],i[2]+this.location[2]};
			if(tent[0]>=0&&tent[1]>=0&&tent[2]>=0&&tent[0]<board.getSize()[0]&&tent[1]<board.getSize()[1]&&tent[2]<board.getSize()[2])
				if(board.getAt(tent)==null||board.getAt(tent).owner!=owner)
					valid.add(tent);
		}
		return valid;
	}

}
