package engine.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.GameDisplay;

public class DebugInfo {

	private static Font f1;
	private static boolean debug = false;
	private static LinkedList<String> shortMsg;
	private static LinkedList<String> longMsg;
	private static Dimension displayBounds;

	static {
		f1 = new Font("Times New Roman", Font.PLAIN, (int) 12);
		shortMsg = new LinkedList<String>();
		longMsg = new LinkedList<String>();
		displayBounds = GameDisplay.getBounds();
	}

	public static void display(Graphics2D g) {
		g.setFont(f1);
		g.setColor(Color.RED);
		for (int i = 0; i < shortMsg.size(); i++) {
			g.drawString(shortMsg.get(i), displayBounds.width - 200,
					(i + 1) * 10);
		}
		shortMsg.clear();
		while (longMsg.size() > 15) {
			longMsg.remove();
		}
		for (int a = 0; a < longMsg.size(); a++) {
			g.drawString(longMsg.get(a), displayBounds.width - 300,
					displayBounds.height - ((longMsg.size() - a + 1) * 10));
		}
	}

	public static void debugShort(String st) {
		if (debug == true) {
			for (String s : st.split("\n")) {
				shortMsg.addLast(s);
			}
		}
	}

	public static void debugLong(String st) {
		if (debug == true) {
			for (String s : st.split("\n")) {
				longMsg.addLast(s);
			}
		}
	}

	public static void debugLog(String st) {
		if (debug == true) {
			System.out.println(st);
		}
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		DebugInfo.debug = debug;
	}
}
