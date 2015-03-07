import java.util.ArrayList;

public interface Board {
	public ArrayList<Piece> getPieces();
	public int[] getSize();
	public Piece getAt(int[] loc);
	public boolean isValidMove(Piece piece, int[] move);
}
