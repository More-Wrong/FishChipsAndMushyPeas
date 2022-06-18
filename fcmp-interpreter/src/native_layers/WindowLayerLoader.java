package native_layers;

import java.util.Deque;

import instruction_pointer.StackElement;
import layers.Layer;
import layers.FishChipsAndMushyPeasTriggerCallback;
import main.FishChipsAndMushyPeasModel;

public class WindowLayerLoader implements FishChipsAndMushyPeasNativeLayerLoader {

	@Override
	public String getLoadName() {
		return "Window";
	}

	@Override
	public Layer generateLayer(FishChipsAndMushyPeasModel model, Deque<StackElement> stack) {
		model.getMemorySpace().getFreeId();
		int keyEvents = (int) stack.pop().integerValue();
		int mouseEvents = (int) stack.pop().integerValue();
		int defaultH = (int) stack.pop().integerValue();
		int defaultW = (int) stack.pop().integerValue();
		int triggery = (int) stack.pop().integerValue();
		int triggerx = (int) stack.pop().integerValue();
		int triggerid = (int) stack.pop().integerValue();
		StringBuilder name = new StringBuilder();
		while (!stack.isEmpty()) {
			name.append((char) stack.pop().integerValue());
		}
		FishChipsAndMushyPeasTriggerCallback callback = new FishChipsAndMushyPeasTriggerCallback(triggerid, triggerx, triggery);
		return new WindowLayer(model, model.getMemorySpace().getFreeId(), callback, defaultW, defaultH, name.toString(),
				keyEvents, mouseEvents);
	}

}
