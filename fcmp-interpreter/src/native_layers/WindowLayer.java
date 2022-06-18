package native_layers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import instruction_pointer.Direction;
import instruction_pointer.StackElement;
import instruction_pointer.FishChipsAndMushyPeasIP;
import layers.DataType;
import layers.Layer;
import layers.LayerCell;
import layers.NativeTriggerCallback;
import layers.FishChipsAndMushyPeasTriggerCallback;
import layers.TriggerCallback;
import main.FishChipsAndMushyPeasModel;

public class WindowLayer extends JComponent implements Layer {
	private class WindowTriggerCallback implements NativeTriggerCallback {

		@Override
		public boolean isFishChipsAndMushyPeas() {
			return false;
		}

		@Override
		public void call(Deque<StackElement> stack) {
			int val = (int) stack.pop().integerValue();
			if (val == 0) {
				stack.clear();
				drawImage();
			} else if (val == 1) {
				stack.clear();
				Point p = getMousePosition();
				if (p != null) {
					stack.push(new StackElement(p.getY(), DataType.INT32));
					stack.push(new StackElement(p.getX(), DataType.INT32));
				}
			}
		}

	}

	private final int layerId;
	private final JFrame frame;
	private int[][] data;
	private BufferedImage im;
	private long nextSendResize = System.currentTimeMillis();
	private boolean sendResize = false;

