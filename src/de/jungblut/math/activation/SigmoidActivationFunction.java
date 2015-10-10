package de.jungblut.math.activation;

import org.apache.commons.math3.util.FastMath;

/**
 * Implementation of the sigmoid function.
 * 
 * @author thomas.jungblut
 * 
 */
public final class SigmoidActivationFunction extends AbstractActivationFunction {

  @Override
  public double apply(double input) {
    return sigmoid(input);
  }

  @Override
  public double gradient(double input) {
    return sigmoidGradient(input);
  }

  static double sigmoid(double input) {
    return 1.0 / (1.0 + FastMath.exp(-input));
  }

  static double sigmoidGradient(double input) {
    return sigmoid(input) * (1d - sigmoid(input));
  }

}
