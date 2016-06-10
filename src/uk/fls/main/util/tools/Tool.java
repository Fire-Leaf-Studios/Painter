package uk.fls.main.util.tools;

import uk.fls.main.screens.PaintScreen;

public abstract class Tool {

	public int[] data;
	public int[] shadeData;
	
	// X & Y refernce to the current drawing
	public abstract void onClick(PaintScreen ps, int x, int y, int col, int ccol);
}
