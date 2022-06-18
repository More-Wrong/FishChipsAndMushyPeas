package layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlexibleLayer implements Layer {
	private static final LayerCell EMPTY = new LayerCell(0, (byte) DataType.BYTE.ordinal());
	private final int layerNumber;
	private final Set<DataType> allowedTypes;
	private final List<List<LayerCell>> data;
	private final TriggerCallback triggerCall;
	private int maxX;

	public FlexibleLayer(int layerNumber, Set<DataType> allowedTypes, int initialx, int initialy,
			TriggerCallback triggerCall) {
		if (!allowedTypes.contains(DataType.BYTE)) {
			throw new IllegalArgumentException("Infinite layers must support bytes as a type");
		}
		this.layerNumber = layerNumber;
		this.allowedTypes = allowedTypes;
		this.data = new ArrayList<>(initialy);
		for (int i = 0; i < initialy; i++) {
			List<LayerCell> row = new ArrayList<>(initialx);
			for (int j = 0; j < initialx; j++) {
				row.add(EMPTY);
			}
			data.add(row);
		}
		this.triggerCall = triggerCall;
		this.maxX = initialx;
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
		return true;
	}

	@Override
	public boolean isInfiniteY() {
		return true;
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return triggerCall;
	}

	@Override
	public int getMaxX() {
		return Math.max(maxX, 1);
	}

	@Override
	public int getMaxY() {
		return Math.max(data.size(), 1);
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (y < data.size()) {
			List<LayerCell> row = data.get(y);
			if (x < row.size()) {
				return row.get(x);
			}
		}
		return EMPTY;
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (!allowedTypes.contains(cell.getDataType())) {
			throw new IllegalArgumentException("Layer does not support cells of type " + cell.getDataType().name());
		}
		if (cell.getDataType().isInteger() && cell.getData() == 0) {
			if (y < data.size() && x < data.get(y).size()) {
				if (x == data.get(y).size() - 1) {
					List<LayerCell> row = data.get(y);
					row.remove(x);
					// remove all integer cells with value 0 from the list
					for (int i = x - 1; i >= 0 && row.get(i).getDataType().isInteger()
							&& row.get(i).getData() == 0; i--) {
						row.remove(i);
					}
					// if the row is now empty, and is the last row, remove the row
					if (row.size() == 0 && y == data.size() - 1) {
						data.remove(y);
						// and remove all immediately previous rows
						for (int i = y - 1; i > 0 && data.get(i).size() == 0; i--) {
							data.remove(i);
						}
					}
					if (x + 1 == maxX) {
						// if this was as long as the longest row, then recalculate maxX
						maxX = 0;
						for (List<LayerCell> list : data) {
							if (list.size() > maxX) {
								maxX = list.size();
							}
						}
					}
				} else {
					// allow the LayerCell we were given to be GC'd
					data.get(y).set(x, EMPTY);
				}
			}
		} else if (y >= data.size()) {
			if (x + 1 > maxX) {
				maxX = x + 1;
			}
			for (int i = data.size(); i < y; i++) {
				List<LayerCell> row = new ArrayList<>(1);
				data.add(row);
			}
			List<LayerCell> row = new ArrayList<>(x);
			for (int j = 0; j < x; j++) {
				row.add(EMPTY);
			}
			row.add(cell);
			data.add(row);
		} else if (x >= data.get(y).size()) {
			if (x + 1 > maxX) {
				maxX = x + 1;
			}
			List<LayerCell> row = data.get(y);
			for (int j = row.size(); j < x; j++) {
				row.add(EMPTY);
			}
			row.add(cell);
		} else {
			data.get(y).set(x, cell);
		}
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
	}
}
