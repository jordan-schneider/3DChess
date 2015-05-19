/**
 * Opponent that interacts with the GUI rather than the base code
 * @author Jordan
 *
 */
public class LocalOpponent extends Opponent {

	/**
	 * Apparently this is required even though its the same
	 * @param id of player, either 0 or 1
	 */
	public LocalOpponent(long id) {
		super(id);
	}

	/**
	 * Asks the game to make a move
	 */
	public void requestMove(){
		game.ui.getMove(id);
	}

	/**
	 * Hardcoded implentation of Opponent method
	 */
	public boolean isHuman(){
		return true;
	}

	/**
	 * Unusued
	 */
	@Override
	void informMove(int[] from, int[] to) {}


	/**
	 * Unused
	 */
	@Override
	public boolean makeMove() { return false; }

	/**
	 * Makes a move based on UI input
	 */
	@Override
	public boolean makeMove(int[] f, int[] t) {
		return game.makeMove(f, t, id);
	}

}
