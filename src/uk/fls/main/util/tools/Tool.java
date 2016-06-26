package uk.fls.main.util.tools;

import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.screens.PaintScreen;

public abstract class Tool {

	public int[] iconData;
	protected SpriteParser spriteSelector;
	protected boolean isFromPlugin = false;
	
	// X & Y refernce to the current drawing
	
	public abstract void init();
	public abstract void onClick(PaintScreen ps, int x, int y, int currentColor, int colorClickOn);
	
	public void setFromPlugin(){
		this.isFromPlugin = true;
	}

	public void setSpriteHolder(SpriteParser sp){
		this.spriteSelector = sp;
	}
	
	protected void setIconData(int x,int y){
		if(this.spriteSelector != null)this.iconData = this.spriteSelector.getData(x, y);
	}
}
