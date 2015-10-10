package de.jungblut.classification.eval;

import com.google.common.base.Preconditions;

import de.jungblut.datastructure.ArrayUtils;
import de.jungblut.math.DoubleVector;

/**
 * Split data class that contains the division of train/test vectors. If no
 * division is yet there, you can use the
 * {@link #create(DoubleVector[], DoubleVector[], float, boolean)} method.
 * 
 * @author thomas.jungblut
 * 
 */
public class EvaluationSplit {

  private final DoubleVector[] trainFeatures;
  private final DoubleVector[] trainOutcome;
  private final DoubleVector[] testFeatures;
  private final DoubleVector[] testOutcome;

  /**
   * Sets a split internally.
   */
  public EvaluationSplit(DoubleVector[] trainFeatures,
      DoubleVector[] trainOutcome, DoubleVector[] testFeatures,
      DoubleVector[] testOutcome) {
    this.trainFeatures = trainFeatures;
    this.trainOutcome = trainOutcome;
    this.testFeatures = testFeatures;
    this.testOutcome = testOutcome;
  }

  public DoubleVector[] getTrainFeatures() {
    return this.trainFeatures;
  }

  public DoubleVector[] getTrainOutcome() {
    return this.trainOutcome;
  }

  public DoubleVector[] getTestFeatures() {
    return this.testFeatures;
  }

  public DoubleVector[] getTestOutcome() {
    return this.testOutcome;
  }

  /**
   * Creates a new evaluation split.
   * 
   * @param features the features of your classifier.
   * @param outcome the target variables of the classifier.
   * @param splitFraction a value between 0f and 1f that sets the size of the
   *          trainingset. With 1k items, a splitPercentage of 0.9f will result
   *          in 900 items to train and 100 to evaluate.
   * @param random true if data needs shuffling before.
   * @return a new {@link EvaluationSplit}.
   */
  public static EvaluationSplit create(DoubleVector[] features,
      DoubleVector[] outcome, float splitFraction, boolean random) {
    Preconditions.checkArgument(features.length == outcome.length,
        "Feature vector and outcome vector must match in length!");
    Preconditions.checkArgument(splitFraction >= 0f && splitFraction <= 1f,
        "splitFraction must be between 0 and 1! Given: " + splitFraction);

    if (random) {
      ArrayUtils.multiShuffle(features, outcome);
    }

    final int splitIndex = (int) (features.length * splitFraction);
    DoubleVector[] trainFeatures = ArrayUtils
        .subArray(features, splitIndex - 1);
    DoubleVector[] trainOutcome = ArrayUtils.subArray(outcome, splitIndex - 1);
    DoubleVector[] testFeatures = ArrayUtils.subArray(features, splitIndex,
        features.length - 1);
    DoubleVector[] testOutcome = ArrayUtils.subArray(outcome, splitIndex,
        outcome.length - 1);
    return new EvaluationSplit(trainFeatures, trainOutcome, testFeatures,
        testOutcome);
  }

}
