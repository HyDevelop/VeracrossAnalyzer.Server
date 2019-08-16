package org.hydev.veracross.analyzer.api.nodes;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;

/**
 * Testing node to see if this api works.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 20:14
 */
public class NodeTest implements ApiNode
{
    @Override
    public String path()
    {
        // 请求路径
        return "/test";
    }

    @Override
    public String process(ApiAccess access)
    {
        // 请求上传的内容
        String content = access.getContent();

        // 处理
        //...

        // 回复到请求的内容
        return "Nice job! You get a test page. 中文";
    }
}
