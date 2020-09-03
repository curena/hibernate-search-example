# Hibernate Search PoC

This is meant to demonstrate the ability to implement full-text searching capabilities using Hibernate Search with a relational database (using in-memory H2 for simplicity's sake here.)\
Using Spring Data JPA, two entities are defined: `BookEntity`, `AuthorEntity`.

There is a many-to-many relation between Books and Authors. 
I.e.: A book can be associated to many authors, an author can be associated to many books.

The entry point is the `BookDao`. There are a few sample full text queries, including searching 
for books by title, and a faceted search example using author as the facet.

See `BookDaoIntegrationSpec` for specific calls.



