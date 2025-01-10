package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singular Palette that can be played to, printed,
 * and figure out it's score by the current rules.
 */
class Palette {
  private final ArrayList<CardImpl> cards;

  /**
   * Creates a new Palette, which holds a list of cards and can append more cards to that list.
   */
  public Palette() {
    this.cards = new ArrayList<CardImpl>();
  }

  /**
   * Play a card onto this palette.
   * @param card The card to play onto this palette
   */
  public void place(CardImpl card) {
    cards.add(card);
  }

  /**
   * Returns a copy of this palette's list of cards.
   * @return A copy of this palette's list of cards.
   */
  public List<CardImpl> getCards() {
    return List.copyOf(cards);
  }

  /**
   * Finds the best subset of cards from this deck for the given color rule.
   * @param colorRule The color rule to use.
   * @return The subset of cards in this Palette that score the best under this rule.
   */
  public List<CardImpl> getBestCards(CardColor colorRule) {
    switch (colorRule) {
      case RED:
        return getBestCardsByRed();
      case ORANGE:
        return getBestCardsByOrange();
      case BLUE:
        return getBestCardsByBlue();
      case INDIGO:
        return getBestCardsByIndigo();
      case VIOLET:
        return getBestCardsByViolet();
      default:
        throw new IllegalArgumentException();
    }
  }

  // These methods are static to allow for ties to be decided easily when using subsets of cards
  // from palettes that aren't contained in a pallet themselves.
  /**
   * For breaking a tie between palettes.
   * This function compares each winning set of cards
   * and finds the best winner based upon the red rule.
   * @param winningPaletteIndexes The palette indexes of the wining selections.
   * @param winningSelections The relevant selection of cards from each winning palette.
   * @return The corresponding palette index from winningPaletteIndexes of the best selection
   *         from winningSelections.
   */
  public static int breakTie(
          List<Integer> winningPaletteIndexes,
          List<List<CardImpl>> winningSelections
  ) {
    // Will hold the best card from each selection, as chosen by the red rule.
    List<CardImpl> bestCards = new ArrayList<CardImpl>();

    // Add the best card from each selection to bestCards
    for (List<CardImpl> selection : winningSelections) {

      bestCards.add(Palette.findHighestCardByRed(selection));
    }

    // Finds the best card from the winning cards from each palette.
    CardImpl absoluteBestCard = Palette.findHighestCardByRed(bestCards);

    // Find the corresponding palette index of the winning card,
    // and returns that index as the absolute winner.
    return winningPaletteIndexes.get(bestCards.indexOf(absoluteBestCard));
  }

  /**
   * Finds the best card in a given list of cards as determined by the red rule.
   * That is, the palette with the highest value card wins.
   * In the event of a tie, the card with the highest ranked color wins.
   * Colors are ranked higher the closer they are to red in the rainbow.
   * @param cards The set of cards to find the best card from.
   * @return The best card from cards.
   * @throws IllegalArgumentException If the provided list of cards is empty or null.
   */
  private static CardImpl findHighestCardByRed(List<CardImpl> cards) {
    if (cards == null || cards.isEmpty()) {
      throw new IllegalArgumentException("Must provide a list of cards!!!");
    }

    CardImpl highestCard = cards.get(0);

    for (CardImpl card : cards) {
      if (
              // The current card has a higher number
              (
                      card.getNumber() > highestCard.getNumber()
              )
                      // Or the card has the same number and a better color
                      || (
                              card.getNumber() == highestCard.getNumber()
                                      && card.getColor().getRank()
                                      > highestCard.getColor().getRank()
              )
      ) {
        highestCard = card;
      }
    }

    return highestCard;
  }

  /**
   * Finds the best card in this palette as determined by the red rule.
   * That is, the palette with the highest value card wins.
   * In the event of a tie, the card with the highest ranked color wins.
   * Colors are ranked higher the closer they are to red in the rainbow.
   * @return A list containing only the best card from this palette.
   */
  private List<CardImpl> getBestCardsByRed() {
    // Returning the singular card as a list so that this rule is consistent with the others
    List<CardImpl> bestCard = new ArrayList<>();
    bestCard.add(findHighestCardByRed(this.getCards()));
    return bestCard;
  }


