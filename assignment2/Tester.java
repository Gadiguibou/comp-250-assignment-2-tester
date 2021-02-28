package assignment2;

import java.util.ArrayList;
import java.util.Arrays;

// For most of these of to work you will need to implement a toString() method on the Deck class
// that prints all the cards in the deck separated by spaces.

class Deck_Deck_one_card implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(1, 1);
    String result = Tester.deckToString(deck);
    String expected = "AC RJ BJ";

    if (!result.equals(expected)) {
      throw new AssertionError("new Deck(1, 1) returned " + result + " but expected " + expected);
    }
    System.out.println("One card deck test passed.");
  }
}


class Deck_Deck_all_cards implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 4);
    String result = Tester.deckToString(deck);
    String expected = "AC 2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AD 2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AH 2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AS 2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS RJ BJ";

    if (!result.equals(expected)) {
      throw new AssertionError("new Deck(13, 4) returned " + result + " but expected " + expected);
    }
    System.out.println("All cards deck test passed.");
  }
}

class Deck_Deck_references implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 4);
    Tester.checkReferences(deck);
    System.out.println("Deck() references test passed");
  }
}


class Deck_Deck_too_many_cards implements Runnable {
  @Override
  public void run() {
    boolean thrown = false;
    try {
      new Deck(14, 4);
    } catch (IllegalArgumentException expected) {
      thrown = true;
    }

    if (thrown == false) {
      throw new AssertionError("new Deck(14, 4) did not throw an IllegalArgumentException");
    } else {
      System.out.println("Too many cards deck test passed.");
    }
  }
}


class Deck_Deck_too_few_cards implements Runnable {
  @Override
  public void run() {
    boolean thrown = false;
    try {
      new Deck(0, 4);
    } catch (IllegalArgumentException expected) {
      thrown = true;
    }

    if (!thrown) {
      throw new AssertionError("new Deck(0, 4) did not throw an IllegalArgumentException");
    } else {
      System.out.println("Too few cards deck test passed.");
    }
  }
}


class Deck_Deck_too_many_suits implements Runnable {
  @Override
  public void run() {
    boolean thrown = false;
    try {
      new Deck(13, 5);
    } catch (IllegalArgumentException expected) {
      thrown = true;
    }

    if (thrown == false) {
      throw new AssertionError("new Deck(13, 5) did not throw an IllegalArgumentException");
    } else {
      System.out.println("Too many suits deck test passed.");
    }
  }
}


class Deck_Deck_too_few_suits implements Runnable {
  @Override
  public void run() {
    boolean thrown = false;
    try {
      new Deck(13, 0);
    } catch (IllegalArgumentException expected) {
      thrown = true;
    }

    if (!thrown) {
      throw new AssertionError("new Deck(13, 0) did not throw an IllegalArgumentException");
    } else {
      System.out.println("Too few suits deck test passed.");
    }
  }
}


class Deck_Deck_copy implements Runnable {
  @Override
  public void run() {
    Deck originalDeck = new Deck(13, 4);
    Deck deckCopy = new Deck(originalDeck);
    String result = Tester.deckToString(deckCopy);
    String expected = "AC 2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AD 2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AH 2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AS 2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS RJ BJ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "new Deck(new Deck(13, 4)) returned " + result + " but expected " + expected);
    }
    System.out.println("Deck copy test passed.");
  }
}


/*
 * Checks that Deck(Deck d) produces a deep copy of d (i.e. changing d does not
 * affect the copy)
 */
class Deck_Deck_deep_copy implements Runnable {
  @Override
  public void run() {
    Deck d = new Deck(13, 4);
    Deck deckCopy = new Deck(d);
    String expected = "AC 2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AD 2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AH 2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AS 2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS RJ BJ";
    String received;

    // Change order of d
    d.moveCard(d.head, 2);
    received = Tester.deckToString(deckCopy);
    if (!expected.equals(received))
      throw new AssertionError("The copied deck was changed when the original deck was changed");

    // Modify a card within d
    ((Deck.PlayingCard) d.head).rank++;
    received = Tester.deckToString(deckCopy);
    if (!expected.equals(received))
      throw new AssertionError("The copied deck was changed when the original deck was changed");

    System.out.println("Deck deep copy test passed.");
  }
}


