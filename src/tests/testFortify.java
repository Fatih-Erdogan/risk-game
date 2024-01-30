package tests;
import domain.object.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.awt.*;
import java.util.TreeMap;


import static domain.object.TurnPhase.TERRITORYSELECTION;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testFortify {
    BuildGame buildgame;
    Game game;

    Turn turn;

    @BeforeAll
    @DisplayName("Fortify Test Setup")
    void testSetup() {
        Territory.createAllTerritories("csv");
        buildgame = new BuildGame();
        buildgame.setNumberOfPlayers(3);
        buildgame.createPlayer("Berat",Color.black);
        buildgame.createPlayer("Serra",Color.blue);
        buildgame.createPlayer("Eren",Color.green);

        TreeMap<Integer, Player> tmap = new TreeMap<>();
        int i = 1;
        for (Player p : Player.getAllPlayers()){
            tmap.put(i, p);
            i++;
        }
        buildgame.createTurnList(tmap);
        Territory.findFromId(27).setOwner(buildgame.getTurnList().get(0));
        Territory.findFromId(28).setOwner(buildgame.getTurnList().get(0));
        Territory.findFromId(29).setOwner(buildgame.getTurnList().get(0));
        Territory.findFromId(30).setOwner(buildgame.getTurnList().get(1));
        Territory.findFromId(31).setOwner(buildgame.getTurnList().get(1));
        buildgame.start();
        game = Game.getGame();
        turn = game.getTurn();
        System.out.println(buildgame.getTurnList());
        System.out.printf("Turn of %s\n",turn.getPlayer());

        System.out.println("Fortify Test is setup");
    }

    @Test
    void testFortify() {
        Territory to = Territory.findFromId(27);
        Territory from = Territory.findFromId(28);
        from.setNumOfArmy(10);
        to.setNumOfArmy(5);

        turn.fortify(from, to,3 );
        System.out.println(from);
        System.out.println(to);
        assertTrue(turn.fortify(from, to,3 ) , "correct fortify");
    }
    @Test
    void testFalseFortify() {
        Territory to = Territory.findFromId(27);
        Territory from = Territory.findFromId(28);
        from.setNumOfArmy(10);
        to.setNumOfArmy(5);

        assertTrue(!turn.fortify(from, to,11 ) , "false fortify");
    }
    @Test
    void testIncrease() {
        Territory to = Territory.findFromId(27);
        Territory from = Territory.findFromId(28);
        from.setNumOfArmy(10);
        to.setNumOfArmy(5);
        turn.fortify(from, to,3 );
        assertEquals( 8,to.getNumOfArmy(), "increase army in target territory");
    }
    @Test
    void testDecrease() {
        Territory to = Territory.findFromId(27);
        Territory from = Territory.findFromId(28);
        from.setNumOfArmy(10);
        to.setNumOfArmy(5);
        turn.fortify(from, to,3 );
        assertEquals( 7,from.getNumOfArmy(), "decrese army in home territory");
    }
    @Test
    void testNextTurn() {
        Territory to = Territory.findFromId(27);
        Territory from = Territory.findFromId(28);
        from.setNumOfArmy(10);
        to.setNumOfArmy(5);
        Turn curr = game.getTurn();
        turn.fortify(from, to,3 );
        Turn next = game.getTurn();
        assertEquals( true,curr.equals(next), "go to next turn");
    }
    /**
     * Turn de fortifyi bul commentleri copy pastele.
     * public boolean fortify(Territory from, Territory to, int numOfArmies){
     *         // @requires two territories owned by the current player
     *         // @modifies from, to
     *         // @effects increase the number of army on target territory, decrease number of army at home territory,
     *         // and go to next turn
     */



}
