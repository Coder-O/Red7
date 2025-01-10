package cs3500.solored.model.hw02;

/**
 * Represents the numerical value of a card.
 */
public enum CardNumber {
  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

  private final int number;

  CardNumber(int number) {
    this.number = number;
  }

  public final int getNumber() {
    return number;
  }
}
