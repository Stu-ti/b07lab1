import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;


public class Polynomial {
    double[] coefficients;
    int[] exponents;


    public Polynomial() {
        coefficients = new double[]{0};
        exponents = new int[]{0};
    }


    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients.clone();
        this.exponents = exponents.clone();
    }


    public Polynomial(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        String pstring = scanner.nextLine();
        scanner.close();

        String[] parts = pstring.split("(?=[+-])");

        int numParts = parts.length;
        coefficients = new double[numParts];
        exponents = new int[numParts];

        for (int i =0; i < numParts; i++) {
            String part = parts[i].replaceAll("x\\^", "x");
            
            double coeff;
            int exp;

            if (part.contains("x")) {
                String[] terms = part.split("x");
                coeff = terms[0].isEmpty() || terms[0].equals("+") ? 1: terms[0].equals("-") ? -1 : Double.parseDouble(terms[0]);
                exp = terms.length == 2 ? Integer.parseInt(terms[1]) : 1;
            } else {
                coeff = Double.parseDouble(part);
                exp = 0;
            }
        
        coefficients[i] = coeff;
        exponents[i] = exp;
        }
    }
    

    public Polynomial add(Polynomial poly_2) {
        int len = this.coefficients.length + poly_2.coefficients.length;
        double[] sumCoefficients = new double[len];
        int[] sumExponents = new int[len];

        int i = 0, j = 0, k = 0;
        //continues loop until coefficients from both polynomials have been added
        while (i < this.coefficients.length && j < poly_2.coefficients.length) {
            //if there are more coefficients in the current polynomial AND (if there are no more coefficients in poly_2 or if the current exponent
            //if greater than that of poly_2), then the current coefficient is added to the sum array at index k
            if (this.exponents[i] > poly_2.exponents[j]) {
                sumCoefficients[k] = this.coefficients[i];
                sumExponents[k] = this.exponents[i];
                i++;
            } else if ( this.exponents[i] < poly_2.exponents[j]) {
                sumCoefficients[k] = poly_2.coefficients[j];
                sumExponents[k] = poly_2.exponents[j];
                j++;
            } else {
                sumCoefficients[k] = this.coefficients[i] + poly_2.coefficients[j];
                sumExponents[k] = this.exponents[i];
                i++;
                j++;
            }
            k++;
        }

        while (i < this.coefficients.length) {
            sumCoefficients[k] = this.coefficients[i];
            sumExponents[k] = this.exponents[i];
            i++;
            k++;
        }

        while (j < poly_2.coefficients.length) {
            sumCoefficients[k] = poly_2.coefficients[j];
            sumExponents[k] = poly_2.exponents[j];
            j++;
            k++;
        }

        double[] resultCo = new double[k];
        int[] resultEx = new int[k];
        System.arraycopy(sumCoefficients, 0, resultCo, 0, k);
        System.arraycopy(sumExponents, 0, resultEx, 0, k);

        return new Polynomial(resultCo, resultEx);
    }


    public Polynomial multiply(Polynomial poly_2) {
        //find the max exponent that can be produced (sum of the biggest exponents in both arrays)
        int largestExp = this.exponents[this.exponents.length - 1] + poly_2.exponents[poly_2.exponents.length -1];
        //will hold the coefficients of the multiplied array
        double[] sumCoefficients = new double[largestExp + 1];

        int i = 0;
        //iterates through the coefficients of the current polynomial
        while (i < this.coefficients.length) {
            //iterated through the coefficients of the other polynomial
            for (int j = 0; j < poly_2.coefficients.length; j++) {
                //multiply the coefficients  of the correspinding index from both arrays
                double currentCoefficient = this.coefficients[i] * poly_2.coefficients[j];
                //add the exponents of the corresponding index from both arrays
                int currentExponent = this.exponents[i] + poly_2.exponents[j];  
                sumCoefficients[currentExponent] += currentCoefficient;        
            }
        i++;
        }

        int NonZeroTerms = 0;
        //count how many coefficients are non-zero in the final polynomial
        for (double coefficient: sumCoefficients) {
            if (coefficient != 0) {
                NonZeroTerms++;
            }
        }

        //hold the non-zero coefficients and their exponents
        double[] resultCo = new double[NonZeroTerms];
        int[] resultEx = new int[NonZeroTerms];
        
        //store each non zero coefficient in resultCo and their exponents in resultEx
        int k = 0;
        for (int exp = 0; exp < sumCoefficients.length; exp++) {
            if (sumCoefficients[exp] != 0) {
                resultCo[k] = sumCoefficients[exp];
                resultEx[k] = exp;
                k++;
            }
        }

        return new Polynomial(resultCo, resultEx);
    }


    public double evaluate(double value) {
        double ans = 0;
        for (int i = 0; i < coefficients.length; i++) {
            ans += coefficients[i] * Math.pow(value, exponents[i]);
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


    public void saveToFile(String file) throws IOException {
        FileWriter writer = new FileWriter(file);

        for (int i=0; i < coefficients.length; i++) {
            double coefficient = coefficients[i];
            int exponent = exponents[i];

            if (coefficient > 0 && i != 0) {
                writer.write("+");
            }

            if (exponent == 0) {
                writer.write(String.valueOf(coefficient));
            } else if (exponent == 1) {
                writer.write(coefficient + "x");
            } else {
                writer.write(coefficient + "x" + exponent);
            }
        }
    writer.close();
    }
}