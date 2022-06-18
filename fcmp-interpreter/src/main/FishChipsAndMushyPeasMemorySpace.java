package main;

import java.util.ArrayList;
import java.util.List;

import layers.Layer;

public class FishChipsAndMushyPeasMemorySpace {
	private class LayerWrapper {
		private final int layerId;
		private LayerWrapper prevClear = null;
		private LayerWrapper nextClear = null;
		private Layer layer;

		public LayerWrapper(Layer layer) {
			this.layer = layer;
			this.layerId = layer.getLayerId();
		}

		public int getLayerId() {
			return layerId;
		}

		public boolean hasLayer() {
			return layer != null;
		}

		public Layer getLayer() {
			return layer;
		}

		public LayerWrapper getNextClear() {
			return nextClear;
		}

		public LayerWrapper getPrevClear() {
			return prevClear;
		}

		public void setLayer(Layer layer) {
			this.layer = layer;
			this.prevClear = null;
			this.nextClear = null;
		}

		public void clearLayer(LayerWrapper prevClear, LayerWrapper nextClear) {
			this.layer = null;
			this.prevClear = prevClear;
			this.nextClear = nextClear;
		}

		public void setNextClear(LayerWrapper nextClear) {
			this.nextClear = nextClear;
		}

		public void setPrevClear(LayerWrapper prevClear) {
			this.prevClear = prevClear;
		}
	}

	// kind of a linked array list, with empty slots being linked, so as to allow
	// the ids to remain constant despite removal of layers before them... (just an
	// attempt to avoid a real map, and having some fun with data-structures)
	private final List<LayerWrapper> layers = new ArrayList<>();
	private LayerWrapper firstClear = null;
	private LayerWrapper lastClear = null;

	public Layer getLayer(int layerId) {
		if (layerId < 0 || layerId > layers.size() || !layers.get(layerId).hasLayer()) {
			throw new IllegalArgumentException("No such layer: " + layerId);
		} else {
			return layers.get(layerId).getLayer();
		}
	}

	public boolean hasLayers() {
		return !layers.isEmpty();
	}

	public void removeLayer(int layerId) {
		if (layerId < layers.size() - 1) {
			// sort of try to keep it a bit sorted... if its in the fist half put it at the
			// Beginning, otherwise at the end - not great, but something
			if (firstClear == null) {
				layers.get(layerId).clearLayer(null, firstClear);
				firstClear = layers.get(layerId);
				lastClear = layers.get(layerId);
			} else if (layerId < (firstClear.getLayerId() + lastClear.getLayerId()) / 2) {
				layers.get(layerId).clearLayer(null, firstClear);
				firstClear.setPrevClear(layers.get(layerId));
				firstClear = layers.get(layerId);
			} else {
				layers.get(layerId).clearLayer(lastClear, null);
				lastClear.setNextClear(layers.get(layerId));
				lastClear = layers.get(layerId);
			}
		} else if (layerId == layers.size() - 1) {
			// if removing the last layer, try to remove as many wrappers as possible
			layers.remove(layers.size() - 1);
			while (!layers.get(layers.size() - 1).hasLayer()) {
				LayerWrapper wrapper = layers.get(layers.size() - 1);
				if (wrapper.getNextClear() != null) {
					wrapper.getNextClear().setPrevClear(wrapper.getPrevClear());
				}
				if (wrapper.getPrevClear() != null) {
					wrapper.getPrevClear().setNextClear(wrapper.getNextClear());
				}
				layers.remove(layers.size() - 1);
			}
		} else {
			throw new IllegalArgumentException("Layer id beyond end of layers" + layerId);
		}
	}

	public void addLayer(Layer l) {
		int id = l.getLayerId();
		if (id == layers.size()) {
			layers.add(new LayerWrapper(l));
		} else if (id < layers.size()) {
			LayerWrapper wrapper = layers.get(id);
			if (wrapper.hasLayer()) {
				throw new IllegalArgumentException("Layer id already in use" + id);
			}
			if (wrapper.getNextClear() == null) {
				lastClear = wrapper.getPrevClear();
			}
			if (wrapper.getPrevClear() == null) {
				firstClear = wrapper.getNextClear();
			}
			wrapper.setLayer(l);
		} else {
			throw new IllegalArgumentException("Layer id beyond end of layers" + id);
		}
	}

	public int getFreeId() {
		if (firstClear == null) {
			return layers.size();
		} else {
			return firstClear.getLayerId();
		}
	}

	public void clear() {
		for (LayerWrapper wrapper : layers) {
			if (wrapper.hasLayer()) {
				wrapper.getLayer().cleanup(true);
			}
		}
		layers.clear();
		firstClear = null;
		lastClear = null;
	}
}
