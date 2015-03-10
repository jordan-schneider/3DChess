import javax.swing.JFrame;

/**
 * Two dimensional graphical UI manager for {@code Game}.
 * @author Matthew
 *
 */
public class Local_GUI extends JFrame implements UI{
	Opponent[] opps;
	private long[] ids;
	public Game g;
	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		this.opps=opps;
		this.g=g;
		this.ids=ids;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("3D Chess");
		this.getContentPane().add(new TwoDPanel(this));
		this.pack();
		this.setVisible(true);
	}
	@Override
	public void getMove(long id) {
		System.out.println("It is "+((g.cPlayer==RaumschachBoard.WHITE)?"white's":"black's")+" turn.");
		for(Piece p:g.cPlayer==Board.WHITE?g.board.whitepiece:g.board.blackpiece)
			for(int[] i:p.getMoves())
				if(g.board.isValidMove(p, i))
					System.out.println(p.getClass().getName()+": "+RaumschachBoard.moveToString(p.location)+"->"+RaumschachBoard.moveToString(i));

	}

}
