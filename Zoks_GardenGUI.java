/* Meena Zoks
 * Creates a garden made out of a grid and flower images
 * that change everyday (step) based on the surrounding flowers
 */

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.swing.border.*;

public class Zoks_GardenGUI extends JFrame implements ActionListener{

	private BufferedImage image;
		
	public  final int[] HORZDISP = {1,1,0,-1,-1,-1,1,0};
	public  final int[] VERTDISP = {0,1,1,1,-1,0,-1,-1};
	
	public final int SIZE = 8;
	
	private PicPanel[][] allPanels;
	private JPanel grid;
	
	public Zoks_GardenGUI(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//sets size and background color
		setSize(1000, 850);
		setLayout(null);
		setTitle("Garden");
		getContentPane().setBackground(new Color(18, 145, 15));
		
		//loads flower image
		try {
			image = ImageIO.read(new File("fireflower.jpg"));

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Could not read in the pic");
			System.exit(0);
		}		
		
		//creates grid panel
		grid = new JPanel();
		grid.setLayout(new GridLayout(SIZE, SIZE, 5, 5));
		grid.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(4.0f), Color.black));
		grid.setBackground(Color.black);
		grid.setBounds(80, 60, 700, 700);

		//loads flower locations
		allPanels = new PicPanel[SIZE][SIZE];
		Scanner keyboard = new Scanner (System.in);
		
		Scanner inFile = null;
		try {
			inFile = new Scanner (new File ("flowerLocs.txt"));
			
		}catch(FileNotFoundException e) {
			System.out.println("File not found.");
			System.exit(-1);
		}

		while(inFile.hasNextInt())
			allPanels[inFile.nextInt()][inFile.nextInt()] = new PicPanel();
		
		//loads images on to the grid panel based on flower locs
		for(int row=0; row<SIZE; row++) {
			
			for(int col=0; col<SIZE; col++) {
				
				if(allPanels[row][col] != null)
					allPanels[row][col].addFlower();
				
				else
					allPanels[row][col] = new PicPanel();
				
				grid.add(allPanels[row][col]);
			}
		}
		
		//creates step button
		JButton step = new JButton("Step");
		step.addActionListener(this);
		step.setBounds(820, 375, 150, 100);
		
		add(grid);
		add(step);
		setVisible(true);
	}
	
	//called when step button is pressed
	public void actionPerformed(ActionEvent ae){
		
		int[][] newDay = new int[allPanels.length][allPanels.length];
		
		//determines how to update panels based on previous day
		for(int row=0; row<SIZE; row++) {
			
			for(int col=0; col<SIZE; col++) {
				
				int neighbors = countNeighbors(row, col);
				
				//checks if flower will die
				if(allPanels[row][col].hasFlower) {
					
					if(neighbors>3 || neighbors<2)
						newDay[row][col] = -1;
				}
				
				//checks if flower will grow
				else {
					
					if(neighbors==3)
						newDay[row][col] = 1;
				}
			}
		}
		
		Border redBorder = BorderFactory.createStrokeBorder(new BasicStroke(4.0f), Color.red);
		
		//updates panels to reflect the new day
		for(int row=0; row<SIZE; row++) {
			
			for(int col=0; col<SIZE; col++) {
				
				if(newDay[row][col] != 0) {
					
					//draws border on panels that have changed
					allPanels[row][col].setBorder(redBorder);
					
					//adds or removes flower
					if(newDay[row][col] == 1)
						allPanels[row][col].addFlower();
					
					else
						allPanels[row][col].removeFlower();
				}
				
				else
					allPanels[row][col].setBorder(null);
			}
		}
	}
	
	//counts number of flowers surrounding one cell
	public int countNeighbors(int currentRow, int currentCol) {
		int neighbors=0;
		
		for(int displc=0; displc<HORZDISP.length; displc++) {
				
			int row=currentRow+VERTDISP[displc];
			int col=currentCol+HORZDISP[displc];
				
			//checks if cell is in bounds and contains a flower
			if(row>=0 && row<allPanels.length && col>=0 && col<allPanels.length)
						
				if(allPanels[row][col].hasFlower)
					neighbors++;
		}
		
		return neighbors;
	}

	class PicPanel extends JPanel{
		
		private boolean hasFlower = false;

		public PicPanel(){
			setBackground(new Color(121,30,3));
		}

		public void addFlower(){
			hasFlower = true;
			repaint();
		}
		
		public void removeFlower(){
			hasFlower = false;
			repaint();
		}

		//this will draw the image
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			if(hasFlower)
				g.drawImage(image,0,0,this);
		}
	}
	
	public static void main (String[] args){
		new Zoks_GardenGUI();
	}
}
