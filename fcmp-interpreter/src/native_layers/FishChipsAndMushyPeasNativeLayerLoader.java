package native_layers;

import java.util.Deque;

import instruction_pointer.StackElement;
import layers.Layer;
import main.FishChipsAndMushyPeasModel;

public interface FishChipsAndMushyPeasNativeLayerLoader {

	String getLoadName();

	Layer generateLayer(FishChipsAndMushyPeasModel model, Deque<StackElement> stack);
}
