
/**
 * Game runs a process that sends move requests to players when it is their turn. The Game also accepts moves from players when they are ready to submit their moves.
 * @author Matthew 
 */
public class Game extends Thread{
	Board board;
	private Opponent[] players;
	private long[] ids;
	int movesIn=0;
	long[] timeLeft=new long[2];
	long timeOfLastAction=-1;
	TimeControl tc;
	int cPlayer;
	public final UI ui;
	boolean started;
	boolean changed=false;
	/**
	 * Initializes the Game and the UI manager.
	 * @param p1 - First player
	 * @param p1id - First player id
	 * @param p2 - Second player
	 * @param p2id - Second player id
	 * @param ui - UI manager for the game
	 */
	public Game(Opponent p1,long p1id,Opponent p2,long p2id,UI ui,TimeControl tc){
		players = new Opponent[2];
		players[0]=p1;
		players[1]=p2;
		board = new RaumschachBoard(players[0],players[1]);
		cPlayer = 0;
		started = false;
		this.tc=tc;
		timeLeft[0]=tc.sTime[0]*1000;
		timeLeft[1]=tc.sTime[1]*1000;
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
	public synchronized void playerChanged(){
		changed=true;
		System.out.println("Yo");
	}
	public synchronized boolean getPlayerChanged(){
		if(changed){
			changed=false;
			return true;
		}
		return false;
	}
	public void run(){
		while(!board.isStalemate(cPlayer)&&timeLeft[1-cPlayer]>0){
			System.out.println("everywhere");
			int ctemp=cPlayer;
			players[cPlayer].requestMove();
			timeOfLastAction=System.currentTimeMillis();
			timeLeft[cPlayer]+=tc.incr[cPlayer]*1000;
			
			while(!getPlayerChanged()&&(System.currentTimeMillis()-timeOfLastAction+tc.delay[cPlayer]*1000)<timeLeft[cPlayer]);
			System.out.println("here");
			if(cPlayer==ctemp)
				break;
			else if(System.currentTimeMillis()-timeOfLastAction>1000*tc.delay[1-cPlayer])
				timeLeft[1-cPlayer]+=timeOfLastAction-System.currentTimeMillis()+1000*tc.delay[1-cPlayer];
		}
		System.out.println("there");
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
		playerChanged();
		return true;
	}
}
