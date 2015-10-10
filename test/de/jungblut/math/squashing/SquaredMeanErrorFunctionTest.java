package de.jungblut.math.squashing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.dense.DenseDoubleMatrix;

public class SquaredMeanErrorFunctionTest {

  @Test
  public void testSmeError() {
    DoubleMatrix y = new DenseDoubleMatrix(new double[] { 0d, 1d, 0d, 1d, 0d },
        1, 5);
    DoubleMatrix hypothesis = new DenseDoubleMatrix(new double[] { 0d, 0d, 0d,
        1d, 0d }, 1, 5);
    double error = new SquaredMeanErrorFunction().calculateError(y, hypothesis);
    assertEquals(1d, error, 1e-4);
  }

}
