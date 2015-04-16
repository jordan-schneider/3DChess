import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

@SuppressWarnings("serial")
public class Toy_GUI_3D extends JFrame implements UI {

	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		GLCanvas toyCanvas = new ToyCanvas();
		FPSAnimator animator = new FPSAnimator(toyCanvas,60,true);
		
		add(toyCanvas);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if (toyCanvas.getAnimator().isStarted()) toyCanvas.getAnimator().stop();
						g.stop();
						System.exit(0);
					}
				}.start();
			}
		});
		
		//pack();
		setPreferredSize(new Dimension(1000,500));
		pack();
		animator.start();
		setVisible(true);
	}

	@Override
	public void getMove(long id) {
		//LOLOLOLOLOLOL
	}

	@Override
	public void reloadBoard() {
		
	}

	@Override
	public boolean informMove(int[] from, int[] to, long id) {
		return false;
	}

}
