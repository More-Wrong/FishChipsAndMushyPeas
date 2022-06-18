package layers;

import java.util.Deque;

import instruction_pointer.StackElement;

public interface NativeTriggerCallback extends TriggerCallback {
	void call(Deque<StackElement> stack);
}
