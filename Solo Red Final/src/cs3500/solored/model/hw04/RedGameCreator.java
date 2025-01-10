package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * A factory for the different types of RedGameModel implementations.
 */
public class RedGameCreator {
  /**
   * Represents a type of RedGameModel implementation.
   */
  public enum GameType {
    BASIC, ADVANCED
  }

  /**
   * Creates a RedGameModel of the given type.
   * @param type The GameType to use.
   * @return A new object of the corresponding implementation type.
   */
  public static RedGameModel createGame(GameType type) {
    switch (type) {
      case BASIC:
        return new SoloRedGameModel();
      case ADVANCED:
        return new AdvancedSoloRedGameModel();
      default:
        // This should never happen. There are only two GameTypes
        return null;
    }
  }
}
