import { create } from 'zustand';

export interface Course {
  id: string;
  title: string;
  description: string;
  createdAt: string;
  pdfUrl?: string;
  summary?: string;
  flashCards?: FlashCard[];
  studyPlan?: StudyPlan;
}

export interface FlashCard {
  id: string;
  question: string;
  answer: string;
  isReviewed: boolean;
}

export interface StudyPlan {
  id: string;
  weeklyGoals: string[];
  dailyTasks: { [key: string]: string[] };
  estimatedHours: number;
}

interface CourseState {
  courses: Course[];
  currentCourse: Course | null;
  addCourse: (course: Omit<Course, 'id' | 'createdAt'>) => void;
  updateCourse: (id: string, updates: Partial<Course>) => void;
  setCurrentCourse: (course: Course) => void;
  generateSummary: (courseId: string, pdfContent: string) => Promise<void>;
  generateFlashCards: (courseId: string, count: number) => Promise<void>;
  generateStudyPlan: (courseId: string) => Promise<void>;
}

export const useCourseStore = create<CourseState>((set, get) => ({
  courses: [],
  currentCourse: null,

  addCourse: (courseData) => {
    const newCourse: Course = {
      ...courseData,
      id: Date.now().toString(),
      createdAt: new Date().toISOString(),
    };
    set((state) => ({
      courses: [...state.courses, newCourse],
    }));
  },

  updateCourse: (id, updates) => {
    set((state) => ({
      courses: state.courses.map((course) =>
        course.id === id ? { ...course, ...updates } : course
      ),
    }));
  },

  setCurrentCourse: (course) => {
    set({ currentCourse: course });
  },

  generateSummary: async (courseId, pdfContent) => {
    // Simulated Gemini API call
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    const mockSummary = `Bu ders notlarının özeti:
    
• Ana konu başlıkları ve temel kavramlar
• Önemli formüller ve tanımlar  
• Kritik noktalar ve dikkat edilmesi gerekenler
• Pratik uygulamalar ve örnekler
    
Toplam ${Math.floor(Math.random() * 50 + 20)} sayfa içerik özetlenmiştir.`;

    get().updateCourse(courseId, { summary: mockSummary });
  },

  generateFlashCards: async (courseId, count) => {
    // Simulated Gemini API call
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    const mockFlashCards: FlashCard[] = Array.from({ length: count }, (_, i) => ({
      id: `card-${i + 1}`,
      question: `Soru ${i + 1}: Bu konuyla ilgili önemli bir kavram nedir?`,
      answer: `Cevap ${i + 1}: Detaylı açıklama ve örneklerle birlikte...`,
      isReviewed: false,
    }));

    get().updateCourse(courseId, { flashCards: mockFlashCards });
  },

  generateStudyPlan: async (courseId) => {
    // Simulated Gemini API call
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    const mockStudyPlan: StudyPlan = {
      id: `plan-${courseId}`,
      weeklyGoals: [
        '1. Hafta: Temel kavramları öğren',
        '2. Hafta: Pratik alıştırmalar yap',
        '3. Hafta: Flash kartları tekrarla',
        '4. Hafta: Genel tekrar ve değerlendirme',
      ],
      dailyTasks: {
        'Pazartesi': ['Flash kart tekrarı (30 dk)', 'Yeni konu okuması (45 dk)'],
        'Salı': ['Problem çözme (60 dk)', 'Önceki konuları tekrar (30 dk)'],
        'Çarşamba': ['Flash kart tekrarı (30 dk)', 'Yeni konu okuması (45 dk)'],
        'Perşembe': ['Problem çözme (60 dk)', 'Önceki konuları tekrar (30 dk)'],
        'Cuma': ['Flash kart tekrarı (30 dk)', 'Yeni konu okuması (45 dk)'],
        'Cumartesi': ['Genel tekrar (90 dk)', 'Zayıf konuları güçlendir'],
        'Pazar': ['Dinlenme günü', 'Haftalık değerlendirme (30 dk)'],
      },
      estimatedHours: 25,
    };

    get().updateCourse(courseId, { studyPlan: mockStudyPlan });
  },
}));