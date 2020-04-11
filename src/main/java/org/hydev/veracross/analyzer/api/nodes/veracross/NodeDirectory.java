package org.hydev.veracross.analyzer.api.nodes.veracross;

import com.google.gson.Gson;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-04-11!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-11 13:45
 */
public class NodeDirectory extends JsonApiNode<NodeDirectory.Model>
{
    @Override
    public String path()
    {
        return "/api/directory";
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class).key("token", LENGTH_TOKEN);
    }

    protected static class Model
    {
        String token;
    }

    // TODO: This method of caching only works for the one school, have to be modified if other schools are added.
    private static String directoryJsonCache;
    private static long cacheTime = 0;

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Check cache
        if (directoryJsonCache == null || System.currentTimeMillis() - cacheTime > 24 * 3600000)
        {
            // Update cache
            directoryJsonCache = new Gson().toJson(veracross.getDirectoryStudents());
            cacheTime = System.currentTimeMillis();
        }

        return directoryJsonCache;
    }
}
