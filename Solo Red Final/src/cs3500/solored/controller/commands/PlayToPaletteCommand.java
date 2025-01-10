package cs3500.solored.controller.commands;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * A command to call the playToPalette method of a RedGameModel.
 */
public class PlayToPaletteCommand implements RedGameCommand {
  private final int paletteIdx;
  private final int cardIdxInHand;

  /**
   * A command to call the playToPalette method of a RedGameModel.
   * @param paletteIdx a 1-index number representing which palette to play to.
   * @param cardIdxInHand a 1-index number representing the card to play from the hand
   */
  public PlayToPaletteCommand(int paletteIdx, int cardIdxInHand) {
    this.paletteIdx = paletteIdx - 1;
    this.cardIdxInHand = cardIdxInHand - 1;
  }

  /**
   * Run this command on the given model.
   * @param model The model to run this command on.
   */
  @Override
  public void execute(RedGameModel model) {
    model.playToPalette(paletteIdx, cardIdxInHand);
    if (!model.isGameOver()) {
      model.drawForHand();
    }
  }
}
