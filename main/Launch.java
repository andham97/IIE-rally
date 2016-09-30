package main;

public class Launch {
	public static void main(String[] args) throws Exception {
		Brain b = new Brain();
		b.start();
		b.run();
		b.stop();
	}
}
