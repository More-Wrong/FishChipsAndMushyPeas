package instruction_pointer.instructions;

import java.util.Deque;
import java.util.Iterator;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.LayerCell;

public class JumpInstruction implements FishChipsAndMushyPeasInstruction {

	public void exitLayer(FishChipsAndMushyPeasIP ip) {
		if (ip.getCurrentStackIndex() == 0) {
			ip.getThread().terminate();
		} else {
			Deque<StackElement> prev = ip.getStacks().get(ip.getCurrentStackIndex() - 1);
			StackElement elementY = prev.pop();
			StackElement elementX = prev.pop();
			StackElement layer = prev.pop();
			ip.setCurrentLayer(ip.getMemorySpace().getLayer((int) layer.integerValue()));
			ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
			ip.setY((int) (elementY.integerValue() % ip.getCurrentLayer().getMaxY()));
			// then remove current stack to return
			for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
				ip.getStacks().get(ip.getCurrentStackIndex() - 1).push(iter.next());
			}
			ip.getStacks().remove(ip.getCurrentStackIndex());
			ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
		}
	}

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();
		Deque<StackElement> prev;
		if (ip.getCurrentStackIndex() != 0) {
			prev = ip.getStacks().get(ip.getCurrentStackIndex() - 1);
		} else {
			prev = null;
		}
		switch (instruction) {
		case 'j': {
			StackElement elementY = ip.getCurrentStack().pop();
			StackElement elementX = ip.getCurrentStack().pop();
			ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
			ip.setY((int) elementY.integerValue() % ip.getCurrentLayer().getMaxY());
			ip.doNotMoveNow();
			break;
		}
		case 'C': {
			StackElement elementY = ip.getCurrentStack().pop();
			StackElement elementX = ip.getCurrentStack().pop();
			prev.push(new StackElement(ip.getX(), DataType.INT32));
			prev.push(new StackElement(ip.getY(), DataType.INT32));
			ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
			ip.setY((int) elementY.integerValue() % ip.getCurrentLayer().getMaxY());
			ip.doNotMoveNow();
			break;
		}
		case 'R': {
			if (ip.getCurrentStackIndex() == 0) {
				ip.getThread().terminate();
			} else {
				StackElement elementY = prev.pop();
				StackElement elementX = prev.pop();
				ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
				ip.setY((int) elementY.integerValue() % ip.getCurrentLayer().getMaxY());
				// then remove current stack to return
				for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
					ip.getStacks().get(ip.getCurrentStackIndex() - 1).push(iter.next());
				}
				ip.getStacks().remove(ip.getCurrentStackIndex());
				ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
			}
			break;
		}
		case 'J': {
			StackElement elementY = ip.getCurrentStack().pop();
			StackElement elementX = ip.getCurrentStack().pop();
			StackElement layer = ip.getCurrentStack().pop();
			ip.setCurrentLayer(ip.getMemorySpace().getLayer((int) layer.integerValue()));
			ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
			ip.setY((int) (elementY.integerValue() % ip.getCurrentLayer().getMaxY()));
			ip.doNotMoveNow();
			break;
		}
		case 'm': {
			StackElement elementY = ip.getCurrentStack().pop();
			StackElement elementX = ip.getCurrentStack().pop();
			StackElement layer = ip.getCurrentStack().pop();
			prev.push(new StackElement(ip.getCurrentLayer().getLayerId(), DataType.INT32));
			prev.push(new StackElement(ip.getX(), DataType.INT32));
			prev.push(new StackElement(ip.getY(), DataType.INT32));
			ip.setCurrentLayer(ip.getMemorySpace().getLayer((int) layer.integerValue()));
			ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
			ip.setY((int) (elementY.integerValue() % ip.getCurrentLayer().getMaxY()));
			ip.doNotMoveNow();
			break;
		}
		case 'M': {
			if (ip.getCurrentStackIndex() == 0) {
				ip.getThread().terminate();
			} else {
				StackElement elementY = prev.pop();
				StackElement elementX = prev.pop();
				StackElement layer = prev.pop();
				ip.setCurrentLayer(ip.getMemorySpace().getLayer((int) layer.integerValue()));
				ip.setX((int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX()));
				ip.setY((int) (elementY.integerValue() % ip.getCurrentLayer().getMaxY()));
				// then remove current stack to return
				for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
					ip.getStacks().get(ip.getCurrentStackIndex() - 1).push(iter.next());
				}
				ip.getStacks().remove(ip.getCurrentStackIndex());
				ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
			}
			break;
		}
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known jump instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'j', 'J', 'C', 'R', 'm', 'M' };
	}

}
