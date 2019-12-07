package org.hydev.veracross.analyzer.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hydev.veracross.sdk.exceptions.VeracrossException;

import java.util.Map.Entry;

import static java.lang.System.err;
import static org.hydev.veracross.analyzer.VAConstants.GSON;

/**
 * This class is a type of api node dealing with json.
 * <p>
 * Class created by the HyDEV Team on 2019-09-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-07 10:32
 */
public abstract class JsonApiNode<T> implements ApiNode
{
    private final JsonApiConfig<T> config = initConfig();

    @Override
    public String process(ApiAccess access)
    {
        try
        {
            // Validate body
            String body = access.getContent();
            if (body == null || body.isEmpty() || body.length() > config.maxBodyLength())
                throw new JsonKnownError("Bad request");

            // Parse body
            JsonObject data = new JsonParser().parse(access.getContent()).getAsJsonObject();

            // Check key length
            for (Entry<String, Integer> entry : config.keyLengths().entrySet())
            {
                // Check if key exists
                if (!data.has(entry.getKey())) throw new JsonKnownError("Missing keys");

                // Check key length
                if (data.get(entry.getKey()).getAsString().length() > entry.getValue())
                    throw new JsonKnownError("Bad request");
            }

            // Process
            Object result = processJson(access, GSON.fromJson(data, config.model()));

            // Null case
            if (result == null || result == "") return "";

            // Directly return strings
            if (result instanceof String) return (String) result;

            // Return
            return GSON.toJson(new GeneralReturnData(true, result));
        }
        catch (Exception e)
        {
            // Log errors if it is not known
            if (!(e instanceof JsonKnownError
                    || e instanceof VeracrossException
                    || e.getMessage().equals("JsonNull")
                    || e instanceof JsonSyntaxException))
            {
                err.println("Error occurred when processing " + path());
                err.println("- With headers: " + access.getHeaders());

                if (!config.secret())
                {
                    err.println("\n - With json: " + access.getContent());
                }

                e.printStackTrace();
            }

            return GSON.toJson(new GeneralReturnData(false, "Error: " + e.getMessage()));
        }
    }

    /**
     * Process the submitted object and return an object
     *
     * @param access Api access info
     * @param data Submitted json data
     * @return Object to return as json
     */
    protected abstract Object processJson(ApiAccess access, T data) throws Exception;

    /**
     * Define the config
     *
     * @return Config
     */
    protected abstract JsonApiConfig<T> config();

    /**
     * Init config
     *
     * @return Config
     */
    private JsonApiConfig<T> initConfig()
    {
        // Get config from sub class
        JsonApiConfig<T> config = config();

        // Check null case
        if (config == null) throw new NullPointerException("Config can't be null");

        // Check auto length
        if (config.maxBodyLength() == -1)
        {
            int length = 0;
            for (int value : config.keyLengths().values()) length += value + 5;
            config.maxBodyLength(length);
        }

        // Return
        return config;
    }

    /**
     * Return data
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GeneralReturnData
    {
        protected boolean success;
        protected Object data;
    }
}
