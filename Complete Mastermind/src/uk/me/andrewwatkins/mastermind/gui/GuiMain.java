package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import uk.me.andrewwatkins.mastermind.data.ColourCode;

public class GuiMain implements MouseListener {
	
	//Dimension constants
	private static final int PREF_WIDTH = 800;
	private static final int PREF_HEIGHT = 600;
	
	//Game size constants
	private static final int DEFAULT_XSIZE = 12;
	private static final int DEFAULT_YSIZE = 4;
	private static final int DEFAULT_COLOURSIZE = 6;
	
	private static final int MAX_XSIZE = 14;
	private static final int MAX_YSIZE = 8;
	private static final int MAX_COLOURSIZE = 6;
	
	//Game selection variables
	private JSpinner xSizeSpinner;
	private JSpinner ySizeSpinner;
	private JSpinner colourSizeSpinner;
	private JButton startButton;
	private int xSize;
	private int ySize;
	private int colourSize;
	
	//Game panel variables
	private MarkSlotBoard markslots;
	private SlotBoard slots;
	private SlotBoard codeSlots;
	private ColourPicker picker;
	private JButton readyButton;
	private ColourCode curColour = ColourCode.EMPTY;
	private int curColumn = 0;
	
	//Frame variables
	private JFrame frame;
	private JPanel menuPanel;
	
	

	public GuiMain() {
		
		frame = initialiseFrame();
		
		menuPanel = initialisePanel();
		createSelectionPanel(menuPanel);
		
		frame.add(menuPanel);
		
		frame.pack();
		frame.setVisible(true);
	}


	private JFrame initialiseFrame() {
		JFrame frame = new JFrame("Complete Mastermind");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (screenSize.width - PREF_WIDTH)/2;
		int yPos = (screenSize.height - PREF_HEIGHT)/2;
		
		frame.setMinimumSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		frame.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		frame.setLocation(xPos, yPos);
		frame.setLayout(null);
		return frame;
	}


	private JPanel initialisePanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, PREF_WIDTH, PREF_HEIGHT);
		panel.setLayout(null);
		return panel;
	}
	
	private void createSelectionPanel(JPanel panel) {
		int baseXPos = 50;
		int baseYPos = 50;
		
		int yPos = baseYPos;
		JLabel xSizeLabel = new JLabel("Amount of Guesses");
		xSizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_XSIZE, 1, MAX_XSIZE, 1));
		xSizeLabel.setLabelFor(xSizeSpinner);
		
		this.setPosition(xSizeLabel, baseXPos, yPos);
		this.setPosition(xSizeSpinner, xSizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(xSizeLabel);
		panel.add(xSizeSpinner);
		
		yPos += xSizeLabel.getPreferredSize().height + 10;
		
		JLabel ySizeLabel = new JLabel("Length of Code");
		ySizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_YSIZE, 1, MAX_YSIZE, 1));
		ySizeLabel.setLabelFor(ySizeSpinner);
		
		this.setPosition(ySizeLabel, baseXPos, yPos);
		this.setPosition(ySizeSpinner, ySizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(ySizeLabel);
		panel.add(ySizeSpinner);
		
		yPos += ySizeLabel.getPreferredSize().height + 10;
		
		JLabel colourSizeLabel = new JLabel("Amount of Colours");
		colourSizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_COLOURSIZE, 1, MAX_COLOURSIZE, 1));
		colourSizeLabel.setLabelFor(colourSizeSpinner);
		
		this.setPosition(colourSizeLabel, baseXPos, yPos);
		this.setPosition(colourSizeSpinner, colourSizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(colourSizeLabel);
		panel.add(colourSizeSpinner);
		
		yPos += colourSizeLabel.getPreferredSize().height + 10;
		
		startButton = new JButton("Start");
		this.setPosition(startButton, baseXPos, yPos);
		
		panel.add(startButton);
		
		startButton.addMouseListener(this);
	}
	
	private void setPosition(JComponent comp, int x, int y) {
		comp.setBounds(x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
	}
	
	private void createMastermindPanel(JPanel panel) {
		markslots = new MarkSlotBoard(xSize, ySize, 0, 0);
		
		panel.add(markslots);
		
		slots = new SlotBoard(xSize, ySize, 0, markslots.getHeight()+1, true);
		
		panel.add(slots);
		
		codeSlots = new SlotBoard(1, ySize, (xSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1, false);
		
		panel.add(codeSlots);
		
		picker = new ColourPicker(colourSize, 0, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20);
		
		panel.add(picker);
		
		readyButton = new JButton("Ready");
		
		this.setPosition(readyButton, (colourSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20);
		
		panel.add(readyButton);
		
		slots.addMouseListener(this);
		picker.addMouseListener(this);
		readyButton.addMouseListener(this);
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
		if (comp == null) {
			return;
		}
		
		//Creation Menu
		if (comp == startButton) {
			this.xSize = (int) xSizeSpinner.getModel().getValue();
			this.ySize = (int) ySizeSpinner.getModel().getValue();
			this.colourSize = (int) colourSizeSpinner.getModel().getValue();
			
			frame.remove(menuPanel);
			
			JPanel mastermindPanel = initialisePanel();
			createMastermindPanel(mastermindPanel);
			
			mastermindPanel.setLayout(null);
			
			frame.add(mastermindPanel);
			
			frame.repaint();
		}
		
		
		//GamePlay
		if (comp == picker) {
			this.curColour = picker.getColour(mouse.getX(), mouse.getY());
			return;
		}
		
		if (comp == slots) {
			slots.setColour(mouse.getX(), mouse.getY(), curColour);
			return;
		}
		
		if (comp == readyButton) {
			//This needs to check for matchingness
			if (slots.isColumnFull(this.curColumn)) {
				curColumn++;
				slots.setColumnCovered(curColumn, false);
			}
			return;
		}
	}

}
