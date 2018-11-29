# --- !Ups
insert into user_account (name, username, email, password, is_on, profile, created, updated)
values ('admin', 'admin', 'xavi@lasalle.edu', '$s0$81010$VITgKFmVaDk78aYGUQjeZA==$m8PRIuq0mU68Us/MWm+IWpyE7twzkfUZsg5uHBOa8cw=', true, 'ADMIN', now(), now());

# --- !Downs