package cs3500.solored.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * Tests for SoloRedTextController, specifically those that rely on implementation details.
 */
public class TestSoloRedTextController {
  private Readable readable;
  private Appendable appendable;

  @Before
  public void initControllerTests() {
    readable = new StringReader("");
    appendable = new StringBuilder();
  }

  @Test
  public void testConstructorExceptionsReadableNull() {
    Assert.assertThrows(
            "The constructor should throw an exception when passed null arguments!!!",
            IllegalArgumentException.class,
        () -> new SoloRedTextController(null, appendable)
    );
  }

  @Test
  public void testConstructorExceptionsAppendableNull() {
    Assert.assertThrows(
            "The constructor should throw an exception when passed null arguments!!!",
            IllegalArgumentException.class,
        () -> new SoloRedTextController(readable, null)
    );
  }

  @Test
  public void testConstructorNoExceptions() {
    RedGameController controller = new SoloRedTextController(readable, appendable);
    Assert.assertTrue(
            "The constructor did not throw an exception, as desired.",
            true
    );
  }
}
