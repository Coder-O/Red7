package cs3500.solored.view.hw02;

import cs3500.solored.model.hw02.Card;

/**
 * A mock of a Card implementation.
 */
public class MockCard implements Card {
  private final String string;

  public MockCard(String string) {
    this.string = string;
  }

  @Override
  public String toString() {
    return string;
  }
}
