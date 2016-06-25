package uk.fls.main.util.tools;

import fls.engine.main.util.rendertools.SpriteParser;
import uk.fls.main.screens.PaintScreen;

public class Pipette extends Tool{
	
	public Pipette(SpriteParser sp){
		this.data = sp.getData(2, 2);
	}

	@Override
	public void onClick(PaintScreen ps, int x, int y, int col, int ccol) {
		for(int i = 0; i < ps.currentPallet.colors.length; i++){
			if(ps.currentPallet.colors[i] == ccol){
				ps.currentColorIndex = i;
				return;
			}
		}
	}

}
