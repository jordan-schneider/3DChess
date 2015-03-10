
/**
 * Game runs a process that sends move requests to players when it is their turn. The Game also accepts moves from players when they are ready to submit their moves.
 * @author Matthew 
 */
public class Game extends Thread{
	Board board;
	private Opponent[] players;
	private long[] ids;
	int cPlayer;
	public final UI ui;
	boolean started;
	/**
	 * Initializes the Game and the UI manager.
	 * @param p1 - First player
	 * @param p1id - First player id
	 * @param p2 - Second player
	 * @param p2id - Second player id
	 * @param ui - UI manager for the game
	 */
	public Game(Opponent p1,long p1id,Opponent p2,long p2id,UI ui){
		players = new Opponent[2];
		players[0]=p1;
		players[1]=p2;
		board = new RaumschachBoard(players[0],players[1]);
		cPlayer = 0;
		started = false;
		p1.init(this, p1id);
		p2.init(this, p2id);
		ids=new long[]{p1id,p2id};
		this.ui=ui;
		ui.init(players,ids,this);
	}
	public void start(){
		this.started=true;
		super.start();
	}
	public void run(){
		while(!board.isStalemate(cPlayer)){
			int ctemp=cPlayer;
			players[cPlayer].requestMove();
			while(cPlayer==ctemp){}

		}
	}
	/**
	 * Attempts to make a piece move from one location to another.
	 * @param pieceSpot - starting location of the piece
	 * @param moveSpot - destination of the piece
	 * @param cid - player id of the current player (to whom the piece must belong)
	 * @return {@code true} if the move was successful, and {@code false} otherwise.
	 * 
	 */
	public boolean makeMove(int[] pieceSpot, int[] moveSpot, long cid){
		if(pieceSpot==null||pieceSpot.length!=3||moveSpot==null||moveSpot.length!=3)
			return false;
		if(cid!=ids[cPlayer])
			return false;
		if(board.getAt(pieceSpot)==null||board.getAt(pieceSpot).owner!=cPlayer)
			return false;
		if(!board.isValidMove(board.getAt(pieceSpot), moveSpot))
			return false;
		if(board.getAt(moveSpot)!=null)
			if(this.cPlayer==Board.BLACK)
				board.whitepiece.remove(board.getAt(moveSpot));
			else
				board.blackpiece.remove(board.getAt(moveSpot));
		board.setAt(board.getAt(pieceSpot),moveSpot);
		board.getAt(moveSpot).move(moveSpot);
		board.setAt(null,pieceSpot);
		cPlayer=(cPlayer+1)%2;
		return true;
	}
}
