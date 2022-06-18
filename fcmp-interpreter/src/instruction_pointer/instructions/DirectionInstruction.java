package instruction_pointer.instructions;

import java.util.Random;

import instruction_pointer.Direction;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.LayerCell;

public class DirectionInstruction implements FishChipsAndMushyPeasInstruction {
	Random random = new Random(1023);

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		switch (instruction) {
		case ('^'):
			ip.setDirection(Direction.NORTH);
			break;
		case ('>'):
			ip.setDirection(Direction.EAST);
			break;
		case ('v'):
			ip.setDirection(Direction.SOUTH);
			break;
		case ('<'):
			ip.setDirection(Direction.WEST);
			break;
		case ('/'):
			ip.setDirection(switch (ip.getDirection()) {
			case NORTH -> Direction.EAST;
			case EAST -> Direction.NORTH;
			case SOUTH -> Direction.WEST;
			case WEST -> Direction.SOUTH;
			});
			break;
		case ('\\'):
			ip.setDirection(switch (ip.getDirection()) {
			case NORTH -> Direction.WEST;
			case EAST -> Direction.SOUTH;
			case SOUTH -> Direction.EAST;
			case WEST -> Direction.NORTH;
			});
			break;
		case ('_'):
			ip.setDirection(switch (ip.getDirection()) {
			case NORTH -> Direction.SOUTH;
			case EAST -> Direction.EAST;
			case SOUTH -> Direction.NORTH;
			case WEST -> Direction.WEST;
			});
			break;
		case ('|'):
			ip.setDirection(switch (ip.getDirection()) {
			case NORTH -> Direction.NORTH;
			case EAST -> Direction.WEST;
			case SOUTH -> Direction.SOUTH;
			case WEST -> Direction.EAST;
			});
			break;
		case ('#'):
			ip.setDirection(switch (ip.getDirection()) {
			case NORTH -> Direction.SOUTH;
			case EAST -> Direction.WEST;
			case SOUTH -> Direction.NORTH;
			case WEST -> Direction.EAST;
			});
			break;
		case ('x'):
			int r = random.nextInt(4);
			ip.setDirection(switch (r) {
			case 0 -> Direction.SOUTH;
			case 1 -> Direction.WEST;
			case 2 -> Direction.NORTH;
			case 3 -> Direction.EAST;
			default -> {
				throw new IllegalStateException("Random.nextInt didn't work properly...");
			}
			});
			break;
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known direction instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return true;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '^', '>', 'v', '<', '\\', '/', '_', '|', '#', 'x' };
	}

}
