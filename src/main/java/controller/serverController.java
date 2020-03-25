package controller;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;


public class serverController {

	int width = 1800;
	int height = 1800;
	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


	static final double DEFAULT_ZOOM       = 100.0;
	static final double DEFAULT_TOP_LEFT_X = -3.0;
	static final double DEFAULT_TOP_LEFT_Y = +3.0;


	double zoomFactor = DEFAULT_ZOOM;
	double topLeftX = DEFAULT_TOP_LEFT_X ;
	double topLeftY = DEFAULT_TOP_LEFT_Y;


	public File testController(double zoom, double topLeftX, double topLeftY ,int MAX_ITER) throws Exception {

		zoomFactor = zoom;
		this.topLeftX= topLeftX;
		this.topLeftY=topLeftY;
		//		x0 = cx + (i/width - 0.5)*lx;

		//		y0 = cy + (j/width - 0.5)*ly;

		System.out.println(this.topLeftX);
		System.out.println(this.topLeftY);
		updateFractal(this.topLeftX, this.topLeftY ,MAX_ITER );

		File file = new File("caspar.jpg");
		ImageIO.write(img, "jpg", file);

		return file;

	}

	private double getXPos(double x, double topLeftX) {

		return x/zoomFactor - topLeftX/zoomFactor;
	} // getXPos

	// -------------------------------------------------------------------

	private double getYPos(double y, double topLeftY) {
		return y/zoomFactor  - topLeftY/zoomFactor;
	} // getYPos


	public void updateFractal(double topLeftX, double topLeftY, int MAX_ITER ) { //Syncronize

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		AtomicInteger aX = new AtomicInteger(), aY = new AtomicInteger(), aMAX = new AtomicInteger(MAX_ITER);
//		AtomicReference<Double> ac_r = new AtomicReference<Double>(), ac_i = new AtomicReference<Double>();
		for (int x = 0; x < width; x++ ) {
			for (int y = 0; y < height; y++ ) {

//				double c_r = getXPos(x, topLeftX) * (100/zoomFactor)*0.5;
//				double c_i = getYPos(y, topLeftY) * (100/zoomFactor)*0.5;

//				int iterCount;
				aX.set(x);
				aY.set(y);
				
				//AtomicReference<Double> ac_r = new AtomicReference<Double>(c_r), ac_i = new AtomicReference<Double>(c_i);
				
//				executorService.execute(new Runnable() {
//				    public void run() {
				        setPixelColor(0.0, 0.0, aMAX, aX, aY);
//				    }
//				});


				
//
//				iterCount = computeIterations(c_r, c_i, MAX_ITER); // thread
//
//
//
//
//				int pixelColor = makeColor(iterCount);
//
//				img.setRGB(x, y, pixelColor);

			}
		}



	} 


	private int computeIterations(double c_r, double c_i, int MAX_ITER) {

		/*

		Let c = c_r + c_i
		Let z = z_r + z_i

		z' = z*z + c
		   = (z_r + z_i)(z_r + z_i) + (c_r + c_i)
		   = z_r² + 2*z_r*z_i - z_i² + c_r + c_i
		     z_r' = z_r² - z_i² + c_r
		     z_i' = 2*z_i*z_r + c_i

		 */

		double z_r = 0.0;
		double z_i = 0.0;

		int iterCount = 0;

		// Modulus (distance) formula:
		// √(a² + b²) <= 2.0
		// a² + b² <= 4.0
		while ( z_r*z_r + z_i*z_i <= 4.0 ) {

			double z_r_tmp = z_r;

			z_r = z_r*z_r - z_i*z_i + c_r;
			z_i = 2*z_i*z_r_tmp + c_i;

			// Point was inside the Mandelbrot set
			if (iterCount >= MAX_ITER) {
				return MAX_ITER;
			}


			iterCount++;

		}

		// Complex point was outside Mandelbrot set
		return iterCount;

	} // computeIterations

	private int makeColor( int iterCount ) {

		int color = 0b011011100001100101101000; 
		int mask  = 0b000000000000010101110111; 
		int shiftMag = iterCount / 13;

		if (iterCount == 256) 
			return Color.BLACK.getRGB();

		return color | (mask << shiftMag);

	} // makeColor
	
	public void setPixelColor(double c_r, double c_i, AtomicInteger MAX_ITER, AtomicInteger aX, AtomicInteger aY ) {
		
		
		c_r = getXPos(aX.get(), topLeftX) * (100/zoomFactor)*0.5;
		c_i = getYPos(aY.get(), topLeftY) * (100/zoomFactor)*0.5;
		
		int iterCount;		

		iterCount = computeIterations(c_r, c_i, MAX_ITER.get()); // thread

		int pixelColor = makeColor(iterCount);

		img.setRGB(aX.get(), aY.get(), pixelColor);
		
	}

}
