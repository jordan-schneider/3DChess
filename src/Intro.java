import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Intro extends JFrame{

	private static final long	serialVersionUID	= 1L;
	Image back;
	int width, height;

	public Intro(UI gui) {
		back = null;
		try{
			back = ImageIO.read(new File("back.jpg"));
		}
		catch(IOException e){System.err.println("HALP");}
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

		class CustomPane extends JPanel{
			private static final long	serialVersionUID	= 1L;
			
			public CustomPane(JFrame frame) {
				BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
				setLayout(box);
				
				JLabel title = new JLabel("Raumschach:");
				title.setFont(new Font("Courier New", Font.PLAIN, 50));
				title.setAlignmentX(Component.CENTER_ALIGNMENT);
				title.setForeground(Color.WHITE);
				add(title);

				JLabel subtitle = new JLabel("A Game of Stalemates");
				subtitle.setFont(new Font("Courier New", Font.PLAIN, 25));
				subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
				subtitle.setForeground(Color.WHITE);

				add(subtitle);

				add(Box.createRigidArea(new Dimension(10, 50)));

				JLabel text = new JLabel("SO I HEARD YOU LIKE CHESS. YOU ARE WRONG. CHESS IS EVIL. IT'S TOO EASY. PLAY 3D CHESS INSTEAD.");
				text.setForeground(Color.WHITE);
				text.setAlignmentX(Component.CENTER_ALIGNMENT);
				text.setFont(new Font("Courier New", Font.PLAIN, 20));
				add(text);

				JButton start = new JButton("Play Against Computer");
				start.setAlignmentX(Component.CENTER_ALIGNMENT);
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Opponent a=new LocalOpponent(0);
						Opponent b=new Basic_AI(1,1);
						Game g=new Game(a,b,gui,new TimeControl(5,0,0));
						g.init();
						
						gui.init(new Opponent[]{a,b},new long[]{a.id,b.id},g);	
						frame.dispose();
					}
				});
				add(start);
				add(Box.createVerticalStrut(10));
				start = new JButton("Play Against Human");
				start.setAlignmentX(Component.CENTER_ALIGNMENT);
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Opponent a=new LocalOpponent(0);
						Opponent b=new LocalOpponent(1);
						Game g=new Game(a,b,gui,new TimeControl(5,0,0));
						g.init();
						gui.init(new Opponent[]{a,b},new long[]{a.id,b.id},g);	
						frame.dispose();
					}
				});
				add(start);
				
				add(Box.createVerticalStrut(10));
				start = new JButton("Computer Exhibition");
				start.setAlignmentX(Component.CENTER_ALIGNMENT);
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Opponent a=new Basic_AI(0,0);
						Opponent b=new Basic_AI(1,1);
						Game g=new Game(a,b,gui,new TimeControl(5,0,0));
						g.init();
						gui.init(new Opponent[]{a,b},new long[]{a.id,b.id},g);	
						frame.dispose();
					}
				});
				add(start);
				width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
				height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponents(g);	
				g.drawImage(back, 0, 0,width,height, null);
			}

		}
		
		add(new CustomPane(this));
		setPreferredSize(new Dimension(width, height));

		pack();

		setVisible(true);
	}
}
