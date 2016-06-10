package uk.fls.main.screens;

import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;

import fls.engine.main.io.FileIO;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Renderer;
import uk.fls.main.util.CompressionManager;
import uk.fls.main.util.Pallet;
import uk.fls.main.util.SpriteParser;
import uk.fls.main.util.Tile;
import uk.fls.main.util.tools.Fill;
import uk.fls.main.util.tools.Pencil;
import uk.fls.main.util.tools.Pipette;
import uk.fls.main.util.tools.Tool;

public class PaintScreen extends Screen {

	
	private Renderer r;
	private int mx,my;
	private int cx, cy;
	private int sheetSize = 8;
	public int cTile;
	public Tile[] tiles;
	public Pallet currentPallet;
	private String font;
	private int[][] letters;
	private int[][] borderData;
	private int[] cursorData;
	public int currentColorIndex;
	private int inputDelay;
	
	private int windowScale;
	private String documentTitle;
	private String currentPos;
	
	private int currentVersion;

	private Tool[] tools;
	private Tool currentTool;
	private SpriteParser sp;
	private int numTools;
	
	public void postInit(){
		this.r = new Renderer(this.game.getImage());
		this.tiles = new Tile[this.sheetSize * this.sheetSize];
		for(int i = 0; i < this.tiles.length; i++){
			this.tiles[i] = new Tile(sheetSize);
		}
		this.cTile = 0;
		this.currentPallet = Pallet.def;
		this.currentColorIndex = 0;
		this.windowScale = 3;
		this.inputDelay = 10;
		this.font = "SAVELOD";
		this.letters = new int[this.font.length()][];
		this.documentTitle = "Untitled";
		this.currentVersion = 2;
		this.currentPos = new File("").getAbsolutePath();
		
		this.sp = new SpriteParser(8, FileIO.instance.readInternalFile("/gui.art"));
		initTools();
		initChars();
		initBorder();
		initCursor();
	}
	
	
	@Override
	public void update() {
		if(this.game.hasFocus()){
			if(this.inputDelay > 0){
				this.inputDelay--;
				return;
			}
			
			this.cx = this.input.mouse.getX();
			this.cy = this.input.mouse.getY();
			this.mx = this.cx / windowScale;
			this.my = this.cy / windowScale;
			
			
			// This checks to see if the mouse if within the tile select area
			int tsxm = 8;
			int tsym = 8;
			int tsxM = tsxm + (this.sheetSize * this.sheetSize);
			int tsyM = tsym + (this.sheetSize * this.sheetSize);
			
			if(mx > tsym && my > tsym && mx < tsxM && my < tsyM){
				int tsX = (mx - tsxm) / this.sheetSize;
				int tsY = (my - tsym) / this.sheetSize;
				
				if(this.input.leftMouseButton.justClicked()){
					this.cTile = tsX + tsY * this.sheetSize;
				}
			}
			
			//This checks to see if the mouse if over the draw area
			int drxm = 128;
			int drym = 8;
			
			int drxM = drxm + (this.sheetSize * this.sheetSize);
			int dryM = drym + (this.sheetSize * this.sheetSize);
			
			if(mx > drxm && my > drym && mx < drxM && my < dryM){
				int tsX = (mx - drxm) / this.sheetSize;
				int tsY = (my - drym) / this.sheetSize;
				
				if(this.input.leftMouseButton.justClicked()){
					//this.tiles[cTile].setData(tsX, tsY, this.currentPallet.colors[this.currentColorIndex]);
					this.currentTool.onClick(this, tsX, tsY, this.currentPallet.colors[this.currentColorIndex], this.tiles[this.cTile].getData()[tsX + tsY * 8]);
					//this.inputDelay = 5;
				}
				
				if(this.input.rightMouseButton.justClicked()){// Perminent eraser
					this.tiles[cTile].setData(tsX, tsY, -1);
					//this.inputDelay = 20;
				}
			}
			
		
			//This section checks to see if the mouse is over the pallet area
			int paxm = 8;
			int paym = this.game.getHeight() / windowScale - 48 + 8;
			int paxM = paxm + (16 * 8);
			int payM = paym + (Math.max((this.currentPallet.colors.length / 16), 1) * 8);
			
			if(mx > paxm && my > paym && mx < paxM && my < payM){
				int tsX = (mx - paxm) / this.sheetSize;
				int tsY = (my - paym) / this.sheetSize;
				
				if(this.input.leftMouseButton.justClicked()){
					int newIndex = tsX + tsY * 16;
					
					if(newIndex > this.currentPallet.colors.length - 1)newIndex = this.currentPallet.colors.length - 1;
					this.currentColorIndex = newIndex;
				}
			}
			
			//this.r.setPixel(paxM, payM+1, 255 << 16);
			
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
		int tpyM = tpym + (Math.max((this.numTools / 3),1) * 8);
		if(mx > tpxm && my > tpym && mx < tpxM && my < tpyM){
			int tsX = (mx - tpxm) / this.sheetSize;
			int tsY = (my - tpym) / this.sheetSize;
			if(this.input.leftMouseButton.justClicked()){
				int newToolIndex = tsX + tsY * 3;
				if(newToolIndex > this.tools.length || newToolIndex < 0)newToolIndex = 0;
				this.currentTool = this.tools[newToolIndex];
			}
		}
	}

	@Override
	public void render(Graphics g) {
		this.r.fill(this.r.makeRGB(123, 164, 255));
		

		drawTools();
		drawWholeSheet();
		drawCurrent();
		renderBorder(1,14,23,4);
		drawPallet();
		drawButtons();
		
		//renderString(this.documentTitle,8,64 + 8 * 3);
		
		drawCursor();
		
		this.r.finaliseRender();
	}
	
	public void drawWholeSheet(){
		int xoff = 8;
		int yoff = 8;
		
		renderBorder(1,1,8,8);
		
		for(int i = 0; i < this.tiles.length; i++){
			Tile t = this.tiles[i];
			int tx = i % this.sheetSize;
			int ty = i / this.sheetSize;
			int[] cData = t.getData();
			for(int j = 0; j < this.sheetSize * this.sheetSize; j++){
				int dx = j % this.sheetSize;
				int dy = j / this.sheetSize;
				
				int fx = xoff + (tx * 8) + dx;
				int fy = yoff + (ty * 8) + dy;
				
				int cc = cData[j] == -1?(this.r.makeRGB(123, 164, 255) & 0xFEFEFE) >> 1:cData[j];
				if(this.cTile == i){
					if(dx == 0 || dy == 0 || dy == this.sheetSize-1|| dx == this.sheetSize-1)this.r.setPixel(fx, fy, this.r.makeRGB(255,0,0));
					else this.r.setPixel(fx, fy, cc);
				}else{
					//if(dx == 0 || dy == 0 || dy == this.sheetSize || dx == this.sheetSize)this.r.setPixel(fx, fy, this.r.makeRGB(64,64,64));
					this.r.setPixel(fx, fy, cc);
				}
			}
		}
	}
	
	public void drawCurrent(){
		int s = 8;
		int xOff = 128;
		int yOff = 8;
		
		int gC = this.r.makeRGB(64, 64, 64);
		int gC2 = this.r.makeRGB(123, 123, 123);
		
		renderBorder(16,1,8,8);
		
		int square = this.sheetSize * this.sheetSize;
		Tile c = this.tiles[this.cTile];
		int[] data = c.getData();
		for(int i = 0; i < square; i++){
			int tx = i % this.sheetSize;
			int ty = i / this.sheetSize;
			int cc = data[i] == -1?(this.r.makeRGB(123, 164, 255) & 0xFEFEFE) >> 1:data[i];
			for(int j = 0; j < square; j++){
				int dx = j % this.sheetSize;
				int dy = j / this.sheetSize;
				this.r.setPixel(xOff + (tx * s) + dx, yOff + (ty * s) + dy, cc);
			}
		}
	}
	
	public void drawPallet(){
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
	
	public void drawButtons(){
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
	
	public void drawTools(){
		renderBorder(11, 2, 3, 6);
		
		int xo = 11 * 8;
		int yo = 2 * 8;
		for(int i = 0; i < this.tools.length; i++){
			Tool t = this.tools[i];
			if(t == null)continue;
			int tx = i % 3;
			int ty = i / 3;
			for(int j = 0; j < 8 * 8; j++){
				int dx = j % 8;
				int dy = j / 8;
				this.r.setPixel(xo + (tx * 8) + dx, yo + (ty * 8) + dy, t.data[j]);
				
				if(this.currentTool == t){
					if(dx == 0 || dy == 0 || dx == 7 || dy == 7){
						this.r.setPixel(xo + (tx * 8) + dx, yo + (ty * 8) + dy, 255 << 16);
					}
				}
			}
		}
	}
	
	public void save(){
		if(this.inputDelay > 0)return;
		JFileChooser jfc = new JFileChooser(this.currentPos);
		int out = jfc.showSaveDialog(game);
		
		if(out == JFileChooser.APPROVE_OPTION){
			
			
			
			File file = jfc.getSelectedFile();
			String output = "";
			output += "?Version: " + this.currentVersion + "\n";		
			
			for(int i = 0; i < this.tiles.length; i++){
				String thisTile = "#";
				int[] data = this.tiles[i].getData();
				int amt = 0;	
				for(int j = 0; j < data.length; j++){
					int c = data[j];
					thisTile += (c==-1?"-1":Integer.toHexString(c))+",";
				}
				thisTile = thisTile.trim();
				output += thisTile + "\n";
			}

			boolean noEnding = file.getName().indexOf(".")==-1;
			this.documentTitle = file.getName().indexOf(".")==-1?file.getName():file.getName().substring(0,file.getName().indexOf("."));
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
	
	public void load(){
		if(this.inputDelay > 0)return;
		JFileChooser jfc = new JFileChooser(this.currentPos);
		int out = jfc.showOpenDialog(game);
		
		if(out == JFileChooser.APPROVE_OPTION){
			String fileName = jfc.getSelectedFile().getName();
			this.currentPos = jfc.getSelectedFile().getParent();
			if(fileName.indexOf(".") != -1){//Has extension
				String ext = fileName.substring(fileName.indexOf(".") + 1);
				ext = ext.trim();
				if(!ext.equals("art")){
					System.err.println("Not a useable file type!!");
					return;
				}
				fileName = fileName.substring(0, fileName.indexOf("."));
				this.documentTitle = fileName;
			}
			String[] lines = FileIO.instance.loadFile(jfc.getSelectedFile().getAbsolutePath()).split("\n");
			
			boolean usingHex = false;
			boolean usingCompresion = false;
			
			for(int i = 0; i < lines.length; i++){//Looking for meta data
				String l = lines[i].trim();
				if(l.trim().startsWith("?")){//Found data
					String data = l.substring(1);
					String name = data.substring(0, data.indexOf(":")).trim();
					String value = data.substring(data.indexOf(":") + 1).trim();
					System.out.println(name);
					if(name.equals("Version")){
						int ver = Integer.valueOf(value);
						if(ver >= 2){
							usingHex = true;
						}
						
						if(ver >= 3){
							usingCompresion = true;
						}
					}
				}
			}
			
			int cell = 0;
			for(int i = 0; i < lines.length; i++){
				String line = lines[i];
				if(line.startsWith("#")){
					String[] data = line.substring(1).split(",");
					int[] frame = new int[this.sheetSize * this.sheetSize];
					for(int j = 0; j < data.length; j++){
						String c = data[j].trim();
						if(!usingHex){// Not HEX values
							frame[j] = Integer.parseInt(c);
						}else{
							frame[j] = CompressionManager.decompress(c);
						}
						

						int dx = j % this.sheetSize;
						int dy = j / this.sheetSize;
						this.tiles[cell].setData(dx, dy, frame[j]);
					}
					cell++;
				}
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
	
	public void initChars(){
		int off = 6;
		for(int i = 0; i < this.letters.length; i++){
			int tx = (off + i) % 8;
			int ty = (off + i) / 8;
			this.letters[i] = this.sp.getData(tx, ty);
		}
	}
	
	public void initBorder(){
		this.borderData = new int[5][];
		int off = 1;
		for(int i = 0; i < this.borderData.length; i++){
			int tx = (off + i) % 8;
			int ty = (off + i) / 8;
			this.borderData[i] = this.sp.getData(tx, ty);
		}
	}
	
	public void initCursor(){
		this.cursorData = this.sp.getData(1, 2);
	}
	
	public void initTools(){
		this.tools = new Tool[12];
		this.tools[0] = new Pencil(this.sp);
		this.tools[1] = new Fill(this.sp);
		this.tools[2] = new Pipette(this.sp);
		
		this.currentTool = this.tools[0];
		
		int amt = 0;
		for(int i = 0; i < this.tools.length; i++){
			if(this.tools[i] != null)amt++;
		}
		this.numTools = amt;
	}
	
	public void drawCursor(){
		for(int i = 0; i < 8 * 8; i++){
			int dx = i % 8;
			int dy = i / 8;
			this.r.setPixel(this.mx + dx - 2, this.my + dy - 6, this.currentTool.data[i]);
		}
	}
	
	public void setColorIndex(int col){
		int res = -1;
		for(int i = 0; i < this.currentPallet.colors.length; i++){
			if(this.currentPallet.colors[i] == col){
				res = i;
				return;
			}
		}
		this.currentColorIndex = res;
	}

}
