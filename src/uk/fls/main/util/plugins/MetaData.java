package uk.fls.main.util.plugins;

import java.util.HashMap;

import fls.engine.main.io.FileIO;

public class MetaData {

	private String fileData;
	private boolean loaded;
	private HashMap<String, String> values;

	public MetaData(String d) {
		this.fileData = d;
		this.values = new HashMap<String, String>();
		init();
		this.loaded = true;
	}

	private void init() {
		String[] lines = this.fileData.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String[] splits = lines[i].trim().split(":", 2);
			this.values.put(splits[0].trim(), splits[1].trim());
		}
	}

	public String getAuthor() {
		return this.values.get("author");
	}

	public Version getMinVersion() {
		return Version.parse(this.values.get("max-version"));
	}

	public Version getMaxVersion() {
		return Version.parse(this.values.get("min-version"));
	}

	public Version getPluginVersion() {
		return Version.parse(this.values.get("version"));
	}

	public String getUpdated() {
		return this.values.get("updated");
	}

	public String getDescription() {
		return this.values.get("desc");
	}
	
	public String getMain(){
		return this.values.get("main");
	}
	
	public String getSprites(){
		return this.values.get("sprites");
	}
	
	public String getName(){
		return this.values.get("name");
	}

	public boolean loaded() {
		return this.loaded;
	}
}
