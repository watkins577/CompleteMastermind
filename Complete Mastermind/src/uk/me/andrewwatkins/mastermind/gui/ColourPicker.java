package uk.me.andrewwatkins.mastermind.gui;

import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class ColourPicker extends SlotBoard {

	private static final long serialVersionUID = 2259771252520696525L;
	
	public ColourPicker(int xSize, int xPos, int yPos) {
		super(xSize, 1, xPos, yPos, false);
		
		this.setRow(0, generatePickerColours(xSize));
	}

	private ColourCode[] generatePickerColours(int xSize) {
		ColourCode[] colours = new ColourCode[xSize];
		
		for (int i = 0; i < xSize; i++) {
			colours[i] = ColourCode.values()[i+1];
		}
		return colours;
	}
	
	@Override
	public void setColour(int x, int y, ColourCode colour) {
		
	}
	
	public ColourCode getColour(int x, int y) {
		int slotx = x/ColourSlot.WIDTH;
		int sloty = y/ColourSlot.HEIGHT;
		
		this.setSelected(slotx, sloty);
		
		this.repaint();
		return slots[slotx][sloty].getColourCode();
	}

}
