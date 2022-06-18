package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class ValueInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction >= '0' && instruction <= '9') {
			ip.getCurrentStack().push(new StackElement((byte) (instruction - '0'), DataType.BYTE));
		} else if (instruction >= 'a' && instruction <= 'z') {
			ip.getCurrentStack().push(new StackElement((byte) (instruction - 'a' + 10), DataType.BYTE));
		} else {
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known value instruction");
		}

	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		byte[] values = new byte[16];
		for (int i = 0; i < 10; i++) {
			values[i] = (byte) ('0' + i);
		}
		for (int i = 0; i < 6; i++) {
			values[i + 10] = (byte) ('a' + i);
		}
		return values;
	}
}
