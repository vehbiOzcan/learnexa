from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import JSONResponse
from utils.gemini_client import chat_with_gemini
import PyPDF2

app = FastAPI()

@app.post("/ask")
async def ask_gemini(
    prompt: str = Form(None),
    file: UploadFile = File(None)
):
    text = ""
    if file is not None:
        # PDF dosyasını okuyup ve metne çeviriyoruz
        try:
            contents = await file.read()
            from io import BytesIO
            pdf_reader = PyPDF2.PdfReader(BytesIO(contents))
            for page in pdf_reader.pages:
                text += page.extract_text() or ""
        except Exception as e:
            return JSONResponse(content={"error": f"PDF okunamadı: {str(e)}"}, status_code=400)
    elif prompt:
        text = prompt
    else:
        return JSONResponse(content={"error": "Prompt veya PDF dosyası gereklidir."}, status_code=400)

    # Gemini APIdan yanıt alma kısmı
    response = chat_with_gemini(text)
    return {"response": response}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.responses import JSONResponse
import fitz  # PyMuPDF
from utils.gemini_client import chat_with_gemini

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Gemini FastAPI aktif ve google-genai kullanıyor."}

@app.post("/text")
async def chat_text(prompt: str = Form(...)):
    cevap = chat_with_gemini(prompt)
    return JSONResponse(content={"soru": prompt, "cevap": cevap})

@app.post("/pdf")
async def chat_pdf(file: UploadFile = File(...)):
    try:
        pdf_bytes = await file.read()
        doc = fitz.open("pdf", pdf_bytes)
        full_text = ""
        for page in doc:
            full_text += page.get_text()

        if not full_text.strip():
            raise HTTPException(status_code=400, detail="PDF boş veya okunamıyor.")

        # API limitleri ve performans için max 3000 karakter alıyoruz
        soru_metni = full_text[:3000]
        cevap = chat_with_gemini(soru_metni)
        return JSONResponse(content={"pdf_ozet": cevap})
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
