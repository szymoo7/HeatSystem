module service {
    requires org.xerial.sqlitejdbc;
    opens org.example.service;
    exports org.example.service;

}