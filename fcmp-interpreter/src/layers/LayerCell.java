package layers;

public class LayerCell {
	private final long data;
	private final byte dataType;

	public LayerCell(long data, byte dataType) {
		this.data = data;
		this.dataType = dataType;
	}

	public LayerCell(long data, DataType dataType) {
		this.data = data;
		this.dataType = (byte) dataType.ordinal();
	}

	public long getData() {
		return data;
	}

	public byte getDataTypeByte() {
		return dataType;
	}

	public DataType getDataType() {
		return DataType.values()[dataType];
	}

}