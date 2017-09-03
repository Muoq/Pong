import java.awt.*;
import static java.lang.Math.*;

/**
 * Created by victo on 02-08-2017.
 */
public class Screen {

	private int width, height;
	Graphics g;
	public int[] pixels;

	int color;
	int backgroundColor;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;

		pixels = new int[this.width * this.height];

		this.color = 0xffffff;
	}

	public void clear() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = this.backgroundColor;
			}
		}
	}

	public void render() {
		for (int y = 0; y < height; y++) {
			if (y < 0 || y >= height) break;
			for (int x = 0; x < width; x++) {
				if (x < 0 || x >= width) break;
				pixels[x + y * width] = this.color;
			}
		}
	}

	public void fillRect(int x, int y, int widthArg, int heightArg) {
		boolean isOOBX = false; //out of bounds
		boolean isOOBY = false;

		for (int xx = 0; xx < widthArg; xx++) {

			if (x + xx >= width)
				break;
			if (x + xx < 0)
				isOOBX = true;
			else
				isOOBX = false;
			for (int yy = 0; yy < heightArg; yy++) {
				if (y + yy >= height)
					break;
				if (y + yy < 0)
					isOOBY = true;
				else
					isOOBY = false;

				if (!isOOBX & !isOOBY) {
					pixels[x + y * this.width + xx + yy * width] = this.color;
				}
			}
		}
	}

	public void fillCircle(int x, int y, int radius) {
		boolean isOOBX = false;
		boolean isOOBY = false;

		for (int xx = 0; xx < radius * 2; xx++) {
			for (int yy = 0; yy < radius * 2; yy++) {
				if (pow(xx, 2) + pow(yy, 2) - pow(radius, 2) <= 0) {
					pixels[x + y * this.width + xx + yy * width] = this.color;
				}
			}
		}
	}

	public void setColor(int c) {
		this.color = c;
	}

	public void setBackgroundColor(int c) {
		this.backgroundColor = c;
	}
}
