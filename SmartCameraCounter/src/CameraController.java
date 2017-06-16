import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CameraController {

	private VideoCapture capture;
	private ScheduledExecutorService frameCaptureTimer;
	private int cameraId;
	private boolean cameraStatus;

	public CameraController() {
		this.init();
	}

	protected void init() {
		this.cameraStatus = false;
		this.capture = new VideoCapture();
		this.cameraId = 0;

	}

	public void start() {
		if (!this.cameraStatus) {
			// use open to also load a video instead of real time camera feed
			this.capture.open(cameraId);

			if (capture.isOpened()) {
				cameraStatus = true;
				Runnable frameGrabber = new Runnable() {
					@Override
					public void run() {
						Mat frame = grabFrame();

					}

				};

				this.frameCaptureTimer = Executors.newSingleThreadScheduledExecutor();
				this.frameCaptureTimer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

			} else {
				System.err.println("Cannot open camera connection.");
			}
		} else {// camera is already on
			System.out.println("Camera is already active");
		}

	}

	public boolean isOn() {
		return this.cameraStatus;
	}

	public void setClosed() {
		this.stopAcquisition();
	}

	// private methods
	private void stopAcquisition() {
		if (this.frameCaptureTimer != null && !this.frameCaptureTimer.isShutdown()) {
			try {
				this.frameCaptureTimer.shutdown();
				this.frameCaptureTimer.awaitTermination(33, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}

		if (this.capture.isOpened()) {
			this.capture.release();
		}
		
		this.cameraStatus = false;
	}

	private Mat grabFrame() {
		Mat frame = new Mat();
		if (capture.isOpened()) {
			try {
				this.capture.read(frame);
			} catch (Exception e) {
				System.err.println("An error occurred during frame capture " + e);
			}
		}
		return frame;
	}

}
