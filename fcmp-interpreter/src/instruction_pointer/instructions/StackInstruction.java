package instruction_pointer.instructions;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class StackInstruction implements FishChipsAndMushyPeasInstruction {
	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		switch (instruction) {
		case ':':
			ip.getCurrentStack().push(ip.getCurrentStack().peek());
			break;
		case '~':
			ip.getCurrentStack().pop();
			break;
		case '$': {
			StackElement element1 = ip.getCurrentStack().pop();
			StackElement element2 = ip.getCurrentStack().pop();
			ip.getCurrentStack().push(element1);
			ip.getCurrentStack().push(element2);
			break;
		}
		case '@': {
			StackElement element1 = ip.getCurrentStack().pop();
			StackElement element2 = ip.getCurrentStack().pop();
			StackElement element3 = ip.getCurrentStack().pop();
			ip.getCurrentStack().push(element1);
			ip.getCurrentStack().push(element3);
			ip.getCurrentStack().push(element2);
			break;
		}
		case '}':
			ip.getCurrentStack().addLast(ip.getCurrentStack().pop());
			break;
		case '{':
			ip.getCurrentStack().push(ip.getCurrentStack().removeLast());
			break;
		case 'r': {
			Deque<StackElement> reversedStack = new ArrayDeque<>();
			for (StackElement e : ip.getCurrentStack()) {
				reversedStack.push(e);
			}
			ip.getStacks().set(ip.getCurrentStackIndex(), reversedStack);
			break;
		}
		case 'l':
			ip.getCurrentStack().push(new StackElement(ip.getCurrentStack().size(), DataType.INT32));
			break;
		case '[': {
			int lengthToTake = (int) ip.getCurrentStack().pop().integerValue();
			Deque<StackElement> newStack = new ArrayDeque<>();
			for (int i = 0; i < lengthToTake; i++) {
				newStack.addLast(ip.getCurrentStack().pop());
			}
			ip.getStacks().add(ip.getCurrentStackIndex() + 1, newStack);
			ip.setCurrentStackIndex(ip.getCurrentStackIndex() + 1);
			break;
		}
		case ']': {
			for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
				ip.getStacks().get(ip.getCurrentStackIndex() - 1).push(iter.next());
			}
			ip.getStacks().remove(ip.getCurrentStackIndex());
			ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
			break;
		}
		case '&':
			if (ip.hasRegister()) {
				ip.getCurrentStack().push(ip.getRegister());
				ip.setHasRegister(false);
			} else {
				ip.setRegister(ip.getCurrentStack().pop());
				ip.setHasRegister(true);
			}
			break;
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known stack instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { ':', '~', '$', '@', 'l', 'r', '{', '}', '&', '[', ']' };
	}

}
