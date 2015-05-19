import java.util.ArrayList;
import java.util.Arrays;

/**
 * Raumschach specific Board
 * @author Jordan
 *
 */
public class RaumschachBoard extends Board {
	Piece[][][] board;
	Opponent white,black;

	/**
	 * Creates a new board for two opponents
	 * @param white opponent
	 * @param black opponent
	 */
	public RaumschachBoard(Opponent white, Opponent black) {
		board = new Piece[5][5][5];
		this.white=white;
		this.black=black;

		//Handling black's pieces
		//Using layout defined by http://www.chessvariants.org/3d.dir/3d5.html
		reset();
	}

	/**
	 * Adds a piece to the board
	 * @param piece
	 */
	private void add(Piece piece) {
		board[piece.location[0]][piece.location[1]][piece.location[2]] = piece;
	}

	/**
	 * Checks if a move is valid
	 * @param peice to check
	 * @param move to check piece for
	 * @return if the move is valid
	 */
	@Override
	public boolean isValidMove(Piece piece, int[] move) {
		if(move[0]<0||move[1]<0||move[2]<0||move[0]>4||move[1]>4||move[2]>4)
			return false;
		for(int[] a:piece.getMoves()){
			if(Arrays.equals(move, a)){
				int[] t=piece.location;
				Piece at=board[move[0]][move[1]][move[2]];
				if(at!=null)
					at.location=null;
				//make move
				piece.move(move);
				board[move[0]][move[1]][move[2]]=piece;
				boolean g=isCheck(piece.owner);	//check if the move reveals a pin.
				piece.move(t);	//undo move
				board[t[0]][t[1]][t[2]]=piece;
				board[move[0]][move[1]][move[2]]=at;
				if(at!=null)
					at.location=new int[]{move[0],move[1],move[2]};
				if(!g)
					return true;
			}
		}
		return false;
	}

	@Override
	/**
	 * @return a list of pieces on the board
	 */
	public ArrayList<Piece> getPieces() {
		ArrayList<Piece> r=new ArrayList<Piece>(whitepiece);
		r.addAll(blackpiece);
		return r;
	}

	/**
	 * @return a triplet with the x, y, and z size of the board
	 */
	@Override
	public int[] getSize() {
		return new int[]{board[0][0].length,board[0].length,board.length};
	}

	/**
	 * Gets pieces from the board
	 * @param loc triplet of piece
	 */
	@Override
	public Piece getAt(int[] loc) {
		if(loc[0]<0||loc[1]<0||loc[2]<0||loc[0]>4||loc[1]>4||loc[2]>4)
			return null;
		return this.board[loc[0]][loc[1]][loc[2]];
	}

	/**
	 * [MATTHEW WRite THIS I DON't KNOW WHAT IT DOES]
	 */
	public String toString(){
		StringBuilder str=new StringBuilder();
		str.append("A:abcde B:abcde C:abcde D:abcde E:abcde\n");
		for(int y=4;y>=0;y--){
			for(int z=0;z<5;z++){
				str.append((y+1)+" ");
				for(int x=0;x<5;x++)
					if(this.getAt(new int[]{x,y,z})==null)
						str.append(".");
					else
						str.append(this.getAt(new int[]{x,y,z}).getSymbol());
				str.append(" ");
			}
			str.append('\n');
		}


		return str.toString();

	}
	
	/**
	 * Checks if a player is in check
	 * @param player to check
	 * @return if the player is in check
	 */
	public boolean isCheck(int player){
		int[] k=null;
		for(Piece p:(player==WHITE)?whitepiece:blackpiece)
			if(p instanceof King)
				k=p.location;
		for(Piece p:(player==WHITE)?blackpiece:whitepiece){
			if(p.location!=null)
				for(int[] m:p.getMoves())
					if(Arrays.equals(m, k)){
						System.out.println("C: "+p.getClass().getName()+" "+RaumschachBoard.moveToString(p.location)+"->"+RaumschachBoard.moveToString(m));
						
						return true;
					}
		}
		return false;
	}
	
	/**
	 * Checks if the given player is in checkmate
	 */
	public boolean isCheckmate(int player){
		if(!isStalemate(player))
			return false;
		return isCheck(player);
	}

	/**
	 * Set the given peice's location to the given location
	 */
	@Override
	public void setAt(Piece piece, int[] to) {
		board[to[0]][to[1]][to[2]]=piece;
	}

