package cs3500.solored;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.CardColor;
import cs3500.solored.model.hw02.CardImpl;
import cs3500.solored.model.hw02.CardNumber;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.RedGameView;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * General tests of the model from the client's perspective. Should work with any implementation.
 */
public abstract class GeneralModelTests {
  protected RedGameModel model;
  protected RedGameView view;
  protected List<Card> fullDeck;

  @Before
  public abstract void initCommonFields();

  @Test
  public void testModelWorks() {
    model.startGame(fullDeck, false, 4, 5);

    Assert.assertEquals(
            "The view and model should have the predicted display!",
            "Canvas: R\nP1: R1\nP2: R2\nP3: R3\n> P4: R4\nHand: R5 R6 R7 O1 O2",
            view.toString()
    );
  }

  @Test
  public void testNormalGame() {
    // This test was formed manually by me playing the game naturally.
    RedGameModel semiRandModel = new SoloRedGameModel(new Random(12024));
    RedGameView semiRandView = new SoloRedGameTextView(semiRandModel);

    semiRandModel.startGame(semiRandModel.getAllCards(), true, 4, 5);

    System.out.println(semiRandView + "\n");
    semiRandModel.playToCanvas(3);
    System.out.println(semiRandView + "\n");

    semiRandModel.playToPalette(2, 3);
    semiRandModel.drawForHand();
    System.out.println(semiRandView + "\n");

    semiRandModel.playToCanvas(0);
    System.out.println(semiRandView + "\n");

    semiRandModel.playToPalette(1, 1);
    semiRandModel.drawForHand();
    System.out.println(semiRandView + "\n");

    semiRandModel.playToCanvas(4);
    System.out.println(semiRandView + "\n");

    Assert.assertEquals(
            "A normal game should continue as expected!",
            "Canvas: I\n"
                    + "P1: I2\n"
                    + "> P2: R6 B3\n"
                    + "P3: I4 B1\n"
                    + "P4: B6\n"
                    + "Hand: O4 O3 O1 R1",
            semiRandView.toString());
  }

  @Test
  public void testModelShuffles() {
    model.startGame(fullDeck, true, 2, 33);

    Assert.assertNotEquals(
            "The deck should be shuffled! This setup suggests that it doesn't! Note that "
                    + "it is possible for a correct implementation to fail this test, though "
                    + "unlikely",
            fullDeck.subList(2,fullDeck.size()),
            model.getHand()
    );
  }

  @Test
  public void testNoMutationAllowed() {
    model.startGame(fullDeck, false, 4, 7);

    int orignialHandSize = model.getHand().size();
    int orignialPaletteSize = model.getPalette(0).size();

    try {
      model.getHand().add(new CardImpl(CardColor.RED, CardNumber.ONE));
    } catch (Exception ignored) {

    }

    try {
      model.getPalette(0).add(new CardImpl(CardColor.RED, CardNumber.ONE));
    } catch (Exception ignored) {

    }

    Assert.assertEquals(
            "getHand should not expose the hand to mutation!",
            orignialHandSize,
            model.getHand().size()
    );
    Assert.assertEquals(
            "getPalette should not expose the hand to mutation!",
            orignialPaletteSize,
            model.getPalette(0).size()
    );
  }

  // Operation Exception tests

  @Test
  public void testPlayToPaletteExceptions() {
    Assert.assertThrows(
            "Should not be able to play before the game starts.",
            IllegalStateException.class,
        () -> model.playToPalette(1, 0)
    );

    model.startGame(fullDeck, false, 2, 2);
    Assert.assertThrows(
            "Should not be able to play to the wining palette.",
            IllegalStateException.class,
        () -> model.playToPalette(1, 0)
    );

    Assert.assertThrows(
            "Should not be able to play a card outside the hand.",
            IllegalArgumentException.class,
        () -> model.playToPalette(0, -1)
    );

    Assert.assertThrows(
            "Should not be able to play a card outside the hand.",
            IllegalArgumentException.class,
        () -> model.playToPalette(0, 2)
    );

    // R3 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    //R4 to P2
    model.playToPalette(1, 0);
    model.drawForHand();
    // R5 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    //R6 to P2
    model.playToPalette(1, 0);
    model.drawForHand();
    // R7 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    //O1 to P2
    model.playToPalette(1, 0);
    // Game should be lost

    Assert.assertThrows(
            "Should not be able to play after the game ends.",
            IllegalStateException.class,
        () -> model.playToPalette(1, 0)
    );
  }

