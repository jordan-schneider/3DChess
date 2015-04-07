/**
 * Required methods for all user interfaces.
 * @author Matthew
 *
 */
public interface UI {
	/**
	 * Called to by Game objects to setup the UI
	 * @param opps
	 * @param ids
	 * @param g
	 */
	public void init(Opponent[] opps,long ids[],Game g);
	/**
	 * The UI has permission to make moves for human players. getMove(id) may call game.makeMove().
	 * @param id
	 */
	public void getMove(long id);
	public void reloadBoard();
	public boolean informMove(int[] from,int[] to,long id);
}
