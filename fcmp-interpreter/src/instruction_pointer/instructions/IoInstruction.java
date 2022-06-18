package instruction_pointer.instructions;

import java.io.IOException;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class IoInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction == 'i') {
			try {
				ip.getCurrentStack().push(new StackElement(System.in.read(), DataType.INT32));
			} catch (IOException e) {
				throw new RuntimeException("Could not perform read from stdin", e);
			}
		} else if (instruction == 'o') {
			System.out.print((char) (ip.getCurrentStack().pop().integerValue()));
		} else if (instruction == 'n') {
			StackElement value = ip.getCurrentStack().pop();
			switch (value.getDataType()) {
			case BYTE:
				System.out.print((byte) value.integerValue());
				break;
			case INT32:
				System.out.print((int) value.integerValue());
				break;
			case INT64:
				System.out.print(value.integerValue());
				break;
			case SINGLEFLOAT:
			case DOUBLEFLOAT:
				System.out.print(value.doubleValue());
				break;
			}
		} else {
			throw new IllegalArgumentException("The instruction \"" + instruction + "\" is not a known IO instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'i', 'o', 'n' };
	}

}
