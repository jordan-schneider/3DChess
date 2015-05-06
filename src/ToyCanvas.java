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

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.sun.prism.impl.BufferUtil;

public class ToyCanvas extends GLCanvas implements GLEventListener{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private float[] vertexArray, textureCoordArray;
	private int[] indexArray;
	
	private FloatBuffer vertexBuffer, textureCoordBuffer;
	private IntBuffer indexBuffer;
	
	Texture texture;
	
	private GLU	glu;
	
	public ToyCanvas() {
		addGLEventListener(this);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		
		glu.gluLookAt(0, 0, -100, 0, 0, -50, 0, 1, 0);
		
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity(); // reset the model-view matrix
		
		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
		
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL_FLOAT, 0, textureCoordBuffer);

		texture.bind(gl);
		
		gl.glColor4d(1.0, 1.0, 1.0, 1.0);

		gl.glDrawElements(GL_QUADS, indexBuffer.capacity(), GL_UNSIGNED_INT,
				indexBuffer);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL_VERTEX_ARRAY);

		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
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
		
		vertexArray = new float[] {-10f,-10f,-50f,
				-10f,10f,-50f,
				10f,10f,-50f,
				10f,-10f,-50f};
		textureCoordArray = new float[] {0f,0f,
				1f,1f,
				1f,0f,
				0f,1f};
		indexArray = new int[] {0,1,2,3};
		
		vertexBuffer = BufferUtil.newFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray);
		vertexBuffer.rewind();
		textureCoordBuffer = BufferUtil.newFloatBuffer(textureCoordArray.length);
		textureCoordBuffer.put(textureCoordArray);
		textureCoordBuffer.rewind();
		indexBuffer = BufferUtil.newIntBuffer(indexArray.length);
		indexBuffer.put(indexArray);
		indexBuffer.rewind();
		
		try {
			texture = TextureIO.newTexture(new File("Black Bishop.png"), false);
		} catch (GLException | IOException e) {
			e.printStackTrace();
		} 
		
		texture.bind(gl);
		texture.enable(gl);
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
