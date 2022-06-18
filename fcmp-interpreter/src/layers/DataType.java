package layers;

public enum DataType {
	BYTE(1, true, 0), INT32(4, true, 1), INT64(8, true,2), SINGLEFLOAT(4, false, 10), DOUBLEFLOAT(8, false,11);

	private final boolean isInteger;
	private final int size;
	private final int priority;
	
	DataType(int size, boolean isInteger, int priority) {
		this.size = size;
		this.isInteger = isInteger;
		this.priority = priority;
	}

	public int getSize() {
		return size;
	}

	public boolean isInteger() {
		return isInteger;
	}
	public DataType getResultType(DataType other) {
		if(priority>other.priority) {
			return this;
		}else {
			return other;
		}
	}
}
