package native_layers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import instruction_pointer.Direction;
import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import layers.DataType;
import layers.FlexibleLayer;
import layers.Layer;
import layers.LayerCell;
import layers.FishChipsAndMushyPeasTriggerCallback;
import main.FishChipsAndMushyPeasModel;

public class FishChipsAndMushyPeasLoader {
	static Map<String, FishChipsAndMushyPeasNativeLayerLoader> nativeLoaders = new HashMap<>();
	static List<File> fishPath = new ArrayList<>();

	public static void addToLoaders(FishChipsAndMushyPeasNativeLayerLoader loader) {
		if (nativeLoaders.containsKey(loader.getLoadName())) {
			throw new IllegalStateException(
					"Two FishChipsAndMushyPeasNativeLayerLoader share the same load name: " + loader.getLoadName() + " These are "
							+ loader.toString() + " and " + nativeLoaders.get(loader.getLoadName()).toString());
		}
		nativeLoaders.put(loader.getLoadName(), loader);
	}

	public static void addToPath(File f) {
		fishPath.add(f);
	}

	public static Layer loadLayer(FishChipsAndMushyPeasModel model, String s, Deque<StackElement> stack) {
		String name1 = s + ".><>\\\\.:.";
		String name2 = s + ".fcmp";
		File f = new File(name1);
		if (f.exists() && f.isFile()) {
			Layer l = parseFishChipsAndMushyPeasFile(model, f);
			startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>(stack));
			stack.push(new StackElement(l.getLayerId(), DataType.INT32));
			return l;
		}
		f = new File(name2);
		if (f.exists() && f.isFile()) {
			Layer l = parseFishChipsAndMushyPeasFile(model, f);
			startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>(stack));
			stack.push(new StackElement(l.getLayerId(), DataType.INT32));
			return l;
		}
		for (File parent : fishPath) {
			if (!parent.isDirectory() && (parent.toPath().getFileName().toString().equals(name1)
					|| parent.toPath().getFileName().toString().equals(name2))) {
				Layer l = parseFishChipsAndMushyPeasFile(model, parent);
				startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>(stack));
				stack.push(new StackElement(l.getLayerId(), DataType.INT32));
				return l;
			} else {
				f = new File(parent, name1);
				if (f.exists() && f.isFile()) {
					Layer l = parseFishChipsAndMushyPeasFile(model, f);
					startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>(stack));
					stack.push(new StackElement(l.getLayerId(), DataType.INT32));
					return l;
				}
				f = new File(name2);
				if (f.exists() && f.isFile()) {
					Layer l = parseFishChipsAndMushyPeasFile(model, f);
					startFishChipsAndMushyPeasLayer(model, l, new ArrayDeque<>(stack));
					stack.push(new StackElement(l.getLayerId(), DataType.INT32));
					return l;
				}
			}
		}
		if (nativeLoaders.containsKey(s)) {
			Layer l = nativeLoaders.get(s).generateLayer(model, stack);
			stack.push(new StackElement(l.getLayerId(), DataType.INT32));
			return l;
		}
		throw new IllegalArgumentException("No such file");
	}

	public static Layer parseFishChipsAndMushyPeasFile(FishChipsAndMushyPeasModel model, File f) {
		FileInputStream input;
		try {
			input = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			int id = model.getMemorySpace().getFreeId();
			Layer l = new FlexibleLayer(id, EnumSet.allOf(DataType.class), 10, 10,
					new FishChipsAndMushyPeasTriggerCallback(id, 0, 0));
			int x = 0;
			int y = 0;
			int data = input.read();
			while (data != -1) {
				if (data == '\n') {
					if (x == 0) {
						l.setCellAt(0, y, new LayerCell(0, DataType.BYTE));
					}
					x = 0;
					y++;
				} else {
					l.setCellAt(x++, y, new LayerCell(data, DataType.BYTE));
				}
				data = input.read();
				if (data == ' ') {
					data = 0;
				}
			}
			return l;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void startFishChipsAndMushyPeasLayer(FishChipsAndMushyPeasModel model, Layer l, Deque<StackElement> stack) {
		model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(), l, 0, 0, Direction.EAST, stack));
	}
}
