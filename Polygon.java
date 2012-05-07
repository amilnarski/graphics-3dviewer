
public class Polygon {

	/**
	 * @param args
	 */
	
	private int[][] vertices;
	private int vSize = 10;
	private int vPos;
	private int color;
	public void addVertex(){
		
	}
	
	public Polygon(){
		vertices = new int[2][vSize];
		vPos = 0;
	}
	
	public Polygon(int[][] vList){
		vertices = new int[2][vSize];
		vPos = 0;
		int vLen = vList.length;
		int vCopy;
		if (vLen > vSize)
			vCopy = vSize;
		else
			vCopy = vLen;
		for (int i = 0; i< vCopy; i++){
			vertices [0][i] = vList[0][i]; 
			vertices [1][i] = vList[1][i];
		}
	}
	
	public void setColor(int color){
		this.color = color;
	};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
