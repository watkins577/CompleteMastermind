package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class SlotBoard extends JPanel {

	private static final long serialVersionUID = -2930007036296363084L;
	
	protected ColourSlot[][] slots;

	protected int xSize;

	protected int ySize;
	
	protected static final int COVERED_COLOUR = 0x555555;

	public SlotBoard(int xSize, int ySize, int xPos, int yPos, boolean covered) {
		if (xSize <= 0) {
			xSize = 1;
			System.out.println("Too small width, setting to 1");
		}
		if (ySize <= 0) {
			ySize = 1;
			System.out.println("Too small height, setting to 1");
		}
		
		this.xSize = xSize;
		this.ySize = ySize;
		
		this.setBounds(xPos, yPos, xSize*ColourSlot.WIDTH, ySize*ColourSlot.HEIGHT);
		
		slots = new ColourSlot[xSize][ySize];
		
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				slots[i][j] = new ColourSlot(i*ColourSlot.WIDTH, j*ColourSlot.HEIGHT, ColourCode.EMPTY, covered ? i != 0 : false, false);
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(xSize*ColourSlot.WIDTH, ySize*ColourSlot.HEIGHT);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				ColourSlot slot = slots[i][j];
				g2d.setColor(new Color(slot.isCovered() ? SlotBoard.COVERED_COLOUR : slot.getColourCode().getColour()));
				g2d.fill(slot);
				g2d.setColor(slot.isSelected() ? Color.CYAN : Color.BLACK);
				g2d.draw(slot);
			}
		}
	}
	
	public void setRow(int row, ColourCode[] newColours) {
		for (int i = 0; i < xSize; i++) {
			ColourSlot slot = slots[i][row];
			
			slot.setColour(newColours[i]);
		}
		repaint();
	}
	
	public void setColumn(int column, ColourCode[] newColours) {
		for (int i = 0; i < ySize; i++) {
			ColourSlot slot = slots[column][i];
			
			slot.setColour(newColours[i]);
		}
		repaint();
	}
	
	public void setColumnCovered(int column, boolean covered) {
		for (int i = 0; i < ySize; i++) {
			ColourSlot slot = slots[column-1][i];
			slot.setLocked(true);
			slot = slots[column][i];
			slot.setCovered(covered);
		}
		repaint();
	}
	
	public boolean isColumnFull(int column) {
		boolean returnVal = true;
		for (int i = 0; i < ySize; i++) {
			ColourSlot slot = slots[column][i];
			
			if (slot.getColourCode() == ColourCode.EMPTY) {
				returnVal = false;
			}
		}
		return returnVal;
	}
	
	public void setSelected(int row, int column) {
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				ColourSlot slot = slots[i][j];
				
				if (slot.isSelected()) {
					slot.setSelected(false);
				}
				if (i == row && j == column) {
					slot.setSelected(true);
				}
			}
		}
	}
	
	public void setColour(int x, int y, ColourCode colour) {
		int slotx = x/ColourSlot.WIDTH;
		int sloty = y/ColourSlot.HEIGHT;
		
		ColourSlot slot = slots[slotx][sloty];
		if (!slot.isCovered() && !slot.isLocked()) {
			slot.setColour(colour);
		}
		this.repaint();
	}
}
