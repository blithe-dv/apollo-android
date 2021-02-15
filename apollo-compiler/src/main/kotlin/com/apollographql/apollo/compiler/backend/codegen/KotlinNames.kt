package com.apollographql.apollo.compiler.backend.codegen

import com.apollographql.apollo.compiler.backend.ast.CodeGenerationAst
import com.apollographql.apollo.compiler.escapeKotlinReservedWord

/**
 * This file contains GraphQL -> Kotlin transformations
 *
 * this is mostly empty right now but it'd be nice to centralize everything here so we can have a central place to
 * control name generation
 */
internal fun kotlinNameForEnumValue(graphqlEnumValue: String) = graphqlEnumValue.toUpperCase()
internal fun kotlinNameForEnum(graphqlEnum: String) = graphqlEnum.escapeKotlinReservedWord()
internal fun kotlinNameForField(responseName: String) = responseName.decapitalize().escapeKotlinReservedWord()
internal fun kotlinNameForVariable(variableName: String) = kotlinNameForField(variableName)
internal fun kotlinNameForAdapterField(type: CodeGenerationAst.FieldType): String {
  return kotlinNameForAdapterFieldRecursive(type).decapitalize() + "Adapter"
}
internal fun kotlinNameForTypeCaseAdapterField(typeRef: CodeGenerationAst.TypeRef): String {
  return typeRef.name.escapeKotlinReservedWord() + "Adapter"
}
internal fun kotlinNameForResponseAdapter(graphqlName: String) = "${graphqlName.capitalize()}_ResponseAdapter"


private fun kotlinNameForAdapterFieldRecursive(type: CodeGenerationAst.FieldType): String {
  if (type.nullable) {
    return "Nullable" + kotlinNameForAdapterFieldRecursive(type.nonNullable())
  }

  return when (type) {
    is CodeGenerationAst.FieldType.Array -> "ListOf" + kotlinNameForAdapterFieldRecursive(type.rawType)
    is CodeGenerationAst.FieldType.Input -> "InputOf" + kotlinNameForAdapterFieldRecursive(type.rawType)
    is CodeGenerationAst.FieldType.Object -> type.typeRef.name.capitalize()
    is CodeGenerationAst.FieldType.Scalar -> type.schemaTypeName.capitalize()
  }
}