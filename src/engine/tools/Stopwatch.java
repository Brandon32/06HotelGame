package engine.tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import engine.GameEngine;

public class Stopwatch {

	static float startTime = 0;
	static float nextOutputTime = 5;
	static Map<String, ProfilerRecording> recordings;
	static DecimalFormat df, fd;
	
	static{
		df = new DecimalFormat("0.00000");
		fd = new DecimalFormat("0000.0");
		recordings = new HashMap<String, ProfilerRecording>();// =
	}
																				// new
	static String displayText;

	public static void Awake() {
		
		startTime = System.nanoTime();
		displayText = "\n\nTaking initial readings...";
	}

	public static void OnGUI() {
		System.out.println("Code Profiler");
		System.out.println(displayText);
	}

	public static void Begin(String id) {

		// create a new recording if not present in the list
		if (recordings.get(id) == null) {
			recordings.put(id, new ProfilerRecording(id));
		}

		recordings.get(id).Start();
	}

	public static void End(String id) {
		recordings.get(id).Stop();
	}

	public static void Display() {

		// the overall frame time and frames per second:
		displayText = "\n\n";
		float totalMS = ((System.nanoTime() - startTime)) / 1000000;
		float avgMS = (totalMS / GameEngine.getFrames());
		float fps = (GameEngine.getFrames()/(totalMS /1000 ));
		displayText += "Avg frame time: ";
		displayText += avgMS + "ms, ";
		displayText += fps + " fps \n";

		// the column titles for the individual recordings:
		displayText += "Total";
		displayText += "\t\tMS/frame";
		displayText += "\tCalls/frame";
		displayText += "\tMS/call";
		displayText += "\t\tLabel";
		displayText += "\n";

		// now we loop through each individual recording
		for (ProfilerRecording entry : recordings.values()) {
			// Each "entry" is a key-value pair where the string ID
			// is the key, and the recording instance is the value:
			// ProfilerRecording recording = entry();

			// calculate the statistics for this recording:
			float recordedMS = (entry.Seconds() / 1000000);
			float percent = (recordedMS * 100) / totalMS;
			float msPerFrame = recordedMS / GameEngine.getFrames();
			float msPerCall = recordedMS / entry.Count();
			float timesPerFrame = entry.Count()	/ (float) GameEngine.getFrames();

			// add the stats to the display text
			displayText += (df.format(percent) + "%\t");
			displayText += (fd.format(msPerFrame) + " ms\t");
			displayText += (df.format(timesPerFrame) + "\t\t");
			displayText += (fd.format(msPerCall) + " ms\t");
			displayText += (entry.id + "\t");
			displayText += "\n";

			// and reset the recording
			entry.Reset();
		}
		DebugInfo.debugLong(displayText);
		//System.out.println(displayText);
		startTime = System.nanoTime();
	}
}
