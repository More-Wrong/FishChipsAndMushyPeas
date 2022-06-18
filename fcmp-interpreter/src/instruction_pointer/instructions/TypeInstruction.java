package instruction_pointer.instructions;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class TypeInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		if (instruction == 't') {
			StackElement type = ip.getCurrentStack().pop();
			StackElement el = ip.getCurrentStack().pop();
			ip.getCurrentStack().push(el.withType(switch ((int) type.integerValue()) {
			case 0 -> DataType.BYTE;
			case 1 -> DataType.INT32;
			case 2 -> DataType.INT64;
			case 3 -> DataType.SINGLEFLOAT;
			case 4 -> DataType.DOUBLEFLOAT;
			default -> throw new IllegalArgumentException("Unknown type code: " + type.integerValue());
			}));
		} else if (instruction == 'T') {
			StackElement el = ip.getCurrentStack().peek();
			ip.getCurrentStack().push(new StackElement(switch (el.getDataType()) {
			case BYTE -> 0;
			case INT32 -> 1;
			case INT64 -> 2;
			case SINGLEFLOAT -> 3;
			case DOUBLEFLOAT -> 4;
			}, DataType.BYTE));

		} else {
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known type instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 't', 'T' };
	}

}
