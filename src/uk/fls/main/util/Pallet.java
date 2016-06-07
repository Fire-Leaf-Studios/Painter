package uk.fls.main.util;

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
			0,0,0
			);
	public int[] colors;
	
	public Pallet(int...colors){
		if(colors.length % 3 != 0)throw new RuntimeException("Invalid number of colors given");
		int[] newColors = new int[colors.length / 3];
		for(int i  = 0; i < colors.length; i+=3){
			int r = colors[i];
			int g = colors[i + 1];
			int b = colors[i + 2];
			int rgb = (r << 16) | (g << 8) | b;
			newColors[i/3] = rgb;
		}
		this.colors = newColors;
	}
	
	//This constructor will load a bunch of CSV or in this case just look at the given array and convert them to "real" colours.
	public Pallet(String...colors){
		if(colors.length % 3 != 0)throw new RuntimeException("Invalid number of colors given");
		int[] newColors = new int[colors.length / 3];
		for(int i  = 0; i < colors.length; i+=3){
			int r = Integer.parseInt(colors[i].trim());
			int g = Integer.parseInt(colors[i + 1].trim());
			int b = Integer.parseInt(colors[i + 2].trim());
			int rgb = (r << 16) | (g << 8) | b;
			newColors[i/3] = rgb;
		}
		this.colors = newColors;
	}
}
