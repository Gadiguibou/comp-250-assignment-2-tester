package assignment2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import assignment2.Deck.Joker;
import assignment2.Deck.PlayingCard;

// Official tests
// ==========================================================================================

class AddCard_AllRef implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
    deck.addCard(c1);
    deck.addCard(c2);
    deck.addCard(c3);
    boolean c1ref = c1.next == c2 && c1.prev == c3;
    boolean c2ref = c2.next == c3 && c2.prev == c1;
    boolean c3ref = c3.next == c1 && c3.prev == c2;
    if (!(c1ref && c2ref && c3ref)) {
      throw new AssertionError("Circular doubly linked list references are not correctly set up.");
    }
    System.out.println("Test passed.");
  }
}


class AddCard_CheckHead implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); // 8C
    deck.addCard(c1);
    deck.addCard(c8);
    Deck.Card head = deck.head;
    if (head != c1) {
      throw new AssertionError("addCard should add the input card to the bottom of the deck.\n" +
          "Expected head to be " + c1 + " but got " + head);
    }
    System.out.println("Test passed.");
  }
}


class AddCard_Circular implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
    Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); // 8C
    deck.addCard(c1);
    deck.addCard(c2);
    deck.addCard(c3);
    deck.addCard(c8);
    if (!(c1.prev == c8 && c8.next == c1)) {
      throw new AssertionError("Circular references are not correctly set up.");
    }
    System.out.println("Test passed.");
  }
}


class AddCard_NumOfCards implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
    Deck.Card d11 = deck.new PlayingCard(Deck.suitsInOrder[1], 11); // JD
    deck.addCard(c1);
    deck.addCard(c2);
    deck.addCard(d11);
    deck.addCard(c3);
    int expected = 4;
    int result = deck.numOfCards;
    if (expected != result) {
      throw new AssertionError("numOfCards is not correctly updated.");
    }
    System.out.println("Test passed.");
  }
}


class AddCard_SingleCard implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    deck.addCard(c1);
    if (!(c1.prev == c1 && c1.next == c1)) {
      throw new AssertionError(
          "Card references are not correctly set up when the deck contains only ONE card.");
    }
    System.out.println("Test passed.");
  }
}


class DeepCopy_CheckRefs implements Runnable {
  @Override
  public void run() {
    HashSet<Deck.Card> cardSet = new HashSet<>();
    Deck deck = new Deck();
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 1));
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 3));
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 5));
    cardSet.add(deck.new Joker("black"));
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[1], 2));
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[2], 4));
    cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[3], 6));

    for (Deck.Card c : cardSet) {
      deck.addCard(c);
    }

    Deck copy = new Deck(deck); // should do a deep copy

    Deck.Card cur = copy.head;
    for (int i = 0; i < cardSet.size(); i++) {
      if (cardSet.contains(cur)) {
        throw new AssertionError("Deep copy must create new object.");
      }
      cur = cur.next;
    }

    System.out.println("Test passed.");
  }
}


class DeepCopy_CircularNext implements Runnable {
  @Override
  public void run() {

    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck copy = new Deck(deck); // should do a deep copy

    Deck.Card cur = copy.head;
    for (int i = 0; i < cards.length; i++) {
      if (cur == null) {
        throw new AssertionError("Either head or one of the next pointers is null.");
      }

      if (cards[i].getClass() != cur.getClass()) { // Either one is Joker and other is PlayingCard or vice versa
        throw new AssertionError("The card at the next position of ."
            + i + " from head, has type: " + cur.getClass().getName()
            + " but expected: " + cards[i].getClass().getName());
      }

      if (cur instanceof Deck.PlayingCard) { // both are PlayingCard
        if (cards[i].getValue() != cur.getValue()) {
          throw new AssertionError("The card at the next position of ."
              + i + " from head must have value: " + cards[i].getValue() + " but got: "
              + cur.getValue());
        }
      } else { // both are Joker
        String cardColor = ((Deck.Joker) cards[i]).getColor();
        String curColor = ((Deck.Joker) cur).getColor();
        if (!cardColor.equals(curColor)) {
          throw new AssertionError("The joker card at the next position of ."
              + i + " from head must have color: " + cardColor + " but got: " + curColor);
        }
      }
      cur = cur.next;
    }

    if (cur != copy.head) {
      throw new AssertionError("The last card's next does not point to the head.");
    }

    System.out.println("Test passed.");
  }
}


class DeepCopy_CircularPrev implements Runnable {
  @Override
  public void run() {

    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck copy = new Deck(deck); // should do a deep copy

    Deck.Card cur = copy.head;
    for (int j = 0; j < cards.length; j++) {
      int i = Math.floorMod(-j, cards.length); // i goes 0, n-1, n-2, ..., 1
      if (cur == null) {
        throw new AssertionError("Either head or one of the prev pointers is null.");
      }

      if (cards[i].getClass() != cur.getClass()) { // Either one is Joker and other is PlayingCard or vice versa
        throw new AssertionError("The card at the prev position of ."
            + j + " from head, has type: " + cur.getClass().getName()
            + " but expected: " + cards[i].getClass().getName());
      }

      if (cur instanceof Deck.PlayingCard) { // both are PlayingCard
        if (cards[i].getValue() != cur.getValue()) {
          throw new AssertionError("The card at the prev position of ."
              + j + " from head must have value: " + cards[i].getValue() + " but got: "
              + cur.getValue());
        }
      } else { // both are Joker
        String cardColor = ((Deck.Joker) cards[i]).getColor();
        String curColor = ((Deck.Joker) cur).getColor();
        if (!cardColor.equals(curColor)) {
          throw new AssertionError("The joker card at the prev position of ."
              + j + " from head must have color: " + cardColor + " but got: " + curColor);
        }
      }
      cur = cur.prev;
    }

    if (cur != copy.head) {
      throw new AssertionError("The last card's prev does not point to the head.");
    }

    System.out.println("Test passed.");
  }
}


class LocateJoker_Test1 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C
    Deck.Card expected = tdeck.new Joker("red");

    tdeck.addCard(c1);
    tdeck.addCard(c2);
    tdeck.addCard(c3);
    tdeck.addCard(expected);

    Deck.Card received = tdeck.locateJoker("red");
    if (expected != received) {
      throw new AssertionError("The reference returned was incorrect." +
          "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
          + " but instead got the card " + received + " with reference " + received.hashCode());
    }
    System.out.println("Test passed.");
  }
}


class LocateJoker_Test2 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card c2 = tdeck.new Joker("red");
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C

    Deck.Card expected = tdeck.new Joker("black");

    tdeck.addCard(c1);
    tdeck.addCard(c2);
    tdeck.addCard(c3);
    tdeck.addCard(c4);
    tdeck.addCard(expected);

    Deck.Card received = tdeck.locateJoker("black");
    if (expected != received) {
      throw new AssertionError("The reference returned was incorrect." +
          "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
          + " but instead got the card " + received + " with reference " + received.hashCode());
    }
    System.out.println("Test passed.");
  }
}


