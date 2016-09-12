package uk.fls.main.screens;

import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import fls.engine.main.io.FileIO;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.util.ArtFilter;
import uk.fls.main.util.Pallet;
import uk.fls.main.util.Tile;
import uk.fls.main.util.plugins.FileOperations;
import uk.fls.main.util.plugins.PluginManager;
import uk.fls.main.util.tools.Tool;

public class PaintScreen extends Screen {

	
	private Renderer r;
	private int mx,my;
	private int cx, cy;
	private int sheetSize;
	private int tileSize;
	private Tile[] tiles;
	private Pallet currentPallet;
	private String font;
	private int[][] letters;
	private int[][] borderData;
	private int currentColorIndex;
	private int inputDelay;
	
	private int windowScale;
	private String currentPos;

	private List<Tool> tools;
	private Tool currentTool;
	private SpriteParser sp;
	private int numTools;
	
	private TileViwer tv;
	private SheetViewer sv;
	
	private int state = 1;
	
	public void postInit(){
		this.r = new Renderer(this.game.getImage());
		this.sheetSize = 8;
		this.tileSize = 8;
		
		int nTiles = (this.tileSize);
		this.tiles = new Tile[nTiles * nTiles];
		for(int i = 0; i < this.tiles.length; i++){
			this.tiles[i] = new Tile(nTiles);
		}
		this.currentPallet = Pallet.def;
		this.currentColorIndex = 0;
		this.windowScale = 3;
		this.inputDelay = 10;
		this.font = "SAVELOD-+0123456789><";
		this.letters = new int[this.font.length()][];
		this.currentPos = new File("").getAbsolutePath();
		
		this.sp = new SpriteParser(8, FileIO.instance.readInternalFile("/gui.art"));
		this.tv = new TileViwer(new Point(128,8), this.tiles[0]);
		this.sv = new SheetViewer(tiles, 64);
		initialise();
	}
	
	
	@Override
	public void update() {
		if(this.game.hasFocus()){
			if(this.inputDelay > 0){
				this.inputDelay--;
			}
			
			this.cx = this.input.mouse.getX();
			this.cy = this.input.mouse.getY();
			this.mx = this.cx / windowScale;
			this.my = this.cy / windowScale;
			
			this.tv.update(input, this, mx, my);
			if(this.sv.update(input, this, mx, my)){
				this.tv.setTile(this.sv.getCurrentTile());
			}
			
		
			//This section checks to see if the mouse is over the pallet area
			int paxm = 8;
			int paym = this.game.getHeight() / windowScale - 48 + 8;
			int paxM = paxm + (16 * 8);
			int payM = paym + (Math.max((this.currentPallet.colors.length / 16), 1) * 8);
			
			if(mx > paxm && my > paym && mx < paxM && my < payM){
				int tsX = (mx - paxm) / 8;
				int tsY = (my - paym) / 8;
				
				if(this.input.leftMouseButton.justClicked()){
					int newIndex = tsX + tsY * 16;
					
					if(newIndex > this.currentPallet.colors.length - 1)newIndex = this.currentPallet.colors.length - 1;
					this.currentColorIndex = newIndex;
				}
			}
			
			//This section will check to see if the mouse is over the load and save buttons
			int baxm = 128 + 24 + 8 - 16;
			int baym = 104 + 8;
			int baxM = baxm + 16 * 3;
			int bayM = baym + (8 * 2) * 2;
			
			if(mx > baxm && my > paym && mx < baxM && my < bayM){
				if(my < baym + (8 * 2)){// Save button
					if(this.input.leftMouseButton.justClicked()){
						this.input.relaseMouseButtons();
						save();
					}
				}else{//Load button
					if(this.input.leftMouseButton.justClicked()){
						this.input.relaseMouseButtons();
						load();
					}
				}
			}
		}
		
		//This section checks if the mouse is over the tool panel
		
		int tpxm = 11 * 8;
		int tpym = 2 * 8;
		int tpxM = tpxm + (3 * 8);
		int tpyM = tpym + (Math.max((this.numTools / 3) + 1,1) * 8);
		if(mx > tpxm && my > tpym && mx < tpxM && my < tpyM){
			int tsX = (mx - tpxm) / 8;
			int tsY = (my - tpym) / 8;
			if(this.input.leftMouseButton.justClicked()){
				int newToolIndex = tsX + tsY * 3;
				if(!(newToolIndex >= this.tools.size() || newToolIndex < 0))this.currentTool = this.tools.get(newToolIndex);
			}
		}
		
		//This section checks to see if the mouse is over the minus size button
		
		int msbx = 10 * 8;
		int msby = 11 * 8;
		
		int psbx = 14 * 8;
		int psby = 11 * 8;
		
		int firstSize = this.sheetSize;
		if(this.inputDelay == 0){
			if(mx > msbx && my > msby && mx < msbx + 8 && my < msby + 8){
				if(this.input.leftMouseButton.justClicked()){
					firstSize /= 2;
					if(firstSize < 4)firstSize = 4;
					this.inputDelay = 10;
				}
			}
			
			if(mx > psbx && my > psby && mx < psbx + 8 && my < psby + 8){
				if(this.input.leftMouseButton.justClicked()){
					firstSize *= 2;
					if(firstSize > 64)firstSize = 64;
					this.inputDelay = 10;
				}
			}
		}
		
		if(this.sheetSize != firstSize){
			chageSize(firstSize);
			this.sv.setTileArray(this.tiles);
			this.tv.setTile(this.sv.getCurrentTile());
			this.sheetSize = firstSize;
		}
		
		if(this.input.leftMouseButton.justClicked() && this.inputDelay == 0){
			if(my >= 8*11 && my <= 8*12){
				if(mx >= 8 && mx <= 16){
					this.state--;
					if(this.state < 0)this.state = 0;
					this.inputDelay = 10;
				}else if(mx >= 8 * 23 && mx <= 8 * 24){
					this.state++;
					if(this.state > 2)this.state = 2;
					this.inputDelay = 10;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		this.r.fill(this.r.makeRGB(123, 164, 255));
		

		
		if(this.state == 1){
			drawTools();
			//drawWholeSheet();
	
			renderBorder(1,1,8,8);
			this.sv.render(r);
			
			renderBorder(16,1,8,8);
			this.tv.render(r);
			
			
			renderBorder(1,14,23,4);
			drawPallet();
			drawButtons();
			
			drawCurrentSize();
			drawCursor();
		}else if(this.state == 0){
			
		}
	
		drawArrows();
		
		//this.r.finaliseRender();
	}
	
	private void drawCurrentSize(){
		if(this.sheetSize > 4){
			for(int i = 0; i < 8 * 8; i++){
				int dx = i % 8;
				int dy = i / 8;
				r.setPixel((8 * 10) + dx, dy + (8 * 11), 255 << 16);
			}
			renderString("-", 8 * 10, 8 * 11);
		}
		
		int xo = ((""+this.sheetSize).length() - 1) * 4;
		renderString(""+this.sheetSize,8 * 12 - xo,8 * 11);

		if(this.sheetSize < 64){
			for(int i = 0; i < 8 * 8; i++){
				int dx = i % 8;
				int dy = i / 8;
				r.setPixel((8 * 14) + dx, dy + (8 * 11), 255 << 16);
			}
			renderString("+", 8 * 14, 8 * 11);
		}
	}
	
	private void drawPallet(){
		int s = 8;
		int xoff = 8;
		int yoff = 104 + 8;
		for(int i = 0; i < this.currentPallet.colors.length; i++){
			int c = this.currentPallet.colors[i];
			int tx = i % 16;
			int ty = i / 16;
			for(int j = 0; j < s * s; j++){
				int dx = j % s;
				int dy = j / s;
				this.r.setPixel(xoff + (tx * s) + dx, yoff + (ty * s) + dy, c);
				if(i == this.currentColorIndex){
					if(dx == 0 || dy == 0 || dy == s - 1 || dx == s - 1)this.r.setPixel(xoff + (tx * s) + dx, yoff + (ty * s) + dy, 255 << 16);
				}
			}
		}
	}
	
	private void drawButtons(){
		int bw = 16 * 3;
		int bh = 8 * 2;
		String[] labels = new String[]{"save","load"};
		int xoff = 128 + 24 + 8;
		int yoff = 104 + 8;
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < bw * bh; j++){
				int dx = j % bw;
				int dy = j / bw;
				this.r.setPixel(xoff + dx - 16, yoff + dy + i * bh, (255-(i*123)) << 16);
			}
			renderString(labels[i], xoff - 8, yoff + i * bh + 4);
		}
	}
	
	private void drawArrows(){
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 8 * 8; j++){
				
				int cx = 8 + (j % 8) + (i * 8 * 22);
				int cy = 8 * 11 + (j / 8);
				if(i == 0){
					if(this.state == 0){
						continue;
					}
				}else if(i == 1){
					if(this.state == 2){
						continue;
					}
				}
				this.r.setPixel(cx, cy, this.r.makeRGB(255,0,0));
			}
		}
		
