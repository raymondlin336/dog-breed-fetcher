package dogapi;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private Map<String, List<String>> cache = new HashMap<>();
    private BreedFetcher fetcher;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }
        else {
            try {
                List<String> list = fetcher.getSubBreeds(breed);
                callsMade++;
                cache.put(breed, list);
                return list;
            }
            catch (BreedFetcher.BreedNotFoundException e) {
                callsMade++;
                throw new BreedFetcher.BreedNotFoundException(breed);
            }
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}