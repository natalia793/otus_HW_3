public enum SchemaPath {


  store("SchemaStore.json"),
  pet("SchemaPetAdd.json");

  public final String schema;

  SchemaPath(String schema) {
    this.schema = schema;
  }


}