		if(this.state == 1){
			renderString("<", 8, 8 * 11);
			renderString(">", 8 * 23, 8 * 11);
		}else if(this.state == 2){
			renderString("<", 8, 8 * 11);
		}else{
			renderString(">", 8 * 23, 8 * 11);
		}
	}
	
	private void drawTools(){
		renderBorder(11, 2, 3, 6);
		int xo = 11 * 8;
		int yo = 2 * 8;
		for(int i = 0; i < this.tools.size(); i++){
			Tool t = this.tools.get(i);
			if(t == null)continue;
			int tx = i % 3;
			int ty = i / 3;
			for(int j = 0; j < 8 * 8; j++){
				int dx = j % 8;
				int dy = j / 8;
				this.r.setPixel(xo + (tx * 8) + dx, yo + (ty * 8) + dy, t.iconData[j]);
				
				if(this.currentTool == t){// Draws a red box around the currently selected tool
					if(dx == 0 || dy == 0 || dx == 7 || dy == 7){
						this.r.setPixel(xo + (tx * 8) + dx, yo + (ty * 8) + dy, 255 << 16);
					}
				}
			}
		}
	}
	
	private void save(){
		if(this.inputDelay > 0)return;
		JFileChooser jfc = getSaveLoadDialog();
		int out = jfc.showSaveDialog(game);
		
		if(out == JFileChooser.APPROVE_OPTION){
			File file = jfc.getSelectedFile();

			FileOperations fo = PluginManager.instance.getFileOperationsWithExtension(file.getName().substring(file.getName().lastIndexOf(".")));
			
			String output = "";
			output = fo.save(sv);

			boolean noEnding = file.getName().indexOf(".")==-1;
			File newFile = new File((noEnding?file.getAbsolutePath()+".art":file.getAbsolutePath()));
			this.currentPos = newFile.getParent();
			try(FileWriter fw = new FileWriter(newFile)) {
			    fw.write(output);
			    fw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.inputDelay = 30;
	}
	
	private void load(){
		if(this.inputDelay > 0)return;
		JFileChooser jfc = getSaveLoadDialog();
		int out = jfc.showOpenDialog(game);
		
		if(out == JFileChooser.APPROVE_OPTION){
			
			File sel = jfc.getSelectedFile();
			String fileName = sel.getName();
			this.currentPos = sel.getParent();
			if(fileName.indexOf(".") != -1){//Has extension
				String ext = fileName.substring(fileName.indexOf("."));
				ext = ext.trim();
				Tile[] t = PluginManager.instance.getFileOperationsWithExtension(ext).load(FileIO.instance.loadFile(sel.getAbsolutePath()));
				this.sv.setTileArray(t);
				this.tv.setTile(this.sv.getCurrentTile());
				this.sheetSize = PluginManager.instance.getFileOperationsWithExtension(ext).getLoadedSpriteSize();
			}else{
				SpriteParser newFile = new SpriteParser(FileIO.instance.loadFile(sel.getAbsolutePath()));
				Tile[] newTiles = new Tile[newFile.getNumCells()];
				for(int i = 0; i < newFile.getNumCells(); i++){
					Tile newTile = new Tile(newFile.getCellWidth());
					newTile.setData(newFile.getData(i));
					newTiles[i] = newTile;
				}
				
				this.sv.setTileArray(newTiles);
				this.tv.setTile(this.sv.getCurrentTile());
				this.sheetSize = newFile.getCellWidth();
			}
		}
		this.inputDelay = 30;
	}
	
	public void renderString(String msg, int x,int y){
		msg = msg.toUpperCase();
		for(int i = 0; i < msg.length(); i++){
				int p = this.font.indexOf(msg.charAt(i));
				if(p == -1)continue;
				for(int j = 0; j < 8 * 8; j++){
					int dx = j % 8;
					int dy = j / 8;
					this.r.setPixel(x + (i * 8) + dx, y + dy, this.letters[p][j]);
				}
		}
	}
	
	public void renderBorder(int x, int y, int w, int h){
		x *= 8;
		y *= 8;
		for(int xx = -1; xx <= w; xx++){
			for(int yy = -1; yy <= h; yy++){
				
				int tx = 1;
				boolean xflip = false;
				boolean yflip = false;
				
				if(xx == -1)tx = 0;
				if(yy == -1)tx = 2;
				if(xx == w){
					tx = 0;
					
					if(yy != -1 && yy != h)xflip = true;
				}
				if(yy == h){
					tx = 2;
					yflip = true;
				}
				if(xx == -1 && yy == -1){
					tx = 3;
				}
				if(xx == -1 && yy == h){
					tx = 3;
					yflip = true;
				}
				if(xx == w && yy == -1){
					tx = 4;
				}
				if(xx == w && yy == h){
					tx = 4;
				}
				
				
				int[] data = this.borderData[tx];
				
				for(int i = 0; i < 8 * 8; i++){
					int dx = i % 8;
					int dy = i / 8;
					
					if(tx != 1){
						if(xflip)dx = 7 - dx;
						if(yflip)dy = 7 - dy;
						this.r.setPixel(x + (xx * 8) + dx, y + (yy * 8) + dy, data[i]);
					}else{
						this.r.setPixel(x + (xx * 8) + dx, y + (yy * 8) + dy, (this.r.makeRGB(123, 164, 255) & 0xFEFEFE) >> 1);
					}
				}
			}
		}
	}
	
	private void initChars(){
		try{
		int off = 5;
		for(int i = 0; i < this.letters.length; i++){
			if(i > 6)off = 35;
			int tx = (off + i) % 8;
			int ty = (off + i) / 8;
			this.letters[i] = this.sp.getData(tx, ty);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initBorder(){
		this.borderData = new int[5][];
		int off = 0;
		for(int i = 0; i < this.borderData.length; i++){
			int tx = (off + i) % 8;
			int ty = (off + i) / 8;
			this.borderData[i] = this.sp.getData(tx, ty);
		}
	}
	
	private void initCursor(){
		this.currentTool = this.tools.get(0);
	}
	
	private void drawCursor(){
		for(int i = 0; i < 8 * 8; i++){
			int dx = i % 8;
			int dy = i / 8;
			this.r.setPixel(this.mx + dx - 2, this.my + dy - 6, this.currentTool.iconData[i]);
		}
	}
	
	public void setColorIndex(int col){
		this.currentColorIndex = col;
	}
	
	private void initialise(){
		pluginInit();
		initChars();
		initBorder();
		initCursor();
	}
	
	private void chageSize(int nSize){
		int[] wholeSheet = this.sv.getWholeSheet();
		int numTiles = this.sv.w / nSize; // New tile size
		int tileDrawSize = this.sv.w / numTiles; // The number of new tiles we want
		Tile[] newTiles = new Tile[numTiles * numTiles];
		for(int i = 0; i < newTiles.length; i++){
			Tile newTile = new Tile(nSize);
			int cx = i % numTiles;
			int cy = i / numTiles;
			
			int nw = newTile.getWidth();
			int[] d = new int[nw * nw];
			for(int j = 0; j < d.length; j++){
				int tx = j % nw;
				int ty = j / nw;
				
				int wsx = (cx * tileDrawSize) + tx;
				int wsy = (cy * tileDrawSize) + ty;
				d[tx + ty * nw] = wholeSheet[wsx + wsy * this.sv.w];
			}
			newTile.setData(d);
			newTiles[i] = newTile;
		}
		this.sv.setCurrentTile(0);
		this.tiles = newTiles;
	}
	
	private void pluginInit(){
		PluginManager.instance.reload();
		this.tools = new ArrayList<Tool>();
		PluginManager.instance.loadTools(this.tools, this);
		this.currentTool = this.tools.get(0);
		this.numTools = this.tools.size();
		PluginManager.instance.loadFileManagers();
	}
	
	public Tile getCurrentTile(){
		return this.sv.getCurrentTile();
	}
	
	public Pallet getPallet(){
		return this.currentPallet;
	}
	
	public int getCurrentColor(){
		return getPallet().colors[this.currentColorIndex];
	}
	
	public Tool getCurrentTool(){
		return this.currentTool;
	}
	
	private JFileChooser getSaveLoadDialog(){
		JFileChooser jfc = new JFileChooser(this.currentPos);
		jfc.setFileHidingEnabled(true);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileHidingEnabled(true);
		for(int i = 0; i < PluginManager.instance.getFileManagerList().length; i++){
			jfc.addChoosableFileFilter(new ArtFilter(PluginManager.instance.getFileManagerList()[i], PluginManager.instance.getFileManagerDescList()[i]));
		}
		return jfc;
	}

}
