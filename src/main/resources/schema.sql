-- delete all tables

drop table IF EXISTS users    CASCADE;
drop table IF EXISTS items    CASCADE;
drop table IF EXISTS bookings CASCADE;
drop table IF EXISTS requests CASCADE;
drop table IF EXISTS comments CASCADE;

-- create user related tables

create TABLE IF NOT EXISTS users
(
  id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name 	VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  UNIQUE(id)
);

create TABLE IF NOT EXISTS requests
(
  id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  requestor_id BIGINT,
  description  VARCHAR(1000) NOT NULL,
  created      TIMESTAMP NOT NULL,
  CONSTRAINT   fk_request_to_users FOREIGN KEY(requestor_id) REFERENCES users(id),
  UNIQUE(id)
);

create TABLE IF NOT EXISTS items
(
  id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  owner_id     BIGINT,
  request_id   BIGINT, 
  name 	       VARCHAR(100) NOT NULL,
  description  VARCHAR(1000) NOT NULL,
  is_available BOOLEAN DEFAULT FALSE,
  CONSTRAINT   fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
  CONSTRAINT   fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id),
  UNIQUE(id)
);

create TABLE IF NOT EXISTS bookings
(
  id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  booker_id  BIGINT,
  item_id    BIGINT,
  start_date TIMESTAMP NOT NULL,
  end_date   TIMESTAMP NOT NULL,
  status     VARCHAR(10) NOT NULL,
  CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id), 
  CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id),
  UNIQUE(id)
);

create TABLE IF NOT EXISTS comments
(
  id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  author_id  BIGINT,
  item_id    BIGINT,
  text       VARCHAR,
  created    TIMESTAMP,
  CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id),
  CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id),
  UNIQUE(id)
);