package instruction_pointer;

import layers.LayerCell;

public interface FishChipsAndMushyPeasInstruction {
	void execute(LayerCell instruction, FishChipsAndMushyPeasIP ip);

	boolean executeInDive();

	byte[] getValidInstructions();
}
