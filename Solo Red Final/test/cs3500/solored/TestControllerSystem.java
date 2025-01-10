package cs3500.solored;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.solored.controller.RedGameController;
import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;


/**
 * Tests for a controller interface with working implementations of other necessary classes.
 * Should be able to swap out any implementation of RedGameController in the initControllerTests
 * function.
 */
public class TestControllerSystem {
  private RedGameController controller;
  private Appendable appendable;
  private RedGameModel model;

  @Before
  public void initControllerTests() {
    initControllerTests("q");
  }

  private void initControllerTests(String input) {
    appendable = new StringBuilder();
    model = new SoloRedGameModel();
    Readable readable = new StringReader(input);
    controller = new SoloRedTextController(readable, appendable);
  }

  private void initFaultyControllerTests() {
    initControllerTests();

    Appendable faulty_appendable = new FaultyAppendable();
    Readable faulty_readable = new FaultyReadable();
    controller = new SoloRedTextController(faulty_readable, faulty_appendable);
  }

  @Test
  public void testControllerWorksEmptyRead() {
    initControllerTests("");

    Assert.assertThrows(
            "If input/output fails, should throw an IllegalStateException!",
            IllegalStateException.class,
        () -> controller.playGame(model, model.getAllCards(), false, 5, 5)
    );

    Assert.assertEquals(
            "Despite I/O Failure, the game should have started.",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1 O2 O3\n"
                    + "Number of cards in deck: 25\n",
            appendable.toString()
    );
  }

  @Test
  public void tes1TurnPaletteLost() {
    initControllerTests("palette 1 3");

    controller.playGame(model, model.getAllCards(), false, 5, 5);

    Assert.assertEquals(
            "This move should loose!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1 O2 O3\n"
                    + "Number of cards in deck: 25\n"
                    + "Game lost.\n"
                    + "Canvas: R\n"
                    + "P1: R1 O1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O2 O3\n"
                    + "Number of cards in deck: 25\n",
            appendable.toString()
    );
  }