class LocateJoker_Test3 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card expected = tdeck.new Joker("red");
    Deck.Card c2 = tdeck.new Joker("black");
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C

    tdeck.addCard(c1);
    tdeck.addCard(expected);
    tdeck.addCard(c2);
    tdeck.addCard(c3);
    tdeck.addCard(c4);

    Deck.Card received = tdeck.locateJoker("red");
    if (expected != received) {
      throw new AssertionError("The reference returned was incorrect." +
          "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
          + " but instead got the card " + received + " with reference " + received.hashCode());
    }
    System.out.println("Test passed.");
  }
}


class LookUpCard_Test1 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); // AC
    Deck.Card expected = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); // 2C
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); // 3C

    tdeck.addCard(c1);
    tdeck.addCard(expected);
    tdeck.addCard(c3);

    Deck.Card received = tdeck.lookUpCard();
    if (expected != received) {
      throw new AssertionError("The reference returned was incorrect. The second card should have " +
          "been returned. Expected the card " + expected.toString() + " with reference "
          + expected.hashCode()
          + " but instead got the card " + received + " with reference " + received.hashCode());
    }
    System.out.println("Test passed.");
  }
}


class LookUpCard_Test2 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 6); // 6C
    Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[1], 2); // 2D
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 3); // 3H
    Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 4); // 4H
    Deck.Card c5 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 5); // 5H
    Deck.Card c6 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 6); // 6H
    Deck.Card expected = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7); // 7H
    Deck.Card c7 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 8); // 8H
    Deck.Card c8 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 9); // 9H

    tdeck.addCard(c1);
    tdeck.addCard(c2);
    tdeck.addCard(c3);
    tdeck.addCard(c4);
    tdeck.addCard(c5);
    tdeck.addCard(c6);
    tdeck.addCard(expected);
    tdeck.addCard(c7);
    tdeck.addCard(c8);

    Deck.Card received = tdeck.lookUpCard();
    if (expected != received) {
      throw new AssertionError("The reference returned was incorrect. " +
          "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
          + " but instead got the card " + received + " with reference " + received.hashCode());
    }
    System.out.println("Test passed.");
  }
}


class LookUpCard_Test3 implements Runnable {
  @Override
  public void run() {
    Deck tdeck = new Deck();
    Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 8); // 6C
    Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[1], 2); // 2D
    Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 3); // 3H
    Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 4); // 4H
    Deck.Card c5 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 5); // 5H
    Deck.Card c6 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 6); // 6H
    Deck.Card c7 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7); // 7H
    Deck.Card c8 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 8); // 8H
    Deck.Card c9 = tdeck.new Joker("red"); // JR

    tdeck.addCard(c1);
    tdeck.addCard(c2);
    tdeck.addCard(c3);
    tdeck.addCard(c4);
    tdeck.addCard(c5);
    tdeck.addCard(c6);
    tdeck.addCard(c7);
    tdeck.addCard(c8);
    tdeck.addCard(c9);

    Deck.Card received = tdeck.lookUpCard();
    if (received != null) {
      throw new AssertionError("Null should be returned in case a Joker is found.");
    }
    System.out.println("Test passed.");
  }
}


class MoveCard_CheckNext1 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck.Card[] expected = new Deck.Card[] {
        cards[0], cards[1], cards[3], cards[4],
        cards[5], cards[2], cards[6] };

    deck.moveCard(cards[2], 3);

    Deck.Card cur = deck.head;
    for (int i = 0; i < expected.length; i++) {
      // System.out.println(cur);
      if (expected[i] != cur) {
        throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
      }
      cur = cur.next;
    }
    System.out.println("Test passed.");
  }
}


class MoveCard_CheckNext2 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck.Card[] expected = new Deck.Card[] {
        cards[0], cards[3], cards[1], cards[2],
        cards[4], cards[5], cards[6] };

    deck.moveCard(cards[3], 4);

    Deck.Card cur = deck.head;
    for (int i = 0; i < expected.length; i++) {
      // System.out.println(cur);
      if (expected[i] != cur) {
        throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
      }
      cur = cur.next;
    }
    System.out.println("Test passed.");
  }
}


class MoveCard_CheckPrev1 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck.Card[] expected = new Deck.Card[] {
        cards[0], cards[1], cards[3], cards[4],
        cards[5], cards[2], cards[6] };

    deck.moveCard(cards[2], 3);

    Deck.Card cur = deck.head;
    for (int j = 0; j < expected.length; j++) {
      int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
      // System.out.println(cur);
      if (expected[i] != cur) {
        throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
      }
      cur = cur.prev;
    }
    System.out.println("Test passed.");
  }
}


class MoveCard_CheckPrev2 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card[] cards = new Deck.Card[] {
        deck.new PlayingCard(Deck.suitsInOrder[0], 1),
        deck.new PlayingCard(Deck.suitsInOrder[0], 3),
        deck.new PlayingCard(Deck.suitsInOrder[0], 5),
        deck.new Joker("black"),
        deck.new PlayingCard(Deck.suitsInOrder[1], 2),
        deck.new PlayingCard(Deck.suitsInOrder[2], 4),
        deck.new PlayingCard(Deck.suitsInOrder[3], 6)
    };

    for (Deck.Card c : cards) {
      deck.addCard(c);
    }

    Deck.Card[] expected = new Deck.Card[] {
        cards[0], cards[3], cards[1], cards[2],
        cards[4], cards[5], cards[6] };

    deck.moveCard(cards[3], 4);

    Deck.Card cur = deck.head;
    for (int j = 0; j < expected.length; j++) {
      int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
      // System.out.println(cur);
      if (expected[i] != cur) {
        throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
      }
      cur = cur.prev;
    }
    System.out.println("Test passed.");
  }
}


class Shuffle_Empty implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();

    deck.shuffle();

    if (!(deck.head == null)) {
      throw new AssertionError("Deck should be empty.");
    }
    System.out.println("Test passed.");
  }
}


class Shuffle_Example implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    // example in instruction pdf
    // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
    Deck.Card[] arrDeck = new Deck.Card[12];
    for (int i = 0; i < 10; i++) {
      int suit = i / 5;
      int rank = i % 5 + 1;
      Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
      arrDeck[i] = card;
      deck.addCard(card);
    }
    Deck.Card rj = deck.new Joker("red");
    Deck.Card bj = deck.new Joker("black");
    arrDeck[10] = rj;
    arrDeck[11] = bj;
    deck.addCard(rj);
    deck.addCard(bj);

    int seed = 10;
    Deck.gen.setSeed(seed);
    deck.shuffle();

    // expected result
    // 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D

    int[] shuffledIndex = { 2, 7, 5, 4, 11, 1, 6, 8, 0, 10, 3, 9 };

    // .next references
    Deck.Card cur = deck.head;
    for (int i = 0; i < 12; i++) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Forward references are not correctly set up. " +
            "Expected card at index " + i + " iterating using .next is " + expected + " but got "
            + cur);
      }
      cur = cur.next;
    }

    // .prev references
    cur = deck.head.prev;
    for (int i = 11; i >= 0; i--) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Backward references are not correctly set up. " +
            "Expected card at index " + i + " iterating using .prev is " + expected + " but got "
            + cur);
      }
      cur = cur.prev;
    }
    System.out.println("Test passed.");
  }
}


