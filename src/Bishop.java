import java.util.ArrayList;

/**
 * Raumschach Bishops move like bishops in 3 planes.
 * @author Matthew
 *
 */
public class Bishop extends Piece {

	public Bishop(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'B');
	}

	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		for(int i=0;i<3;i++){
			for(int j=i+1;j<3;j++){
				int[] loc=this.location.clone();
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
