public class Matrix{
	private double[][] transform;
	
	public double [][] getTrans(){
		return transform;
	}
	
	public Matrix(double[][] transformValues){
		this.transform = transformValues;
	}
	
	public Matrix(){
		this.transform = new double [][]{{0,0},{0,0}};
	}
	
	public Matrix mult(Matrix matrix){
		//CHECK MATRIX DIMENSTIONS
		if (transform[0].length != matrix.getTrans().length){
			//CAN'T MULT, ERROR OUT
			return null;
		}
		double[][] result = new double [transform[0].length][matrix.getTrans().length];
		for (int row = 0; row<result.length; row++){
			for (int col = 0; col<result.length; col++){
				//CALCULATE THE RESULT FOR THIS POSITION IN THE RESULT MATRIX
				for (int pos =0; pos<transform[0].length; pos++){
					result [row][col] += transform[row][pos] * matrix.getTrans()[pos][col];
				}
			}
		}
		//RETURN A NEW MATRIX WITH THE RESULT
		return new Matrix(result);
		
	}
	
	public Vertex transform(Vertex v){
		Matrix vertex = new Matrix(new double[][]{{v.getX(),0,0,0},{v.getY(),0,0,0}, {v.getZ(),0,0,0},{1,0,0,0}});
		//Matrix vertex = new Matrix(new double[][]{{v.getX(),v.getY(), v.getZ(),0}});
		//System.out.println(vertex);
		Matrix tv = mult(vertex);
		//System.out.print("After Mult: "+tv);
		double[][] res = tv.getTrans();
		Vertex tran = new Vertex(res[0][0],res[1][0],res[2][0]);
		return tran;
	}
	
	public String toString(){
		String to = "";
		to+="[ "+'\n';
		for (int row = 0; row<transform.length; row++){
			to+=" ";
			for (int col = 0; col< transform[0].length; col++){
				to += transform[row][col];
				to+="\t";
			}
			to+=" "+'\n';
		}
		to+=" ]";
	return to;
	}
	
	public static void main (String args[]){
		Matrix m = new Matrix(new double[][]{{1,1},{3,1},{1,2}});
		Matrix n = new Matrix(new double[][]{{2,72,1},{2,12,1}});
		System.out.println(m);
		System.out.println(n);
		System.out.println(m.mult(n));
	}
}