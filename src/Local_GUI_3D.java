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
	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		setBackground(Color.WHITE);
		this.g=g;
		this.opps=opps;
		this.ids=ids;
		GLCanvas cube = new CubeCanvas(g.board,this);
		FPSAnimator animator = new FPSAnimator(cube, 60,true);
		
		JPanel data = new DataPanel();

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

		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setPreferredSize(new Dimension(width, height));

		pack();
		
		split.setDividerLocation(0.75);
		
		animator.start();
		setVisible(true);
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
		return false;
	}

}
