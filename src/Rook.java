import java.util.ArrayList;


public class Rook extends Piece {

	/**
	 * Creates a new rook
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @param owner of piece, either 0 or 1
	 * @param board piece is on
	 */
	public Rook(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'R');
	}

	/**
	 * @returns list of valid moves
	 */
	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		for(int i=0;i<3;i++){
			int[] loc=this.location.clone();
			loc[i]++;
			for(;loc[i]<board.getSize()[i];loc[i]++){

				final Piece p=board.getAt(loc);
				if(p==null)
					valid.add(loc.clone());
				else if(p.owner==owner)
					break;
				else{
					valid.add(loc.clone());
					break;
				}
			}
			loc=this.location.clone();
			loc[i]--;
			for(;loc[i]>=0;loc[i]--){
				final Piece p=board.getAt(loc);
				if(p==null)
					valid.add(loc.clone());
				else if(p.owner==owner)
					break;
				else{
					valid.add(loc.clone());
					break;
				}
			}
		}
		return valid;
	}


}
