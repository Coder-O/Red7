package cs3500.solored;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * Tests of the model from the client's perspective, specifically for the basic implementation.
 */
public class ModelTests extends GeneralModelTests {

  @Before
  public void initCommonFields() {
    model = new SoloRedGameModel(new Random(0));
    view = new SoloRedGameTextView(model);
    fullDeck = model.getAllCards();
  }

  @Test
  public void testDrawForHand() {
    model.startGame(fullDeck.subList(1,11), false, 4, 4);

    model.playToCanvas(3);
    model.playToPalette(2, 0);
    model.drawForHand();

    Assert.assertEquals(
            "The number of cards in hand should be 4!",
            4,
            model.getHand().size()
    );
  }

}
