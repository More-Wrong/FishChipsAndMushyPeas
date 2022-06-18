package instruction_pointer.instructions;

import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import instruction_pointer.FishChipsAndMushyPeasInstruction;
import layers.DataType;
import layers.FixedSize32Layer;
import layers.FixedSizeByteLayer;
import layers.FlexibleLayer;
import layers.FlexibleXLayer;
import layers.FlexibleYLayer;
import layers.Layer;
import layers.LayerCell;
import layers.NativeTriggerCallback;
import layers.FishChipsAndMushyPeasTriggerCallback;
import layers.TriggerCallback;

public class LayerInstruction implements FishChipsAndMushyPeasInstruction {

	@Override
	public void execute(LayerCell cell, FishChipsAndMushyPeasIP ip) {
		if (!cell.getDataType().isInteger() || cell.getData() > 128 || cell.getData() < 0) {
			throw new IllegalArgumentException("Not a valid cell type to be a normal instruction");
		}
		byte instruction = (byte) cell.getData();

		switch (instruction) {
		case '`': {
			ip.getCurrentStack().push(new StackElement(ip.getCurrentLayer().getLayerId(), DataType.INT32));
			break;
		}
		case 'X': {
			int layerId = (int) ip.getCurrentStack().pop().integerValue();
			ip.getMemorySpace().getLayer(layerId).cleanup(false);
			if (ip.getMemorySpace().hasLayers()) {
				ip.getMemorySpace().removeLayer(layerId);
				ip.getThread().getModel().clearLayer(layerId);
			}
			break;
		}
		case 'S': {
			Layer layer = ip.getMemorySpace().getLayer((int) ip.getCurrentStack().pop().integerValue());
			ip.getCurrentStack().push(new StackElement(layer.getMaxY(), DataType.INT32));
			ip.getCurrentStack().push(new StackElement(layer.getMaxX(), DataType.INT32));
			int layerInfinity = layer.isInfiniteX() ? 1 : 0;
			layerInfinity |= layer.isInfiniteY() ? 2 : 0;
			ip.getCurrentStack().push(new StackElement(layerInfinity, DataType.BYTE));
			int types = 0;
			if (layer.getAllowedDataTypes().contains(DataType.BYTE)) {
				types |= 1;
			}
			if (layer.getAllowedDataTypes().contains(DataType.INT32)) {
				types |= 2;
			}
			if (layer.getAllowedDataTypes().contains(DataType.INT64)) {
				types |= 4;
			}
			if (layer.getAllowedDataTypes().contains(DataType.SINGLEFLOAT)) {
				types |= 8;
			}
			if (layer.getAllowedDataTypes().contains(DataType.DOUBLEFLOAT)) {
				types |= 16;
			}
			ip.getCurrentStack().push(new StackElement(types, DataType.BYTE));
			break;
		}
		case 'O': {
			Layer layer = ip.getMemorySpace().getLayer((int) ip.getCurrentStack().pop().integerValue());
			if (layer.getTriggerCall().isFishChipsAndMushyPeas()) {
				FishChipsAndMushyPeasTriggerCallback triggerCall = (FishChipsAndMushyPeasTriggerCallback) layer.getTriggerCall();
				Deque<StackElement> prev = ip.getStacks().get(ip.getCurrentStackIndex() - 1);
				int layerTarget = triggerCall.getLayerId();
				int xTarget = triggerCall.getX();
				int yTarget = triggerCall.getY();
				prev.push(new StackElement(ip.getCurrentLayer().getLayerId(), DataType.INT32));
				prev.push(new StackElement(ip.getX(), DataType.INT32));
				prev.push(new StackElement(ip.getY(), DataType.INT32));
				ip.setCurrentLayer(ip.getMemorySpace().getLayer(layerTarget));
				ip.setX(xTarget % ip.getCurrentLayer().getMaxX());
				ip.setY(yTarget % ip.getCurrentLayer().getMaxY());
				ip.doNotMoveNow();
			} else {
				NativeTriggerCallback triggerCall = (NativeTriggerCallback) layer.getTriggerCall();
				triggerCall.call(ip.getCurrentStack());

				// then remove current stack to return
				for (Iterator<StackElement> iter = ip.getCurrentStack().descendingIterator(); iter.hasNext();) {
					ip.getStacks().get(ip.getCurrentStackIndex() - 1).push(iter.next());
				}
				ip.getStacks().remove(ip.getCurrentStackIndex());
				ip.setCurrentStackIndex(ip.getCurrentStackIndex() - 1);
			}
			break;
		}
		case 'N': {
			Set<DataType> allowedTypes = EnumSet.noneOf(DataType.class);

			int layerTrigger;
			int xTrigger;
			int yTrigger;

			boolean isInfiniteX;
			boolean isInfiniteY;
			int xMax;
			int yMax;

			int maxSize;
			int freeId = ip.getMemorySpace().getFreeId();

			int typeBitField = (int) ip.getCurrentStack().pop().integerValue();
			if ((typeBitField & 1) != 0) {
				allowedTypes.add(DataType.BYTE);
			}
			if ((typeBitField & 2) != 0) {
				allowedTypes.add(DataType.INT32);
			}
			if ((typeBitField & 4) != 0) {
				allowedTypes.add(DataType.INT64);
			}
			if ((typeBitField & 8) != 0) {
				allowedTypes.add(DataType.SINGLEFLOAT);
			}
			if ((typeBitField & 16) != 0) {
				allowedTypes.add(DataType.DOUBLEFLOAT);
			}
			boolean externalTrigger = ip.getCurrentStack().pop().isZero();
			if (externalTrigger) {
				yTrigger = (int) (ip.getCurrentStack().pop().integerValue() % ip.getCurrentLayer().getMaxY());
				xTrigger = (int) (ip.getCurrentStack().pop().integerValue() % ip.getCurrentLayer().getMaxX());
				layerTrigger = ip.getCurrentLayer().getLayerId();
			} else {
				yTrigger = 0;
				xTrigger = 0;
				layerTrigger = freeId;
			}
			int inf = (int) ip.getCurrentStack().pop().integerValue();
			isInfiniteX = (inf & 1) != 0;
			isInfiniteY = (inf & 2) != 0;
			if (allowedTypes.size() == 0) {
				allowedTypes.add(DataType.BYTE);
			}
			if (isInfiniteX && isInfiniteY) {
				allowedTypes.add(DataType.BYTE);
				xMax = 10;
				yMax = 10;
				maxSize = 8;
			} else if (isInfiniteX) {
				allowedTypes.add(DataType.BYTE);
				xMax = 10;
				yMax = (int) ip.getCurrentStack().pop().integerValue();
				maxSize = 8;
			} else if (isInfiniteY) {
				allowedTypes.add(DataType.BYTE);
				xMax = (int) ip.getCurrentStack().pop().integerValue();
				yMax = 10;
				maxSize = 8;
			} else {
				yMax = (int) ip.getCurrentStack().pop().integerValue();
				xMax = (int) ip.getCurrentStack().pop().integerValue();
				maxSize = 0;
				for (DataType dataType : allowedTypes) {
					maxSize = Math.max(maxSize, dataType.getSize());
				}
			}

			Layer l;

			TriggerCallback callback = new FishChipsAndMushyPeasTriggerCallback(layerTrigger, xTrigger, yTrigger);
			if (isInfiniteX && isInfiniteY) {
				l = new FlexibleLayer(freeId, allowedTypes, xMax, yMax, callback);
			} else if (isInfiniteX) {
				l = new FlexibleXLayer(freeId, allowedTypes, xMax, yMax, callback);
			} else if (isInfiniteY) {
				l = new FlexibleYLayer(freeId, allowedTypes, xMax, yMax, callback);
			} else {
				if (maxSize == 1) {
					l = new FixedSizeByteLayer(freeId, xMax, yMax, callback);
				} else if (maxSize <= 4) {
					l = new FixedSize32Layer(freeId, allowedTypes, xMax, yMax, callback);
				} else if (maxSize <= 8) {
					l = new FixedSize32Layer(freeId, allowedTypes, xMax, yMax, callback);
				} else {
					throw new IllegalStateException("No known Layer for types with size >8 bytes");
				}
			}
			ip.getMemorySpace().addLayer(l);
			ip.getCurrentStack().push(new StackElement(l.getLayerId(), DataType.INT32));
			break;
		}
		default:
			throw new IllegalArgumentException(
					"The instruction \"" + instruction + "\" is not a known layer instruction");
		}
	}

	@Override
	public boolean executeInDive() {
		return false;
	}

	@Override
	public byte[] getValidInstructions() {
		return new byte[] { '`', 'X', 'S', 'O', 'N' };
	}

}
