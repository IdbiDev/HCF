package me.idbi.hcf.Tools.Database.MongoDB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class AsyncMongoDBDriver {
    private static MongoDatabase database;

    public static void setClient(MongoDatabase d) {
        database = d;
    }

    public static CompletableFuture<BsonValue> Insert(String collection, Document document) {
        CompletableFuture<BsonValue> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection);
        InsertOneResult res = col.insertOne(document);
        if (res.wasAcknowledged()) {
            result.complete(res.getInsertedId());
        }
        result.complete(null);
        return result;
    }

    public static CompletableFuture<BsonValue> Insert(MongoDBDriver.MongoCollections collection, Document document) {
        CompletableFuture<BsonValue> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection.getName());
        InsertOneResult res = col.insertOne(document);
        if (res.wasAcknowledged()) {
            result.complete(res.getInsertedId());
        }
        result.complete(null);
        return result;
    }

    /**
     * @param collection The collection (MYSQL Table)
     * @param where      Which document should be updated (MYSQL Where statement)
     * @param updates    The actual update document (MYSQL VALUES statement)
     *                   <p>
     *                   Cheat sheet:
     *                   Comparison
     *                   <p>
     *                   eq("_id", 1); == Equals<p>
     *                   gt("_id", 1); == Greater<p>
     *                   gte("_id", 1); == Greater or equal<p>
     *                   lt("_id", 1); == Less<p>
     *                   lte("_id", 1); == Less or equal<p>
     *                   ne("_id", 1); == not Equals<p>
     *                   in("array", 1); == OBJ in array<p>
     *                   nin("array", 1); == not in Array<p>
     *                   empty("array", 1); == ALL<p>
     *                   <p>
     *                   Boolean Logic:
     *                   <p>
     *                   and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                   or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                   not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                   nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     *                   <p>
     *                   Sort options:
     *                   ascending("_id") -- ASC MYSQL<p>
     *                   descending("_id") -- DESC MYSQL<p>
     *                   orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     *                   <p>
     *                   set("_id", 1); == Set Colum - Value<p>
     *                   addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                   pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                   push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                   combine(set(),pull()) Combined update<p>
     */
    public static void Update(String collection, Bson where, Bson updates) {
        MongoCollection<Document> col = database.getCollection(collection);
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult res = col.updateOne(where, updates, options);


    }

    public static void Update(MongoDBDriver.MongoCollections collection, Bson where, Bson updates) {
        MongoCollection<Document> col = database.getCollection(collection.getName());
        UpdateOptions options = new UpdateOptions().upsert(true);
        UpdateResult res = col.updateOne(where, updates, options);


    }

    /**
     * @param collection The collection (MYSQL Table)
     * @param where      Which document should be updated (MYSQL Where statement)
     * @param updates    The actual update document (MYSQL VALUES statement)
     *                   <p>
     *                   Cheat sheet:
     *                   Comparison
     *                   <p>
     *                   eq("_id", 1); == Equals<p>
     *                   gt("_id", 1); == Greater<p>
     *                   gte("_id", 1); == Greater or equal<p>
     *                   lt("_id", 1); == Less<p>
     *                   lte("_id", 1); == Less or equal<p>
     *                   ne("_id", 1); == not Equals<p>
     *                   in("array", 1); == OBJ in array<p>
     *                   nin("array", 1); == not in Array<p>
     *                   empty("array", 1); == ALL<p>
     *                   <p>
     *                   Boolean Logic:
     *                   <p>
     *                   and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                   or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                   not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                   nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     *                   <p>
     *                   Sort options:
     *                   ascending("_id") -- ASC MYSQL<p>
     *                   descending("_id") -- DESC MYSQL<p>
     *                   orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     *                   <p>
     *                   <p>
     *                   set("_id", 1); == Set Colum - Value<p>
     *                   addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                   pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                   push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                   combine(set(),pull()) Combined update<p>
     */
    public static void UpdateMany(String collection, Bson where, Bson updates) {
        MongoCollection<Document> col = database.getCollection(collection);

        UpdateResult res = col.updateMany(where, updates);

    }

    public static void UpdateMany(MongoDBDriver.MongoCollections collection, Bson where, Bson updates) {
        MongoCollection<Document> col = database.getCollection(collection.getName());

        UpdateResult res = col.updateMany(where, updates);

    }

    /**
     * @param collection the collection
     * @param where      MYSQL Where Statement
     * @return found document, or null
     * Cheat sheet:
     * Comparison
     * <p>
     * eq("_id", 1); == Equals<p>
     * gt("_id", 1); == Greater<p>
     * gte("_id", 1); == Greater or equal<p>
     * lt("_id", 1); == Less<p>
     * lte("_id", 1); == Less or equal<p>
     * ne("_id", 1); == not Equals<p>
     * in("array", 1); == OBJ in array<p>
     * nin("array", 1); == not in Array<p>
     * empty("array", 1); == ALL<p>
     * <p>
     * Boolean Logic:
     * <p>
     * and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     * or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     * not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     * nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     * Sort options:
     * ascending("_id") -- ASC MYSQL<p>
     * descending("_id") -- DESC MYSQL<p>
     * orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     * <p>
     * set("_id", 1); == Set Colum - Value<p>
     * addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     * pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     * push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     * combine(set(),pull()) Combined update<p>
     */

    public static CompletableFuture<Document> Find(String collection, Bson where) {
        CompletableFuture<Document> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection);
        result.complete(col.find(where).first());
        return result;
    }

    public static CompletableFuture<Document> Find(MongoDBDriver.MongoCollections collection, Bson where) {
        CompletableFuture<Document> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection.getName());
        result.complete(col.find(where).first());
        return result;
    }

    /**
     * @param collection the collection
     * @param where      MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     * Comparison
     * <p>
     * eq("_id", 1); == Equals<p>
     * gt("_id", 1); == Greater<p>
     * gte("_id", 1); == Greater or equal<p>
     * lt("_id", 1); == Less<p>
     * lte("_id", 1); == Less or equal<p>
     * ne("_id", 1); == not Equals<p>
     * in("array", 1); == OBJ in array<p>
     * nin("array", 1); == not in Array<p>
     * empty("array", 1); == ALL<p>
     * <p>
     * Boolean Logic:
     * <p>
     * and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     * or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     * not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     * nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     * Sort options:
     * ascending("_id") -- ASC MYSQL<p>
     * descending("_id") -- DESC MYSQL<p>
     * orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     * <p>
     * set("_id", 1); == Set Colum - Value<p>
     * addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     * pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     * push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     * combine(set(),pull()) Combined update<p>
     */

    public static CompletableFuture<ArrayList<Document>> FindAll(String collection, Bson where) {
        CompletableFuture<ArrayList<Document>> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection);
        ArrayList<Document> retvalue = new ArrayList<>();
        try {
            FindIterable<Document> res = col.find(where).sort(Sorts.ascending("_id"));
            while (res.cursor().hasNext()) {
                retvalue.add(res.cursor().next());
            }
            result.complete(retvalue);
        } catch (Exception e) {
            e.printStackTrace();
            result.completeExceptionally(e);
        }
        return result;
    }

    public static CompletableFuture<ArrayList<Document>> FindAll(MongoDBDriver.MongoCollections collection, Bson where) {
        CompletableFuture<ArrayList<Document>> result = new CompletableFuture<>();
        MongoCollection<Document> col = database.getCollection(collection.getName());
        ArrayList<Document> retvalue = new ArrayList<>();
        try {
            FindIterable<Document> res = col.find(where).sort(Sorts.ascending("_id"));
            while (res.cursor().hasNext()) {
                retvalue.add(res.cursor().next());
            }
            result.complete(retvalue);
        } catch (Exception e) {
            e.printStackTrace();
            result.completeExceptionally(e);
        }
        return result;
    }

    /**
     * @param collection the collection
     * @param where      MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     * Comparison
     * <p>
     * eq("_id", 1); == Equals<p>
     * gt("_id", 1); == Greater<p>
     * gte("_id", 1); == Greater or equal<p>
     * lt("_id", 1); == Less<p>
     * lte("_id", 1); == Less or equal<p>
     * ne("_id", 1); == not Equals<p>
     * in("array", 1); == OBJ in array<p>
     * nin("array", 1); == not in Array<p>
     * empty("array", 1); == ALL<p>
     * <p>
     * Boolean Logic:
     * <p>
     * and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     * or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     * not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     * nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     * Sort options:
     * ascending("_id") -- ASC MYSQL<p>
     * descending("_id") -- DESC MYSQL<p>
     * orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     * <p>
     * set("_id", 1); == Set Colum - Value<p>
     * addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     * pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     * push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     * combine(set(),pull()) Combined update<p>
     */

    public static void Delete(String collection, Bson where) {
        MongoCollection<Document> col = database.getCollection(collection);
        col.deleteMany(where);
    }

    public static void Delete(MongoDBDriver.MongoCollections collection, Bson where) {
        MongoCollection<Document> col = database.getCollection(collection.getName());
        col.deleteMany(where);
    }
}
