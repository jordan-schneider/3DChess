import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2GL3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

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
	private GLUT	glut;

	float[]			vertexArray;
	FloatBuffer		vertexBuffer;
	int[]			indexArray;
	IntBuffer		indexBuffer;

	int				boardX, boardY, boardZ;

	/**
	 * Creates a Canvas to render a given board
	 * 
	 * @param board
	 *            to render
	 */
	public CubeCanvas(Board board) {
		addGLEventListener(this);
		this.board = board;
		this.boardX = board.getSize()[0];
		this.boardY = board.getSize()[1];
		this.boardZ = board.getSize()[2];
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity(); // reset the model-view matrix

		gl.glColor4d(1.0, 1.0, 1.0, 0.2); // use a transparent color

		// glutSolidCube centers the cube at the origin so I have to shift back 1/2 the length of the cube
		double x_0 = boardX / 2.0 - .5;
		double y_0 = boardY / 2.0 - .5;
		double z_0 = boardZ / 2.0 - .5;

		double cameraX = this.r * cos(this.phi) * sin(this.theta) + x_0;
		double cameraY = this.r * sin(this.phi) + y_0;
		double cameraZ = this.r * cos(this.phi) * cos(this.theta) + z_0;

		double upX = sin(this.phi) * sin(this.theta);
		double upY = abs(cos(this.phi));
		double upZ = sin(this.phi) * cos(this.theta);
		this.glu.gluLookAt(cameraX, cameraY, cameraZ, x_0, y_0, z_0, upX, upY,
				upZ); // Polar conversion + phi is from x-z plane instead of y axis


		// TODO sort indexArray before making into buffer

		IntBuffer indexBuffer = IntBuffer.wrap(this.indexArray);

		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

		gl.glDrawElements(GL_QUADS, 1, GL_FLOAT, indexBuffer);

		// This will be depricated as soon as I make Vertex Arrays work
		for (int i = 0; i < boardZ; i++) {
			for (int j = 0; j < boardY; j++) {
				for (int k = 0; k < boardX; k++) {
					this.glut.glutSolidCube(1f);
					gl.glTranslatef(1f, 0f, 0f);
				}
				gl.glTranslatef(-1 * boardX, 1f, 0f);
			}
			gl.glTranslatef(0f, -1 * boardY, 1f);
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		this.glu = new GLU(); // get GL Utilities
		this.glut = new GLUT();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

		addMouseMotionListener(new MouseMotionListener() {

			int	mouseX, mouseY;

			@Override
			public void mouseDragged(MouseEvent e) {
				CubeCanvas.this.theta += (this.mouseX - e.getX()) / 500.0;
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

		this.r = 2.0 * max(max(boardX, boardY),boardZ);

		// Sets up vertex arrays
		this.vertexArray = new float[ (1 + boardX)
		                              * (1 + boardY) * (1 + boardZ)
		                              * 3 // three dimension values per vertex
		                              ];

		// Hoping it wants the vertices in (x,y,z) format
		for (int z = 0; z <= this.boardZ; z++) {
			for (int y = 0; y <= this.boardY; y++) {
				for (int x = 0; x <= this.boardX; x++) {
					int base = 3 * (x + y * (1 + boardX) + z * (1 + boardX) * (1 + boardY));
					this.vertexArray[base] = x;
					this.vertexArray[base + 1] = y;
					this.vertexArray[base + 2] = z;
				}
			}
		}

		this.vertexBuffer = FloatBuffer.wrap(this.vertexArray);

		this.indexArray = new int[12 * 3 * (1 + boardX) * (1 + boardY) * (1 + boardZ)];
		for(int dimension=0;dimension<3;dimension++){ //let dimension(0) = x, dimension(1) = y, ...
			for(int z=0;z<=boardZ;z++){
				for(int y=0;y<=boardY;y++){
					for(int x=0;z<=boardX;z++){
						int base = 12 * (x + y * (1 + boardX) + z * (1 + boardX) * (1 + boardY) + dimension * (1 + boardX) * (1 + boardY) * (1 + boardZ));
						indexArray[base] = 1;
						indexArray[base+1] = 1;
						indexArray[base+2] = 1;
						indexArray[base+3] = 1;
						indexArray[base+4] = 1;
						indexArray[base+5] = 1;
						indexArray[base+6] = 1;
						indexArray[base+7] = 1;
						indexArray[base+8] = 1;
						indexArray[base+9] = 1;
						indexArray[base+10] = 1;
						indexArray[base+11] = 1;
						indexArray[base+12] = 1;
					}
				}
			}
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		this.glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect,
		// zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

		// Allows transparency
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
}