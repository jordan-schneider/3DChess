import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class Local_GUI_3D extends JFrame implements UI{

	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds(0, 0, width, height);
		setBackground(Color.WHITE);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel cube = new CubePanel();
		JPanel data = new DataPanel();

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cube, data);

		getContentPane().add(split);
	}

	@Override
	public void getMove(long id) {
		// TODO Auto-generated method stub
		// BULLSHIT LEGACY CODE
	}

}
