@java -classpath ..\_schemacrawler\lib\schemacrawler-8.3.jar;..\_schemacrawler\lib\hsqldb-2.0.0.jar schemacrawler.Main -g schemacrawler.config.properties -c hsqldb -command script -infolevel=maximum -sorttables=false -outputformat %1