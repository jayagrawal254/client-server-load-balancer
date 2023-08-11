import java.net.*;
import java.io.*;
import java.util.*;
class KeyValuePair<K, V> {
    K key;
    V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

class HashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private LinkedList<KeyValuePair<K, V>>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMap() {
        buckets = new LinkedList[INITIAL_CAPACITY];
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
    }

    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % buckets.length;
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];

        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new KeyValuePair<>(key, value));
        size++;

        if ((double) size / buckets.length > 0.75) {
            resize();
        }
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];

        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    public V remove(K key) {
        int index = getBucketIndex(key);
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];

        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return entry.value; // Return the removed value
            }
        }

        return null; // Return null if key was not found
    }

    private void resize() {
        LinkedList<KeyValuePair<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedList[buckets.length * 2];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;

        for (LinkedList<KeyValuePair<K, V>> oldBucket : oldBuckets) {
            for (KeyValuePair<K, V> entry : oldBucket) {
                put(entry.key, entry.value);
            }
        }
    }

    public int size() {
        return size;
    }
}

public class Server {
    private static final int PORT = 12345;
    private static HashMap<String, String> dataStore = new HashMap<>(); // Store data in-memory

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create input and output streams for the client socket
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String requestType;
                while ( (requestType = in.readLine()) != null && !requestType.equals("QUIT")) {
                    // Read the request type from the client (GET, PUT, REMOVE)
                    System.out.println("Received request: " + requestType);
                    // Process the request based on the type
                    String response;
                    if ( "GET".equals(requestType) ) {
                        String key = in.readLine();
                        response = dataStore.getOrDefault(key , "Key not found");
                    } else if ( "PUT".equals(requestType) ) {
                        String key = in.readLine();
                        String value = in.readLine();
                        dataStore.put(key , value);
                        response = "PUT operation successful";
                    } else if ( "REMOVE".equals(requestType) ) {
                        String key = in.readLine();
                        if ( dataStore.remove(key) != null ) {
                            response = "REMOVE operation successful";
                        } else {
                            response = "Key not found for REMOVE operation";
                        }
                    } else {
                        response = "Invalid request type";
                    }
                    // Send the response back to the client
                    out.println(response);
                }
                // Close the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
