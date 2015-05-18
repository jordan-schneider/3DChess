import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

@SuppressWarnings("serial")
public class Local_GUI_3D extends JFrame implements UI{
	public Game g;
	public Opponent[] opps;
	public long[] ids;
	DataPanel data;
	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		this.g=g;
		this.opps=opps;
		this.ids=ids;
		GLCanvas cube = new CubeCanvas(g.board,this);
		FPSAnimator animator = new FPSAnimator(cube, 60,true);
		
		data = new DataPanel(this,(CubeCanvas)cube);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cube, data); //TODO find way to make default split 75/25
		split.setDividerSize(0);
		
		getContentPane().add(split);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if (cube.getAnimator().isStarted()) cube.getAnimator().stop();
						g.stop();
						System.exit(0);
					}
				}.start();
			}
		});

		pack();
		
		split.setDividerLocation(0.75);
		
		animator.start();
		setVisible(true);
		opps[g.cPlayer].requestMove();
	}

	@Override
	public void getMove(long id) {
		// BULLSHIT LEGACY CODE
	}

	@Override
	public void reloadBoard() {
		
	}

	@Override
	public boolean informMove(int[] from, int[] to, long id) {
		boolean b=g.makeMove(from, to, id);
		System.out.println("It become ridiculous!");
		if(!b)
			return false;
		data.mp.move(from, to);
		opps[g.cPlayer].requestMove();
		data.repaint();
		return true;
	}

}
