package cs3500.solored.model.hw02;

/**
 * A color of a card in the game RedSeven.
 */
public enum CardColor {
  RED("R", 4), ORANGE("O", 3),
  BLUE("B", 2), INDIGO("I", 1),
  VIOLET("V", 0);

  // The letter that represents this color.
  // While a character would be a more precise choice of datatype for what this represents,
  // a String is more convenient for programmers to use.
  private final String colorLetter;

  // The relative rank of the color, given by its distance from red in a rainbow.
  // A higher number is closer to red.
  private final int colorRank;

  CardColor(String colorLetter, int colorRank) {
    this.colorLetter = colorLetter;
    this.colorRank = colorRank;
  }

  /**
   * Returns the relative rank of this color,
   * based on its closeness to the color red in a rainbow.
   * @return The rank of this color as an integer.
   *         The greater the number, the closer it is to red.
   */
  public int getRank() {
    return colorRank;
  }

  /**
   * Returns the letter that represents this color, as a String.
   * The color choices and their representative letters are:
   * Red: R, Orange: O, Blue: B, Indigo: I, Violet: V.
   * This is functionally identical to toString().
   * @return the letter that represents this color, as a String.
   */
  public String getColorLetter() {
    return colorLetter;
  }

  /**
   * Returns the letter that represents this color, as a String.
   * This is functionally identical to getColorLetter().
   * @return the letter that represents this color, as a String.
   */
  @Override
  public String toString() {
    return colorLetter;
  }
}
