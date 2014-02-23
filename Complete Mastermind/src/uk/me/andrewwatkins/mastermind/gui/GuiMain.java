package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class GuiMain implements MouseListener {
	
	private static final int PREF_WIDTH = 800;
	private static final int PREF_HEIGHT = 600;
	
	private MarkSlotBoard markslots;
	private SlotBoard slots;
	private SlotBoard codeSlots;
	private ColourPicker picker;
	private JButton readyButton;
	private ColourCode curColour = ColourCode.EMPTY;
	
	private int curColumn = 0;
	

	public GuiMain(int xSize, int ySize, int colourSize) {
		JFrame frame = new JFrame("Complete Mastermind");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (screenSize.width - PREF_WIDTH)/2;
		int yPos = (screenSize.height - PREF_HEIGHT)/2;
		
		frame.setMinimumSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		frame.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		frame.setLocation(xPos, yPos);
		frame.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, PREF_WIDTH, PREF_HEIGHT);
		panel.setLayout(null);
		
		markslots = new MarkSlotBoard(xSize, ySize, 0, 0);
		
		panel.add(markslots);
		
		slots = new SlotBoard(xSize, ySize, 0, markslots.getHeight()+1, true);
		
		panel.add(slots);
		
		codeSlots = new SlotBoard(1, ySize, (xSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1, false);
		
		panel.add(codeSlots);
		
		picker = new ColourPicker(colourSize, 0, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20);
		
		panel.add(picker);
		
		readyButton = new JButton("Ready");
		Dimension buttonSize = readyButton.getPreferredSize();
		
		readyButton.setBounds((colourSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20, buttonSize.width, buttonSize.height);
		
		panel.add(readyButton);
		
		slots.addMouseListener(this);
		picker.addMouseListener(this);
		readyButton.addMouseListener(this);
		
		frame.add(panel);
		
		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void mouseClicked(MouseEvent mouse) {
		
	}


	@Override
	public void mouseEntered(MouseEvent mouse) {
		
	}


	@Override
	public void mouseExited(MouseEvent mouse) {
		
	}


	@Override
	public void mousePressed(MouseEvent mouse) {
		
	}


	@Override
	public void mouseReleased(MouseEvent mouse) {
		Component comp = mouse.getComponent();
		if (comp == picker) {
			this.curColour = picker.getColour(mouse.getX(), mouse.getY());
		}
		
		if (comp == slots) {
			slots.setColour(mouse.getX(), mouse.getY(), curColour);
		}
		
		if (comp == readyButton) {
			//This needs to check for matchingness
			if (slots.isColumnFull(this.curColumn)) {
				curColumn++;
				slots.setColumnCovered(curColumn, false);
			}
		}
	}

}
