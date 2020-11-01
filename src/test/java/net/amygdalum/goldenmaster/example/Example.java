package net.amygdalum.goldenmaster.example;

/*
 * This is an example for legacy code.
 * Actually it is meaningless, but it shares the core properties of legacy code:
 * It is really hard to imagine what this class/method will do!
 */
public class Example {

	private String a;
	private boolean b;
	private int c;
	private double d;

	public Example(String a, boolean b, int c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public String getA() {
		return a;
	}

	public boolean isB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public double getD() {
		return d;
	}

	public Example next() {
		String a = this.b
			? this.a + this.c
			: this.a + this.d;
		boolean b = !this.b;
		int c = (int) this.d * 13;
		int d = -1 / this.c + 1;
		return new Example(a, b, c, d);
	}

}
