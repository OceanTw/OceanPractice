package lol.oce.vpractice.players;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.vpractice.database.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;

public class UserRepository {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public UserRepository() {
        this.database = MongoDB.getDatabase();
        this.collection = database.getCollection("users");
    }

    public void saveUser(User user) {
        Document doc = new Document("player", user.player.getUniqueId())
                .append("kitStats", user.kitStats.serialize())
                .append("status", user.status.toString());
        collection.insertOne(doc);
    }

    public User loadUser(String playerName) {
        Document doc = collection.find(new Document("player", playerName)).first();
        if (doc == null) {
            return null;
        }
        User user = new User();
        user.player = Bukkit.getPlayer(doc.getString("player"));
        user.kitStats = new UserKitStats().deserialize(doc.getString("kitStats"));
        user.status = UserStatus.valueOf(doc.getString("status"));
        return user;
    }
}