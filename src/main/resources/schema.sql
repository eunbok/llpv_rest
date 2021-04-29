CREATE TABLE IF NOT EXISTS `llpv-log`
(
    run_file varchar(100) NOT NULL,
    run_title varchar(100) NOT NULL,
    run_sec int NOT NULL,
    _datetime datetime NOT NULL,
    stored_time datetime NOT NULL,
    client_ip varchar(15) NOT NULL,
    _timestamp timestamp NOT NULL default CURRENT_TIMESTAMP,
    large_category varchar(100) NOT NULL,
    medium_category varchar(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS `llpv-cate`
(
    run_file varchar(100),
    run_title varchar(100),
    _datetime datetime NOT NULL default current_timestamp,
    large_category varchar(100),
    medium_category varchar(100)
);

CREATE TABLE IF NOT EXISTS `llpv-rank`
(
    rank_date date NOT NULL PRIMARY KEY,
    rank_result JSON CHECK (JSON_VALID(rank_result)),
    rank_type varchar(100)
);