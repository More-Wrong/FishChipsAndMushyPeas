package instruction_pointer.instructions;

import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class DiveInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction == 'u') {
			ip.setDiving(true);
		} else if (instruction == 'U') {
			ip.setDiving(false);
		} else {
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known dive instruction");
		}

	}

	@Override
	public boolean executeInDive() {
		return true;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'u', 'U' };
	}

}
