
public class Game {
	Board board;
	Opponent[] players;
	int cPlayer;
	
	public void init(){
		//TODO un-hardcode this
		board = new RaumschachBoard(players[0],players[1]);
		players = new Opponent[2];
		cPlayer = 0;
	}
	
	public void makeMove(int[] pieceSpot, int[] moveSpot, int player){
		
	}
}
