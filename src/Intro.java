import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Intro extends JFrame{

	private static final long	serialVersionUID	= 1L;

	public Intro() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@Override
					public void run() {
						System.exit(0);
					}
				}.start();
			}
		});		
		
		JPanel pane = new JPanel();
		this.add(pane);
		
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		
		JLabel title = new JLabel("Raumschach:");
		title.setFont(new Font("Courier New", Font.PLAIN, 50));
		pane.add(title);
		
		JLabel subtitle = new JLabel("A Game of Stalemates");
		subtitle.setFont(new Font("Courier New", Font.PLAIN, 25));
		pane.add(subtitle);
		
		JLabel text = new JLabel("\n\n\nSO I HEARD YOU LIKE CHESS. YOU ARE WRONG. CHESS IS EVIL. IT'S TOO EASY.");
		text.setFont(new Font("Courier New", Font.PLAIN, 20));
		pane.add(text);

		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setPreferredSize(new Dimension(width, height));

		pack();
		
		setVisible(true);
	}
	
	
}
