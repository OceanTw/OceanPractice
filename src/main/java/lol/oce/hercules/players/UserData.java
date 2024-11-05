package lol.oce.hercules.players;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.hercules.database.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;

import javax.print.Doc;

public class UserData {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public UserData() {
        this.database = MongoDB.getDatabase();
        this.collection = MongoDB.getCollection("users");
    }

    public void saveUser(User user) {
        Document original = collection.find(new Document("player", user.player.getUniqueId().toString())).first();
        Document doc = new Document("player", user.player.getUniqueId().toString())
                .append("kitStats", user.kitStats.serialize());

        if (original == null) {
            collection.insertOne(doc);
        } else {
            collection.replaceOne(original, doc);
        }
    }

    public User loadUser(String playerName) {
        Document doc = collection.find(new Document("player", playerName)).first();
        if (doc == null) {
            return createUser(playerName);
        }
        return User.builder()
                .setMatch(null)
                .setPlayer(Bukkit.getPlayer(doc.getString("player")))
                .setStatus(UserStatus.IN_LOBBY)
                .setKitStats(new UserKitStats().deserialize(doc.getString("kitStats")))
                .build();
    }

    public User createUser(String playerName) {
        User user = User.builder()
                .setPlayer(Bukkit.getPlayer(playerName))
                .setMatch(null)
                .setStatus(UserStatus.IN_LOBBY)
                .setKitStats(new UserKitStats())
                .build();
        user.player = Bukkit.getPlayer(playerName);
        user.kitStats = new UserKitStats();
        user.status = UserStatus.IN_LOBBY;
        saveUser(user);
        return user;
    }
}