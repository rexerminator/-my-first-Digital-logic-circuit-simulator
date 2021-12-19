import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CircuitSimPanel extends JPanel implements ActionListener {
	//serialVersionUID =============================================================================
	private static final long serialVersionUID = 1L;
	//global variables and objects =================================================================
	final static int PANEL_WIDTH = 1400;
	final static int PANEL_HEIGHT = 600;
	final static int UNIT_SIZE = 50;
	final static int DELAY = 1000;
	final static int UNITS_ON_X_AXIS = PANEL_WIDTH / UNIT_SIZE;
	final static int UNITS_ON_Y_AXIS = PANEL_HEIGHT / UNIT_SIZE;
	int[][] cell = new int[UNITS_ON_X_AXIS][UNITS_ON_Y_AXIS];	//0 for black empty cell,
																//1 for dark gray turned off data input cell,
																//2 for light gray turned on data input cell,
																//3 for dark green turned off data cell,
																//4 for green turned on data cell,
	int[] wirePoint = {-1, 0};
	int selectedTool = 0;
	int numberOfWires = 0;
	boolean visualOnlyLine = false;
	boolean started = false;
	boolean running = false;
	String[] tools = {"data input cell" , "data cell" , "wire", "wire remover"};
	ArrayList<Integer> wiresX = new ArrayList<Integer>();
	ArrayList<Integer> wiresY = new ArrayList<Integer>();
	Point visualOnlyLineFirstPoint;
	Point visualOnlyLineSecondPoint;
	JLabel selectedToolLabel;
	Timer timer;
	//constructor ==================================================================================
	public CircuitSimPanel() {
		for (int[] i : cell) {
			for (int j : i) {
				j = 0;
			}
		}
		CircuitSimMouseAdapter mouseAdapter = new CircuitSimMouseAdapter();
		selectedToolLabel = new JLabel(tools[selectedTool]);
		selectedToolLabel.setBounds(PANEL_WIDTH - PANEL_WIDTH / 5, PANEL_HEIGHT - 50, PANEL_WIDTH / 5, 50);
		selectedToolLabel.setForeground(Color.green);
		selectedToolLabel.setFont(new Font("Sitka", Font.PLAIN, 24));
		this.setPreferredSize(new Dimension(PANEL_WIDTH + 2, PANEL_HEIGHT + 2));
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addKeyListener(new CircuitSimKeyAdapter());
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.add(selectedToolLabel);
		this.setLayout(null);
		timer = new Timer(DELAY, this);
		timer.start();
	}
	//methods ======================================================================================
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
		checkCells();
		drawCells(g);
		drawWires(g);
		if (visualOnlyLine) {
			drawVisualOnlyLine(g);
		}
	}
	private void drawGrid(Graphics g) {
		g.setColor(new Color(75, 50, 75));
		for (int i = 0; i <= UNITS_ON_X_AXIS; i++) {
			g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, PANEL_HEIGHT);
		}
		for (int i = 0; i <= UNITS_ON_Y_AXIS; i++) {
			g.drawLine(0, i * UNIT_SIZE, PANEL_WIDTH, i * UNIT_SIZE);
		}
	}
	private void drawWires(Graphics g) {
	/*	if (wiresX.size() != 0) {
			for (int i = 0; i < wiresX.size(); i++) {
				if (i % 2 == 0) {
					g.setColor(Color.red);
					g.drawLine((wiresX.get(i) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresY.get(i) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresX.get(i + 1) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresY.get(i + 1) * UNIT_SIZE) + (UNIT_SIZE / 2));
				}
				else {
				}
			}
		}	*/
		int originalNumberOfWires = numberOfWires;
		g.setColor(Color.red);
		while (numberOfWires > 0) {
			g.drawLine((wiresX.get(numberOfWires - 2) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresY.get(numberOfWires - 2) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresX.get(numberOfWires - 1) * UNIT_SIZE) + (UNIT_SIZE / 2), (wiresY.get(numberOfWires - 1) * UNIT_SIZE) + (UNIT_SIZE / 2));
			numberOfWires -= 2;
		}
		numberOfWires = originalNumberOfWires;
	}
	private void drawCells(Graphics g) {
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[i].length; j++) {
				switch (cell[i][j]) {
					case 0:
						g.setColor(Color.black);
						g.fillRect(i * UNIT_SIZE + 1, j * UNIT_SIZE + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
						break;
					case 1:
						g.setColor(Color.darkGray);
						g.fillRect(i * UNIT_SIZE + 1, j * UNIT_SIZE + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
						break;
					case 2:
						g.setColor(Color.lightGray);
						g.fillRect(i * UNIT_SIZE + 1, j * UNIT_SIZE + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
						break;
					case 3:
						g.setColor(new Color(0, 50, 0));
						g.fillRect(i * UNIT_SIZE + 1, j * UNIT_SIZE + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
						break;
					case 4:
						g.setColor(Color.green);
						g.fillRect(i * UNIT_SIZE + 1, j * UNIT_SIZE + 1, UNIT_SIZE - 1, UNIT_SIZE - 1);
						break;
				}
			}
		}
	}
	private void checkCells() {
		boolean done;
		boolean finished = false;
		do {
			done = true;
			for (int i = 0; i < UNITS_ON_X_AXIS && !finished; i++) {
				for (int j = 0; j < UNITS_ON_Y_AXIS && !finished; j++) {
					if (cell[i][j] == 4) {
						checkWiresAndTurnOff(i, j);
						repaint();
						finished = true;
						done = false;
					}
					else {
					}
					if (cell[i][j] == 2 || cell[i][j] == 4) {
						checkWiresAndTurnON(i, j);
						repaint();
						finished = true;
						done = false;
					}
					else {
					}
				}
			}
			finished = true;
		} while (!done);
	}
	private void checkWiresAndTurnON(int x, int y) {
		for (int i = 0; i < wiresX.size(); i++) {
			if (i % 2 == 0) {
				if (wiresX.get(i) == x && wiresY.get(i) == y) {
					if (cell[wiresX.get(i + 1)][wiresY.get(i + 1)] == 3) {
						cell[wiresX.get(i + 1)][wiresY.get(i + 1)] = 4;
					}
					else {
					}
				}
				else {
				}
			}
			else {
			}
		}
	}
	private void checkWiresAndTurnOff(int x, int y) {
		for (int i = 0; i < wiresX.size(); i++) {
			if (i % 2 != 0) {
				if (wiresX.get(i) == x && wiresY.get(i) == y) {
					if (cell[wiresX.get(i - 1)][wiresY.get(i - 1)] == 1 ||
						cell[wiresX.get(i - 1)][wiresY.get(i - 1)] == 3) {
						cell[wiresX.get(i)][wiresY.get(i)] = 3;
					}
					else {
					}
				}
				else {
				}
			}
			else {
			}
		}
	}
	private void drawVisualOnlyLine(Graphics g) {
		g.setColor(Color.red);
		g.drawLine((int)(visualOnlyLineFirstPoint.getX()), (int)(visualOnlyLineFirstPoint.getY()), (int)(visualOnlyLineSecondPoint.getX()), (int)(visualOnlyLineSecondPoint.getY()));
	}
	public void addWire(int x1, int y1, int x2, int y2) {
		int firstUnitX = -1;
		int firstUnitY = -1;
		int secondUnitX = -1;
		int secondUnitY = -1;
		for (int i = 0; i < UNITS_ON_X_AXIS; i++) {
			for (int j = 0; j < UNITS_ON_Y_AXIS; j++) {
				if (x1 >= i * UNIT_SIZE &&
					x1 <= (i * UNIT_SIZE) + UNIT_SIZE &&
					y1 >= j * UNIT_SIZE &&
					y1 <= (j * UNIT_SIZE) + UNIT_SIZE) {
					firstUnitX = i;
					firstUnitY = j;
				}
				else if (x2 >= i * UNIT_SIZE &&
						 x2 <= (i * UNIT_SIZE) + UNIT_SIZE &&
					 	 y2 >= j * UNIT_SIZE &&
						 y2 <= (j * UNIT_SIZE) + UNIT_SIZE) {
					secondUnitX = i;
					secondUnitY = j;
				}
				else {
				}
			}
		}
		if (firstUnitX != -1 && firstUnitY != -1 && secondUnitX != -1 && secondUnitY != -1) {
			if (cell[firstUnitX][firstUnitY] != 0) {
				if (firstUnitX == secondUnitX && firstUnitY == secondUnitY) {
				}
				else {
					wiresX.add(firstUnitX);
					wiresX.add(secondUnitX);
					wiresY.add(firstUnitY);
					wiresY.add(secondUnitY);
					numberOfWires += 2;
				}
			}
			else {
			}
		}
		else {
		}
	}
	public void removeWire(int x, int y) {
		if (cell[x][y] != 0) {
			if (wiresX.size() != 0) {
				for (int i = 0; i < wiresX.size(); i++) {
					if (i % 2 == 0) {
						if (wiresX.get(i) == x && wiresY.get(i) == y) {
							wiresX.remove(i);
							wiresX.remove(i);
							wiresY.remove(i);
							wiresY.remove(i);
							numberOfWires -= 2;
						}
						else {
						}
					}
					else {
					}
				}
			}
			else {
			}
		}
		else {
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	private class CircuitSimMouseAdapter extends MouseAdapter {
		//methods ======================================================================================
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				for (int i = 0; i < UNITS_ON_X_AXIS; i++) {
					for (int j = 0; j < UNITS_ON_Y_AXIS; j++) {
						if (e.getX() >= i * UNIT_SIZE &&
							e.getX() <= (i * UNIT_SIZE) + UNIT_SIZE &&
							e.getY() >= j * UNIT_SIZE &&
							e.getY() <= (j * UNIT_SIZE) + UNIT_SIZE) {
							if (selectedTool == 0) {
								if (cell[i][j] == 0) {
									cell[i][j] = 1;
								}
								else if (cell[i][j] == 1) {
									cell[i][j] = 2;
								}
								else if (cell[i][j] == 2) {
									cell[i][j] = 1;
								}
								else {
								}
							}
							else if (selectedTool == 1) {
								if (cell[i][j] == 0) {
									cell[i][j] = 3;
								}
								else {
								}
							}
							else if (selectedTool == 3) {
								removeWire(i, j);
							}
							else {
							}
						}
					}
				}
				repaint();
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (selectedTool == 2) {
					visualOnlyLineFirstPoint = e.getPoint();
					visualOnlyLineSecondPoint = e.getPoint();
					visualOnlyLine = true;
				}
				repaint();
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (selectedTool == 2) {
				visualOnlyLineSecondPoint = e.getPoint();
			}
			repaint();
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (visualOnlyLine) {
				visualOnlyLine = false;
				int x1 = (int)visualOnlyLineFirstPoint.getX();
				int y1 = (int)visualOnlyLineFirstPoint.getY();
				int x2 = (int)visualOnlyLineSecondPoint.getX();
				int y2 = (int)visualOnlyLineSecondPoint.getY();
				addWire(x1, y1, x2, y2);
			}
		}
	}
	private class CircuitSimKeyAdapter extends KeyAdapter {
		//methods ======================================================================================
		@Override
		public void keyTyped(KeyEvent e) {
			switch (e.getKeyChar()) {
				case '1':
					selectedTool = 0;
					break;
				case '2':
					selectedTool = 1;
					break;
				case '3':
					selectedTool = 2;
					break;
				case '4':
					selectedTool = 3;
					break;
				default:
			}
			selectedToolLabel.setText(tools[selectedTool]);
			repaint();
		}
	}
}
