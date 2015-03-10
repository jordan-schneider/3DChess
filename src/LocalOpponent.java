
public class LocalOpponent extends Opponent {
	public LocalOpponent(long id) {
		super(id);
	}
	
	
	public void requestMove(){
		// TODO hook this up to UI pipe
		game.ui.getMove(id);
	}
	public boolean isHuman(){
		return true;
	}


	@Override
	void informMove(int[] from, int[] to) {}


	@Override
	public boolean makeMove() { return false; }


	@Override
	public boolean makeMove(int[] f, int[] t) {
		return game.makeMove(f, t, id);
	}

}
