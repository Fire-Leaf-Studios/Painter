package uk.fls.main.util.tools;

import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.screens.PaintScreen;

public class Pencil extends Tool{

	public Pencil(SpriteParser sp){
		this.data = sp.getData(0, 2);
	}

	@Override
	public void onClick(PaintScreen ps, int x, int y, int col, int ccol) {
		ps.tiles[ps.cTile].setData(x, y, col);
	}
}
