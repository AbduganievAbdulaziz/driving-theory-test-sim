import json
import os
from pathlib import Path

LANG_MAP = {
    'uz':   {'id': 1, 'code': 'uz',   'name': "O'zbekcha (Lotin)"},
    'ru':   {'id': 2, 'code': 'ru',   'name': "Русский"},
    'cyrl': {'id': 3, 'code': 'cyrl', 'name': "Ўзбекча (Кирилл)"}
}

LANG_ORDER = ['uz', 'ru', 'cyrl']
PRIMARY_LANG = LANG_ORDER[0]

next_question_id = 1
next_option_id   = 1
next_template_id = 1

db_questions = {}
db_templates = {}

pos_to_question = {}  # (template_code, display_idx) -> primary_orig_q_id
pos_to_option   = {}  # (template_code, display_idx, opt_idx) -> primary_orig_opt_id
seen_template_questions = set()


def parse_body(body_list):
    text_parts = []
    image_url  = None
    for item in sorted(body_list, key=lambda x: x.get('order', 0)):
        if item.get('type') == 1:
            text_parts.append(item.get('value', ''))
        elif item.get('type') == 2:
            image_url = item.get('value')
    return " ".join(text_parts), image_url


def escape_sql(val):
    if val is None:
        return "NULL"
    s = str(val)
    if not s:
        return "NULL"
    return "'" + s.replace("'", "''") + "'"


def process_primary(template_code, questions):
    global next_question_id, next_option_id, next_template_id
    lang_id = LANG_MAP[PRIMARY_LANG]['id']

    if template_code not in db_templates:
        db_templates[template_code] = {'new_id': next_template_id, 'questions': []}
        next_template_id += 1

    for display_idx, q in enumerate(questions, start=1):
        orig_q_id = q['id']
        q_text, img_url = parse_body(q.get('body', []))
        desc    = q.get('answer_description')
        vid_url = q.get('answer_video')
        status  = 'A' if q.get('status') == 1 else 'I'

        if orig_q_id not in db_questions:
            db_questions[orig_q_id] = {
                'new_id': next_question_id, 'status': status,
                'contents': {}, 'options': {}
            }
            next_question_id += 1

        pos_to_question[(template_code, display_idx)] = orig_q_id

        if (template_code, orig_q_id) not in seen_template_questions:
            seen_template_questions.add((template_code, orig_q_id))
            db_templates[template_code]['questions'].append({
                'new_q_id': db_questions[orig_q_id]['new_id'],
                'display_order': display_idx
            })

        db_questions[orig_q_id]['contents'][lang_id] = {
            'text': q_text, 'desc': desc, 'img': img_url, 'vid': vid_url
        }

        for opt_idx, ans in enumerate(q.get('answers', []), start=1):
            orig_opt_id = ans['id']
            opt_text, _ = parse_body(ans.get('body', []))

            if orig_opt_id not in db_questions[orig_q_id]['options']:
                db_questions[orig_q_id]['options'][orig_opt_id] = {
                    'new_id': next_option_id, 'display_order': opt_idx,
                    'is_correct': ans.get('check') == 1, 'contents': {}
                }
                next_option_id += 1

            pos_to_option[(template_code, display_idx, opt_idx)] = orig_opt_id
            db_questions[orig_q_id]['options'][orig_opt_id]['contents'][lang_id] = opt_text


def process_translation(lang_code, template_code, questions):
    lang_id = LANG_MAP[lang_code]['id']

    for display_idx, q in enumerate(questions, start=1):
        primary_q_id = pos_to_question.get((template_code, display_idx))
        if primary_q_id is None:
            continue

        q_data = db_questions[primary_q_id]

        if lang_id not in q_data['contents']:
            q_text, img_url = parse_body(q.get('body', []))
            q_data['contents'][lang_id] = {
                'text': q_text, 'desc': q.get('answer_description'),
                'img': img_url, 'vid': q.get('answer_video')
            }

        for opt_idx, ans in enumerate(q.get('answers', []), start=1):
            primary_o_id = pos_to_option.get((template_code, display_idx, opt_idx))
            if primary_o_id is None:
                continue

            if lang_id not in q_data['options'][primary_o_id]['contents']:
                opt_text, _ = parse_body(ans.get('body', []))
                q_data['options'][primary_o_id]['contents'][lang_id] = opt_text


