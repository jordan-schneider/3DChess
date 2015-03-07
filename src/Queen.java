import java.util.ArrayList;

/**
 * Combines the behavior of Rooks, Bishops, and Unicorn
 * @author Matthew
 *
 */
public class Queen extends Piece {

	public Queen(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board);
	}

	@Override
	ArrayList<int[]> getMoves() {
		ArrayList<int[]> valid=new ArrayList<int[]>();
		//Rook-like
		for(int i=0;i<3;i++){
			int[] loc=this.location.clone();
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
		//Bishop-like
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
		//Unicorn-like
		for(int i=-1;i<2;i+=2)
			for(int j=-1;j<2;j+=2)
				for(int k=-1;k<2;k+=2){
					int[] loc=this.location.clone();
					for(;loc[0]<board.getSize()[0]&&loc[1]<board.getSize()[1]&&loc[2]<board.getSize()[2];loc[0]+=i,loc[1]+=j,loc[2]+=k){
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
