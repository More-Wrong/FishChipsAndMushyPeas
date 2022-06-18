package instruction_pointer.instructions;

import java.util.Iterator;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.Layer;
import layers.LayerCell;
import native_layers.FishChipsAndMushyPeasNativeLayerLoader;

public class ThreadingInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		switch (instruction) {
		case 'E': {
			StringBuilder s = new StringBuilder(ip.getCurrentStack().size());
			for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
				s.append((char) iter.next().integerValue());
			}
			throw new RuntimeException("FishChipsAndMushyPeas Error, stack: \n" + s.toString() + "\n");
		}
		case ';': {
			ip.getThread().terminate();
			break;
		}
		case '.': {
			StackElement elementY = ip.getCurrentStack().pop();
			StackElement elementX = ip.getCurrentStack().pop();
			StackElement layer = ip.getCurrentStack().pop();
			Layer startLayer = ip.getMemorySpace().getLayer((int) layer.integerValue());
			int startX = (int) (elementX.integerValue() % ip.getCurrentLayer().getMaxX());
			int startY = (int) (elementY.integerValue() % ip.getCurrentLayer().getMaxY());
			FishChipsAndMushyPeasIP newIp = new FishChipsAndMushyPeasIP(ip.getMemorySpace(), startLayer, startX, startY, ip.getDirection(),
					ip.getCurrentStack());
			ip.getStacks().remove(ip.getCurrentStackIndex());
			ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
			ip.getThread().getModel().addThread(newIp);
			break;
		}
		case 's': {
			StackElement time = ip.getCurrentStack().pop();
			ip.getThread().delay((int) time.integerValue());
			break;
		}
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known threading instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { 'E', ';', 's', '.' };
	}
}
