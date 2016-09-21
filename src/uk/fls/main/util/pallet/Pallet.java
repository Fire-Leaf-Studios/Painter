package uk.fls.main.util.pallet;

import fls.engine.main.io.FileIO;

public class Pallet {

	
	public static Pallet def = new Pallet(120,120,120,
			0,0,252,
			0,0,188,
			68,40,188,
			148,0,132,
			168,0,32,
			168,16,0,
			136,20,0,
			80,48,0,
			0,120,0,
			0,104,0,
			0,88,0,
			0,64,88,
			0,0,0,
			0,0,0,
			0,0,0,
			188,188,188,
			0,120,248,
			0,88,248,
			104,68,252,
			216,0,204,
			228,0,88,
			248,56,0,
			228,92,16,
			172,124,0,
			0,184,0,
			0,168,0,
			0,168,68,
			0,136,136,
			0,0,0,
			0,0,0,
			0,0,0,
			248,248,248,
			60,188,252,
			104,136,252,
			152,120,248,
			248,120,248,
			248,88,152,
			248,120,88,
			252,160,68,
			248,184,0,
			184,248,24,
			88,216,84,
			88,248,152,
			0,232,216,
			120,120,120,
			0,0,0,
			0,0,0,
			252,252,252,
			164,228,252,
			184,184,248,
			216,184,248,
			248,184,248,
			248,164,192,
			240,208,176,
			252,224,168,
			248,216,120,
			216,248,120,
			184,248,184,
			184,248,216,
			0,252,252,
			248,216,248,
			0,0,0,
			0,0,0,
			123,123,123
			);
	public int[] colors;
	
	public final boolean valid;
	
	public Pallet(int...colors){
		if(colors.length % 3 != 0)throw new RuntimeException("Invalid number of colors given");
		int numColors = 0;
		int[] newColors = new int[64];
		for(int i  = 0; i < colors.length; i+=3){
			int r = colors[i];
			int g = colors[i + 1];
			int b = colors[i + 2];
			int rgb = (r << 16) | (g << 8) | b;
			newColors[i/3] = rgb;
			numColors++;
			if(numColors >= 64)break;
		}
		this.colors = newColors;
		this.valid = true;
	}
	
	//This constructor will load a bunch of CSV or in this case just look at the given array and convert them to "real" colours.
	public Pallet(String...colors){
		if(colors.length % 3 != 0)throw new RuntimeException("Invalid number of colors given");
		int numColors = 0;
		int[] newColors = new int[64];
		for(int i  = 0; i < colors.length; i+=3){
			int r = Integer.parseInt(colors[i].trim());
			int g = Integer.parseInt(colors[i + 1].trim());
			int b = Integer.parseInt(colors[i + 2].trim());
			int rgb = (r << 16) | (g << 8) | b;
			newColors[i/3] = rgb;
			numColors++;
			if(numColors >= 64)break;
		}
		this.colors = newColors;
		this.valid = true;
	}
	
	/**
	 * Converts hex values to required values
	 * @param file
	 */
	public Pallet(String file){
		String[] data = FileIO.instance.loadFile(file).split("\n");
		int[] newColors = new int[64];
		int numColors = 0;
		for(int i = 0; i < data.length; i++){
			if(data[i].startsWith(";"))continue;
			newColors[numColors] = hexToInt(data[i]);
			numColors++;
			if(numColors >= 64)break;
		}
		this.colors = newColors;
		this.valid = true;
	}
	
	private int hexToInt(String line){
		line = line.trim();
		if(line.length() == 8){// Has alpha component
			line = line.substring(2);
		}
		int r = Integer.parseInt(line.substring(0, 2), 16);
		int g = Integer.parseInt(line.substring(2, 4), 16);
		int b = Integer.parseInt(line.substring(4, 6), 16);
		int rgb = (r << 16) | (g << 8) | b;
		return rgb;
	}
}
