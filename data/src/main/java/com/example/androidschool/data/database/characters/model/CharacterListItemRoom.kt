package com.example.androidschool.data.database.characters.model

import androidx.room.*
import com.example.androidschool.domain.search.model.ListItem

@Entity(tableName = "characters")
data class CharacterListItemRoom(
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
    val status: String,
    @ColumnInfo(name = "character_offset")
    val offset: Int
) {
    fun toDomainModel(): ListItem.CharacterListItem {
        return ListItem.CharacterListItem(
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

fun List<CharacterListItemRoom>.toDomainList(): List<ListItem.CharacterListItem> {
    return this.map { it.toDomainModel() }
}