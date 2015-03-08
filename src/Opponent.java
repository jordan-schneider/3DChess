import java.util.ArrayList;


public abstract class Opponent {
	protected Game game;
	ArrayList<Piece> pieces=new ArrayList<Piece>();
	protected final long id;
	public Opponent(long id){
		this.id=id;
	}
	public boolean init(Game g,long tid){
		if(tid==id)
			game=g;
		else
			return false;
		return true;
		
	}
	abstract void requestMove();
	abstract void informMove(int[] from,int[] to);

	public void add(Piece piece) {
		pieces.add(piece);
	}
	public abstract boolean isHuman();
	public abstract boolean makeMove();
	public abstract boolean makeMove(int[] f,int[] t);
}
