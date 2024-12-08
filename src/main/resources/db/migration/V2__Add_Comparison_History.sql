create table comparison_history(
  id serial primary key,
  user_id integer references users(id) on delete cascade,
  faction_won text,
  prediction_was_right boolean,
  played_map text not null,
    match_link text not null,
  failure_message text,
  created_at timestamp default now(),
  found_result_at timestamp
);