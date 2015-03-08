import javax.swing.JFrame;


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
		// TODO Auto-generated method stub
		
	}

}
