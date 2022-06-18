package layers;

import java.util.Set;

public class FixedSize32Layer implements Layer {
	private final int layerNumber;
	private final Set<DataType> allowedTypes;
	private final int xSize;
	private final int ySize;
	private final int[] data;
	private final byte[] dataTypes;
	private final TriggerCallback triggerCall;

	public FixedSize32Layer(int layerNumber, Set<DataType> allowedTypes, int xSize, int ySize,
			TriggerCallback triggerCall) {
		this.layerNumber = layerNumber;
		this.allowedTypes = allowedTypes;
		this.xSize = xSize;
		this.ySize = ySize;
		this.data = new int[ySize * xSize];
		this.dataTypes = new byte[ySize * xSize];
		int defaultData = 0;
		DataType defaultType = DataType.BYTE;
		for (byte i = 0; i < DataType.values().length; i++) {
			if (allowedTypes.contains(DataType.values()[i])) {
				defaultType = DataType.values()[i];
				break;
			}
		}
		switch (defaultType) {
		case SINGLEFLOAT:
			defaultData = Float.floatToRawIntBits((float) 0.0);
			break;
		default:
			defaultData = 0;
			break;
		}
		byte defaultTypeByte = (byte) defaultType.ordinal();
		if (defaultData != 0 || defaultTypeByte != 0) {
			for (int i = 0; i < data.length; i++) {
				data[i] = defaultData;
				dataTypes[i] = defaultTypeByte;
			}
		}
		this.triggerCall = triggerCall;
	}

	@Override
	public int getLayerId() {
		return layerNumber;
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return allowedTypes;
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
		if (x > xSize || y > ySize) {
			throw new IllegalArgumentException("Fixed size layer has size: " + xSize + " x " + ySize
					+ ", tried to get value at: (" + x + ", " + y + ")");
		}
		return new LayerCell(data[x + y * xSize], dataTypes[x + y * xSize]);
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (!allowedTypes.contains(cell.getDataType())) {
			throw new IllegalArgumentException("Layer does not support cells of type " + cell.getDataType().name());
		}
		if (x > xSize || y > ySize) {
			throw new IllegalArgumentException("Fixed size layer has size: " + xSize + " x " + ySize
					+ ", tried to set value at: (" + x + ", " + y + ")");
		}
		dataTypes[x + y * xSize] = cell.getDataTypeByte();
		data[x + y * xSize] = (int) cell.getData();
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
