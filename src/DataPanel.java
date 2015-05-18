import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class DataPanel extends JPanel {

	Local_GUI_3D lg3d;
	CubeCanvas cc;
	PlayerPanel white,black;
	MovesPanel mp;
	ThreePanel tp;
	ButtonPanel bp;
	public DataPanel(Local_GUI_3D lg3d,CubeCanvas cc) {
		//add(new JButton("lose"));
		this.lg3d=lg3d;
		this.cc=cc;
		int width=lg3d.getPreferredSize().width;
		int height=lg3d.getPreferredSize().height;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		white=new PlayerPanel(this,true);
		white.setPreferredSize(new Dimension(width,50));
		white.setMaximumSize(new Dimension(width,50));
		black=new PlayerPanel(this,false);
		black.setPreferredSize(new Dimension(width,50));
		black.setMaximumSize(new Dimension(width,50));
		mp=new MovesPanel(this);
		mp.setPreferredSize(new Dimension(width,lg3d.getPreferredSize().height-50-50-50-500));
		System.out.println(lg3d.getPreferredSize().height-650);
		mp.setMaximumSize(new Dimension(width,lg3d.getPreferredSize().height-50-50-50-500));
		tp=new ThreePanel(this);
		tp.setMinimumSize(new Dimension(width,200));
		bp=new ButtonPanel(this);
		bp.setPreferredSize(new Dimension(width,50));
		this.add(white);
		this.add(mp);
		this.add(tp);
		this.add(bp);
		this.add(black);
		this.validate();
	}
}
class PlayerPanel extends JPanel{
	DataPanel dp;
	boolean isWhite;
	JLabel time;
	JLabel name;
	JLabel isTurn;
	public PlayerPanel(DataPanel dp,boolean isWhite){
		this.dp=dp;
		this.isWhite=isWhite;
	}
	public void paintComponent(Graphics g){
		if(isWhite)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLACK);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		if(isWhite)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.WHITE);
		if(this.dp.cc.clicked_on!=null)
			g.drawString(new String(new byte[]{(byte)this.dp.cc.clicked_on.getSymbol()}), 10, 10);

	}

}
class ButtonPanel extends JPanel{
	DataPanel dp;
	public ButtonPanel(DataPanel dp){
		this.dp=dp;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(new JButton("yo1"));
		this.add(new JButton("yo2"));
	}
}
class MovesPanel extends JPanel{

	DataPanel dp;
	private	JTable		table;
	private	JScrollPane scrollPane;
	int cri=0;
	int cci=1;
	public void move(int[] from,int[] to){
		table.getModel().setValueAt(RaumschachBoard.moveToString(from)+""+RaumschachBoard.moveToString(to), cri, cci);
		if(++cci==3){
			cci=1;
			cri++;
		}
	}
	public MovesPanel(DataPanel dp){
		this.dp=dp;
		this.setLayout( new BorderLayout() );



		// Create a new table instance
		table = new JTable(new AbstractTableModel() {
			ArrayList<String[]> list=new ArrayList<String[]>();

			private	String		columnNames[]=new String[]{"###","White","Black"};
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if(list.isEmpty())
					if(columnIndex==0)
						return (rowIndex+1)+".";
					else
						return "";
				else
					return list.get(rowIndex)[columnIndex];
			}

			@Override
			public int getRowCount() {
				if(list.isEmpty())
					return 1;
				return list.size();
			}

			@Override
			public int getColumnCount() {
				return 3;
			}
			@Override
			public boolean isCellEditable(int r,int c){
				return false;
			}
			@Override
			public String getColumnName(int i){
				return columnNames[i];
			}
			@Override
			public void setValueAt(Object value,int row,int col){
				System.out.println((String)value+" @"+row+","+col);
				if(row<list.size()){
					list.get(row)[2]=(String)value;
				}else{
					list.add(new String[]{(row+1)+".",(String)value,""});
				}
				fireTableDataChanged();
				//correct calls...improper rendering
			}
		});

		// Configure some of JTable's paramters
		table.setShowHorizontalLines( true );
		//table.setRowSelectionAllowed( true );
		//table.setColumnSelectionAllowed( true );

		// Change the selection colour
		table.setSelectionForeground( Color.white );
		table.setSelectionBackground( Color.red );

		// Add the table to a scrolling pane
		scrollPane = table.createScrollPaneForTable( table );
		this.add( scrollPane, BorderLayout.CENTER );
	}
}
class ThreePanel extends JPanel{
	protected static BufferedImage whitePawn,blackPawn,whiteKnight,blackKnight,whiteRook,blackRook;
	protected static BufferedImage whiteBishop,blackBishop,whiteQueen,blackQueen,whiteUnicorn,blackUnicorn;
	protected static BufferedImage whiteKing,blackKing;

