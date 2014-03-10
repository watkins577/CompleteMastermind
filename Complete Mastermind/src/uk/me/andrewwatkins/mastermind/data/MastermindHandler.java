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

	private ArrayList<CodeObject> possibleGuesses;
	private ArrayList<ColourCode> possibleColours;

	private boolean guessing;
	
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
				guess.mark(i);
				poscol++;
			}
		}
		
		for (int i = 0; i < guess.length(); i++) {
			for (int j = 0; j < code.length(); j++) {
				if (guess.getColour(i) == code.getColour(j)) {
					if (i == j) {
						break;
					}
					if (code.isMarked(j) || guess.isMarked(i)) {
						continue;
					}
					guess.mark(i);
					code.mark(j);
					col++;
				}
			}
		}
		
		code.reset();
		
		markslots.setSlots(curColumn, poscol, col);
		
		markslots.repaint();
		
		this.guimain.checkWin(poscol);
		
		if (this.guessing) {
			this.guimain.slots.setColumnCovered(this.guimain.curColumn, false);
			this.guimain.slots.repaint();
			if (!this.guimain.won && this.guimain.curColumn < this.guimain.xSize-1) {
				this.guimain.curColumn++;
				nextGuess();
			}
		}
	}

	public void startGuessing() {
		generateIntialPossibleColours();
		generatePossibleGuesses();
	}
	
	private void generateIntialPossibleColours() {
		this.possibleColours = new ArrayList<ColourCode>();
		for (int i = 1; i <= this.colourLength; i++) {
			this.possibleColours.add(ColourCode.values()[i]);
		}
	}
	
	private void nextGuess() {
		randomGuess();
	}

	private void generatePossibleGuesses() {
		if (this.codeLength <= 4 && this.possibleColours.size() <= 6) {
			this.possibleGuesses = generatePossibleGuesses(generateInitialCodes(), this.codeLength-1);
			
			randomGuess();
		} else {
			randomGuess();
		}
	}
	
	private void knuthGuess() {
		
	}

	private void randomGuess() {
		this.guess = new CodeObject();
		ColourCode[] randGuess = new ColourCode[this.codeLength];
		for (int i = 0; i < this.codeLength; i++) {
			this.guess.addColour(ColourCode.values()[random.nextInt(this.colourLength)+1]);
			randGuess[i] = this.guess.getColour(i);
		}
		
		this.guimain.slots.setColumn(this.guimain.curColumn, randGuess);
		compare(guimain.markslots, guimain.curColumn);
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
