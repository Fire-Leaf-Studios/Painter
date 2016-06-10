package uk.fls.main.util;

public class CompressionManager {

	
	public static int decompress(String s){
		int res = 0;
		if(s.equals("-1")){
			res= -1;
		}else res = Integer.parseInt(s, 16);
		return res;
	}
}
