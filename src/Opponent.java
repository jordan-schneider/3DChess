import java.util.ArrayList;


public abstract class Opponent {
	Game game;
	ArrayList<Piece> pieces;
	long id;
	
	public int[] requestMove(){
		return null;
	}

	public void add(Piece piece) {
		pieces.add(piece);
	}
}
