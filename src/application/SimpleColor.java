package application;

public class SimpleColor {
    private double r;
    private double g;
    private double b;

    public SimpleColor(double r, double g, double b) {
	this.r = r;
	this.g = g;
	this.b = b;
    }

    public double getR() {
	return r;
    }

    public double getG() {
	return g;
    }

    public double getB() {
	return b;
    }

    public void setR(double r) {
	this.r = Math.min(Math.max(0, r), 1);
    }

    public void setG(double g) {
	this.g = Math.min(Math.max(0, g), 1);
    }

    public void setB(double b) {
	this.b = Math.min(Math.max(0, b), 1);
    }
}
