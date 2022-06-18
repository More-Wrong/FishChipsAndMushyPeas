package layers;

import java.util.Set;

public interface Layer {

	Set<DataType> getAllowedDataTypes();

	boolean isInfiniteX();

	boolean isInfiniteY();

	int getMaxX();

	int getMaxY();

	TriggerCallback getTriggerCall();

	LayerCell getCellAt(int x, int y);

	void setCellAt(int x, int y, LayerCell cell);

	int getLayerId();

	void cleanup(boolean isShuttingDown);
}
