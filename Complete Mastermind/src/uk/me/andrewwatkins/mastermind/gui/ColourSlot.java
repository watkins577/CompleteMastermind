package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Rectangle;

import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class ColourSlot extends Rectangle {
	
	private static final long serialVersionUID = -5440786790724921861L;
	
	public static final int WIDTH = 45;
	public static final int HEIGHT = 45;
	
	private boolean covered;
	private boolean selected;
	private boolean locked;
	
	private ColourCode colour = ColourCode.EMPTY;

	public ColourSlot(int xPos, int yPos, ColourCode colour, boolean covered, boolean selected) {
		//The -1 is to accommodate for the extra pixel
		super(xPos, yPos, WIDTH-1, HEIGHT-1);
		this.colour = colour;
		this.covered = covered;
		this.selected = selected;
	}
	
	public ColourCode getColourCode() {
		if (covered) {
			return null;
		}
		return colour;
	}
	
	public void setColour(ColourCode colour) {
		this.colour = colour;
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