class Deck_addCard implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(1, 1);
    String expected = "AC RJ BJ 5C";
    deck.addCard(deck.new PlayingCard("clubs", 5));

    // Check that list structure is ok
    Tester.checkReferences(deck);

    // Check that cards are in the right order
    String received = Tester.deckToString(deck);
    if (!received.equals(expected))
      throw new AssertionError("Expected deck " + expected + " but received " + received);

    System.out.println("Deck addCard() test passed.");
  }
}


class Deck_numOfCards implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 4);
    int result = deck.numOfCards;
    int expected = 54;
    if (result != expected) {
      throw new AssertionError(
          "(new Deck(13, 4)).numOfCards is " + result + " but should have been " + expected);
    }
    System.out.println("Deck numOfCards test passed.");
  }
}


class Deck_shuffle implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    String result = Tester.deckToString(deck);
    String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The shuffled deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck shuffle test passed.");
  }
}


class Deck_locate_joker implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();

    // Expected deck status: 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D

    Deck.Joker redJoker = deck.locateJoker("red");
    Deck.Joker blackJoker = deck.locateJoker("black");

    String expectedRedJokerString = "RJ";
    String expectedBlackJokerString = "BJ";

    String resultRedJokerNext = redJoker.next.toString();
    String resultRedJokerPrev = redJoker.prev.toString();
    String resultBlackJokerNext = blackJoker.next.toString();
    String resultBlackJokerPrev = blackJoker.prev.toString();
    String expectedRedJokerNext = "4C";
    String expectedRedJokerPrev = "AC";
    String expectedBlackJokerNext = "2C";
    String expectedBlackJokerPrev = "5C";


    if (!redJoker.toString().equals(expectedRedJokerString)) {
      throw new AssertionError("deck.locateJoker(\"red\") returned " + redJoker + " but expected "
          + expectedRedJokerString);
    }

    if (!blackJoker.toString().equals(expectedBlackJokerString)) {
      throw new AssertionError("deck.locateJoker(\"black\") returned " + blackJoker
          + " but expected " + expectedBlackJokerString);
    }

    if (!resultRedJokerNext.equals(expectedRedJokerNext)
        || !resultRedJokerPrev.equals(expectedRedJokerPrev)) {
      throw new AssertionError("The next card after the red joker is " + resultRedJokerNext
          + ". The card before the red joker is " + resultRedJokerPrev + ". They should have been: "
          + expectedRedJokerNext + " & " + expectedRedJokerPrev);
    }

    if (!resultBlackJokerNext.equals(expectedBlackJokerNext)
        || !resultBlackJokerPrev.equals(expectedBlackJokerPrev)) {
      throw new AssertionError("The next card after the black joker is " + resultBlackJokerNext
          + ". The card before the black joker is " + resultBlackJokerPrev
          + ". They should have been: " + expectedBlackJokerNext + " & " + expectedBlackJokerPrev);
    }

    System.out.println("Deck locate joker test passed.");
  }
}

