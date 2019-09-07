package org.hydev.veracross.analyzer.api;

import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;

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
public abstract class JsonApiNode<S, R extends JsonApiNode.GeneralReturnData> implements ApiNode
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
        S data = GSON.fromJson(access.getContent(), new TypeToken<S>(){}.getType());

        // Process
        return GSON.toJson(processJson(access, data));
    }

    /**
     * Process the submitted object and return an object
     *
     * @param access Api access info
     * @param data Submitted json data
     * @return Object to return as json
     */
    protected abstract R processJson(ApiAccess access, S data);

    /**
     * Return data
     */
    @Data @AllArgsConstructor
    public static class GeneralReturnData
    {
        protected boolean success;
        protected String message;
    }
}
