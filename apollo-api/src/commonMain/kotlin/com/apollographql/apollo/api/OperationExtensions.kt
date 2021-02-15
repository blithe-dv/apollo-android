package com.apollographql.apollo.api

import com.apollographql.apollo.api.CustomScalarAdapters.Companion.DEFAULT
import com.apollographql.apollo.api.internal.MapJsonReader
import com.apollographql.apollo.api.internal.MapResponseParser
import com.apollographql.apollo.api.internal.OperationRequestBodyComposer
import com.apollographql.apollo.api.internal.StreamResponseParser
import com.apollographql.apollo.api.internal.json.JsonUtf8Writer
import okio.Buffer
import okio.BufferedSource
import okio.ByteString
import okio.IOException
import kotlin.jvm.JvmOverloads

/**
 * Serializes GraphQL operation response data into its equivalent Json representation.
 * For example:
 * <pre>{@code
 *    {
 *      "data": {
 *        "allPlanets": {
 *          "__typename": "PlanetsConnection",
 *          "planets": [
 *            {
 *              "__typename": "Planet",
 *              "name": "Tatooine",
 *              "surfaceWater": 1.0
 *            }
 *          ]
 *        }
 *      }
 *    }
 * }</pre>
 *
 * @param indent the indentation string to be repeated for each level of indentation in the encoded document. Must be a string
 * containing only whitespace. If [indent] is an empty String the encoded document will be compact. Otherwise the encoded
 * document will be more human-readable.
 * @param customScalarAdapters configured instance of custom GraphQL scalar type adapters. Default adapters are used if this
 * param is not provided.
 */
@JvmOverloads
fun <D : Operation.Data> Operation<D>.toJson(data: D, indent: String = "", customScalarAdapters: CustomScalarAdapters = DEFAULT): String {
  return try {
    val buffer = Buffer()
    val writer = JsonUtf8Writer(buffer).apply {
      this.indent = indent
    }
    // Do we need to wrap in data?
    writer.beginObject()
    writer.name("data")
    adapter(customScalarAdapters).toResponse(writer, data)
    writer.endObject()
    buffer.readUtf8()
  } catch (e: IOException) {
    throw IllegalStateException(e)
  }
}

/**
 * Composes POST JSON-encoded request body to be sent to the GraphQL server.
 *
 * In case when [autoPersistQueries] is set to `true` special `extension` attributes, required by query auto persistence,
 * will be encoded along with regular GraphQL request body. If query was previously persisted on the GraphQL server
 * set [withQueryDocument] to `false` to skip query document be sent in the request.
 *
 * Optional [customScalarAdapters] must be provided in case when this operation defines variables with custom GraphQL scalar type.
 *
 * *Example*:
 * ```
 * {
 *    "query": "query TestQuery($episode: Episode) { hero(episode: $episode) { name } }",
 *    "operationName": "TestQuery",
 *    "variables": { "episode": "JEDI" }
 *    "extensions": {
 *      "persistedQuery": {
 *        "version": 1,
 *        "sha256Hash": "32637895609e6c51a2593f5cfb49244fd79358d327ff670b3e930e024c3db8f6"
 *      }
 *    }
 * }
 * ```
 */
@JvmOverloads
fun Operation<*>.composeRequestBody(
    autoPersistQueries: Boolean,
    withQueryDocument: Boolean,
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): ByteString {
  return OperationRequestBodyComposer.compose(
      operation = this,
      autoPersistQueries = autoPersistQueries,
      withQueryDocument = withQueryDocument,
      customScalarAdapters = customScalarAdapters
  )
}

/**
 * Composes POST JSON-encoded request body with provided [customScalarAdapters] to be sent to the GraphQL server.
 *
 * *Example*:
 * ```
 * {
 *    "query": "query TestQuery($episode: Episode) { hero(episode: $episode) { name } }",
 *    "operationName": "TestQuery",
 *    "variables": { "episode": "JEDI" }
 * }
 * ```
 */
@JvmOverloads
fun Operation<*>.composeRequestBody(
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): ByteString {
  return OperationRequestBodyComposer.compose(
      operation = this,
      autoPersistQueries = false,
      withQueryDocument = true,
      customScalarAdapters = customScalarAdapters
  )
}

/**
 * Parses GraphQL operation raw response from the [source] with provided [customScalarAdapters] and returns result [Response]
 *
 * This will consume [source] so you don't need to close it. Also, you cannot reuse it
 */
@JvmOverloads
fun <D : Operation.Data> Operation<D>.parse(
    source: BufferedSource,
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): Response<D> {
  return StreamResponseParser.parse(source, this, customScalarAdapters)
}

/**
 * Parses GraphQL operation raw response from [byteString] with provided [customScalarAdapters] and returns result [Response]
 */
fun <D : Operation.Data> Operation<D>.parse(
    byteString: ByteString,
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): Response<D> {
  return parse(Buffer().write(byteString), customScalarAdapters)
}

/**
 * Parses GraphQL operation raw response from [string] with provided [customScalarAdapters] and returns result [Response]
 */
fun <D : Operation.Data> Operation<D>.parse(
    string: String,
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): Response<D> {
  return parse(Buffer().writeUtf8(string), customScalarAdapters)
}

/**
 * Parses GraphQL operation raw response from [map] with provided [customScalarAdapters] and returns result [Response]
 *
 * @param map: a [Map] representing the response. It typically include a "data" field
 */
fun <D : Operation.Data> Operation<D>.parse(
    map: Map<String, Any?>,
    customScalarAdapters: CustomScalarAdapters = DEFAULT
): Response<D> {
  return MapResponseParser.parse(map, this, customScalarAdapters)
}

/**
 * Parses GraphQL operation raw response from [map] with provided [customScalarAdapters] and returns result [Response]
 *
 * @param map: a [Map] representing the response. It typically include a "data" field
 */
fun <D : Operation.Data, M: Map<String, Any?>> Operation<D>.parseData(
    map: M,
    customScalarAdapters: CustomScalarAdapters = DEFAULT,
): D {
  return MapJsonReader(
      map,
  ).let {
    adapter(customScalarAdapters).fromResponse(it)
  }
}


fun Operation.Variables.toJson(customScalarAdapters: CustomScalarAdapters): String {
  return Buffer().apply {
    toResponse(JsonUtf8Writer(this), customScalarAdapters)
  }.readUtf8()
}