package de.debuglevel.omnitrackerdatabasebinding.webserviceconsumerprofile

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.ColumnType
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
    override val table = "IbWscProfiles"
    override val columns = mapOf(
        "id" to ColumnType.Integer,
        "name" to ColumnType.String,
        "alias" to ColumnType.String,
        "user_" to ColumnType.Integer,
        "profile_version" to ColumnType.Integer,
        "last_change" to ColumnType.Integer,
        "ws_endpoint" to ColumnType.String,
        "ws_username" to ColumnType.String,
        "ws_password" to ColumnType.String,
        "gw_address" to ColumnType.String,
        "gen_proxy_mode" to ColumnType.Integer,
        "gen_proxy_options" to ColumnType.Integer,
        "type" to ColumnType.Integer,
        "ws_certificate" to ColumnType.String,
        "no_wscred_toclient" to ColumnType.Integer
    )

    override fun build(resultSet: ResultSet): WebServiceConsumerProfile {
        logger.trace { "Building WebServiceConsumerProfile for ResultSet $resultSet..." }

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

        logger.trace { "Built WebServiceConsumerProfile: $webServiceConsumerProfile" }
        return webServiceConsumerProfile
    }
}