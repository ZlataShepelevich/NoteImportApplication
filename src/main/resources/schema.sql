CREATE TABLE IF NOT EXISTS company_user (
                              id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,

                              login varchar(255) NOT NULL,

                              CONSTRAINT company_user_pkey PRIMARY KEY (id),
                              CONSTRAINT company_user_uniq_login UNIQUE (login)
);


CREATE TABLE IF NOT EXISTS patient_profile (
                                 id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                                 first_name varchar(255) NULL,
                                 last_name varchar(255) NULL,
                                 old_client_guid varchar(255) NULL,
                                 status_id int2 NOT NULL,

                                 CONSTRAINT patient_profile_pkey PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS patient_note (
                              id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                              guid VARCHAR(255) NOT NULL,  -- Field for identifying notes from the old system
                              created_date_time timestamp NOT NULL,
                              last_modified_date_time timestamp NOT NULL,
                              created_by_user_id int8 NULL,
                              last_modified_by_user_id int8 NULL,
                              note varchar(4000) NULL,
                              patient_id int8 NOT NULL,

                              CONSTRAINT patient_note_pkey PRIMARY KEY (id)
);

-- INSERT INTO patient_profile (first_name, last_name, old_client_guid, status_id)
-- VALUES ('Ne', 'Abr', '01588E84-D45A-EB98-F47F-716073A4F1EF', 200);