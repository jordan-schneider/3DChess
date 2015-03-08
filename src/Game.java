import java.util.Arrays;


public class Game extends Thread{
	Board board;
	private Opponent[] players;
	private long[] ids;
	int cPlayer;
	public final UI ui;
	boolean started;

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
		while(true){
			int ctemp=cPlayer;
			System.out.println(players[0]);
			players[cPlayer].requestMove();
			while(cPlayer==ctemp){}
			
		}
	}

	public boolean makeMove(int[] pieceSpot, int[] moveSpot, long cid){
		if(pieceSpot==null||pieceSpot.length!=3||moveSpot==null||moveSpot.length!=3)
			return false;
		if(cid!=ids[cPlayer])
			return false;
		if(board.getAt(pieceSpot)==null||board.getAt(pieceSpot).owner!=cPlayer)
			return false;
		if(!board.isValidMove(board.getAt(pieceSpot), moveSpot))
			return false;
		board.setAt(board.getAt(pieceSpot),moveSpot);
		board.getAt(moveSpot).move(moveSpot);
		board.setAt(null,pieceSpot);
		cPlayer=(cPlayer+1)%2;
		return true;
	}
}
