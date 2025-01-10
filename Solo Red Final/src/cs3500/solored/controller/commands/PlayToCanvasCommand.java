package cs3500.solored.controller.commands;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * A command to call the playToCanvas method of a RedGameModel.
 */
public class PlayToCanvasCommand implements RedGameCommand {
  private final int cardIdxInHand;

  /**
   * A command to call the playToCanvas method of a RedGameModel.
   * @param cardIdxInHand a 1-index number representing the card to play from the hand.
   */
  public PlayToCanvasCommand(int cardIdxInHand) {
    this.cardIdxInHand = cardIdxInHand - 1;
  }

  /**
   * Run this command on the given model.
   * @param model The model to run this command on.
   */
  @Override
  public void execute(RedGameModel model) {
    model.playToCanvas(cardIdxInHand);
  }
}
