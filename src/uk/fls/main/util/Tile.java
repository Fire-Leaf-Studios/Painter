package uk.fls.main.util;

import java.util.Arrays;

public class Tile {
	
	private int[] data;
	private final int w;
	
	public Tile(int w){
		this.w = w;
		this.data = new int[w * w];
		Arrays.fill(this.data, -1);
	}
	
	public void setData(int x,int y, int c){
		if(x < 0 || y < 0 || x >= this.w || y >= this.w)return;
		this.data[x + y * this.w] = c;
	}
	
	public void setData(int[] d){
		if(d.length != this.data.length)return;
		this.data = d;
	}
	
	public int[] getData(){
		return this.data;
	}
	
	public int getWidth(){
		return this.w;
	}
}