class Shuffle_FullDeck implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    // all 54 cards
    Deck.Card[] arrDeck = new Deck.Card[54];
    for (int i = 0; i < 52; i++) {
      int suit = i / 13;
      int rank = i % 13 + 1;
      Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
      arrDeck[i] = card;
      deck.addCard(card);
    }
    Deck.Card rj = deck.new Joker("red");
    Deck.Card bj = deck.new Joker("black");
    arrDeck[52] = rj;
    arrDeck[53] = bj;
    deck.addCard(rj);
    deck.addCard(bj);

    int seed = 10;
    Deck.gen.setSeed(seed);
    deck.shuffle();

    // expected result
    // 7S QD 7H JH KH KD 8C 4C 9S JD KC 9C 5C QC 2S 5S 10H 10D
    // 4S 5D 6H 4D 9D 8D 3H 6D 4H 7C RJ 9H 3C 2D JC 6C 8H JS 5H
    // AH BJ 3S 6S 3D QS AS 7D 2C AD KS 10S 8S 10C QH AC 2H
    int[] shuffledIndex = {
        45, 24, 32, 36, 38, 25, 7, 3, 47, 23, 12, 8, 4, 11, 40, 43, 35, 22,
        42, 17, 31, 16, 21, 20, 28, 18, 29, 6, 52, 34, 2, 14, 10, 5, 33, 49, 30,
        26, 53, 41, 44, 15, 50, 39, 19, 1, 13, 51, 48, 46, 9, 37, 0, 27 };

    // .next references
    Deck.Card cur = deck.head;
    for (int i = 0; i < 54; i++) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Forward references are not correctly set up. " +
            "Expected card at index " + i + " is " + expected + " but got " + cur);
      }
      cur = cur.next;
    }

    // .prev references
    cur = deck.head.prev;
    for (int i = 53; i >= 0; i--) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Backward references are not correctly set up. " +
            "Expected card at index " + i + " iterating using .prev is " + expected + " but got "
            + cur);
      }
      cur = cur.prev;
    }
    System.out.println("Test passed.");
  }
}


class Shuffle_NewCard implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    // example in instruction pdf
    // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
    Set<Deck.Card> cardSet = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      int suit = i / 5;
      int rank = i % 5 + 1;
      Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
      deck.addCard(card);
      cardSet.add(card);
    }
    Deck.Card rj = deck.new Joker("red");
    Deck.Card bj = deck.new Joker("black");
    deck.addCard(rj);
    deck.addCard(bj);
    cardSet.add(rj);
    cardSet.add(bj);

    int seed = 10;
    Deck.gen.setSeed(seed);
    deck.shuffle();

    Deck.Card cur = deck.head;
    // forward ref
    for (int i = 0; i < 12; i++) {
      if (!cardSet.contains(cur)) {
        throw new AssertionError("Shuffle should not create new cards.");
      }
      cur = cur.next;
    }
    if (cur != deck.head) {
      throw new AssertionError("Deck is not correctly shuffled. " +
          "Tail does not connect to head or new cards were added.");
    }

    // backward ref
    for (int i = 11; i >= 0; i--) {
      cur = cur.prev;
    }
    if (cur != deck.head) {
      throw new AssertionError("Deck is not correctly shuffled. " +
          "Backward references are not correctly set up.");
    }

    System.out.println("Test passed.");
  }
}


class Shuffle_SingleCard implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    Deck.Card c = deck.new Joker("red");
    deck.addCard(c);

    deck.shuffle();

    if (!(deck.head.getValue() == c.getValue() &&
        c.next.getValue() == c.getValue() && c.prev.getValue() == c.getValue())) {
      throw new AssertionError("Deck is not correctly shuffled when it only has one card.");
    }
    System.out.println("Test passed.");
  }
}


class Shuffle_Three implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck();
    // AC 2C 3C 4C 5C
    Deck.Card[] arrDeck = new Deck.Card[5];
    for (int i = 0; i < 5; i++) {
      Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[0], i + 1);
      arrDeck[i] = card;
      deck.addCard(card);
    }

    int seed = 250;
    Deck.gen.setSeed(seed);
    deck.shuffle();
    deck.shuffle();
    deck.shuffle();

    // expected first pass
    // AC, 4C, 5C, 3C, 2C

    // expected second pass
    // 5C, AC, 4C, 2C, 3C

    // expected third pass
    // AC, 5C, 3C, 2C, 4C

    int[] shuffledIndex = { 0, 4, 2, 1, 3 };

    // .next references
    Deck.Card cur = deck.head;
    for (int i = 0; i < 5; i++) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Forward references are not correctly set up. " +
            "Expected card at index " + i + " is " + expected + " but got " + cur);
      }
      cur = cur.next;
    }

    // .prev references
    cur = deck.head.prev;
    for (int i = 4; i >= 0; i--) {
      Deck.Card expected = arrDeck[shuffledIndex[i]];
      if (cur.getValue() != expected.getValue()) {
        throw new AssertionError("Deck is not correctly shuffled.\n" +
            "Backward references are not correctly set up. " +
            "Expected card at index " + i + " iterating using .prev is " + expected + " but got "
            + cur);
      }
      cur = cur.prev;
    }
    System.out.println("Test passed.");
  }
}

// Student tests
//==========================================================================================


