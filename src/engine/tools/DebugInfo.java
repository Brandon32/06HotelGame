package engine.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.GameDisplay;

public class DebugInfo {

	private static Font f1;
	private static boolean debug = true; // switch to false on release
	private static LinkedList<String> str;
	private static Dimension displayBounds;
	
	static{
	f1 = new Font("Times New Roman", Font.PLAIN, (int) 12);
	str = new LinkedList<String>();
	displayBounds = GameDisplay.getBounds();
	}
	
	public static void display(Graphics2D g) {
		if (debug == true) {
			g.setFont(f1);
			g.setColor(Color.RED);
			for(int i = 0; i < str.size(); i++){
				g.drawString(str.get(i), displayBounds.width - 200, (i + 1) * 10);
			}
			str.clear();
		}
	}

	public static void debug(String st) {
		str.addLast(st);
	}
}
