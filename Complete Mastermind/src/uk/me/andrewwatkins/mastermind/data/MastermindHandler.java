package uk.me.andrewwatkins.mastermind.data;

import java.util.Random;

import uk.me.andrewwatkins.mastermind.gui.ColourSlot;
import uk.me.andrewwatkins.mastermind.gui.SlotBoard;

public class MastermindHandler {
	
	private CodeObject code = new CodeObject();
	
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
			ColourCode colour = ColourCode.values()[this.random.nextInt(colourLength)];
			code.addColour(colour);
			codeSlots.setColour(0, i*ColourSlot.HEIGHT, colour);
		}
		
		codeSlots.setColumnCovered(0, true);
		
		codeSlots.repaint();
		
		System.out.println(code);
	}
}
