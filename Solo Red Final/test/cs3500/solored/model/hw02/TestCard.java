package cs3500.solored.model.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Card implementation CardImpl.
 */
public class TestCard {
  ObservableCard red1;
  ObservableCard red2;
  ObservableCard red3;
  ObservableCard orange4;
  ObservableCard blue5;
  ObservableCard indigo5;
  ObservableCard violet6;
  ObservableCard violet7;

  @Before
  public void initCards() {
    red1 = new CardImpl(CardColor.RED, CardNumber.ONE);
    red2 = new CardImpl(CardColor.RED, CardNumber.TWO);
    red3 = new CardImpl(CardColor.RED, CardNumber.THREE);
    orange4 = new CardImpl(CardColor.ORANGE, CardNumber.FOUR);
    blue5 = new CardImpl(CardColor.BLUE, CardNumber.FIVE);
    indigo5 = new CardImpl(CardColor.INDIGO, CardNumber.FIVE);
    violet6 = new CardImpl(CardColor.VIOLET, CardNumber.SIX);
    violet7 = new CardImpl(CardColor.VIOLET, CardNumber.SEVEN);
  }

  @Test
  public void testCardToString() {
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "R1",
            red1.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "R2",
            red2.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "R3",
            red3.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "O4",
            orange4.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "B5",
            blue5.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "I5",
            indigo5.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "V6",
            violet6.toString()
    );
    Assert.assertEquals(
            "Cards should have the expected string representation",
            "V7",
            violet7.toString()
    );
  }

  @Test
  public void testCardObserveColor() {
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.RED,
            red1.getColor()
    );
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.RED,
            red2.getColor()
    );
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.ORANGE,
            orange4.getColor()
    );
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.BLUE,
            blue5.getColor()
    );
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.INDIGO,
            indigo5.getColor()
    );
    Assert.assertEquals(
            "Cards should be able to return their color",
            CardColor.VIOLET,
            violet6.getColor()
    );
  }

  @Test
  public void testCardObserveNumber() {
    Assert.assertEquals(
            "Cards should be able to return their number",
            1,
            red1.getNumber()
    );
    Assert.assertEquals(
            "Cards should be able to return their number",
            2,
            red2.getNumber()
    );
    Assert.assertEquals(
            "Cards should be able to return their number",
            4,
            orange4.getNumber()
    );
    Assert.assertEquals(
            "Cards should be able to return their number",
            5,
            blue5.getNumber()
    );
    Assert.assertEquals(
            "Cards should be able to return their number",
            5,
            indigo5.getNumber()
    );
    Assert.assertEquals(
            "Cards should be able to return their number",
            6,
            violet6.getNumber()
    );
  }


}
