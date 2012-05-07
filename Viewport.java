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
import java.util.LinkedList;

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
		frame.setSize(700, 700);

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

// back face culling - just negate the z coordinate

class DrawInput implements GLEventListener, KeyListener, MouseListener {
	private GL gl;
	private GLU glu;

	// world coord limits
	public static final int minX = 0;
	public static final int maxX = 400;
	public static final int minY = 0;
	public static final int maxY = 400; // 294

	private GLCanvas canvas;
	private ReadObjectAndViewingFiles reader = new ReadObjectAndViewingFiles();
	// view properties
	Matrix Nper;
	// polygon properties
	private boolean viewChanged = true;
	private boolean readPolygons = false;
	private Vertex[] vertex;
	private Vertex[] drawVertices;
	double[][] polyColor;
	int[][] polygonsArray;
	int numPoly;
	int numVs;
	Vertex vrp;
	Vector vpn;
	Vector vup;
	Vertex prp;
	double umin;
	double umax;
	double vmin;
	double vmax;
	double frontClip;
	double backClip;
	Vector u;
	Vector v;
	Vector n;

	// projection matrix
	Matrix s = new Matrix(new double[][] { { 200, 0, 0, 0 }, { 0, 200, 0, 0 },
			{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
	Matrix tp = new Matrix(new double[][] { { 1, 0, 0, 1 }, { 0, 1, 0, 1 },
			{ 0, 0, 1, 0 }, { 0, 0, 0, 0 } });
	Matrix stp = s.mult(tp);

	// view transformation booleans
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean in;
	private boolean out;
	private boolean rotateRight;
	private boolean rotateLeft;
	private boolean reset;
	private boolean recolor;

	public void display(GLAutoDrawable drawable) {
		if (!readPolygons) {
			readPolygons();
			System.out.println("read files");
		}
		if (viewChanged) {
			viewChanged();
		}
		gl.glClear(GL.GL_COLOR_BUFFER_BIT); // clears color buffer
		gl.glColor3f(0, 0, 0); // fill color for polygons

		/*
		 * // random poly gl.glBegin(GL.GL_POLYGON); gl.glVertex2d(10, 10);
		 * gl.glVertex2d(100, 60); gl.glVertex2d(150, 75); gl.glVertex2d(100,
		 * 100); gl.glVertex2d(25, 200); gl.glEnd();
		 */
		/*gl.glBegin(GL.GL_POLYGON);
		for (int i = 0; i < this.drawVertices.length; i++) {
			if (drawVertices[i] != null) {
				//System.out.println(drawVertices[i]);
				Vertex v = new Vertex(-1*drawVertices[i].getX()/drawVertices[i].getZ(),-1*drawVertices[i].getY()/drawVertices[i].getZ(),-1*drawVertices[i].getZ()/drawVertices[i].getZ());
				System.out.println(v);
				v = stp.transform(v);
				//System.out.println(v);
				gl.glVertex2d(v.getX(), v.getY());
				}

		}
		gl.glEnd();*/
		
		
		for(int i = 0; i<this.polygonsArray[0].length; i++){
			if (isFrontFacing(i)){
				gl.glColor3d(this.polyColor[i][0], this.polyColor[i][1], this.polyColor[i][2]);
				if(recolor){
					gl.glColor3d(this.polyColor[i][1], this.polyColor[i][2], this.polyColor[i][0]);
				}
				gl.glBegin(GL.GL_POLYGON);
				for (int j = 1; j<=polygonsArray[i][0]; j++){
					Vertex v = new Vertex(-1*drawVertices[polygonsArray[i][j]].getX()/drawVertices[polygonsArray[i][j]].getZ(),-1*drawVertices[polygonsArray[i][j]].getY()/drawVertices[polygonsArray[i][j]].getZ(),-1*drawVertices[polygonsArray[i][j]].getZ()/drawVertices[polygonsArray[i][j]].getZ());
					v = stp.transform(v);
					gl.glVertex2d(v.getX(), v.getY());
				}
				gl.glEnd();
			}
		}

		gl.glFlush();
		drawable.swapBuffers();

		//

		// mod the values to change the view
		//1 original x y x
		//rotate by a degree with vup
		
		if (this.left) {
			vrp = new Vertex(vrp.getX() + 1, vrp.getY(), vrp.getZ());
			viewChanged = true;
			System.out.println("L");
		}
		if (this.right) {
			vrp = new Vertex(vrp.getX() - 1, vrp.getY(), vrp.getZ());
			viewChanged = true;
			System.out.println("R");
		}
		if (this.up) {
			vrp = new Vertex(vrp.getX(), vrp.getY()-1, vrp.getZ());
			viewChanged = true;
			System.out.println("U");
		}
		if (this.down) {
			vrp = new Vertex(vrp.getX(), vrp.getY()+1, vrp.getZ());
			viewChanged = true;
			System.out.println("D");
		}
		if (this.in) {
			vrp = new Vertex(vrp.getX(), vrp.getY(), vrp.getZ()-1);
			viewChanged = true;
			System.out.println("I");
		}
		if (this.out) {
			vrp = new Vertex(vrp.getX(), vrp.getY(), vrp.getZ()+1);
			viewChanged = true;
			System.out.println("O");
		}
		if (this.rotateLeft) {
			double [] vupD = vup.getValue();
			vup = new Vector(vupD[0]+0.1,vupD[1]+0.1,vupD[2]);
			viewChanged = true;
			System.out.println("\\");
		}
		if (this.rotateRight) {
			double [] vupD = vup.getValue();
			vup = new Vector(vupD[0]-0.1,vupD[1]-0.1,vupD[2]);
			viewChanged = true;
			System.out.println("/");
		}

		// reset the view modification booleans
		left = false;
		right = false;
		up = false;
		down = false;
		in = false;
		out = false;
		rotateRight = false;
		rotateLeft = false;
	}
	
	private boolean isFrontFacing(int index){
		boolean frontFacing = false;
		Vertex one = this.drawVertices[this.polygonsArray[index][1]];
		Vertex two = this.drawVertices[this.polygonsArray[index][2]];
		Vertex three = this.drawVertices[this.polygonsArray[index][3]];
		Vector v1 = new Vector(one.getX() - two.getX(),one.getY() - two.getY(),one.getZ() - two.getZ());
		Vector v2 = new Vector(three.getX() - two.getX(),three.getY() - two.getY(),three.getZ() - two.getZ());
		Vector planeNorm = v1.crossProduct(v2);
		Vector viewNorm = new Vector(0,0,1);
		//this might need to be > ?
		if (viewNorm.dotProduct(planeNorm) < 0){
			frontFacing = true;
		}
		return frontFacing;
	}

	private void viewChanged() {
		// Nper = Sper SHper T(-PRP) R T(-VRP)
		double SHx = ((umin + umax) / 2 - prp.getX()) / prp.getZ();
		double SHy = ((vmin + vmax) / 2 - prp.getY()) / prp.getZ();
		n = vpn;
		n.normalize();
		u = vup.crossProduct(n);
		u.normalize();
		v = n.crossProduct(u);
		System.out.println("u - " + u + "\nv - " + v + "\nn - " + n); // print
																		// u,v,n
		double[] uD = u.getValue();
		double[] vD = v.getValue();
		double[] nD = n.getValue();
		Matrix shPer = new Matrix(new double[][] { { 1, 0, SHx, 0 },
				{ 0, 1, SHy, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });
		Matrix tNegVRP = new Matrix(new double[][] {
				{ 1, 0, 0, (-(this.vrp.getX())) },
				{ 0, 1, 0, (-(this.vrp.getY())) },
				{ 0, 0, 1, (-(this.vrp.getZ())) }, { 0, 0, 0, 1 } });
		Matrix r = new Matrix(new double[][] { { uD[0], uD[1], uD[2], 0 },
				{ vD[0], vD[1], vD[2], 0 }, { nD[0], nD[1], nD[2], 0 },
				{ 0, 0, 0, 1 } });
		Matrix tNegPRP = new Matrix(new double[][] {
				{ 1, 0, 0, (-(this.prp.getX())) },
				{ 0, 1, 0, (-(this.prp.getY())) },
				{ 0, 0, 1, (-(this.prp.getZ())) }, { 0, 0, 0, 1 } });
		double b = backClip;
		System.out.println("B - " + b);
		Matrix sXY = new Matrix(new double[][] {
				{ (2 * prp.getZ()) / (umax - umin), 0, 0, 0 },
				{ 0, (2 * prp.getZ()) / (vmax - vmin), 0, 0 }, { 0, 0, 1, 0 },
				{ 0, 0, 0, 1 } });
		Matrix sXYZ = new Matrix(new double[][] {
				{ (-1 / (-prp.getZ() + b)), 0, 0, 0 },
				{ 0, (-1 / (-prp.getZ() + b)), 0, 0 },
				{ 0, 0, (-1 / (-prp.getZ() + b)), 0 }, { 0, 0, 0, 1 } });
		Matrix sPer = sXYZ.mult(sXY);
		System.out.println("T(-VRP) - " + tNegVRP + "\nR - " + r
				+ "\nT(-PRP) - " + tNegPRP + "\nShear - " + shPer
				+ "\nScale - " + sPer);
		// mult these matrices to get the Nper Matrix
		this.Nper = r.mult(tNegVRP);
		this.Nper = tNegPRP.mult(Nper);
		this.Nper = shPer.mult(Nper);
		this.Nper = sPer.mult(Nper);

		System.out.println("NPER - \n" + Nper);

		// create the set of transformed vertices to draw
		this.drawVertices = new Vertex[this.vertex.length];
		if (vertex == null) {
			System.out.println("vertex[] is null");
			System.exit(-100);
		}
		for (int i = 0; i < this.drawVertices.length; i++) {
			if (vertex[i] != null) {
				System.out.println(vertex[i]);
				drawVertices[i] = Nper.transform(vertex[i]);
				System.out.println(drawVertices[i]);
				// System.out.println("@"+ i+'\t'+drawVertices[i]);
			} else {
				vertex[i] = null;
			}
		}

		// set view as not changed
		this.viewChanged = false;
	}

	@SuppressWarnings("static-access")
	private void readPolygons() {
		reader.main(new String[] { "house.object", "fig6_22.view" });
		// polygon data
		this.vertex = reader.v;
		this.polyColor = reader.polyColor;
		this.polygonsArray = reader.polygon;
		// view data
		this.vrp = reader.vrp;
		this.umin = reader.umin;
		this.umax = reader.umax;
		this.vmin = reader.vmin;
		this.vmax = reader.vmax;
		this.prp = reader.prp;
		this.frontClip = reader.frontClip;
		this.backClip = reader.backClip;
		this.vpn = reader.vpn;
		this.vup = reader.vup;

		// switch read to true
		this.readPolygons = true;
	}

	public void keyTyped(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		// Left
		case 'l':
		case 'L':
			this.left = true;
			break;
		// Right
		case 'r':
		case 'R':
			this.right = true;
			break;
		// In
		case 'i':
		case 'I':
			this.in = true;
			break;
		// Out
		case 'o':
		case 'O':
			this.out = true;
			break;
		// Up
		case 'u':
		case 'U':
			this.up = true;
			break;
		// Down
		case 'd':
		case 'D':
			this.down = true;
			break;
		// Rotate Right
		case '/':
			this.rotateRight = true;
			break;
		case '\\':
			this.rotateLeft = true;
			break;
		case 'c':
		case 'C':
			this.recolor = !this.recolor;
			break;
		case '!':
			//reset the display
			this.readPolygons = false;
			this.viewChanged = true;
			break;
		default:
			System.out
					.println("The accepted commands are 'U' or 'D', 'L' or 'R', 'I' or 'O', '/' or '\\'.");
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

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// TODO Auto-generated method stub

	}

	public void init(GLAutoDrawable drawable) {
		this.gl = drawable.getGL();
		this.glu = new GLU();
		gl.glClearColor(255, 255, 255, 0);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(minX, maxX, minY, maxY);
		drawable.setGL(new DebugGL(drawable.getGL()));

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

}