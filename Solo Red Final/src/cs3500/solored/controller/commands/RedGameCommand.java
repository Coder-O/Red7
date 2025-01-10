package cs3500.solored.controller.commands;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * A command for a RedGame controller and model.
 */
public interface RedGameCommand {

  /**
   * Run this command on the given model.
   * @param model The model to run this command on.
   */
  void execute(RedGameModel model);
}
