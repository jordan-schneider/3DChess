import java.util.ArrayList;

/**
 * 
 * @author Matthew
 *
 */
public abstract class Opponent {
	protected Game game;
	ArrayList<Piece> pieces=new ArrayList<Piece>();
	protected final long id;	//id assigned to player for security
	/**
	 * 
	 * @param id
	 */
	public Opponent(long id){
		this.id=id;
	}
	/**
	 * Initializes the {@code Opponent} by passing the {@code Game} object to it.
	 * @param g - {@code Game} game
	 * @param tid - {@code long} id for verification
	 * @return
	 */
	public boolean init(Game g,long tid){
		if(tid==id)
			game=g;
		else
			return false;
		return true;
		
	}
	/**
	 * Function called by {@code Game} to inform the {@code Opponent} that it is its turn.
	 */
	abstract void requestMove();
	/**
	 * Function called by {@code Game} to inform the {@code Opponent} of a move that was made.
	 * @param from - source location on the {@code Board}
	 * @param to - destination location on the {@code Board}
	 */
	abstract void informMove(int[] from,int[] to);
	/**
	 * @param piece - to add to {@code pieces} ArrayList.
	 */
	public void add(Piece piece) {
		pieces.add(piece);
	}
	/**
	 * @return {@code true} if the {@code Opponent} is human controlled.
	 */
	public abstract boolean isHuman();
	/**
	 * Function is mostly reserved for non-human players.  This function can be called by {@code UI} managers to ask the player to make a move.
	 * @return {@code true} if the move is successful.
	 */
	public abstract boolean makeMove();
	/**
	 * Function is mostly reserved for human players.  This function can be called by {@code UI} managers to ask the player to make a move from {@code from} to {@code to}.
	 * @return {@code true} if the move is successful.
	 * @param from - source location
	 * @param to - destination location
	 */
	public abstract boolean makeMove(int[] from,int[] to);
}
