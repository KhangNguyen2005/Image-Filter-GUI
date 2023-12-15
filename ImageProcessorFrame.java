package assign11;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Khang Hoang Nguyen
 * @version Dec 07 2023
 * 
 *          The main frame of the Image Processor application.
 * 
 *          This frame provides a graphical user interface for opening images,
 *          applying filters, and saving the modified images.
 */
public class ImageProcessorFrame extends JFrame implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	private Image image;
	private ImagePanel imagePanel;
	private JMenuItem openingItem;
	private JMenuItem saveItem;
	private JMenuItem redBlueSwapFilter;
	private JMenuItem blackAndWhiteFilter;
	private JMenuItem rotateClockwiseFilter;
	private JMenuItem colorInversion;
	private JMenuItem brightness;
	private JMenuItem crop;
	private JMenuItem zoom;
	private File selectedFile;
	private JFileChooser chooser;
	private JPanel panel;
	private JSlider bSlider;
	private JSlider zSlider;
	private boolean isDrawn = false;

	/**
	 * Creates a new ImageProcessorFrame with a menu bar and initial setup.
	 * Initializes the UI components and sets up event listeners.
	 */
	public ImageProcessorFrame() {
		this.panel = new JPanel();

		this.add(panel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		this.openingItem = new JMenuItem("Opening an image file");
		openingItem.addActionListener(this);
		fileMenu.add(openingItem);

		this.saveItem = new JMenuItem("Save");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);
		menubar.add(fileMenu);
		this.saveItem.setEnabled(false);

		JMenu filterMenu = new JMenu("Filter");
		this.redBlueSwapFilter = new JMenuItem("Red Blue Swap Filter");
		this.redBlueSwapFilter.setToolTipText("Swap the red and blue amounts of the RGB model for color");
		redBlueSwapFilter.addActionListener(this);
		filterMenu.add(redBlueSwapFilter);
		this.redBlueSwapFilter.setEnabled(false);

		this.blackAndWhiteFilter = new JMenuItem("Black And White Filter");
		this.blackAndWhiteFilter.setToolTipText("Swap the black and white amounts of the RGB model for color");
		blackAndWhiteFilter.addActionListener(this);
		filterMenu.add(blackAndWhiteFilter);
		this.blackAndWhiteFilter.setEnabled(false);

		this.rotateClockwiseFilter = new JMenuItem("Rotate Clockwise Filter");
		this.rotateClockwiseFilter.setToolTipText("Rotate the image clockwise (by 90 degrees)");
		rotateClockwiseFilter.addActionListener(this);
		filterMenu.add(rotateClockwiseFilter);
		this.rotateClockwiseFilter.setEnabled(false);

		this.colorInversion = new JMenuItem("Color Inversion");
		this.colorInversion.setToolTipText("Inverts the colors of the image");
		colorInversion.addActionListener(this);
		filterMenu.add(colorInversion);
		this.colorInversion.setEnabled(false);

		this.brightness = new JMenuItem("Brightness");
		this.brightness.setToolTipText("Control the image brightness using the slider below");
		brightness.addActionListener(this);
		filterMenu.add(brightness);
		this.brightness.setEnabled(false);

		this.crop = new JMenuItem("Crop");
		this.crop.setToolTipText(
				"Crop the image using user's mouse. Start from clicking the image then drag to choose the dimension to crop.");
		this.crop.addActionListener(this);
		filterMenu.add(crop);
		this.crop.setEnabled(false);

		this.zoom = new JMenuItem("Zoom ");
		this.zoom.setToolTipText("Use the slider below. Slide left to zoom out, slide right to zoom in.");
		this.zoom.addActionListener(this);
		filterMenu.add(zoom);
		this.zoom.setEnabled(false);

		menubar.add(filterMenu);
		this.setPreferredSize(new Dimension(700, 700));
		this.setTitle("GUI");
		this.setJMenuBar(menubar);
		this.pack();

	}

	/**
	 * Handles the opening of an image file. Enables filter options, opens a file
	 * chooser dialog, loads the selected image, and updates the UI to display the
	 * selected image.
	 */
	private void handleOpeningItemAction() {
		this.redBlueSwapFilter.setEnabled(true);
		this.blackAndWhiteFilter.setEnabled(true);
		this.rotateClockwiseFilter.setEnabled(true);
		this.colorInversion.setEnabled(true);
		this.brightness.setEnabled(true);
		this.crop.setEnabled(true);
		this.zoom.setEnabled(true);

		this.chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG files", "jpg", "jpeg", "png");
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);

		this.selectedFile = new File("/Users/nguyen_hoang_khang/Downloads");
		chooser.setCurrentDirectory(this.selectedFile);

		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.selectedFile = new File(chooser.getSelectedFile().getAbsolutePath());
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
			this.image = new Image(this.selectedFile.getAbsolutePath());
			this.imagePanel = new ImagePanel(this.image, this);
			setContentPane(this.imagePanel);
			revalidate();

		} else {
			JOptionPane.showMessageDialog(null, "alert", "Get File cancelled", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Handles the saving of the modified image to a file.
	 */
	private void handleSaveItemAction() {
		this.chooser = new JFileChooser();
		chooser.setSelectedFile(new File("new_drawing.jpg"));
		chooser.setFileFilter(new FileNameExtensionFilter("JPG  Images", "jpg"));
		chooser.setDialogTitle("Select the location for the new file.");
		if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "Save file cancelled.");
			return;
		}	
		BufferedImage img = new BufferedImage(this.imagePanel.getWidth(), this.imagePanel.getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		this.imagePanel.paint(img.getGraphics());
		try {
			ImageIO.write(img, "jpg", chooser.getSelectedFile());
		}
		catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "The drawing cannot be written to file.");
		}
	
	}

	/**
	 * Handles the action of applying the red-blue swap filter and updating the UI.
	 */
	private void handleredBlueSwapFilterAction() {
		this.image.redBlueSwapFilter();
		this.imagePanel = new ImagePanel(this.image, this);
		setContentPane(this.imagePanel);
		revalidate();

	}

	/**
	 * Handles the action of applying the black and white filter and updating the
	 * UI.
	 */
	private void handleblackAndWhiteFilterAction() {
		this.image.blackAndWhiteFilter();
		this.imagePanel = new ImagePanel(this.image, this);
		setContentPane(this.imagePanel);
		revalidate();
	}

	/**
	 * Handles the action of applying the rotate clockwise filter and updating the
	 * UI.
	 */
	private void handlerotateClockwiseFilterAction() {
		this.image.rotateClockwiseFilter();
		this.imagePanel = new ImagePanel(this.image, this);
		setContentPane(this.imagePanel);
		revalidate();
	}

	/**
	 * Handles the action of applying the color inversion filter and updating the
	 * UI.
	 */
	private void handlecolorInversionFilterAction() {

		this.image.customFilter();
		this.imagePanel = new ImagePanel(this.image, this);
		setContentPane(this.imagePanel);
		revalidate();
	}

	/**
	 * Handles the action to apply a brightness filter to the image. This method
	 * initializes a user interface component (JSlider) for adjusting brightness.
	 * 
	 * The brightness can be adjusted by sliding the slider to the left (negative
	 * values for darker) or to the right (positive values for brighter).
	 */

	private void handleBrightnessFilterAction() {
		this.bSlider = new JSlider(-200, 200, 0);
		this.bSlider.setMajorTickSpacing(50);
		this.bSlider.setMinorTickSpacing(5);
		this.bSlider.setPaintTicks(true);
		this.bSlider.setPaintLabels(true);
		this.bSlider.addChangeListener(this);
		this.bSlider.setEnabled(true);

		JPanel overall = new JPanel(new BorderLayout());
		overall.add(bSlider, BorderLayout.SOUTH);
		overall.add(this.imagePanel, BorderLayout.CENTER);
		this.setContentPane(overall);
		revalidate();
	}

	/**
	 * Handles the action to apply a crop filter to the image. This method
	 * initializes a new ImagePanel with the cropped image and updates the content
	 * pane.
	 */
	private void handleCropFilterAction() {
		this.imagePanel = new ImagePanel(this.image, this);
		this.setContentPane(this.imagePanel);
		revalidate();
	}

	/**
	 * Disables various filter menu items based on the current state of drawing.
	 */
	public void disableFilterMenuItems() {
		this.blackAndWhiteFilter.setEnabled(this.isDrawn);
		this.redBlueSwapFilter.setEnabled(this.isDrawn);
		this.rotateClockwiseFilter.setEnabled(this.isDrawn);
		this.colorInversion.setEnabled(this.isDrawn);
		this.brightness.setEnabled(this.isDrawn);
		this.zoom.setEnabled(this.isDrawn);
	}

	/**
	 * Enables various filter menu items. 
	 */
	public void enableFilterMenuItems() {
		this.isDrawn = true;

		this.blackAndWhiteFilter.setEnabled(this.isDrawn);
		this.redBlueSwapFilter.setEnabled(this.isDrawn);
		this.rotateClockwiseFilter.setEnabled(this.isDrawn);
		this.colorInversion.setEnabled(this.isDrawn);
		this.brightness.setEnabled(this.isDrawn);
		this.zoom.setEnabled(isDrawn);

	}

	/**
	 * Handles the action to apply a zoom filter to the image. 
	 * This method initializes a JSlider for adjusting the zoom level and updates the content
	 * pane. 
	 * 
	 * The zoom level can be adjusted by sliding the slider to the left
	 * (negative values for zooming out) or to the right (positive values for
	 * zooming in).
	 */
	public void handleZoomFilter() {
		this.zSlider = new JSlider(-4, 4, 0);
		this.zSlider.setMajorTickSpacing(2);
		this.zSlider.setPaintTicks(true);
		this.zSlider.setPaintLabels(true);
		this.zSlider.addChangeListener(this);
		this.zSlider.setEnabled(true);

		JPanel overall = new JPanel(new BorderLayout());
		overall.add(zSlider, BorderLayout.SOUTH);
		overall.add(this.imagePanel, BorderLayout.CENTER);
		this.setContentPane(overall);
		revalidate();
	}

	/**
	 * Handles the event triggered when the user chooses to open an image file.
	 * Enables filter options, opens a file chooser dialog, loads the selected
	 * image, and updates the UI to display the selected image.
	 * 
	 * @param e The ActionEvent representing the user's action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.openingItem) {

			handleOpeningItemAction();

		} else if (e.getSource() == this.saveItem) {

			handleSaveItemAction();

		} else if (e.getSource() == this.blackAndWhiteFilter) {
			handleblackAndWhiteFilterAction();
			this.saveItem.setEnabled(true);

		} else if (e.getSource() == this.redBlueSwapFilter) {
			handleredBlueSwapFilterAction();
			this.saveItem.setEnabled(true);

		} else if (e.getSource() == this.rotateClockwiseFilter) {
			handlerotateClockwiseFilterAction();
			this.saveItem.setEnabled(true);

		} else if (e.getSource() == this.colorInversion) {
			handlecolorInversionFilterAction();
			this.saveItem.setEnabled(true);

		} else if (e.getSource() == this.brightness) {
			handleBrightnessFilterAction();
			this.saveItem.setEnabled(true);

		} else if (e.getSource() == this.crop) {
			handleCropFilterAction();
			enableFilterMenuItems();
			this.saveItem.setEnabled(true);
		} else if (e.getSource() == this.zoom) {
			handleZoomFilter();
			this.saveItem.setEnabled(true);

		}
	}

	/**
	 * Invoked when the state of a slider component changes. 
	 */

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == this.bSlider) {
			JSlider src = (JSlider) e.getSource();
			if (!src.getValueIsAdjusting()) {
				int val = (int) src.getValue();

				this.image.brightnessFilter(val);
				this.imagePanel = new ImagePanel(this.image, this);
				setContentPane(this.imagePanel);
				revalidate();

			}
		} else if (e.getSource() == this.zSlider) {
			JSlider src = (JSlider) e.getSource();
			if (!src.getValueIsAdjusting()) {
				int val = (int) src.getValue();

				this.image.customFeatureZoomIn(val);
				this.imagePanel = new ImagePanel(this.image, this);
				setContentPane(this.imagePanel);
				revalidate();
			}
		}
	}
}