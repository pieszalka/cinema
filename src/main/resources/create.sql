CREATE TABLE movies (
  id            VARCHAR(255) PRIMARY KEY,
  name          VARCHAR(255)      NOT NULL,
  description   VARCHAR(255)   NOT NULL,
  category      VARCHAR(255)   NOT NULL,
  create_date   TIMESTAMP NOT NULL
);

CREATE TABLE reviews (
  id            VARCHAR(255) PRIMARY KEY,
  movieId       VARCHAR(255)      NOT NULL,
  rate          INTEGER NOT NULL,
  description   VARCHAR(255)   NOT NULL,
  create_date   TIMESTAMP NOT NULL
);

INSERT INTO movies(id, name, description, category, create_date) values('1', 'Strange Movie', 'description', 'HORROR', now());
INSERT INTO movies(id, name, description, category, create_date) values('2', 'Movie 2', 'description', 'DRAMA', now());
INSERT INTO movies(id, name, description, category, create_date) values('3', 'Movie 3', 'description', 'DRAMA', now());
INSERT INTO movies(id, name, description, category, create_date) values('4', 'Movie 4', 'description', 'DRAMA', now());
INSERT INTO movies(id, name, description, category, create_date) values('5', 'Movie 5', 'description', 'DRAMA', now());

INSERT INTO reviews(id, movieId, rate, description, create_date) values('1', '1', 3, 'description-1', now());
INSERT INTO reviews(id, movieId, rate, description, create_date) values('2', '1', 4, 'description-2', now());
INSERT INTO reviews(id, movieId, rate, description, create_date) values('3', '1', 4, 'description-3', now());
INSERT INTO reviews(id, movieId, rate, description, create_date) values('4', '2', 4, 'description-4', now());