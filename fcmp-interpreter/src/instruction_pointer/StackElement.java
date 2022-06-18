package instruction_pointer;

import layers.DataType;
import layers.LayerCell;

public class StackElement {
	private final long dataLong;
	private final double dataDouble;
	private final DataType dataType;

	public StackElement(LayerCell cell) {
		dataType = cell.getDataType();
		switch (cell.getDataType()) {
		case BYTE:
			dataLong = (byte) cell.getData();
			dataDouble = 0;
			break;
		case INT32:
			dataLong = (int) cell.getData();
			dataDouble = 0;
			break;
		case INT64:
			dataLong = cell.getData();
			dataDouble = 0;
			break;
		case SINGLEFLOAT:
			dataLong = 0;
			dataDouble = Float.intBitsToFloat((int) cell.getData());
			break;
		case DOUBLEFLOAT:
			dataLong = (byte) cell.getData();
			dataDouble = Double.longBitsToDouble(cell.getData());
			break;
		default:
			throw new IllegalArgumentException("Unknown data type: " + dataType.name());
		}
	}

	public StackElement(long data, DataType type) {
		dataLong = data;
		dataDouble = 0.0;
		dataType = type;
	}

	public StackElement(double data, DataType type) {
		dataLong = 0;
		dataDouble = data;
		dataType = type;
	}

	public LayerCell toLayerCell() {
		switch (dataType) {
		case BYTE:
		case INT32:
		case INT64:
			return new LayerCell(dataLong, dataType);
		case SINGLEFLOAT:
			return new LayerCell(Float.floatToRawIntBits((float) dataDouble), dataType);
		case DOUBLEFLOAT:
			return new LayerCell(Double.doubleToRawLongBits(dataDouble), dataType);
		default:
			throw new IllegalArgumentException("Unknown data type: " + dataType.name());
		}

	}

	public double doubleValue() {
		if (dataType.isInteger()) {
			return dataLong;
		} else {
			return dataDouble;
		}
	}

	public long integerValue() {
		if (dataType.isInteger()) {
			return dataLong;
		} else {
			return (long) dataDouble;
		}
	}

	public boolean isZero() {
		if (dataType.isInteger()) {
			return dataLong == 0;
		} else {
			return dataDouble == 0;
		}
	}

	public boolean isEqualTo(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return dataLong == other.dataLong;
		} else {
			return doubleValue() == other.doubleValue();
		}
	}

	public StackElement divideBy(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return new StackElement(dataLong / other.dataLong, dataType.getResultType(other.dataType));
		} else {
			return new StackElement(doubleValue() / other.doubleValue(), dataType.getResultType(other.dataType));
		}
	}

	public StackElement multiply(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return new StackElement(dataLong * other.dataLong, dataType.getResultType(other.dataType));
		} else {
			return new StackElement(doubleValue() * other.doubleValue(), dataType.getResultType(other.dataType));
		}
	}

	public StackElement add(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return new StackElement(dataLong + other.dataLong, dataType.getResultType(other.dataType));
		} else {
			return new StackElement(doubleValue() + other.doubleValue(), dataType.getResultType(other.dataType));
		}
	}

	public StackElement subtract(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return new StackElement(dataLong - other.dataLong, dataType.getResultType(other.dataType));
		} else {
			return new StackElement(doubleValue() - other.doubleValue(), dataType.getResultType(other.dataType));
		}
	}

	public StackElement modulo(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return new StackElement(dataLong % other.dataLong, dataType.getResultType(other.dataType));
		} else {
			return new StackElement(doubleValue() % other.doubleValue(), dataType.getResultType(other.dataType));
		}
	}

	public StackElement copy() {
		if (dataType.isInteger()) {
			return new StackElement(dataLong, dataType);
		} else {
			return new StackElement(dataDouble, dataType);
		}
	}

	public boolean isLessThan(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return dataLong < other.dataLong;
		} else {
			return doubleValue() < other.doubleValue();
		}
	}

	public boolean isGreaterThan(StackElement other) {
		if (dataType.isInteger() && other.dataType.isInteger()) {
			return dataLong > other.dataLong;
		} else {
			return doubleValue() > other.doubleValue();
		}
	}

	public StackElement withType(DataType type) {
		if (type == dataType) {
			return this;
		}
		switch (type) {
		case BYTE:
			return new StackElement((byte) integerValue(), type);
		case INT32:
			return new StackElement((int) integerValue(), type);
		case INT64:
			return new StackElement(integerValue(), type);
		case SINGLEFLOAT:
			return new StackElement((float) doubleValue(), type);
		case DOUBLEFLOAT:
			return new StackElement(doubleValue(), type);
		default:
			throw new IllegalArgumentException("Unknown data type: " + type.name());
		}
	}

	public DataType getDataType() {
		return dataType;
	}
}
