// AUTO-GENERATED FILE. DO NOT MODIFY.
//
// This class was automatically generated by Apollo GraphQL plugin from the GraphQL queries it found.
// It should not be modified by hand.
//
package com.example.fragments_with_type_condition.adapter

import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.internal.NullableResponseAdapter
import com.apollographql.apollo.api.internal.ResponseAdapter
import com.apollographql.apollo.api.internal.doubleResponseAdapter
import com.apollographql.apollo.api.internal.json.JsonReader
import com.apollographql.apollo.api.internal.json.JsonWriter
import com.apollographql.apollo.api.internal.stringResponseAdapter
import com.apollographql.apollo.exception.UnexpectedNullValue
import com.example.fragments_with_type_condition.TestQuery
import kotlin.Array
import kotlin.Double
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

@Suppress("NAME_SHADOWING", "UNUSED_ANONYMOUS_PARAMETER", "LocalVariableName",
    "RemoveExplicitTypeArguments", "NestedLambdaShadowedImplicitParameter", "PropertyName",
    "RemoveRedundantQualifierName")
class TestQuery_ResponseAdapter(
  customScalarAdapters: CustomScalarAdapters
) : ResponseAdapter<TestQuery.Data> {
  val r2Adapter: ResponseAdapter<TestQuery.Data.R2?> =
      NullableResponseAdapter(R2(customScalarAdapters))

  val lukeAdapter: ResponseAdapter<TestQuery.Data.Luke?> =
      NullableResponseAdapter(Luke(customScalarAdapters))

  override fun fromResponse(reader: JsonReader): TestQuery.Data {
    var r2: TestQuery.Data.R2? = null
    var luke: TestQuery.Data.Luke? = null
    reader.beginObject()
    while(true) {
      when (reader.selectName(RESPONSE_NAMES)) {
        0 -> r2 = r2Adapter.fromResponse(reader)
        1 -> luke = lukeAdapter.fromResponse(reader)
        else -> break
      }
    }
    reader.endObject()
    return TestQuery.Data(
      r2 = r2,
      luke = luke
    )
  }

  override fun toResponse(writer: JsonWriter, value: TestQuery.Data) {
    writer.beginObject()
    writer.name("r2")
    r2Adapter.toResponse(writer, value.r2)
    writer.name("luke")
    lukeAdapter.toResponse(writer, value.luke)
    writer.endObject()
  }

  companion object {
    val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
      ResponseField(
        type = ResponseField.Type.Named.Object("Character"),
        fieldName = "hero",
        responseName = "r2",
        fieldSets = listOf(
          ResponseField.FieldSet("Human", R2.HumanR2.RESPONSE_FIELDS),
          ResponseField.FieldSet("Droid", R2.DroidR2.RESPONSE_FIELDS),
          ResponseField.FieldSet(null, R2.OtherR2.RESPONSE_FIELDS),
        ),
      ),
      ResponseField(
        type = ResponseField.Type.Named.Object("Character"),
        fieldName = "hero",
        responseName = "luke",
        fieldSets = listOf(
          ResponseField.FieldSet("Human", Luke.HumanLuke.RESPONSE_FIELDS),
          ResponseField.FieldSet("Droid", Luke.DroidLuke.RESPONSE_FIELDS),
          ResponseField.FieldSet(null, Luke.OtherLuke.RESPONSE_FIELDS),
        ),
      )
    )

    val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
  }

  class R2(
    customScalarAdapters: CustomScalarAdapters
  ) : ResponseAdapter<TestQuery.Data.R2> {
    val humanR2Adapter: HumanR2 =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.R2.HumanR2(customScalarAdapters)

    val droidR2Adapter: DroidR2 =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.R2.DroidR2(customScalarAdapters)

    val otherR2Adapter: OtherR2 =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.R2.OtherR2(customScalarAdapters)

    override fun fromResponse(reader: JsonReader): TestQuery.Data.R2 {
      reader.beginObject()
      check(reader.nextName() == "__typename")
      val typename = reader.nextString()

      return when(typename) {
        "Human" -> humanR2Adapter.fromResponse(reader, typename)
        "Droid" -> droidR2Adapter.fromResponse(reader, typename)
        else -> otherR2Adapter.fromResponse(reader, typename)
      }
      .also { reader.endObject() }
    }

    override fun toResponse(writer: JsonWriter, value: TestQuery.Data.R2) {
      when(value) {
        is TestQuery.Data.R2.HumanR2 -> humanR2Adapter.toResponse(writer, value)
        is TestQuery.Data.R2.DroidR2 -> droidR2Adapter.toResponse(writer, value)
        is TestQuery.Data.R2.OtherR2 -> otherR2Adapter.toResponse(writer, value)
      }
    }

    class HumanR2(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val nameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val heightAdapter: ResponseAdapter<Double?> = NullableResponseAdapter(doubleResponseAdapter)

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.R2.HumanR2 {
        var __typename: String? = __typename
        var name: String? = null
        var height: Double? = null
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            1 -> name = nameAdapter.fromResponse(reader) ?: throw UnexpectedNullValue("name")
            2 -> height = heightAdapter.fromResponse(reader)
            else -> break
          }
        }
        return TestQuery.Data.R2.HumanR2(
          __typename = __typename!!,
          name = name!!,
          height = height
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.R2.HumanR2) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.name("name")
        nameAdapter.toResponse(writer, value.name)
        writer.name("height")
        heightAdapter.toResponse(writer, value.height)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename,
          ResponseField(
            type = ResponseField.Type.NotNull(ResponseField.Type.Named.Other("String")),
            fieldName = "name",
          ),
          ResponseField(
            type = ResponseField.Type.Named.Other("Float"),
            fieldName = "height",
          )
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }

    class DroidR2(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val nameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val primaryFunctionAdapter: ResponseAdapter<String?> =
          NullableResponseAdapter(stringResponseAdapter)

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.R2.DroidR2 {
        var __typename: String? = __typename
        var name: String? = null
        var primaryFunction: String? = null
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            1 -> name = nameAdapter.fromResponse(reader) ?: throw UnexpectedNullValue("name")
            2 -> primaryFunction = primaryFunctionAdapter.fromResponse(reader)
            else -> break
          }
        }
        return TestQuery.Data.R2.DroidR2(
          __typename = __typename!!,
          name = name!!,
          primaryFunction = primaryFunction
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.R2.DroidR2) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.name("name")
        nameAdapter.toResponse(writer, value.name)
        writer.name("primaryFunction")
        primaryFunctionAdapter.toResponse(writer, value.primaryFunction)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename,
          ResponseField(
            type = ResponseField.Type.NotNull(ResponseField.Type.Named.Other("String")),
            fieldName = "name",
          ),
          ResponseField(
            type = ResponseField.Type.Named.Other("String"),
            fieldName = "primaryFunction",
          )
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }

    class OtherR2(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.R2.OtherR2 {
        var __typename: String? = __typename
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            else -> break
          }
        }
        return TestQuery.Data.R2.OtherR2(
          __typename = __typename!!
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.R2.OtherR2) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }
  }

  class Luke(
    customScalarAdapters: CustomScalarAdapters
  ) : ResponseAdapter<TestQuery.Data.Luke> {
    val humanLukeAdapter: HumanLuke =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.Luke.HumanLuke(customScalarAdapters)

    val droidLukeAdapter: DroidLuke =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.Luke.DroidLuke(customScalarAdapters)

    val otherLukeAdapter: OtherLuke =
        com.example.fragments_with_type_condition.adapter.TestQuery_ResponseAdapter.Luke.OtherLuke(customScalarAdapters)

    override fun fromResponse(reader: JsonReader): TestQuery.Data.Luke {
      reader.beginObject()
      check(reader.nextName() == "__typename")
      val typename = reader.nextString()

      return when(typename) {
        "Human" -> humanLukeAdapter.fromResponse(reader, typename)
        "Droid" -> droidLukeAdapter.fromResponse(reader, typename)
        else -> otherLukeAdapter.fromResponse(reader, typename)
      }
      .also { reader.endObject() }
    }

    override fun toResponse(writer: JsonWriter, value: TestQuery.Data.Luke) {
      when(value) {
        is TestQuery.Data.Luke.HumanLuke -> humanLukeAdapter.toResponse(writer, value)
        is TestQuery.Data.Luke.DroidLuke -> droidLukeAdapter.toResponse(writer, value)
        is TestQuery.Data.Luke.OtherLuke -> otherLukeAdapter.toResponse(writer, value)
      }
    }

    class HumanLuke(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val nameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val heightAdapter: ResponseAdapter<Double?> = NullableResponseAdapter(doubleResponseAdapter)

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.Luke.HumanLuke {
        var __typename: String? = __typename
        var name: String? = null
        var height: Double? = null
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            1 -> name = nameAdapter.fromResponse(reader) ?: throw UnexpectedNullValue("name")
            2 -> height = heightAdapter.fromResponse(reader)
            else -> break
          }
        }
        return TestQuery.Data.Luke.HumanLuke(
          __typename = __typename!!,
          name = name!!,
          height = height
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.Luke.HumanLuke) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.name("name")
        nameAdapter.toResponse(writer, value.name)
        writer.name("height")
        heightAdapter.toResponse(writer, value.height)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename,
          ResponseField(
            type = ResponseField.Type.NotNull(ResponseField.Type.Named.Other("String")),
            fieldName = "name",
          ),
          ResponseField(
            type = ResponseField.Type.Named.Other("Float"),
            fieldName = "height",
          )
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }

    class DroidLuke(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val nameAdapter: ResponseAdapter<String> = stringResponseAdapter

      val primaryFunctionAdapter: ResponseAdapter<String?> =
          NullableResponseAdapter(stringResponseAdapter)

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.Luke.DroidLuke {
        var __typename: String? = __typename
        var name: String? = null
        var primaryFunction: String? = null
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            1 -> name = nameAdapter.fromResponse(reader) ?: throw UnexpectedNullValue("name")
            2 -> primaryFunction = primaryFunctionAdapter.fromResponse(reader)
            else -> break
          }
        }
        return TestQuery.Data.Luke.DroidLuke(
          __typename = __typename!!,
          name = name!!,
          primaryFunction = primaryFunction
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.Luke.DroidLuke) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.name("name")
        nameAdapter.toResponse(writer, value.name)
        writer.name("primaryFunction")
        primaryFunctionAdapter.toResponse(writer, value.primaryFunction)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename,
          ResponseField(
            type = ResponseField.Type.NotNull(ResponseField.Type.Named.Other("String")),
            fieldName = "name",
          ),
          ResponseField(
            type = ResponseField.Type.Named.Other("String"),
            fieldName = "primaryFunction",
          )
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }

    class OtherLuke(
      customScalarAdapters: CustomScalarAdapters
    ) {
      val __typenameAdapter: ResponseAdapter<String> = stringResponseAdapter

      fun fromResponse(reader: JsonReader, __typename: String?): TestQuery.Data.Luke.OtherLuke {
        var __typename: String? = __typename
        while(true) {
          when (reader.selectName(RESPONSE_NAMES)) {
            0 -> __typename = __typenameAdapter.fromResponse(reader) ?: throw
                UnexpectedNullValue("__typename")
            else -> break
          }
        }
        return TestQuery.Data.Luke.OtherLuke(
          __typename = __typename!!
        )
      }

      fun toResponse(writer: JsonWriter, value: TestQuery.Data.Luke.OtherLuke) {
        writer.beginObject()
        writer.name("__typename")
        __typenameAdapter.toResponse(writer, value.__typename)
        writer.endObject()
      }

      companion object {
        val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
          ResponseField.Typename
        )

        val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
      }
    }
  }
}