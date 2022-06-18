package instruction_pointer.instructions;

import java.util.Deque;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class CellRWInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		Deque<StackElement> currentStack = ip.getCurrentStack();
		StackElement elementY = currentStack.pop();
		StackElement elementX = currentStack.pop();
		switch (instruction) {
		case 'g':
			currentStack.push(new StackElement(
					ip.getCurrentLayer().getCellAt((int) elementX.integerValue(), (int) elementY.integerValue())));
			break;
		case 'G': {
			currentStack.push(new StackElement(ip.getMemorySpace().getLayer((int) currentStack.pop().integerValue())
					.getCellAt((int) elementX.integerValue(), (int) elementY.integerValue())));
			break;
		}
		case 'p':
			ip.getCurrentLayer().setCellAt((int) elementX.integerValue(), (int) elementY.integerValue(),
					currentStack.pop().toLayerCell());
			break;
		case 'P':
			ip.getMemorySpace().getLayer((int) currentStack.pop().integerValue()).setCellAt(
					(int) elementX.integerValue(), (int) elementY.integerValue(), currentStack.pop().toLayerCell());
			break;
		case 'y': {
			currentStack.push(new StackElement(
					ip.getCurrentLayer().getCellAt((int) elementX.integerValue(), (int) elementY.integerValue())));
			incrementPosition(ip, elementY, elementX);
			break;
		}
		case 'Y': {
			StackElement elementLayer = currentStack.pop();

			currentStack.push(new StackElement(ip.getMemorySpace().getLayer((int) elementLayer.integerValue())
					.getCellAt((int) elementX.integerValue(), (int) elementY.integerValue())));
			currentStack.push(elementLayer);
			incrementPosition(ip, elementY, elementX);
			break;
		}
		case 'w': {
			ip.getCurrentLayer().setCellAt((int) elementX.integerValue(), (int) elementY.integerValue(),
					currentStack.pop().toLayerCell());
			incrementPosition(ip, elementY, elementX);
			break;
		}
		case 'W': {
			StackElement elementLayer = currentStack.pop();
			ip.getMemorySpace().getLayer((int) elementLayer.integerValue()).setCellAt((int) elementX.integerValue(),
					(int) elementY.integerValue(), currentStack.pop().toLayerCell());
			currentStack.push(elementLayer);
			incrementPosition(ip, elementY, elementX);
			break;
		}
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known cell r/w instruction");
		}
	}

	private void incrementPosition(FishChipsAndMushyPeasIP ip, StackElement elementY, StackElement elementX) {
		switch (ip.getDirection()) {
		case NORTH: {
			ip.getCurrentStack().push(elementX);
			ip.getCurrentStack().push(new StackElement(elementY.integerValue() - 1, DataType.INT32));
			break;
		}
		case EAST: {
			ip.getCurrentStack().push(new StackElement(elementX.integerValue() + 1, DataType.INT32));
			ip.getCurrentStack().push(elementY);
			break;
		}
		case SOUTH: {
			ip.getCurrentStack().push(elementX);
			ip.getCurrentStack().push(new StackElement(elementY.integerValue() + 1, DataType.INT32));
			break;
		}
		case WEST: {
			ip.getCurrentStack().push(new StackElement(elementX.integerValue() - 1, DataType.INT32));
			ip.getCurrentStack().push(elementY);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + ip.getDirection());
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'g', 'G', 'p', 'P', 'y', 'Y', 'w', 'W' };
	}

}
