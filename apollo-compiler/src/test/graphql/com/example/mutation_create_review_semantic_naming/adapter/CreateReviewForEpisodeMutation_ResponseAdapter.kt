// AUTO-GENERATED FILE. DO NOT MODIFY.
//
// This class was automatically generated by Apollo GraphQL plugin from the GraphQL queries it found.
// It should not be modified by hand.
//
package com.example.mutation_create_review_semantic_naming.adapter

import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.internal.NullableResponseAdapter
import com.apollographql.apollo.api.internal.ResponseAdapter
import com.apollographql.apollo.api.internal.intResponseAdapter
import com.apollographql.apollo.api.internal.json.JsonReader
import com.apollographql.apollo.api.internal.json.JsonWriter
import com.apollographql.apollo.api.internal.stringResponseAdapter
import com.apollographql.apollo.exception.UnexpectedNullValue
import com.example.mutation_create_review_semantic_naming.CreateReviewForEpisodeMutation
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List

@Suppress("NAME_SHADOWING", "UNUSED_ANONYMOUS_PARAMETER", "LocalVariableName",
    "RemoveExplicitTypeArguments", "NestedLambdaShadowedImplicitParameter", "PropertyName",
    "RemoveRedundantQualifierName")
class CreateReviewForEpisodeMutation_ResponseAdapter(
  customScalarAdapters: CustomScalarAdapters
) : ResponseAdapter<CreateReviewForEpisodeMutation.Data> {
  val createReviewAdapter: ResponseAdapter<CreateReviewForEpisodeMutation.Data.CreateReview?> =
      NullableResponseAdapter(CreateReview(customScalarAdapters))

  override fun fromResponse(reader: JsonReader): CreateReviewForEpisodeMutation.Data {
    var createReview: CreateReviewForEpisodeMutation.Data.CreateReview? = null
    reader.beginObject()
    while(true) {
      when (reader.selectName(RESPONSE_NAMES)) {
        0 -> createReview = createReviewAdapter.fromResponse(reader)
        else -> break
      }
    }
    reader.endObject()
    return CreateReviewForEpisodeMutation.Data(
      createReview = createReview
    )
  }

  override fun toResponse(writer: JsonWriter, value: CreateReviewForEpisodeMutation.Data) {
    writer.beginObject()
    writer.name("createReview")
    createReviewAdapter.toResponse(writer, value.createReview)
    writer.endObject()
  }

  companion object {
    val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
      ResponseField(
        type = ResponseField.Type.Named.Object("Review"),
        fieldName = "createReview",
        arguments = mapOf<String, Any?>(
          "episode" to mapOf<String, Any?>(
            "kind" to "Variable",
            "variableName" to "ep"),
          "review" to mapOf<String, Any?>(
            "kind" to "Variable",
            "variableName" to "review")),
        fieldSets = listOf(
          ResponseField.FieldSet(null, CreateReview.RESPONSE_FIELDS)
        ),
      )
    )

    val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
  }

  class CreateReview(
    customScalarAdapters: CustomScalarAdapters
  ) : ResponseAdapter<CreateReviewForEpisodeMutation.Data.CreateReview> {
    val starsAdapter: ResponseAdapter<Int> = intResponseAdapter

    val commentaryAdapter: ResponseAdapter<String?> = NullableResponseAdapter(stringResponseAdapter)

    override fun fromResponse(reader: JsonReader):
        CreateReviewForEpisodeMutation.Data.CreateReview {
      var stars: Int? = null
      var commentary: String? = null
      reader.beginObject()
      while(true) {
        when (reader.selectName(RESPONSE_NAMES)) {
          0 -> stars = starsAdapter.fromResponse(reader) ?: throw UnexpectedNullValue("stars")
          1 -> commentary = commentaryAdapter.fromResponse(reader)
          else -> break
        }
      }
      reader.endObject()
      return CreateReviewForEpisodeMutation.Data.CreateReview(
        stars = stars!!,
        commentary = commentary
      )
    }

    override fun toResponse(writer: JsonWriter,
        value: CreateReviewForEpisodeMutation.Data.CreateReview) {
      writer.beginObject()
      writer.name("stars")
      starsAdapter.toResponse(writer, value.stars)
      writer.name("commentary")
      commentaryAdapter.toResponse(writer, value.commentary)
      writer.endObject()
    }

    companion object {
      val RESPONSE_FIELDS: Array<ResponseField> = arrayOf(
        ResponseField(
          type = ResponseField.Type.NotNull(ResponseField.Type.Named.Other("Int")),
          fieldName = "stars",
        ),
        ResponseField(
          type = ResponseField.Type.Named.Other("String"),
          fieldName = "commentary",
        )
      )

      val RESPONSE_NAMES: List<String> = RESPONSE_FIELDS.map { it.responseName }
    }
  }
}