	DataPanel dp;
	Image black,white;
	public ThreePanel(DataPanel dp){
		this.dp=dp;
		try {
			black = ImageIO.read(new File("Blackboard.gif"));
		} catch (IOException e) {}
		try {
			white = ImageIO.read(new File("WhiteBoard.gif"));
		} catch (IOException e) {}
	}
	//0 for xy, 1 for xz, 2 for yz
	public void drawBoard(Graphics2D g,int x_shift,int y_shift,int which_plane,int[] location){
		switch(which_plane){
		case 0:
			int z=location[2];
			for(int x=0;x<5;x++)
				for(int y=0;y<5;y++){
					if((x+y+z)%2==0)
						g.drawImage(black,30*(x)+x_shift, 30*(4-y)+y_shift,30,30,null);
					else
						g.drawImage(white,30*(x)+x_shift, 30*(4-y)+y_shift,30,30,null);
					Piece p=dp.lg3d.g.board.getAt(new int[]{x,y,z});

					
					if(p!=null){
						System.out.println(p.getSymbol());
						Rectangle rectz=new Rectangle(40, 40);
						BufferedImage piece = null;
						BufferedImage buf=new BufferedImage(rectz.width,rectz.height,BufferedImage.TYPE_INT_ARGB);
						Graphics2D bufg2d=buf.createGraphics();
						if(p.owner==Board.WHITE){
							if(p instanceof King)
								piece=whiteKing;
							else if(p instanceof Queen)
								piece=whiteQueen;
							else if(p instanceof Rook)
								piece=whiteRook;
							else if(p instanceof Knight)
								piece=whiteKnight;
							else if(p instanceof Bishop)
								piece=whiteBishop;
							else if(p instanceof Unicorn)
								piece=whiteUnicorn;
							else
								piece=whitePawn;
						}else{
							if(p instanceof King)
								piece=blackKing;
							else if(p instanceof Queen)
								piece=blackQueen;
							else if(p instanceof Rook)
								piece=blackRook;
							else if(p instanceof Knight)
								piece=blackKnight;
							else if(p instanceof Bishop)
								piece=blackBishop;
							else if(p instanceof Unicorn)
								piece=blackUnicorn;
							else
								piece=blackPawn;
						}
						bufg2d.drawImage(piece,0,0,40,40,null);
						bufg2d.dispose();
						g.drawImage(buf,30*(x)+x_shift, 30*(4-y)+y_shift,30,30,null);
					}
				}
			break;
		case 1:
			int y=location[1];
			for(int x=0;x<5;x++)
				for(z=0;z<5;z++){
					if((x+y+z)%2==0)
						g.drawImage(black,30*(x)+x_shift, 30*(4-z)+y_shift,30,30,null);
					else
						g.drawImage(white,30*(x)+x_shift, 30*(4-z)+y_shift,30,30,null);
					Piece p=dp.lg3d.g.board.getAt(new int[]{x,y,z});


					if(p!=null){
						Rectangle rectz=new Rectangle(40, 40);
						BufferedImage piece = null;
						BufferedImage buf=new BufferedImage(rectz.width,rectz.height,BufferedImage.TYPE_INT_ARGB);
						Graphics2D bufg2d=buf.createGraphics();
						bufg2d.drawImage(piece,0,0,40,40,null);
						if(p.owner==Board.WHITE){
							if(p instanceof King)
								piece=whiteKing;
							else if(p instanceof Queen)
								piece=whiteQueen;
							else if(p instanceof Rook)
								piece=whiteRook;
							else if(p instanceof Knight)
								piece=whiteKnight;
							else if(p instanceof Bishop)
								piece=whiteBishop;
							else if(p instanceof Unicorn)
								piece=whiteUnicorn;
							else
								piece=whitePawn;
						}else{
							if(p instanceof King)
								piece=blackKing;
							else if(p instanceof Queen)
								piece=blackQueen;
							else if(p instanceof Rook)
								piece=blackRook;
							else if(p instanceof Knight)
								piece=blackKnight;
							else if(p instanceof Bishop)
								piece=blackBishop;
							else if(p instanceof Unicorn)
								piece=blackUnicorn;
							else
								piece=blackPawn;
						}
						bufg2d.dispose();
						g.drawImage(buf,30*(x)+x_shift, 30*(4-z)+y_shift,30,30,null);
					}
				}
			break;
		case 2:
			int x=location[0];
			for(z=0;z<5;z++)
				for(y=0;y<5;y++){
					if((x+y+z)%2==0)
						g.drawImage(black,30*(z)+x_shift, 30*(4-y)+y_shift,30,30,null);
					else
						g.drawImage(white,30*(z)+x_shift, 30*(4-y)+y_shift,30,30,null);
					Piece p=dp.lg3d.g.board.getAt(new int[]{x,y,z});


					if(p!=null){
						Rectangle rectz=new Rectangle(40, 40);
						BufferedImage piece = null;
						BufferedImage buf=new BufferedImage(rectz.width,rectz.height,BufferedImage.TYPE_INT_ARGB);
						Graphics2D bufg2d=buf.createGraphics();
						bufg2d.drawImage(piece,0,0,40,40,null);
						if(p.owner==Board.WHITE){
							if(p instanceof King)
								piece=whiteKing;
							else if(p instanceof Queen)
								piece=whiteQueen;
							else if(p instanceof Rook)
								piece=whiteRook;
							else if(p instanceof Knight)
								piece=whiteKnight;
							else if(p instanceof Bishop)
								piece=whiteBishop;
							else if(p instanceof Unicorn)
								piece=whiteUnicorn;
							else
								piece=whitePawn;
						}else{
							if(p instanceof King)
								piece=blackKing;
							else if(p instanceof Queen)
								piece=blackQueen;
							else if(p instanceof Rook)
								piece=blackRook;
							else if(p instanceof Knight)
								piece=blackKnight;
							else if(p instanceof Bishop)
								piece=blackBishop;
							else if(p instanceof Unicorn)
								piece=blackUnicorn;
							else
								piece=blackPawn;
						}
						bufg2d.dispose();
						g.drawImage(buf,30*(z)+x_shift, 30*(4-y)+y_shift,30,30,null);
					}
				}
			break;
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int height=this.getHeight();
		int width=this.getWidth();
		Graphics2D g2d = (Graphics2D) g;
		if(this.dp.cc.clicked_on!=null){
			this.drawBoard(g2d, 30, 30, 0, this.dp.cc.clicked_on.location);
			this.drawBoard(g2d, 30, 30, 1, this.dp.cc.clicked_on.location);
			this.drawBoard(g2d, 105, 210, 2, this.dp.cc.clicked_on.location);
		}
	}
}