import java.util.ArrayList;


public class Unicorn extends Piece {

	/**
	 * Creates a new unicorn
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @param owner of piece, either 0 or 1
	 * @param board piece is on
	 */
	public Unicorn(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'U');
	}

	/**
	 * @returns list of valid moves
	 */
	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		for(int i=-1;i<2;i+=2)
			for(int j=-1;j<2;j+=2)
				for(int k=-1;k<2;k+=2){
					int[] loc=this.location.clone();
					loc[0]+=i;loc[1]+=j;loc[2]+=k;
					for(;loc[0]>=0&&loc[1]>=0&&loc[2]>=0&&loc[0]<board.getSize()[0]&&loc[1]<board.getSize()[1]&&loc[2]<board.getSize()[2];loc[0]+=i,loc[1]+=j,loc[2]+=k){
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
