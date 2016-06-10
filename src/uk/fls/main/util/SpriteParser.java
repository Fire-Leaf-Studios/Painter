package uk.fls.main.util;

public class SpriteParser {

	
	private int[][] data;
	
	private int w;
	
	public SpriteParser(int s, String data){
		this.w = s;
		String[] lines = data.split("\n");
		for(String line : lines){
			if(line.startsWith("#")) {
			}
		}
		this.data = new int[(s * s) * (s * s)][];
		for(int i = 0; i < lines.length; i++){
			String l = lines[i];
			if(!l.startsWith("#"))continue;
			l = l.substring(1);
			String[] nd = l.trim().split(",");
			int[] d = new int[s * s];
			for(int j = 0; j < nd.length; j++){
				d[j] = CompressionManager.decompress(nd[j]);
			}
			this.data[i] = d;
		}
	}
	
	public int[] getData(int x, int y){
		int[] res = new int[this.w * this.w];
		int tx = x;
		int ty = y;
		if(tx < 0 || ty < 0 || tx >= this.w  || ty >= this.w)return null;
		for(int i = 0; i < this.w * this.w; i++){
			int dx = i % this.w;
			int dy = i / this.w;
			res[dx + dy * this.w] = this.data[tx + ty * this.w][i];
		}
		return res;
	}
}
