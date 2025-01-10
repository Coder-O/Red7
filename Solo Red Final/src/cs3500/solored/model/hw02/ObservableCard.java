package cs3500.solored.model.hw02;

/**
 * Expanded behaviors for a Card in the Game of RedSeven.
 * These cards can have their values observed directly, as opposed to just the toString() method.
 */
public interface ObservableCard extends Card {
  /**
   * Returns the color of this card.
   * @return The color of this card.
   */
  CardColor getColor();

  /**
   * Returns the number of this card.
   * @return The number of this card.
   */
  int getNumber();


}
