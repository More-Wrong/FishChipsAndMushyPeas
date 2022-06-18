package instruction_pointer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import instruction_pointer.instructions.JumpInstruction;
import layers.DataType;
import layers.Layer;
import layers.LayerCell;
import main.FishChipsAndMushyPeasMemorySpace;
import main.FishChipsAndMushyPeasThread;

public class FishChipsAndMushyPeasIP {
	private final FishChipsAndMushyPeasMemorySpace memorySpace;
	private FishChipsAndMushyPeasThread thread;
	private Layer currentLayer;
	private int x;
	private int y;
	private Direction direction;
	private List<Deque<StackElement>> stacks;
	private int currentStackIndex;
	private StackElement register = new StackElement(0, DataType.BYTE);
	private boolean hasRegister = false;
	private boolean isInString1 = false;
	private boolean isInString2 = false;
	private boolean isDiving = false;
	private boolean skipNext = false;
	private boolean moveNow = true;

	public FishChipsAndMushyPeasIP(FishChipsAndMushyPeasMemorySpace memorySpace, Layer currentLayer, int x, int y, Direction direction,
			Deque<StackElement> intialStack) {
		this.memorySpace = memorySpace;
		this.currentLayer = currentLayer;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.stacks = new ArrayList<>();

		stacks.add(intialStack);
		currentStackIndex = 0;
	}

	public void exitLayer() {
		new JumpInstruction().exitLayer(this);// I mean, hey worth a shot, right?
	}

	public void executeInstruction() {
		LayerCell cell = currentLayer.getCellAt(x, y);
		try {
			FishChipsAndMushyPeasInstruction instruction = FishChipsAndMushyPeasInstructionInterpreter.parseInstruction(cell, this);
//		if (currentLayer.getLayerId() == 2) {
//		System.out.println(currentLayer.getLayerId() + ": (" + x + ", " + y + ")");
//		}
			instruction.execute(cell, this);
		} catch (Exception e) {
			throw new RuntimeException("failed at position: " + currentLayer.getLayerId() + ": (" + x + ", " + y + ")",
					e);
		}
		if (moveNow) {
			switch (direction) {
			case NORTH:
				y--;
				if (y < 0) {
					y = currentLayer.getMaxY();
				}
				break;
			case EAST:
				x++;
				if (x >= currentLayer.getMaxX()) {
					x = 0;
				}
				break;
			case SOUTH:
				y++;
				if (y >= currentLayer.getMaxY()) {
					y = 0;
				}
				break;
			case WEST:
				x--;
				if (x < 0) {
					x = currentLayer.getMaxX();
				}
				break;
			default:
				break;
			}
		}
		moveNow = true;
	}

	public void doNotMoveNow() {
		this.moveNow = false;
	}

	public FishChipsAndMushyPeasMemorySpace getMemorySpace() {
		return memorySpace;
	}

	public void setThread(FishChipsAndMushyPeasThread thread) {
		this.thread = thread;
	}

	public FishChipsAndMushyPeasThread getThread() {
		return thread;
	}

	public Layer getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(Layer currentLayer) {
		this.currentLayer = currentLayer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Deque<StackElement> getCurrentStack() {
		return stacks.get(currentStackIndex);
	}

	public int getCurrentStackIndex() {
		return currentStackIndex;
	}

	public void setCurrentStackIndex(int currentStack) {
		this.currentStackIndex = currentStack;
	}

	public StackElement getRegister() {
		return register;
	}

	public void setRegister(StackElement register) {
		this.register = register;
	}

	public boolean hasRegister() {
		return hasRegister;
	}

	public void setHasRegister(boolean hasRegister) {
		this.hasRegister = hasRegister;
	}

	public boolean isInString1() {
		return isInString1;
	}

	public void setInString1(boolean isInString1) {
		this.isInString1 = isInString1;
	}

	public boolean isInString2() {
		return isInString2;
	}

	public void setInString2(boolean isInString2) {
		this.isInString2 = isInString2;
	}

	public boolean isDiving() {
		return isDiving;
	}

	public void setDiving(boolean isDiving) {
		this.isDiving = isDiving;
	}

	public List<Deque<StackElement>> getStacks() {
		return stacks;
	}

	public void setSkipNext(boolean skipNext) {
		this.skipNext = skipNext;
	}

	public boolean skipNext() {
		return skipNext;
	}

}
