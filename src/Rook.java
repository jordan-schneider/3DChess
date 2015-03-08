import java.util.ArrayList;


public class Rook extends Piece {

	public Rook(int x, int y, int z, int owner, Board board) {
		super(x, y, z, owner, board,'R');
	}

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
