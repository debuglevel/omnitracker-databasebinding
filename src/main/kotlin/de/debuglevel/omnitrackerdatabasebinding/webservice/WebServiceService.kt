package de.debuglevel.omnitrackerdatabasebinding.webservice

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) {
    private val logger = KotlinLogging.logger {}

    private val webServiceConsumerCallProfilesQuery = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[alias]\n" +
            "      ,[user_]\n" +
            "      ,[profile_version]\n" +
            "      ,[profile_status]\n" +
            "      ,[folder_id_ot]\n" +
            "      ,[wsc_profile_id]\n" +
            "      ,[wsc_operation]\n" +
            "      ,[log_errors]\n" +
            "      ,[logfile_prefix]\n" +
            "      ,[logpath]\n" +
            "      ,[binding_settings]\n" +
            "      ,[in_param_script]\n" +
            "      ,[out_param_script]\n" +
            "      ,[last_change]\n" +
            "      ,[call_async]\n" +
            "  FROM [IbWscCallProfiles]"

    private val webServiceConsumerProfilesQuery = "SELECT [id]\n" +
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

    fun getAllWebServiceConsumerCallProfiles(): Map<Int, WebServiceConsumerCallProfile> {
        logger.debug { "Getting WebServiceConsumerCallProfiles..." }

        val webServiceConsumerCallProfiles = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(webServiceConsumerCallProfilesQuery)

            val webServiceConsumerCallProfiles = hashMapOf<Int, WebServiceConsumerCallProfile>()

            while (resultSet.next()) {
                val webServiceConsumerProfile = buildWebServiceConsumerCallProfile(resultSet)
                webServiceConsumerCallProfiles[webServiceConsumerProfile.id] = webServiceConsumerProfile
            }

            webServiceConsumerCallProfiles
        }

        logger.debug { "Got ${webServiceConsumerCallProfiles.size} WebServiceConsumerCallProfiles" }
        return webServiceConsumerCallProfiles
    }

    fun getAllWebServiceConsumerProfiles(): Map<Int, WebServiceConsumerProfile> {
        logger.debug { "Getting getAllWebServiceConsumerProfiles..." }

        val webServiceConsumerProfiles = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery(webServiceConsumerProfilesQuery)

            val webServiceConsumerProfiles = hashMapOf<Int, WebServiceConsumerProfile>()

            while (resultSet.next()) {
                val webServiceConsumerProfile = buildWebServiceConsumerProfile(resultSet)
                webServiceConsumerProfiles[webServiceConsumerProfile.id] = webServiceConsumerProfile
            }

            webServiceConsumerProfiles
        }

        logger.debug { "Got ${webServiceConsumerProfiles.size} WebServiceConsumerProfiles" }
        return webServiceConsumerProfiles
    }

    fun getWebServiceConsumerCallProfile(id: Int): WebServiceConsumerCallProfile? {
        logger.debug { "Getting WebServiceConsumerCallProfile id=$id..." }
        val webServiceConsumerCallProfile = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$webServiceConsumerCallProfilesQuery WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> buildWebServiceConsumerCallProfile(resultSet)
                else -> null
            }
        }

        logger.debug { "Got WebServiceConsumerCallProfile: $webServiceConsumerCallProfile" }
        return webServiceConsumerCallProfile
    }

    fun getWebServiceConsumerProfile(id: Int): WebServiceConsumerProfile? {
        logger.debug { "Getting WebServiceConsumerProfile id=$id..." }
        val webServiceConsumerProfile = databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet = sqlStatement.executeQuery("$webServiceConsumerProfilesQuery WHERE id=$id")

            val available = resultSet.next()
            when {
                available -> buildWebServiceConsumerProfile(resultSet)
                else -> null
            }
        }

        logger.debug { "Got WebServiceConsumerProfile: $webServiceConsumerProfile" }
        return webServiceConsumerProfile
    }

    private fun buildWebServiceConsumerCallProfile(resultSet: ResultSet): WebServiceConsumerCallProfile {
        logger.debug { "Building WebServiceConsumerCallProfile for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val profileStatus = resultSet.getInt("profile_status")
        val folderId = resultSet.getInt("folder_id_ot")
        val webServiceConsumerProfileId = resultSet.getInt("wsc_profile_id")

        val folder = folderService.get(folderId)
        val webServiceConsumerProfile = getWebServiceConsumerProfile(webServiceConsumerProfileId)

        val webServiceConsumerCallProfile = WebServiceConsumerCallProfile(
            id,
            name,
            alias,
            profileVersion,
            profileStatus,
            folderId,
            webServiceConsumerProfileId,
            folder,
            webServiceConsumerProfile
            //lazy { folderService.fetchFolders() },
            //lazy { fetchWebServiceConsumerProfiles() }
        )

        logger.debug { "Built WebServiceConsumerCallProfile: $webServiceConsumerCallProfile" }
        return webServiceConsumerCallProfile
    }

    private fun buildWebServiceConsumerProfile(resultSet: ResultSet): WebServiceConsumerProfile {
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