import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2GL3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;
import static com.jogamp.opengl.GL2.*;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;

import javafx.geometry.Point3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;
import com.sun.prism.impl.BufferUtil;

/**
 * Canvas on which the board is rendered. Almost all the graphics code resides here, and all of the 3D code does.
 *
 * @author Jordan
 */
@SuppressWarnings("serial")
public class CubeCanvas extends GLCanvas implements GLEventListener {
	private Board	board;
	private double	theta	= 0, phi = 0, r;	// theta is the angle in the x-z plane. phi is the angle from the x-z plane to the vector
	private GLU		glu;

	float[]			boardVertexArray, pieceVertexArray, textureCoordArray;
	FloatBuffer		boardVertexBuffer, pieceVertexBuffer, textureCoordBuffer;
	int[]			boardIndexArray;
	IntBuffer		boardIndexBuffer;

	int				boardX, boardY, boardZ;

	private double	cameraX, cameraY, cameraZ;

	Texture			texture;

	private static final int	BISHOP	= 0, KING = 600, KNIGHT = 1200, PAWN = 1800, QUEEN = 2400,
			ROOK = 3000, UNICORN = 3600;
	private Local_GUI_3D lg3d;
	/**
	 * Creates a Canvas to render a given board
	 *
	 * @param board
	 *            to render
	 */
	public CubeCanvas(Board board,Local_GUI_3D lg3d) {
		addGLEventListener(this);
		this.board = board;
		this.lg3d=lg3d;
		this.boardX = board.getSize()[0];
		this.boardY = board.getSize()[1];
		this.boardZ = board.getSize()[2];
	}
	int x_click,y_click;
	Piece clicked_on=null;
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		this.glu = new GLU(); // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

		// Defines mouse movement for rotations
		// TODO add clicking logic
		addMouseMotionListener(new MouseMotionListener() {

			int	mouseX, mouseY;

			@Override
			public void mouseDragged(MouseEvent e) {
				CubeCanvas.this.theta += (this.mouseX - e.getX()) / 500.0; // TODO move the 500 value to a constant
				CubeCanvas.this.phi += (this.mouseY - e.getY()) / 500.0;
				this.mouseX = e.getX();
				this.mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				this.mouseX = e.getX();
				this.mouseY = e.getY();
			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				CubeCanvas.this.x_click=e.getX();
				CubeCanvas.this.y_click=e.getY();
			}
		});

		this.r = 2.0 * max(max(this.boardX, this.boardY), this.boardZ); // radius of camera

		setUpVertexArray();

		setUpIndexArrays();

		try {
			texture = TextureIO.newTexture(new File("TextureAtlas.png"), false);
		} catch (GLException | IOException e1) {
			e1.printStackTrace();
		}

