// Code generated by moshi-kotlin-codegen. Do not edit.
@file:Suppress("DEPRECATION", "unused", "ClassName", "REDUNDANT_PROJECTION",
    "RedundantExplicitType", "LocalVariableName", "RedundantVisibilityModifier",
    "PLATFORM_CLASS_MAPPED_TO_KOTLIN", "IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")

package com.example.androidschool.feature_characters.`data`.network.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.`internal`.Util
import java.lang.NullPointerException
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.emptySet
import kotlin.text.buildString

public class CharacterNetworkEntityJsonAdapter(
  moshi: Moshi
) : JsonAdapter<CharacterNetworkEntity>() {
  private val options: JsonReader.Options = JsonReader.Options.of("appearance",
      "better_call_saul_appearance", "birthday", "category", "char_id", "img", "name", "nickname",
      "occupation", "portrayed", "status")

  private val listOfIntAdapter: JsonAdapter<List<Int>> =
      moshi.adapter(Types.newParameterizedType(List::class.java, Int::class.javaObjectType),
      emptySet(), "appearance")

  private val stringAdapter: JsonAdapter<String> = moshi.adapter(String::class.java, emptySet(),
      "birthday")

  private val intAdapter: JsonAdapter<Int> = moshi.adapter(Int::class.java, emptySet(), "charId")

  private val listOfStringAdapter: JsonAdapter<List<String>> =
      moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java), emptySet(),
      "occupation")

  public override fun toString(): String = buildString(44) {
      append("GeneratedJsonAdapter(").append("CharacterNetworkEntity").append(')') }

  public override fun fromJson(reader: JsonReader): CharacterNetworkEntity {
    var appearance: List<Int>? = null
    var betterCallSaulAppearance: List<Int>? = null
    var birthday: String? = null
    var category: String? = null
    var charId: Int? = null
    var img: String? = null
    var name: String? = null
    var nickname: String? = null
    var occupation: List<String>? = null
    var portrayed: String? = null
    var status: String? = null
    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.selectName(options)) {
        0 -> appearance = listOfIntAdapter.fromJson(reader) ?:
            throw Util.unexpectedNull("appearance", "appearance", reader)
        1 -> betterCallSaulAppearance = listOfIntAdapter.fromJson(reader) ?:
            throw Util.unexpectedNull("betterCallSaulAppearance", "better_call_saul_appearance",
            reader)
        2 -> birthday = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("birthday",
            "birthday", reader)
        3 -> category = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("category",
            "category", reader)
        4 -> charId = intAdapter.fromJson(reader) ?: throw Util.unexpectedNull("charId", "char_id",
            reader)
        5 -> img = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("img", "img", reader)
        6 -> name = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("name", "name",
            reader)
        7 -> nickname = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("nickname",
            "nickname", reader)
        8 -> occupation = listOfStringAdapter.fromJson(reader) ?:
            throw Util.unexpectedNull("occupation", "occupation", reader)
        9 -> portrayed = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("portrayed",
            "portrayed", reader)
        10 -> status = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("status",
            "status", reader)
        -1 -> {
          // Unknown name, skip it.
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    reader.endObject()
    return CharacterNetworkEntity(
        appearance = appearance ?: throw Util.missingProperty("appearance", "appearance", reader),
        betterCallSaulAppearance = betterCallSaulAppearance ?:
            throw Util.missingProperty("betterCallSaulAppearance", "better_call_saul_appearance",
            reader),
        birthday = birthday ?: throw Util.missingProperty("birthday", "birthday", reader),
        category = category ?: throw Util.missingProperty("category", "category", reader),
        charId = charId ?: throw Util.missingProperty("charId", "char_id", reader),
        img = img ?: throw Util.missingProperty("img", "img", reader),
        name = name ?: throw Util.missingProperty("name", "name", reader),
        nickname = nickname ?: throw Util.missingProperty("nickname", "nickname", reader),
        occupation = occupation ?: throw Util.missingProperty("occupation", "occupation", reader),
        portrayed = portrayed ?: throw Util.missingProperty("portrayed", "portrayed", reader),
        status = status ?: throw Util.missingProperty("status", "status", reader)
    )
  }

  public override fun toJson(writer: JsonWriter, value_: CharacterNetworkEntity?): Unit {
    if (value_ == null) {
      throw NullPointerException("value_ was null! Wrap in .nullSafe() to write nullable values.")
    }
    writer.beginObject()
    writer.name("appearance")
    listOfIntAdapter.toJson(writer, value_.appearance)
    writer.name("better_call_saul_appearance")
    listOfIntAdapter.toJson(writer, value_.betterCallSaulAppearance)
    writer.name("birthday")
    stringAdapter.toJson(writer, value_.birthday)
    writer.name("category")
    stringAdapter.toJson(writer, value_.category)
    writer.name("char_id")
    intAdapter.toJson(writer, value_.charId)
    writer.name("img")
    stringAdapter.toJson(writer, value_.img)
    writer.name("name")
    stringAdapter.toJson(writer, value_.name)
    writer.name("nickname")
    stringAdapter.toJson(writer, value_.nickname)
    writer.name("occupation")
    listOfStringAdapter.toJson(writer, value_.occupation)
    writer.name("portrayed")
    stringAdapter.toJson(writer, value_.portrayed)
    writer.name("status")
    stringAdapter.toJson(writer, value_.status)
    writer.endObject()
  }
}
