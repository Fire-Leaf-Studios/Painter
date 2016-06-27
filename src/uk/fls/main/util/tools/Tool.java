package uk.fls.main.util.tools;

import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.screens.PaintScreen;
import uk.fls.main.util.plugins.Plugin;

public abstract class Tool {

	public int[] iconData;
	protected SpriteParser spriteSelector;
	protected boolean isFromPlugin = false;
	protected Plugin plugin;
	
	
	public abstract void init();
	
	// X & Y refernce to the current drawing
	public abstract void onClick(PaintScreen ps, int x, int y, int currentColor, int colorClickOn);

	public void setSpriteHolder(SpriteParser sp){
		this.spriteSelector = sp;
	}
	
	protected void setIconData(int x,int y){
		if(this.spriteSelector != null)this.iconData = this.spriteSelector.getData(x, y);
	}
	
	public void setPlugin(Plugin p){
		this.plugin = p;
		this.isFromPlugin = true;
	}
	
	
}
