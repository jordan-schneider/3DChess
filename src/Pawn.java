import java.util.ArrayList;


public class Pawn extends Piece {

	public static final int[][] captures={
		{1,1,0},{-1,1,0},{1,0,1},{-1,0,1},{0,1,1}
	};
	public Pawn(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board);
	}

	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		if(location[1]<board.getSize()[1])
			if(board.getAt(new int[]{location[0],location[1]+1,location[2]})==null)
				valid.add(new int[]{location[0],location[1]+1,location[2]});
		if(location[2]<board.getSize()[2])
			if(board.getAt(new int[]{location[0],location[1],location[2]+1})==null)
				valid.add(new int[]{location[0],location[1],location[2]+1});
		for(int[] i:captures){
			int[] loc={this.location[0]+i[0],this.location[1]+i[1],this.location[2]+i[2]};
			if(loc[0]>=0&&loc[1]>=0&&loc[2]>=0&&loc[0]<=board.getSize()[0]&&loc[1]<=board.getSize()[1]&&loc[2]<=board.getSize()[2])
				if(board.getAt(loc)!=null&&board.getAt(loc).owner!=owner)
					valid.add(i);
		}
		return valid;
	}

}
