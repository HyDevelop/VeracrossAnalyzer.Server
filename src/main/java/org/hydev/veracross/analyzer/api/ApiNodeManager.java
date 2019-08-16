package org.hydev.veracross.analyzer.api;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class manages the registration of the nodes.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 19:17
 */
public class ApiNodeManager
{
    private final ArrayList<ApiNode> registeredNodes = new ArrayList<>();

    /**
     * Register some nodes.
     *
     * @param node Nodes.
     */
    public void register(ApiNode... node)
    {
        registeredNodes.addAll(Arrays.asList(node));
    }

    /**
     * Get node by path
     *
     * @param nodePath Node path.
     * @return Registered node. Return null if not found.
     */
    public ApiNode getNode(String nodePath)
    {
        nodePath = nodePath.toLowerCase();

        for (ApiNode node : registeredNodes)
        {
            if (node.path().equalsIgnoreCase(nodePath)) return node;
        }
        return null;
    }
}
