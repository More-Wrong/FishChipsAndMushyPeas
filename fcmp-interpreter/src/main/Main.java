package main;

import java.io.File;
import java.util.ArrayDeque;

import layers.Layer;
import native_layers.FishChipsAndMushyPeasLoader;
import native_layers.FishChipsAndMushyPeasLoaderLayer;
import native_layers.WindowLayerLoader;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("Need argument for ><>\\.:. file to execute");
		}
		FishChipsAndMushyPeasLoader.addToLoaders(new WindowLayerLoader());
		for (String string : args) {
			if (string.startsWith("-I")) {
				FishChipsAndMushyPeasLoader.addToPath(new File(string.substring(2)));
			}
		}
		FishChipsAndMushyPeasModel model = new FishChipsAndMushyPeasModel();
		model.getMemorySpace().addLayer(new FishChipsAndMushyPeasLoaderLayer(model));
		Layer l = FishChipsAndMushyPeasLoader.parseFishChipsAndMushyPeasFile(model, new File(args[args.length - 1]));
		FishChipsAndMushyPeasLoader.startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>());
		model.getMemorySpace().addLayer(l);
		model.execute();
	}

}
