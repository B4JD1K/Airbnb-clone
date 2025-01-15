package pl.b4jd1k.airbnb_clone_back.user.application;

public class UserException extends RuntimeException {

  // utworzony ctor, przykład użycia
  // if (user==null)
  // throw new UserExcetption("user with id" + id + "not exist");

  public UserException(String message) {
    super(message);
  }
}
