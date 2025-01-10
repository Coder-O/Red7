package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.solored.model.hw02.CardColor;
import cs3500.solored.model.hw02.CardImpl;
import cs3500.solored.model.hw02.Palette;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * General implementation for a solo game of RedSeven.
 * The game consists of four structures:
 * <ul>
 *   <li>A deck of cards to draw from</li>
 *   <li>A hand to play from</li>
 *   <li>Four palettes to play to</li>
 *   <li>A canvas that dictates the winning rule</li>
 * </ul>
 * The goal of the game is to use all the cards in the deck
 * while ensuring exactly one palette is winning each round.
 * <p>Notes on this implementation:</p>
 * <ul>
 *   <li>The last card in a list is considered the top card of the virtual stack.</li>
 * </ul>
 *
 */
public abstract class AbstractSoloRedGameModel implements RedGameModel<CardImpl> {
  private final Random rand;
  private boolean gameHasStarted;
  private boolean gameHasEnded;
  protected List<CardImpl> deck;
  private final List<CardImpl> canvas;
  private Palette[] palettes;
  protected final List<CardImpl> hand;
  private int maxHandSize;
  private int winningPaletteIndex;
  private int lastIndexPlayed;
  protected boolean playedToCanvas;
  // Whether the user's last move was to the canvas.

  // Constructors

  /**
   * Creates a new AbstractSoloRedGameModel and sets up the state so that startGame() is ready to be
   * called.
   */
  protected AbstractSoloRedGameModel() {
    this(new Random());
  }

  /**
   * Creates a new SoloRedGameModel and sets up the state so that startGame() is ready to be called.
   * @param rand A Random object to provide for shuffling.
   */
  protected AbstractSoloRedGameModel(Random rand) {
    if (rand == null) {
      throw new IllegalArgumentException("The given Random object must not be null!");
    }
    gameHasStarted = false;
    gameHasEnded = false;
    playedToCanvas = false;
    winningPaletteIndex = 0;
    lastIndexPlayed = -1;
    hand = new ArrayList<CardImpl>();
    canvas = new ArrayList<CardImpl>();
    this.rand = rand;
  }

  // Operations

