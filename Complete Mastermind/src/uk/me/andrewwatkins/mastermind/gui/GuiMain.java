package uk.me.andrewwatkins.mastermind.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import uk.me.andrewwatkins.mastermind.data.ColourCode;
import uk.me.andrewwatkins.mastermind.data.MastermindHandler;

public class GuiMain implements MouseListener, ComponentListener {
	
	//Dimension constants
	private static final int PREF_WIDTH = 800;
	private static final int PREF_HEIGHT = 600;
	
	//Game size constants
	private static final int DEFAULT_XSIZE = 12;
	private static final int DEFAULT_YSIZE = 4;
	private static final int DEFAULT_COLOURSIZE = 6;
	
	//Game size variables
	private int maxXSize = 14;
	private int maxYSize = 8;
	private int maxColourSize = 12;
	
	//Game selection variables
	private JSpinner xSizeSpinner;
	private JSpinner ySizeSpinner;
	private JSpinner colourSizeSpinner;
	private JCheckBox codebreakerCheckbox;
	private JButton startButton;
	private int xSize;
	private int ySize;
	private int colourSize;
	private boolean guesser;
	
	//Game panel variables
	private MarkSlotBoard markslots;
	private SlotBoard slots;
	private SlotBoard codeSlots;
	private ColourPicker picker;
	private JButton readyButton;
	private ColourCode curColour = ColourCode.EMPTY;
	private int curColumn = 0;
	
	private MastermindHandler mmHandler;
	
	//Frame variables
	private JFrame frame;
	private JPanel menuPanel;
	private JPanel mastermindPanel;
	
	

	public GuiMain() {
		
		frame = initialiseFrame();
		
		menuPanel = initialisePanel();
		createSelectionPanel(menuPanel);
		
		frame.add(menuPanel);
		
		frame.setVisible(true);
	}


	private JFrame initialiseFrame() {
		JFrame frame = new JFrame("Complete Mastermind");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		this.maxXSize = (Math.min(1600, screenSize.width)-300)/45;
		this.maxYSize = (Math.min(900, screenSize.height)-300)/55;
		
		frame.setMaximumSize(new Dimension(this.maxXSize, this.maxYSize));
		frame.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
		frame.setLayout(null);
		
		frame.pack();
		
		reposition(frame);
		
		frame.addComponentListener(this);
		return frame;
	}
	
	private void reposition(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (screenSize.width - frame.getSize().width)/2;
		int yPos = (screenSize.height - frame.getSize().height)/2;
		
		frame.setLocation(xPos, yPos);
	}


