public class Vector {
	private double[] coord;

	public Vector(double x, double y, double z) {
		this.coord = new double[] { x, y, z };
	}

	public Vector crossProduct(Vector vec) {
		double[] vecCoord = vec.getValue();
		// V1 x V2 = (v1yv2z Ð v1z v2y, v1zv2x Ð v1x v2z, v1xv2y Ð v1y v2x
		double xCross = (coord[1] * vecCoord[2]) - (coord[2] * vecCoord[1]);
		double yCross = (coord[2] * vecCoord[0]) - (coord[0] * vecCoord[2]);
		double zCross = (coord[0] * vecCoord[1]) - (coord[1] * vecCoord[0]);
		return new Vector(xCross, yCross, zCross);
	}

	public double dotProduct(Vector vec) {
		double[] vecCoord = vec.getValue();
		return (coord[0] * vecCoord[0] + coord[1] * vecCoord[1] + coord[2]
				* vecCoord[2]);
	}

	public double magnitude() {
		return Math.sqrt(Math.pow(coord[0], 2) + Math.pow(coord[1], 2)
				+ Math.pow(coord[2], 2));
	}

	public void normalize() {
		double mag = magnitude();
		coord[0] = coord[0]/mag;
		coord[1] = coord[1]/mag;
		coord[2] = coord[2]/mag;
		
	}

	public boolean isZero() {
		boolean zero = true;
		for (int i = 0; i < 3; ++i) {
			if (coord[i] != 0)
				zero = zero & false;
		}
		return zero;
	}

	public boolean equals(Vector vec) {
		boolean equal = true;
		double[] vecCoord = vec.getValue();
		for (int i = 0; i < 3; ++i) {
			if (coord[i] != vecCoord[i])
				equal = equal & false;
		}
		return equal;
	}

	public void scale(double scalar) {
		coord[0] = coord[0] * scalar;
		coord[1] = coord[1] * scalar;
		coord[2] = coord[2] * scalar;
	}

	public double[] getValue() {
		return coord;
	}

	public String toString() {
		return "[ " + coord[0] + ", " + coord[1] + ", " + coord[2] + " ]";
	}

	public static void main(String[] args) {
		Vector vec = new Vector(0, 0, 0);
		Vector vec1 = new Vector(1, 1, 1);
		System.out.println(vec);
		System.out.println(vec1);
		System.out.println(vec.isZero()); // true
		System.out.println(vec1.isZero()); // false
		System.out.println(vec.equals(vec1)); // false
		System.out.println(vec.equals(vec)); // true
		System.out.println(vec.crossProduct(vec1));
		vec1.scale(10);
		System.out.println(vec1);
		vec1.normalize();
		System.out.print(vec1);

	}
}