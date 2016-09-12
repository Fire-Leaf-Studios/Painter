package uk.fls.main.util.plugins;

import uk.fls.main.screens.SheetViewer;
import uk.fls.main.util.Tile;

public abstract class FileOperations {
	
	
	private String ext;
	private int spriteSize;
	private Plugin p;
	
	
	public FileOperations(String ext){
		this.ext = ext;
		this.spriteSize = 8;
	}
	
	public String getExt(){
		return this.ext;
	}

	protected void setSpriteSize(int s){
		this.spriteSize = s;
	}
	
	public int getLoadedSpriteSize() {
		return this.spriteSize;
	}
	
	public void setPlugin(Plugin p){
		this.p = p;
	}
	
	public Plugin getPlugin(){
		return this.p;
	}
	
	public abstract Tile[] load(String data);
	
	public abstract String save(SheetViewer tiles);
	
	public abstract String getDesc();
}
