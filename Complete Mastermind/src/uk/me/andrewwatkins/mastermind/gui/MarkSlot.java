package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Rectangle;

public class MarkSlot extends Rectangle {

	private static final long serialVersionUID = 1396080751365229337L;
	
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private int colour;
	
	public MarkSlot(int xPos, int yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
	}
	
	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}
}
