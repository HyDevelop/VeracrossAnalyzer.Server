package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.util.List;

import static java.util.stream.Collectors.toList;
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

    /**
     * Return model, used to filter irrelevant data
     */
    @Data @AllArgsConstructor
    protected static class ReturnModel
    {
        int grade;
        String classes;
    }

    // TODO: This method of caching only works for the one school, have to be modified if other schools are added.
    private static List<ReturnModel> directoryJsonCache;
    private static long cacheTime = 0;

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Check cache
        if (System.currentTimeMillis() - cacheTime > 24 * 3600000)
        {
            // Update cache
            directoryJsonCache = veracross.getDirectoryStudents().stream()
                .map(s -> new ReturnModel(s.getCurrentGradeId(), s.getAllClasses())).collect(toList());
            cacheTime = System.currentTimeMillis();
        }

        return directoryJsonCache;
    }
}
