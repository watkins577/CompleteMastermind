package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MarkSlotPiece extends JPanel {

	private static final long serialVersionUID = 4287065579583818356L;
	
	public static final int WIDTH = (2*MarkSlot.WIDTH)+5;
	private int height;
	
	private MarkSlot[] markslots;

	private int size;
	
	public MarkSlotPiece(int xPos, int yPos, int size) {
		if (size < 1) {
			size = 1;
			System.out.println("Too small size, setting to 1");
		}
		
		this.size = size;
		
		this.height = ((int)(((size-1)/4)+1))*(MarkSlot.HEIGHT*2+5);
		
		this.setBounds(xPos, yPos, MarkSlotPiece.WIDTH, this.height);
		
		markslots = new MarkSlot[size];
		
		boolean left = true;
		boolean top = true;
		for (int i = 0; i < size; i++) {
			int curx = xPos + 2;
			int cury = yPos + 2 + ((int)(i/4))*(MarkSlot.HEIGHT*2+5);
			if (!left) {
				curx += MarkSlot.WIDTH+1;
			}
			if (!top) {
				cury += MarkSlot.HEIGHT+1;
			}
			markslots[i] = new MarkSlot(curx, cury);
			left = !left;
			if (left) {
				top = !top;
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MarkSlotPiece.WIDTH, this.height);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < size; i++) {
			MarkSlot slot = markslots[i];
			g2d.setColor(slot.getColour() == MarkSlot.EMPTY ? Color.LIGHT_GRAY : (slot.getColour() == MarkSlot.WHITE ? Color.WHITE : Color.BLACK));
			g2d.fill(slot);
			g2d.setColor(Color.GRAY);
			g2d.draw(slot);
			BufferedImage image;
			System.out.println(new File("src\\resources\\markslot.png").getAbsolutePath());
			try {
				image = ImageIO.read(new File("src\\resources\\markslot.png"));
				g2d.drawImage(image, slot.x, slot.y, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getHeight() {
		return height;
	}

	public void setSlots(int poscol, int col) {
		for (int i = 0; i < poscol; i++) {
			markslots[i].setColour(MarkSlot.BLACK);
		}
		
		for (int i = poscol; i < col+poscol; i++) {
			markslots[i].setColour(MarkSlot.WHITE);
		}
	}

}
