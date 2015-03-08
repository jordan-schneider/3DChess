import java.util.ArrayList;
import java.util.List;

public abstract class Board {
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	protected List<Piece> whitepiece=new ArrayList<Piece>();
	protected List<Piece> blackpiece=new ArrayList<Piece>();
	public abstract ArrayList<Piece> getPieces();
	public abstract int[] getSize();
	public abstract Piece getAt(int[] loc);
	public abstract void setAt(Piece piece,int[] to);
	public abstract boolean isValidMove(Piece piece, int[] move);
	public abstract boolean isStalemate(int player);
	public abstract boolean isCheckmate(int player);
}
