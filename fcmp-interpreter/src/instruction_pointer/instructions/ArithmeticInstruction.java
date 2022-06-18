package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class ArithmeticInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();
		StackElement element1 = ip.getCurrentStack().pop();
		StackElement element2 = ip.getCurrentStack().pop();
		switch (instruction) {
		case '+':
			ip.getCurrentStack().push(element2.add(element1));
			break;
		case '-':
			ip.getCurrentStack().push(element2.subtract(element1));
			break;
		case ',':
			ip.getCurrentStack().push(element2.divideBy(element1));
			break;
		case '*':
			ip.getCurrentStack().push(element2.multiply(element1));
			break;
		case '%':
			ip.getCurrentStack().push(element2.modulo(element1));
			break;
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known arithmetic instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '+', '-', ',', '*', '%' };
	}

}
