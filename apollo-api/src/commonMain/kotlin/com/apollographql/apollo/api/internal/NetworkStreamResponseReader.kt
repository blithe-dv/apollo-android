package com.apollographql.apollo.api.internal

import com.apollographql.apollo.api.CustomScalar
import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.JsonElement
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.internal.json.JsonReader
import com.apollographql.apollo.api.internal.json.Utils.readRecursively

 class NetworkStreamResponseReader(
    private val jsonReader: JsonReader,
    private val customScalarAdapters: CustomScalarAdapters,
) : ResponseReader, ResponseReader.ListItemReader {
  private var selectedField: ResponseField? = null

  private var selectedFieldIndex: Int = -1

  override fun selectField(fields: Array<ResponseField>): Int {
    while (jsonReader.hasNext()) {
      val nextFieldName = jsonReader.nextName()

      // trying to guess that next selected field will be the next field in the JSON stream
      selectedFieldIndex++

      if (selectedFieldIndex >= fields.size || fields[selectedFieldIndex].responseName != nextFieldName) {
        // our guess failed, fallback to full scan
        selectedFieldIndex = fields.indexOfFirst { field -> field.responseName == nextFieldName }
      }

      if (selectedFieldIndex == -1) {
        jsonReader.skipValue()
      } else {
        selectedField = fields[selectedFieldIndex]
        return selectedFieldIndex
      }
    }

    return -1
  }

  override fun readString(field: ResponseField): String? {
    return readValue(field) {
      nextString()
    }
  }

  override fun readInt(field: ResponseField): Int? {
    return readValue(field) {
      nextInt()
    }
  }

  override fun readDouble(field: ResponseField): Double? {
    return readValue(field) {
      nextDouble()
    }
  }

  override fun readBoolean(field: ResponseField): Boolean? {
    return readValue(field) {
      nextBoolean()
    }
  }

  override fun <T : Any> readObject(field: ResponseField, block: (ResponseReader) -> T): T? {
    return readValue(field) {
      beginObject()
      val result = block(this@NetworkStreamResponseReader)
      endObject()
      result
    }
  }

  override fun <T : Any> readList(field: ResponseField, block: (ResponseReader.ListItemReader) -> T): List<T?>? {
    return readValue(field) {
      beginArray()
      val result = ArrayList<T?>()
      while (hasNext()) {
        when (peek()) {
          JsonReader.Token.NULL -> result.add(jsonReader.nextNull())
          else -> result.add(block(this@NetworkStreamResponseReader))
        }
      }
      endArray()
      result
    }
  }

  override fun <T : Any> readCustomScalar(field: ResponseField.CustomScalarField): T? {
    val typeAdapter = customScalarAdapters.adapterFor<T>(field.customScalar)
    val value = readValue(field) {
      readRecursively()
    }
    return value?.let { typeAdapter.decode(JsonElement.fromRawValue(it)) }
  }

  private inline fun <T> readValue(field: ResponseField, readValue: JsonReader.() -> T?): T? {
    val fieldToRead = selectedField.also {
      selectedField = null
    }

    check(fieldToRead == null || fieldToRead.responseName == field.responseName) {
      "Expected `${field.responseName}` field to read next from JSON stream but found `${fieldToRead?.responseName}`"
    }

    if (fieldToRead == null) {
      val nextFieldName = jsonReader.nextName()
      check(nextFieldName == field.responseName) {
        "Expected `${field.responseName}` field to read next from JSON stream but found `$nextFieldName`"
      }
    }

    return when (jsonReader.peek()) {
      JsonReader.Token.NULL -> if (field.optional) jsonReader.nextNull() else throw NullPointerException(
        "Couldn't read `${field.responseName}` field value, expected non null value"
      )
      else -> readValue(jsonReader)
    }
  }

  override fun readString(): String {
    return jsonReader.nextString()!!
  }

  override fun readInt(): Int {
    return jsonReader.nextInt()
  }

  override fun readDouble(): Double {
    return jsonReader.nextDouble()
  }

  override fun readBoolean(): Boolean {
    return jsonReader.nextBoolean()
  }

  override fun <T : Any> readCustomScalar(customScalar: CustomScalar): T{
    val typeAdapter = customScalarAdapters.adapterFor<T>(customScalar)
    val value = jsonReader.readRecursively()!!
    return typeAdapter.decode(JsonElement.fromRawValue(value))
  }

  override fun <T : Any> readObject(block: (ResponseReader) -> T): T {
    jsonReader.beginObject()
    val result = block(this@NetworkStreamResponseReader)
    jsonReader.endObject()
    return result
  }

  override fun <T : Any> readList(block: (ResponseReader.ListItemReader) -> T): List<T?> {
    jsonReader.beginArray()
    val result = ArrayList<T?>()
    while (jsonReader.hasNext()) {
      when (jsonReader.peek()) {
        JsonReader.Token.NULL -> result.add(jsonReader.nextNull())
        else -> result.add(block(this))
      }
    }
    jsonReader.endArray()
    return result
  }
}