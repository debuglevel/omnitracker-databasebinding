package de.debuglevel.omnitrackerdatabasebinding.webservice

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceConsumerCallProfileService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService,
    private val webServiceConsumerProfileService: WebServiceConsumerProfileService
) : EntityService<WebServiceConsumerCallProfile>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "WebService Consumer Call Profile"
    override val query = "SELECT [id]\n" +
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

    override fun build(resultSet: ResultSet): WebServiceConsumerCallProfile {
        logger.debug { "Building WebServiceConsumerCallProfile for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val profileStatus = resultSet.getInt("profile_status")
        val folderId = resultSet.getInt("folder_id_ot")
        val webServiceConsumerProfileId = resultSet.getInt("wsc_profile_id")

        val folder = folderService.get(folderId)
        val webServiceConsumerProfile = webServiceConsumerProfileService.get(webServiceConsumerProfileId)

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
}