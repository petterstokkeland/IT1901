module moneySpender.persistence {
  requires transitive moneySpender.core;
  requires transitive com.google.gson;

  exports persistence;

  opens persistence to
      com.google.gson; 
}
