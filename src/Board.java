import java.util.ArrayList;

public interface Board {
	public ArrayList<Piece> getPieces();
	public int[] getSize();
	public Piece getAt(int[] loc);
	public void setAt(Piece piece,int[] to);
	public boolean isValidMove(Piece piece, int[] move);
}
