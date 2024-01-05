module moneySpender.restapi {
  requires transitive moneySpender.core;
  requires transitive moneySpender.persistence;
  requires spring.web;
  requires spring.beans;
  requires spring.boot;
  requires spring.context;
  requires spring.boot.autoconfigure;
  requires com.google.gson;

  opens restapi to
      spring.beans,
      spring.context,
      spring.web;
}