  /**
   * Play the given card from the hand to the losing palette chosen.
   * The card is removed from the hand and placed at the far right
   * end of the palette.
   *
   * @param paletteIdx    a 0-index number representing which palette to play to
   * @param cardIdxInHand a 0-index number representing the card to play from the hand
   * @throws IllegalStateException    if the game has not started or the game is over
   * @throws IllegalArgumentException if paletteIdx < 0 or more than the number of palettes
   * @throws IllegalArgumentException if cardIdxInHand < 0
   *                                  or greater/equal to the number of cards in hand
   * @throws IllegalStateException    if the palette referred to by paletteIdx is winning
   */
  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    if (!gameHasStarted || gameHasEnded) {
      throw new IllegalStateException("The game is not running! Cannot play to a palette.");
    }
    if (paletteIdx < 0 || paletteIdx > numPalettes() - 1) {
      throw new IllegalArgumentException(
              "The given paletteIdx ("
                      + paletteIdx
                      + ") is out of the valid range 0-"
                      + (numPalettes() - 1)
                      + ", inclusive!"
      );
    }
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException(
              "The given cardIdxInHand ("
                      + cardIdxInHand
                      + ") is out of the valid range 0-"
                      + (hand.size() - 1)
      );
    }
    if (paletteIdx == winningPaletteIndex()) {
      throw new IllegalStateException("The palette at " + paletteIdx + " is winning already!");
    }

    palettes[paletteIdx].place(hand.remove(cardIdxInHand));
    lastIndexPlayed = paletteIdx;
    findWinningPalette();
    isGameOver();
    playedToCanvas = false;
  }

  /**
   * Play the given card from the hand to the canvas.
   * This changes the rules of the game for all palettes.
   * The method can only be called once per turn.
   *
   * @param cardIdxInHand a 0-index number representing the card to play from the hand
   * @throws IllegalStateException    if the game has not started or the game is over
   * @throws IllegalArgumentException if cardIdxInHand < 0
   *                                  or greater/equal to the number of cards in hand
   * @throws IllegalStateException    if this method was already called once in a given turn
   * @throws IllegalStateException    if there is exactly one card in hand
   */
  @Override
  public void playToCanvas(int cardIdxInHand) {
    if (!gameHasStarted || gameHasEnded) {
      throw new IllegalStateException("The game is not running, you may not play to the canvas!");
    }
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException(
              "The cardIdxInHand value "
                      + cardIdxInHand
                      + " is outside the valid range (0,"
                      + hand.size()
                      + ")!"
      );
    }
    if (playedToCanvas) {
      throw new IllegalStateException(
              "You have already played to the canvas this turn! You cannot play again!"
      );
    }
    if (hand.size() == 1) {
      throw new IllegalStateException(
              "There is only 1 card in hand, it must be played to a pallet!"
      );
    }

    canvas.add(hand.remove(cardIdxInHand));
    findWinningPalette();
    playedToCanvas = true;
  }

  /**
   * Updates winningPaletteIndex so that it references the palette that is currently wining.
   * This follows the rules given by the color of the top canvas card:
   * <ul>
   *   <li>Red: The palette with the highest card wins.</li>
   *   <li>Orange: The palette with the most of a single number of card wins.</li>
   *   <li>Blue: The palette with the most different colors wins.</li>
   *   <li>Indigo: The palette with the longest run wins.</li>
   *   <li>Violet: The palette with the most cards below the value of 4 wins.</li>
   * </ul>
   * In the event of a tie, the palette with the significant card that scores highest by the red
   * rule wins.
   * (A significant card is a card that is helping that palette tie in the original rules.)
   * If there are no significant cards, then the palette that wins under the red rule is found.
   */
  private void findWinningPalette() {
    findWinningPalette(getCanvas().getColor());
  }

  /**
   * Updates winningPaletteIndex so that it references the palette that is currently wining under
   * the given color rule:
   * <ul>
   *   <li>Red: The palette with the highest card wins.</li>
   *   <li>Orange: The palette with the most of a single number of card wins.</li>
   *   <li>Blue: The palette with the most different colors wins.</li>
   *   <li>Indigo: The palette with the longest run wins.</li>
   *   <li>Violet: The palette with the most cards below the value of 4 wins.</li>
   * </ul>
   * In the event of a tie, the palette with the significant card that scores highest by the red
   * rule wins.
   * (A significant card is a card that is helping that palette tie in the original rules.)
   * If there are no significant cards, then the palette that wins under the red rule is found.
   * @param color The color rule to use.
   */
  private void findWinningPalette(CardColor color) {
    int maxValue = 0;

    // Stores the current wining indexes and those palette's winning selection of cards.
    // Each palette index in winningIndexes has a corresponding list of cards at the same
    // position in winningSelections.
    // These exist to track ties, so they can be broken later.
    List<Integer> winningIndexes = new ArrayList<>();
    List<List<CardImpl>> winningSelections = new ArrayList<List<CardImpl>>();

    for (int i = 0; i < numPalettes(); i++) {
      List<CardImpl> currentBestSelection = palettes[i].getBestCards(color);
      if (currentBestSelection.size() > maxValue) {
        // Reset all ties and the current value - this palette has set a new standard.
        maxValue = currentBestSelection.size();
        winningIndexes.clear();
        winningSelections.clear();

        // Add this palette and its selection to the list of current winners
        winningIndexes.add(i);
        winningSelections.add(currentBestSelection);
      } else if (currentBestSelection.size() == maxValue) {
        // Add this palette and its selection to the list of current winners
        winningIndexes.add(i);
        winningSelections.add(currentBestSelection);
      }
    }

    if (maxValue == 0) {
      // If no palettes had a valid section for the current rule, use red rule.
      findWinningPalette(CardColor.RED);
    } else if (winningIndexes.size() > 1) {
      // If there is a tie, and as such there is more than one element in the indexes...
      // Break the tie
      winningPaletteIndex = Palette.breakTie(winningIndexes, winningSelections);
    } else {
      // If there is one wining index, that is the winner.
      winningPaletteIndex = winningIndexes.get(0);
    }
  }

  /**
   * Draws cards from the deck until the hand is full
   * OR until the deck is empty, whichever occurs first. Newly drawn cards
   * are added to the end of the hand (far-right conventionally).
   * SIDE-EFFECT: Allows the player to play to the canvas again.
   *
   * @throws IllegalStateException if the game has not started or the game is over, or the
   *         palette hasn't been played to since the last draw.
   */
  protected void drawFullHand() throws IllegalStateException {
    // Only care about the exceptions thrown by this method, the return value is checked
    // intrinsically by the while loop.
    canDrawToHand();
    while (hand.size() < maxHandSize && !deck.isEmpty()) {
      hand.add(deck.remove(0));
    }
    playedToCanvas = false;
  }

  // Hand draw helpers
  /**
   * A method to help ensure that drawing to the hand is currently allowed.
   * @return Whether cards can be drawn to the hand (if the hand is not full and the deck is not
   *          empty.)
   * @throws IllegalStateException if the game has not started or the game is over.
   */
  protected boolean canDrawToHand() throws IllegalStateException {
    if (!gameHasStarted || gameHasEnded) {
      throw new IllegalStateException(
              "The game is not running! It either started or it has ended!"
      );
    }
    return hand.size() < maxHandSize && !deck.isEmpty();
  }


  /**
   * Starts the game with the given options. The deck given is used
   * to set up the palettes and hand. Modifying the deck given to this method
   * will not modify the game state in any way.
   *
   * @param deck        the cards used to set up and play the game
   * @param shuffle     whether the deck should be shuffled prior to setting up the game
   * @param numPalettes number of palettes in the game
   * @param handSize    the maximum number of cards allowed in the hand
   * @throws IllegalStateException    if the game has started or the game is over
   * @throws IllegalArgumentException if numPalettes < 2 or maxHandSize <= 0
   * @throws IllegalArgumentException if deck's size is not large enough to set up the game
   * @throws IllegalArgumentException if deck has non-unique cards or null cards
   * @throws IllegalArgumentException if deck is null
   */
  @Override
  public void startGame(List<CardImpl> deck, boolean shuffle, int numPalettes, int handSize) {
    checkExceptions(deck, numPalettes, handSize);

    palettes = new Palette[numPalettes];
    this.maxHandSize = handSize;

    this.deck = new ArrayList<CardImpl>(deck);

    if (shuffle) {
      for (int i = 0; i < this.deck.size(); i++) {
        int randIndex = rand.nextInt(this.deck.size());
        CardImpl temp = this.deck.get(i);
        this.deck.set(i, this.deck.get(randIndex));
        this.deck.set(randIndex, temp);
      }
    }

    for (int i = 0; i < palettes.length; i++) {
      palettes[i] = new Palette();
      palettes[i].place(this.deck.remove(0));
    }

    // The only requirement for this card is that it is red.
    // The number is meaningless and chosen arbitrarily.
    CardImpl canvasCard = new CardImpl(CardColor.RED, CardNumber.ONE);
    canvas.add(canvasCard);

    gameHasStarted = true;
    findWinningPalette();
    // To keep a consistent game state for isGameOver to check.
    lastIndexPlayed = winningPaletteIndex;
    drawFullHand();
  }

  /**
   * For use in the startGame() method, this method checks if that method should throw an exception.
   * If so, it does.
   * This helper method exists solely for readability and organization.
   * @param deck The deck parameter of startGame()
   * @param numPalettes the numPalettes parameter of startGame()
   * @param handSize the handSize parameter of startGame
   */
  private void checkExceptions(List<CardImpl> deck, int numPalettes, int handSize) {
    if (gameHasEnded || gameHasStarted) {
      throw new IllegalStateException("The game cannot be started again!!!");
    }
    if (numPalettes < 2) {
      throw new IllegalArgumentException("Must have at least 2 pallets! Not " + numPalettes);
    }
    if (handSize <= 0) {
      throw new IllegalArgumentException("Hand size must be greater than 0.");
    }
    if (deck == null) {
      throw new IllegalArgumentException("The deck may not be null!");
    }
    if (deck.size() < handSize + numPalettes) {
      throw new IllegalArgumentException(
              "The deck provided is only "
                      + deck.size()
                      + ", Which is not large enough to make "
                      + numPalettes
                      + "palettes and a hand of "
                      + handSize
                      + " cards!"
      );
    }
    // Keeps track of all possible values that may be in a deck.
    // Every row represents one possible color, every column a possible number.
    boolean[][] hasPossibleCards = new boolean[5][7];

    for (CardImpl card : deck) {
      if (card == null) {
        throw new IllegalArgumentException("No cards in the deck may be null!!!");
      }

      // If the deck has been found to have this specific card before
      if (hasPossibleCards[card.getColor().getRank()][card.getNumber() - 1]) {
        throw new IllegalArgumentException("The deck may not have repeat cards!!!");
      }

      hasPossibleCards[card.getColor().getRank()][card.getNumber() - 1] = true;
    }
  }

  // Observations

  /**
   * Returns the number of cards remaining in the deck used in the game.
   *
   * @return the number of cards in the deck
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int numOfCardsInDeck() {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started yet! No cards are in the deck!");
    }
    return deck.size();
  }

  /**
   * Returns the number of palettes in the running game.
   *
   * @return the number of palettes in the game
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int numPalettes() {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started yet! No palettes exist!");
    }
    return palettes.length;
  }

  /**
   * Returns the index of the winning palette in the game.
   *
   * @return the 0-based index of the winning palette
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public int winningPaletteIndex() {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started yet, no palette is winning!");
    }
    return winningPaletteIndex;
  }

  /**
   * Returns if the game is over.
   * The game is over if either the last move the player made was to play to a pallet in such a way
   * that it did not change the winning palette, or both the deck and the player's hand are empty.
   * @return true if the game has ended and false otherwise
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public boolean isGameOver() {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started yet, it can't be over!");
    }

    // The game should not be over if the user just played to the canvas
    gameHasEnded = !playedToCanvas
            && (winningPaletteIndex != lastIndexPlayed)
            || (hand.isEmpty() && deck.isEmpty());
    return gameHasEnded;
  }

  /**
   * Returns if the game is won by the player as specified by the implementation.
   *
   * @return true if the game has been won or false if the game has not
   * @throws IllegalStateException if the game has not started or the game is not over
   */
  @Override
  public boolean isGameWon() {
    if (!(gameHasEnded && gameHasStarted)) {
      throw new IllegalStateException(
              "The game cannot have been won unless it has both started and ended!"
      );
    }
    return hand.isEmpty() && deck.isEmpty() && lastIndexPlayed == winningPaletteIndex;
  }

  /**
   * Returns a copy of the hand in the game. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   *
   * @return a new list containing the cards in the player's hand in the same order
   *         as in the current state of the game.
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public List<CardImpl> getHand() {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started, there is no hand to get!");
    }
    return List.copyOf(hand);
  }

  /**
   * Returns a copy of the specified palette. This means modifying the returned list
   * or the cards in the list has no effect on the game.
   *
   * @param paletteNum 0-based index of a particular palette
   * @return a new list containing the cards in specified palette in the same order
   *         as in the current state of the game.
   * @throws IllegalStateException    if the game has not started
   * @throws IllegalArgumentException if paletteNum < 0 or more than the number of palettes
   */
  @Override
  public List<CardImpl> getPalette(int paletteNum) {
    if (!gameHasStarted) {
      throw new IllegalStateException("The game hasn't started, there are no pallets to get!");
    }
    if (paletteNum < 0 || paletteNum > numPalettes() - 1) {
      throw new IllegalArgumentException(
              "The palette index "
                      + paletteNum
                      + " is out of the valid range 0-"
                      + (numPalettes() - 1)
                      + ", inclusive!"
      );
    }

    // This getCards() method already returns a copy of its data that cannot mutate the original
    // list, so there is no reason to copy it again here.
    return palettes[paletteNum].getCards();
  }

  /**
   * Return the top card of the canvas.
   * Modifying this card has no effect on the game.
   *
   * @return the top card of the canvas
   * @throws IllegalStateException if the game has not started
   */
  @Override
  public CardImpl getCanvas() {
    if (!gameHasStarted) {
      throw new IllegalStateException(
              "Cannot get the canvas before the game starts!"
      );
    }
    // Making a copy of the top card on the canvas.
    return new CardImpl(
            canvas.get(canvas.size() - 1).getColor(),
            canvas.get(canvas.size() - 1).getCardNumber()
    );
  }

  /**
   * Get a NEW list of all cards that can be used to play the game.
   * Editing this list should have no effect on the game itself.
   * Repeated calls to this method should produce a list of cards in the same order.
   * Modifying the cards in this list should have no effect on any returned list
   * or the game itself.
   *
   * @return a new list of all possible cards that can be used for the game
   */
  @Override
  public List<CardImpl> getAllCards() {
    ArrayList<CardImpl> allCards = new ArrayList<>();
    for (CardColor color : CardColor.values()) {
      for (CardNumber number : CardNumber.values()) {
        allCards.add(new CardImpl(color, number));
      }
    }
    return allCards;
  }
}