	public WindowLayer(FishChipsAndMushyPeasModel model, int layerId, FishChipsAndMushyPeasTriggerCallback trigger, int defaultW, int defaultH,
			String name, int keyEvents, int mouseEvents) {
		this.layerId = layerId;
		this.frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		data = new int[defaultW][defaultH];
		im = new BufferedImage(defaultW, defaultH, BufferedImage.TYPE_INT_RGB);
		if (keyEvents != 0) {
			frame.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					if ((keyEvents & 0x01) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getKeyChar(), DataType.INT32));
						stack.push(new StackElement(5, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if ((keyEvents & 0x02) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getKeyChar(), DataType.INT32));
						stack.push(new StackElement(6, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if ((keyEvents & 0x04) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getKeyChar(), DataType.INT32));
						stack.push(new StackElement(7, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));

					}
				}
			});
		}
		if (mouseEvents != 0) {
			frame.addMouseListener(new MouseInputAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if ((mouseEvents & 0x01) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getButton(), DataType.INT32));
						stack.push(new StackElement(e.getY(), DataType.INT32));
						stack.push(new StackElement(e.getX(), DataType.INT32));
						stack.push(new StackElement(8, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));

					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if ((mouseEvents & 0x02) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getButton(), DataType.INT32));
						stack.push(new StackElement(e.getY(), DataType.INT32));
						stack.push(new StackElement(e.getX(), DataType.INT32));
						stack.push(new StackElement(9, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					if ((mouseEvents & 0x04) != 0) {
						Deque<StackElement> stack = new ArrayDeque<>();
						stack.push(new StackElement(e.getModifiersEx(), DataType.INT32));
						stack.push(new StackElement(e.getButton(), DataType.INT32));
						stack.push(new StackElement(e.getY(), DataType.INT32));
						stack.push(new StackElement(e.getX(), DataType.INT32));
						stack.push(new StackElement(10, DataType.INT32));
						stack.push(new StackElement(layerId, DataType.INT32));
						model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
								model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(), trigger.getY(),
								Direction.EAST, stack));
					}
				}
			});
		}
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowIconified(WindowEvent e) {
				Deque<StackElement> stack = new ArrayDeque<>();
				stack.push(new StackElement(2, DataType.INT32));
				stack.push(new StackElement(layerId, DataType.INT32));
				model.addThread(
						new FishChipsAndMushyPeasIP(model.getMemorySpace(), model.getMemorySpace().getLayer(trigger.getLayerId()),
								trigger.getX(), trigger.getY(), Direction.EAST, stack));
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				Deque<StackElement> stack = new ArrayDeque<>();
				stack.push(new StackElement(3, DataType.INT32));
				stack.push(new StackElement(layerId, DataType.INT32));
				model.addThread(
						new FishChipsAndMushyPeasIP(model.getMemorySpace(), model.getMemorySpace().getLayer(trigger.getLayerId()),
								trigger.getX(), trigger.getY(), Direction.EAST, stack));
			}

			@Override
			public void windowClosing(WindowEvent e) {
				Deque<StackElement> stack = new ArrayDeque<>();
				stack.push(new StackElement(1, DataType.INT32));
				stack.push(new StackElement(layerId, DataType.INT32));
				model.addThread(
						new FishChipsAndMushyPeasIP(model.getMemorySpace(), model.getMemorySpace().getLayer(trigger.getLayerId()),
								trigger.getX(), trigger.getY(), Direction.EAST, stack));
			}
		});
		ExecutorService service = Executors.newSingleThreadExecutor();
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				long time = System.currentTimeMillis();
				if (!sendResize && nextSendResize < time) {
					nextSendResize = time + 300;
					data = new int[getWidth()][getHeight()];
					im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
					drawImage();
					Deque<StackElement> stack = new ArrayDeque<>();
					stack.push(new StackElement(getHeight(), DataType.INT32));
					stack.push(new StackElement(getWidth(), DataType.INT32));
					stack.push(new StackElement(4, DataType.INT32));
					stack.push(new StackElement(layerId, DataType.INT32));
					model.addThread(
							new FishChipsAndMushyPeasIP(model.getMemorySpace(), model.getMemorySpace().getLayer(trigger.getLayerId()),
									trigger.getX(), trigger.getY(), Direction.EAST, stack));
				} else if (!sendResize) {
					sendResize = true;
					service.execute(() -> {
						try {
							sendResize = false;
							Thread.sleep(300);
							nextSendResize = System.currentTimeMillis() + 300;
							data = new int[getWidth()][getHeight()];
							im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
							drawImage();
							Deque<StackElement> stack = new ArrayDeque<>();
							stack.push(new StackElement(getHeight(), DataType.INT32));
							stack.push(new StackElement(getWidth(), DataType.INT32));
							stack.push(new StackElement(4, DataType.INT32));
							stack.push(new StackElement(layerId, DataType.INT32));
							model.addThread(new FishChipsAndMushyPeasIP(model.getMemorySpace(),
									model.getMemorySpace().getLayer(trigger.getLayerId()), trigger.getX(),
									trigger.getY(), Direction.EAST, stack));
						} catch (InterruptedException ex) {
						}
					});
				}
			}
		});
		setPreferredSize(new Dimension(defaultW, defaultH));
		frame.add(this);
		frame.setSize(defaultW, defaultH);
		frame.pack();
		frame.setVisible(true);
	}

	private void drawImage() {
		outer: for (int i = 0; i < data.length; i++) {
			int[] js = data[i];
			for (int j = 0; j < js.length; j++) {
				int v = js[j];
				try {
					im.setRGB(i, j, v);
				} catch (ArrayIndexOutOfBoundsException e) {
					break outer;
				}
			}
		}
		repaint();
	}

	@Override
	public Set<DataType> getAllowedDataTypes() {
		return EnumSet.of(DataType.INT32);
	}

	@Override
	public boolean isInfiniteX() {
		return false;
	}

	@Override
	public boolean isInfiniteY() {
		return false;
	}

	@Override
	public int getMaxX() {
		return getWidth();
	}

	@Override
	public int getMaxY() {
		return getHeight();
	}

	@Override
	public TriggerCallback getTriggerCall() {
		return new WindowTriggerCallback();
	}

	@Override
	public LayerCell getCellAt(int x, int y) {
		if (x < getWidth() && y < getHeight()) {
			return new LayerCell(data[x][y], DataType.INT32);
		}
		return new LayerCell(0, DataType.INT32);
	}

	@Override
	public void setCellAt(int x, int y, LayerCell cell) {
		if (cell.getDataType() != DataType.INT32) {
			throw new IllegalArgumentException(
					"Layer only supports 32 bit ints, tried to set value to " + cell.getDataType().name());
		}
		if (x < getWidth() && y < getHeight()) {
			data[x][y] = (int) cell.getData();
		}
	}

	@Override
	public int getLayerId() {
		return layerId;
	}

	@Override
	public void cleanup(boolean isShuttingDown) {
		frame.dispose();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(im, 0, 0, getWidth(), getHeight(), null);
	}

}