class Deck_Deck_one_card implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(1, 1);
    String result = Tester.deckToString(deck);
    String expected = "AC RJ BJ";

    if (!result.equals(expected)) {
      throw new AssertionError("new Deck(1, 1) returned " + result + " but expected " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


/*
 * Checks that the Deck constructor can create the correct order when the order
 * of Deck.suitsInOrder is changed
 */
class Deck_Deck_suitsInOrder_changed implements Runnable {
  @Override
  public void run() {
    Deck.suitsInOrder = new String[] { "spades", "hearts", "diamonds", "clubs" };
    Deck deck = new Deck(4, 3);
    String result = Tester.deckToString(deck);
    String expected = "AS 2S 3S 4S AH 2H 3H 4H AD 2D 3D 4D RJ BJ";
    Deck.suitsInOrder = new String[] { "clubs", "diamonds", "hearts", "spades" };

    if (!result.equals(expected)) {
      throw new AssertionError("Using Deck.suitsInOrder = {S, H, D, C}, new Deck(4, 3) returned "
          + result + " but expected " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


class default_deck_more_than_one_card implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck(4, 3);
    String result = Tester.deckToString(d1);
    String expected = "AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ";
    if (!result.equals(expected) || d1.numOfCards != 14) {
      throw new AssertionError("got returned " + result
          + " but expected " + expected + "No Cards: " + d1.numOfCards);
    }
    System.out.println("Test passed.");
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
      System.out.println("Test passed.");
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
      System.out.println("Test passed.");
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
      System.out.println("Test passed.");
    }
  }
}


/*
 * Checks that the Deck constructor checks numOfSuits against the length
 * suitsInOrder and not just the constant 4
 */
class Deck_Deck_too_many_suits2 implements Runnable {
  @Override
  public void run() {
    Deck.suitsInOrder = new String[] { "clubs" };
    boolean thrown = false;
    try {
      new Deck(13, 2);
    } catch (IllegalArgumentException expected) {
      thrown = true;
    } finally {
      Deck.suitsInOrder = new String[] { "clubs", "diamonds", "hearts", "spades" };
    }

    if (!thrown) {
      throw new AssertionError(
          "new Deck(13, 2) did not throw an IllegalArgumentException when Deck.suitsInOrder was changed");
    } else {
      System.out.println("Test passed.");
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
      System.out.println("Test passed.");
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

    Tester.checkReferences(deckCopy);

    System.out.println("Test passed.");
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

    System.out.println("Test passed.");
  }
}


class deep_copy_deck implements Runnable {
  @Override
  public void run() {
    // create a deck 4*3
    Deck oldDeck = new Deck(4, 3);
    Deck newDeck = new Deck(oldDeck);

    String resultOldDeck = Tester.deckToString(oldDeck);
    String resultNewdDeck = Tester.deckToString(newDeck);

    if (!resultNewdDeck.equals(resultOldDeck) ||
        oldDeck.numOfCards != newDeck.numOfCards) {
      throw new AssertionError("got old deck " + resultOldDeck
          + " new Deck" + resultNewdDeck + "No Cards in old and new deck"
          + oldDeck.numOfCards + " " + newDeck.numOfCards);
    }
    System.out.println("Test passed.");
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

    System.out.println("Test passed.");
  }
}


class addOneCardToBottom implements Runnable {
  @Override
  public void run() {
    // create a deck 4*3
    Deck newDeck = new Deck(4, 3);
    Deck.Card newCard = newDeck.new PlayingCard("c", 5);
    newDeck.addCard(newCard);
    String result = Tester.deckToString(newDeck);
    String expected = "AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ 5C";
    if (!result.equals(expected) || newDeck.numOfCards != 15) {
      throw new AssertionError("got " + result
          + " but expected " + expected + "No Cards: " + newDeck.numOfCards);
    }
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
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
      throw new AssertionError("The shuffled deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


class shuffleDeck implements Runnable {
  @Override
  public void run() {
    // create a deck 4*3
    Deck newDeck = new Deck(4, 3);
    Deck.Card current = newDeck.head;
    int numCards = newDeck.numOfCards;
    String beforeShuffle = "";

    while (current != newDeck.head || numCards != 0) {

      beforeShuffle = beforeShuffle + current.toString() + " ";

      current = current.next;
      numCards--;
    }
    newDeck.shuffle();

    current = newDeck.head;
    numCards = newDeck.numOfCards;
    String afterShuffle = "";

    while (current != newDeck.head || numCards != 0) {

      afterShuffle = afterShuffle + current.toString() + " ";

      current = current.next;
      numCards--;
    }

    if ((beforeShuffle.equals(afterShuffle))) {
      throw new AssertionError("the deck was not shuffled ");
    }
    System.out.println("Test passed.");
    System.gc();
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
      throw new AssertionError("deck.locateJoker(\"black\") returned " + blackJoker + " but expected "
          + expectedBlackJokerString);
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

    System.out.println("Test passed.");
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
      throw new AssertionError("deck.locateJoker(\"black\") returned " + blackJoker + " but expected "
          + expectedBlackJokerString);
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

    System.out.println("Test passed.");
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
      throw new AssertionError("deck.locateJoker(\"red\") returned " + resultingRedJoker
          + " but expected " + expectedRedJoker);
    }

    if (resultingBlackJoker != expectedBlackJoker) {
      throw new AssertionError("deck.locateJoker(\"black\") returned " + resultingBlackJoker
          + " but expected " + expectedBlackJoker);
    }

    System.out.println("Test passed.");
  }
}


class Deck_locate_joker_wrong_color implements Runnable {

  @Override
  public void run() {
    Deck deck = new Deck(10, 2);
    Joker result;
    try {
      result = deck.locateJoker("green");
    } catch (Throwable t) {
      throw new AssertionError(
          "deck.locateJoker(\"green\") threw an exception, but should return null");
    }

    if (result != null) {
      throw new AssertionError(
          "deck.locateJoker(\"green\") returned " + result + ", but expected null");
    }


  }
}


class locatejoker implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck(4, 3);
    Deck.Card getJoker = d1.locateJoker("black");
    String returnedJoker = getJoker.toString();

    if (!returnedJoker.equals("BJ")) {
      throw new AssertionError("Wrong card returned");
    }
    System.out.println("Test passed.");
  }
}


class noJokersInDeck implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    for (int i = 1; i <= 13; i++) {
      Deck.Card newCard = d1.new PlayingCard("c", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("d", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("h", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("s", i);
      d1.addCard(newCard);
    }

    if (d1.locateJoker("red") != null || d1.locateJoker("black") != null) {
      throw new AssertionError("this deck shouldn't have jokers");
    }
    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


/*
 * Implements question @1209 on Piazza. If p is greater than the number of
 * cards, the card itself should not be counted.
 */
class Deck_move_card_loop_around implements Runnable {
  @Override
  public void run() {
    Deck d = new Deck();
    d.addCard(d.new PlayingCard("clubs", 1));
    d.addCard(d.new PlayingCard("clubs", 2));
    d.addCard(d.new PlayingCard("clubs", 3));
    d.addCard(d.new PlayingCard("clubs", 4));
    d.moveCard(d.head.next, 5);
    String result = Tester.deckToString(d);
    String expected = "AC 3C 4C 2C";

    if (!result.equals(expected))
      throw new AssertionError("Received " + result + " but expected " + expected);

    Tester.checkReferences(d);

    System.out.println("Test passed.");
  }
}


class moveCardHead implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    for (int i = 1; i <= 1; i++) {
      Deck.Card newCard = d1.new PlayingCard("c", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("d", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("h", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("s", i);
      d1.addCard(newCard);
    }

    d1.moveCard(d1.head, 2);

    String result = Tester.deckToString(d1);
    String expected = "AC AS AD AH";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
  }
}


class moveCardTailby1 implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    for (int i = 1; i <= 1; i++) {
      Deck.Card newCard = d1.new PlayingCard("c", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("d", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("h", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("s", i);
      d1.addCard(newCard);
    }

    Deck.Card current = d1.head.prev;
    d1.moveCard(current, 1);
    String result = Tester.deckToString(d1);
    String expected = "AC AS AD AH";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
  }
}


class moveCardTailby2 implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    for (int i = 1; i <= 1; i++) {
      Deck.Card newCard = d1.new PlayingCard("c", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("d", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("h", i);
      d1.addCard(newCard);
      newCard = d1.new PlayingCard("s", i);
      d1.addCard(newCard);
    }

    Deck.Card current = d1.head.prev;
    d1.moveCard(current, 2);

    String result = Tester.deckToString(d1);
    String expected = "AC AD AS AH";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


class tripleCut implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();

    String[] suits = { "c", "c", "c", "c", "c", "d", "d", "d", "d", "c", "c",
        "j", "c", "c", "d", "d", "d", "d", "c",
        "j", "c", "c", "c", "d", "d", "d", "d", "d" };
    int[] rank = { 1, 4, 7, 10, 13, 3, 6, 9, 12, 3, 6, 0, 9, 12, 2, 5, 8, 11, 2, 0, 5, 8, 11, 1, 4, 7, 10,
        13 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 11) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 19) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        Deck.Card newCard = d1.new PlayingCard(suits[i], rank[i]);
        d1.addCard(newCard);

      }
    }

    Deck.Card firstJoker = d1.locateJoker("black");
    Deck.Card secondJoker = d1.locateJoker("red");
    d1.tripleCut(firstJoker, secondJoker);

    String result = Tester.deckToString(d1);
    String expected = "5C 8C JC AD 4D 7D 10D KD BJ 9C QC 2D 5D 8D JD 2C RJ AC 4C 7C 10C KC 3D 6D 9D QD 3C 6C";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
  }
}


class tripleCutFirstCardHead implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();

    String[] suits = { "j", "c", "c", "d", "d", "d", "d", "c",
        "j", "c", "c", "c", "d", "d", "d", "d", "d" };
    int[] rank = { 0, 9, 12, 2, 5, 8, 11, 2, 0, 5, 8, 11, 1, 4, 7, 10, 13 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 0) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 8) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        Deck.Card newCard = d1.new PlayingCard(suits[i], rank[i]);
        d1.addCard(newCard);

      }
    }

    Deck.Card firstJoker = d1.locateJoker("black");
    Deck.Card secondJoker = d1.locateJoker("red");
    d1.tripleCut(firstJoker, secondJoker);

    String result = Tester.deckToString(d1);
    String expected = "5C 8C JC AD 4D 7D 10D KD BJ 9C QC 2D 5D 8D JD 2C RJ";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
  }
}


class tripleCutsecondcardbottom implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();

    String[] suits = { "d", "c", "c", "j", "d", "d", "d", "c",
        "c", "c", "c", "c", "d", "d", "d", "d", "j" };
    int[] rank = { 0, 9, 12, 2, 5, 8, 11, 2, 0, 5, 8, 11, 1, 4, 7, 10, 4 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 3) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 16) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        Deck.Card newCard = d1.new PlayingCard(suits[i], rank[i]);
        d1.addCard(newCard);

      }
    }

    Deck.Card firstJoker = d1.locateJoker("black");
    Deck.Card secondJoker = d1.locateJoker("red");
    d1.tripleCut(firstJoker, secondJoker);

    String result = Tester.deckToString(d1);
    String expected = "BJ 5D 8D JD 2C 0C 5C 8C JC AD 4D 7D 10D RJ 0D 9C QC";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
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
      throw new AssertionError("The resulting deck is " + result + " but should have been " + expected);
    }

    Tester.checkReferences(deck);

    System.out.println("Test passed.");
  }
}


