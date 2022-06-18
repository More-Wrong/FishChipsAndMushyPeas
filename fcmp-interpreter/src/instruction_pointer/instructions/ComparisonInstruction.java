package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class ComparisonInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		StackElement element1 = ip.getCurrentStack().pop();
		StackElement element2 = ip.getCurrentStack().pop();
		boolean result = false;
		switch (instruction) {
		case '=':
			result = element2.isEqualTo(element1);
			break;
		case '(':
			result = element2.isLessThan(element1);
			break;
		case ')':
			result = element2.isGreaterThan(element1);
			break;
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known comparison instruction");
		}
		ip.getCurrentStack().push(new StackElement(result ? 1 : 0, DataType.BYTE));
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '=', '(', ')' };
	}

}