class Deck_locate_joker_top_or_bottom_cards implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.moveCard(deck.locateJoker("red"), 6);
    deck.head = deck.locateJoker("black");

    // Expected deck status: 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D

    Deck.Joker redJoker = deck.locateJoker("red");
    Deck.Joker blackJoker = deck.locateJoker("black");

    String expectedRedJokerString = "RJ";
    String expectedBlackJokerString = "BJ";

    String resultRedJokerNext = redJoker.next.toString();
    String resultRedJokerPrev = redJoker.prev.toString();
    String resultBlackJokerNext = blackJoker.next.toString();
    String resultBlackJokerPrev = blackJoker.prev.toString();
    String expectedRedJokerNext = "BJ";
    String expectedRedJokerPrev = "5C";
    String expectedBlackJokerNext = "2C";
    String expectedBlackJokerPrev = "RJ";


    if (!redJoker.toString().equals(expectedRedJokerString)) {
      throw new AssertionError("deck.locateJoker(\"red\") returned " + redJoker + " but expected "
          + expectedRedJokerString);
    }

    if (!blackJoker.toString().equals(expectedBlackJokerString)) {
      throw new AssertionError("deck.locateJoker(\"black\") returned " + blackJoker
          + " but expected " + expectedBlackJokerString);
    }

    if (!resultRedJokerNext.equals(expectedRedJokerNext)
        || !resultRedJokerPrev.equals(expectedRedJokerPrev)) {
      throw new AssertionError("The next card after the red joker is " + resultRedJokerNext
          + ". The card before the red joker is " + resultRedJokerPrev + ". They should have been: "
          + expectedRedJokerNext + " & " + expectedRedJokerPrev);
    }

    if (!resultBlackJokerNext.equals(expectedBlackJokerNext)
        || !resultBlackJokerPrev.equals(expectedBlackJokerPrev)) {
      throw new AssertionError("The next card after the black joker is " + resultBlackJokerNext
          + ". The card before the black joker is " + resultBlackJokerPrev
          + ". They should have been: " + expectedBlackJokerNext + " & " + expectedBlackJokerPrev);
    }

    System.out.println("Deck locate joker test passed.");
  }
}

class Deck_locate_joker_no_jokers implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    deck.addCard(deck.new PlayingCard("Spades", 1));

    Deck.Joker resultingRedJoker = deck.locateJoker("red");
    Deck.Joker resultingBlackJoker = deck.locateJoker("black");
    Deck.Joker expectedRedJoker = null;
    Deck.Joker expectedBlackJoker = null;

    if (resultingRedJoker != expectedRedJoker) {
      throw new AssertionError("deck.locateJoker(\"red\") returned " + resultingRedJoker + " but expected "
          + expectedRedJoker);
    }

    if (resultingBlackJoker != expectedBlackJoker) {
      throw new AssertionError("deck.locateJoker(\"black\") returned " + resultingBlackJoker + " but expected "
          + expectedBlackJoker);
    }

    System.out.println("Deck locate joker no jokers test passed.");
  }
}

class Deck_move_card_no_change implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.moveCard(deck.locateJoker("red"), 0);
    deck.moveCard(deck.locateJoker("black"), 0);
    String result = Tester.deckToString(deck);
    String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck card move no change test passed.");
  }
}

class Deck_move_card_with_change implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.moveCard(deck.locateJoker("red"), 1);
    deck.moveCard(deck.locateJoker("black"), 2);
    String result = Tester.deckToString(deck);
    String expected = "3C 3D AD 5C 2C 2D BJ 4D AC 4C RJ 5D";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck card move with change test passed.");
  }
}


class Deck_triple_cut_regular implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.tripleCut(deck.locateJoker("black"), deck.locateJoker("red"));
    String result = Tester.deckToString(deck);
    String expected = "4C 5D BJ 2C 2D 4D AC RJ 3C 3D AD 5C";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck regular triple cut test passed.");
  }
}


class Deck_triple_cut_empty_end implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.moveCard(deck.locateJoker("red"), 2);
    deck.tripleCut(deck.locateJoker("black"), deck.locateJoker("red"));
    String result = Tester.deckToString(deck);
    String expected = "BJ 2C 2D 4D AC 4C 5D RJ 3C 3D AD 5C";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck empty end triple cut test passed.");
  }
}


class Deck_triple_cut_empty_start implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.head = deck.locateJoker("black");
    deck.tripleCut(deck.locateJoker("black"), deck.locateJoker("red"));
    String result = Tester.deckToString(deck);
    String expected = "4C 5D 3C 3D AD 5C BJ 2C 2D 4D AC RJ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck empty start triple cut test passed.");
  }
}