  @Test
  public void testGameIsntOverAfterPlayingToCanvas() {
    model.startGame(model.getAllCards().subList(10,20), false, 3, 7);

    model.playToCanvas(3);

    Assert.assertEquals(
            "The game should not be over after only playing to the canvas!!!",
            false,
            model.isGameOver()
    );
  }

  @Test
  public void testPlayToCanvasExceptions() {
    Assert.assertThrows(
            "Should not be able to play before the game starts.",
            IllegalStateException.class,
        () -> model.playToCanvas(0)
    );

    model.startGame(fullDeck.subList(0,10), false, 2, 2);

    Assert.assertThrows(
            "Should not be able to play a card outside the hand.",
            IllegalArgumentException.class,
        () -> model.playToCanvas(-1)
    );
    Assert.assertThrows(
            "Should not be able to play a card outside the hand.",
            IllegalArgumentException.class,
        () -> model.playToCanvas(2)
    );

    // R3 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    // R4 to P2
    model.playToPalette(1, 0);
    model.drawForHand();
    // R5 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    // R6 to P2
    model.playToPalette(1, 0);
    model.drawForHand();
    // R7 to P1
    model.playToPalette(0, 0);
    model.drawForHand();
    // O1 to Canvas
    model.playToCanvas(0);

    Assert.assertThrows(
            "Should not be able to play to the canvas twice in a row.",
            IllegalStateException.class,
        () -> model.playToCanvas(0)
    );

    // O2 to P2
    model.playToPalette(1, 0);
    model.drawForHand();

    Assert.assertThrows(
            "Should not be able to play to canvas when there is only one card left!",
            IllegalStateException.class,
        () -> model.playToCanvas(0)
    );

    // O3 to P1
    model.playToPalette(0, 0);
    // Game won

    Assert.assertThrows(
            "Should not be able to play after the game ends.",
            IllegalStateException.class,
        () -> model.playToCanvas(0)
    );
  }

  @Test
  public void testDrawForHandExceptions() {
    Assert.assertThrows(
            "Should not be able to draw to hand before the game starts.",
            IllegalStateException.class,
        () -> model.drawForHand()
    );

    model.startGame(fullDeck.subList(0,4), false, 2, 1);

    model.playToPalette(0,0);
    model.drawForHand();
    model.playToPalette(1,0);

    Assert.assertThrows(
            "Should not be able to draw to hand after the game ends.",
            IllegalStateException.class,
        () -> model.drawForHand()
    );
  }

  @Test
  public abstract void testDrawForHand();

  @Test
  public void testStartGameExceptions() {
    Assert.assertThrows(
            "Should not start with too little palettes!",
            IllegalArgumentException.class,
        () -> model.startGame(fullDeck, false, 1, 1)
    );
    Assert.assertThrows(
            "Should not start with too small a hand!",
            IllegalArgumentException.class,
        () -> model.startGame(fullDeck, false, 2, 0)
    );
    Assert.assertThrows(
            "Should not start with too small a deck!",
            IllegalArgumentException.class,
        () -> model.startGame(fullDeck, false, 100, 320)
    );

    List<Card> doubleDeck = model.getAllCards();
    doubleDeck.addAll(model.getAllCards());
    Assert.assertThrows(
            "Should not start with non-unique cards!",
            IllegalArgumentException.class,
        () -> model.startGame(doubleDeck, false, 2, 0)
    );
    List<Card> nullDeck = model.getAllCards();
    nullDeck.add(null);
    Assert.assertThrows(
            "Should not start with any null cards!",
            IllegalArgumentException.class,
        () -> model.startGame(nullDeck, false, 2, 0)
    );

    model.startGame(fullDeck.subList(0,3), false, 2, 1);

    Assert.assertThrows(
            "Should not be able to start twice!",
            IllegalStateException.class,
        () -> model.startGame(fullDeck, false, 2, 1)
    );

    // R3 to P1
    model.playToPalette(0,0);
    // Game over

    Assert.assertThrows(
            "Should not be able to start after the game is over!",
            IllegalStateException.class,
        () -> model.startGame(fullDeck, false, 2, 1)
    );
  }

  // Observation Tests

