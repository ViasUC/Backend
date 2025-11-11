package com.vias.uc.backend.codegen.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.vias.uc.backend.codegen.types.Alumno;
import graphql.schema.DataFetchingEnvironment;
import jakarta.annotation.Generated;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
@DgsComponent
public class AlumnoPorEmailDatafetcher {
  @DgsData(
      parentType = "Query",
      field = "alumnoPorEmail"
  )
  public Alumno getAlumnoPorEmail(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
