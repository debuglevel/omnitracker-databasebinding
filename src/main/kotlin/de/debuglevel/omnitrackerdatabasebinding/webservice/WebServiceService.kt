package de.debuglevel.omnitrackerdatabasebinding.webservice

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.DriverManager
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceService(
    private val databaseService: DatabaseService
    //private val folderService: FolderService
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

    fun fetchWebServiceConsumerCallProfiles(): Map<Int, WebServiceConsumerCallProfile> {
        databaseService.getConnection().use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery(
                    webServiceConsumerCallProfilesQuery
                )

            val webServiceConsumerCallProfiles = hashMapOf<Int, WebServiceConsumerCallProfile>()

            while (resultSet.next()) {
                val webServiceConsumerProfile =
                    buildWebServiceConsumerCallProfile(resultSet)

                webServiceConsumerCallProfiles[webServiceConsumerProfile.id] = webServiceConsumerProfile
            }

            return webServiceConsumerCallProfiles
        }
    }

    private fun buildWebServiceConsumerCallProfile(resultSet: ResultSet): WebServiceConsumerCallProfile {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val profileStatus = resultSet.getInt("profile_status")
        val folderId = resultSet.getInt("folder_id_ot")
        val webServiceConsumerProfileId = resultSet.getInt("wsc_profile_id")

        val webServiceConsumerProfile =
            WebServiceConsumerCallProfile(
                id,
                name,
                alias,
                profileVersion,
                profileStatus,
                folderId,
                webServiceConsumerProfileId
                //lazy { folderService.fetchFolders() },
                //lazy { fetchWebServiceConsumerProfiles() }
            )
        return webServiceConsumerProfile
    }

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

    fun fetchWebServiceConsumerProfiles(): Map<Int, WebServiceConsumerProfile> {
        DriverManager.getConnection(databaseService.connectionstring).use { connection ->
            val sqlStatement = connection.createStatement()
            val resultSet =
                sqlStatement.executeQuery(
                    webServiceConsumerProfilesQuery
                )

            val webServiceConsumerProfiles = hashMapOf<Int, WebServiceConsumerProfile>()

            while (resultSet.next()) {
                val webServiceConsumerProfile =
                    buildWebServiceConsumerProfile(resultSet)
                webServiceConsumerProfiles[webServiceConsumerProfile.id] = webServiceConsumerProfile
            }

            return webServiceConsumerProfiles
        }
    }

    private fun buildWebServiceConsumerProfile(resultSet: ResultSet): WebServiceConsumerProfile {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val endpointUrl = resultSet.getString("ws_endpoint")

        val webServiceConsumerProfile =
            WebServiceConsumerProfile(
                id,
                name,
                alias,
                profileVersion,
                endpointUrl
            )
        return webServiceConsumerProfile
    }
}