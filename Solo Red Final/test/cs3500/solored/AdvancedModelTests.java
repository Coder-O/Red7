package cs3500.solored;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * Tests of the model from the client's perspective, specifically for the advanced implementation.
 */
public class AdvancedModelTests extends GeneralModelTests {

  @Override
  public void initCommonFields() {
    model = new AdvancedSoloRedGameModel(new Random(0));
    view = new SoloRedGameTextView(model);
    fullDeck = model.getAllCards();
  }

  @Override
  @Test
  public void testDrawForHand() {
    model.startGame(fullDeck.subList(1,11), false, 4, 5);
    model.playToPalette(2, 0);
    model.drawForHand();

    Assert.assertEquals(
            "The model should only draw 1 card!",
            5,
            model.getHand().size()
    );
  }

  @Test
  public void testDraw1WhenCanvasLessThanWinningLength() {
    model.startGame(fullDeck.subList(1,11), false, 4, 5);

    model.playToCanvas(2);
    model.playToPalette(2, 0);
    model.drawForHand();

    Assert.assertEquals(
            "Should only draw 1 card when the canvas card's value < the winning length!",
            4,
            model.getHand().size()
    );
  }

  @Test
  public void testDraw1WhenCanvasEqualToWinningLength() {
    model.startGame(fullDeck.subList(1,11), false, 4, 5);

    model.playToCanvas(3);
    model.playToPalette(2, 0);
    model.drawForHand();

    Assert.assertEquals(
            "Should only draw 1 card when the canvas card's value = the winning length!",
            4,
            model.getHand().size()
    );
  }

  @Test
  public void testDraw2WhenCanvasGreaterThanWinningLength() {
    model.startGame(fullDeck.subList(1,15), false, 4, 5);

    model.playToCanvas(4);
    model.playToPalette(2, 0);
    model.drawForHand();

    Assert.assertEquals(
            "Should draw 2 cards when the canvas card's value > the winning length!",
            5,
            model.getHand().size()
    );
  }

  @Test
  public void testDelayedDrawDraws2() {
    model.startGame(fullDeck.subList(1,15), false, 4, 5);

    model.playToCanvas(4);
    model.playToPalette(2, 0);
    model.playToPalette(1, 0);
    model.playToPalette(0, 1);
    model.drawForHand();

    Assert.assertEquals(
            "Should draw 2 cards, even when the canvas wasn't played to within the last turn!",
            3,
            model.getHand().size()
    );
  }

}
