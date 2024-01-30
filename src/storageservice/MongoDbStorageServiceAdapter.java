package storageservice;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mongodb.client.MongoCollection;

import domain.object.ArmyCard;
import domain.object.Card;
import domain.object.Player;
import domain.object.Territory;

import org.bson.Document;

public class MongoDbStorageServiceAdapter implements IStorageServiceAdapter {
	
	private MongoDbStorageService mongoService;
	private final String territoriesPath = "src/data/territories.csv";
    private final String territoryLinksPath = "src/data/territoryLinks.csv";
    private final String savedTerritoriesPath = "SavedTerritories";
    private final String savedTerritoryLinksPath = "SavedAdjacents";
    private final String savedPlayersPath = "SavedPlayers";
    private final String savedArmyAndTerritoryCardsPath = "SavedA_t_cards";

    public MongoDbStorageServiceAdapter(){
        this.mongoService = new MongoDbStorageService();
    }
    
    @Override
    public List<String[]> getTerritoryLines() {
        return this.mongoService.readFile(this.territoriesPath, ",");
    }
    @Override
    public List<String[]> getAdjacentIndicesLines() {
        return this.mongoService.readFile(this.territoryLinksPath, ",");
    }
	@Override
	public void saveTerritories(Set<Territory> territories) {
		this.mongoService.resetCollection(savedTerritoriesPath);
		this.mongoService.resetCollection(savedTerritoryLinksPath);
		String ter_path = this.savedTerritoriesPath;
        String adj_path = this.savedTerritoryLinksPath;
		for (Territory t : territories){
            Document entry = this.constructTerritoryEntry(t);
            Document adjs = this.constructAdjacencyEntry(t);
            this.mongoService.writeCollection(entry, ter_path);
            this.mongoService.writeCollection(adjs, adj_path);
        }
        System.out.println("File write operation completed.");		
	}
	@Override
	public void savePlayers(LinkedList<Player> players) {
		this.mongoService.resetCollection(savedPlayersPath);
        this.mongoService.resetCollection(savedArmyAndTerritoryCardsPath);	
		String p_path = this.savedPlayersPath;
        String a_t_path = this.savedArmyAndTerritoryCardsPath;
        for (Player p : players){
        	Document entry = this.constructPlayerEntry(p);
        	this.mongoService.writeCollection(entry, p_path);
        	for (Card c : p.getArmyAndTerritoryCards()) {
        		Document a_t_entry = this.constructArmyAndTerritoryEntry(p.getPlayerNum(), c);
        		this.mongoService.writeCollection(a_t_entry, a_t_path);
        	}            
        }		
	}
	@Override
	public void saveArmyAndTerritoryCards(ArrayList<Card> cards) {
		String a_t_path = this.savedArmyAndTerritoryCardsPath;
        for (Card c : cards){
        	Document entry = this.constructArmyAndTerritoryEntry(0, c);
            this.mongoService.writeCollection(entry, a_t_path);
        }		
	}
	@Override
	public void resetSavedFiles() {
		this.mongoService.resetCollection(savedTerritoriesPath);
        this.mongoService.resetCollection(savedTerritoryLinksPath);
        this.mongoService.resetCollection(savedPlayersPath);
        this.mongoService.resetCollection(savedArmyAndTerritoryCardsPath);		
	}
	@Override
	public List<String[]> loadPlayers() {
		String savedPlayersPath = this.savedPlayersPath;
        return this.mongoService.readCollection(savedPlayersPath);
	}
	@Override
	public List<String[]> loadTerritories() {
		String savedTerritoriesPath = this.savedTerritoriesPath;
		return this.mongoService.readCollection(savedTerritoriesPath);
	}
	@Override
	public List<String[]> loadNeighbors() {
		String savedTerritoryLinksPath = this.savedTerritoryLinksPath;
		return this.mongoService.readCollection(savedTerritoryLinksPath);
	}
	@Override
	public List<String[]> loadArmyAndTerritoryCards() {
		String savedArmyAndTerritoryCardsPath = this.savedArmyAndTerritoryCardsPath;
		return this.mongoService.readCollection(savedArmyAndTerritoryCardsPath);
	}
	
	private Document constructTerritoryEntry(Territory t){
		Document document = new Document("_id",t.getId())
				.append("isEnabled",t.isEnabled() ? 1 : 0)
				.append("NumArmy",t.getNumOfArmy())
				.append("Owner",t.isEnabled() ? t.getOwner().getPlayerNum() : "nan");
        return document;
    }

    private Document constructAdjacencyEntry(Territory t){
    	Document document = new Document("_id",t.getId());
        List<Integer> adj = new ArrayList<>();
        for (Territory ter : t.getNeighbors()){
        	adj.add(ter.getId());
        }
        document.append("Neighbors",adj);
        return document;
    }

    private Document constructPlayerEntry(Player p){
    	Document document = new Document("_id",p.getPlayerNum())
    			.append("Name",p.getName())
    			.append("Color",this.getColorName(p.getColor()));
        return document;
    }

    private String getColorName(Color color) {
        if (Color.RED.equals(color)) {
            return "Red";
        } else if (Color.BLUE.equals(color)) {
            return "Blue";
        } else if (Color.GREEN.equals(color)) {
            return "Green";
        } else if (Color.CYAN.equals(color)) {
            return "Cyan";
        } else if (Color.ORANGE.equals(color)) {
            return "Orange";
        } else if (Color.MAGENTA.equals(color)) {
            return "Purple";
        } else {
            return "Unknown";
        }
    }

    private Document constructArmyAndTerritoryEntry(int id, Card c) {
        Document document = new Document("player_id", id)
                .append("Card", c.getName())
                .append("Type", c instanceof ArmyCard ? 1 : 0);
        return document;
    }
}
