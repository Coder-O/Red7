package cs3500.solored.view.hw02;

import java.io.IOException;

/**
 * Behaviors needed for a view of the RedSeven implementation
 * that transmits information to the user.
 */
public interface RedGameView {

  /**
   * Creates a String with state of the game.
   * This rendering includes
   * <ul>
   *   <li>The color of the card on the Canvas</li>
   *   <li>Each palette from P1 to Pn, where n is the number of palettes, where each palette
   *   has all of its card printed with one space between them</li>
   *   <li>A greater than symbol indicating the winning palette</li>
   *   <li>The hand, where all cards are printed with one space between them</li>
   * </ul>
   * An example below for a 4-palette, 7-hand game in-progress
   * Canvas: R
   * P1: R6 B1
   * > P2: R7
   * P3: V1
   * P4: I2
   * Hand: V2 I3 R1 O2 G6 R5 O1
   * @return a String representation of the game at its current state post-start
   */
  String toString();

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}
