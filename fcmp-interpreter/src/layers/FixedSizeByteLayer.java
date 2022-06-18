package layers;

import java.util.EnumSet;
import java.util.Set;

public class FixedSizeByteLayer implements Layer {
	private final int layerNumber;
	private final int xSize;
	private final int ySize;
	private final byte[] data;
	private final TriggerCallback triggerCall;

	public FixedSizeByteLayer(int layerNumber, int xSize, int ySize, TriggerCallback triggerCall) {
		this.layerNumber = layerNumber;
		this.xSize = xSize;
		this.ySize = ySize;
		this.data = new byte[xSize * ySize];
		this.triggerCall = triggerCall;
	}

	@Override
	public int getLayerId() {
		return layerNumber;
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return EnumSet.of(DataType.BYTE);
	}

	@Override
	public boolean isInfiniteX() {
		return false;
	}

	@Override
	public boolean isInfiniteY() {
		return false;
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return triggerCall;
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (x >= xSize || y >= ySize) {
			throw new IllegalArgumentException("Fixed size layer has size: " + xSize + " x " + ySize
					+ ", tried to get value at: (" + x + ", " + y + ")");
		}
		return new LayerCell(data[x + y * xSize], (byte) DataType.BYTE.ordinal());
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (cell.getDataType() != DataType.BYTE) {
			throw new IllegalArgumentException(
					"Layer only supports 1 byte numbers, tried to set value to " + cell.getDataType().name());
		}
		if (x >= xSize || y >= ySize) {
			throw new IllegalArgumentException("Fixed size layer has size: " + xSize + " x " + ySize
					+ ", tried to set value at: (" + x + ", " + y + ")");
		}
		data[x + y * xSize] = (byte) cell.getData();
	}

	@Override
	public int getMaxX() {
		return xSize;
	}

	@Override
	public int getMaxY() {
		return ySize;
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
	}
}
