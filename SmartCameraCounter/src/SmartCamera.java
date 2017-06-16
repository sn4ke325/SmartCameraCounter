import java.util.*;

import org.opencv.core.Core;

public class SmartCamera {

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Scanner scanner = new Scanner(System.in);
		CameraController camera = new CameraController();
		if (!Arrays.asList(args).contains("-u")) {
			camera.start();
		}

		Runnable inputReader = new Runnable() {
			public void run() {
				try {
					while (true) {
						if (camera.isOn())
							System.out.println("Smart camera is acquiring video...");
						System.out.println("Please input a command");
						String line = scanner.nextLine();
						String output = "";
						switch (line) {
						case "start":
							if (!camera.isOn()) {
								camera.start();
								output = "Camera started.";
							} else {
								output = "Camera has already started.";
							}
							break;

						case "stop":
							if (!camera.isOn())
								output = "Camera is off or has already stopped.";
							else {
								camera.setClosed();
								output = "Camera has stopped.";
								}
							break;
						default:
							output = "Invalid command.";
							break;
						}
						System.out.println(output);

					}
				} catch (IllegalStateException | NoSuchElementException e) {
					System.out.print("System.in was closed by user. Exiting.");
				}

			}
		};

		(new Thread(inputReader)).start();

	}

}
