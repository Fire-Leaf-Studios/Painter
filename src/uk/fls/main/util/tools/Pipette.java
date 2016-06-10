package uk.fls.main.util.tools;

import uk.fls.main.screens.PaintScreen;
import uk.fls.main.util.SpriteParser;

public class Pipette extends Tool{
	
	public Pipette(SpriteParser sp){
		this.data = sp.getData(3, 2);
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
