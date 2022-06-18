package layers;

import java.util.Set;

public class FlexibleYLayer implements Layer {
	// bit of a hack, but store a flexible layer with x, y inverted - allows less
	// repeat code
	private final FlexibleLayer underlying;
	private final int maxX;

	public FlexibleYLayer(int layerNumber, Set<DataType> allowedTypes, int maxX, int initialy,
			TriggerCallback triggerCall) {
		this.underlying = new FlexibleLayer(layerNumber, allowedTypes, initialy, maxX, triggerCall);
		this.maxX = maxX;
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return underlying.getAllowedDataTypes();
	}

	@Override
	public boolean isInfiniteX() {
		return false;
	}

	@Override
	public boolean isInfiniteY() {
		return true;
	}

	@Override
	public int getMaxX() {
		return maxX;
	}

	@Override
	public int getMaxY() {
		return underlying.getMaxX();
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return underlying.getTriggerCall();
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (x >= maxX) {
			throw new IllegalArgumentException("Layer has fixed x size: " + maxX + ", tried to get value at: " + x);
		}
		return underlying.getCellAt(y, x);
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (x >= maxX) {
			throw new IllegalArgumentException("Layer has fixed x size: " + maxX + ", tried to set value at: " + x);
		}
		underlying.setCellAt(y, x, cell);

	}

	@Override
	public int getLayerId() {
		return underlying.getLayerId();
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
	}
}
