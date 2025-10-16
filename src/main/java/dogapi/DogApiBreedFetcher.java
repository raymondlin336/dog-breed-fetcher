package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */


    static JSONObject run(String url) throws IOException {
        final OkHttpClient client = new OkHttpClient();
         Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        }
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        try{
            JSONObject response = DogApiBreedFetcher.run("https://dog.ceo/api/breed/hound/list");
            JSONArray breeds = response.getJSONArray("message");
            ArrayList b = new ArrayList<>();
            for (int i = 0; i < breeds.length(); i++) {
                b.add(breeds.getString(i));
            }
            return b;
        }
        catch(IOException e) {
            throw new BreedNotFoundException("");
        }
    }
}