	/**
	 * Checks if the game is in stalemate
	 */
	@Override
	public boolean isStalemate(int player,boolean refute) {
		for(Piece p:(player==WHITE?whitepiece:blackpiece)){
			for(int[] m:p.getMoves()){
				if((p instanceof Bishop))
					System.out.println("A: "+p.getClass().getName()+" "+RaumschachBoard.moveToString(p.location)+"->"+RaumschachBoard.moveToString(m));
				//for reversal
				int[] t=p.location;
				//System.out.println(p.getClass().getName()+" "+RaumschachBoard.moveToString(t)+"->"+RaumschachBoard.moveToString(m));
				Piece at=board[m[0]][m[1]][m[2]];
				if(at!=null)
					at.location=null;
				//make move
				p.move(m);
				board[m[0]][m[1]][m[2]]=p;
				//test if legal
				if(!isCheck(player)){
					//undo move
					if(refute)
						System.out.println("Refute:" + p.getClass().getName()+": "+RaumschachBoard.moveToString(t)+"->"+RaumschachBoard.moveToString(m));
					p.move(t);
					board[t[0]][t[1]][t[2]]=p;
					board[m[0]][m[1]][m[2]]=at;
					if(at!=null)
						at.location=new int[]{m[0],m[1],m[2]};
					System.out.println("yo?");
					return false;	//legal move
				}
				//undo move
				p.move(t);
				board[t[0]][t[1]][t[2]]=p;
				board[m[0]][m[1]][m[2]]=at;
				if(at!=null)
					at.location=new int[]{m[0],m[1],m[2]};
			}
		}

		return true;
	}
	
	/**
	 * Converts location to a string
	 * @param t location
	 * @return string representation of the location
	 */
	public static String moveToString(int[] t){
		byte[] b=new byte[]{(byte)(t[2]+'A'),(byte)(t[0]+'a'),(byte)(t[1]+'1')};
		return new String(b);
	}

	/**
	 * Resets the board
	 */
	@Override
	public void reset() {
		black.pieces.clear();
		white.pieces.clear();
		whitepiece.clear();
		blackpiece.clear();
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				for(int k=0;k<5;k++)
					board[i][j][k]=null;
		King king = new King(2,4,4,BLACK,this);
		black.add(king);
		add(king);

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
			for(int x=0;x<5;x++){
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

		bishop = new Bishop(4,4,3,BLACK,this);
		black.add(bishop);
		add(bishop);

		Unicorn unicorn = new Unicorn(1,4,3,BLACK,this);
		black.add(unicorn);
		add(unicorn);

		unicorn = new Unicorn(3,4,3,BLACK,this);
		black.add(unicorn);
		add(unicorn);

		
		king = new King(2,0,0,WHITE,this);
		white.add(king);
		add(king);

		rook = new Rook(0,0,0,WHITE,this);
		white.add(rook);
		add(rook);

		rook = new Rook(4,0,0,WHITE,this);
		white.add(rook);
		add(rook);

		knight = new Knight(1,0,0,WHITE,this);
		white.add(knight);
		add(knight);

		knight = new Knight(3,0,0,WHITE,this);
		white.add(knight);
		add(knight);

		for(int z=0;z<2;z++){
			for(int x=0;x<5;x++){
				pawn = new Pawn(x,1,z,WHITE,this);
				white.add(pawn);
				add(pawn);
			}
		}

		queen = new Queen(2,0,1,WHITE,this);
		white.add(queen);
		add(queen);

		bishop = new Bishop(0,0,1,WHITE,this);
		white.add(bishop);
		add(bishop);

		bishop = new Bishop(4,0,1,WHITE,this);
		white.add(bishop);
		add(bishop);

		unicorn = new Unicorn(1,0,1,WHITE,this);
		white.add(unicorn);
		add(unicorn);

		unicorn = new Unicorn(3,0,1,WHITE,this);
		white.add(unicorn);
		add(unicorn);
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				for(int k=0;k<5;k++)
					if(board[i][j][k]!=null&&board[i][j][k].owner==RaumschachBoard.WHITE)
						whitepiece.add(board[i][j][k]);
					else if(board[i][j][k]!=null)
						blackpiece.add(board[i][j][k]);
		
	}
}
