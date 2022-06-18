package instruction_pointer.instructions;

import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class NothingInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
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
