package com.example.androidschool.data.database.characters.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidschool.domain.characters.model.CharacterDetails

@Entity(tableName = "characters_details")
data class CharacterDetailsRoom(
    @PrimaryKey
    @ColumnInfo(name = "charId")
    val charId: Int,
    @ColumnInfo(name = "appearance")
    val appearance: List<Int>,
    @ColumnInfo(name = "betterCallSaulAppearance")
    val betterCallSaulAppearance: List<Int>,
    @ColumnInfo(name = "birthday")
    val birthday: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "img")
    val img: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @ColumnInfo(name = "occupation")
    val occupation: List<String>,
    @ColumnInfo(name = "portrayed")
    val portrayed: String,
    @ColumnInfo(name = "status")
    val status: String
) {

    fun toDomainModel(): CharacterDetails {
        return CharacterDetails(
            charId = charId,
            appearance = appearance,
            betterCallSaulAppearance = betterCallSaulAppearance,
            birthday = birthday,
            category = category,
            img = img,
            name = name,
            nickname = nickname,
            occupation = occupation,
            portrayed = portrayed,
            status = status
        )
    }
}