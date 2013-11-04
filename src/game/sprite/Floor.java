package game.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.GameDisplay;

public class Floor {

	public static final int PILLARSTRENGTH = 0;
	public static final int WEIGHT = 1;
	public static final int REGION = 2;
	public static final int TOTALWEIGHT = 3;
	public static final int COMPRESSION = 4;

	public static final int maxpillars = 4;
	private static int width = 300;
	private static int height = 40;
	private static int pillarWidth = width / (maxpillars*2);

	private int rtCompression;
	private int ltCompression;
	private int level;

	private int[][] pillar;
	private double floorAngle = 0;

	private Dimension position;
	private Dimension displayBounds;
	private int torque;


	public Floor(int lvl) {
		level = lvl;
		displayBounds = GameDisplay.getBounds();

		position = new Dimension();
		position.setSize(displayBounds.width / 2 - (width / 2),
				(displayBounds.height * 3 / 4 - (height * level))
						- (height / 2));

		pillar = new int[maxpillars][5];

		pillar[0][PILLARSTRENGTH] = 1;
		pillar[1][PILLARSTRENGTH] = 1;
		pillar[2][PILLARSTRENGTH] = 1;
		pillar[3][PILLARSTRENGTH] = 1;

		pillar[0][REGION] = -2;
		pillar[1][REGION] = -1;
		pillar[2][REGION] = 1;
		pillar[3][REGION] = 2;

		pillar[0][TOTALWEIGHT] = 1;
		pillar[1][TOTALWEIGHT] = 1;
		pillar[2][TOTALWEIGHT] = 1;
		pillar[3][TOTALWEIGHT] = 1;
		
		pillar[0][WEIGHT] = 1;
		pillar[1][WEIGHT] = 1;
		pillar[2][WEIGHT] = 1;
		pillar[3][WEIGHT] = 1;
		
	}

	public void update(double angle, int[] w) {

		addWeight(w);
		
		this.floorAngle += angle;

		calcCompression();
		calcHeight();
		// check for degrees
		if (angle > Math.PI * 2)
			angle = 0;
		if (angle < -Math.PI * 2)
			angle = 0;
	}

	private void addWeight(int[] w){
		for (int i = 0; i < maxpillars; i++){
			pillar[i][TOTALWEIGHT] = w[i] + pillar[i][WEIGHT];
		}
	}
	
	public void addWeight(int pillar, int weight){
		if(pillar < maxpillars){
			this.pillar[pillar][WEIGHT] += weight;
		}
	}
	
	private void calcCompression() {

		rtCompression = 0;
		torque = 0;
		for (int i = 0; i < maxpillars; i++) {
			torque += pillar[i][WEIGHT] * pillar[i][REGION];
		}
		for (int i = 0; i < maxpillars; i++) {
			if (pillar[i][TOTALWEIGHT] != 0)
				pillar[i][COMPRESSION] = torque / pillar[i][TOTALWEIGHT] / pillar[i][PILLARSTRENGTH];
			else 
				pillar[i][COMPRESSION] = 0;
		}

	}

	private void calcHeight() {
		ltCompression = 0;
		rtCompression = 0;
		for (int i = 0; i < maxpillars; i++)
			ltCompression += pillar[i][COMPRESSION];
		for (int i = 0; i < maxpillars; i++)
			rtCompression += pillar[i][COMPRESSION];
		
		
	}

	public void draw(Graphics2D g) {
		
		g.setColor(Color.green);
		g.fillRect(position.width, position.height, pillarWidth, height - ltCompression);
		g.fillRect(position.width + width, position.height, pillarWidth, height - rtCompression);
		g.setColor(Color.BLACK);
		g.drawLine(position.width, position.height + (height - ltCompression), position.width + width, position.height + (height - rtCompression));
		
//		// draw floor
//		g.rotate(-floorAngle, position.width + (width / 2), position.height
//				+ (height / 2));
//		{
//			g.setColor(Color.BLUE);
//			g.fillRect(position.width, position.height, width, height);
//
//			// draw pillars
//			g.setColor(Color.BLACK);
//			for (int i = 0; i < maxpillars; i++) {
//				g.fillRect(position.width + (i * pillarWidth)
//						+ (pillarWidth / 4), position.height, width / 10,
//						height);
//			}
//
//			g.setColor(Color.BLACK);
//			g.drawRect(position.width, position.height, width, height);
//		}
//		g.rotate(floorAngle, position.width + (width / 2), position.height
//				+ (height / 2));
//		// end floor

		// draw stats
		g.drawString("Level: " + level, position.width + 400, position.height);
		String s = "COMPRESSION: ";
		for (int i = 0; i < maxpillars; i++){
			s +=  pillar[i][COMPRESSION] + " ";
		}

		g.drawString("Weight: " + s,	position.width + 400, position.height + height / 2);
		
		//		g.drawString("Angle: " + (int) Math.toDegrees(floorAngle) + " Degrees",
//				position.width + 400, position.height + height / 2);
	}

	public double getAngle() {
		return floorAngle;
	}

	public boolean upgradePillar(int p) {
		if (pillar[p][PILLARSTRENGTH] < 5) {
			pillar[p][PILLARSTRENGTH]++;
			return true;
		}
		return false;
	}
	
	public int[] getWeight(){
		
		int[] w = new int[maxpillars];
		for (int i = 0; i < maxpillars; i++) {
		w[i] = pillar[i][TOTALWEIGHT];
		}
		return w;
	}
}
