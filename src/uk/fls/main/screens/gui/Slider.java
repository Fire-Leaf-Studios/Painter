package uk.fls.main.screens.gui;

import fls.engine.main.util.Renderer;

public class Slider {

	
	private int l = 80;
	private int h = 8;
	
	private int x = 0;
	private int y = 0;
	
	private float perc = 0.5f;
	private int value = 255/2;
	
	public int sliderColor = 0;
	
	
	public void render(Renderer r, int x, int y){
		for(int xx = 0; xx < this.l; xx++){
			for(int yy = 0; yy < this.h; yy++){
				r.setPixel(x + xx, y + yy, 0);
			}
		}
		
		for(int xo = -3; xo < 3; xo ++){
			for(int i = 0; i < this.h; i++){
				int dx = (int)(this.l * this.perc) + xo;
				if(dx < 0 || dx > this.l)continue;
				r.setPixel(x + dx, y + i, this.sliderColor);
			}
		}
		
		this.x = x;
		this.y = y;
	}
	
	public void update(boolean c, int mx, int my){
		if(c){
			if(mx >= x && mx <= x + this.l){
				if(my >= y && my <= y + this.h){
					float v = (float)(mx - this.x) / (float)(this.l);
					setPerc(v);
				}
			}
		}
	}
	
	public int getValue(){
		return this.value;
	}
	
	private void setPerc(float f){
		this.value = (int)(255f * f);
		this.perc = f;
	}
}
