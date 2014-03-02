package uk.me.andrewwatkins.mastermind.data;

import java.util.ArrayList;
import java.util.Random;

import uk.me.andrewwatkins.mastermind.gui.ColourSlot;
import uk.me.andrewwatkins.mastermind.gui.GuiMain;
import uk.me.andrewwatkins.mastermind.gui.MarkSlotBoard;
import uk.me.andrewwatkins.mastermind.gui.SlotBoard;

public class MastermindHandler {
	
	private CodeObject code = new CodeObject();
	
	private CodeObject guess;
	
	private int codeLength;
	private int colourLength;

	private SlotBoard codeSlots;

	private Random random;

	private GuiMain guimain;

	private boolean guessing;
	
	private ArrayList<CodeObject> possibleGuesses;
	
	public MastermindHandler(GuiMain guiMain, int codeLength, int colourLength, SlotBoard codeSlots, boolean guessing) {
		this.codeLength = codeLength;
		this.colourLength = colourLength;
		this.codeSlots = codeSlots;
		
		this.guimain = guiMain;
		
		this.guessing = guessing;
		
		this.random = new Random();
		
		if (!guessing) {
			generateCode();
		}
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
	
	public void setCode(ColourSlot[] column) {
		code = new CodeObject();
		
		for (int i = 0; i < column.length; i++) {
			code.addColour(column[i].getColourCode());
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
		
		this.guimain.checkWin(poscol);
	}

	public void startGuessing() {
		generatePossibleGuesses();
	}
	
	private void generatePossibleGuesses() {
		this.possibleGuesses = generatePossibleGuesses(generateInitialCodes(), this.codeLength-1);
		
		System.out.println(this.possibleGuesses.size());
	}
	
	private ArrayList<CodeObject> generateInitialCodes() {
		ArrayList<CodeObject> initCodes = new ArrayList<CodeObject>();
		for (int i = 0; i < this.colourLength; i++) {
			CodeObject newCode = new CodeObject();
			newCode.addColour(ColourCode.values()[i+1]);
			initCodes.add(newCode);
		}
		return initCodes;
	}

	private ArrayList<CodeObject> generatePossibleGuesses(ArrayList<CodeObject> curCodes, int codeLength) {
		System.out.println(curCodes.size());
		if (codeLength == 0) {
			return curCodes;
		}
		ArrayList<CodeObject> nextCodes = new ArrayList<CodeObject>();
		for (int i = 0; i < curCodes.size(); i++) {
			for (int j = 0; j < this.colourLength; j++) {
				CodeObject curCode = curCodes.get(i).clone();
				
				curCode.addColour(ColourCode.values()[j+1]);
				nextCodes.add(curCode);
			}
		}
		curCodes = null;
		return generatePossibleGuesses(nextCodes, codeLength-1);
	}
	
}
