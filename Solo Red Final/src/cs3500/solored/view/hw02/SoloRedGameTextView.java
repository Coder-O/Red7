package cs3500.solored.view.hw02;


import java.io.IOException;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * A view of the RedSeven implementation
 * that transmits information to the user.
 */
public class SoloRedGameTextView implements RedGameView {
  private final RedGameModel<?> model;
  private final Appendable appendable;

  /**
   * Creates a new SoloRedGameTextView with the given model.
   * @param model The model to use for the view.
   * @throws IllegalArgumentException if the model is null.
   */
  public SoloRedGameTextView(RedGameModel<?> model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("View cannot take in null arguments!");
    }

    this.model = model;
    this.appendable = new StringBuilder();
  }

  /**
   * Creates a new SoloRedGameTextView with the given model.
   * @param model The model to use for the view.
   * @param appendable The appendable to transmit output to.
   * @throws IllegalArgumentException If the model or appendable are null.
   */
  public SoloRedGameTextView(
          RedGameModel<?> model,
          Appendable appendable
  ) throws IllegalArgumentException {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("View cannot take in null arguments!");
    }

    this.model = model;
    this.appendable = appendable;
  }


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
   * @return A string representation of the state of the game
   */
  @Override
  public String toString() {
    StringBuilder toReturn = new StringBuilder(
            "Canvas: "
                    + model.getCanvas()
                    .toString()
                    .substring(0,1)
                    + "\n"
    );
    for (int i = 0; i < model.numPalettes(); i++) {
      if (i == model.winningPaletteIndex()) {
        toReturn.append("> ");
      }
      toReturn.append("P" + (i + 1) + ":");
      if (model.getPalette(i).isEmpty()) {
        toReturn.append(" ");
      }
      for (Card card : model.getPalette(i)) {
        toReturn.append(" " + card.toString());
      }
      toReturn.append("\n");
    }
    toReturn.append("Hand:");
    if (model.getHand().isEmpty()) {
      toReturn.append(" ");
    }
    for (Card card : model.getHand()) {
      toReturn.append(" " + card);
    }

    return toReturn.toString();
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    appendable.append(this.toString());
  }
}