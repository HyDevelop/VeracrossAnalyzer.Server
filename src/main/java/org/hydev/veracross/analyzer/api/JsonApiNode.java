package org.hydev.veracross.analyzer.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    protected int maxBodyLength = 2000;

    @Override
    public String process(ApiAccess access)
    {
        // Validate body
        String body = access.getContent();
        if (body == null || body.isEmpty() || body.length() > maxBodyLength)
        {
            return GSON.toJson(new GeneralReturnData(false, "Bad request"));
        }

        // Parse body
        JsonObject data = new JsonParser().parse(access.getContent()).getAsJsonObject();

        try
        {
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
