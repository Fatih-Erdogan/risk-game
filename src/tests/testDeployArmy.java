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
public class testDeployArmy {
    BuildGame buildgame;
    Game game;

    Turn turn;

    @BeforeAll
    @DisplayName("Deploy Test Setup")
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

        System.out.println("Deploy Test is setup");
    }

    @Test
    void testDeployOneArmy() {
        Territory todeploy = Territory.findFromId(27);
        System.out.println(todeploy.getNumOfArmy());
        turn.deployOneArmy(todeploy);
        System.out.println(todeploy.getNumOfArmy());
        assertTrue(turn.deployOneArmy(todeploy) , "1 army deployed");
    }
    @Test
    void testDecreasenumberofArmy() {
        Territory todeploy = Territory.findFromId(27);
        int correct = turn.getArmyToDeploy()-1;
        turn.deployOneArmy(todeploy);
        assertEquals(correct,turn.getArmyToDeploy(), "1 army decreased from player");
    }
    @Test
    void testIncreasenumberofArmy() {
        Territory todeploy = Territory.findFromId(27);
        int correct = todeploy.getNumOfArmy() +1;
        turn.deployOneArmy(todeploy);
        assertEquals(correct,todeploy.getNumOfArmy(), "1 army increased in territory");
    }

    @Test
    void testPhase() {
        Territory todeploy = Territory.findFromId(27);
        System.out.println(turn.getPhase());
        turn.deployOneArmy(todeploy);
        System.out.println(turn.getPhase());
        assertEquals(TERRITORYSELECTION,turn.getPhase(), "in correct Phase");
    }
    @Test
    void testNextTurn() {
        Territory todeploy = Territory.findFromId(30);
        todeploy.setNumOfArmy(40);
        System.out.println(game.getTurn());
        game.getTurn().deployOneArmy(todeploy);
        System.out.println(game.getTurn());
        assertEquals(false,turn.equals(game.getTurn()), "go to next turn");
    }

    /**
     * Turn de bul commentleri copy paste le
     * public boolean deployOneArmy(Territory t){
     *         // @requires a territory owns by the current player
     *         // @modifies t
     *         // @effects increase the number of army on territory, decrease number of army of the player,
     *         // control the phase and go to next turn
     */

}
