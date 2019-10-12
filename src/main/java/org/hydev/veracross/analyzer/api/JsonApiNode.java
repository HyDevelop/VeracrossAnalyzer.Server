package org.hydev.veracross.analyzer.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Map.Entry;

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
public abstract class JsonApiNode implements ApiNode
{
    private final JsonApiConfig config = config();

    @Override
    public String process(ApiAccess access)
    {
        // Validate body
        String body = access.getContent();
        if (body == null || body.isEmpty() || body.length() > config.getMaxBodyLength())
        {
            return GSON.toJson(new GeneralReturnData(false, "Bad request"));
        }

        // Parse body
        JsonObject data = new JsonParser().parse(access.getContent()).getAsJsonObject();

        try
        {
            // Check key length
            for (Entry<String, Integer> entry : config.getKeyLengths().entrySet())
            {
                // Check if key exists
                if (!data.has(entry.getKey())) throw new Exception("Missing keys");

                // Check key length
                if (data.get(entry.getKey()).getAsString().length() > entry.getValue())
                    throw new Exception("Bad request");
            }

            // Process
            Object result = processJson(access, data);

            // Null case
            if (result == null || result == "") return "";

            // Return
            return GSON.toJson(new GeneralReturnData(true, result));
        }
        catch (Exception e)
        {
            e.printStackTrace();

            // TODO: Log errors
            return GSON.toJson(new GeneralReturnData(false, e.getMessage()));
        }
    }

    /**
     * Process the submitted object and return an object
     *
     * @param access Api access info
     * @param data Submitted json data
     * @return Object to return as json
     */
    protected abstract Object processJson(ApiAccess access, JsonObject data) throws Exception;

    /**
     * Define the config
     *
     * @return Config
     */
    protected abstract JsonApiConfig config();

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
