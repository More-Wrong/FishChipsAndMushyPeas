package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class SkipInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction == '!') {
			ip.setSkipNext(true);
		} else if (instruction == '?') {
			StackElement el = ip.getCurrentStack().pop();
			if (el.isZero()) {
				ip.setSkipNext(true);
			}
		} else {
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known skip instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '!', '?' };
	}

}
