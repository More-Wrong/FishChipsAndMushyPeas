package instruction_pointer;

import instruction_pointer.instructions.AppendInstruction;
import instruction_pointer.instructions.ArithmeticInstruction;
import instruction_pointer.instructions.CellRWInstruction;
import instruction_pointer.instructions.ComparisonInstruction;
import instruction_pointer.instructions.DirectionInstruction;
import instruction_pointer.instructions.DiveInstruction;
import instruction_pointer.instructions.IoInstruction;
import instruction_pointer.instructions.JumpInstruction;
import instruction_pointer.instructions.LayerInstruction;
import instruction_pointer.instructions.MetaStackInstruction;
import instruction_pointer.instructions.NothingInstruction;
import instruction_pointer.instructions.SkipInstruction;
import instruction_pointer.instructions.StackInstruction;
import instruction_pointer.instructions.StringInstruction;
import instruction_pointer.instructions.ThreadingInstruction;
import instruction_pointer.instructions.TypeInstruction;
import instruction_pointer.instructions.ValueInstruction;
import layers.LayerCell;

public class FishChipsAndMushyPeasInstructionInterpreter {
	private static FishChipsAndMushyPeasInstruction[] instructions = generateInstrArray();
	private static StringInstruction stringInstruction = new StringInstruction();
	private static NothingInstruction nothingInstruction = new NothingInstruction();
	private static AppendInstruction appendInstruction = new AppendInstruction();

	private static FishChipsAndMushyPeasInstruction[] generateInstrArray() {
		FishChipsAndMushyPeasInstruction[] possibleInstructions = new FishChipsAndMushyPeasInstruction[] { new ArithmeticInstruction(),
				new CellRWInstruction(), new ComparisonInstruction(), new DirectionInstruction(), new DiveInstruction(),
				new IoInstruction(), new JumpInstruction(), new LayerInstruction(), new MetaStackInstruction(),
				new SkipInstruction(), new StackInstruction(), new StringInstruction(), new ThreadingInstruction(),
				new TypeInstruction(), new ValueInstruction() };
		FishChipsAndMushyPeasInstruction[] instructions = new FishChipsAndMushyPeasInstruction[128];
		for (int i = 0; i < instructions.length; i++) {
			instructions[i] = nothingInstruction;
		}
		for (FishChipsAndMushyPeasInstruction instr : possibleInstructions) {
			for (byte b : instr.getValidInstructions()) {
				instructions[b] = instr;
			}
		}
		return instructions;

	}

	public static FishChipsAndMushyPeasInstruction parseInstruction(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (ip.skipNext()) {
			ip.setSkipNext(false);
			return nothingInstruction;
		}
		if (ip.isInString1() || ip.isInString2()) {
			if (!cell.getDataType().isInteger()) {
				return appendInstruction;
			} else if (cell.getData() == '"' && ip.isInString1()) {
				return stringInstruction;
			} else if (cell.getData() == '\'' && ip.isInString2()) {
				return stringInstruction;
			} else {
				return appendInstruction;
			}
		}
		if (ip.isDiving()) {
			if (!cell.getDataType().isInteger() || cell.getData() < 0 || cell.getData() >= 128) {
				return nothingInstruction;
			} else {
				if (instructions[(int) cell.getData()].executeInDive()) {
					return instructions[(int) cell.getData()];
				} else {
					return nothingInstruction;
				}
			}
		}
		if (!cell.getDataType().isInteger() || cell.getData() < 0 || cell.getData() >= 128) {
			throw new IllegalStateException("Attempting to execute cell which is not an instruction");
		} else {
			FishChipsAndMushyPeasInstruction instr = instructions[(int) cell.getData()];
			if (instr != null) {
				return instr;
			} else {
				return nothingInstruction;
			}
		}
	}
}
