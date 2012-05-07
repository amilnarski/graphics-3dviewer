public class Vertex{
	private double[] coord;
	
	public Vertex(double x, double y, double z, double h){
		coord = new double [] {x,y,z};
	}
	public Vertex(double x, double y, double z){
		coord = new double [] {x,y,z};
	}
	
	public Vector getVector(Vertex v){
		
		return null;
		
	}
	
	public double getX(){
		return coord[0];
	}
	public double getY(){
		return coord[1];
	}
	public double getZ(){
		return coord[2];
	}
	
	
	public String toString(){
		return "( "+coord[0]+", "+coord[1]+", "+coord[2]+")";	
		//return "( "+coord[0]+", "+coord[1]+", "+coord[2]+", "+coord[3]+" )";
		}
}