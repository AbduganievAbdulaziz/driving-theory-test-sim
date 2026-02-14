CREATE TABLE languages (
  lang_id       SMALLINT      NOT NULL,
  lang_code     VARCHAR(10)   NOT NULL,
  name          TEXT          NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT languages_pk PRIMARY KEY (lang_id),
  CONSTRAINT languages_u1 UNIQUE (lang_code)
);

CREATE TABLE migration_question_id_map (
  old_question_id   INTEGER   NOT NULL,
  new_question_id   INTEGER   NOT NULL,
  CONSTRAINT migration_question_id_map_pk PRIMARY KEY (old_question_id),
  CONSTRAINT migration_question_id_map_u1 UNIQUE (new_question_id)
);

CREATE TABLE migration_option_id_map (
  old_option_id   INTEGER   NOT NULL,
  new_option_id   INTEGER   NOT NULL,
  CONSTRAINT migration_option_id_map_pk PRIMARY KEY (old_option_id),
  CONSTRAINT migration_option_id_map_u1 UNIQUE (new_option_id)
);

CREATE TABLE questions (
  question_id   INTEGER   NOT NULL,
  status        CHAR(1)   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT questions_pk PRIMARY KEY (question_id),
  CONSTRAINT questions_c1 CHECK (status IN ('A', 'P'))
);

COMMENT ON COLUMN questions.status IS '(A)ctive, (P)assive';

CREATE TABLE question_contents (
  question_id       INTEGER   NOT NULL,
  lang_id           SMALLINT  NOT NULL,
  question_text     TEXT      NOT NULL,
  explanation_text  TEXT,
  image_url         TEXT,
  answer_video_url  TEXT,
  created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at       TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT question_contents_pk PRIMARY KEY (question_id, lang_id),
  CONSTRAINT question_contents_f1 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  CONSTRAINT question_contents_f2 FOREIGN KEY (lang_id) REFERENCES languages(lang_id)
);

CREATE TABLE options (
  option_id     INTEGER NOT NULL,
  status        CHAR(1)   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT options_pk PRIMARY KEY (option_id),
  CONSTRAINT options_c1 CHECK (status IN ('A', 'P'))
);

COMMENT ON COLUMN options.status IS '(A)ctive, (P)assive';

CREATE TABLE option_contents (
  option_id     INTEGER   NOT NULL,
  lang_id       SMALLINT  NOT NULL,
  option_text   TEXT      NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT option_contents_pk PRIMARY KEY (option_id, lang_id),
  CONSTRAINT option_contents_f1 FOREIGN KEY (option_id) REFERENCES options(option_id) ON DELETE CASCADE,
  CONSTRAINT option_contents_f2 FOREIGN KEY (lang_id) REFERENCES languages(lang_id)
);

CREATE TABLE question_options (
  question_id   INTEGER   NOT NULL,
  option_id     INTEGER   NOT NULL,
  display_order SMALLINT  NOT NULL,
  is_correct    BOOLEAN   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT question_options_pk PRIMARY KEY (question_id, option_id),
  CONSTRAINT question_options_f1 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  CONSTRAINT question_options_f2 FOREIGN KEY (option_id) REFERENCES options(option_id) ON DELETE CASCADE
);

CREATE TABLE templates (
  template_id   INTEGER   NOT NULL,
  code          TEXT      NOT NULL,
  status        CHAR(1)   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT templates_pk PRIMARY KEY (template_id),
  CONSTRAINT templates_u1 UNIQUE (code),
  CONSTRAINT templates_c1 CHECK (status IN ('A', 'P'))
);

COMMENT ON COLUMN templates.status IS '(A)ctive, (P)assive';

CREATE TABLE template_questions (
  template_id     INTEGER   NOT NULL,
  question_id     INTEGER   NOT NULL,
  display_order   SMALLINT  NOT NULL,
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at     TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT template_questions_pk PRIMARY KEY (template_id, question_id),
  CONSTRAINT template_questions_f1 FOREIGN KEY (template_id) REFERENCES templates(template_id) ON DELETE CASCADE,
  CONSTRAINT template_questions_f2 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);