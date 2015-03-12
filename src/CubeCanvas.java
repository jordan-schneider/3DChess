import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.opengl.util.gl2.GLUT;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import static java.lang.Math.*;

@SuppressWarnings("serial")
public class CubeCanvas extends GLCanvas implements GLEventListener{

	private Board board;
	private float theta, phi, r;
	private GLU glu; 
	private GLUT glut;

	public CubeCanvas(Board board) {
		//super();
		addGLEventListener(this);
		this.board = board;
		//setMinimumSize(new Dimension(0,0)); Unnecesary for fixed size
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
				theta += e.getX() - x;
				phi += e.getY() - y;
				x = e.getX();
				y = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				//theta = 0; phi=0;
			}
		});
		
		r = 2*max(max(board.getSize()[0], board.getSize()[1]), board.getSize()[2]);
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
	}

	//gl.glTranslated(-1 * board.getSize()[0] / 2, -1 * board.getSize()[1] / 2, 0); //Center's the board before generating it
			//gl.glTranslated(0, 0, -5 * board.getSize()[2] / 2); //Center's the board before generating it
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity();  // reset the model-view matrix
		gl.glColor4d(1.0, 1.0, 1.0, 0.2);

		//glu.gluLookAt(0, 0, r, 0,0,0,0,1,0);
		
		//gl.glTranslated(-board.getSize()[0] / -2, board.getSize()[1] / -2, board.getSize()[2] / -2); //Center's the board before generating it
		
		
		
		for(int i=0;i<board.getSize()[2];i++){
			for(int j=0;j<board.getSize()[1];j++){
				for(int k=0;k<board.getSize()[0];k++){
					glut.glutSolidCube(1f);
					gl.glTranslatef(1f, 0f, 0f);
				}
				gl.glTranslatef(-5f, 1f, 0f);
			}
			gl.glTranslatef(0f,-5f, 1f);
		}
		
		gl.glTranslated(0,0,-10);
		
		//glu.gluLookAt(r * cos(phi) * cos(theta), r * cos(phi) * sin(theta), r * sin(phi), 0, 0, 0, sin(phi) * sin(theta), sin(phi) * cos(theta), cos(phi));
		
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) { }
}