pomParser
=========

Have you ever had many maven projects, depending on each other?
Have you ever tried to find a tool to show you schematically how these projects are related to each other,
but you don't really like them or they are very hard to install or to use?

pomParser creates an html document showing your POM dependency trees, whith hyperlinks between project names,
where you can search and navigate both ways: 
which projects depend on certain project, and on which projects depends certain project

Steps:

1)put the project into a folder and compile it with maven:
mvn install

2)gather all your pom files and change their names (so they don't overrite each other) and copy them to a directory.

3)get the jar and use it like this:
java -jar pom_parser-X.X.jar <origin_dir_where_all_your_poms_are> <dest_dir_where_the_graph_goes>

And that's it, you'll get a nice html document that will help you understand better how your poms are related to each other.

