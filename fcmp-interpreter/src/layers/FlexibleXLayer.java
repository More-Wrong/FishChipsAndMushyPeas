package layers;

import java.util.Set;

public class FlexibleXLayer implements Layer {
	private final FlexibleLayer underlying;
	private final int maxY;

	public FlexibleXLayer(int layerNumber, Set<DataType> allowedTypes, int initialx, int maxY,
			TriggerCallback triggerCall) {
		this.underlying = new FlexibleLayer(layerNumber, allowedTypes, initialx, maxY, triggerCall);
		this.maxY = maxY;
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return underlying.getAllowedDataTypes();
	}

	@Override
	public boolean isInfiniteX() {
		return true;
	}

	@Override
	public boolean isInfiniteY() {
		return false;
	}

	@Override
	public int getMaxX() {
		return underlying.getMaxX();
	}

	@Override
	public int getMaxY() {
		return maxY;
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return underlying.getTriggerCall();
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (y >= maxY) {
			throw new IllegalArgumentException("Layer has fixed y size: " + maxY + ", tried to get value at: " + y);
		}
		return underlying.getCellAt(x, y);
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (y >= maxY) {
			throw new IllegalArgumentException("Layer has fixed y size: " + maxY + ", tried to set value at: " + y);
		}
		underlying.setCellAt(x, y, cell);

	}

	@Override
	public int getLayerId() {
		return underlying.getLayerId();
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
	}
}
