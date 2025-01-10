package cs3500.solored.model.hw02;

import java.util.Random;

/**
 * Implementation for a solo game of RedSeven.
 * As there is no noteworthy deviation from a standard implementation, this class is essentially the
 * same as {@link AbstractSoloRedGameModel}.
 * The game consists of four structures:
 * <ul>
 *   <li>A deck of cards to draw from</li>
 *   <li>A hand to play from</li>
 *   <li>Four palettes to play to</li>
 *   <li>A canvas that dictates the winning rule</li>
 * </ul>
 * The goal of the game is to use all the cards in the deck
 * while ensuring exactly one palette is winning each round.
 * <p>Notes on this implementation:</p>
 * <ul>
 *   <li>The last card in a list is considered the top card of the virtual stack.</li>
 * </ul>
 *
 */
public class SoloRedGameModel extends AbstractSoloRedGameModel implements RedGameModel<CardImpl> {
  public SoloRedGameModel() {
    super();
  }

  public SoloRedGameModel(Random rand) {
    super(rand);
  }

  /**
   * Draws cards from the deck until the hand is full
   * OR until the deck is empty, whichever occurs first. Newly drawn cards
   * are added to the end of the hand (far-right conventionally).
   * SIDE-EFFECT: Allows the player to play to the canvas again.
   *
   * @throws IllegalStateException if the game has not started or the game is over, or the
   *         palette hasn't been played to since the last draw.
   */
  @Override
  public void drawForHand() {
    drawFullHand();
  }
}
