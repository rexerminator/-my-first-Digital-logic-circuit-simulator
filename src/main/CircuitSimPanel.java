package main;
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
import util.CellPosition;
import util.Wire;

public class CircuitSimPanelV2 extends JPanel implements ActionListener {
	//serialVersionUID =============================================================================
	private static final long serialVersionUID = 1L;
	//global variables and objects =================================================================
	final static int PANEL_WIDTH = 1400;
	final static int PANEL_HEIGHT = 600;
	final static int UNIT_SIZE = 50;
	final static int UNITS_ON_X_AXIS = PANEL_WIDTH / UNIT_SIZE;
	final static int UNITS_ON_Y_AXIS = PANEL_HEIGHT / UNIT_SIZE;
	int selectedTool = 0;
	boolean visualOnlyLine = false;
	String[] tools = {"data input cell" , "data cell" , "output cell" , "wire", "wire remover", "wires deleted", "everything deleted"};
	ArrayList<CellPosition> cellPositions = new ArrayList<CellPosition>();
	ArrayList<Wire> wires = new ArrayList<Wire>();
	Point visualOnlyLineFirstPoint;
	Point visualOnlyLineSecondPoint;
	JLabel selectedToolLabel;
	//constructor ==================================================================================
	public CircuitSimPanelV2() {
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
	}
	//methods ======================================================================================
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		checkWiresAndCells();
		drawCells(g);
		drawWires(g);
		if (visualOnlyLine) {
			drawVisualOnlyLine(g);
		}
		else {
		}
	}
	private void drawCells(Graphics g) {
		if (cellPositions.size() > 0) {
			for (CellPosition cp : cellPositions) {
				if (cp.getType() == 0) {
					if (cp.isOn()) {
						g.setColor(Color.lightGray);
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
					else {
						g.setColor(Color.darkGray);
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
				}
				else if (cp.getType() == 1) {
					if (cp.isOn()) {
						g.setColor(Color.green);
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
					else {
						g.setColor(new Color(0, 50, 0));
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
				}
				else if (cp.getType() == 2) {
					if (cp.isOn()) {
						g.setColor(Color.red);
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
					else {
						g.setColor(new Color(50, 0, 0));
						g.fillRect(cp.getX(), cp.getY(), UNIT_SIZE, UNIT_SIZE);
					}
				}
				else {
				}
			}
		}
		else {
		}
	}
	private void drawVisualOnlyLine(Graphics g) {
		g.setColor(Color.red);
		g.drawLine((int)(visualOnlyLineFirstPoint.getX()),
					(int)(visualOnlyLineFirstPoint.getY()),
					(int)(visualOnlyLineSecondPoint.getX()),
					(int)(visualOnlyLineSecondPoint.getY()));
	}
	private void drawWires(Graphics g) {
		g.setColor(Color.red);
		for (Wire w : wires) {
			g.drawLine(w.getFirstX(), w.getFirstY(), w.getSecondX(), w.getSecondY());
		}
	}
	private void checkWiresAndCells() {
		/*#TODO what i have in mind for this piece, is adding turned on or off state to the Wire class as well.
		  		might work out better ...*/
		boolean loopBreaker = true;
		while (loopBreaker) {
			loopBreaker = false;
			for (int i = 0; i < cellPositions.size(); i++) {
				if (cellPositions.get(i).getType() == 0) {
					if (wires.size() > 0) {
						for (Wire w : wires) {
							if (w.getFirstCompIndex() == i && cellPositions.get(i).isOn()) {
								if (!w.isOn()) {
									w.turnOn();
								}
								else {
								}
							}
							else if (w.getFirstCompIndex() == i && !cellPositions.get(i).isOn()) {
								if (w.isOn()) {
									w.turnOff();
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
					if (wires.size() > 0) {
						for (Wire w : wires) {
							if (w.getFirstCompIndex() == i && cellPositions.get(i).isOn()) {
								if (!w.isOn()) {
									w.turnOn();
									loopBreaker = true;
								}
								else {
								}
							}
							else if (w.getFirstCompIndex() == i && !cellPositions.get(i).isOn()) {
								if (w.isOn()) {
									w.turnOff();
									loopBreaker = true;
								}
								else {
								}
							}
							else if (w.getSecondCompIndex() == i && w.isOn() && !cellPositions.get(i).isOn()) {
								cellPositions.get(i).turnOn();
								loopBreaker = true;
							}
							else if (w.getSecondCompIndex() == i && !w.isOn()) {
								if (cellPositions.get(i).getWiresNum() <= 1) {
									if (cellPositions.get(i).isOn()) {
										cellPositions.get(i).turnOff();
										loopBreaker = true;
									}
									else {
									}
								}
								else {
									if (cellPositions.get(i).isOn() && !shouldBeOn(i)) {
										cellPositions.get(i).turnOff();
									}
									else {
									}
								}
							}
						}
					}
				}
				if (cellPositions.get(i).getType() != 0 && cellPositions.get(i).getWiresNum() == 0 && cellPositions.get(i).isOn()) {
					cellPositions.get(i).turnOff();
					loopBreaker = true;
				}
				else {
				}
			}
		}
		/*loopBreaker = true;
		while (loopBreaker) {
			loopBreaker = false;
			for (Wire w : wires) {
				if (cellPositions.get(w.getFirstCompIndex()).isOn() && !cellPositions.get(w.getSecondCompIndex()).isOn()) {
					cellPositions.get(w.getSecondCompIndex()).turnOn();
					loopBreaker = true;
				}
				else if (!cellPositions.get(w.getFirstCompIndex()).isOn() && cellPositions.get(w.getSecondCompIndex()).isOn()) {
					cellPositions.get(w.getSecondCompIndex()).turnOff();
					loopBreaker = true;
				}
				else {
				}
			}
		}*/
	}
	public void checkLeftClickedCell(int x, int y) {
		boolean blankCheck = true;
		if (cellPositions.size() > 0) {
			for (int i = 0; i < cellPositions.size(); i++) {
				if (x > cellPositions.get(i).getX() - UNIT_SIZE &&
					y > cellPositions.get(i).getY() - UNIT_SIZE &&
					x < cellPositions.get(i).getX() + (UNIT_SIZE * 2) &&
					y < cellPositions.get(i).getY() + (UNIT_SIZE * 2)) {
					blankCheck = false;
					if (x > cellPositions.get(i).getX() &&
							y > cellPositions.get(i).getY() &&
							x < cellPositions.get(i).getX() + UNIT_SIZE &&
							y < cellPositions.get(i).getY() + UNIT_SIZE) {
						if (cellPositions.get(i).getType() == 0 && selectedTool == 0) {
							if (!cellPositions.get(i).isOn()) {
								cellPositions.get(i).turnOn();
							}
							else {
								cellPositions.get(i).turnOff();
							}
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
			if (blankCheck) {
				addCell(x, y);
			}
			else {
			}
		}
		else {
			addCell(x, y);
		}
		repaint();
	}
	private void checkRightClickedCell(int x, int y) {
		if (cellPositions.size() > 0) {
			for (int i = 0; i < cellPositions.size(); i++) {
				if (x > cellPositions.get(i).getX() &&
					y > cellPositions.get(i).getY() &&
					x < cellPositions.get(i).getX() + UNIT_SIZE &&
					y < cellPositions.get(i).getY() + UNIT_SIZE) {
					boolean wasWired = false;
					for (int j = wires.size() - 1; j >= 0; j--) {
						if (wires.get(j).getFirstCompIndex() == i) {
							wasWired = true;
							if (selectedTool != 4) {
								cellPositions.get(wires.get(j).getSecondCompIndex()).subWire();
								wires.remove(j);
								break;
							}
							else {
								for (int k = 0; k < wires.size(); k++) {
									if (wires.get(k).getFirstCompIndex() == i) {
										cellPositions.get(wires.get(k).getSecondCompIndex()).subWire();
										wires.remove(k);
										k--;
									}
									else {
									}
								}
								break;
							}
						}
						else if (wires.get(j).getSecondCompIndex() == i) {
							wasWired = true;
						}
						else {
						}
					}
					if (!wasWired) {
						cellPositions.remove(i);
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
		repaint();
	}
	public void checkMouseDrag(int x1, int y1, int x2, int y2) {
		boolean loopBreaker = false;
		if (cellPositions.size() > 0) {
			for  (CellPosition cp1 : cellPositions) {
				if (!loopBreaker) {
					if (x1 > cp1.getX() &&
						y1 > cp1.getY() &&
						x1 < cp1.getX() + UNIT_SIZE &&
						y1 < cp1.getY() + UNIT_SIZE) {
						for (CellPosition cp2 : cellPositions) {
							if (x2 > cp2.getX() &&
								y2 > cp2.getY() &&
								x2 < cp2.getX() + UNIT_SIZE &&
								y2 < cp2.getY() + UNIT_SIZE) {
								if (cp1.getType() != 2 && cp2.getType() != 0) {
									addWire(cellPositions.indexOf(cp1), cellPositions.indexOf(cp2));
									loopBreaker = true;
									break;
								}
								else {
									loopBreaker = true;
									break;
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
					break;
				}
			}
		}
		else {
		}
		repaint();
	}
	private boolean shouldBeOn(int i) {
		boolean result = false;
		for (Wire w : wires) {
			if (!result && w.isOn() && w.getSecondCompIndex() == i) {
				result = true;
			}
			else {
			}
		}
		return result;
	}
	private void addCell(int x, int y) {
		cellPositions.add(new CellPosition(x  - (UNIT_SIZE / 2), y  - (UNIT_SIZE / 2), selectedTool));
	}
	public void addWire(int i1, int i2) {
		int x1 = cellPositions.get(i1).getX() + (UNIT_SIZE / 2);
		int y1 = cellPositions.get(i1).getY() + (UNIT_SIZE / 2);
		int x2 = cellPositions.get(i2).getX() + (UNIT_SIZE / 2);
		int y2 = cellPositions.get(i2).getY() + (UNIT_SIZE / 2);
		wires.add(new Wire(i1, i2, x1, y1, x2, y2));
		cellPositions.get(i2).addWire();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//#TODO might want to add timers in the future ... that is if i make clocks ...
	}
	private class CircuitSimMouseAdapter extends MouseAdapter {
		//methods ======================================================================================
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (selectedTool == 3) {
					visualOnlyLineFirstPoint = e.getPoint();
					visualOnlyLineSecondPoint = e.getPoint();
					visualOnlyLine = true;
				}
				else {
					checkLeftClickedCell(e.getX(), e.getY());
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {
				checkRightClickedCell(e.getX(), e.getY());
			}
			else {
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (selectedTool == 3) {
				visualOnlyLineSecondPoint = e.getPoint();
			}
			else {
				if (visualOnlyLine) {
					visualOnlyLine = false;
				}
				else {
				}
			}
			repaint();
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (visualOnlyLine) {
				visualOnlyLine = false;
				checkMouseDrag((int)visualOnlyLineFirstPoint.getX(),
								(int)visualOnlyLineFirstPoint.getY(),
								(int)visualOnlyLineSecondPoint.getX(),
								(int)visualOnlyLineSecondPoint.getY());
			}
			else {
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
				case '5':
					selectedTool = 4;
					break;
				case '6':
					selectedTool = 5;
					wires.clear();
					break;
				case '7':
					selectedTool = 6;
					wires.clear();
					cellPositions.clear();
					break;
				default:
			}
			selectedToolLabel.setText(tools[selectedTool]);
			repaint();
		}
	}
}
