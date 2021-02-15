package com.apollographql.apollo.compiler.backend

import com.apollographql.apollo.compiler.backend.ast.AstBuilder.Companion.buildAst
import com.apollographql.apollo.compiler.backend.codegen.implementationTypeSpec
import com.apollographql.apollo.compiler.backend.codegen.interfaceTypeSpec
import com.apollographql.apollo.compiler.backend.codegen.patchKotlinNativeOptionalArrayProperties
import com.apollographql.apollo.compiler.backend.codegen.responseAdapterTypeSpec
import com.apollographql.apollo.compiler.backend.codegen.typeSpec
import com.apollographql.apollo.compiler.backend.codegen.typeSpecs
import com.apollographql.apollo.compiler.backend.ir.BackendIr
import com.apollographql.apollo.compiler.escapeKotlinReservedWord
import com.apollographql.apollo.compiler.frontend.Schema
import com.apollographql.apollo.compiler.frontend.toIntrospectionSchema
import com.apollographql.apollo.compiler.operationoutput.OperationOutput
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

internal class GraphQLCodeGenerator(
    private val backendIr: BackendIr,
    private val schema: Schema,
    private val customScalarsMapping: Map<String, String>,
    private val generateAsInternal: Boolean = false,
    private val operationOutput: OperationOutput,
    private val generateFilterNotNull: Boolean,
    private val enumAsSealedClassPatternFilters: List<Regex>,
    private val enumsToGenerate: Set<String>,
    private val inputObjectsToGenerate: Set<String>,
    private val generateScalarMapping: Boolean,
    private val typesPackageName: String,
    private val fragmentsPackageName: String,
    private val generateFragmentImplementations: Boolean,
) {
  fun write(outputDir: File) {

    val introspectionSchema = schema.toIntrospectionSchema()
    val ast = backendIr.buildAst(
        schema = introspectionSchema,
        customScalarsMapping = customScalarsMapping,
        operationOutput = operationOutput,
        typesPackageName = typesPackageName,
        fragmentsPackage = fragmentsPackageName
    )

    if (generateScalarMapping && ast.customScalarTypes.isNotEmpty()) {
      ast.customScalarTypes.values.typeSpec(generateAsInternal)
          .fileSpec(typesPackageName)
          .writeTo(outputDir)
    }

    ast.enumTypes
        .filter { enumType -> enumsToGenerate.contains(enumType.graphqlName) }
        .forEach { enumType ->
          fileSpec(typesPackageName, enumType.name.escapeKotlinReservedWord()) {
            enumType.typeSpecs(
                generateAsInternal = generateAsInternal,
                enumAsSealedClassPatternFilters = enumAsSealedClassPatternFilters,
                packageName = typesPackageName
            ).forEach {
              addType(it)
            }
          }.writeTo(outputDir)
        }

    ast.inputTypes
        .filter { inputType -> inputObjectsToGenerate.contains(inputType.graphqlName) }
        .forEach { inputType ->
          inputType
              .typeSpec(generateAsInternal)
              .fileSpec(typesPackageName)
              .writeTo(outputDir)

          inputType
              .responseAdapterTypeSpec(generateAsInternal)
              .fileSpec("${typesPackageName}.adapter")
              .writeTo(outputDir)
        }

    ast.fragmentTypes
        .forEach { fragmentType ->
          fragmentType
              .interfaceTypeSpec(generateAsInternal)
              .fileSpec(fragmentsPackageName)
              .writeTo(outputDir)

          if (generateFragmentImplementations) {
            fragmentType
                .implementationTypeSpec(generateAsInternal = generateAsInternal)
                .fileSpec(fragmentsPackageName)
                .writeTo(outputDir)

            fragmentType
                .responseAdapterTypeSpec(generateAsInternal)
                .fileSpec("${fragmentsPackageName}.adapter")
                .writeTo(outputDir)
          }
        }

    ast.operationTypes.forEach { operationType ->
      operationType
          .typeSpec(
              targetPackage = operationType.packageName,
              generateAsInternal = generateAsInternal
          )
          .let {
            if (generateFilterNotNull) {
              it.patchKotlinNativeOptionalArrayProperties()
            } else it
          }
          .fileSpec(operationType.packageName)
          .writeTo(outputDir)
    }

    ast.operationTypes.forEach { operationType ->
      operationType.responseAdapterTypeSpec(generateAsInternal)
          .fileSpec("${operationType.packageName}.adapter")
          .writeTo(outputDir)
    }
  }

  private fun TypeSpec.fileSpec(packageName: String) = fileSpec(packageName, name!!) {
    addType(this@fileSpec)
  }

  private fun fileSpec(packageName: String, name: String, block: FileSpec.Builder.() -> Unit) =
      FileSpec
          .builder(packageName, name)
          .apply(block)
          .addComment("AUTO-GENERATED FILE. DO NOT MODIFY.\n\n" +
              "This class was automatically generated by Apollo GraphQL plugin from the GraphQL queries it found.\n" +
              "It should not be modified by hand.\n"
          )
          .build()
}