class countCut implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    String fullSuitName = "";

    String[] suits = { "C", "C", "C", "D", "D", "D", "D", "D", "J",
        "C", "C", "D", "D", "D", "D", "C", "J",
        "C", "C", "C", "C", "C", "D", "D", "D", "D", "C", "C" };

    int[] rank = { 5, 8, 11, 1, 4, 7, 10, 13, 0,
        9, 12, 2, 5, 8, 11, 2, 0,
        1, 4, 7, 10, 13, 3, 6, 9, 12, 3, 6 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 8) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 16) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        switch (suits[i]) {
        case "C":
          fullSuitName = "clubs";
          break;
        case "D":
          fullSuitName = "diamonds";
          break;
        case "H":
          fullSuitName = "hearts";
          break;
        case "S":
          fullSuitName = "spades";
          break;

        }
        Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
        d1.addCard(newCard);

      }
    }

    d1.countCut();

    String result = Tester.deckToString(d1);
    String expected = "10D KD BJ 9C QC 2D 5D 8D JD 2C RJ AC 4C 7C 10C KC 3D 6D 9D QD 3C 5C 8C JC AD 4D 7D 6C";
    if (!expected.equals(result)) {
      throw new AssertionError("got " + result
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
  }
}


class lookUpCard implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    String fullSuitName = "";

    String[] suits = { "C", "C", "C", "D", "D", "D", "D", "D", "J",
        "C", "C", "D", "D", "D", "D", "C", "J",
        "C", "C", "C", "C", "C", "D", "D", "D", "D", "C", "C" };

    int[] rank = { 5, 8, 11, 1, 4, 7, 10, 13, 0,
        9, 12, 2, 5, 8, 11, 2, 0,
        1, 4, 7, 10, 13, 3, 6, 9, 12, 3, 6 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 8) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 16) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        switch (suits[i]) {
        case "C":
          fullSuitName = "clubs";
          break;
        case "D":
          fullSuitName = "diamonds";
          break;
        case "H":
          fullSuitName = "hearts";
          break;
        case "S":
          fullSuitName = "spades";
          break;

        }
        Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
        d1.addCard(newCard);

      }
    }

    d1.countCut();

    Deck.Card cardReturned = d1.lookUpCard();

    String expected = "JC";

    if (!cardReturned.toString().equals(expected)) {
      throw new AssertionError("got " + cardReturned.toString()
          + " but expected " + expected);
    }
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
  }
}


