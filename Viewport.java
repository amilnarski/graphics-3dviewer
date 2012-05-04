import java.awt.event.*;
import java.awt.*;

// imports the openGL stuff
import javax.media.opengl.GL;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.sun.opengl.util.Animator;
import javax.media.opengl.GLCapabilities;

@SuppressWarnings("unused")
public class Viewport {

	/**
	 * @param args
	 */
	private static Frame frame;
	private static DrawInput input;

	public static void main(String[] args) {
		Frame frame = new Frame("Viewport");
		frame.setLocation(10, 10);
		frame.setSize(500, 588);

		GLCapabilities glCap = new GLCapabilities();
		glCap.setRedBits(8);
		glCap.setGreenBits(8);
		glCap.setBlueBits(8);
		glCap.setAlphaBits(8);

		glCap.setDoubleBuffered(true);

		GLCanvas canvas = new GLCanvas(glCap);
		frame.add(canvas);

		Animator ani = new Animator(canvas);

		input = new DrawInput(canvas);
		canvas.addGLEventListener(input);
		canvas.addKeyListener(input);
		canvas.addMouseListener(input);
		canvas.swapBuffers(); // this might be able to be ignored.

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		frame.setVisible(true);
		ani.start();

	}
}

class DrawInput implements GLEventListener, KeyListener, MouseListener {
	private GL gl;
	private GLU glu;
		
	// world coord limits
	public static final int minX = 0;
	public static final int maxX = 250;
	public static final int minY = 0;
	public static final int maxY = 250; //294

	private GLCanvas canvas;
	private ReadObjectAndViewingFiles reader  = new ReadObjectAndViewingFiles();
	
	//polygon properties
	
	//view transformation booleans
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean in;
	private boolean out;
	
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT); //clears color buffer
		gl.glColor3f(0, 0, 0); //fill color for polygons
		
		//draw buttons
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2i(0, 0);
		gl.glVertex2i(0, 44);
		gl.glVertex2i(22, 44);
		gl.glVertex2i(22, 0);
		gl.glEnd();
		
		
		gl.glColor3f(0.3f, 0.3f, 0.3f);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2i(22, 0);
		gl.glVertex2i(22, 44);
		gl.glVertex2i(44, 44);
		gl.glVertex2i(44, 0);
		gl.glEnd();
		
		
		gl.glColor3f(0.5f, 0.5f, 0.5f);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2i(44, 0);
		gl.glVertex2i(44, 44);
		gl.glVertex2i(44, 0);
		gl.glVertex2i(66, 0);
		gl.glEnd();
		
		
		gl.glFlush();
		drawable.swapBuffers();
	}
	
	public void keyTyped(KeyEvent arg0) {
		switch(arg0.getKeyChar()){
		//Left
		case 'l': case 'L':
			this.left = true;
			break;
		//Right
		case 'r': case 'R':
			this.right = true;
			break;
		//In
		case 'i': case 'I':
			this.in = true;
			break;
		//Out
		case 'o': case 'O':
			this.out = true;
			break;
		//Up
		case 'u': case 'U':
			this.up = true;
			break;
		//Down
		case 'd': case 'D':
			this.down = true;
			break;
		default: 
			System.out.println("The accepted commands are 'U'/'D','L'/'R','I'/'O'.");
			break;
		}

	}
	
	public DrawInput(GLCanvas c) {
		this.canvas = c;
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// TODO Auto-generated method stub

	}

	public void init(GLAutoDrawable drawable) {
		this.gl = drawable.getGL();
		this.glu = new GLU();
		gl.glClearColor(255, 255, 255, 0);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(minX, maxX, minY, maxY);
		drawable.setGL(new DebugGL (drawable.getGL()));

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

}