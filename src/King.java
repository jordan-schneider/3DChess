import java.util.ArrayList;


public class King extends Piece {
	boolean hasMoved;

	public King(int x, int y, int z, int owner, Board board){
		super(x,y,z,owner,board);
		hasMoved = false;
	}

	@Override
	public void move(int[] to) {
		super.move(to);
		hasMoved = false;
	}

	@Override
	public ArrayList<int[]> getMoves() {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		int[] zPlusOne = {location[0],location[1],location[2]+1};
		if(board.isValidMove(this,zPlusOne)) moves.add(zPlusOne);
		int[] zMinusOne = {location[0],location[1],location[2]-1};
		if(board.isValidMove(this,zMinusOne)) moves.add(zMinusOne);
		int[] yPlusOne = {location[0],location[1]+1,location[2]};
		if(board.isValidMove(this,yPlusOne)) moves.add(yPlusOne);
		int[] yMinusOne = {location[0],location[1]-1,location[2]};
		if(board.isValidMove(this,yMinusOne)) moves.add(yMinusOne);
		int[] xPlusOne = {location[0]+1,location[1],location[2]};
		if(board.isValidMove(this,xPlusOne)) moves.add(xPlusOne);
		int[] xMinusOne = {location[0]-1,location[1],location[2]};
		if(board.isValidMove(this,xMinusOne)) moves.add(xMinusOne);
		if(!hasMoved && isLeftClear()); //TODO handle castling, write isLeft/RightClear() methods
		if(!hasMoved && isRightClear());
		return moves;
	}
}
