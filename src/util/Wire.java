package util;

import java.awt.Color;

public class Wire {
	//global variables and objects =================================================================
	Color wireColor = Color.red; //not implemented colored wires yet
	int firstIndex;
	int secondIndex;
	int firstX;
	int firstY;
	int secondX;
	int secondY;
	boolean on = false;
	//constructor ==================================================================================
	public Wire(int i1, int i2, int x1, int y1, int x2, int y2) {
		//this.wireColor = color; //not implemented colored wires yet
		this.firstIndex = i1;
		this.secondIndex = i2;
		this.firstX = x1;
		this.firstY = y1;
		this.secondX = x2;
		this.secondY = y2;
	}
	//methods ======================================================================================
	public Color getColor() {
		return wireColor;
	}
	public boolean isOn() {
		return this.on;
	}
	public int getFirstCompIndex() {
		return firstIndex;
	}
	public int getSecondCompIndex() {
		return secondIndex;
	}
	public int getFirstX() {
		return firstX;
	}
	public int getFirstY() {
		return firstY;
	}
	public int getSecondX() {
		return secondX;
	}
	public int getSecondY() {
		return secondY;
	}
	public void turnOn() {
		this.on = true;
	}
	public void turnOff() {
		this.on = false;
	}
}
