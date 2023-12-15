package assign11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import meeting21.Shape;
import meeting21.Rectangle;

/**
 * This class represents a GUI component for displaying an image.
 *
 * @author Prof. Martin and Khang Hoang Nguyen
 * @version Dec 07 2023
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {

	private BufferedImage bufferedImg;
	private ArrayList<Shape> shapes;
	private Color color;
	private Rectangle selectedRectangle;
	private ImageProcessorFrame frame;
	private Image img;

	/**
	 * Creates a new ImagePanel to display the given image.
	 *
	 * @param img - the given image
	 */
	public ImagePanel(Image img, ImageProcessorFrame frame) {
		int rowCount = img.getNumberOfRows();
		int colCount = img.getNumberOfColumns();

		this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < rowCount; i++)
			for (int j = 0; j < colCount; j++)
				this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());

		this.setPreferredSize(new Dimension(colCount, rowCount));

		this.frame = frame;
		this.img = img;
		this.shapes = new ArrayList<Shape>();
		this.color = new Color(105, 105, 105, 125);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times: --when the component first appears --when
	 * the size of the component changes (including resizing by the user) --when
	 * repaint() is called
	 *
	 * Partially overrides the paintComponent method of JPanel.
	 *
	 * @param g -- graphics context onto which we can draw
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.bufferedImg, 0, 0, this);

		if (selectedRectangle != null) {

			g.setColor(this.color);
			g.fillRect(selectedRectangle.getPositionX(), selectedRectangle.getPositionY(), selectedRectangle.getSizeX(),
					selectedRectangle.getSizeY());
			for (Shape o : this.shapes)

				o.paintMe(g);
		}
	}

	private static final long serialVersionUID = 1L;

	/**
	 * When a mouse is dragged. A rectangle will appear indicate the dimension the
	 * user wanted to crop.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		if (this.selectedRectangle != null) {

			this.selectedRectangle.setSize(e.getX() - selectedRectangle.getPositionX(),
					e.getY() - selectedRectangle.getPositionY());
			repaint();
			frame.disableFilterMenuItems();

		}

	}

	/**
	 * Apply a crop filter to the image based on the selected rectangle coordinates.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (this.selectedRectangle != null) {

			img.cropFilter(this.selectedRectangle.getPositionX(), this.selectedRectangle.getPositionY(), e.getY(),
					e.getX());

			repaint();
		}

	}

	/**
	 * Create a new rectangle at the clicked position with initial size and color
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.selectedRectangle = new Rectangle(e.getX(), e.getY(), 0, 0, this.color);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
