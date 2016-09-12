package uk.fls.main.util.plugins;

import java.util.ArrayList;
import java.util.List;

import uk.fls.main.util.tools.Tool;

public abstract class Plugin {
	
	private PluginResource resources;
	private List<Tool> tools;
	private List<FileOperations> fileOps;
	
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
		t.setPlugin(this);
		this.tools.add(t);
	}
	
	protected void addFileOperationsManager(FileOperations fo){
		if(this.fileOps == null)this.fileOps = new ArrayList<FileOperations>();
		fo.setPlugin(this);
		this.fileOps.add(fo);
	}
	
	public int[] getSpriteData(int x,int y){
		if(this.resources.getIconData() == null)return new int[8 * 8];
		return this.resources.getIconData() .getData(x, y);
	}
	
	public List<Tool> getTools(){
		return this.tools;
	}
	
	public List<FileOperations> getFileOps(){
		return this.fileOps;
	}
	
	public void log(String out){
		System.out.println("[" + getTitle() + "] " + out);
	}
	
	public void logf(String s, Object...a){
		System.out.printf("[" + getTitle() + "] " + s + "\n", a);
	}
	
	private String getTitle(){
		return this.resources.getInfo().getName() + " : " + this.resources.getInfo().getPluginVersion().asString();
	}
}
