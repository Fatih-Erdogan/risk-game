package storageservice;

import domain.object.Territory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDbStorageService {

	private MongoDatabase database = MongoDbJava.getDatabase();

	public List<String[]> readCollection(String collectionName){
		MongoCollection<Document> collection = database.getCollection(collectionName);
		List<String[]> valuesList = new ArrayList<>();

		MongoCursor<Document> cursor = collection.find().iterator();
		while (cursor.hasNext()) {
			Document document = cursor.next();
			List<String> propertyValues = new ArrayList<>();

			for (String key : document.keySet()) {
				if (collectionName == "SavedA_t_cards") {
					if (key.equals("_id")) {
						continue;
					}
				}
				Object value = document.get(key);
				if (value instanceof List) {
					List<?> listValue = (List<?>) value;
					for (Object listItem : listValue) {
						String strListItem = (listItem != null) ? listItem.toString() : "";
						propertyValues.add(strListItem);
					}
				} else {
					String strValue = (value != null) ? value.toString() : "";
					propertyValues.add(strValue);
				}
			}

			valuesList.add(propertyValues.toArray(new String[0]));
		}
		cursor.close();
		return valuesList;
	}
	
	public void writeCollection(Document document, String collectionName){
		MongoCollection<Document> collection = database.getCollection(collectionName);
		collection.insertOne(document);		
    }
		
	public void resetCollection(String collectionName){
		MongoCollection<Document> collection = database.getCollection(collectionName);
		//collection.drop();
		//database.getCollection(collectionName).drop();
		collection.deleteMany(new Document());
    }
	
	public List<String[]> readFile(String path, String delimiter){
        String csvFile = path;
        String line = "";
        String cvsSplitBy = delimiter;
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) { continue; }
                String[] values = line.split(cvsSplitBy);
                lines.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
