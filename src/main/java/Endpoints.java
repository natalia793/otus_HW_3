public enum Endpoints {

  Pets("/pet"),
  NotFoundPet("/pet/1298798798687675765"),

  storeEndpoint("/store/order");

  public final String endpoints;

  Endpoints(String endpoints) {
    this.endpoints = endpoints;
  }
}