
public class HumanPlayer extends Opponent {
	private static final int WHITE = 0;
	private static final int BLACK = 1;
	
	Piece[] pieces;
	int id;
	public HumanPlayer(Board b, int id) {
		pieces = new Piece[20];
		
		this.id = id;		
	}
	
	
	public int[] requestMove(){
		// TODO hook this up to UI pipe
		return null;
	}

}
