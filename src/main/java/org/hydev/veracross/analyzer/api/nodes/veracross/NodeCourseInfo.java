package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.database.model.CourseInfo;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

/**
 * Returns all courses for course selection
 * <p>
 * Class created by the HyDEV Team on 2020-04-11!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-11 17:44
 */
public class NodeCourseInfo extends JsonApiNode<NodeCourseInfo.Model>
{
    @Override
    public String path()
    {
        return "/api/course-info";
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

    @Data
    @AllArgsConstructor
    protected static class StudentInfo
    {
        int gradeLevel;
        String classes;
    }

    @Data
    @AllArgsConstructor
    protected static class ReturnModel
    {
        List<CourseInfo> courseInfos;
        List<StudentInfo> studentInfos;
    }

    // TODO: This method of caching only works for the one school, have to be modified if other schools are added.
    private static List<StudentInfo> directoryJsonCache;
    private static long cacheTime = 0;

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Validate login
        if (!veracross.validateLogin()) throw new JsonKnownError("Login expired!");

        // Check cache
        if (System.currentTimeMillis() - cacheTime > 24 * 3600000)
        {
            // Update cache
            directoryJsonCache = veracross.getDirectoryStudents().stream()
                .map(s -> new StudentInfo(s.getCurrentGradeId(), s.getAllClasses())).collect(toList());
            cacheTime = System.currentTimeMillis();
        }

        return new ReturnModel(CourseInfo.getAll(), directoryJsonCache);
    }
}
