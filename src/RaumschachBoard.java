import java.util.ArrayList;


public class RaumschachBoard implements Board {
	private static final int WHITE = 0;
	private static final int BLACK = 1;
	Piece[][][] board;

	public RaumschachBoard(Opponent white, Opponent black) {
		board = new Piece[5][5][5];

		//Handling black's pieces
		//Using layout defined by http://www.chessvariants.org/3d.dir/3d5.html
		King king = new King(2,4,4,BLACK,this);
		black.add(king);
		add(king);

		//TODO finish other piece classes
		Rook rook = new Rook(0,4,4,BLACK,this);
		black.add(rook);
		add(rook);

		rook = new Rook(4,4,4,BLACK,this);
		black.add(rook);
		add(rook);

		Knight knight = new Knight(1,4,4,BLACK,this);
		black.add(knight);
		add(knight);

		knight = new Knight(3,4,4,BLACK,this);
		black.add(knight);
		add(knight);

		Pawn pawn;
		for(int z=3;z<5;z++){
			for(int x=0;i<5;i++){
				pawn = new Pawn(x,3,z,BLACK,this);
				black.add(pawn);
				add(pawn);
			}
		}
		
		Queen queen = new Queen(2,4,3,BLACK,this);
		black.add(queen);
		add(queen);
		
		Bishop bishop = new Bishop(0,4,3,BLACK,this);
		black.add(bishop);
		add(bishop);
		
		bishop = new Bishop(3,4,3,BLACK,this);
		black.add(bishop);
		add(bishop);
		
		Unicorn unicorn = new Unicorn(1,4,3,BLACK,this);
		black.add(unicorn);
		add(unicorn);
		
		unicorn = new Unicorn(3,4,3,BLACK,this);
		black.add(unicorn);
		add(unicorn);
		
		//TODO finish white later
	}

	private void add(Piece piece) {
		board[piece.location[0]][piece.location[1]][piece.location[2]] = piece;
	}
	
	@Override
	public boolean isValidMove(Piece piece, int[] move) {
		// TODO Write isValidMove
		return false;
	}

	@Override
	public ArrayList<Piece> getPieces() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for(Piece[][] plane:board){
			for(Piece[] row:plane){
				for(Piece p: row){
					if(p!=null){
						pieces.add(p);
					}
				}
			}
		}
		return pieces;
	}

}
