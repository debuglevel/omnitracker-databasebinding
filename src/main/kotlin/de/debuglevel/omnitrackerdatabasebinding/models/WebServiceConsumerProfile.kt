package de.debuglevel.omnitrackerdatabasebinding.models

data class WebServiceConsumerProfile(
    val id: Int,
    val name: String,
    val alias: String,
    val profileVersion: Int,
    val endpointUrl: String
) {
    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebServiceConsumerProfile

        if (id != other.id) return false
        if (name != other.name) return false
        if (alias != other.alias) return false
        if (profileVersion != other.profileVersion) return false
        if (endpointUrl != other.endpointUrl) return false

        return true
    }
}