import java.util.ArrayList;

public interface Board {
	public ArrayList<Piece> getPieces();

	public boolean isValidMove(Piece piece, int[] move);
}
