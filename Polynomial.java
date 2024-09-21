public class Polynomial {
    double[] coefficients;

    public Polynomial() {
        coefficients = new double[]{0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients.clone();
    }

    public Polynomial add(Polynomial poly_2) {
        int len = Math.max(this.coefficients.length, poly_2.coefficients.length);
        double[] sum = new double[len];

        for (int i = 0; i < len; i++) {
	    double thisCoefficient;
	    double poly_2Coefficient;

	    if (i < this.coefficients.length) {
		thisCoefficient = this.coefficients[i];
	    } else {
	        thisCoefficient = 0;
	    }

            if (i < poly_2.coefficients.length) {
		poly_2Coefficient = poly_2.coefficients[i];
	    } else {
	        poly_2Coefficient = 0;
	    }

            sum[i] = thisCoefficient + poly_2Coefficient;
        }

        return new Polynomial(sum);
    }

    public double evaluate(double value) {
        double ans = 0;
        for (int i = 0; i < coefficients.length; i++) {
            ans += coefficients[i] * Math.pow(value, i);
        }
        return ans;
    }

    public boolean hasRoot(double p_root) {
        if (evaluate(p_root) == 0) {
		    return true;
	    } else {
            return false;
        }
    }
}