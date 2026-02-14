import json
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
TEMPLATES_DIR = PROJECT_ROOT / "data" / "templates"
MEDIA_DIR = PROJECT_ROOT / "data" / "media"

TRAVERSE_DIRS = ["uz", "ru", "cyrl"]

success_count = 0
for lang_code in TRAVERSE_DIRS:
    templates_path = TEMPLATES_DIR / lang_code
    
    for idx in range(1, 61):
        template_path = templates_path / f"{idx:02d}.json"
        # To do

        try:
            with open(template_path, "r", encoding="utf-8") as file:
                data = json.load(file)
                success_count += 1
        except Exception as e:
            print(f"✗ (failed: {e})")

print(success_count)