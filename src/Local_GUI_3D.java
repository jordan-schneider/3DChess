import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

@SuppressWarnings("serial")
public class Local_GUI_3D extends JFrame implements UI{
	public Game g;
	public Opponent[] opps;
	public long[] ids;
	DataPanel data;
	CubeCanvas cc;
	@Override
	public void init(Opponent[] opps, long[] ids, Game g) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		this.g=g;
		this.opps=opps;
		this.ids=ids;
		cc=new CubeCanvas(g.board,this);
		GLCanvas cube = cc;
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
		this.setJMenuBar(createMenuBar());
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
		if(!b)
			return false;
		data.mp.move(from, to,this.g.board.getAt(to).getSymbol());
		opps[g.cPlayer].requestMove();
		data.repaint();
		if(this.g.board.isCheck(this.g.cPlayer)){
			System.out.println("In check");
			for(Piece p:this.g.cPlayer==Board.BLACK?this.g.board.blackpiece:this.g.board.whitepiece)
				if(p instanceof King){
					System.out.println("Found King");
					this.cc.checked=p;
					break;
				}
		}else
			cc.checked=null;
		return true;
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
		menu.getPopupMenu().setLightWeightPopupEnabled(false);
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
					Local_GUI_3D.this.g.pauseGame();
					source.setText("Resume Game");
				}else{
					Local_GUI_3D.this.g.unPauseGame();
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
		submenu.getPopupMenu().setLightWeightPopupEnabled(false);
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
				Local_GUI_3D.this.g.reset();
				Local_GUI_3D.this.g.start();

			}
		});
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
		menu.getPopupMenu().setLightWeightPopupEnabled(false);
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
						"Copyright \u00a9 Matthew D. Das Sarma, Jordan J. Schneider, Ben P. Verschell 2015\n\n"
								+ "This project was created for a software design course at the Montgomery Blair Mathematics, Science, and Computer Science Magnet program.");	
			}
		});
		menu.add(menuItem);
		return menuBar;
	}

}
