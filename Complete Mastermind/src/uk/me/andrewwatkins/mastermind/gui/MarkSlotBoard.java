package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MarkSlotBoard extends JPanel {

	private static final long serialVersionUID = -3327603696012622581L;
	
	private MarkSlotPiece[] markpieces;
	
	private int xSize;
	public MarkSlotBoard(int xSize, int ySize, int xPos, int yPos) {
		if (xSize <= 0) {
			xSize = 1;
			System.out.println("Too small width, setting to 1");
		}
		if (ySize <= 0) {
			ySize = 1;
			System.out.println("Too small height, setting to 1");
		}
		
		this.xSize = xSize;
		markpieces = new MarkSlotPiece[xSize];
		
		for (int i = 0; i < xSize; i++) {
			markpieces[i] = new MarkSlotPiece(i*MarkSlotPiece.WIDTH, yPos, ySize);
		}
		
		this.setBounds(xPos, yPos, xSize*MarkSlotPiece.WIDTH, markpieces[0].getHeight());
		
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(xSize*ColourSlot.WIDTH, markpieces[0].getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int i = 0; i < xSize; i++) {
			markpieces[i].paintComponent(g);
		}
	}
	
	@Override
	public int getHeight() {
		return markpieces[0].getHeight();
	}

	public void setSlots(int column, int poscol, int col) {
		markpieces[column].setSlots(poscol, col);
	}

}
