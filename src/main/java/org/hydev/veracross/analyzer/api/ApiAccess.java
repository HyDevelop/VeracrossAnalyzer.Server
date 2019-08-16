package org.hydev.veracross.analyzer.api;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This POJO class contains the information for one api access.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 19:13
 */
@Data
public class ApiAccess
{
    private final HttpServletRequest request;
    private final Map<String, String> headers;
    private final String content;
}