  /**
   * Returns the best set of cards in this palette, as determined by the orange rule.
   * That is, the largest set where all the cards have the same number.
   * @return The largest list containing only cards of the same number
   *         that could be formed from this palette.
   */
  private List<CardImpl> getBestCardsByOrange() {
    // Counts how many cards of each number this palette has
    int[] numOfEachValue = new int[7];
    for (CardImpl card : getCards()) {
      numOfEachValue[card.getNumber() - 1] += 1;
    }

    // Find the most common number
    int mostCommonNumber = 0;
    int maxNumber = 0;

    for (int idx = 0; idx < numOfEachValue.length; idx++) {
      // >= so that later numbers supersede earlier ones.
      // Later numbers are prioritized because they score better via the red rule
      if (numOfEachValue[idx] >= maxNumber) {
        maxNumber = numOfEachValue[idx];
        // The most common number is the number on a card, not its 0-based index.
        mostCommonNumber = idx + 1;
      }
    }

    // Gather all the cards with the mostCommonNumber and return them as a list.
    List<CardImpl> allCardsOfCommonNumber = new ArrayList<>();
    for (CardImpl card : cards) {
      if (card.getNumber() == mostCommonNumber) {
        allCardsOfCommonNumber.add(card);
      }
    }

    return allCardsOfCommonNumber;
  }

  /**
   * Returns the best set of cards in this palette, as determined by the blue rule.
   * That is, the set with the most different colors in it,
   * prioritizing higher value cards if there are multiple cards of the same color.
   * @return A list of different colored cards,
   *         with as many different colors and as high values as possible.
   */
  private List<CardImpl> getBestCardsByBlue() {
    // An array with a slot for one card of each color
    CardImpl[] oneOfEachColor = new CardImpl[5];

    // For every card in the palette...
    for (CardImpl card : getCards()) {
      int colorIndex = card.getColor().getRank();

      // If there is no card of that color or the current card is a higher card
      if (
              oneOfEachColor[colorIndex] == null
                      || oneOfEachColor[colorIndex].getNumber() < card.getNumber()
      ) {
        // Place the current card into the array at its correct color index.
        oneOfEachColor[colorIndex] = card;
      }
    }

    // Convert the array to a list for returning and get rid of any leftover nulls.
    List<CardImpl> outputList = new ArrayList<CardImpl>();
    for (CardImpl card : oneOfEachColor) {
      if (card != null) {
        outputList.add(card);
      }
    }

    return outputList;
  }

  /**
   * Returns the best set of cards in this palette, as determined by the indigo rule.
   * That is, the longest run in this palette, with the highest-ranking colors possible.
   * @return The larges run of cards with the highest-ranked colors possible.
   */
  private List<CardImpl> getBestCardsByIndigo() {
    // An array with one slot for each possible car number
    CardImpl[] oneOfEachNumber = new CardImpl[7];
    // Fills as many slots in oneOfEachNumber as possible, with the best card possible.
    for (CardImpl card : getCards()) {
      int numberIdx = card.getNumber() - 1;
      // If there is no card of that number or the current card is a better card
      if (
              oneOfEachNumber[numberIdx] == null
                      || oneOfEachNumber[numberIdx].getColor().getRank() < card.getColor().getRank()
      ) {
        // Place the current card into the array at its correct numberIndex.
        oneOfEachNumber[numberIdx] = card;
      }
    }

    // Find the longest run
    List<CardImpl> bestRun = new ArrayList<>();
    List<CardImpl> currentRun = new ArrayList<>();

    for (CardImpl card : oneOfEachNumber) {
      if (card == null) {
        // Reset the run
        currentRun = new ArrayList<>();
      } else {
        currentRun.add(card);
        if (currentRun.size() > bestRun.size()) {
          // The current run is the best run.
          bestRun = currentRun;
        } else if (currentRun.size() == bestRun.size()) {
          // If they are tied, break the tie.
          // Get the list of indexes needed for the breakTie() method
          List<Integer> indexes = new ArrayList<>();
          indexes.add(0);
          indexes.add(1);
          // Combine the runs into a list for the breakTie() method
          List<List<CardImpl>> comparingRuns = new ArrayList<>();
          comparingRuns.add(bestRun);
          comparingRuns.add(currentRun);
          // Break the tie
          bestRun = comparingRuns.get(breakTie(indexes, List.of(bestRun, currentRun)));
        }
      }
    }
    return bestRun;
  }

  /**
   * Returns the best set of cards in this palette, as determined by the violet rule.
   * That is, all the cards with a value under 4.
   * @return A list containing all cards from this palette with a value under four.
   */
  private List<CardImpl> getBestCardsByViolet() {
    List<CardImpl> cardsUnder4 = new ArrayList<>();

    for (CardImpl card : getCards()) {
      if (card.getNumber() < 4) {
        cardsUnder4.add(card);
      }
    }

    return cardsUnder4;
  }
}