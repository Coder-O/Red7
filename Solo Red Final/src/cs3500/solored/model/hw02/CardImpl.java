package cs3500.solored.model.hw02;

import java.util.Objects;

/**
 * A representation of a card in the game RedSeven.
 */
public class CardImpl implements ObservableCard {
  private final CardColor color;
  private final CardNumber number;

  /**
   * Creates a new card, which has a color and a number between 1 and 7, inclusive.
   * @param color The color of this card.
   * @param number The number of this card.
   * @throws NullPointerException If color is null.
   */
  public CardImpl(
          CardColor color,
          CardNumber number
  ) throws NullPointerException {
    Objects.requireNonNull(color);
    Objects.requireNonNull(number);

    this.color = color;
    this.number = number;
  }


  /**
   * Returns the color of this card.
   *
   * @return The color of this card.
   */
  @Override
  public CardColor getColor() {
    return this.color;
  }

  /**
   * Returns the number of this card.
   *
   * @return The number of this card.
   */
  @Override
  public int getNumber() {
    return this.number.getNumber();
  }

  /**
   * Returns the CardNumber of this card.
   *
   * @return The CardNumber of this card.
   */
  public CardNumber getCardNumber() {
    return this.number;
  }



  /**
   * Prints the color and number of the card.
   * The colors are printed R, O, B, I, or V.
   * The numbers are printed as 1-7.
   * As an example, a blue 5 is printed as B5.
   * @return a two character representation of the card
   */
  @Override
  public String toString() {
    return this.color.toString() + this.number.getNumber();
  }

  /**
   * Determines if this CardImpl is equivalent to another object.
   * That is to say, they have the same class, color, and number.
   * @param o The other object to compare to.
   * @return If the objects are equivalent.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof CardImpl)) {
      return false;
    }
    // Note that this scenario where 2 cards are identical should never occur by the rules of the
    // game, but having this method is good practice
    return this.toString().equals(o.toString());
  }

  /**
   * Provides the hash code for this card based upon its color and number.
   * @return The hash code for this card.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.getNumber(), this.getColor());
  }
}