		texture.bind(gl);
		texture.enable(gl);
	}

	private void setUpVertexArray() {
		// Sets up vertex arrays
		int numBoardVertices = 3 * (1 + this.boardX) * (1 + this.boardY) * (1 + this.boardZ);
		this.boardVertexArray = new float[numBoardVertices];

		// filling in the vertices of the board
		for (int z = 0; z <= this.boardZ; z++) {
			for (int y = 0; y <= this.boardY; y++) {
				for (int x = 0; x <= this.boardX; x++) {
					int base = 3 * indexOfBoardWall(x, y, z); // 3 vertex values per index value
					this.boardVertexArray[base] = x;
					this.boardVertexArray[base + 1] = y;
					this.boardVertexArray[base + 2] = z;
				}
			}
		}

		// Wrapping vertexArray into the Buffer because C pointers
		this.boardVertexBuffer = BufferUtil.newFloatBuffer(this.boardVertexArray.length);
		this.boardVertexBuffer.put(this.boardVertexArray);
		this.boardVertexBuffer.rewind();

		// Don't touch pieces until we know which ones are left
	}

	private void setUpIndexArrays() {
		// Filling the board index array for the first time
		int numBoardWallIndicies = 4 * 3 * ((this.boardX * this.boardY * this.boardZ)
				+ (this.boardX * this.boardY) + (this.boardX * this.boardZ) + (this.boardY * this.boardZ));
		this.boardIndexArray = new int[numBoardWallIndicies];

		int currentPoint = 0;
		for (int dimension = 0; dimension < 3; dimension++) { // let dimension(0) = x, dimension(1) = y, ...
			for (int z = 0; z <= this.boardZ; z++) {
				for (int y = 0; y <= this.boardY; y++) {
					for (int x = 0; x <= this.boardX; x++) {
						if (dimension == 0) { // Do the sides in the y-z plane first
							if ((z != this.boardZ) && (y != this.boardY)) {
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y + 1, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y + 1,
										z + 1);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y, z + 1);
								currentPoint++;
							}
						} else if (dimension == 1) { // Then in the x-z
							if ((z != this.boardZ) && (x != this.boardX)) {
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x + 1, y, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x + 1, y,
										z + 1);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y, z + 1);
								currentPoint++;
							}
						} else { // finally x-y
							if ((x != this.boardX) && (y != this.boardY)) {
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x, y + 1, z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x + 1, y + 1,
										z);
								currentPoint++;
								this.boardIndexArray[currentPoint] = indexOfBoardWall(x + 1, y, z);
								currentPoint++;
							}
						}
					}
				}
			}
		}
	}

	private int indexOfBoardWall(int x, int y, int z) {
		return x + (y * (1 + this.boardX)) + (z * (1 + this.boardX) * (1 + this.boardY)); // consider it like a base n value converted to decimal
	}

	private Point3D pointAt(int index) {
		return new Point3D(this.boardVertexArray[index * 3],
				this.boardVertexArray[(index * 3) + 1], this.boardVertexArray[(index * 3) + 2]); // 3 vertex values per index value
	}
	Point3D point=null;
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity(); // reset the model-view matrix

		// Point the camera towards the middle and do rotations
		orientCamera();
		/*	for(Point3D point:points){
			for(int i=0;i<5;i++){
				gl.glTranslated(point.getX()*0.1,point.getY()*0.1,point.getZ()*0.1);
			gl.glColor3f(0.3f, 0.5f, 1f);
	        GLUquadric earth = glu.gluNewQuadric();
	        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
	        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
	        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
	        final float radius = 0.001f;
	        final int slices = 16;
	        final int stacks = 16;
	        glu.gluSphere(earth, radius, slices, stacks);
	        glu.gluDeleteQuadric(earth);
			}
			for(int i=0;i<5;i++)
	        gl.glTranslated(-point.getX()*0.1,-point.getY()*0.1,-point.getZ()*0.1);
		}*/

		// Setting up the vertex and texture array data for the current set of pieces on the board
		addPieceData();

		// Loads in the vertex array for pieces
		gl.glEnableClientState(GL_VERTEX_ARRAY);

		pieceVertexBuffer = BufferUtil.newFloatBuffer(pieceVertexArray.length);
		pieceVertexBuffer.put(pieceVertexArray);
		pieceVertexBuffer.rewind();
		gl.glVertexPointer(3, GL_FLOAT, 0, this.pieceVertexBuffer);

		// Load in the texture coordinate array
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

		textureCoordBuffer = BufferUtil.newFloatBuffer(textureCoordArray.length);
		textureCoordBuffer.put(textureCoordArray);
		textureCoordBuffer.rewind();
		gl.glTexCoordPointer(2, GL_FLOAT, 0, textureCoordBuffer);

		// Load in the texture
		texture.bind(gl);

		// Set the color to fully opaque
		gl.glColor4d(1.0, 1.0, 1.0, 1.0);

		// Draw the pieces
		// gl.glDrawRangeElements(GL_QUADS, 4*2, 4*3, 4, type, indices);
		gl.glDrawArrays(GL_QUADS, 0, pieceVertexArray.length / 3);

		// Disable texture arrays as soon as possible, still need vertex array for board
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

		// Rendering transparent board
		gl.glColor4d(1.0, 1.0, 1.0, 0.1);

		// Need to sort from back to front because transparency
		this.boardIndexBuffer = faceSort();

		// Load the board's vertex data
		gl.glVertexPointer(3, GL_FLOAT, 0, this.boardVertexBuffer);

		// Draws the board
		gl.glDrawElements(GL_QUADS, this.boardIndexBuffer.capacity(), GL_UNSIGNED_INT,
				this.boardIndexBuffer);

		// Clean up
		gl.glDisableClientState(GL_VERTEX_ARRAY);
		
		if(x_click>=0){
			int viewport[] = new int[4];
			double modelview[] = new double[16];
			double projection[] = new double[16];
			float winX, winY, winZ;
			float posX, posY, posZ;
			double wcoord1[] = new double[4];
			double wcoord2[] = new double[4];

			gl.glGetDoublev( GL2.GL_MODELVIEW_MATRIX, modelview, 0 );
			gl.glGetDoublev( GL2.GL_PROJECTION_MATRIX, projection, 0 );
			gl.glGetIntegerv( GL2.GL_VIEWPORT, viewport, 0 );
			winX = (float)x_click;
			winY = (float)viewport[3] - (float)y_click;
			boolean test = 	glu.gluUnProject( winX, winY, 0.0, modelview, 0, projection, 0, viewport, 0, wcoord1, 0);
			test = 	test&&glu.gluUnProject( winX, winY, 1.0, modelview, 0, projection, 0, viewport, 0, wcoord2, 0);
			//System.out.println("pre: "+x_click+","+y_click);
			//System.out.println("x: " + (wcoord2[0]-wcoord1[0]) +"y: "+(wcoord2[1]-wcoord1[1])+"z: "+(wcoord2[2]-wcoord1[2])+" worked? "+test);
			//System.out.println(this.cameraX+","+this.cameraY+","+this.cameraZ);

			x_click=-1;
			point=(new Point3D(wcoord2[0]-wcoord1[0],wcoord2[1]-wcoord1[1], wcoord2[2]-wcoord1[2]));
			int[] prevto=null;
			for(double k=0;k<1;k+=0.001){

				int[] to=toLoc(new double[]{this.cameraX+point.getX()*k,this.cameraY+point.getY()*k,this.cameraZ+point.getZ()*k});
				//System.out.println(Arrays.toString(new double[]{this.cameraX+point.getX()*k,this.cameraY+point.getY()*k,this.cameraZ+point.getZ()*k}));
				if(prevto==null);
				else if(to==null)
					continue;
				else if(Arrays.equals(prevto, to))
					continue;
				prevto=to;
				if(to!=null){
					if(clicked_on==null){
						if(this.board.getAt(to)!=null){
							System.out.println("Selected "+k+" Ray intersects "+this.board.getAt(to).cCode+" located in "+Arrays.toString(to));
							clicked_on=this.board.getAt(to);
							break;
						}else{

							//System.out.println(k+" Ray intersects nothing in "+Arrays.toString(to));

						}
					}else{
						if(this.board.getAt(to)!=null){
							if(clicked_on==this.board.getAt(to)){
								System.out.println("Unselected "+k+" Ray intersects "+this.board.getAt(to).cCode+" located in "+Arrays.toString(to));
								clicked_on=null;
							}else if(this.board.isValidMove(clicked_on, to)){
								//move 'clicked_on' to 'to'
								System.out.println("Took "+k+" Piece takes "+this.board.getAt(to).cCode+" located in "+Arrays.toString(to));
								lg3d.g.makeMove(clicked_on.location, to, lg3d.ids[lg3d.g.cPlayer]);
								clicked_on=null;
							}else
								System.out.println("Illegal hit "+k+" Ray intersects "+this.board.getAt(to).cCode+" located in "+Arrays.toString(to));
							//clicked_on=this.board.getAt(to);
							break;
						}else{
							if(this.board.isValidMove(clicked_on,to)){
								System.out.println("Move to "+k+" located "+Arrays.toString(to));
								lg3d.g.makeMove(clicked_on.location, to, lg3d.ids[lg3d.g.cPlayer]);
								//move 'clicked_on' to 'to'
								clicked_on=null;
							}else
								System.out.println("Illegal move to "+k+" located "+Arrays.toString(to));
							//System.out.println(k+" Ray intersects nothing in "+Arrays.toString(to));

						}
					}
				}
			}
		}
	}
	public static int[] toLoc(double[] coord){
		int[] r=new int[3];
		for(int i=0;i<3;i++){
			coord[0]-=0.2;
			if(Math.floor(coord[i]-0.1)>=0&&Math.floor(coord[i]-0.1)<5&&coord[i]-Math.floor(coord[i]-0.1)<0.8)
				r[i]=(int)Math.floor(coord[i]-0.1);
			else
				return null;

		}
		int t=r[2];
		r[2]=r[1];
		r[1]=t;
		return r;
	}

	private void addPieceData() {
		pieceVertexArray = new float[board.getPieces().size() * 4 * 6 * 3];
		textureCoordArray = new float[board.getPieces().size() * 4 * 6 * 2];

		int pieceRendered = 0;
		for (Piece p : this.board.getPieces()) {

			// Figure out which texture to use
			int piece;
			switch (p.cCode) {
			case 'B':
				piece = BISHOP;
				break;
			case 'K':
				piece = KING;
				break;
			case 'N':
				piece = KNIGHT;
				break;
			case 'P':
				piece = PAWN;
				break;
			case 'Q':
				piece = QUEEN;
				break;
			case 'R':
				piece = ROOK;
				break;
			case 'U':
				piece = UNICORN;
				break;
			default:
				piece = -1;
				System.out.println("FAIL");
			}

			TextureCoords current = texture.getSubImageTexCoords(piece, p.owner * 600, piece + 600,
					p.owner * 600 + 600); // king and white are the size of the increment

			int baseIndexInTextureArray = pieceRendered * 6 * 4 * 2;
			int baseIndexInVertexArray = pieceRendered * 6 * 4 * 3;


			for (int face = 0; face < 6; face++) { // for each face
				int dimension = face / 2;
				float dimMod = face % 2 * 0.6f;
				switch (dimension) {
					case 0: // x
						pieceVertexArray[baseIndexInVertexArray + 0 + face * 12] = p.location[0]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 1 + face * 12] = p.location[2] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 2 + face * 12] = p.location[1] + .2f;
						textureCoordArray[baseIndexInTextureArray + 0 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 1 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 3 + face * 12] = p.location[0]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 4 + face * 12] = p.location[2] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 5 + face * 12] = p.location[1] + .2f;
						textureCoordArray[baseIndexInTextureArray + 2 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 3 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 6 + face * 12] = p.location[0]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 7 + face * 12] = p.location[2] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 8 + face * 12] = p.location[1] + .8f;
						textureCoordArray[baseIndexInTextureArray + 4 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 5 + face * 8] = current.top();

						pieceVertexArray[baseIndexInVertexArray + 9 + face * 12] = p.location[0]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 10 + face * 12] = p.location[2] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 11 + face * 12] = p.location[1] + .8f;
						textureCoordArray[baseIndexInTextureArray + 6 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 7 + face * 8] = current.top();
						break;
					case 1: // y
						pieceVertexArray[baseIndexInVertexArray + 0 + face * 12] = p.location[0] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 1 + face * 12] = p.location[2]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 2 + face * 12] = p.location[1] + .2f;
						textureCoordArray[baseIndexInTextureArray + 0 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 1 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 3 + face * 12] = p.location[0] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 4 + face * 12] = p.location[2]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 5 + face * 12] = p.location[1] + .2f;
						textureCoordArray[baseIndexInTextureArray + 2 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 3 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 6 + face * 12] = p.location[0] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 7 + face * 12] = p.location[2]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 8 + face * 12] = p.location[1] + .8f;
						textureCoordArray[baseIndexInTextureArray + 4 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 5 + face * 8] = current.top();

						pieceVertexArray[baseIndexInVertexArray + 9 + face * 12] = p.location[0] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 10 + face * 12] = p.location[2]
								+ .2f + dimMod;
						pieceVertexArray[baseIndexInVertexArray + 11 + face * 12] = p.location[1] + .8f;
						textureCoordArray[baseIndexInTextureArray + 6 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 7 + face * 8] = current.top();
						break;

					case 2: // z
						pieceVertexArray[baseIndexInVertexArray + 0 + face * 12] = p.location[0] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 1 + face * 12] = p.location[2] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 2 + face * 12] = p.location[1]
								+ .2f + dimMod;
						textureCoordArray[baseIndexInTextureArray + 0 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 1 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 3 + face * 12] = p.location[0] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 4 + face * 12] = p.location[2] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 5 + face * 12] = p.location[1]
								+ .2f + dimMod;
						textureCoordArray[baseIndexInTextureArray + 2 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 3 + face * 8] = current
								.bottom();

						pieceVertexArray[baseIndexInVertexArray + 6 + face * 12] = p.location[0] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 7 + face * 12] = p.location[2] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 8 + face * 12] = p.location[1]
								+ .2f + dimMod;
						textureCoordArray[baseIndexInTextureArray + 4 + face * 8] = current.right();
						textureCoordArray[baseIndexInTextureArray + 5 + face * 8] = current.top();

						pieceVertexArray[baseIndexInVertexArray + 9 + face * 12] = p.location[0] + .2f;
						pieceVertexArray[baseIndexInVertexArray + 10 + face * 12] = p.location[2] + .8f;
						pieceVertexArray[baseIndexInVertexArray + 11 + face * 12] = p.location[1]
								+ .2f + dimMod;
						textureCoordArray[baseIndexInTextureArray + 6 + face * 8] = current.left();
						textureCoordArray[baseIndexInTextureArray + 7 + face * 8] = current.top();
						break;
				}
			}
			pieceRendered++;
		}
	}
	private void orientCamera() {
		double x_0 = (this.boardX / 2.0);
		double y_0 = (this.boardY / 2.0);
		double z_0 = (this.boardZ / 2.0);

		this.cameraX = (this.r * cos(this.phi) * sin(this.theta)) + x_0;
		this.cameraY = (this.r * sin(this.phi)) + y_0;
		this.cameraZ = (this.r * cos(this.phi) * cos(this.theta)) + z_0;

		double upX = sin(this.phi) * sin(this.theta);
		double upY = cos(this.phi);
		double upZ = sin(this.phi) * cos(this.theta);
		this.glu.gluLookAt(this.cameraX, this.cameraY, this.cameraZ, x_0, y_0, z_0, upX, upY, upZ); // Polar conversion + phi is from x-z plane instead of y axis

	}

	private IntBuffer faceSort() {
		if ((this.boardIndexArray == null) || (this.boardIndexArray.length == 0)) {
			return null;
		}

		quicksort(0, this.boardIndexArray.length - 1 - 3); // want the end index to be at the start of the face
		return (IntBuffer) BufferUtil.newIntBuffer(this.boardIndexArray.length)
				.put(this.boardIndexArray).rewind();
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;

		double pivot = eval(((low + ((high - low) / 2)) / 4) * 4); // the /4*4 thing is a integer division trick. It forces the pivot to be a multiple of 4 i.e. the start of a face. It naturally chooses the pivot and then rounds down.

		while (i <= j) {
			while (eval(i) > pivot) {
				i += 4;
			}

			while (eval(j) < pivot) {
				j -= 4;
			}

			if (i <= j) {
				exchange(i, j);
				i += 4;
				j -= 4;
			}
		}

		if (low < j) {
			quicksort(low, j);
		}
		if (i < high) {
			quicksort(i, high);
		}
	}

	private double eval(int index) {
		Point3D p_1 = pointAt(this.boardIndexArray[index]), p_2 = pointAt(this.boardIndexArray[index + 1]), p_3 = pointAt(this.boardIndexArray[index + 2]), p_4 = pointAt(this.boardIndexArray[index + 3]);
		Point3D pMid = getMidpointOfSquare(p_1, p_2, p_3, p_4);
		return pMid.distance(this.cameraX, this.cameraY, this.cameraZ);
	}

	private void exchange(int i, int j) {
		int temp_1 = this.boardIndexArray[i], temp_2 = this.boardIndexArray[i + 1], temp_3 = this.boardIndexArray[i + 2], temp_4 = this.boardIndexArray[i + 3];
		this.boardIndexArray[i] = this.boardIndexArray[j];
		this.boardIndexArray[i + 1] = this.boardIndexArray[j + 1];
		this.boardIndexArray[i + 2] = this.boardIndexArray[j + 2];
		this.boardIndexArray[i + 3] = this.boardIndexArray[j + 3];

		this.boardIndexArray[j] = temp_1;
		this.boardIndexArray[j + 1] = temp_2;
		this.boardIndexArray[j + 2] = temp_3;
		this.boardIndexArray[j + 3] = temp_4;
	}

	private Point3D getMidpointOfSquare(Point3D p_1, Point3D p_2, Point3D p_3, Point3D p_4) {
		double maxX = max(max(p_1.getX(), p_2.getX()), max(p_3.getX(), p_4.getX())); // Can we have functions that accept n inputs already
		double minX = min(min(p_1.getX(), p_2.getX()), min(p_3.getX(), p_4.getX()));
		double midX = (maxX + minX) / 2.0;

		double maxY = max(max(p_1.getY(), p_2.getY()), max(p_3.getY(), p_4.getY()));
		double minY = min(min(p_1.getY(), p_2.getY()), min(p_3.getY(), p_4.getY()));
		double midY = (maxY + minY) / 2.0;

		double maxZ = max(max(p_1.getZ(), p_2.getZ()), max(p_3.getZ(), p_4.getZ()));
		double minZ = min(min(p_1.getZ(), p_2.getZ()), min(p_3.getZ(), p_4.getZ()));
		double midZ = (maxZ + minZ) / 2.0;

		return new Point3D(midX, midY, midZ);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		texture.disable(gl);
		texture.destroy(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		if (height == 0) {
			height = 1; // prevent divide by zero
		}
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		this.glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

		// Allows transparency
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}


}