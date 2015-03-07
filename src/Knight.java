import java.util.ArrayList;


public class Knight extends Piece {
	public static final int[][] movePattern = {
		{0,1,2},{0,2,1},{1,0,2},{1,2,0},{2,0,1},{2,1,0},
		{0,-1,2},{0,2,-1},{-1,0,2},{-1,2,0},{2,0,-1},{2,-1,0},
		{0,1,-2},{0,-2,1},{1,0,-2},{1,-2,0},{-2,0,1},{-2,1,0},
		{0,-1,-2},{0,-2,-1},{-1,0,-2},{-1,-2,0},{-2,0,-1},{-2,-1,0}
	};
	public Knight(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board);
	}

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
