package assignment2;

import java.util.HashSet;
import java.util.Set;


class AddCard_AllRef implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        boolean c1ref = c1.next == c2 && c1.prev == c3;
        boolean c2ref = c2.next == c3 && c2.prev == c1;
        boolean c3ref = c3.next == c1 && c3.prev == c2;
        if (!(c1ref && c2ref && c3ref)){
            throw new AssertionError("Circular doubly linked list references are not correctly set up.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_CheckHead implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c8);
        Deck.Card head = deck.head;
        if (head != c1){
            throw new AssertionError("addCard should add the input card to the bottom of the deck.\n" +
                    "Expected head to be " + c1 + " but got " + head);
        }
        System.out.println("Test passed.");
    }
}


class AddCard_Circular implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(c8);
        if (!(c1.prev == c8 && c8.next == c1)){
            throw new AssertionError("Circular references are not correctly set up.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_NumOfCards implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card d11 = deck.new PlayingCard(Deck.suitsInOrder[1], 11); //JD
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(d11);
        deck.addCard(c3);
        int expected = 4;
        int result = deck.numOfCards;
        if (expected != result){
            throw new AssertionError("numOfCards is not correctly updated.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_SingleCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        deck.addCard(c1);
        if (!(c1.prev == c1 && c1.next == c1)){
            throw new AssertionError("Card references are not correctly set up when the deck contains only ONE card.");
        }
        System.out.println("Test passed.");
    }
}


class DeepCopy_CheckRefs implements Runnable{
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

        for(Deck.Card c: cardSet) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;
        for (int i = 0; i < cardSet.size(); i++) {
            if (cardSet.contains(cur)){
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
        Deck.Card[] cards = new Deck.Card[]{
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
                            + i + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());
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
        Deck.Card[] cards = new Deck.Card[]{
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
                            + j + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());
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


class LocateJoker_Test1 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<13; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("red");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LocateJoker_Test2 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<14; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("black");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LocateJoker_Test3 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,4 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<53; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("black");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test1 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head.next;
        Deck.Card received = tdeck.lookUpCard();
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test2 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(11, 4);
        Deck.Card old_head = tdeck.head;
        tdeck.head = tdeck.new PlayingCard(Deck.suitsInOrder[3], 2);
        tdeck.head.next = old_head.next;
        tdeck.head.prev = old_head.prev;

        Deck.Card expected = tdeck.head;

        for(int i = 0; i<41 ; ++i)
        {
            expected = expected.next;
        }

        Deck.Card received = tdeck.lookUpCard();
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. " +
                    "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test3 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(8, 4);
        Deck.Card old_head = tdeck.head;
        tdeck.head = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7);
        tdeck.head.next = old_head.next;
        tdeck.head.prev = old_head.prev;


        Deck.Card received = tdeck.lookUpCard();
        if (received != null){
            throw new AssertionError("Null should be returned in case a Joker is found.");
        }
        System.out.println("Test passed.");
    }
}


class MoveCard_CheckNext1 implements Runnable {
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
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

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            if(expected[i] != cur) {
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
        Deck.Card[] cards = new Deck.Card[]{
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

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            if(expected[i] != cur) {
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
        Deck.Card[] cards = new Deck.Card[]{
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

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            if(expected[i] != cur) {
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
        Deck.Card[] cards = new Deck.Card[]{
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

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            if(expected[i] != cur) {
                throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
            }
            cur = cur.prev;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Empty implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();

        deck.shuffle();

        if (!(deck.head == null)){
            throw new AssertionError("Deck should be empty.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Example implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Deck.Card[] arrDeck = new Deck.Card[12];
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
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
        int[] shuffledIndex = {2,7,5,4,11,1,6,8,0,10,3,9};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 12; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_FullDeck implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // all 54 cards
        Deck.Card[] arrDeck = new Deck.Card[54];
        for (int i = 0; i < 52; i++) {
            int suit = i/13;
            int rank = i%13 + 1;
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
                26, 53, 41, 44, 15, 50, 39, 19, 1, 13, 51, 48, 46, 9, 37, 0, 27};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 54; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_NewCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Set<Deck.Card> cardSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
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
        for (int i = 0; i < 12; i++) {
            if (!cardSet.contains(cur)){
                throw new AssertionError("Shuffle should not create new cards.");
            }
            cur = cur.next;
        }
        if (cur != deck.head){
            throw new AssertionError("Deck is not correctly shuffled. " +
                    "Tail does not connect to head or new cards were added.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_SingleCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c = deck.new Joker("red");
        deck.addCard(c);

        deck.shuffle();

        if (!(deck.head.getValue() == c.getValue() &&
                c.next.getValue() == c.getValue() && c.prev.getValue() == c.getValue())){
            throw new AssertionError("Deck is not correctly shuffled when it only has one card.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Three implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // AC 2C 3C 4C 5C
        Deck.Card[] arrDeck = new Deck.Card[5];
        for (int i = 0; i < 5; i++) {
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[0], i+1);
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

        int[] shuffledIndex = {0, 4, 2, 1, 3};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 5; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


public class A2_Minitester_Updated1 {
    // To skip running some tests, just comment them out below.
    static String[] tests = {
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
        "assignment2.Shuffle_Three"
    };
    public static void main(String[] args) {
        int numPassed = 0;
        for(String className: tests)    {
            System.out.printf("%n======= %s =======%n", className);
            System.out.flush();
            try {
                Runnable testCase = (Runnable) Class.forName(className).getDeclaredConstructor().newInstance();
                testCase.run();
                numPassed++;
            } catch (AssertionError e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        System.out.printf("%n%n%d of %d tests passed.%n", numPassed, tests.length);
    }
}
