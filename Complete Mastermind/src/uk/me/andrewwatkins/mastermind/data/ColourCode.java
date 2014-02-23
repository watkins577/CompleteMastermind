package uk.me.andrewwatkins.mastermind.data;

public enum ColourCode {
	
	EMPTY (0xFFFFFF), RED (0xFF0000), GREEN (0x00FF00), BLUE (0x0000FF), YELLOW (0xFFFF00), PURPLE (0x8000FF), ORANGE (0xFF8000);
	
	int colour = 0;
	
	ColourCode(int colour) {
		if (colour > 0xFFFFFF) {
			colour = 0xFFFFFF;
		}
		if (colour < 0) {
			colour = 0;
		}
		this.colour = colour;
	}
	
	public int getColour() {
		return colour;
	}
	
}
