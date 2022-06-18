package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class AppendInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell instruction, FishChipsAndMushyPeasIP ip) {
		if (instruction.getDataType() == DataType.BYTE && instruction.getData() == 0) {
			ip.getCurrentStack().push(new StackElement(' ', DataType.BYTE));
		} else {
			ip.getCurrentStack().push(new StackElement(instruction));
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[0];
	}

}
