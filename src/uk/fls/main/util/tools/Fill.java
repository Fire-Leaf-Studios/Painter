package uk.fls.main.util.tools;

import uk.fls.main.screens.PaintScreen;
import uk.fls.main.util.SpriteParser;
import uk.fls.main.util.Tile;

public class Fill extends Tool{

	public Fill(SpriteParser sp){
		this.data = sp.getData(2, 2);
	}

	@Override
	public void onClick(PaintScreen ps, int x, int y, int col, int ccol) {
		Tile cf = ps.tiles[ps.cTile];
		if(x < 0 || y < 0 || x >= 8 || y >= 8)return;
		if(cf.getData()[x + y * 8] != ccol || cf.getData()[x + y * 8] == col)return;
		cf.setData(x, y, col);
		onClick(ps, x - 1, y, col, ccol);
		onClick(ps, x, y - 1, col, ccol);
		onClick(ps, x + 1, y, col, ccol);
		onClick(ps, x, y + 1, col, ccol);
	}

}
