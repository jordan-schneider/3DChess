import java.util.ArrayList;

/**
 * Raumschach Bishops move like normal chess bishops in 3 any of the facially aligned planes.
 * @author Matthew
 *
 */
public class Bishop extends Piece {
	/**
	 * Standard Piece/Bishop constructor.
	 * @param x - initial x-coordinate
	 * @param y - initial y-coordinate
	 * @param z - initial z-coordinate
	 * @param owner - black or white
	 * @param board - associated board
	 */
	public Bishop(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'B');
	}

	/**
	 * Returns the valid moves of the bishop.
	 * A move is valid if it is in a diagonal in any plane of the board from the bishop.
	 * In other words, valid moves are multiples of either (0,1,1), (1,1,0), or (1,0,1)
	 */
	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		for(int i=0;i<3;i++){
			for(int j=i+1;j<3;j++){
				int[] loc=this.location.clone();
				loc[i]++;loc[j]++;
				for(;loc[i]<board.getSize()[i]&&loc[j]<board.getSize()[j];loc[i]++,loc[j]++){
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
				loc[i]--;loc[j]--;
				for(;loc[i]>=0&&loc[j]>=0;loc[i]--,loc[j]--){
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
				loc[i]++;loc[j]--;
				for(;loc[i]<board.getSize()[i]&&loc[j]>=0;loc[i]++,loc[j]--){
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
				loc[i]--;loc[j]++;
				for(;loc[i]>=0&&loc[j]<board.getSize()[j];loc[i]--,loc[j]++){
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
		}
		return valid;
	}

}
