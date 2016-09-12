package uk.fls.main.screens;

import fls.engine.main.input.Input;
import fls.engine.main.util.Renderer;
import uk.fls.main.util.Tile;

public class SheetViewer {

	
	public Tile[] tiles;
	public int w;
	public int numTiles;
	private int tileDrawSize;
	private int currentTile;
	

	public SheetViewer(Tile[] t, int w) {
		this.w = w;
		setTileArray(t);
		this.currentTile = 0;
	}
	
	
	public void render(Renderer r){
		for(int i = 0; i < this.tiles.length; i++){
			int cx = i % this.numTiles;
			int cy = i / this.numTiles;
			drawTile(r, this.tiles[i], cx * this.tileDrawSize + 8, cy * this.tileDrawSize + 8, i == this.currentTile);
		}
	}
	
	private void drawTile(Renderer r, Tile t, int x, int y, boolean current){
		int[] d = t.getData();
		for(int i = 0; i < d.length; i++){
			int dx = i % this.tileDrawSize;
			int dy = i / this.tileDrawSize;
			r.setPixel(x + dx, y + dy, d[i]);
			
			if(current){
				if(dx == 0 || dy == 0 || dx == this.tileDrawSize-1 || dy == this.tileDrawSize-1)r.setPixel(x + dx, y + dy, 255 << 16);
			}
		}
	}
	
	public boolean update(Input in,PaintScreen ps, int mx, int my){
		int tsxm = 8;
		int tsym = 8;
		int tsxM = tsxm + 64;
		int tsyM = tsym + 64;
		
		if(mx > tsym && my > tsym && mx < tsxM && my < tsyM){
			int tsX = (mx - tsxm) / this.tileDrawSize;
			int tsY = (my - tsym) / this.tileDrawSize;
			
			if(in.leftMouseButton.justClicked()){
				int cTile = tsX + tsY * this.numTiles;
				setCurrentTile(cTile);
				return true;
			}
		}
		
		return false;
	}
	
	public void setTileArray(Tile[] t){
		this.tiles = t;
		this.numTiles = this.w / t[0].getWidth();
		this.tileDrawSize = this.w / this.numTiles;
	}
	
	public void setCurrentTile(int i){
		this.currentTile = i;
		if(this.currentTile > this.tiles.length) i = this.tiles.length-1;
		if(this.currentTile < 0)this.currentTile = 0;
	}
	
	public Tile getCurrentTile(){
		return this.tiles[this.currentTile];
	}
	
	public int getCurrentTileIndex(){
		return this.currentTile;
	}
	
	public int getTileSize(){
		return this.tileDrawSize;
	}
	
	public int getTotalNumberOfTiles(){
		return this.tiles.length;
	}
	
	public int[] getWholeSheet(){
		int[] res = new int[this.w * this.w];
		for(int i = 0; i < this.tiles.length; i++){
			int cx = i % this.numTiles;
			int cy = i / this.numTiles;
			int[] d = this.tiles[i].getData();
			int tileWidth = this.tiles[i].getWidth();
			for(int j = 0; j < d.length; j++){
				int tx = j % tileWidth;
				int ty = j / tileWidth;
				
				int rx = (cx * this.tileDrawSize) + tx;
				int ry = (cy * this.tileDrawSize) + ty;
				res[rx + ry * this.w] = d[j];
			}
		}
		return res;
	}
}
