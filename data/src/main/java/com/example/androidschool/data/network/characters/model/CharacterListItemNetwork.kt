package com.example.androidschool.data.network.characters.model


import com.example.androidschool.domain.characters.model.CharacterDetails
import com.example.androidschool.domain.characters.model.CharacterInEpisode
import com.example.androidschool.domain.characters.model.CharacterListItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterNetworkEntity(
    @Json(name = "appearance")
    val appearance: List<Int>,
    @Json(name = "better_call_saul_appearance")
    val betterCallSaulAppearance: List<Int>,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "char_id")
    val charId: Int,
    @Json(name = "img")
    val img: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "occupation")
    val occupation: List<String>,
    @Json(name = "portrayed")
    val portrayed: String,
    @Json(name = "status")
    val status: String
) {
    fun toDomainModel(): CharacterListItem {
        return CharacterListItem(
            appearance = appearance,
            betterCallSaulAppearance = betterCallSaulAppearance,
            birthday = birthday,
            category = category,
            charId = charId,
            img = img,
            name = name,
            nickname = nickname,
            occupation = occupation,
            portrayed = portrayed,
            status = status
        )
    }

    fun toDomainDetailsModel(): CharacterDetails {
        return CharacterDetails(
            appearance = appearance,
            betterCallSaulAppearance = betterCallSaulAppearance,
            birthday = birthday,
            category = category,
            charId = charId,
            img = img,
            name = name,
            nickname = nickname,
            occupation = occupation,
            portrayed = portrayed,
            status = status
        )
    }

    fun toDomainInEpisodeModel(): CharacterInEpisode {
        return CharacterInEpisode(
            appearance = appearance,
            betterCallSaulAppearance = betterCallSaulAppearance,
            birthday = birthday,
            category = category,
            charId = charId,
            img = img,
            name = name,
            nickname = nickname,
            occupation = occupation,
            portrayed = portrayed,
            status = status
        )
    }
}