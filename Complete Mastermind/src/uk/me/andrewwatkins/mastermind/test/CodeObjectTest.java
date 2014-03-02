package uk.me.andrewwatkins.mastermind.test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.me.andrewwatkins.mastermind.data.CodeObject;
import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class CodeObjectTest {

	@Test
	public void testAddColour() {
		String test = "ORANGE;";
		CodeObject obj1 = new CodeObject();
		
		obj1.addColour(ColourCode.ORANGE);
		
		assertEquals(test, obj1.toString());
	}

	@Test
	public void testGetColour() {
		String test = "RED";
		
		CodeObject obj1 = new CodeObject();
		obj1.addColour(ColourCode.BLUE);
		obj1.addColour(ColourCode.RED);
		
		assertEquals(test, obj1.getColour(1).toString());
	}

	@Test
	public void testEqualsObject() {
		CodeObject obj1 = new CodeObject();
		CodeObject obj2 = new CodeObject();
		
		for (int i = 1; i < 4; i++) {
			obj1.addColour(ColourCode.values()[i]);
			obj2.addColour(ColourCode.values()[i]);
		}
		
		assertTrue(obj1.equals(obj2));
	}
	
	@Test
	public void testToString() {
		String test = "BLUE;RED;GREEN;";
		
		CodeObject obj1 = new CodeObject();
		
		obj1.addColour(ColourCode.BLUE);
		obj1.addColour(ColourCode.RED);
		obj1.addColour(ColourCode.GREEN);
		
		assertEquals(test, obj1.toString());
	}

	@Test
	public void testLength() {
		CodeObject obj1 = new CodeObject();
		
		obj1.addColour(ColourCode.BLUE);
		obj1.addColour(ColourCode.GREEN);
		
		assertEquals(2, obj1.length());
	}

}
