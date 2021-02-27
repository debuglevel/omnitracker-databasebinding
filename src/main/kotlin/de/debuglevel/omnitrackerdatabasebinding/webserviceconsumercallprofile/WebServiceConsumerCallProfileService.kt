package de.debuglevel.omnitrackerdatabasebinding.webserviceconsumercallprofile

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.ColumnType
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import de.debuglevel.omnitrackerdatabasebinding.webserviceconsumerprofile.WebServiceConsumerProfileService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceConsumerCallProfileService(
    databaseService: DatabaseService,
    private val folderService: FolderService,
    private val webServiceConsumerProfileService: WebServiceConsumerProfileService
) : EntityService<WebServiceConsumerCallProfile>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "WebService Consumer Call Profile"
    override val table = "IbWscCallProfiles"
    override val columns = mapOf(
        "name" to ColumnType.String,
        "alias" to ColumnType.String,
        "user_" to ColumnType.Integer,
        "profile_version" to ColumnType.Integer,
        "profile_status" to ColumnType.Integer,
        "folder_id_ot" to ColumnType.Integer,
        "wsc_profile_id" to ColumnType.Integer,
        "wsc_operation" to ColumnType.Integer,
        "log_errors" to ColumnType.Boolean,
        "logfile_prefix" to ColumnType.String,
        "logpath" to ColumnType.String,
        "binding_settings" to ColumnType.Integer,
        "binding_settings" to ColumnType.Integer,
        "in_param_script" to ColumnType.Integer,
        "out_param_script" to ColumnType.Integer,
        "last_change" to ColumnType.Integer,
        "call_async" to ColumnType.Boolean
    )

    override fun build(resultSet: ResultSet): WebServiceConsumerCallProfile {
        logger.trace { "Building WebServiceConsumerCallProfile for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val alias = resultSet.getString("alias")
        val profileVersion = resultSet.getInt("profile_version")
        val profileStatus = resultSet.getInt("profile_status")
        val folderId = resultSet.getInt("folder_id_ot")
        val webServiceConsumerProfileId = resultSet.getInt("wsc_profile_id")
        val isCallAsynchronous = resultSet.getBoolean("call_async")
        val logPath = resultSet.getString("logpath")
        val logFilePrefix = resultSet.getString("logfile_prefix")
        val isLoggingErrors = resultSet.getBoolean("log_errors")

        val folder = folderService.get(folderId)
        val webServiceConsumerProfile = webServiceConsumerProfileService.get(webServiceConsumerProfileId)

        val webServiceConsumerCallProfile =
            WebServiceConsumerCallProfile(
                id,
                name,
                alias,
                profileVersion,
                profileStatus,
                folderId,
                webServiceConsumerProfileId,
                folder,
                webServiceConsumerProfile,
                isCallAsynchronous,
                logPath,
                logFilePrefix,
                isLoggingErrors

                //lazy { folderService.fetchFolders() },
                //lazy { fetchWebServiceConsumerProfiles() }
            )

        logger.trace { "Built WebServiceConsumerCallProfile: $webServiceConsumerCallProfile" }
        return webServiceConsumerCallProfile
    }
}