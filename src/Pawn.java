import java.util.ArrayList;


public class Pawn extends Piece {

	public static final int[][] wcaptures={
		{1,1,0},{-1,1,0},{1,0,1},{-1,0,1},{0,1,1}
	};
	public static final int[][] bcaptures={
		{1,-1,0},{-1,-1,0},{1,0,-1},{-1,0,-1},{0,-1,-1}
	};
	public Pawn(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'P');
	}

	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		int x=this.location[0];
		int y=this.location[1];
		int z=this.location[2];
		if(owner==RaumschachBoard.BLACK){
			if(x-1>=0&&y-1>=0&&board.getAt(new int[]{x-1,y-1,z})!=null&&board.getAt(new int[]{x-1,y-1,z}).owner!=owner)
				valid.add(new int[]{x-1,y-1,z});
			if(x-1>=0&&z-1>=0&&board.getAt(new int[]{x-1,y,z-1})!=null&&board.getAt(new int[]{x-1,y,z-1}).owner!=owner)
				valid.add(new int[]{x-1,y,z-1});
			if(x+1<5&&y-1>=0&&board.getAt(new int[]{x+1,y-1,z})!=null&&board.getAt(new int[]{x+1,y-1,z}).owner!=owner)
				valid.add(new int[]{x+1,y-1,z});
			if(x+1<5&&z-1>=0&&board.getAt(new int[]{x+1,y,z-1})!=null&&board.getAt(new int[]{x+1,y,z-1}).owner!=owner)
				valid.add(new int[]{x+1,y,z-1});
			if(y-1>0&&board.getAt(new int[]{x,y-1,z})==null)
				valid.add(new int[]{x,y-1,z});
			if(z-1>0&&board.getAt(new int[]{x,y,z-1})==null)
				valid.add(new int[]{x,y,z-1});
		}else{
			if(x-1>=0&&y+1<5&&board.getAt(new int[]{x-1,y+1,z})!=null&&board.getAt(new int[]{x-1,y+1,z}).owner!=owner)
				valid.add(new int[]{x-1,y+1,z});
			if(x-1>=0&&z+1<5&&board.getAt(new int[]{x-1,y,z+1})!=null&&board.getAt(new int[]{x-1,y,z+1}).owner!=owner)
				valid.add(new int[]{x-1,y,z+1});
			if(x+1<5&&y+1<5&&board.getAt(new int[]{x+1,y+1,z})!=null&&board.getAt(new int[]{x+1,y+1,z}).owner!=owner)
				valid.add(new int[]{x+1,y+1,z});
			if(x+1<5&&z+1<5&&board.getAt(new int[]{x+1,y,z+1})!=null&&board.getAt(new int[]{x+1,y,z+1}).owner!=owner)
				valid.add(new int[]{x+1,y,z+1});
			if(y+1<5&&board.getAt(new int[]{x,y+1,z})==null)
				valid.add(new int[]{x,y+1,z});
			if(z+1<5&&board.getAt(new int[]{x,y,z+1})==null)
				valid.add(new int[]{x,y,z+1});
			return valid;
		}
		return valid;
	}
}
