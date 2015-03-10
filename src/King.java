import java.util.ArrayList;


public class King extends Piece {
	boolean hasMoved;
	public static final int[][] moves={
		{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}
	};
	public King(int x, int y, int z, int owner, Board board){
		super(x,y,z,owner,board,'K');
		hasMoved = false;
	}

	@Override
	public void move(int[] to) {
		super.move(to);
		hasMoved = true;
	}

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