class Deck_triple_cut_both_ends_empty implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.moveCard(deck.locateJoker("red"), 6);
    deck.head = deck.locateJoker("black");
    deck.tripleCut(deck.locateJoker("black"), deck.locateJoker("red"));
    String result = Tester.deckToString(deck);
    String expected = "BJ 2C 2D 4D AC 4C 5D 3C 3D AD 5C RJ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck both ends empty triple cut test passed.");
  }
}


class Deck_count_cut_no_change_1 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.addCard(deck.new PlayingCard("clubs", 13));
    deck.countCut();
    String result = Tester.deckToString(deck);
    String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D KC";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck count cut no change test 1 passed.");
  }
}

class Deck_count_cut_no_change_2 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.addCard(deck.new PlayingCard("clubs", 12));
    deck.countCut();
    String result = Tester.deckToString(deck);
    String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D QC";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck count cut no change test 2 passed.");
  }
}


class Deck_count_cut_with_change implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.countCut();
    String result = Tester.deckToString(deck);
    String expected = "2D 4D AC RJ 4C 3C 3D AD 5C BJ 2C 5D";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting deck is " + result + " but should have been " + expected);
    }
    System.out.println("Deck count cut with change test passed.");
  }
}


class Deck_look_up_card_joker implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    deck.addCard(deck.new PlayingCard("clubs", 5));
    deck.head = deck.head.prev;
    Deck.Card result = deck.lookUpCard();
    Deck.Card expected = null;

    if (result != expected) {
      throw new AssertionError("lookUpCard() returned " + result + " but expected " + expected);
    }
    System.out.println("Deck look up card joker test passed.");
  }
}


class Deck_look_up_card_regular implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    String result = deck.lookUpCard().toString();
    String expected = "5C";

    if (!result.equals(expected)) {
      throw new AssertionError("lookUpCard() returned " + result + " but expected " + expected);
    }
    System.out.println("Deck look up card joker test passed.");
  }
}


class Deck_generate_next_keystream_value implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    int[] results = new int[12];
    int[] expected = { 4, 4, 15, 3, 3, 2, 1, 14, 16, 17, 17, 14 };

    for (int i = 0; i < 12; i++) {
      results[i] = deck.generateNextKeystreamValue();
    }

    if (!Arrays.equals(results, expected)) {
      throw new AssertionError("The resulting keystream values are " + Arrays.toString(results)
          + " but should have been " + Arrays.toString(expected));
    }
    System.out.println("Deck keystream generation test passed.");
  }
}


class SolitaireCipher_get_keystream implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    int[] results = solitaireCipher.getKeystream(12);
    int[] expected = { 4, 4, 15, 3, 3, 2, 1, 14, 16, 17, 17, 14 };

    if (!Arrays.equals(results, expected)) {
      throw new AssertionError("The resulting keystream values are " + Arrays.toString(results)
          + " but should have been " + Arrays.toString(expected));
    }
    System.out.println("SolitaireCipher keystream generation test passed.");
  }
}


class SolitaireCipher_encode implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.encode("Is that you, Bob?");
    String expected = "MWIKDVZCKSFP";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting encoded message is " + result + " but should have been " + expected);
    }
    System.out.println("SolitaireCipher message encoding test passed.");
  }
}


class SolitaireCipher_decode implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(5, 2);
    Deck.gen.setSeed(10);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.decode("MWIKDVZCKSFP");
    String expected = "ISTHATYOUBOB";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting decoded message is " + result + " but should have been " + expected);
    }
    System.out.println("SolitaireCipher message decoding test passed.");
  }
}

class SolitaireCipher_decode_secret_message implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 2);
    Deck.gen.setSeed(22022021);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.decode("HFCFGIYJOJLYL");
    String expected = "HAVEFUNWITHIT";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting decoded message is " + result + " but should have been " + expected);
    }
    System.out.println("SolitaireCipher message decoding yielded: " + result + " on announcement secret message.");
  }
}