  @Test
  public void testNumCardsInDeck() {
    Assert.assertThrows(
            "Should not be able to get the numCardsInDeck if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.numOfCardsInDeck()
    );

    model.startGame(fullDeck, false, 2, 1);

    Assert.assertEquals(
            "The number of cards in the deck should be predictable!",
            32,
            model.numOfCardsInDeck()
    );

    model.playToPalette(0,0);
    model.drawForHand();

    Assert.assertEquals(
            "The number of cards in the deck should be predictable!",
            31,
            model.numOfCardsInDeck()
    );

    initCommonFields();
    model.startGame(fullDeck.subList(0,3), false, 2, 1);
    Assert.assertEquals(
            "The number of cards in the deck should be predictable!",
            0,
            model.numOfCardsInDeck()
    );
  }

  @Test
  public void testNumPalettes() {
    Assert.assertThrows(
            "Should not be able to get the numPalettes if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.numPalettes()
    );

    model.startGame(fullDeck, false, 2, 2);

    Assert.assertEquals(
            "The number of palettes should be what was inputted!",
            2,
            model.numPalettes()
    );

    model.playToPalette(0,0);
    model.drawForHand();
    model.playToCanvas(0);

    Assert.assertEquals(
            "The number of palettes should not change!",
            2,
            model.numPalettes()
    );

    initCommonFields();
    model.startGame(fullDeck, false, 34, 1);

    Assert.assertEquals(
            "The number of palettes should be what was inputted!",
            34,
            model.numPalettes()
    );
  }

  @Test
  public void testWinningPaletteIndex() {
    Assert.assertThrows(
            "Should not be able to get the winningPaletteIndex if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.winningPaletteIndex()
    );
    List<Card> customDeck = new ArrayList<>();
    customDeck.add(fullDeck.get(1)); // R2
    customDeck.add(fullDeck.get(5)); // R6
    customDeck.add(fullDeck.get(9)); // O2
    customDeck.add(fullDeck.get(34)); // V7
    customDeck.add(fullDeck.get(33)); // V6
    model.startGame(customDeck, false, 2, 2);

    Assert.assertEquals(
            "The winning palette should be predictable",
            1,
            model.winningPaletteIndex()
    );

    // V7 -> Canvas
    model.playToCanvas(1);

    Assert.assertEquals(
            "The winning palette should be predictable",
            0,
            model.winningPaletteIndex()
    );

    // O2 -> P2
    model.playToPalette(1, 0);
    model.drawForHand();

    Assert.assertEquals(
            "The winning palette should be predictable",
            1,
            model.winningPaletteIndex()
    );

    // V6 -> P1
    model.playToPalette(0, 0);

    Assert.assertEquals(
            "The winning palette should be predictable",
            1,
            model.winningPaletteIndex()
    );
  }

  @Test
  public void testIsGameOverAndIsGameWonLoss() {
    Assert.assertThrows(
            "Should not be able to get isGameOver if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.isGameOver()
    );
    Assert.assertThrows(
            "Should not be able to get isGameWon if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.isGameWon()
    );

    model.startGame(fullDeck, false, 3, 3);

    Assert.assertEquals(
            "Game should not be over right after starting!",
            false,
            model.isGameOver()
    );
    Assert.assertThrows(
            "Should not be able to get isGameWon if the game hasn't ended!",
            IllegalStateException.class,
        () -> model.isGameWon()
    );

    model.playToPalette(0,2);
    model.drawForHand();

    Assert.assertEquals(
            "Game should not be over yet!",
            false,
            model.isGameOver()
    );
    Assert.assertThrows(
            "Should not be able to get isGameWon if the game hasn't ended!",
            IllegalStateException.class,
        () -> model.isGameWon()
    );

    model.playToPalette(1,0);

    Assert.assertEquals(
            "Game should be over!",
            true,
            model.isGameOver()
    );
    Assert.assertEquals(
            "Game should be lost!",
            false,
            model.isGameWon()
    );
  }

  @Test
  public void testIsGameOverAndIsGameWonWin() {
    Assert.assertThrows(
            "Should not be able to get isGameOver if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.isGameOver()
    );
    Assert.assertThrows(
            "Should not be able to get isGameWon if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.isGameWon()
    );

    model.startGame(fullDeck.subList(0,4), false, 3, 1);

    Assert.assertEquals(
            "Game should not be over right after starting!",
            false,
            model.isGameOver()
    );
    Assert.assertThrows(
            "Should not be able to get isGameWon if the game hasn't ended!",
            IllegalStateException.class,
        () -> model.isGameWon()
    );

    model.playToPalette(0,0);

    Assert.assertEquals(
            "Game should be over!",
            true,
            model.isGameOver()
    );
    Assert.assertEquals(
            "Game should be won!",
            true,
            model.isGameWon()
    );
  }

