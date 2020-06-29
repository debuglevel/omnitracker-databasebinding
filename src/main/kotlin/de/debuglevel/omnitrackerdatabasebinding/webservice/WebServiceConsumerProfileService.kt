package de.debuglevel.omnitrackerdatabasebinding.webservice

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceConsumerProfileService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    private val query = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[alias]\n" +
            "      ,[user_]\n" +
            "      ,[profile_version]\n" +
            "      ,[last_change]\n" +
            "      ,[ws_endpoint]\n" +
            "      ,[ws_username]\n" +
            "      ,[ws_password]\n" +
            "      ,[gw_address]\n" +
            "      ,[gen_proxy_mode]\n" +
            "      ,[gen_proxy_options]\n" +
            "      ,[type]\n" +
            "      ,[ws_certificate]\n" +
            "      ,[no_wscred_toclient]\n" +
            "  FROM [IbWscProfiles]"

    fun getAll(): Map<Int, WebServiceConsumerProfile> {
        logger.debug { "Getting getAllWebServiceConsumerProfiles..." }

        val webServiceConsumerProfiles = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(query)

            val webServiceConsumerProfiles = hashMapOf<Int, WebServiceConsumerProfile>()

            while (resultSet.next()) {
                val webServiceConsumerProfile = build(resultSet)
                webServiceConsumerProfiles[webServiceConsumerProfile.id] = webServiceConsumerProfile
            }

            webServiceConsumerProfiles
        }

        logger.debug { "Got ${webServiceConsumerProfiles.size} WebServiceConsumerProfiles" }
        return webServiceConsumerProfiles
    }

    fun get(id: Int): WebServiceConsumerProfile? {
        logger.debug { "Getting WebServiceConsumerProfile id=$id..." }
        val webServiceConsumerProfile = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$query WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> build(resultSet)
                else -> null
            }
        }

        logger.debug { "Got WebServiceConsumerProfile: $webServiceConsumerProfile" }
        return webServiceConsumerProfile
    }

    private fun build(resultSet: ResultSet): WebServiceConsumerProfile {
        logger.debug { "Building WebServiceConsumerProfile for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val endpointUrl = resultSet.getString("ws_endpoint")

        val webServiceConsumerProfile = WebServiceConsumerProfile(
            id,
            name,
            alias,
            profileVersion,
            endpointUrl
        )

        logger.debug { "Built WebServiceConsumerProfile: $webServiceConsumerProfile" }
        return webServiceConsumerProfile
    }
}