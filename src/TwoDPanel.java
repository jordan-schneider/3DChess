import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class TwoDPanel extends JPanel {
	LinkedList<TwoDPiece> gpieces=new LinkedList<TwoDPiece>();
	int pX, pY;
	BufferedImage black = null;
	BufferedImage white = null;
	int[] from;
	int[] fromcoord;
	int[] atCheckSquare=null;
	TwoDPiece whichPiece=null;
	List<int[]> highlight=new LinkedList<int[]>();
	boolean isFirstTime = true;
	Rectangle area;
	boolean pressz = false;
	Local_GUI gui;
	public TwoDPanel(Local_GUI gui){
		this.gui=gui;
		try {
			black = ImageIO.read(new File("Blackboard.gif"));
		} catch (IOException e) {
		}
		try {
			white = ImageIO.read(new File("WhiteBoard.gif"));
		} catch (IOException e) {
		}
		setBackground(Color.LIGHT_GRAY);
		addMouseMotionListener(new DragAdapter());
		addMouseListener(new DragAdapter());
		for(Piece p:gui.g.board.getPieces())
			gpieces.add(new TwoDPiece(p));
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1120,280);
	}
	public static int[] getLocationCorner(int[] loc){
		return new int[]{20+220*loc[2]+40*loc[0], 200-40*loc[1]};
	}
	public static int[] pointToLocation(int x,int y){
		int l0=((x%220)-20)/40;
		return new int[]{l0,5-y/40,(x-20-40*l0)/220};
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for(int z=0;z<5;z++)
			for(int x=0;x<5;x++)
				for(int y=0;y<5;y++){
					if((x+y+z)%2==0)
						g2d.drawImage(black,20*(z+1)+40*(5*z+x), 40*(4-y)+40,40,40,null);
					else
						g2d.drawImage(white,20*(z+1)+40*(5*z+x), 40*(4-y)+40,40,40,null);
				}
		//(1080)+20 , 240



		if (isFirstTime) {
			area = new Rectangle(getPreferredSize());
			isFirstTime = false;
		}
		ListIterator<TwoDPiece> li=gpieces.listIterator(gpieces.size());
		while(li.hasPrevious()){
			TwoDPiece rect=li.previous();
			g2d.drawImage(rect.img, rect.x, rect.y, null);
		}
		g.setColor(Color.RED);
		if(atCheckSquare!=null)
			g2d.drawRect(getLocationCorner(atCheckSquare)[0], getLocationCorner(atCheckSquare)[1], 40,40);
		g.setColor(Color.YELLOW);
		for(int[] i:highlight)
			g2d.drawRect(getLocationCorner(i)[0], getLocationCorner(i)[1], 40, 40);
	}
	boolean checkRect() {
		if (area==null||area.contains(whichPiece.x, whichPiece.y, whichPiece.getWidth(), whichPiece.getHeight()))
			return true;
		int new_x = whichPiece.x;
		int new_y = whichPiece.y;
		if ((whichPiece.x + whichPiece.getWidth()) > area.getWidth()) 
			new_x = (int) area.getWidth() - (int) (whichPiece.getWidth() - 1);
		if (whichPiece.x < 0) 
			new_x = -1;
		if ((whichPiece.y + whichPiece.getHeight()) > area.getHeight()) 
			new_y = (int) area.getHeight() - (int) (whichPiece.getHeight() - 1);
		if (whichPiece.y < 0) 
			new_y = -1;
		whichPiece.setLocation(new_x, new_y);
		return false;
	}
	private class DragAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			boolean bad=true;
			ListIterator<TwoDPiece> li=gpieces.listIterator();
			while(li.hasNext()){
				TwoDPiece rect=li.next();
				pX = rect.x - e.getX();
				pY = rect.y - e.getY();
				if (rect.contains(e.getX(), e.getY())) {
					from=rect.p.location;
					fromcoord=new int[]{rect.x,rect.y};
					whichPiece=rect;
					highlight.clear();
					for(int[] i:rect.p.getMoves())
						if(gui.g.board.isValidMove(rect.p, i))
							highlight.add(i);
					li.remove();
					TwoDPanel.this.gpieces.addFirst(rect);
					updateLocation(e);
					bad=false;
					break;
				}
			}
			if(bad)
				pressz = true;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!pressz) {
				updateLocation(e);
			} else {
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (whichPiece!=null&&whichPiece.contains(e.getX(), e.getY())) {
				updateLocation(e);
				//fit to square closest to cursor
				int[] to=pointToLocation(e.getX(),e.getY());
				Piece tempp = gui.g.board.getAt(to);
				if(whichPiece.p.owner==gui.g.cPlayer&&gui.opps[gui.g.cPlayer].isHuman()){
					if(gui.opps[gui.g.cPlayer].makeMove(from, to)){
						whichPiece.setLocation(getLocationCorner(to)[0], getLocationCorner(to)[1]);
						if(tempp!=null){
							ListIterator<TwoDPiece> li=gpieces.listIterator();
							while(li.hasNext())
								if(li.next().p==tempp){
									li.remove();
									repaint();
									break;
								}
						}
						if(gui.g.board.isCheck(gui.g.cPlayer)){
							System.out.println("!"+gui.g.board.isStalemate(gui.g.cPlayer,true));
							for(Piece p:Board.BLACK==gui.g.cPlayer?gui.g.board.blackpiece:gui.g.board.whitepiece)
								if(p instanceof King)
									atCheckSquare=p.location;
						}else
							atCheckSquare=null;
					}else
						whichPiece.setLocation(fromcoord[0], fromcoord[1]);
				}else
					whichPiece.setLocation(fromcoord[0], fromcoord[1]);
			} else 
				pressz = false;
			whichPiece=null;
			highlight.clear();
		}

		public void updateLocation(MouseEvent e) {
			whichPiece.setLocation(pX + e.getX(), pY + e.getY());
			checkRect();

			repaint();
		}
	}
}