def generate_sql_script(target_dir):
    os.makedirs(target_dir, exist_ok=True)

    with open(target_dir / "99_seed.sql", "w", encoding="utf-8") as f:
        f.write("BEGIN;\n\n")

        # Languages
        f.write("-- Languages\n")
        for lang in LANG_MAP.values():
            f.write(
                f"INSERT INTO languages (lang_id, lang_code, name)\n"
                f"VALUES ({lang['id']}, {escape_sql(lang['code'])}, "
                f"{escape_sql(lang['name'])});\n\n"
            )

        # Templates
        f.write("-- Templates\n")
        for code in sorted(db_templates.keys()):
            t = db_templates[code]
            f.write(
                f"INSERT INTO templates (template_id, code, status)\n"
                f"VALUES ({t['new_id']}, {escape_sql(code)}, 'A');\n\n"
            )

        # Questions
        f.write("-- Questions\n")
        for q in db_questions.values():
            f.write(
                f"INSERT INTO questions (question_id, status)\n"
                f"VALUES ({q['new_id']}, '{q['status']}');\n\n"
            )

        # Question contents
        f.write("-- Question contents\n")
        for q in db_questions.values():
            for lang_id, c in q['contents'].items():
                f.write(
                    f"INSERT INTO question_contents\n"
                    f"       (question_id, lang_id, question_text,\n"
                    f"        explanation_text, image_url, answer_video_url, status)\n"
                    f"VALUES ({q['new_id']}, {lang_id},\n"
                    f"        {escape_sql(c['text'])},\n"
                    f"        {escape_sql(c['desc'])},\n"
                    f"        {escape_sql(c['img'])},\n"
                    f"        {escape_sql(c['vid'])},\n"
                    f"        '{q['status']}');\n\n"
                )

        # Options
        f.write("-- Options\n")
        for q in db_questions.values():
            for opt in q['options'].values():
                is_correct = "TRUE" if opt['is_correct'] else "FALSE"
                f.write(
                    f"INSERT INTO options (option_id, question_id, display_order, is_correct)\n"
                    f"VALUES ({opt['new_id']}, {q['new_id']}, "
                    f"{opt['display_order']}, {is_correct});\n\n"
                )

        # Option contents
        f.write("-- Option contents\n")
        for q in db_questions.values():
            for opt in q['options'].values():
                for lang_id, text in opt['contents'].items():
                    f.write(
                        f"INSERT INTO option_contents (option_id, lang_id, option_text)\n"
                        f"VALUES ({opt['new_id']}, {lang_id}, {escape_sql(text)});\n\n"
                    )

        # Template questions
        f.write("-- Template questions\n")
        for code in sorted(db_templates.keys()):
            t = db_templates[code]
            for tq in t['questions']:
                f.write(
                    f"INSERT INTO template_questions (template_id, question_id, display_order)\n"
                    f"VALUES ({t['new_id']}, {tq['new_q_id']}, {tq['display_order']});\n\n"
                )

        f.write("COMMIT;\n\n")

        # Reset sequences (outside transaction)
        f.write("-- Reset sequences\n")
        f.write("SELECT setval('languages_sq', (SELECT COALESCE(MAX(lang_id), 1)    FROM languages));\n")
        f.write("SELECT setval('templates_sq', (SELECT COALESCE(MAX(template_id), 1) FROM templates));\n")
        f.write("SELECT setval('questions_sq', (SELECT COALESCE(MAX(question_id), 1) FROM questions));\n")
        f.write("SELECT setval('options_sq',   (SELECT COALESCE(MAX(option_id), 1)   FROM options));\n")


if __name__ == "__main__":
    project_root    = Path(__file__).resolve().parents[1]
    data_source_dir = project_root / "data" / "templates"
    target_dir      = project_root / "db" / "init"

    print("Parsing JSON files...")

    primary_dir = data_source_dir / PRIMARY_LANG
    if primary_dir.is_dir():
        for tf in sorted(primary_dir.glob("*.json"), key=lambda p: int(p.stem)):
            with open(tf, "r", encoding="utf-8") as f:
                process_primary(tf.stem, json.load(f).get('data', []))

    for lang_code in LANG_ORDER[1:]:
        lang_dir = data_source_dir / lang_code
        if not lang_dir.is_dir():
            print(f"  warning: skipping {lang_dir}")
            continue
        for tf in sorted(lang_dir.glob("*.json"), key=lambda p: int(p.stem)):
            with open(tf, "r", encoding="utf-8") as f:
                process_translation(lang_code, tf.stem, json.load(f).get('data', []))

    print(f"  {len(db_questions)} unique questions")
    print(f"  {len(db_templates)} templates")
    print(f"  {len(seen_template_questions)} template-question links")

    total_contents = sum(len(q['contents']) for q in db_questions.values())
    total_options  = sum(len(q['options']) for q in db_questions.values())
    print(f"  {total_contents} question translations")
    print(f"  {total_options} options")

    print("Generating SQL scripts...")
    generate_sql_script(target_dir)
    print(f"Done → {target_dir}/")