class generateNextKeystreamValue implements Runnable {
  @Override
  public void run() {
    Deck d1 = new Deck();
    String fullSuitName = "";

    String[] suits = { "C", "C", "C", "C", "C", "D", "D", "D", "D",
        "J", "C", "C", "C", "C", "D", "D", "D", "D",
        "J", "C", "C", "C", "C", "D", "D", "D", "D", "D" };

    int[] rank = { 1, 4, 7, 10, 13, 3, 6, 9, 12,
        0, 3, 6, 9, 12, 2, 5, 8, 11,
        0, 2, 5, 8, 11, 1, 4, 7, 10, 13 };
    for (int i = 0; i < rank.length; i++) {
      if (i == 9) {
        Deck.Card newJoker = d1.new Joker("black");
        d1.addCard(newJoker);
      } else if (i == 18) {
        Deck.Card newJoker = d1.new Joker("red");
        d1.addCard(newJoker);

      } else {
        switch (suits[i]) {
        case "C":
          fullSuitName = "clubs";
          break;
        case "D":
          fullSuitName = "diamonds";
          break;
        case "H":
          fullSuitName = "hearts";
          break;
        case "S":
          fullSuitName = "spades";
          break;

        }
        Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
        d1.addCard(newCard);

      }
    }
    int valueReturned = d1.generateNextKeystreamValue();
    // d1.countCut();
    if (valueReturned != 11) {
      throw new AssertionError("got " + valueReturned
          + " but expected 11 ");
    }
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_encode2 implements Runnable {
  @Override
  public void run() {

    Deck deck = new Deck(13, 3);
    // System.out.println("Original deck");
    // System.out.println(Tester.deckToString(deck));
    Deck.gen.setSeed(20210314);
    deck.shuffle();
    // System.out.println("Shuffled Deck");
    // System.out.println(Tester.deckToString(deck));
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.encode("thereareplacesirememberallmylife!!");
    // System.out.println(Tester.deckToString(deck));
    // System.out.println(Arrays.toString(solitaireCipher.getKeystream(22)));
    String expected = "HWCWHIULXTRHLQLADPFUFSGAJFLFYCZZ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting encoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_encode_random implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 4);
    Deck.gen.setSeed(20210303);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.encode(
        "XM;iny[r=@Hk4C!j9~GHk:S2)9H\"7z/\nQ;g+CZ3hFv1*Uvrj\"FH%> tK\"DP.Rz    K.1t'[?'s?j_,ZV,``0L4'7[[9c}uiL49=A|_/oTzu;|*w9@tELWFtA3s%/y\\W\"snI,W!ULYv9.6}?ReY\\~/n x6jA/5XdsEgK\"awJ");
    String expected = "CQSTJWQJVYPJXSWCYGKKMAZIIBZOLEZOFDORBRXYEPMMBGOSKBABZWOLISYRPTSWJYIPNBQUPKPCYLBOGBHXNQ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting encoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
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
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_decode2 implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 3);
    Deck.gen.setSeed(20210314);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.decode("HWCWHIULXTRHLQLADPFUFSGAJFLFYCZZ");
    // System.out.println(Tester.deckToString(deck));
    // System.out.println(Arrays.toString(solitaireCipher.getKeystream(22)));
    String expected = "THEREAREPLACESIREMEMBERALLMYLIFE";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting decoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_decode_random implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 4);
    Deck.gen.setSeed(20210303);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.decode(
        "CQSTJWQJVYPJXSWCYGKKMAZIIBZOLEZOFDORBRXYEPMMBGOSKBABZWOLISYRPTSWJYIPNBQUPKPCYLBOGBHXNQ");
    String expected = "XMINYRHKCJGHKSHZQGCZHFVUVRJFHTKDPRZKTSJZVLCUILAOTZUWTELWFTASYWSNIWULYVREYNXJAXDSEGKAWJ";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting decoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_charShiftEncode implements Runnable {
  @Override
  public void run() {

    Deck deck = new Deck(13, 3);
    // System.out.println("Original deck");
    // System.out.println(Tester.deckToString(deck));
    Deck.gen.setSeed(20210314);
    deck.shuffle();
    // System.out.println("Shuffled Deck");
    // System.out.println(Tester.deckToString(deck));
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.encode("BOBDHUSANTA");
    // System.out.println(Tester.deckToString(deck));
    // System.out.println(Arrays.toString(solitaireCipher.getKeystream(22)));
    String expected = "PDZIKCVHVBR";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting encoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
  }
}


class SolitaireCipher_charShiftDecode implements Runnable {
  @Override
  public void run() {
    Deck deck = new Deck(13, 3);
    Deck.gen.setSeed(20210314);
    deck.shuffle();
    SolitaireCipher solitaireCipher = new SolitaireCipher(deck);
    String result = solitaireCipher.decode("PDZIKCVHVBR");
    // System.out.println(Tester.deckToString(deck));
    // System.out.println(Arrays.toString(solitaireCipher.getKeystream(22)));
    String expected = "BOBDHUSANTA";

    if (!result.equals(expected)) {
      throw new AssertionError(
          "The resulting decoded message is " + result + " but should have been " + expected);
    }
    System.out.println("Test passed.");
  }
}


/*
 * Checks that every non-private method in SolitaireCipher is one of the
 * required methods
 */
class SolitaireCipher_extra_methods implements Runnable {
  @Override
  public void run() {
    Class<SolitaireCipher> cipherClass = SolitaireCipher.class;
    TMethod[] requiredMethods = getRequiredMethods();

    for (Method m : cipherClass.getDeclaredMethods()) {
      if (!Modifier.isPrivate(m.getModifiers()) && !TMethod.elementOf(m, requiredMethods)) {
        throw new AssertionError("Extra non-private method found: " + m);
      }
    }
  }

  private TMethod[] getRequiredMethods() {
    TMethod[] requiredMethods = new TMethod[3];
    requiredMethods[0] = new TMethod(Modifier.PUBLIC, String.class, "decode",
        new Class[] { String.class }, new Class[0]);
    requiredMethods[1] = new TMethod(Modifier.PUBLIC, String.class, "encode",
        new Class[] { String.class }, new Class[0]);
    requiredMethods[2] = new TMethod(Modifier.PUBLIC, int[].class, "getKeystream",
        new Class[] { int.class }, new Class[0]);
    return requiredMethods;
  }
}


/*
 * Checks that every non-private method in Deck is one of the required methods
 */
class Deck_extra_methods implements Runnable {
  @Override
  public void run() {
    Class<Deck> deckClass = Deck.class;
    TMethod[] requiredMethods = getRequiredMethods();

    for (Method m : deckClass.getDeclaredMethods()) {
      if (!Modifier.isPrivate(m.getModifiers()) && !TMethod.elementOf(m, requiredMethods)) {
        throw new AssertionError("Extra non-private method found: " + m);
      }
    }
  }

  private TMethod[] getRequiredMethods() {
    TMethod[] requiredMethods = new TMethod[8];
    requiredMethods[0] = new TMethod(Modifier.PUBLIC, Void.TYPE, "shuffle", new Class[0], new Class[0]);
    requiredMethods[1] = new TMethod(Modifier.PUBLIC, Integer.TYPE, "generateNextKeystreamValue",
        new Class[0], new Class[0]);
    requiredMethods[2] = new TMethod(Modifier.PUBLIC, Void.TYPE, "addCard",
        new Class[] { Deck.Card.class }, new Class[0]);
    requiredMethods[3] = new TMethod(Modifier.PUBLIC, Void.TYPE, "moveCard",
        new Class[] { Deck.Card.class, Integer.TYPE }, new Class[0]);
    requiredMethods[4] = new TMethod(Modifier.PUBLIC, Void.TYPE, "tripleCut",
        new Class[] { Deck.Card.class, Deck.Card.class }, new Class[0]);
    requiredMethods[5] = new TMethod(Modifier.PUBLIC, Deck.Joker.class, "locateJoker",
        new Class[] { String.class }, new Class[0]);
    requiredMethods[6] = new TMethod(Modifier.PUBLIC, Void.TYPE, "countCut", new Class[0], new Class[0]);
    requiredMethods[7] = new TMethod(Modifier.PUBLIC, Deck.Card.class, "lookUpCard", new Class[0], new Class[0]);
    return requiredMethods;
  }
}


class SolitaireCipher_extra_fields implements Runnable {
  @Override
  public void run() {
    Class<SolitaireCipher> cipherClass = SolitaireCipher.class;
    TField[] requiredFields = getRequiredFields();

    for (Field f : cipherClass.getDeclaredFields()) {
      if (!Modifier.isPrivate(f.getModifiers()) && !TField.elementOf(f, requiredFields))
        throw new AssertionError("Extra field found: " + f);
    }
  }

  private TField[] getRequiredFields() {
    TField[] requiredFields = new TField[1];
    requiredFields[0] = new TField(Modifier.PUBLIC, Deck.class, "key");
    return requiredFields;
  }
}


class Deck_extra_fields implements Runnable {
  @Override
  public void run() {
    Class<Deck> deckClass = Deck.class;
    TField[] requiredFields = getRequiredFields();

    for (Field f : deckClass.getDeclaredFields()) {
      if (!Modifier.isPrivate(f.getModifiers()) && !TField.elementOf(f, requiredFields))
        throw new AssertionError("Extra field found: " + f);
    }
  }

  private TField[] getRequiredFields() {
    TField[] requiredFields = new TField[4];
    requiredFields[0] = new TField(Modifier.PUBLIC + Modifier.STATIC, String[].class, "suitsInOrder");
    requiredFields[1] = new TField(Modifier.PUBLIC + Modifier.STATIC, java.util.Random.class, "gen");
    requiredFields[2] = new TField(Modifier.PUBLIC, int.class, "numOfCards");
    requiredFields[3] = new TField(Modifier.PUBLIC, Deck.Card.class, "head");
    return requiredFields;
  }
}


class SolitaireCipher_extra_constructors implements Runnable {
  @Override
  @SuppressWarnings("rawtypes")
  public void run() {
    Class<SolitaireCipher> cipherClass = SolitaireCipher.class;
    TConstructor[] requiredConstructors = getRequiredConstructors();

    for (Constructor c : cipherClass.getDeclaredConstructors()) {
      if (!TConstructor.elementOf(c, requiredConstructors))
        throw new AssertionError("Extra constructor found: " + c);
    }
  }

  public TConstructor[] getRequiredConstructors() {
    TConstructor[] requiredConstructors = new TConstructor[1];
    // Get rid of "class " at beginning of name
    String name = SolitaireCipher.class.toString().split(" ")[1];

    requiredConstructors[0] = new TConstructor(Modifier.PUBLIC, name,
        new Class[] { Deck.class }, new Class[0]);
    return requiredConstructors;
  }
}


class Deck_extra_constructors implements Runnable {
  @SuppressWarnings("rawtypes")
  @Override
  public void run() {
    Class<Deck> deckClass = Deck.class;
    TConstructor[] requiredConstructors = getRequiredConstructors();

    for (Constructor c : deckClass.getDeclaredConstructors()) {
      if (!TConstructor.elementOf(c, requiredConstructors))
        throw new AssertionError("Extra constructor found: " + c);
    }
  }

  public TConstructor[] getRequiredConstructors() {
    TConstructor[] requiredConstructors = new TConstructor[3];
    // Get rid of "class" at beginning of name
    String name = Deck.class.toString().split(" ")[1];

    requiredConstructors[0] = new TConstructor(Modifier.PUBLIC, name, new Class[] { Deck.class }, new Class[0]);
    requiredConstructors[1] = new TConstructor(Modifier.PUBLIC, name, new Class[] { int.class, int.class },
        new Class[0]);
    requiredConstructors[2] = new TConstructor(Modifier.PUBLIC, name, new Class[0], new Class[0]);
    return requiredConstructors;
  }
}


@SuppressWarnings("rawtypes")
class SolitaireCipher_extra_classes implements Runnable {
  @Override
  public void run() {
    Class<SolitaireCipher> cipherClass = SolitaireCipher.class;
    Class[] requiredClasses = getRequiredClasses();

    for (Class c : cipherClass.getDeclaredClasses()) {
      if (!Arrays.asList(requiredClasses).contains(c))
        throw new AssertionError("Extra nested class found: " + c);
    }
  }

  public Class[] getRequiredClasses() {
    return new Class[0];
  }
}


@SuppressWarnings("rawtypes")
class Deck_extra_classes implements Runnable {
  @Override
  public void run() {
    Class<Deck> deckClass = Deck.class;
    Class[] requiredClasses = getRequiredClasses();

    for (Class c : deckClass.getDeclaredClasses()) {
      if (!Arrays.asList(requiredClasses).contains(c))
        throw new AssertionError("Extra nested class found: " + c);
    }
  }

  public Class[] getRequiredClasses() {
    Class[] requiredClasses = new Class[3];
    requiredClasses[0] = Deck.Card.class;
    requiredClasses[1] = Deck.PlayingCard.class;
    requiredClasses[2] = Deck.Joker.class;
    return requiredClasses;
  }
}


class General_extra_code implements Runnable {
  private static String[] tests = {
      "assignment2.Deck_extra_methods",
      "assignment2.Deck_extra_fields",
      "assignment2.Deck_extra_constructors",
      "assignment2.Deck_extra_classes",
      "assignment2.SolitaireCipher_extra_methods",
      "assignment2.SolitaireCipher_extra_fields",
      "assignment2.SolitaireCipher_extra_constructors",
      "assignment2.SolitaireCipher_extra_classes"
  };

  @Override
  public void run() {
    for (String str : tests) {
      try {
        Runnable testCase = (Runnable) Class.forName(str).getDeclaredConstructor().newInstance();
        testCase.run();
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException | NoSuchMethodException | SecurityException
          | ClassNotFoundException e) {
      }
    }

    System.out.println("Test passed.");
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
    System.out.println(
        "SolitaireCipher message decoding yielded: " + result + " on announcement secret message.");
  }
}

// Utility classes
//==========================================================================================


/*
 * Stores information about methods. Is meant to be compared to instances of
 * java.lang.reflect.Method (which has no public constructor).
 */
@SuppressWarnings("rawtypes")
class TMethod {
  private int modifiers;
  private Class returnType;
  private String name;
  private Class[] params;
  private Class[] exceptions;

  /*
   * Creates a new TMethod by saving all the given arguments directly to the
   * corresponding fields
   */
  public TMethod(int modifiers, Class returnType, String name, Class[] params, Class[] exceptions) {
    this.modifiers = modifiers;
    this.returnType = returnType;
    this.name = name;
    this.params = params;
    this.exceptions = exceptions;
  }

  /*
   * A TMethod is equal to a TMethod or a Method if and only if all its fields
   * match
   * 
   * This operation is not commutative for TMethods and Methods
   */
  public boolean equals(Object o) {
    if (o instanceof Method) {
      Method m = (Method) o;
      return this.modifiers == m.getModifiers() && this.returnType.equals(m.getReturnType())
          && this.name.equals(m.getName()) && Arrays.equals(this.params, m.getParameterTypes())
          && Arrays.equals(this.exceptions, m.getExceptionTypes());
    } else if (o instanceof TMethod) {
      TMethod t = (TMethod) o;
      return this.modifiers == t.modifiers && this.returnType.equals(t.returnType)
          && this.name.equals(t.name) && Arrays.equals(this.params, t.params)
          && Arrays.equals(this.exceptions, t.exceptions);
    } else
      return false;
  }

  /*
   * Checks if method is equal (using TMethod.equals(method)) to any of the
   * elements in tMethods
   */
  @SuppressWarnings("unlikely-arg-type")
  public static boolean elementOf(Method method, TMethod[] tMethods) {
    for (TMethod t : tMethods) {
      if (t.equals(method))
        return true;
    }
    return false;
  }
}


/*
 * Stores information about Fields. Is meant to be compared to instances of
 * java.lang.reflect.Field (which has no public constructor).
 */
@SuppressWarnings("rawtypes")
class TField {
  private int modifiers;
  private Class type;
  private String name;

  /*
   * Creates a new TField by saving all the given arguments directly to the
   * corresponding fields
   */
  public TField(int modifiers, Class type, String name) {
    this.modifiers = modifiers;
    this.type = type;
    this.name = name;
  }

  /*
   * A TField is equal to a TField or a Field if and only if all its fields match
   * 
   * This operation is not commutative for TFields and Fields
   */
  public boolean equals(Object o) {
    if (o instanceof Field) {
      Field f = (Field) o;
      return this.modifiers == f.getModifiers() && this.type.equals(f.getType())
          && this.name.equals(f.getName());
    } else if (o instanceof TField) {
      TField t = (TField) o;
      return this.modifiers == t.modifiers && this.type.equals(t.type) && this.name.equals(t.name);
    } else
      return false;
  }

  /*
   * Checks if field is equal (using TField.equals(field)) to any of the elements
   * in tFields
   */
  @SuppressWarnings("unlikely-arg-type")
  public static boolean elementOf(Field field, TField[] tFields) {
    for (TField t : tFields) {
      if (t.equals(field))
        return true;
    }
    return false;
  }
}


/*
 * Stores information about Constructors. Is meant to be compared to instances
 * of java.lang.reflect.Constructor (which has no public constructor*).
 * 
 * * "Ironic. He could constructor others, but not himself."
 */
@SuppressWarnings("rawtypes")
class TConstructor {
  private int modifiers;
  private String name;
  private Class[] params;
  private Class[] exceptions;

  /*
   * Creates a new TMethod by saving all the given arguments directly to the
   * corresponding fields
   */
  public TConstructor(int modifiers, String name, Class[] params, Class[] exceptions) {
    this.modifiers = modifiers;
    this.name = name;
    this.params = params;
    this.exceptions = exceptions;
  }

  /*
   * A TConstructor is equal to a TConstructor or a Constructor if and only if all
   * its fields match
   * 
   * This operation is not commutative for TConstructors and Constructors
   */
  public boolean equals(Object o) {
    if (o instanceof Constructor) {
      Constructor c = (Constructor) o;
      return this.modifiers == c.getModifiers() && this.name.equals(c.getName())
          && Arrays.equals(this.params, c.getParameterTypes())
          && Arrays.equals(this.exceptions, c.getExceptionTypes());
    } else if (o instanceof TConstructor) {
      TConstructor t = (TConstructor) o;
      return this.modifiers == t.modifiers && this.name.equals(t.name)
          && Arrays.equals(this.params, t.params) && Arrays.equals(this.exceptions, t.exceptions);
    } else
      return false;
  }

  /*
   * Checks if constructor is equal (using TConstructor.equals(constructor)) to
   * any of the elements in tConstructors
   */
  @SuppressWarnings("unlikely-arg-type")
  public static boolean elementOf(Constructor constructor, TConstructor[] tConstructors) {
    for (TConstructor t : tConstructors) {
      if (t.equals(constructor))
        return true;
    }
    return false;
  }
}

// Main class
//================================================================================


public class Tester {
  static String[] tests = {
      "assignment2.General_extra_code",
      "assignment2.AddCard_AllRef",
      "assignment2.AddCard_CheckHead",
      "assignment2.AddCard_Circular",
      "assignment2.AddCard_NumOfCards",
      "assignment2.AddCard_SingleCard",
      "assignment2.DeepCopy_CheckRefs",
      "assignment2.DeepCopy_CircularNext",
      "assignment2.DeepCopy_CircularPrev",
      "assignment2.LocateJoker_Test1",
      "assignment2.LocateJoker_Test2",
      "assignment2.LocateJoker_Test3",
      "assignment2.LookUpCard_Test1",
      "assignment2.LookUpCard_Test2",
      "assignment2.LookUpCard_Test3",
      "assignment2.MoveCard_CheckNext1",
      "assignment2.MoveCard_CheckNext2",
      "assignment2.MoveCard_CheckPrev1",
      "assignment2.MoveCard_CheckPrev2",
      "assignment2.Shuffle_Empty",
      "assignment2.Shuffle_Example",
      "assignment2.Shuffle_FullDeck",
      "assignment2.Shuffle_NewCard",
      "assignment2.Shuffle_SingleCard",
      "assignment2.Shuffle_Three",
      "assignment2.Deck_Deck_one_card",
      "assignment2.Deck_Deck_all_cards",
      "assignment2.Deck_Deck_suitsInOrder_changed",
      "assignment2.Deck_Deck_too_many_cards",
      "assignment2.Deck_Deck_too_few_cards",
      "assignment2.Deck_Deck_too_many_suits",
      "assignment2.Deck_Deck_too_many_suits2",
      "assignment2.Deck_Deck_too_few_suits",
      "assignment2.Deck_Deck_copy",
      "assignment2.Deck_Deck_deep_copy",
      "assignment2.Deck_addCard",
      "assignment2.Deck_numOfCards",
      "assignment2.Deck_shuffle",
      "assignment2.Deck_locate_joker",
      "assignment2.Deck_locate_joker_top_or_bottom_cards",
      "assignment2.Deck_locate_joker_no_jokers",
      "assignment2.Deck_locate_joker_wrong_color",
      "assignment2.Deck_move_card_no_change",
      "assignment2.Deck_move_card_with_change",
      "assignment2.Deck_move_card_loop_around",
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
      "assignment2.SolitaireCipher_encode2",
      "assignment2.SolitaireCipher_decode2",
      "assignment2.SolitaireCipher_charShiftEncode",
      "assignment2.SolitaireCipher_charShiftDecode",
      "assignment2.SolitaireCipher_encode_random",
      "assignment2.SolitaireCipher_decode_random",
      "assignment2.default_deck_more_than_one_card",
      "assignment2.deep_copy_deck",
      "assignment2.addOneCardToBottom",
      "assignment2.shuffleDeck",
      "assignment2.locatejoker",
      "assignment2.noJokersInDeck",
      "assignment2.moveCardHead",
      "assignment2.moveCardTailby1",
      "assignment2.moveCardTailby2",
      "assignment2.tripleCut",
      "assignment2.tripleCutFirstCardHead",
      "assignment2.tripleCutsecondcardbottom",
      "assignment2.countCut",
      "assignment2.lookUpCard",
      "assignment2.generateNextKeystreamValue",
      "assignment2.SolitaireCipher_decode_secret_message",
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
      System.out.println("All clear!\n\nDon't forget to check the time complexity of your methods."
          + "\nSasha\'s RuntimeTester is a great tool for this:"
          + "\nhttps://www.youtube.com/watch?v=UNCopqdTX00"
          + "\n\nYou can find some basic tests (using the RuntimeTester) in TimeTester.java");
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
        throw new AssertionError(
            "The links between card " + i + " and card " + (i + 1) + " are inconsistent.");

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
