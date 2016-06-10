package uk.fls.main.util.tools;

import uk.fls.main.screens.PaintScreen;
import uk.fls.main.util.SpriteParser;

public class Pencil extends Tool{

	public Pencil(SpriteParser sp){
		this.data = sp.getData(1, 2);
	}

	@Override
	public void onClick(PaintScreen ps, int x, int y, int col, int ccol) {
		ps.tiles[ps.cTile].setData(x, y, col);
	}
}
