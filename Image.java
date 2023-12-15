package assign11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and
 * provides a number of image filters (via instance methods) for changing the
 * appearance of the image. Application of multiple filters is cumulative; e.g.,
 * obj.redBlueSwapFilter() followed by obj.rotateClockwiseFilter() results in an
 * image altered both in color and orientation.
 *
 * Note: - The pixel in the northwest corner of the image is stored in the first
 * row, first column. - The pixel in the northeast corner of the image is stored
 * in the first row, last column. - The pixel in the southeast corner of the
 * image is stored in the last row, last column. - The pixel in the southwest
 * corner of the image is stored in the last row, first column.
 *
 * @author Prof. Martin and Khang Hoang Nguyen
 * @version Dec 07 2023
 */
public class Image {

	private Pixel[][] imageArray;

	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of the given image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}

		imageArray = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for (int i = 0; i < imageArray.length; i++)
			for (int j = 0; j < imageArray[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				imageArray[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
	}

	/**
	 * Create an Image object directly from a pre-made Pixel array. This is
	 * primarily to be used in testing.
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image(Pixel[][] imageArray) {
		this.imageArray = imageArray;
	}

	/**
	 * Create a new "default" Image object, whose purpose is to be used in testing.
	 *
	 * The orientation of this image: cyan red green magenta yellow blue
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image() {
		imageArray = new Pixel[3][2];
		imageArray[0][0] = new Pixel(0, 255, 255); // cyan
		imageArray[0][1] = new Pixel(255, 0, 0); // red
		imageArray[1][0] = new Pixel(0, 255, 0); // green
		imageArray[1][1] = new Pixel(255, 0, 255); // magenta
		imageArray[2][0] = new Pixel(255, 255, 0); // yellow
		imageArray[2][1] = new Pixel(0, 0, 255); // blue
	}

	/**
	 * Gets the pixel at the specified row and column indexes.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param rowIndex    - given row index
	 * @param columnIndex - given column index
	 * @return the pixel at the given row index and column index
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= imageArray.length)
			throw new IndexOutOfBoundsException("rowIndex must be in range 0-" + (imageArray.length - 1));

		if (columnIndex < 0 || columnIndex >= imageArray[0].length)
			throw new IndexOutOfBoundsException("columnIndex must be in range 0-" + (imageArray[0].length - 1));

		return imageArray[rowIndex][columnIndex];
	}

	/**
	 * Writes the image represented by this object to file. Does nothing if the
	 * image length is 0.
	 *
	 * DO NOT MODIFY THIS METHOD
	 *
	 * @param filename - name of image file to write
	 * @throws IOException if file does cannot be written
	 */
	public void writeImage(String filename) {
		if (imageArray.length > 0) {
			BufferedImage imageOutput = new BufferedImage(imageArray[0].length, imageArray.length,
					BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imageArray.length; i++)
				for (int j = 0; j < imageArray[0].length; j++)
					imageOutput.setRGB(j, i, imageArray[i][j].getPackedRGB());

			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			} catch (IOException e) {
				System.out.println("The image cannot be written to file " + filename);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that for each
	 * pixel the red amount and blue amount are swapped.
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void redBlueSwapFilter() {

		for (int i = 0; i < imageArray.length; i++) {
			for (int j = 0; j < imageArray[0].length; j++) {
				Pixel pixel = imageArray[i][j];
				Pixel swapColor = new Pixel(pixel.getBlueAmount(), pixel.getGreenAmount(), pixel.getRedAmount());
				imageArray[i][j] = swapColor;
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that the color
	 * of each pixel is converted to its corresponding grayscale shade, producing
	 * the effect of a black and white photo. The filter sets the amount of red,
	 * green, and blue all to the value of this average: (originalRed +
	 * originalGreen + originalBlue) / 3
	 *
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void blackAndWhiteFilter() {
		for (int i = 0; i < imageArray.length; i++) {
			for (int j = 0; j < imageArray[0].length; j++) {
				Pixel pixel = imageArray[i][j];
				int red = pixel.getRedAmount();
				int blue = pixel.getBlueAmount();
				int green = pixel.getGreenAmount();
				int filter = (red + green + blue) / 3;

				Pixel blackFilter = new Pixel(filter, filter, filter);
				imageArray[i][j] = blackFilter;
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that it is
	 * rotated clockwise (by 90 degrees). This filter rotates directly clockwise, it
	 * should not do this by rotating counterclockwise 3 times.
	 *
	 * HINT: If the image is not square, this filter requires creating a new array
	 * with different lengths. Use the technique of creating and reassigning a new
	 * backing array from BetterDynamicArray (assign06) as a guide for how to make a
	 * second array and eventually reset the imageArray reference to this new array.
	 * Note that we learned how to rotate a square 2D array *left* in Class Meeting
	 * 11.
	 */
	public void rotateClockwiseFilter() {

		int height = imageArray.length;
		int width = imageArray[0].length;
		Pixel[][] rotatedArray = new Pixel[width][height];

		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {

				rotatedArray[row][col] = imageArray[height - col - 1][row];

			}

		}
		imageArray = rotatedArray;
	}

	/**
	 * Color inversion. The process inverts the colors by subtracting each color
	 * component's value from the maximum possible value (usually 255 in an 8-bit
	 * color system) to obtain the new color component value.
	 */
	public void customFilter() {
		for (int i = 0; i < imageArray.length; i++) {
			for (int j = 0; j < imageArray[0].length; j++) {
				Pixel pixel = imageArray[i][j];
				int red = 255 - pixel.getRedAmount();
				int blue = 255 - pixel.getBlueAmount();
				int green = 255 - pixel.getGreenAmount();

				Pixel colorInversion = new Pixel(red, green, blue);
				imageArray[i][j] = colorInversion;
			}
		}
	}

	/**
	 * Gets the number of rows in the image array.
	 * 
	 * @return - number of rows.
	 */
	public int getNumberOfRows() {
		return this.imageArray.length;
	}

	/**
	 * Gets the number of columns in the image array.
	 * 
	 * @return - number of columns, or 0 if the image array is empty.
	 */
	public int getNumberOfColumns() {
		if (this.imageArray.length == 0)
			return 0;
		return this.imageArray[0].length;
	}

	/**
	 * Applies a brightness filter to the image.
	 * 
	 * @param addAmount - amount to add to each color channel for brightness
	 *                  adjustment.
	 */
	public void brightnessFilter(int addAmount) {
		for (int i = 0; i < imageArray.length; i++) {
			for (int j = 0; j < imageArray[0].length; j++) {
				Pixel pixel = imageArray[i][j];
				int red = pixel.getRedAmount() + addAmount;
				int blue = pixel.getBlueAmount() + addAmount;
				int green = pixel.getGreenAmount() + addAmount;

				if (red >= 255 || blue >= 255 || green >= 255) {
					red = 255;
					blue = 255;
					green = 255;
				} else if (red <= 0 || blue <= 0 || green <= 0) {
					red = 0 - addAmount;
					blue = 0 - addAmount;
					green = 0 - addAmount;
				}

				Pixel brightness = new Pixel(red, green, blue);
				imageArray[i][j] = brightness;
			}
		}
	}

	/**
	 * Applies a crop filter to the image, selecting a rectangular region defined by
	 * the given coordinates.
	 * 
	 * @param startX - the starting X-coordinate of the crop region.
	 * @param startY - the starting Y-coordinate of the crop region.
	 * @param endX   - the ending X-coordinate of the crop region.
	 * @param endY   - the ending Y-coordinate of the crop region
	 */
	public void cropFilter(int startX, int startY, int endX, int endY) {
		Pixel[][] croppedImage = new Pixel[endX - startX + 1][endY - startY + 1];

		for (int i = startX; i <= endX; i++) {
			for (int j = startY; j <= endY; j++) {
				croppedImage[i - startX][j - startY] = imageArray[i][j];
			}
		}

		this.imageArray = croppedImage;

	}

	/**
	 * Applies a custom zoom filter to the image.
	 * 
	 * @param addAmount - amount to adjust the zoom level.
	 */
	public void customFeatureZoomIn(double addAmount) {
		int height = imageArray.length;
		int width = imageArray[0].length;

		double zoomFactor = 0.00001 + addAmount;

		if (zoomFactor > 1) {
			int newHeight = (int) (height * zoomFactor);
			int newWidth = (int) (width * zoomFactor);

			Pixel[][] zoomedArray = new Pixel[newHeight][newWidth];

			for (int row = 0; row < newHeight; row++) {
				for (int col = 0; col < newWidth; col++) {

					int originalRow = (int) (row / zoomFactor);
					int originalCol = (int) (col / zoomFactor);

					zoomedArray[row][col] = imageArray[originalRow][originalCol];

				}
			}

			imageArray = zoomedArray;

		} else if (zoomFactor < 0) {
			zoomFactor = addAmount * addAmount;
			int newHeight = (int) (height / zoomFactor);
			int newWidth = (int) (width / zoomFactor);

			Pixel[][] zoomedArray = new Pixel[newHeight][newWidth];

			for (int row = 0; row < newHeight; row++) {
				for (int col = 0; col < newWidth; col++) {
					int originalRow = (int) (row * zoomFactor);
					int originalCol = (int) (col * zoomFactor);

					zoomedArray[row][col] = imageArray[originalRow][originalCol];
				}
			}

			imageArray = zoomedArray;
		}
	}

}
