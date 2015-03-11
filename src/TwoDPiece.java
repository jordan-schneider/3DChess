import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TwoDPiece {
	public Image img;
	public int x,y;
	public final Piece p;
	public TwoDPiece(Piece p){
		Rectangle rectz=new Rectangle(40, 40);
		BufferedImage piece = null;
		BufferedImage buf=new BufferedImage(rectz.width,rectz.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D bufg2d=buf.createGraphics();
		if(p instanceof Bishop){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Bishop.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Bishop.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof King){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black King.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White King.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof Knight){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Knight.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Knight.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof Pawn){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Pawn.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Pawn.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof Queen){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Queen.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Queen.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof Rook){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Rook.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Rook.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		else if(p instanceof Unicorn){
			if(p.owner==Board.BLACK){
				try {
					piece = ImageIO.read(new File("Black Unicorn.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
			else if(p.owner==Board.WHITE){
				try {
					piece = ImageIO.read(new File("White Unicorn.png"));
				} catch (IOException e) {
				}
				bufg2d.drawImage(piece,0,0,40,40,null);
			}
		}
		//bufg2d.setColor(p.owner==Board.BLACK?Color.BLACK:Color.WHITE);
		//bufg2d.fill(rectz);
		//bufg2d.setColor(Color.GREEN);
		//FontMetrics fm = bufg2d.getFontMetrics();
        //Rectangle2D r = fm.getStringBounds(""+p.cCode, bufg2d);
        //int x = (buf.getWidth() - (int) r.getWidth()) / 2;
        //int y = (buf.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        //bufg2d.drawString(""+p.getSymbol(), x, y);
		bufg2d.dispose();
		this.x=TwoDPanel.getLocationCorner(p.location)[0];
		this.y=TwoDPanel.getLocationCorner(p.location)[1];
		this.img=buf;
		this.p=p;
	}
	public boolean contains(int X, int Y) {
		int w = this.getWidth();
		int h = this.getHeight();
		if ((w | h) < 0) {	//either less than zero
			return false;
		}
		int x = this.x;
		int y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		h += y;
		return ((w < x || w > X) &&
				(h < y || h > Y));
	}
	public int getWidth() {
		return img.getWidth(null);
	}
	public int getHeight() {
		return img.getHeight(null);
	}
	public void setLocation(int new_x, int new_y) {
		this.x=new_x;
		this.y=new_y;
	}
}
