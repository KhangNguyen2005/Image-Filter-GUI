package assign11;

/**
 * @author Khang Hoang Nguyen
 * @version Dec 07 2023
 */
public class Pixel {

	private int redAmount;
	private int greenAmount;
	private int blueAmount;

	/**
	 * This constructor creates a new Pixel object, using the given red, green, and
	 * blue amounts. It throws an IllegalArgumentException if any of the values
	 * passed to the constructor is out of range.
	 * 
	 * @param redAmount   - The red amount.
	 * @param greenAmount - The green amount.
	 * @param blueAmount  - The blue amount.
	 */
	public Pixel(int redAmount, int greenAmount, int blueAmount) {
		this.redAmount = redAmount;
		this.greenAmount = greenAmount;
		this.blueAmount = blueAmount;
		if (redAmount > 255 || greenAmount > 255 || blueAmount > 255 || redAmount < 0 || greenAmount < 0
				|| blueAmount < 0) {
			throw new IllegalArgumentException("Value is out of range");
		}
	}

	/**
	 * Obtains the red amount of the pixel.
	 * 
	 * @return - The red amount, an integer (rgb) value representing the intensity
	 *         of red color.
	 */
	public int getRedAmount() {
		return redAmount;
	}

	/**
	 * Obtains the green amount of the pixel.
	 * 
	 * @return - The green amount, an integer (rgb) value representing the intensity
	 *         of green color.
	 */
	public int getGreenAmount() {
		return greenAmount;
	}

	/**
	 * Obtains the blue amount of the pixel.
	 * 
	 * @return - The blue amount, an integer (rgb) value representing the intensity
	 *         of blue color.
	 */
	public int getBlueAmount() {
		return blueAmount;
	}

	/**
	 * This method returns the red, green, and blue amount of a Pixel object (each a
	 * one-bye integer value) packed into a four-byte int value.
	 * 
	 * @return - A four-byte int value containing the red, green, and blue color
	 *         amounts.
	 */
	public int getPackedRGB() {
		return ((redAmount << 16) | (greenAmount << 8) | blueAmount);

	}

}
