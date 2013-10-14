package engine.sprites.tools;

public class Position {

	int intX, intY;
	double doubleX,doubleY;

	public double getX() {
		return doubleX;
	}

	public double getY() {
		return doubleY;
	}
	
	public void set(int x, int y){
		this.intX = x;
		doubleX = (double) x;
		this.intY = y;
		doubleY = (double) y;
	}
	
	public void set(double x, double y){
		this.doubleX = x;
		intX = (int)x;
		this.doubleY = y;
		intY = (int)y;
	}
	
	public void setX(int x) {
		this.intX = x;
		doubleX = (double) x;
	}
	public void setY(int y) {
		this.intY = y;
		doubleY = (double) y;
	}

	public void setX(double x) {
		this.doubleX = x;
		intX = (int)x;
	}
	public void setY(double y) {
		this.doubleY = y;
		intY = (int)y;
	}


	
}
