package engine.sprites.tools;

public class Position {

	int intX, intY;
	double doubleX,doubleY;

	public Position(){
		set(0,0);
	}
	
	public Position(int x, int y) {
		set(x,y);
	}

	public int getX() {
		return intX;
	}

	public int getY() {
		return intY;
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

	public void moveX(double m) {
		setX(doubleX + m);
	}
	public void moveY(double m) {
		setY(doubleY + m);
	}
	public void moveX(int m) {
		setX(intX + m);
	}
	public void moveY(int m) {
		setY(intY + m);
	}
}
