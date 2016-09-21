package uk.fls.main.screens;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import uk.fls.main.util.Tile;

public class TileViwer {

	
	private Tile currentTile;
	private int w;
	private int scale;
	private Point pos;
	
	public TileViwer(Point pos, Tile t){
		this.pos = pos;
		this.currentTile = t;
		this.w = 64;
		this.scale = this.w / t.getWidth();
	}
	
	public void render(Renderer r){
		int[] d = currentTile.getData();
		
		if(64 / scale <= 16){
			drawLines(r);
		}
		
		for(int i = 0; i < d.length; i++){
			int cx = i % currentTile.getWidth();
			int cy = i / currentTile.getWidth();
			drawLargePixel(r, cx, cy, d[i]);
		}
	}
	
	private void drawLargePixel(Renderer r, int x, int y, int c){
		if(c == -1)return;
		int cc = c == -1?(r.makeRGB(123, 164, 255) & 0xFEFEFE) >> 1:c;
		for(int i = 0; i < this.scale * this.scale; i++){
			int px = i % this.scale;
			int py = i / this.scale;
			r.setPixel(this.pos.getIX() + x * this.scale + px, this.pos.getIY() + y * this.scale + py, cc);
		}
	}
	
	private void drawLines(Renderer r){
		
	}
	
	public void update(Input in,PaintScreen ps, int mx, int my){
		int drxM = this.pos.getIX() + (this.w);
		int dryM = this.pos.getIY() + (this.w);
		if(mx > this.pos.getIX() && my > this.pos.getIY() && mx < drxM && my < dryM){
			int tsX = (mx - this.pos.getIX()) / this.scale;
			int tsY = (my - this.pos.getIY()) / this.scale;
			
			if(in.leftMouseButton.justClicked()){
				ps.getCurrentTool().onClick(this.currentTile, tsX, tsY, ps.getCurrentColor(), this.currentTile.getData()[tsX + tsY * this.currentTile.getWidth()]);
			} else if(in.rightMouseButton.justClicked()){
				ps.getCurrentTool().onRightClick(this.currentTile, tsX, tsY, ps.getCurrentColor(), this.currentTile.getData()[tsX + tsY * this.currentTile.getWidth()]);
			}
		}
	}
	
	public void setTile(Tile t){
		this.currentTile = t;
		this.scale = this.w / t.getWidth();
	}
}
