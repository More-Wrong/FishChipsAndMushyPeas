package main;

import java.util.ArrayList;
import java.util.List;

import instruction_pointer.FishChipsAndMushyPeasIP;

public class FishChipsAndMushyPeasModel {
	private final FishChipsAndMushyPeasMemorySpace memorySpace = new FishChipsAndMushyPeasMemorySpace();
	private final List<FishChipsAndMushyPeasThread> threads = new ArrayList<>();

	public void terminateThread(FishChipsAndMushyPeasThread thread) {
		threads.remove(thread);
	}

	public void terminate() {
		threads.clear();
		memorySpace.clear();
	}

	public void addThread(FishChipsAndMushyPeasIP ip) {
		threads.add(new FishChipsAndMushyPeasThread(this, ip));
	}

	public void execute() {
		while (threads.size() != 0 && memorySpace.hasLayers()) {
			long timeMs = System.currentTimeMillis();
			int size = threads.size();
			for (int i = 0; i < size; i++) {
				if (size > threads.size()) {
					i--;
					size = threads.size();
				}
				threads.get(i).step(timeMs);
			}
		}
	}

	public void clearLayer(int layerId) {
		for (FishChipsAndMushyPeasThread thread : threads) {
			if (thread.getIp().getCurrentLayer().getLayerId() == layerId) {
				// this is sort of last-ditch, but if you cann X on your own layer, this is what
				// you want (otherwise it will just screw everything up, but thats *your*
				// problem)
				thread.getIp().exitLayer();
			}
		}
	}

	public FishChipsAndMushyPeasMemorySpace getMemorySpace() {
		return memorySpace;
	}
}
