package com.apollographql.apollo.api.internal

import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.Response.Companion.builder
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.parseData

/**
 * [MapResponseParser] parses network responses, including data, errors and extensions from a regular Map<String, Any?>.
 * For better performance, you can parse a response from a [okio.BufferedSource] directly with [StreamResponseParser].
 * That will avoid the cost of having to create an entire Map in memory
 */
object MapResponseParser {
  /**
   * @param payload, the root of the response
   *
   */
  fun <D : Operation.Data> parse(
      payload: Map<String, Any?>,
      operation: Operation<D>,
      customScalarAdapters: CustomScalarAdapters,
  ): Response<D> {
    val data = (payload["data"] as Map<String, Any?>?)?.let {
      operation.parseData(it, customScalarAdapters)
    }

    val errors = if (payload.containsKey("errors")) {
      (payload["errors"] as List<Map<String, Any?>>?)?.map {
        parseError(it)
      }
    } else {
      null
    }

    return builder<D>(operation)
        .data(data)
        .errors(errors)
        .extensions(payload["extensions"] as Map<String, Any?>?)
        .build()
  }

  fun parseError(payload: Map<String, Any?>): Error {
    var message = ""
    val locations = mutableListOf<Error.Location>()
    val customAttributes = mutableMapOf<String, Any?>()
    for ((key, value) in payload) {
      if ("message" == key) {
        message = value?.toString() ?: ""
      } else if ("locations" == key) {
        val locationItems = value as List<Map<String, Any>>?
        if (locationItems != null) {
          for (item in locationItems) {
            locations.add(parseErrorLocation(item))
          }
        }
      } else {
        if (value != null) {
          customAttributes[key] = value
        }
      }
    }
    return Error(message, locations, customAttributes)
  }

  private fun parseErrorLocation(data: Map<String, Any>?): Error.Location {
    var line: Long = -1
    var column: Long = -1
    if (data != null) {
      for ((key, value) in data) {
        if ("line" == key) {
          line = (value as Number).toLong()
        } else if ("column" == key) {
          column = (value as Number).toLong()
        }
      }
    }
    return Error.Location(line, column)
  }
}