import json
import time
from pathlib import Path
from os import getenv
from dotenv import load_dotenv

import requests

load_dotenv()

USERNAME = getenv("ACCOUNT_USERNAME")
PASSWORD = getenv("ACCOUNT_PASSWORD")

if not USERNAME or not PASSWORD:
    print("=" * 60)
    print("!!! PLEASE SET ACCOUNT_USERNAME AND ACCOUNT_PASSWORD IN THE .env file !!!")
    print("=" * 60)
    raise SystemExit(1)


class TemplateDownloader:
    BASE_URL = getenv("API_HOST")
    LOGIN_URL = f"{BASE_URL}/login"
    TEMPLATE_URL = getenv("TEMPLATE_ENDPOINT")

    LANG_CODES = {
        1: 'uz',
        2: 'ru',
        3: 'cyrl'
    }

    def __init__(self):
        self.session = requests.Session()
        self.access_token: str | None = None

    def login(self) -> None:
        files = {
            "username": (None, USERNAME),
            "password": (None, PASSWORD),
        }
        r = self.session.post(self.LOGIN_URL, files=files, timeout=30)
        r.raise_for_status()
        self.access_token = r.json()["access_token"]
        print("✓ Logged in successfully")

    def _headers(self) -> dict:
        return {"Authorization": f"Bearer {self.access_token}"}

    def fetch_template(self, idx: int, lang_code: int) -> dict:
        url = f"{self.TEMPLATE_URL}/{idx}/{lang_code}"
        
        r = self.session.get(url, headers=self._headers(), timeout=30)
        
        # Auto-refresh token if expired
        if r.status_code == 401:
            print(f"   → Token expired at template {idx}, re-logging in...")
            self.login()
            r = self.session.get(url, headers=self._headers(), timeout=30)
        
        r.raise_for_status()
        return r.json()

    def download_all(
        self,
        lang_code: int = 1,
        template_range: range = range(1, 61),
        output_dir: str = "templates",
    ) -> None:
        save_path = Path(output_dir) / self.LANG_CODES[lang_code]
        save_path.mkdir(parents=True, exist_ok=True)

        # Initial login
        if not self.access_token:
            self.login()

        success_count = 0
        for idx in template_range:
            print(f"Downloading {idx:02d}/60 ... ", end="", flush=True)
            
            try:
                data = self.fetch_template(idx, lang_code)
                file_path = save_path / f"{idx:02d}.json"
                
                with open(file_path, "w", encoding="utf-8") as f:
                    json.dump(data, f, ensure_ascii=False, indent=2)
                
                print("✓")
                success_count += 1
                time.sleep(1) # to prevent too many requests error
            except Exception as e:
                print(f"✗ (failed: {e})")

        print("\n" + "=" * 60)
        print(f"DONE! Saved {success_count}/{len(template_range)} templates")
        print(f"Location: {save_path.resolve()}")
        print("=" * 60)


if __name__ == "__main__":
    project_root = Path(__file__).resolve().parents[2]
    target_dir = project_root / "data" / "templates"

    downloader = TemplateDownloader()
    print(f"Saving files to: {target_dir}")
    
    # Change lang_code here if you ever need another language
    # 1 = Uzbek Latin, 2 = Russian, 3 = Uzbek Cyrillic
    downloader.download_all(lang_code=1, template_range=range(1, 61), output_dir=target_dir)
    downloader.download_all(lang_code=2, template_range=range(1, 61), output_dir=target_dir)
    downloader.download_all(lang_code=3, template_range=range(1, 61), output_dir=target_dir)