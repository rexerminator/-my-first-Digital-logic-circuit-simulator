# -my-first-Digital-logic-circuit-simulator
----------------------------------------------------------------------------------------------------------------------------------------

HotKeys that you can use in this app:

keyboard------------------------------

select data input cell :          num1

select data cell :                num2

select data output cell :         num3

select wire tool :                num4

select remove each wire tool :    num5

remove all wires :                num6

remove all cells :                num7

mouse---------------------------------

placing/turning on cells/wires :  Lmb

removing cells/wires :            Rmb

*notes*-------------------------------

-you can't place multiple cells on each other.

-left clicking on a data input cell while the selected tool is data input cell, turns that data input cell on/off

-pressing left mouse button on a cell, dragging the mouse and releasing it on another cell while the selected tool is the wire tool, creates a directional wire from the cell that you pressed the left mouse button on, to the cell that you released the left mouse button on.

-wires are directional, meaning that for example if you wire "cell 1" to "cell 2", if "cell 1" turns on, "cell 2" will also turn on but not vice versa.

-right clicking on a cell while the selected tool isn't remove each wire tool, removes the last wire that starts from that cell, and if the cell doesn't have any wires attached to it, it will be deleted. but if a wire is attached to the cell from another cell, nothing happens.

-right clicking on a cell while the selected tool is remove each wire tool, acts similarl to as if it wasn't, but instead of removing the last wire that started from the cell, it removes every wire that started from the cell.

-as of now, if you make a loop of cells wired to each other and turn one on, those cells will stay turned on as long as their wires are in tact. I'm working on fixing it but that's how they are ... for now ...

----------------------------------------------------------------------------------------------------------------------------------------

---entry 19 December 2021---

13:05
always liked the way that logic worked in computers, thought it would be great to make a logic circuit sim ... riddled with bugs for now ... but ill work on it

17:34
as of right now, the way of checking if a data cell is on or off is that if a data cell or an input data cell prior to the current data cell is on, then current cell becomes on, and its done with a bunch of loops and nested loops which is not the best way of going for it, and i encountered a f*** ton of bugs with it wrong cells turning on. i probably need to make a sprite system instead of nested loops checking each empty cell on the grid in each iteration ...

---entry 31 December 2021---

21:03
alright ... I've changed pretty much every crucial thing, and now the code is a little bit more OOP like. there's still some bugs and crashes happening here and there, and you can still make a loop of cells wired to each other and they will stay turned on, but it's much better than what it was before. oh and also it's not grid based anymore. you can now put cells anywhere on the window as long as there's enough space for the cell to be placed on. in case you haven't took a look in the code yet, there's now a list of controlls in the top of this readme file, and I'll (hopefully remember to) update it as I add new controlls.

