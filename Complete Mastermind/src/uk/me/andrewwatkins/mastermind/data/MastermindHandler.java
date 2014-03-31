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
	
	private int guessNum;

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
		
		this.guessNum = 1;
		
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
		
		this.guessNum++;
		
		if (this.guessNum > this.guimain.xSize) {
			this.guimain.slots.setColumnCovered(this.guimain.curColumn, false);
			this.guimain.codeSlots.setColumnCovered(0, false);
			return;
		}
		
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
		nextGuess();
	}
	
	private void generateIntialPossibleColours() {
		this.possibleColours = new ArrayList<ColourCode>();
		for (int i = 1; i <= this.colourLength; i++) {
			this.possibleColours.add(ColourCode.values()[i]);
		}
	}

	private void nextGuess() {
		if (this.codeLength == 4 && this.possibleColours.size() <= 6) {
			//this.possibleGuesses = generatePossibleGuesses(generateInitialCodes(), this.codeLength-1);
			
			knuthGuess();
		} else {
			randomGuess();
		}
	}
	
	private void knuthGuess() {
		if (this.guessNum == 1) {
			this.possibleGuesses = generatePossibleGuesses(generateInitialCodes(), this.codeLength-1);
			this.guess = new CodeObject();
			ColourCode[] knuthGuess = new ColourCode[this.codeLength];
			this.guess.addColour(ColourCode.values()[1]);
			this.guess.addColour(ColourCode.values()[1]);
			this.guess.addColour(ColourCode.values()[2]);
			this.guess.addColour(ColourCode.values()[2]);
			knuthGuess[0] = ColourCode.values()[1];
			knuthGuess[1] = ColourCode.values()[1];
			knuthGuess[2] = ColourCode.values()[2];
			knuthGuess[3] = ColourCode.values()[2];
			
			this.guimain.slots.setColumn(this.guimain.curColumn, knuthGuess);
			compareKnuth(guimain.markslots, guimain.curColumn);
		} else {
			ArrayList<Integer> numberRemoved = new ArrayList<Integer>();
			
			int maxRem = 0;
			for (int i = 0; i < this.possibleGuesses.size(); i++) {
				int numRem = 0;
				for (int poscoltemp = 0; poscoltemp < this.codeLength; poscoltemp++) {
					for (int coltemp = 0; coltemp < this.codeLength; coltemp++) {
						for (int j = 0; j < this.possibleGuesses.size(); j++) {
							if (!checkPossible(this.possibleGuesses.get(i), poscoltemp, coltemp, this.possibleGuesses.get(j))) {
								numRem++;
							}
						}
					}
				}
				maxRem = Math.max(numRem, maxRem);
				numberRemoved.add(numRem);
			}
			
			for (int i = 0; i < numberRemoved.size(); i++) {
				ColourCode[] knuthGuess = new ColourCode[this.codeLength];
				if (maxRem == numberRemoved.get(i)) {
					this.guess = this.possibleGuesses.get(i);
					
					for (int j = 0; j < this.codeLength; j++) {
					
						knuthGuess[j] = this.guess.getColour(j);
					}
					this.guimain.slots.setColumn(this.guimain.curColumn, knuthGuess);
					compareKnuth(guimain.markslots, guimain.curColumn);
				}
			}
		}
	}

	private void compareKnuth(MarkSlotBoard markslots, int curColumn) {
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
		
		for (int i = 0; i < possibleGuesses.size(); i++) {
			if (!checkPossible(guess, poscol, col, possibleGuesses.get(i))) {
				possibleGuesses.remove(i);
				i--;
			}
		}
		System.out.println(possibleGuesses);
		code.reset();
		
		markslots.setSlots(curColumn, poscol, col);
		
		markslots.repaint();
		
		this.guimain.checkWin(poscol);
		
		this.guessNum++;
		
		if (this.guessNum > this.guimain.xSize) {
			this.guimain.codeSlots.setColumnCovered(0, false);
			this.guimain.slots.setColumnCovered(this.guimain.curColumn, false);
			return;
		}
		
		if (this.guessing) {
			this.guimain.slots.setColumnCovered(this.guimain.curColumn, false);
			this.guimain.slots.repaint();
			if (!this.guimain.won && this.guimain.curColumn < this.guimain.xSize-1) {
				this.guimain.curColumn++;
				nextGuess();
			}
		}
	}

	private boolean checkPossible(CodeObject guess, int poscol, int col,
			CodeObject codeObject) {
		int poscoltemp = 0;
		int coltemp = 0;
		
		for (int i = 0; i < guess.length(); i++) {
			if (guess.getColour(i) == codeObject.getColour(i)) {
				codeObject.mark(i);
				guess.mark(i);
				poscoltemp++;
			}
		}
		
		for (int i = 0; i < guess.length(); i++) {
			for (int j = 0; j < codeObject.length(); j++) {
				if (guess.getColour(i) == codeObject.getColour(j)) {
					if (i == j) {
						break;
					}
					if (codeObject.isMarked(j) || guess.isMarked(i)) {
						continue;
					}
					guess.mark(i);
					codeObject.mark(j);
					coltemp++;
				}
			}
		}
		
		guess.reset();
		codeObject.reset();
		return poscol == poscoltemp && col == coltemp;
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
