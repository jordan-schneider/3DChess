import java.util.ArrayList;
import java.util.List;

/**
 * Generic board object
 * Stores information about the pieces on it
 * @author Jordan
 *
 */
public abstract class Board {
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	protected List<Piece> whitepiece=new ArrayList<Piece>();
	protected List<Piece> blackpiece=new ArrayList<Piece>();
	
	/**
	 * @return All pieces on the {@code Board}.
	 */
	public abstract ArrayList<Piece> getPieces();
	/**
	 * 
	 * @return Dimension of the {@code Board} in {@code int[3]} format
	 */
	public abstract int[] getSize();
	/**
	 * 
	 * @param loc - piece location
	 * @return Piece at {@code loc}
	 */
	public abstract Piece getAt(int[] loc);
	/**
	 * Sets the {@code Piece} located at {@code to} to {@code piece}
	 * @param piece
	 * @param to
	 */
	public abstract void setAt(Piece piece,int[] to);
	/**
	 * @param piece
	 * @param move
	 * @return {@code true} if the move of {@code piece} to {@code move} is legal.
	 */
	public abstract boolean isValidMove(Piece piece, int[] move);
	/**
	 * Non-verbose
	 * @param player
	 * @return {@code true} if {@code player} is in stalemate
	 */
	public boolean isStalemate(int player){
		return isStalemate(player,false);
	}
	/**
	 * @param player
	 * @param refute - verbose refute or not
	 * @return {@code true} if {@code player} is in stalemate
	 */
	public abstract boolean isStalemate(int player,boolean refute);
	/**
	 * @param player
	 * @return {@code true} if {@code player} is in checkmate
	 */
	public abstract boolean isCheckmate(int player);
	/**
	 * @param player
	 * @return {@code true} if {@code player} is in check
	 */
	public abstract boolean isCheck(int player);
	
	/**
	 * Resets the board state
	 * Should work while resetting the whole game
	 */
	public abstract void reset();
}
