package cs3500.solored.model.hw04;

import java.util.Random;

import cs3500.solored.model.hw02.AbstractSoloRedGameModel;
import cs3500.solored.model.hw02.CardImpl;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Advanced implementation for a solo game of RedSeven.
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
 *   <li>Drawing cards follows a more restrictive ruleset than a standard {@link SoloRedGameModel}.
 *   Specifically, only 1 card may be drawn at a time, unless a card was played to the canvas since
 *   the last draw and that card's value is greater than the length of the winning palette. In that
 *   case, 2 cards may be drawn. </li>
 * </ul>
 *
 */
public class AdvancedSoloRedGameModel
        extends AbstractSoloRedGameModel
        implements RedGameModel<CardImpl> {
  private boolean canDrawExtra;

  /**
   * Creates a new AdvancedSoloRedGameModel.
   */
  public AdvancedSoloRedGameModel() {
    super();
    canDrawExtra = false;
  }

  /**
   * Creates a new AdvancedSoloRedGameModel.
   * @param rand The random object to use when shuffling.
   */
  public AdvancedSoloRedGameModel(Random rand) {
    super(rand);
    canDrawExtra = false;
  }


  /**
   * Play the given card from the hand to the losing palette chosen.
   * The card is removed from the hand and placed at the far right
   * end of the palette.
   *
   * @param paletteIdx    a 0-index number representing which palette to play to
   * @param cardIdxInHand a 0-index number representing the card to play from the hand
   * @throws IllegalStateException    if the game has not started or the game is over
   * @throws IllegalArgumentException if paletteIdx < 0 or more than the number of palettes
   * @throws IllegalArgumentException if cardIdxInHand < 0
   *                                  or greater/equal to the number of cards in hand
   * @throws IllegalStateException    if the palette referred to by paletteIdx is winning
   */
  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    // If the player played to the canvas since last draw.
    if (playedToCanvas) {
      if (getPalette(winningPaletteIndex()).size() < getCanvas().getNumber()) {
        canDrawExtra = true;
      }
    }
    super.playToPalette(paletteIdx, cardIdxInHand);
  }

  /**
   * Draws a card from the deck unless the hand is full OR the deck is empty.
   * If a card was played to the canvas since the last draw and that card's value is greater than
   * the length of the winning palette, 2 cards are drawn instead of one.
   * Newly drawn cards are added to the end of the hand (far-right conventionally).
   * SIDE-EFFECT: Allows the player to play to the canvas again.
   *
   * @throws IllegalStateException if the game has not started or the game is over, or the
   *         palette hasn't been played to since the last draw.
   */
  @Override
  public void drawForHand() {
    if (!canDrawToHand()) {
      return;
    }

    hand.add(deck.remove(0));
    if (canDrawExtra) {
      canDrawExtra = false;
      drawForHand();
    }

    playedToCanvas = false;
  }
}
