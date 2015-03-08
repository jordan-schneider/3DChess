import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RaumschachBoard extends Board {
	Piece[][][] board;
	private List<Piece> whitepiece=new ArrayList<Piece>();
	private List<Piece> blackpiece=new ArrayList<Piece>();

	public RaumschachBoard(Opponent white, Opponent black) {
		board = new Piece[5][5][5];

		//Handling black's pieces
		//Using layout defined by http://www.chessvariants.org/3d.dir/3d5.html
		King king = new Raum_King(2,4,4,BLACK,this);
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

		//TODO finish white later
		king = new Raum_King(2,0,0,WHITE,this);
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

	private void add(Piece piece) {
		board[piece.location[0]][piece.location[1]][piece.location[2]] = piece;
	}

	@Override
	public boolean isValidMove(Piece piece, int[] move) {
		for(int[] a:piece.getMoves()){
			if(Arrays.equals(move, a))
				return true;
		}
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

	@Override
	public int[] getSize() {
		return new int[]{board[0][0].length,board[0].length,board.length};
	}

	@Override
	public Piece getAt(int[] loc) {
		return this.board[loc[0]][loc[1]][loc[2]];
	}

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
						str.append(this.getAt(new int[]{x,y,z}).owner==BLACK?Character.toLowerCase(this.getAt(new int[]{x,y,z}).cCode):this.getAt(new int[]{x,y,z}).cCode);
					str.append(" ");
			}
			str.append('\n');
		}


		return str.toString();

	}
	public boolean isCheck(int player){
		int[] k=null;
		for(Piece p:(player==WHITE)?whitepiece:blackpiece)
			if(p instanceof Raum_King)
				k=p.location;
		for(Piece p:(player==WHITE)?blackpiece:whitepiece){
			for(int[] m:p.getMoves())
				if(Arrays.equals(m, k))
					return true;
		}
		return false;
	}
	public boolean isCheckmate(int player){
		if(!isStalemate(player))
			return false;
		return isCheck(player);
	}

	@Override
	public void setAt(Piece piece, int[] to) {
		board[to[0]][to[1]][to[2]]=piece;
	}

	@Override
	public boolean isStalemate(int player) {
		for(Piece p:(player==WHITE?whitepiece:blackpiece)){
			for(int[] m:p.getMoves()){
				//for reversal
				int[] t=p.location;
				Piece at=board[m[0]][m[1]][m[2]];
				//make move
				p.move(m);
				board[m[0]][m[1]][m[2]]=p;
				//test if legal
				if(!isCheck((player+1)%2)){
					//undo move
					p.move(t);
					board[t[0]][t[1]][t[2]]=p;
					board[m[0]][m[1]][m[2]]=at;
					return false;	//legal move
				}
				//undo move
				p.move(t);
				board[t[0]][t[1]][t[2]]=p;
				board[m[0]][m[1]][m[2]]=at;
			}
		}
		return true;
	}

}
