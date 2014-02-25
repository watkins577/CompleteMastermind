package uk.me.andrewwatkins.mastermind.data;

import java.util.Random;

import uk.me.andrewwatkins.mastermind.gui.ColourSlot;
import uk.me.andrewwatkins.mastermind.gui.MarkSlotBoard;
import uk.me.andrewwatkins.mastermind.gui.SlotBoard;

public class MastermindHandler {
	
	private CodeObject code = new CodeObject();
	
	private CodeObject guess;
	
	private int codeLength;
	private int colourLength;

	private SlotBoard codeSlots;

	private Random random;
	
	public MastermindHandler(int codeLength, int colourLength, SlotBoard codeSlots) {
		this.codeLength = codeLength;
		this.colourLength = colourLength;
		this.codeSlots = codeSlots;
		
		this.random = new Random();
		
		generateCode();
	}

	private void generateCode() {
		for (int i = 0; i < codeLength; i++) {
			ColourCode colour = ColourCode.values()[(this.random.nextInt(colourLength)+1)];
			code.addColour(colour);
			codeSlots.setColour(0, i*ColourSlot.HEIGHT, colour);
		}
		
		codeSlots.setColumnCovered(0, true);
		
		codeSlots.repaint();
		
		System.out.println(code);
	}

	public void setGuess(ColourSlot[] column) {
		guess = new CodeObject();
		
		for (int i = 0; i < column.length; i++) {
			guess.addColour(column[i].getColourCode());
		}
	}

	public void compare(MarkSlotBoard markslots, int curColumn) {
		int poscol = 0;
		int col = 0;
		
		for (int i = 0; i < guess.length(); i++) {
			if (guess.getColour(i) == code.getColour(i)) {
				code.mark(i);
				poscol++;
			}
		}
		
		for (int i = 0; i < guess.length(); i++) {
			for (int j = 0; j < code.length(); j++) {
				if (guess.getColour(i) == code.getColour(j)) {
					if (i == j) {
						break;
					}
					if (code.isMarked(j)) {
						continue;
					}
					code.mark(j);
					col++;
				}
			}
		}
		
		code.reset();
		
		markslots.setSlots(curColumn, poscol, col);
		
		markslots.repaint();
	}
}
