package com.gilo.soko.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class Logger {
	static File file;

	public Logger() {

	}

	public static void d(String data) {
		try {
			File root = new File(Environment.getExternalStorageDirectory(),
					"debug Logs");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, "log");
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(data);
			writer.flush();
			writer.close();
			// Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void e(String data) {
		try {
			File root = new File(Environment.getExternalStorageDirectory(),
					"Error Logs");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, "log");
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(data);
			writer.flush();
			writer.close();
			// Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(String fname, String data) {
		try {
			File root = new File(Environment.getExternalStorageDirectory(),
					"Logs");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, fname);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(data);
			writer.flush();
			writer.close();
			// Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
