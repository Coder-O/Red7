package cs3500.solored;


import java.io.InputStreamReader;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;

/**
 * The main running class of a game of SoloRed.
 * Allows for configurations to decide what version they play and how many palettes and what hand
 * size to use.
 */
public final class SoloRed {
  /**
   * The main method of this project.
   * This is the starting point that executes the rest of the code.
   * Allows for configurations to decide what version they play and how many palettes and what hand
   * size to use.
   */
  public static void main(String[] args) {
    RedGameModel model;
    switch (args[0]) {
      case "basic": {
        model = RedGameCreator.createGame(RedGameCreator.GameType.BASIC);
        break;
      }
      case "advanced": {
        model = RedGameCreator.createGame(RedGameCreator.GameType.ADVANCED);
        break;
      }
      default:
        throw new IllegalArgumentException("Not a legal game type!!!");
    }

    int p = 4;
    if (args.length > 1) {
      try {
        p = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        System.err.println("Argument" + args[1] + " must be an integer.");
      }
    }

    int h = 7;
    if (args.length > 2) {
      try {
        h = Integer.parseInt(args[2]);

      } catch (NumberFormatException e) {
        System.err.println("Argument" + args[2] + " must be an integer.");
      }
    }

    SoloRedTextController controller = new SoloRedTextController(
            new InputStreamReader(System.in),
            System.out
    );

    // Running the game. If the game would crash, prints out the error message and exits
    try {
      controller.playGame(model, model.getAllCards(), true, p, h);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
