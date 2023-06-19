package me.idbi.hcf.Tools.Database.MongoDB;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import me.idbi.hcf.Main;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBDriver {
    private static final Main m = Main.getPlugin(Main.class);
    public enum MongoCollections {
        MEMBERS("members"),
        DEATHBANS("deathbans"),
        CLAIMS("claims"),
        FACTIONS("factions"),
        RANKS("ranks");

        private String name;

        MongoCollections(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
    private static MongoClient client = null;
    @Getter
    private static MongoDatabase database = null;

    public static void Connect(String url, String database) {
        Logger logger = Logger.getLogger("org.mongodb.driver");
        logger.setLevel(Level.SEVERE);
        try {
            //"mongodb+srv://idbideveloper:<password>@countingbot.q2is5ou.mongodb.net/?retryWrites=true&w=majority"
            ConnectionString connectionString = new ConnectionString(url);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToSocketSettings(builder -> builder.connectTimeout(5000, TimeUnit.MILLISECONDS))
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();
            client = MongoClients.create(settings);

            MongoDBDriver.database = client.getDatabase(database);
            // Create collections if needed
            MongoIterable<String> collection = MongoDBDriver.database.listCollectionNames();
            for (MongoCollections s : MongoCollections.values()) {
                if (!isCollectionExists(s.getName(), collection)) {
                    MongoDBDriver.database.createCollection(s.getName());
                }
            }


            //Ready to use
        }catch (Exception e){
            e.printStackTrace();
            Main.sendCmdMessage("Could not connect to the MongoDB Servers! Please check your password / username!");
        }

    }

    private static boolean isCollectionExists(String name, MongoIterable<String> iterable){
        for (String s : iterable)
            if(s.equalsIgnoreCase(name))
                return true;
        return false;
    }
    private static boolean isDatabaseExists(String name, MongoIterable<String> iterable){
        for (String s : iterable)
            if(s.equalsIgnoreCase(name))
                return true;
        return false;
    }

    public static void Insert(String collection, Document document) {
        new BukkitRunnable() {
            @Override
            public void run() {

                MongoCollection<Document> col = database.getCollection(collection);
                InsertOneResult res = col.insertOne(document);
            }
    }.runTaskAsynchronously(m);
    }

    public static void Insert(MongoCollections collection, Document document) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = database.getCollection(collection.getName());
                InsertOneResult res =  col.insertOne(document);

            }
        }.runTaskAsynchronously(m);
    }

    /**
     *
     * @param collection The collection (MYSQL Table)
     * @param where Which document should be updated (MYSQL Where statement)
     * @param updates The actual update document (MYSQL VALUES statement)
     * <p>
     *                Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public static void Update(String collection, Bson where, Bson updates) {
        new BukkitRunnable() {
            @Override
            public void run() {
        MongoCollection<Document> col = database.getCollection(collection);
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult res = col.updateOne(where,updates,options);
        if(!res.wasAcknowledged())
            System.out.println("The request was not successful!!");
    }
}.runTaskAsynchronously(m);
    }
    public static void Update(MongoCollections collection, Bson where, Bson updates) {
        new BukkitRunnable() {
@Override
public void run() {
        MongoCollection<Document> col = database.getCollection(collection.getName());
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult res = col.updateOne(where,updates,options);
        if(!res.wasAcknowledged())
            System.out.println("The request was not successful!!");
    }
}.runTaskAsynchronously(m);
    }

    /**
     *
     * @param collection The collection (MYSQL Table)
     * @param where Which document should be updated (MYSQL Where statement)
     * @param updates The actual update document (MYSQL VALUES statement)
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public static void UpdateMany(String collection, Bson where, Bson updates) {
        new BukkitRunnable() {
            @Override
            public void run() {
        MongoCollection<Document> col = database.getCollection(collection);

        UpdateResult res = col.updateMany(where,updates);
        if(!res.wasAcknowledged())
            System.out.println("The request was not successful!!");
    }
}.runTaskAsynchronously(m);
    }
    public static void UpdateMany(MongoCollections collection, Bson where, Bson updates) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = database.getCollection(collection.getName());

                UpdateResult res = col.updateMany(where, updates);
                if (!res.wasAcknowledged())
                    System.out.println("The request was not successful!!");
            }
        }.runTaskAsynchronously(m);
    }

    /**
     *
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found document, or null
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */

    public static Document Find(String collection,Bson where){
        MongoCollection<Document> col = database.getCollection(collection);
        Document doc = col.find(where).sort(Sorts.ascending("_id")).first();
        return doc == null ? new Document() : doc;
    }
    public static Document Find(MongoCollections collection,Bson where){
        MongoCollection<Document> col = database.getCollection(collection.getName());
        Document doc = col.find(where).sort(Sorts.ascending("_id")).first();
        return doc == null ? new Document() : doc;
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */

    public static ArrayList<Document> FindAll(String collection, Bson where) {
        MongoCollection<Document> col = database.getCollection(collection);
        ArrayList<Document> retvalue = new ArrayList<>();
        try {
            FindIterable<Document> res = col.find(where).sort(Sorts.ascending("_id"));

            res.cursor().forEachRemaining(retvalue::add);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retvalue;
    }
    public static ArrayList<Document> FindAll(MongoCollections collection, Bson where) {
        MongoCollection<Document> col = database.getCollection(collection.getName());
        ArrayList<Document> retvalue = new ArrayList<>();
        try {
            FindIterable<Document> res = col.find(where).sort(Sorts.ascending("_id"));
            res.cursor().forEachRemaining(retvalue::add);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retvalue;
    }
    /**
     *
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */

    public static void Delete(String collection, Bson where) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = database.getCollection(collection);
                col.deleteMany(where);
            }
        }.runTaskAsynchronously(m);
    }
    public static void Delete(MongoCollections collection, Bson where) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = database.getCollection(collection.getName());
                col.deleteMany(where);
            }
    }.runTaskAsynchronously(m);
    }

}
