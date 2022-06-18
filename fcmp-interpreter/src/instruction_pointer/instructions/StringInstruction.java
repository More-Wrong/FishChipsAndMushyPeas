package instruction_pointer.instructions;

import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class StringInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction == '"') {
			ip.setInString1(!ip.isInString1());
		} else if (instruction == '\'') {
			ip.setInString2(!ip.isInString2());
		} else {
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known string instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '\'', '"' };
	}

}
