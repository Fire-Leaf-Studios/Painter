package uk.fls.main.util.plugins;

import java.util.ArrayList;
import java.util.List;

import fls.engine.main.io.FileIO;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.Painter;
import uk.fls.main.util.tools.Tool;

public abstract class Plugin {
	
	private PluginResource resources;
	private List<Tool> tools;
	
	public MetaData getInfo(){
		return this.resources.getInfo();
	}
	
	public PluginResource getResources(){
		return this.resources;
	}
	
	public void setResources(PluginResource md){
		this.resources = md;
	}
	
	protected void addTool(Tool t){
		if(this.tools == null)this.tools = new ArrayList<Tool>();
		this.tools.add(t);
	}
	
	public int[] getSpriteData(int x,int y){
		if(this.resources.getIconData() == null)return new int[8 * 8];
		return this.resources.getIconData() .getData(x, y);
	}
	
	public List<Tool> getTools(){
		return this.tools;
	}
}
