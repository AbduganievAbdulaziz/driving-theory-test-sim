CREATE TABLE languages (
  lang_id       SMALLINT      NOT NULL,
  lang_code     VARCHAR(10)   NOT NULL,
  name          TEXT          NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT languages_pk PRIMARY KEY (lang_id),
  CONSTRAINT languages_u1 UNIQUE (lang_code),
  CONSTRAINT languages_c1 CHECK (trim(lang_code) = lang_code),
  CONSTRAINT languages_c2 CHECK (trim(name) = name)
);

CREATE TABLE questions (
  question_id   INTEGER   NOT NULL,
  status        CHAR(1)   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT questions_pk PRIMARY KEY (question_id),
  CONSTRAINT questions_c1 CHECK (status IN ('A', 'I', 'D'))
);

COMMENT ON COLUMN questions.status IS '(A)ctive, (I)nactive, (D)raft';

CREATE TABLE question_contents (
  question_id       INTEGER   NOT NULL,
  lang_id           SMALLINT  NOT NULL,
  question_text     TEXT      NOT NULL,
  explanation_text  TEXT,
  image_url         TEXT,
  answer_video_url  TEXT,
  status            CHAR(1)   NOT NULL,
  created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT question_contents_pk PRIMARY KEY (question_id, lang_id),
  CONSTRAINT question_contents_f1 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  CONSTRAINT question_contents_f2 FOREIGN KEY (lang_id) REFERENCES languages(lang_id),
  CONSTRAINT question_contents_c1 CHECK (status IN ('A', 'I', 'D'))
);

COMMENT ON COLUMN question_contents.status IS '(A)ctive, (I)nactive, (D)raft';

CREATE INDEX question_contents_i1 ON question_contents(lang_id);

CREATE TABLE options (
  option_id     INTEGER   NOT NULL,
  question_id   INTEGER   NOT NULL,
  display_order SMALLINT  NOT NULL,
  is_correct    BOOLEAN   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT options_pk PRIMARY KEY (option_id),
  CONSTRAINT options_u1 UNIQUE (question_id, display_order),
  CONSTRAINT options_f1 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  CONSTRAINT options_c1 CHECK (display_order > 0)
);

CREATE TABLE option_contents (
  option_id     INTEGER   NOT NULL,
  lang_id       SMALLINT  NOT NULL,
  option_text   TEXT      NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT option_contents_pk PRIMARY KEY (option_id, lang_id),
  CONSTRAINT option_contents_f1 FOREIGN KEY (option_id) REFERENCES options(option_id) ON DELETE CASCADE,
  CONSTRAINT option_contents_f2 FOREIGN KEY (lang_id) REFERENCES languages(lang_id)
);

CREATE INDEX option_contents_i1 ON option_contents(lang_id);

CREATE TABLE templates (
  template_id   INTEGER   NOT NULL,
  code          TEXT      NOT NULL,
  status        CHAR(1)   NOT NULL,
  created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT templates_pk PRIMARY KEY (template_id),
  CONSTRAINT templates_u1 UNIQUE (code),
  CONSTRAINT templates_c1 CHECK (trim(code) = code),
  CONSTRAINT templates_c2 CHECK (status IN ('A', 'I', 'D'))
);

COMMENT ON COLUMN templates.status IS '(A)ctive, (I)nactive, (D)raft';

CREATE TABLE template_questions (
  template_id     INTEGER   NOT NULL,
  question_id     INTEGER   NOT NULL,
  display_order   SMALLINT  NOT NULL,
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  modified_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT template_questions_pk PRIMARY KEY (template_id, question_id),
  CONSTRAINT template_questions_u1 UNIQUE (template_id, display_order),
  CONSTRAINT template_questions_f1 FOREIGN KEY (template_id) REFERENCES templates(template_id) ON DELETE CASCADE,
  CONSTRAINT template_questions_f2 FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  CONSTRAINT template_questions_c1 CHECK (display_order > 0)
);

CREATE INDEX template_questions_i1 ON template_questions(question_id);