package uk.fls.main;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.screens.PaintScreen;

@SuppressWarnings("serial")
public class Painter extends Init{

	private static int w = 200;
	private static int h = (w / 4) * 3;
	private static int s = 3;
	public Painter(){
		super("FLS Painter V0.8", w * s, h * s);
		useCustomBufferedImage(w, h, false);
		setInput(new Input(this, Input.MOUSE));
		setScreen(new PaintScreen());
		skipInit();
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		this.frame.getContentPane().setCursor(blankCursor);
		
		
		int scale = 4;
		BufferedImage icon = new BufferedImage(8 * scale, 8 * scale,BufferedImage.TYPE_INT_RGB);
		SpriteParser sp = new SpriteParser(8, FileIO.instance.readInternalFile("/gui.art"));
		
		int[] d = sp.getData(1, 2);
		int[] res = new int[32 * 32];
		for(int i = 0 ; i <  8 * 8; i++){
			int tx = i % 8;
			int ty = i / 8;
			
			for(int j = 0; j < scale * scale; j++){
				int dx = j % scale;
				int dy = j / scale;
				
				int fx = (tx * scale) + dx;
				int fy = (ty * scale) + dy;
				res[fx + fy * (8 * scale)] = d[tx + ty * 8];
			}
		}
		icon.setRGB(0, 0, 8 * scale, 8 * scale, res, 0, 8 * scale);
		this.frame.setIconImage(icon);
	}
	
	public static void main(String[] args){
		new Painter().start();
	}
}