  @Test
  public void test1TurnPaletteWin() {
    initControllerTests("palette 1 1");

    controller.playGame(model, model.getAllCards().subList(0,6), false, 5, 1);

    Assert.assertEquals(
            "This move should win!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6\n"
                    + "Number of cards in deck: 0\n"
                    + "Game won.\n"
                    + "Canvas: R\n"
                    + "> P1: R1 R6\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "P5: R5\n"
                    + "Hand: \n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }

  @Test
  public void testPlayToCanvas() {
    initControllerTests("canvas 3 Q");

    controller.playGame(model, model.getAllCards().subList(0,8), false, 5, 3);

    Assert.assertEquals(
            "Play to canvas then quit",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Canvas: O\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7\n"
                    + "Number of cards in deck: 0\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: O\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7\n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }

  @Test
  public void testQuit() {
    String[] quitingInputs = {"q", "Q", "q canvas 1", "Q aoiDhfOwOaef"};

    for (String input : quitingInputs) {
      initControllerTests(input);

      controller.playGame(model, model.getAllCards(), false, 5, 5);

      Assert.assertEquals(
              "Quit should work!",
              "Canvas: R\n"
                      + "P1: R1\n"
                      + "P2: R2\n"
                      + "P3: R3\n"
                      + "P4: R4\n"
                      + "> P5: R5\n"
                      + "Hand: R6 R7 O1 O2 O3\n"
                      + "Number of cards in deck: 25\n"
                      + "Game quit!\n"
                      + "State of game when quit:\n"
                      + "Canvas: R\n"
                      + "P1: R1\n"
                      + "P2: R2\n"
                      + "P3: R3\n"
                      + "P4: R4\n"
                      + "> P5: R5\n"
                      + "Hand: R6 R7 O1 O2 O3\n"
                      + "Number of cards in deck: 25\n",
              appendable.toString()
      );
    }
  }

  @Test
  public void testQuitInCommand() {
    initControllerTests("canvas Q");

    controller.playGame(model, model.getAllCards().subList(0,8), false, 5, 3);

    Assert.assertEquals(
            "Start to play to canvas but quit instead.",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }

  @Test
  public void testInvalidCommand() {
    initControllerTests("asdfpaiefj quit adoij qqqQQQ pallette 1 1 canvass 20 hiqpqqQ q");

    controller.playGame(model, model.getAllCards().subList(0,8), false, 5, 3);

    Assert.assertEquals(
            "Try invalid commands and then quit.",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'asdfpaiefj'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'quit'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'adoij'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'qqqQQQ'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'pallette'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not '1'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not '1'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'canvass'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not '20'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid command. Try again. "
                    + "Valid commands are: 'q', 'Q', 'canvas', 'palette'; not 'hiqpqqQ'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }

  @Test
  public void testInvalidMove() {
    initControllerTests("canvas 3 canvas 2 q");

    controller.playGame(model, model.getAllCards().subList(0,8), false, 5, 3);

    Assert.assertEquals(
            "Play to canvas twice then quit",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7 O1\n"
                    + "Number of cards in deck: 0\n"
                    + "Canvas: O\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid move. Try again. "
                    + "Make sure you aren't playing to the canvas twice"
                    + " or when there is only 1 card in hand.\n"
                    + "Canvas: O\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7\n"
                    + "Number of cards in deck: 0\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: O\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "> P5: R5\n"
                    + "Hand: R6 R7\n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }

  @Test
  public void testExceptionNullModel() {
    Assert.assertThrows(
            "The controller should throw an exception when passed a null model!!!",
            IllegalArgumentException.class,
        () -> controller.playGame(null, model.getAllCards(), false, 5, 5)
    );
  }

  @Test
  public void testControllerCantStartTwice() {
    controller.playGame(model, model.getAllCards(), false, 5, 5);
    Assert.assertThrows(
            "The controller should throw an exception when the game cannot start!!!",
            IllegalArgumentException.class,
        // Cannot start a model twice
        () -> controller.playGame(model, model.getAllCards(), false, 5, 5)
    );
  }

  @Test
  public void testControllerExceptionWhenIllegalModelStartState() {
    Assert.assertThrows(
            "The controller should throw an exception when the game cannot start!!!",
            IllegalArgumentException.class,
        // numPalettes is less than 2
        () -> controller.playGame(model, model.getAllCards(), false, 1, 5)
    );
    Assert.assertThrows(
            "The controller should throw an exception when the game cannot start!!!",
            IllegalArgumentException.class,
        // handSize <= 0
        () -> controller.playGame(model, model.getAllCards(), false, 5, 0)
    );
    Assert.assertThrows(
            "The controller should throw an exception when the game cannot start!!!",
            IllegalArgumentException.class,
        // deck isn't big enough to set up the game
        () -> controller.playGame(model, List.of(), false, 5, 5)
    );

    List nulList = new ArrayList<>();
    nulList.add(null);
    Assert.assertThrows(
            "The controller should throw an exception when the game cannot start!!!",
            IllegalArgumentException.class,
        // deck has non-unique cards or null cards
        () -> controller.playGame(model, nulList, false, 5, 5)
    );
  }

  @Test
  public void testControllerNoExceptions() {
    controller.playGame(model, model.getAllCards(), false, 5, 5);
    Assert.assertTrue(
            "The playGame method did not throw an exception, as expected.",
            true
    );
  }

  @Test
  public void testControllerExceptionIOFail() {
    initFaultyControllerTests();
    Assert.assertThrows(
            "The controller should throw an exception when input or output are unavailable!!!",
            IllegalStateException.class,
        () -> controller.playGame(model, model.getAllCards(), false, 5, 5)
    );
  }

  // Everything before here was tests I wrote before implementing, although I refactored them a bit
  // after.

  @Test
  public void testOnlyRereadValuesNotCommand() {
    initControllerTests("palette aajfahg 1 1 palette 2 palette 1 q");

    controller.playGame(model, model.getAllCards(), false, 4, 7);

    Assert.assertEquals(
            "This command should still have its input read correctly!!!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "> P4: R4\n"
                    + "Hand: R5 R6 R7 O1 O2 O3 O4\n"
                    + "Number of cards in deck: 24\n"
                    + "Canvas: R\n"
                    + "> P1: R1 R5\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "Hand: R6 R7 O1 O2 O3 O4 O5\n"
                    + "Number of cards in deck: 23\n"
                    + "Canvas: R\n"
                    + "P1: R1 R5\n"
                    + "> P2: R2 R6\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "Hand: R7 O1 O2 O3 O4 O5 O6\n"
                    + "Number of cards in deck: 22\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: R\n"
                    + "P1: R1 R5\n"
                    + "> P2: R2 R6\n"
                    + "P3: R3\n"
                    + "P4: R4\n"
                    + "Hand: R7 O1 O2 O3 O4 O5 O6\n"
                    + "Number of cards in deck: 22\n",
            appendable.toString()
    );
  }

  @Test
  public void testCanQuitInPalettePaletteIdx() {
    initControllerTests("palette q");

    controller.playGame(model, model.getAllCards(), false, 4, 7);

    Assert.assertEquals(
            "This command should quit without doing anything!!!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "> P4: R4\n"
                    + "Hand: R5 R6 R7 O1 O2 O3 O4\n"
                    + "Number of cards in deck: 24\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "> P4: R4\n"
                    + "Hand: R5 R6 R7 O1 O2 O3 O4\n"
                    + "Number of cards in deck: 24\n",
            appendable.toString()
    );
  }

  @Test
  public void testCanQuitInPaletteCardIdx() {
    initControllerTests("palette 1 q");

    controller.playGame(model, model.getAllCards(), false, 4, 7);

    Assert.assertEquals(
            "This command should quit without doing anything!!!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "> P4: R4\n"
                    + "Hand: R5 R6 R7 O1 O2 O3 O4\n"
                    + "Number of cards in deck: 24\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "P2: R2\n"
                    + "P3: R3\n"
                    + "> P4: R4\n"
                    + "Hand: R5 R6 R7 O1 O2 O3 O4\n"
                    + "Number of cards in deck: 24\n",
            appendable.toString()
    );
  }

  @Test
  public void testFullGame() {
    initControllerTests(
            "pallette paplet palete palette 1 ad quit 1 canvas qwe 3 palette 2 1 palette 1 1"
    );
    controller.playGame(model, model.getAllCards().subList(0,5), false, 2, 2);

    Assert.assertEquals(
            "This input should win the game!!!",
            "Canvas: R\n"
                    + "P1: R1\n"
                    + "> P2: R2\n"
                    + "Hand: R3 R4\n"
                    + "Number of cards in deck: 1\n"
                    + "Invalid command. Try again. Valid commands are: "
                    + "'q', 'Q', 'canvas', 'palette'; not 'pallette'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "> P2: R2\n"
                    + "Hand: R3 R4\n"
                    + "Number of cards in deck: 1\n"
                    + "Invalid command. Try again. Valid commands are: "
                    + "'q', 'Q', 'canvas', 'palette'; not 'paplet'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "> P2: R2\n"
                    + "Hand: R3 R4\n"
                    + "Number of cards in deck: 1\n"
                    + "Invalid command. Try again. Valid commands are: "
                    + "'q', 'Q', 'canvas', 'palette'; not 'palete'.\n"
                    + "Canvas: R\n"
                    + "P1: R1\n"
                    + "> P2: R2\n"
                    + "Hand: R3 R4\n"
                    + "Number of cards in deck: 1\n"
                    + "Canvas: R\n"
                    + "> P1: R1 R3\n"
                    + "P2: R2\n"
                    + "Hand: R4 R5\n"
                    + "Number of cards in deck: 0\n"
                    + "Invalid move. Try again. Make sure you're indexes are in range.\n"
                    + "Canvas: R\n"
                    + "> P1: R1 R3\n"
                    + "P2: R2\n"
                    + "Hand: R4 R5\n"
                    + "Number of cards in deck: 0\n"
                    + "Canvas: R\n"
                    + "P1: R1 R3\n"
                    + "> P2: R2 R4\n"
                    + "Hand: R5\n"
                    + "Number of cards in deck: 0\n"
                    + "Game won.\n"
                    + "Canvas: R\n"
                    + "> P1: R1 R3 R5\n"
                    + "P2: R2 R4\n"
                    + "Hand: \n"
                    + "Number of cards in deck: 0\n",
            appendable.toString()
    );
  }
}
