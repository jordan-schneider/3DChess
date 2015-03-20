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
		double x_0 = this.boardX / 2.0 - .5;
		double y_0 = this.boardY / 2.0 - .5;
		double z_0 = this.boardZ / 2.0 - .5;

		double cameraX = this.r * cos(this.phi) * sin(this.theta) + x_0;
		double cameraY = this.r * sin(this.phi) + y_0;
		double cameraZ = this.r * cos(this.phi) * cos(this.theta) + z_0;

		double upX = sin(this.phi) * sin(this.theta);
		double upY = abs(cos(this.phi));
		double upZ = sin(this.phi) * cos(this.theta);
		this.glu.gluLookAt(cameraX, cameraY, cameraZ, x_0, y_0, z_0, upX, upY,
				upZ); // Polar conversion + phi is from x-z plane instead of y axis

		// TODO sort indexArray before making into buffer
		// this.indexBuffer = faceSort(cameraX, cameraY, cameraZ);

		this.indexBuffer = BufferUtil.newIntBuffer(this.indexArray.length);
		this.indexBuffer.put(this.indexArray);
		this.indexBuffer.rewind();

		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
		gl.glDrawElements(GL_QUADS, 1, GL_FLOAT, this.indexBuffer);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	private void exchange(int i, int j) {
		int temp = this.indexArray[i];
		this.indexArray[i] = this.indexArray[j];
		this.indexArray[j] = temp;
	}

	// TODO reprogram this blatantly stolen quicksort code and make it work on distance and not value in the array
	private IntBuffer faceSort(double x, double y, double z) {
		if (this.indexArray == null || this.indexArray.length == 0) {
			return null;
		}
		quicksort(0, this.indexArray.length - 1);
		return BufferUtil.newIntBuffer(this.indexArray.length).put(
				this.indexArray);
	}

	private int index(int x, int y, int z) {
		return x + y * (1 + this.boardX) + z * (1 + this.boardX)
				* (1 + this.boardY);
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

		this.r = 2.0 * max(max(this.boardX, this.boardY), this.boardZ);

		// Sets up vertex arrays
		this.vertexArray = new float[3 * (1 + this.boardX) * (1 + this.boardY) * (1 + this.boardZ)];

		// Hoping it wants the vertices in (x,y,z) format
		for (int z = 0; z <= this.boardZ; z++) {
			for (int y = 0; y <= this.boardY; y++) {
				for (int x = 0; x <= this.boardX; x++) {
					int base = 3 * index(x, y, z);
					this.vertexArray[base] = x;
					this.vertexArray[base + 1] = y;
					this.vertexArray[base + 2] = z;
				}
			}
		}

		this.vertexBuffer = BufferUtil.newFloatBuffer(this.vertexArray.length);
		this.vertexBuffer.put(this.vertexArray);
		this.vertexBuffer.rewind();

		this.indexArray = new int[4 * 3 *
				(this.boardX * this.boardY * this.boardZ +
						this.boardX * this.boardY +
						this.boardX * this.boardZ +
				this.boardY * this.boardZ)];

		int currentPoint = 0;
		for (int dimension = 0; dimension < 3; dimension++) { // let dimension(0) = x, dimension(1) = y, ...
			for (int z = 0; z <= this.boardZ; z++) {
				for (int y = 0; y <= this.boardY; y++) {
					for (int x = 0; x <= this.boardX; x++) {
						if (dimension == 0) { // Do the sides in the y-z plane first
							if (z != this.boardZ && y != this.boardY) {
								this.indexArray[currentPoint] = index(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x, y + 1, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x, y + 1, z + 1);
								currentPoint++;
								this.indexArray[currentPoint] = index(x, y, z + 1);
								currentPoint++;
							}
						}
						else if (dimension == 1) { // Then in the x-z
							if (z != this.boardZ && x != this.boardX) {
								this.indexArray[currentPoint] = index(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x + 1, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x + 1, y, z + 1);
								currentPoint++;
								this.indexArray[currentPoint] = index(x, y, z + 1);
								currentPoint++;
							}
						}
						else { // finally x-y
							if (x != this.boardX && y != this.boardY) {
								this.indexArray[currentPoint] = index(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x, y + 1,
										z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x + 1,
										y + 1, z);
								currentPoint++;
								this.indexArray[currentPoint] = index(x + 1, y,
										z);
								currentPoint++;
							}
						}
					}
				}
			}
		}
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;

		int pivot = this.indexArray[low + (high - low) / 2];

		while (i <= j) {
			while (this.indexArray[i] < pivot) {
				i++;
			}

			while (this.indexArray[j] > pivot) {
				j--;
			}

			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}

		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
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
		this.glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

		// Allows transparency
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
}