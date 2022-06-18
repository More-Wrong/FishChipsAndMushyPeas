package main;

import instruction_pointer.FishChipsAndMushyPeasIP;

public class FishChipsAndMushyPeasThread {
	private final FishChipsAndMushyPeasModel model;
	private final FishChipsAndMushyPeasIP ip;
	private long moveAt = System.currentTimeMillis();

	public FishChipsAndMushyPeasThread(FishChipsAndMushyPeasModel model, FishChipsAndMushyPeasIP ip) {
		this.model = model;
		this.ip = ip;
		ip.setThread(this);
	}

	public void terminate() {
		model.terminateThread(this);
	}

	public FishChipsAndMushyPeasModel getModel() {
		return model;
	}

	public FishChipsAndMushyPeasIP getIp() {
		return ip;
	}

	public void step(long timeMs) {
		if (moveAt <= timeMs) {
			moveAt = timeMs;
			ip.executeInstruction();
		}
	}

	public void delay(int msDelay) {
		if (msDelay > 0) {
			moveAt += msDelay;
		}
	}
}
