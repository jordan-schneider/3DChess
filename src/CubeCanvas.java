import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GLArrayData;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import static com.jogamp.opengl.GL2.*;
import static java.lang.Math.*;

@SuppressWarnings("serial")
public class CubeCanvas extends GLCanvas implements GLEventListener{

	private Board board;
	private double theta = 0, phi = 0, r;
	private GLU glu; 
	private GLUT glut;
	float[] vertexArray;
	FloatBuffer vertexBuffer;
	int[] indexArray;
	IntBuffer indexBuffer;

	public CubeCanvas(Board board) {
		addGLEventListener(this);
		this.board = board;
	}

	@Override
	public void init(GLAutoDrawable drawable) {		
		GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
		glu = new GLU();                         // get GL Utilities
		glut = new GLUT();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f);      // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

		addMouseMotionListener(new MouseMotionListener() {
			int x,y;

			@Override
			public void mouseDragged(MouseEvent e) {
				theta += (x - e.getX()) / 500.0;
				phi += (y - e.getY()) / 500.0;
				x = e.getX();
				y = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}
		});

		r = 2.0 * max(max(board.getSize()[0], board.getSize()[1]), board.getSize()[2]);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		if (height == 0) height = 1;   // prevent divide by zero
		float aspect = (float)width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

		// Allows transparency
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//Sets up vertex arrays
		/*
		vertexArray = new float[(1 + board.getSize()[0]) * 
		                        (1 + board.getSize()[1]) * 
		                        (1 + board.getSize()[2]) * 
		                        3 //three dimension values per vertex
 		                        ];
		
		for(int i=0;i<=board.getSize()[2];i++){
			for(int j=0;j<=board.getSize()[1];j++){
				for(int k=0;k<=board.getSize()[0];k++){
					vertexArray[
					            (
					            		i * (1 + board.getSize()[0]) +
					            		j * (1 + board.getSize()[1]) + 
					            		k
					            ) * 3
					            ] = k;
					vertexArray[
					            (
					            		i * (1 + board.getSize()[0]) +
					            		j * (1 + board.getSize()[1]) + 
					            		k
					            ) * 3 + 1
					            ] = j;
					vertexArray[
					            (
					            		i * (1 + board.getSize()[0]) +
					            		j * (1 + board.getSize()[1]) + 
					            		k
					            ) * 3 + 2
					            ] = i;
				}
			}
		}
		
		vertexBuffer = FloatBuffer.wrap(vertexArray);
		
		indexArray = new int[5]; //TODO figure out how many indices I'll need	
		*/	
	}

	//gl.glTranslated(-1 * board.getSize()[0] / 2, -1 * board.getSize()[1] / 2, 0); //Center's the board before generating it
	//gl.glTranslated(0, 0, -5 * board.getSize()[2] / 2); //Center's the board before generating it

	//If using glTranslate, the camera is fixed at 0,0,0 and the translations only affect the placmenet of objects
	//glTranslate calls after generating objects do nothing
	//glRotate operates in the same fashion

	//gluLookAt also only works if placed before object generation

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity();  // reset the model-view matrix

		gl.glColor4d(1.0, 1.0, 1.0, 0.2);

		System.out.println("Phi is " + phi + " and theta is " + theta);
		
		//glutSolidCube centers the cube at the origin so I have to shift back 1/2 the lengh of the cube
		double x_0 = board.getSize()[0] / 2.0 - .5;
		double y_0 = board.getSize()[1] / 2.0 - .5;
		double z_0 = board.getSize()[2] / 2.0 - .5;
		
		glu.gluLookAt(
				r * cos(phi) * sin(theta) + x_0, r * sin(phi) + y_0, r * cos(phi) * cos(theta) + z_0,
				x_0, y_0, z_0,
				sin(phi) * sin(theta), abs(cos(phi)), sin(phi) * cos(theta));
		
		/*
		//TODO sort indexArray before making into buffer
		
		IntBuffer indexBuffer = IntBuffer.wrap(indexArray);
		
		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
		
		gl.glDrawElements(GL_QUADS, 1, GL_FLOAT, indexBuffer);
		*/
		
		//This will be depricated as soon as I make Vertex Arrays work
		for(int i=0;i<board.getSize()[2];i++){
			for(int j=0;j<board.getSize()[1];j++){
				for(int k=0;k<board.getSize()[0];k++){
					glut.glutSolidCube(1f);
					gl.glTranslatef(1f, 0f, 0f);
				}
				gl.glTranslatef(-1 * board.getSize()[0], 1f, 0f);
			}
			gl.glTranslatef(0f,-1 * board.getSize()[1], 1f);
		}
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) { }
}