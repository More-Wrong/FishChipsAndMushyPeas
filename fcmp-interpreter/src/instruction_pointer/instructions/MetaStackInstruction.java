package instruction_pointer.instructions;

import java.util.Deque;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class MetaStackInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		int index = ip.getCurrentStackIndex();
		switch (instruction) {
		case 'z': {
			Deque<StackElement> currentStack = ip.getStacks().get(index);
			ip.getStacks().set(index, ip.getStacks().get(index - 1));
			ip.getStacks().set(index - 1, currentStack);
			break;
		}
		case 'Z': {
			Deque<StackElement> currentStack = ip.getStacks().get(index);
			ip.getStacks().set(index, ip.getStacks().get(index - 1));
			ip.getStacks().set(index - 1, ip.getStacks().get(index - 2));
			ip.getStacks().set(index - 2, currentStack);
			break;
		}
		case 'I':
			ip.setCurrentStackIndex(index + 1);
			break;
		case 'D':
			ip.setCurrentStackIndex(index - 1);
			break;
		case 'L':
			ip.getCurrentStack().push(new StackElement(ip.getStacks().size(), DataType.INT32));
			break;
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known meta-stack instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'z', 'Z', 'L', 'I', 'D' };
	}

}
