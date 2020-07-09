package de.debuglevel.omnitrackerdatabasebinding.webserviceconsumerprofile

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import mu.KotlinLogging
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class WebServiceConsumerProfileService(
    databaseService: DatabaseService
) : EntityService<WebServiceConsumerProfile>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "WebService Consumer Profile"
    override val query = "SELECT [id]\n" +
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

    override fun build(resultSet: ResultSet): WebServiceConsumerProfile {
        logger.debug { "Building WebServiceConsumerProfile for ResultSet $resultSet..." }

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

        logger.debug { "Built WebServiceConsumerProfile: $webServiceConsumerProfile" }
        return webServiceConsumerProfile
    }
}