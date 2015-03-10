import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;


public class TwoDPiece {
	public Image img;
	public int x,y;
	public final Piece p;
	public TwoDPiece(Piece p){
		Rectangle rectz=new Rectangle(40, 40);
		BufferedImage buf=new BufferedImage(rectz.width,rectz.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D bufg2d=buf.createGraphics();
		
		bufg2d.setColor(p.owner==Board.BLACK?Color.BLACK:Color.WHITE);
		bufg2d.fill(rectz);
		bufg2d.setColor(Color.GREEN);
		FontMetrics fm = bufg2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(""+p.cCode, bufg2d);
        int x = (buf.getWidth() - (int) r.getWidth()) / 2;
        int y = (buf.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        bufg2d.drawString(""+p.getSymbol(), x, y);
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
