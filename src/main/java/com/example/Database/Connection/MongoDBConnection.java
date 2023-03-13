package com.example.Database.Connection;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterType;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {
    private MongoClient mongoClient;
    private final String dbName;
    private final String collectionName;

    public MongoDBConnection(String dbName, String collectionName) {
        this.dbName = dbName;
        this.collectionName = collectionName;

        try {
            this.mongoClient = MongoClients.create(getClientSettings());
            System.out.println("Connected to MongoDB server");
        } catch (MongoSocketException e) {
            System.err.println("Failed to connect to MongoDB server: " + e.getMessage());
            throw e;
        }
    }

    public MongoCollection<Document> getCollection() throws MongoException {
        this.mongoClient = MongoClients.create(getClientSettings());
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }
    public void close() {
        this.mongoClient.close();
    }

    private MongoClientSettings getClientSettings() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost/");
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .codecRegistry(pojoCodecRegistry)
                .applyToClusterSettings(builder -> builder.hosts(List.of(new ServerAddress("localhost"))))
                .build();
    }
}
