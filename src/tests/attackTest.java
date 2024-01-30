package tests;

import domain.object.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.awt.*;
import java.util.TreeMap;


import domain.object.BuildGame;
import domain.object.Map;
import domain.object.Player;
import domain.object.Territory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class attackTest {
        BuildGame buildgame;
        Game game;

        Turn turn;

        @BeforeAll
        @DisplayName("Attack Test Setup")
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
            System.out.printf("Turn of %s\n",turn.getPlayer());

            System.out.println("Attack Test is setup");
        }

    @Test
    void testConstructAttack() {
        Territory from = Territory.findFromId(27);
        Territory to = Territory.findFromId(31);
        from.setNumOfArmy(5);
        to.setNumOfArmy(3);
        assertTrue(turn.constructAttack(from, to, 1,0) , "Type 1 attack constructed");
    }

    @Test
    void testAttackOption1() {
            //glass
        Territory from = Territory.findFromId(27);
        Territory to = Territory.findFromId(31);
        from.setNumOfArmy(5);
        to.setNumOfArmy(3);
        turn.constructAttack(from, to, 1,0);
        Attack attack = turn.getAttack();
        Player winner = attack.attack();
        Player manuelwinner ;
        if(from.getNumOfArmy()<5){
            manuelwinner = to.getOwner();
        }else {
            manuelwinner = from.getOwner();
        }
        assertEquals( manuelwinner,winner ,"check winner");
    }
    @Test
    void testAttackOption2() {
        //glass
        Territory from = Territory.findFromId(27);
        Territory to = Territory.findFromId(31);
        from.setNumOfArmy(5);
        to.setNumOfArmy(3);
        turn.constructAttack(from, to, 2,0);
        Attack attack = turn.getAttack();
        Player winner = attack.attack();
        Player manuelwinner ;
        if(from.getNumOfArmy()<5){
            manuelwinner = to.getOwner();
        }else {
            manuelwinner = from.getOwner();
        }
        assertEquals( manuelwinner,winner ,"check winner");
    }
    @Test
    void testAttackOption3() {
        //glass
        Territory from = Territory.findFromId(27);
        Territory to = Territory.findFromId(31);
        from.setNumOfArmy(5);
        to.setNumOfArmy(3);
        turn.constructAttack(from, to, 3,2);
        Attack attack = turn.getAttack();
        Player winner = attack.attack();
        Player manuelwinner ;
        if(from.getNumOfArmy()<5){
            manuelwinner = to.getOwner();
        }else {
            manuelwinner = from.getOwner();
        }
        assertEquals( manuelwinner,winner ,"check winner");
    }
    @Test
    void testConquered() {
        //glass
        Territory from = Territory.findFromId(27);
        Territory to = Territory.findFromId(31);
        from.setNumOfArmy(5);
        to.setNumOfArmy(3);
        turn.constructAttack(from, to, 3,0);
        Attack attack = turn.getAttack();
        boolean conq = false;
        if(to.getNumOfArmy()== 0){
            conq = true;
        }

        assertEquals( conq, turn.attack() ,"check conquered");
    }
}
