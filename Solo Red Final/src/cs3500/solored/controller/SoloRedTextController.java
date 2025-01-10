package cs3500.solored.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import cs3500.solored.controller.commands.PlayToCanvasCommand;
import cs3500.solored.controller.commands.PlayToPaletteCommand;
import cs3500.solored.controller.commands.RedGameCommand;
import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.view.hw02.RedGameView;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * A controller for a game of Solo Red.
 */
public class SoloRedTextController implements RedGameController {
  private final Scanner scan;
  private final Appendable ap;
  private RedGameView view;
  private RedGameModel model;

  // This message is used to create and identify the specific IllegalStateExceptions that should
  // not be stopped in the playGame() method. In other words, IllegalStateExceptions that have this
  // message are allowed to be thrown by the playGame() method.
  private final String IN_OUT_FAIL_MESSAGE = "Input/Output is unavailable!";

  // This message is used to create and identify the specific IllegalStateExceptions that stemmed
  // from quitting the game, and as such can be ignored by parts of the program.
  private final String QUIT_MESSAGE = "Quiting the game...";

  // This map works as a builder, taking in a string and returning an object.
  // Specifically, the object it returns is a builder for a specific RedGameCommand, which uses a
  // scanner to get input to customize said command.
  private final HashMap<String, Function<Scanner, RedGameCommand>> knownCommands;

