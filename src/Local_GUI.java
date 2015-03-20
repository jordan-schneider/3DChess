import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * Two dimensional graphical UI manager for {@code Game}.
 * @author Matthew
 *
 */
@SuppressWarnings("serial")
public class Local_GUI extends JFrame implements UI,ActionListener{
	Opponent[] opps;
	@SuppressWarnings("unused")
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
		Timer timer = new Timer(40, this);
		timer.setInitialDelay(40);
		this.setJMenuBar(createMenuBar());
		this.pack();
		timer.start();
		startGame();
		this.setVisible(true);
	}
	public void startGame(){
		g.start();
	}
	private JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// ImageIcon icon = new ImageIcon("Black King.png");
		menuItem = new JMenuItem("Pause Game");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source=((JMenuItem)e.getSource());
				if(source.getText().equals("Pause Game")){
					Local_GUI.this.g.pauseGame();
					source.setText("Resume Game");
				}else{
					Local_GUI.this.g.unPauseGame();
					source.setText("Pause Game");
				}

			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Offer Draw");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);
		menuItem = new JMenuItem("Resign");
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);
		menu.addSeparator();
		//a group of JMenuItems
		submenu = new JMenu("New Game");
		//menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Start a new game");

		menuItem = new JMenuItem("Restart");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Local_GUI.this.g.reset();
				Local_GUI.this.g.start();
				
			}
		});
		submenu.add(menuItem);

		menuItem = new JMenuItem("Change time control");
		submenu.add(menuItem);
		menu.add(submenu);

		menuItem = new JMenuItem("Change variant");
		submenu.add(menuItem);
		menu.add(submenu);
		submenu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("CPU White");
		cbMenuItem.setMnemonic(KeyEvent.VK_W);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, ActionEvent.ALT_MASK));
		submenu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("CPU Black");
		cbMenuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_B, ActionEvent.ALT_MASK));
		submenu.add(cbMenuItem);

		//Build second menu in the menu bar.
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menu.getAccessibleContext().setAccessibleDescription(
				"This menu provides help and details about the program");
		menuBar.add(menu);
		//a group of JMenuItems
		menuItem = new JMenuItem("Instructions",
				KeyEvent.VK_I);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_3, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"Provides game help and rules"
				);
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog((Component) e.getSource(),
					    "This is Raumschach, a three dimensional variant of chess. The boards are a little smaller, but now there are 5 of them. \n"
					    + "You can visualize the boards as being stacked on top of each other to form a 5 by 5 by 5 cube. Like normal chess, the\n"
					    + "goal of the game is to capture your opponent's king (you can find yours in the middle of the first or last board).\n"
					    + "Because the game is in three dimensions, the peices move a bit differently. You can experiment to figure them out \n"
					    + "yourself by playing the game. If you click and hold a peice, the squares the peice can move too are highlighted in \n"
					    + "yellow. If you're still puzzled, keep reading. The:\n\n"
					    + "Pawn moves one sqaure forward or up or down, and can capture in either of those directions plus one square left or right\n"
					    + "Rooks move as far as they want forward/back, left/right and up/down. They don't move diagonally.\n"
					    + "Knights move two in one direction and one in any other. This includes up and down.\n"
					    + "Kings can move one in any direction, including all diagonals.\n"
					    + "Bishops diagonally along any face of the cube. Another way to think about it is that they move along any pair of dimensions\n"
					    + "Unicorns (upside down Knights) move any amount in all three dimensions at once. They may only move along these diagonals.\n"
					    + "Queen moves like the king, except she can move any number of squares in those directions.");	
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("About");
		menuItem.getAccessibleContext().setAccessibleDescription("About the program version");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog((Component) e.getSource(),
					    "Copyright \u00a9 Matthew Das Sarma, Jordan Jack Schneider, Ben Peleh Verschell 2015\n\n"
					    + "This project was created for a software design course at the Montgomery Blair Mathematics, Science, and Computer Science Magnet program.");	
			}
		});
		menu.add(menuItem);

		return menuBar;
	}
	@Override
	public void getMove(long id) {
		System.out.println("It is "+((g.cPlayer==RaumschachBoard.WHITE)?"white's":"black's")+" turn.");
		for(Piece p:g.cPlayer==Board.WHITE?g.board.whitepiece:g.board.blackpiece)
			for(int[] i:p.getMoves())
				if(g.board.isValidMove(p, i))
					System.out.println(p.getClass().getName()+": "+RaumschachBoard.moveToString(p.location)+"->"+RaumschachBoard.moveToString(i));

	}
	@Override
	public void actionPerformed(ActionEvent a) {
		repaint();
	}

}