  @Test
  public void testGetHand() {
    Assert.assertThrows(
            "Should not be able to get the hand if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.getHand()
    );
    List<Card> expectedHand = new ArrayList<>();
    expectedHand.add(fullDeck.get(2)); // R3
    expectedHand.add(fullDeck.get(3)); // R4
    expectedHand.add(fullDeck.get(4)); // R5
    expectedHand.add(fullDeck.get(5)); // R6
    expectedHand.add(fullDeck.get(6)); // R7

    model.startGame(fullDeck, false, 2, 5);

    Assert.assertEquals(
            "The contents of the hand should be predictable!",
            expectedHand,
            model.getHand()
    );

    model.playToCanvas(3); // R6 -> Canvas
    expectedHand.remove(3);

    Assert.assertEquals(
            "The contents of the hand should be predictable!",
            expectedHand,
            model.getHand()
    );

    model.playToPalette(0, 1); // R4 -> P1
    model.drawForHand();
    expectedHand.remove(1);
    expectedHand.add(fullDeck.get(7));
    expectedHand.add(fullDeck.get(8));

    Assert.assertEquals(
            "The contents of the hand should be predictable!",
            expectedHand,
            model.getHand()
    );
  }

  @Test
  public void testGetPalette() {
    Assert.assertThrows(
            "Should not be able to get a palette if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.getPalette(0)
    );

    model.startGame(fullDeck.subList(14,21), false, 4, 3);

    Assert.assertThrows(
            "Should not be able to get a palette with an illegal index!",
            IllegalArgumentException.class,
        () -> model.getPalette(-1)
    );
    Assert.assertThrows(
            "Should not be able to get a palette with an illegal index!",
            IllegalArgumentException.class,
        () -> model.getPalette(4)
    );

    List<Card> expectedPalette = new ArrayList<>();
    expectedPalette.add(fullDeck.get(14));
    Assert.assertEquals(
            "Should be able to accurately get the palette!",
            expectedPalette,
            model.getPalette(0)
    );
    expectedPalette = new ArrayList<>();
    expectedPalette.add(fullDeck.get(15));
    Assert.assertEquals(
            "Should be able to accurately get the palette!",
            expectedPalette,
            model.getPalette(1)
    );
    expectedPalette = new ArrayList<>();
    expectedPalette.add(fullDeck.get(17));
    Assert.assertEquals(
            "Should be able to accurately get the palette!",
            expectedPalette,
            model.getPalette(3)
    );

    model.playToPalette(0,1);
    model.drawForHand();
    expectedPalette = new ArrayList<>();
    expectedPalette.add(fullDeck.get(14));
    expectedPalette.add(fullDeck.get(19));
    Assert.assertEquals(
            "Should be able to accurately get the palette!",
            expectedPalette,
            model.getPalette(0)
    );
  }

  @Test
  public void testGetCanvas() {
    Assert.assertThrows(
            "Should not be able to get the canvas if the game hasn't begun!",
            IllegalStateException.class,
        () -> model.getCanvas()
    );

    model.startGame(fullDeck.subList(20,24), false, 2, 2);

    Assert.assertEquals(
            "The top canvas card should be predictable!",
            "R1",
            model.getCanvas().toString()
    );

    model.playToCanvas(0);

    Assert.assertEquals(
            "The top canvas card should be predictable!",
            "I2",
            model.getCanvas().toString()
    );
  }

  @Test
  public void testGetAllCards() {
    Assert.assertEquals(
            "There should be 35 possible cards!",
            35,
            fullDeck.size()
    );
  }

  @Test
  public void testVioletBehavior() {
    // Deck is I5 I6 I7 V1 V2 V3 V4 V5 V6 V7
    model.startGame(fullDeck.subList(25,35), false, 3, 4);

    model.playToCanvas(3);
    Assert.assertEquals(
            "The winning palette should not change!",
            2,
            model.winningPaletteIndex()
    );
  }
}
