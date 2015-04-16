import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2GL3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;
import static java.lang.Math.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javafx.geometry.Point3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
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

	float[]			vertexArray;
	FloatBuffer		vertexBuffer;
	int[]			indexArray;
	IntBuffer		indexBuffer;

	int				boardX, boardY, boardZ;

	private double	cameraX, cameraY, cameraZ;

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

	private int indexOf(int x, int y, int z) {
		return x + y * (1 + this.boardX) + z * (1 + this.boardX)
				* (1 + this.boardY);
	}

	private Point3D pointOf(int index) {
		return new Point3D(vertexArray[index], vertexArray[index + 1], vertexArray[index + 2]);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity(); // reset the model-view matrix

		gl.glColor4d(1.0, 1.0, 1.0, 0.2); // use a transparent color

		// the vertex array cube is centered at the origin so I have to shift back 1/2 the length of the cube
		double x_0 = this.boardX / 2.0 - .5;
		double y_0 = this.boardY / 2.0 - .5;
		double z_0 = this.boardZ / 2.0 - .5;

		cameraX = this.r * cos(this.phi) * sin(this.theta) + x_0;
		cameraY = this.r * sin(this.phi) + y_0;
		cameraZ = this.r * cos(this.phi) * cos(this.theta) + z_0;

		double upX = sin(this.phi) * sin(this.theta);
		double upY = abs(cos(this.phi));
		double upZ = sin(this.phi) * cos(this.theta);
		this.glu.gluLookAt(cameraX, cameraY, cameraZ, x_0, y_0, z_0, upX, upY,
				upZ); // Polar conversion + phi is from x-z plane instead of y axis

		this.indexBuffer = faceSort();
		this.indexBuffer.rewind();

		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
		gl.glDrawElements(GL_QUADS, indexBuffer.capacity(), GL_UNSIGNED_INT, this.indexBuffer);
		gl.glDisableClientState(GL_VERTEX_ARRAY);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

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
					int base = 3 * indexOf(x, y, z);
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
								this.indexArray[currentPoint] = indexOf(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x, y + 1, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x, y + 1, z + 1);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x, y, z + 1);
								currentPoint++;
							}
						}
						else if (dimension == 1) { // Then in the x-z
							if (z != this.boardZ && x != this.boardX) {
								this.indexArray[currentPoint] = indexOf(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x + 1, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x + 1, y, z + 1);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x, y, z + 1);
								currentPoint++;
							}
						}
						else { // finally x-y
							if (x != this.boardX && y != this.boardY) {
								this.indexArray[currentPoint] = indexOf(x, y, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x, y + 1,
										z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x + 1,
										y + 1, z);
								currentPoint++;
								this.indexArray[currentPoint] = indexOf(x + 1, y,
										z);
								currentPoint++;
							}
						}
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
		this.glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

		// Allows transparency
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}


	// TODO this code isn't working because pointOf(indexOf(x,y,z) != (x,y,z). The thing renders the first time, so its probably in pointOf? Check both though.
	private IntBuffer faceSort() {
		if (this.indexArray == null || this.indexArray.length == 0) {
			return null;
		}
		quicksort(0, this.indexArray.length - 1 - 3);
		return (IntBuffer) BufferUtil.newIntBuffer(this.indexArray.length).put(
				this.indexArray).rewind();
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;

		System.out.println("High is " + high + " and low is " + low);
		
		double pivot = eval(low + (high - low) / 2);

		while (i <= j) {
			System.out.println("i is " + i + " and j is " + j);
			while (eval(i) < pivot) {
				i += 4;
			}

			while (eval(j) > pivot) {
				j -= 4;
			}

			if (eval(i) <= eval(j)) {
				exchange(i, j);
				i += 4;
				j -= 4;
			}
		}

		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private double eval(int index) {
		Point3D p_1 = pointOf(indexArray[index]), p_2 = pointOf(indexArray[index + 1]), p_3 = pointOf(indexArray[index + 2]), p_4 = pointOf(indexArray[index + 3]);
		Point3D pMid = getMidpointOfSquare(p_1, p_2, p_3, p_4);
		return pMid.distance(cameraX, cameraY, cameraZ);
	}

	private Point3D getMidpointOfSquare(Point3D p_1, Point3D p_2, Point3D p_3, Point3D p_4) {
		double maxX = max(max(p_1.getX(), p_2.getX()), max(p_3.getX(), p_4.getX()));
		double minX = min(min(p_1.getX(), p_2.getX()), min(p_3.getX(), p_4.getX()));
		double midX = (maxX + minX) / 2;

		double maxY = max(max(p_1.getY(), p_2.getY()), max(p_3.getY(), p_4.getY()));
		double minY = min(min(p_1.getY(), p_2.getY()), min(p_3.getY(), p_4.getY()));
		double midY = (maxY + minY) / 2;

		double maxZ = max(max(p_1.getZ(), p_2.getZ()), max(p_3.getZ(), p_4.getZ()));
		double minZ = min(min(p_1.getZ(), p_2.getZ()), min(p_3.getZ(), p_4.getZ()));
		double midZ = (maxZ + minZ) / 2;

		return new Point3D(midX, midY, midZ);
	}

	private void exchange(int i, int j) {
		int temp_1 = this.indexArray[i], temp_2 = this.indexArray[i + 1], temp_3 = this.indexArray[i + 2], temp_4 = this.indexArray[i + 3];
		this.indexArray[i] = this.indexArray[j];
		indexArray[i + 1] = this.indexArray[j + 1];
		indexArray[i + 2] = indexArray[j + 2];
		indexArray[i + 3] = indexArray[j + 3];

		this.indexArray[j] = temp_1;
		indexArray[j + 1] = temp_2;
		indexArray[j + 2] = temp_3;
		indexArray[j + 3] = temp_4;
	}
}