public class Tester {
  static String[] tests = {
      "assignment2.Deck_Deck_one_card",
      "assignment2.Deck_Deck_all_cards",
      "assignment2.Deck_Deck_references",
      "assignment2.Deck_Deck_too_many_cards",
      "assignment2.Deck_Deck_too_few_cards",
      "assignment2.Deck_Deck_too_many_suits",
      "assignment2.Deck_Deck_too_few_suits",
      "assignment2.Deck_Deck_copy",
      "assignment2.Deck_Deck_deep_copy",
      "assignment2.Deck_Deck_addCard",
      "assignment2.Deck_numOfCards",
      "assignment2.Deck_shuffle",
      "assignment2.Deck_locate_joker",
      "assignment2.Deck_locate_joker_top_or_bottom_cards",
      "assignment2.Deck_locate_joker_no_jokers",
      "assignment2.Deck_move_card_no_change",
      "assignment2.Deck_move_card_with_change",
      "assignment2.Deck_triple_cut_regular",
      "assignment2.Deck_triple_cut_empty_end",
      "assignment2.Deck_triple_cut_empty_start",
      "assignment2.Deck_triple_cut_both_ends_empty",
      "assignment2.Deck_count_cut_no_change_1",
      "assignment2.Deck_count_cut_no_change_2",
      "assignment2.Deck_count_cut_with_change",
      "assignment2.Deck_look_up_card_joker",
      "assignment2.Deck_look_up_card_regular",
      "assignment2.Deck_generate_next_keystream_value",
      "assignment2.SolitaireCipher_get_keystream",
      "assignment2.SolitaireCipher_encode",
      "assignment2.SolitaireCipher_decode",
      "assignment2.SolitaireCipher_decode_secret_message"
  };

  public static void main(String[] args) {
    int numPassed = 0;
    ArrayList<String> failedTests = new ArrayList<String>(tests.length);
    for (String className : tests) {
      System.out.printf("%n======= %s =======%n", className);
      System.out.flush();
      try {
        Runnable testCase = (Runnable) Class.forName(className).getDeclaredConstructor().newInstance();
        testCase.run();
        numPassed++;
      } catch (AssertionError e) {
        System.out.println(e);
        failedTests.add(className);
      } catch (Exception e) {
        e.printStackTrace(System.out);
        failedTests.add(className);
      }
    }
    System.out.printf("%n%n%d of %d tests passed.%n", numPassed, tests.length);
    if (failedTests.size() > 0) {
      System.out.println("Failed test(s):");
      for (String className : failedTests) {
        int dotIndex = className.indexOf('.');
        System.out.println("  " + className.substring(dotIndex + 1));
      }
    }
    if (numPassed == tests.length) {
      System.out.println("All clear! Now get some rest.");
    }
  }

  // Utility methods

  /**
   * Checks that the given deck has consistent references (i.e. card ==
   * card.next.prev for all cards) and loops around to the head only after
   * traversing all cards. If not, throws an AssertionError detailing the issue.
   * 
   * @param deck The deck to be checked
   */
  public static void checkReferences(Deck deck) {
    Deck.Card currentCard = deck.head;

    for (int i = 0; i < deck.numOfCards; i++) {
      // Check that references with the next node are consistent
      if (currentCard != currentCard.next.prev)
        throw new AssertionError("The links between card " + i + " and card " + (i + 1) + " are inconsistent.");

      // Check that the list hasn't looped back to the head prematurely
      if (currentCard.next == deck.head && i != deck.numOfCards - 1)
        throw new AssertionError("The list looped back to the head prematurely.");

      currentCard = currentCard.next;
    }

    // Check that the list looped back to the head
    if (currentCard != deck.head)
      throw new AssertionError("The list did not loop back to the head after traversing all cards.");
  }

  /**
   * Converts the given deck to a String, with one space between each card.
   * 
   * @param deck The deck to be converted to String
   * @return A string listing all cards, separated by spaces
   */
  public static String deckToString(Deck deck) {
    String out = "";
    Deck.Card currentCard = deck.head;

    for (int i = 0; i < deck.numOfCards; i++) {
      out += currentCard.toString() + " ";
      currentCard = currentCard.next;
    }

    return out.substring(0, out.length() - 1);
  }
}
