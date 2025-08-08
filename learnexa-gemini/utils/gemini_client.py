def list_gemini_models():
    try:
        models = genai.list_models()
        return [model.name for model in models]
    except Exception as e:
        return f"Model listesi alınamadı: {str(e)}"
import os
from dotenv import load_dotenv
import google.generativeai as genai 

load_dotenv()
API_KEY = os.getenv("GEMINI_API_KEY")

genai.configure(api_key=API_KEY)


def chat_with_gemini(prompt: str) -> str:
    try:
        chat = genai.GenerativeModel('models/gemini-2.5-pro')
        response = chat.generate_content(prompt)
        return response.text
    except Exception as e:
        return f"Gemini API hatası: {str(e)}"
