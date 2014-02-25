package uk.me.andrewwatkins.mastermind.data;

import java.util.ArrayList;

public class CodeObject {
	
	private ArrayList<ColourCode> code = new ArrayList<ColourCode>();
	private ArrayList<Boolean> marks = new ArrayList<Boolean>();
	
	public void addColour(ColourCode colour) {
		code.add(colour);
		marks.add(false);
	}
	
	public ColourCode getColour(int i) {
		return code.get(i);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CodeObject)){
			return false;
		}
		
		CodeObject otherCode = (CodeObject) other;
		
		if (otherCode.length() != this.length()) {
			return false;
		}
		
		for (int i = 0; i < this.length(); i++) {
			if (otherCode.getColour(i) != this.getColour(i)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder retString = new StringBuilder();
		
		for (int i = 0; i < this.length(); i++) {
			retString.append(this.getColour(i).toString());
			retString.append(';');
		}
		
		return retString.toString();
	}
	
	public int length() {
		return code.size();
	}

	public void mark(int i) {
		marks.set(i, true);
	}
	
	public void reset() {
		for (int i = 0; i < marks.size(); i++) {
			marks.set(i, false);
		}
	}

	public boolean isMarked(int i) {
		return marks.get(i);
	}
	
}
