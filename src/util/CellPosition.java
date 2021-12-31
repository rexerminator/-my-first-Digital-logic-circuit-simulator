package util;

public class CellPosition extends Positions {
	//global variables and objects =================================================================
	int type;
	int numOfWiresToThisCellNum = 0;
	boolean on = false;
	//constructor ==================================================================================
	public CellPosition(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	//methods ======================================================================================
	public boolean isOn() {
		return this.on;
	}
	public int getType() {
		return this.type;
	}
	public int getWiresNum() {
		return this.numOfWiresToThisCellNum;
	}
	public void addWire() {
		this.numOfWiresToThisCellNum++;
	}
	public void subWire() {
		this.numOfWiresToThisCellNum--;
	}
	public void turnOn() {
		this.on = true;
	}
	public void turnOff() {
		this.on = false;
	}
}
