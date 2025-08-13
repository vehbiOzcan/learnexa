// Mock Gemini API Service
// Gerçek API çağrıları backend'den yapılacak

export class GeminiService {
  // Backend API endpoint'i (production'da gerçek URL olacak)
  private baseURL = 'http://localhost:3000/api'; // Backend server URL'i

  async generateSummary(pdfContent: string): Promise<string> {
    try {
      // Backend'e API çağrısı yapılacak
      const response = await fetch(`${this.baseURL}/generate-summary`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ pdfContent }),
      });

      if (!response.ok) {
        throw new Error('Backend API hatası');
      }

      const data = await response.json();
      return data.summary;
    } catch (error) {
      console.error('Backend API Özet Hatası:', error);
      
      // Mock response (backend hazır olmadığında)
      await new Promise(resolve => setTimeout(resolve, 2000)); // 2 saniye bekleme simülasyonu
      
      return `📚 DERS ÖZETİ

🎯 Ana Konular:
• Temel kavramlar ve tanımlar
• Pratik uygulamalar ve örnekler
• İleri seviye konular ve teoriler

🔑 Önemli Kavramlar:
• Kavram 1: Temel prensiplerin anlaşılması
• Kavram 2: Pratik uygulamalara odaklanma
• Kavram 3: Teorik bilgilerin pekiştirilmesi

📝 Kritik Noktalar:
• Düzenli tekrar yapılması gerekiyor
• Pratik örneklerin çözülmesi önemli
• Teorik bilgilerin uygulamayla desteklenmesi

💡 Çalışma Önerileri:
• Günlük çalışma programı oluştur
• Flash kartlar kullanarak tekrar yap
• Konu testleri çözerek bilgini pekiştir

Bu özet backend entegrasyonu tamamlandığında gerçek Gemini AI tarafından oluşturulacak.`;
    }
  }

  async generateFlashCards(content: string, count: number = 10): Promise<Array<{question: string, answer: string}>> {
    try {
      // Backend'e API çağrısı yapılacak
      const response = await fetch(`${this.baseURL}/generate-flashcards`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content, count }),
      });

      if (!response.ok) {
        throw new Error('Backend API hatası');
      }

      const data = await response.json();
      return data.flashCards;
    } catch (error) {
      console.error('Backend API Flash Card Hatası:', error);
      
      // Mock response (backend hazır olmadığında)
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      return this.generateMockFlashCards(count);
    }
  }

  async generateStudyPlan(content: string, targetWeeks: number = 4): Promise<{
    weeklyGoals: string[];
    dailyTasks: { [key: string]: string[] };
    estimatedHours: number;
  }> {
    try {
      // Backend'e API çağrısı yapılacak
      const response = await fetch(`${this.baseURL}/generate-study-plan`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content, targetWeeks }),
      });

      if (!response.ok) {
        throw new Error('Backend API hatası');
      }

      const data = await response.json();
      return data.studyPlan;
    } catch (error) {
      console.error('Backend API Çalışma Planı Hatası:', error);
      
      // Mock response (backend hazır olmadığında)
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      return this.generateMockStudyPlan(targetWeeks);
    }
  }

  private generateMockFlashCards(count: number): Array<{question: string, answer: string}> {
    const mockCards = [];
    
    for (let i = 0; i < Math.min(count, 10); i++) {
      mockCards.push({
        question: `Soru ${i + 1}: Bu konuyla ilgili önemli bir kavram nedir?`,
        answer: `Cevap ${i + 1}: Detaylı açıklama ve örneklerle birlikte konunun ana noktaları.`
      });
    }
    
    return mockCards;
  }

  private generateMockStudyPlan(weeks: number) {
    return {
      weeklyGoals: Array.from({length: weeks}, (_, i) => 
        `${i + 1}. Hafta: Temel konuları öğren ve pekiştir`
      ),
      dailyTasks: {
        'Pazartesi': ['Yeni konu okuması (45 dk)', 'Flash kart tekrarı (30 dk)'],
        'Salı': ['Problem çözme (60 dk)', 'Önceki konuları tekrar (30 dk)'],
        'Çarşamba': ['Yeni konu okuması (45 dk)', 'Flash kart tekrarı (30 dk)'],
        'Perşembe': ['Problem çözme (60 dk)', 'Önceki konuları tekrar (30 dk)'],
        'Cuma': ['Yeni konu okuması (45 dk)', 'Flash kart tekrarı (30 dk)'],
        'Cumartesi': ['Genel tekrar (90 dk)', 'Zayıf konuları güçlendir'],
        'Pazar': ['Dinlenme günü', 'Haftalık değerlendirme (30 dk)'],
      },
      estimatedHours: weeks * 6
    };
  }
}

export const geminiService = new GeminiService();