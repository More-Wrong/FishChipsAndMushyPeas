package layers;

public class FishChipsAndMushyPeasTriggerCallback implements TriggerCallback {
	private final int layerId;
	private final int x;
	private final int y;

	public FishChipsAndMushyPeasTriggerCallback(int layerId, int x, int y) {
		this.layerId = layerId;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isFishChipsAndMushyPeas() {
		return true;
	}

	public int getLayerId() {
		return layerId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
