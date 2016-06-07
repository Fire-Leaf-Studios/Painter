package uk.fls.main.util;

import java.util.Arrays;

public class Tile {
	
	private int[] data;
	private final int w;
	
	public Tile(int s){
		this.w = s;
		this.data = new int[s * s];
		Arrays.fill(data, -1);
	}
	
	public void setData(int x,int y, int c){
		if(x < 0 || y < 0 || x >= this.w || y >= this.w)return;
		this.data[x + y * this.w] = c;
	}
	
	public int[] getData(){
		return this.data;
	}
}
