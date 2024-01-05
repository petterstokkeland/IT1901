module moneySpender.ui {
  requires moneySpender.core;
  requires moneySpender.persistence;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires java.net.http;

  opens ui to
      javafx.graphics,
      javafx.fxml;
}
