package uk.fls.main.util.plugins;

import fls.engine.main.util.rendertools.SpriteParser;

public class PluginResource {

	
	private MetaData infoFile;
	private SpriteParser iconData;
	
	public void setInfo(MetaData m){
		this.infoFile = m;
	}
	
	public void setIconData(String n){
		this.iconData = new SpriteParser(8, n);
	}
	
	public MetaData getInfo(){
		return this.infoFile;
	}
	
	public SpriteParser getIconData(){
		return this.iconData;
	}
}
