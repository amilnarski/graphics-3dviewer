public class Vertex{
	private double[] coord;
	
	public Vertex(double x, double y, double z, double h){
		coord = new double [] {x,y,z,h};
	}
	
	public Vector getVector(Vertex v){
		
		return null;
		
	}
	
	public String toString(){
			return "( "+coord[0]+", "+coord[1]+", "+coord[2]+", "+coord[3]+" )";
		}
}