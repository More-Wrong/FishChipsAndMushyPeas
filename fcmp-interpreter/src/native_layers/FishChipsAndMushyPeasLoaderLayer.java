package native_layers;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import instruction_pointer.StackElement;
import layers.DataType;
import layers.Layer;
import layers.LayerCell;
import layers.NativeTriggerCallback;
import layers.TriggerCallback;
import main.FishChipsAndMushyPeasModel;

public class FishChipsAndMushyPeasLoaderLayer implements Layer {
	private class LoaderTriggerCallback implements NativeTriggerCallback {

		@Override
		public boolean isFishChipsAndMushyPeas() {
			return false;
		}

		@Override
		public void call(Deque<StackElement> stack) {
			StringBuilder builder = new StringBuilder();
			for (Byte b : data) {
				builder.append((char) b.byteValue());
			}
			model.getMemorySpace().addLayer(FishChipsAndMushyPeasLoader.loadLayer(model, builder.toString(), stack));
			data.clear();
		}

	}

	private final List<Byte> data = new ArrayList<>();
	private final FishChipsAndMushyPeasModel model;

	public FishChipsAndMushyPeasLoaderLayer(FishChipsAndMushyPeasModel model) {
		this.model = model;
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return EnumSet.of(DataType.BYTE);
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
		return Math.max(data.size(), 1);
	}

	@Override
	public int getMaxY() {
		return 1;
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return new LoaderTriggerCallback();
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (y >= 1) {
			throw new IllegalArgumentException("Layer has fixed y size: 1, tried to get value at: " + y);
		}
		if (x >= data.size()) {
			return new LayerCell(0, (byte) DataType.BYTE.ordinal());
		}
		return new LayerCell(data.get(x), (byte) DataType.BYTE.ordinal());
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (y >= 1) {
			throw new IllegalArgumentException("Layer has fixed y size: 1, tried to get value at: " + y);
		}
		if (cell.getDataType() != DataType.BYTE) {
			throw new IllegalArgumentException(
					"Layer only supports 1 byte numbers, tried to set value to " + cell.getDataType().name());
		}
		while (x >= data.size()) {
			data.add((byte) 0);
		}
		data.set(x, (byte) cell.getData());
	}

	@Override
	public int getLayerId() {
		return 0;
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
		if (!isShuttingDown) {
			model.terminate();
		}
	}

}
