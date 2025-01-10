package cs3500.solored.view.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * Tests for SoloRedGameTextView.
 */
public class TestView {

  private RedGameModel model;
  private Appendable appendable;
  private RedGameView view;

  @Before
  public void initTests() {
    model = new MockModel();

    appendable = new StringBuilder();
    view = new SoloRedGameTextView(model, appendable);
  }

  @Test
  public void testViewWorks() {
    Assert.assertEquals("The view should work!!!",
            "Canvas: O\n"
                    + "> P1: O7 V1\n"
                    + "P2: R1 O6\n"
                    + "P3: I2 V3\n"
                    + "Hand: R3 R4 I7 V6 R2",
            view.toString()
    );
    try {
      view.render();
    } catch (IOException e) {
      Assert.assertTrue("An IOException should not be thrown!!!", false);
    }

    Assert.assertEquals("The view's render method should work!!!",
            "Canvas: O\n"
                    + "> P1: O7 V1\n"
                    + "P2: R1 O6\n"
                    + "P3: I2 V3\n"
                    + "Hand: R3 R4 I7 V6 R2",
            appendable.toString());
  }

  @Test
  public void testViewExceptions() {
    Assert.assertThrows(
            "The view should not accept null arguments!",
            IllegalArgumentException.class,
        () -> new SoloRedGameTextView(null)
    );
    Assert.assertThrows(
            "The view should not accept null arguments!",
            IllegalArgumentException.class,
        () -> new SoloRedGameTextView(null, appendable)
    );
    Assert.assertThrows(
            "The view should not accept null arguments!",
            IllegalArgumentException.class,
        () -> new SoloRedGameTextView(model, null)
    );
  }
}