  /**
   * Creates a new SoloRedTextController, which manages an overall program for a SoloRedGame.
   * @param rd The Readable this controller will use as input.
   * @param ap The Appendable this controller will use as output.
   * @throws IllegalArgumentException if and only if either rd or ap are null.
   */
  public SoloRedTextController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException(
              "Neither the controller's readable nor it's appendable should be null."
      );
    }

    this.ap = ap;
    scan = new Scanner(rd);

    knownCommands = new HashMap<>();
    makeKnownCommands();
  }

  /**
   * Adds all builders for all commands to knownCommands.
   */
  private void makeKnownCommands() {
    knownCommands.put(
            "canvas",
        s -> {
        int[] inputs;
        inputs = getNextNIntegerInputs(s, 1);
        return new PlayToCanvasCommand(inputs[0]);
        }
    );

    knownCommands.put(
            "palette",
        s -> {
        int[] inputs;
        inputs = getNextNIntegerInputs(s, 2);
        return new PlayToPaletteCommand(inputs[0], inputs[1]);
        }
    );
  }

  /**
   * Plays a new game of Solo Red.
   *
   * @param model       The model to play the game with.
   * @param deck        The deck to play the game with.
   * @param shuffle     Whether the deck should be shuffled.
   * @param numPalettes The number of Palettes to use.
   * @param handSize    The size of the player's hand.
   * @throws IllegalArgumentException If the provided model is null, or the game cannot start.
   * @throws IllegalStateException    Only if this controller is unable to successfully receive
   *                                  input or transmit output
   */
  @Override
  public <C extends Card> void playGame(
          RedGameModel<C> model,
          List<C> deck,
          boolean shuffle,
          int numPalettes,
          int handSize
  ) throws IllegalArgumentException, IllegalStateException {

    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null!");
    }
    this.model = model;
    view = new SoloRedGameTextView(this.model, ap);
    try {
      model.startGame(deck, shuffle, numPalettes, handSize);
    } catch (Exception e) {
      throw new IllegalArgumentException("The model was unable to start!", e);
    }

    try {
      while (!model.isGameOver()) {
        try {
          renderView();
        } catch (IOException e) {
          throw new IllegalStateException(IN_OUT_FAIL_MESSAGE, e);
        }
        getInputAndRunCommand();
      }

      endOfGame();
    } catch (IllegalStateException e) {
      if (!e.getMessage().equals(QUIT_MESSAGE)) {
        throw e;
      }
    }
  }

  /**
   * A helper method for playGame(). This method gets the user's inputs and runs the
   * corresponding command, or deals with invalid input.
   * @throws IllegalStateException Only if this controller is unable to successfully receive
   *                               input or transmit output
   */
  private void getInputAndRunCommand() throws IllegalStateException {
    RedGameCommand command;
    if (!scan.hasNext()) {
      throw new IllegalStateException(IN_OUT_FAIL_MESSAGE);
    }
    String input = scan.next();

    if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("Q")) {
      quitGame();
    }

    // The command's builder
    Function<Scanner, RedGameCommand> cmdBuilder =
            knownCommands.getOrDefault(input, null);
    if (cmdBuilder == null) {
      invalidCommand(input);
    } else {
      // Build the command.
      command = cmdBuilder.apply(scan);
      try {
        command.execute(model);
      } catch (IllegalStateException e) {
        // If input/output fails in a way this program can deal with,
        // (i.e. not an IOException), throw an IllegalStateException.
        // If quitting, use that exception to escape.
        if (e.getMessage() != null
                && (e.getMessage().equals(IN_OUT_FAIL_MESSAGE)
                || e.getMessage().equals(QUIT_MESSAGE))
        ) {
          throw e;
        } else {
          // If the message wasn't from I/O errors or quitting,
          // then the model threw an IllegalStateException.
          // If the model threw an exception, then the user must have entered an invalid input.
          badInput(input, e);
        }
      } catch (Exception e) {
        // If the model threw an exception, then the user must have entered an invalid input.
        badInput(input, e);
      }
    }
  }

  /**
   * Gets the next numInputs positive integer inputs from the scanner.
   * If any of the inputs are 'q' or 'Q', quits the game.
   * @param scan The scanner to use as input.
   * @param numInputs The number of inputs desired.
   * @return An array containing numInputs number of integer inputs.
   * @throws IllegalStateException If the scanner is exhausted before the correct number of inputs
   *         are obtained OR if the user quits.
   */
  private int[] getNextNIntegerInputs(
          Scanner scan,
          int numInputs
  ) throws IllegalStateException {
    int[] inputs = new int[numInputs];

    // For every desired input
    for (int i = 0; i < numInputs; i++) {
      while (true) {
        if (!scan.hasNext()) {
          // If there is no remaining input, the game should end
          throw new IllegalStateException(IN_OUT_FAIL_MESSAGE);
        }
        else if (scan.hasNextInt()) {
          int input = scan.nextInt();
          if (input >= 0) {
            // If the next input is an int > 0, collect it and advance the for loop.
            inputs[i] = input;
            break;
          }
        }
        else {

          String temp = scan.next();

          if (temp.equals("q") || temp.equals("Q")) {
            quitGame();
          }
          // Keep looking for the next int...
        }
      }
    }
    return inputs;
  }

  /**
   * Calls the render function of the view and also outputs the number of cards still in the deck.
   */
  private void renderView() throws IOException {
    view.render();
    ap.append("\n")
            .append("Number of cards in deck: ")
            .append(String.valueOf(model.numOfCardsInDeck()))
            .append("\n");
  }

  /**
   * Quits the game and outputs relevant information to the appendable ap.
   * @throws IllegalStateException If input/output fails for some reason, or as part of its
   *                               intended function.
   */
  private void quitGame() throws IllegalStateException {
    try {
      ap.append("Game quit!\n");
      ap.append("State of game when quit:\n");
      renderView();
    } catch (IOException e) {
      throw new IllegalStateException(IN_OUT_FAIL_MESSAGE, e);
    }
    // Used to easily break out of wherever the program ended.
    throw new IllegalStateException(QUIT_MESSAGE);
  }

  /**
   * Outputs to ap that the given command was invalid, and provides a helpful response.
   * @param input The input that didn't match any commands.
   * @throws IllegalStateException If input/output fails for some reason.
   */
  private void invalidCommand(String input) throws IllegalStateException {
    try {
      ap.append("Invalid command. Try again. Valid commands are: 'q', 'Q'");
      for (String key : knownCommands.keySet()) {
        ap.append(", '");
        ap.append(key);
        ap.append("'");
      }
      ap.append("; not '");
      ap.append(input);
      ap.append("'.\n");
    } catch (IOException e) {
      throw new IllegalStateException(IN_OUT_FAIL_MESSAGE, e);
    }
  }

  /**
   * Outputs to ap that the given input was invalid, and provides a helpful response.
   * Input is considered invalid if it causes the model to throw any exception except an
   * IllegalStateException with the message stored as a field of this controller.
   * @param input The string that matched a command.
   * @param exception The exception thrown by the model.
   * @throws IllegalStateException If input/output fails for some reason.
   */
  private void badInput(String input, Exception exception) throws IllegalStateException {
    try {
      ap.append("Invalid move. Try again. ");
      switch (input) {
        case "canvas": {
          if (exception instanceof IllegalStateException) {
            ap.append(
                    "Make sure you aren't playing to the canvas twice"
                            + " or when there is only 1 card in hand."
            );
          } else if (exception instanceof IllegalArgumentException) {
            ap.append("Make sure you're indexes are in range.");
          }
        }
        break;
        case "palette": {
          if (exception instanceof IllegalStateException) {
            ap.append("Make sure you aren't playing to a wining palette.");
          } else if (exception instanceof IllegalArgumentException) {
            ap.append("Make sure you're indexes are in range.");
          }
        }
        break;
        default: ap.append("Are you sure that was a valid command?");
      }
      ap.append("\n");
    } catch (IOException e) {
      throw new IllegalStateException(IN_OUT_FAIL_MESSAGE, e);
    }
  }

  /**
   * Outputs the result of the game to ap.
   * @throws IllegalStateException If input/output fails for some reason.
   */
  private void endOfGame() throws IllegalStateException {
    try {
      if (model.isGameWon()) {
        ap.append("Game won.\n");
      } else {
        ap.append("Game lost.\n");
      }
      renderView();
    } catch (IOException e) {
      throw new IllegalStateException(IN_OUT_FAIL_MESSAGE, e);
    }
  }
}
