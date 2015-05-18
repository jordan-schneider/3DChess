
/**
 * Game runs a process that sends move requests to players when it is their turn. The Game also accepts moves from players when they are ready to submit their moves.
 * @author Matthew 
 */
public class Game extends Thread{
	Board board;
	private Opponent[] players;
	private long[] ids;
	int movesIn;
	long[] timeLeft=new long[2];
	long timeOfLastAction;
	TimeControl tc;
	int cPlayer;
	public final UI ui;
	boolean started;
	boolean changed;
	/**
	 * Initializes the Game and the UI manager.
	 * @param p1 - First player
	 * @param p1id - First player id
	 * @param p2 - Second player
	 * @param p2id - Second player id
	 * @param ui - UI manager for the game
	 */
	public Game(Opponent p1,Opponent p2,UI ui,TimeControl tc){
		players = new Opponent[2];
		players[0]=p1;
		players[1]=p2;
		cPlayer = 0;
		movesIn=0;
		timeOfLastAction=-1;
		started = false;
		changed = false;
		this.tc=tc;
		timeLeft[0]=tc.sTime[0]*60000;
		timeLeft[1]=tc.sTime[1]*60000;
		ids=new long[]{p1.id,p2.id};
		this.ui=ui;
	}
	
	public void init(){
		board = new RaumschachBoard(players[0],players[1]);
		players[0].init(this, players[0].id);
		players[1].init(this, players[1].id);
	}
	boolean kill=false;
	public synchronized void reset(){
		kill = true;
		board.reset();
		ui.reloadBoard();
		timeLeft[0]=tc.sTime[0]*60000;
		timeLeft[1]=tc.sTime[1]*60000;
		cPlayer = 0;
		movesIn=0;
		timeOfLastAction=-1;
		changed = false;
		System.out.println("Killed");
	}
	public void start(){
		if(!started){
			this.started=true;
			super.start();
		}else{
			this.run();
		}

	}
	long pausedAt=-1;
	long unPausedAt=-1;
	public synchronized void pauseGame(){
		if(pausedAt!=-1||unPausedAt!=-1)
			return;
		pausedAt=System.currentTimeMillis();
	}
	public synchronized void unPauseGame(){
		if(pausedAt==-1||unPausedAt!=-1)
			return;
		unPausedAt=System.currentTimeMillis();
	}
	public synchronized void playerChanged(){
		changed=true;
	}
	public synchronized boolean getPlayerChanged(){
		if(changed){
			changed=false;
			return true;
		}
		return false;
	}

	//Fix for paused time
	public String getTime(int player){
		long ztime;
		if(pausedAt!=-1)
			ztime=pausedAt;
		else
			ztime=System.currentTimeMillis();
		if(cPlayer==Board.WHITE){
			if(player==Board.WHITE)
				if(ztime-timeOfLastAction>1000*tc.delay[Board.WHITE])
					return TimeControl.timeToString(timeLeft[Board.WHITE]+timeOfLastAction-ztime+1000*tc.delay[Board.WHITE]);
				else
					return TimeControl.timeToString(timeLeft[Board.WHITE]);
			return TimeControl.timeToString(timeLeft[Board.BLACK]);
		}else{
			if(player==Board.BLACK)
				if(ztime-timeOfLastAction>1000*tc.delay[Board.BLACK])
					return TimeControl.timeToString(timeLeft[Board.BLACK]+timeOfLastAction-ztime+1000*tc.delay[Board.BLACK]);
				else
					return TimeControl.timeToString(timeLeft[Board.BLACK]);
			return TimeControl.timeToString(timeLeft[Board.WHITE]);
		}
	}
	public void run(){
		while(!board.isStalemate(cPlayer)&&timeLeft[1-cPlayer]>0){
			System.out.println("everywhere");
			if(kill)
				break;
			int ctemp=cPlayer;

			timeOfLastAction=System.currentTimeMillis();
			timeLeft[cPlayer]+=tc.incr[cPlayer]*1000;

			while(!kill&&((pausedAt!=-1)||!getPlayerChanged()&&(System.currentTimeMillis()-timeOfLastAction+tc.delay[cPlayer]*1000)<timeLeft[cPlayer])){
				try {
					Thread.sleep(0,50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(kill)
					break;
				if(pausedAt!=-1){
					if(unPausedAt!=-1){
						System.out.println("GG");
						timeOfLastAction+=unPausedAt-pausedAt;
						unPausedAt=-1;
						pausedAt=-1;
					}
				}
			}
			if(kill)
				break;

			System.out.println("here");
			if(cPlayer==ctemp)
				break;
			else if(System.currentTimeMillis()-timeOfLastAction>1000*tc.delay[1-cPlayer])
				timeLeft[1-cPlayer]+=timeOfLastAction-System.currentTimeMillis()+1000*tc.delay[1-cPlayer];
		}
		System.out.println("there");
		kill=false;
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
		if(pausedAt!=-1||pieceSpot==null||pieceSpot.length!=3||moveSpot==null||moveSpot.length!=3)
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
		players[cPlayer].informMove(pieceSpot, moveSpot);
		playerChanged();
		return true;
	}
}
