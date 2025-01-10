package cs3500.solored.controller;

import java.util.List;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * A controller for a game of Solo Red.
 */
public interface RedGameController {

  /**
   * Plays a new game of Solo Red.
   * @param model The model to play the game with.
   * @param deck The deck to play the game with.
   * @param shuffle Whether the deck should be shuffled.
   * @param numPalettes The number of Palettes to use.
   * @param handSize The size of the player's hand.
   * @param <C> The type of card the model uses. Must extend the Card interface
   * @throws IllegalArgumentException If the provided model is null, or the game cannot start.
   * @throws IllegalStateException Only if this controller is unable to successfully receive input
   *         or transmit output
   */
  <C extends Card> void playGame(
          RedGameModel<C> model,
          List<C> deck,
          boolean shuffle,
          int numPalettes,
          int handSize
  ) throws IllegalArgumentException, IllegalStateException;
}
