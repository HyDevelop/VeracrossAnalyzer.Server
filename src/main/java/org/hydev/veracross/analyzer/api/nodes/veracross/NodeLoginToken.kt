package org.hydev.veracross.analyzer.api.nodes.veracross

import org.hydev.veracross.analyzer.VAConstants
import org.hydev.veracross.analyzer.api.ApiAccess
import org.hydev.veracross.analyzer.api.JsonApiConfig
import org.hydev.veracross.analyzer.api.JsonApiNode
import org.hydev.veracross.analyzer.database.model.User
import org.hydev.veracross.analyzer.database.model.system.SystemMeta
import org.hydev.veracross.analyzer.utils.CookieData
import org.hydev.veracross.sdk.VeracrossHttpClient
import java.io.IOException
import java.util.*

/**
 * Login with token
 *
 *
 * Class created by the HyDEV Team on 2019-11-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-07 19:14
 */
class NodeLoginToken : JsonApiNode<NodeLoginToken.Model?>()
{
    override fun path(): String
    {
        return "/api/login/token"
    }

    @Throws(Exception::class)
    override fun processJson(access: ApiAccess?, data: Model?): Any
    {
        // Create http client with token
        val client = VeracrossHttpClient()
        val cookie = CookieData(data!!.token).store(client)

        // Verify login
        if (!client.validateLogin())
        {
            return object
            {
                val success = false
                val user = "logout"
            }
        }

        // Update token
        cookie.csrf = client.csrfToken

        // After login
        return afterLogin(client, cookie)
    }

    companion object
    {
        /**
         * Process after login
         *
         * @param client Veracross http client
         * @param cookie Cookie data
         * @return ReturnModel data
         */
        @JvmStatic
        @Throws(IOException::class)
        fun afterLogin(client: VeracrossHttpClient, cookie: CookieData): Any
        {
            // Get user from database
            var user = User.get(cookie.username)

            // If user doesn't exist, register.
            if (user == null) user = User.register(cookie.username, client)

            // Update last login and token, and then save
            user!!.apply {
                lastLogin = Date()
                token = cookie.wrap()
            }.save()

            // Return
            return object
            {
                val user = user
            }
        }
    }

    override fun config(): JsonApiConfig
    {
        return JsonApiConfig().key("token", VAConstants.LENGTH_TOKEN)
    }

    override fun model(): Class<Model>
    {
        return Model::class.java
    }

    class Model
    {
        var token: String? = null
    }
}
