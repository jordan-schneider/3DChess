import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.sun.prism.impl.BufferUtil;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

@SuppressWarnings("serial")
public class ToyCanvas extends GLCanvas implements GLEventListener{
	float[]			vertexArray;
	FloatBuffer		vertexBuffer;
	int[]			indexArray;
	IntBuffer		indexBuffer;	
	
	public ToyCanvas() {
		addGLEventListener(this);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity(); // reset the model-view matrix
		
		gl.glColor3f(1f, 1f, 1f);
		
		gl.glTranslatef(0f, 0f, -60f);
		
		gl.glEnableClientState(GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
		
		gl.glDrawElements(GL_QUADS, indexBuffer.capacity(), GL_UNSIGNED_INT, indexBuffer);
		
		gl.glDisableClientState(GL_VERTEX_ARRAY);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		
		vertexArray = new float[]
				{-10f,-10f,-5f,
				10f,-10f,-5f,
				10f,10f,-5f,
				-10f,10f,-5f};
		
		vertexBuffer = BufferUtil.newFloatBuffer(vertexArray.length);
		for(int i=0;i<vertexArray.length;i++){
			vertexBuffer.put(vertexArray[i]);
		}
		//vertexBuffer.put(vertexArray);
		vertexBuffer.rewind();
		
		indexArray = new int[]{0,1,2,3};
		indexBuffer = BufferUtil.newIntBuffer(indexArray.length);
		for(int i=0;i<indexArray.length;i++){
			indexBuffer.put(indexArray[i]);
		}
		//indexBuffer.put(indexArray);
		indexBuffer.rewind();
		
		//System.out.println(Arrays.toString(vertexBuffer.array()));
		
		//System.out.println(Arrays.toString(indexBuffer.array()));
	
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		GLU glu = new GLU();
		
		if (height == 0)
			height = 1; // prevent divide by zero
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}



}