	private JPanel initialisePanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, PREF_WIDTH, PREF_HEIGHT);
		panel.setLayout(null);
		return panel;
	}
	
	private void createSelectionPanel(JPanel panel) {
		xSizeSpinner = null;
		ySizeSpinner = null;
		colourSizeSpinner = null;
		
		int baseXPos = 50;
		int baseYPos = 50;
		
		int yPos = baseYPos;
		JLabel xSizeLabel = new JLabel("Number of Guesses");
		xSizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_XSIZE, 1, maxXSize, 1));
		xSizeLabel.setLabelFor(xSizeSpinner);
		
		this.setPosition(xSizeLabel, baseXPos, yPos);
		this.setPosition(xSizeSpinner, xSizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(xSizeLabel);
		panel.add(xSizeSpinner);
		
		yPos += xSizeLabel.getPreferredSize().height + 10;
		
		JLabel ySizeLabel = new JLabel("Length of Code");
		ySizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_YSIZE, 1, maxYSize, 1));
		ySizeLabel.setLabelFor(ySizeSpinner);
		
		this.setPosition(ySizeLabel, baseXPos, yPos);
		this.setPosition(ySizeSpinner, ySizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(ySizeLabel);
		panel.add(ySizeSpinner);
		
		yPos += ySizeLabel.getPreferredSize().height + 10;
		
		JLabel colourSizeLabel = new JLabel("Number of Colours");
		colourSizeSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_COLOURSIZE, 1, maxColourSize, 1));
		colourSizeLabel.setLabelFor(colourSizeSpinner);
		
		this.setPosition(colourSizeLabel, baseXPos, yPos);
		this.setPosition(colourSizeSpinner, colourSizeLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(colourSizeLabel);
		panel.add(colourSizeSpinner);
		
		yPos += colourSizeLabel.getPreferredSize().height + 10;
		
		JLabel codebreakerLabel = new JLabel("Computer as Codebreaker?");
		codebreakerCheckbox = new JCheckBox();
		codebreakerLabel.setLabelFor(codebreakerCheckbox);
		
		this.setPosition(codebreakerLabel, baseXPos, yPos);
		this.setPosition(codebreakerCheckbox, codebreakerLabel.getPreferredSize().width + baseXPos + 5, yPos);
		
		panel.add(codebreakerLabel);
		panel.add(codebreakerCheckbox);
		
		yPos += codebreakerLabel.getPreferredSize().height + 10;
		
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
		
		if (this.guesser) {
			slots.setColumnCovered(0, true);
		}
		
		panel.add(slots);
		
		codeSlots = new SlotBoard(1, ySize, (xSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1, false);
		
		panel.add(codeSlots);
		
		picker = new ColourPicker(colourSize, 0, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20);
		
		panel.add(picker);
		
		readyButton = new JButton("Ready");
		
		this.setPosition(readyButton, (colourSize*ColourSlot.WIDTH)+20, markslots.getHeight()+1+(ySize*ColourSlot.HEIGHT)+20);
		
		panel.add(readyButton);
		
		panel.setBounds(0, 0, 1600, 900);
		
		frame.setPreferredSize(new Dimension(Math.max(PREF_WIDTH, Math.min(this.maxXSize*45+300, (this.xSize+1)*45+100)), Math.max(PREF_HEIGHT, Math.min(this.maxXSize*55+300, this.ySize*55+200))));
		frame.pack();
		this.reposition(frame);
		
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
			this.guesser = codebreakerCheckbox.isSelected();
			
			System.out.println(this.guesser);
			
			frame.remove(menuPanel);
			
			mastermindPanel = initialisePanel();
			createMastermindPanel(mastermindPanel);
			
			mastermindPanel.setLayout(null);
			
			frame.add(mastermindPanel);
			
			frame.repaint();
			
			mmHandler = new MastermindHandler(this, this.ySize, this.colourSize, this.codeSlots, this.guesser);
			
			startButton.removeMouseListener(this);
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
			if (slots.isColumnFull(this.curColumn)) {
				mmHandler.setGuess(slots.getColumn(curColumn));
				mmHandler.compare(markslots, curColumn);
				
				curColumn++;
				slots.setColumnCovered(curColumn, false);
			}
			return;
		}
	}
	
	public void checkWin(int poscol) {
		if (poscol == this.ySize) {
			frame.remove(this.mastermindPanel);
			
			reset();
			
			frame.repaint();
		}
	}


	@Override
	public void componentHidden(ComponentEvent e) {
		
	}


	@Override
	public void componentMoved(ComponentEvent e) {
		
	}


	@Override
	public void componentResized(ComponentEvent e) {
		frame.repaint();
		
		if (mastermindPanel != null) {
			mastermindPanel.repaint();
			this.picker.repaint();
			this.readyButton.repaint();
		}
	}


	@Override
	public void componentShown(ComponentEvent e) {
		
	}
	
	public void reset() {
		menuPanel = null;
		mastermindPanel = null;
		
		menuPanel = initialisePanel();
		createSelectionPanel(menuPanel);
		
		frame.add(menuPanel);
		
		slots.removeMouseListener(this);
		picker.removeMouseListener(this);
		readyButton.removeMouseListener(this);
		
		this.curColumn = 0;
	}

}
