package com.example.androidschool.data.network.characters.model


import com.example.androidschool.domain.characters.model.CharacterEntity
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
)

fun CharacterNetworkEntity.toDomainModel(): CharacterEntity {
    return CharacterEntity(
        appearance = this.appearance,
        betterCallSaulAppearance = this.betterCallSaulAppearance,
        birthday = this.birthday,
        category = this.category,
        charId = this.charId,
        img = this.img,
        name = this.name,
        nickname = this.nickname,
        occupation = this.occupation,
        portrayed = this.portrayed,
        status = this.status
    )
}