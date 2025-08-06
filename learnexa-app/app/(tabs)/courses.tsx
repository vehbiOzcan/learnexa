import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Modal,
  Alert,
  FlatList,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { useCourseStore, Course } from '../../src/store/courseStore';
import * as DocumentPicker from 'expo-document-picker';

export default function CoursesScreen() {
  const { courses, addCourse, setCurrentCourse } = useCourseStore();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [newCourseTitle, setNewCourseTitle] = useState('');
  const [newCourseDescription, setNewCourseDescription] = useState('');

  const handleCreateCourse = () => {
    if (!newCourseTitle.trim()) {
      Alert.alert('Hata', 'Lütfen ders adını girin');
      return;
    }

    addCourse({
      title: newCourseTitle.trim(),
      description: newCourseDescription.trim(),
    });

    setNewCourseTitle('');
    setNewCourseDescription('');
    setIsModalVisible(false);
    Alert.alert('Başarılı', 'Ders başarıyla oluşturuldu!');
  };

  const handleCoursePress = (course: Course) => {
    setCurrentCourse(course);
    // Navigate to course details page (to be implemented)
  };

  const handleUploadPDF = async (course: Course) => {
    try {
      const result = await DocumentPicker.getDocumentAsync({
        type: 'application/pdf',
        copyToCacheDirectory: true,
      });

      if (result.canceled) return;

      // Here you would typically upload the file and get a URL
      // For now, we'll simulate with a mock URL
      const mockPdfUrl = `file://${result.assets[0].uri}`;
      
      // Generate summary with Gemini API (simulated)
      Alert.alert(
        'PDF Yüklendi',
        'PDF başarıyla yüklendi. Özet oluşturuluyor...',
        [
          {
            text: 'Tamam',
            onPress: () => {
              // Simulate Gemini API call
              setTimeout(() => {
                Alert.alert('Başarılı', 'Ders özeti oluşturuldu!');
              }, 2000);
            },
          },
        ]
      );
    } catch (error) {
      Alert.alert('Hata', 'PDF yüklenirken bir hata oluştu');
    }
  };

  const renderCourseCard = ({ item: course }: { item: Course }) => (
    <TouchableOpacity
      style={styles.courseCard}
      onPress={() => handleCoursePress(course)}
    >
      <View style={styles.courseHeader}>
        <Text style={styles.courseTitle}>{course.title}</Text>
        <Text style={styles.courseDate}>
          {new Date(course.createdAt).toLocaleDateString('tr-TR')}
        </Text>
      </View>
      <Text style={styles.courseDescription} numberOfLines={3}>
        {course.description}
      </Text>
      
      <View style={styles.courseActions}>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={() => handleUploadPDF(course)}
        >
          <Text style={styles.actionButtonText}>📄 PDF Yükle</Text>
        </TouchableOpacity>
        
        {course.summary && (
          <TouchableOpacity style={[styles.actionButton, styles.summaryButton]}>
            <Text style={styles.actionButtonText}>📝 Özet Hazır</Text>
          </TouchableOpacity>
        )}
        
        {course.flashCards && course.flashCards.length > 0 && (
          <TouchableOpacity style={[styles.actionButton, styles.flashCardButton]}>
            <Text style={styles.actionButtonText}>🔄 {course.flashCards.length} Kart</Text>
          </TouchableOpacity>
        )}
      </View>
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Derslerim</Text>
        <TouchableOpacity
          style={styles.addButton}
          onPress={() => setIsModalVisible(true)}
        >
          <LinearGradient
            colors={['#667eea', '#764ba2']}
            style={styles.addButtonGradient}
          >
            <Text style={styles.addButtonText}>+ Yeni Ders</Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>

      {courses.length === 0 ? (
        <View style={styles.emptyState}>
          <Text style={styles.emptyTitle}>Henüz ders oluşturmadın</Text>
          <Text style={styles.emptyDescription}>
            İlk dersini oluşturarak öğrenme yolculuğuna başla
          </Text>
          <TouchableOpacity
            style={styles.createFirstButton}
            onPress={() => setIsModalVisible(true)}
          >
            <Text style={styles.createFirstButtonText}>İlk Dersini Oluştur</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={courses}
          renderItem={renderCourseCard}
          keyExtractor={(item) => item.id}
          showsVerticalScrollIndicator={false}
          contentContainerStyle={styles.coursesList}
        />
      )}

      {/* Create Course Modal */}
      <Modal
        visible={isModalVisible}
        animationType="slide"
        presentationStyle="pageSheet"
      >
        <SafeAreaView style={styles.modalContainer}>
          <View style={styles.modalHeader}>
            <TouchableOpacity
              onPress={() => setIsModalVisible(false)}
              style={styles.cancelButton}
            >
              <Text style={styles.cancelButtonText}>İptal</Text>
            </TouchableOpacity>
            <Text style={styles.modalTitle}>Yeni Ders</Text>
            <TouchableOpacity
              onPress={handleCreateCourse}
              style={styles.saveButton}
            >
              <Text style={styles.saveButtonText}>Kaydet</Text>
            </TouchableOpacity>
          </View>

          <ScrollView style={styles.modalContent}>
            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Ders Adı *</Text>
              <TextInput
                style={styles.textInput}
                value={newCourseTitle}
                onChangeText={setNewCourseTitle}
                placeholder="Örn: Matematik Analiz"
                maxLength={50}
              />
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.inputLabel}>Açıklama</Text>
              <TextInput
                style={[styles.textInput, styles.textArea]}
                value={newCourseDescription}
                onChangeText={setNewCourseDescription}
                placeholder="Ders hakkında kısa açıklama..."
                multiline
                numberOfLines={4}
                maxLength={200}
              />
            </View>

            <View style={styles.helpBox}>
              <Text style={styles.helpTitle}>💡 İpucu</Text>
              <Text style={styles.helpText}>
                Dersi oluşturduktan sonra PDF notlarını yükleyebilir, 
                Gemini AI ile özet çıkarabilir ve flash kartlar oluşturabilirsin.
              </Text>
            </View>
          </ScrollView>
        </SafeAreaView>
      </Modal>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8fafc',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 24,
    paddingVertical: 16,
  },
  headerTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#1f2937',
  },
  addButton: {
    borderRadius: 8,
  },
  addButtonGradient: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 8,
  },
  addButtonText: {
    color: 'white',
    fontWeight: '600',
    fontSize: 14,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 48,
  },
  emptyTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 8,
    textAlign: 'center',
  },
  emptyDescription: {
    fontSize: 16,
    color: '#6b7280',
    textAlign: 'center',
    marginBottom: 32,
    lineHeight: 24,
  },
  createFirstButton: {
    backgroundColor: '#4f46e5',
    paddingHorizontal: 32,
    paddingVertical: 16,
    borderRadius: 12,
  },
  createFirstButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  coursesList: {
    padding: 24,
    paddingBottom: 100,
  },
  courseCard: {
    backgroundColor: 'white',
    borderRadius: 12,
    padding: 20,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 8,
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
    flex: 1,
    marginRight: 12,
  },
  courseDate: {
    fontSize: 12,
    color: '#6b7280',
    backgroundColor: '#f3f4f6',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
  },
  courseDescription: {
    fontSize: 14,
    color: '#6b7280',
    lineHeight: 20,
    marginBottom: 16,
  },
  courseActions: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  actionButton: {
    backgroundColor: '#f3f4f6',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
  },
  summaryButton: {
    backgroundColor: '#dbeafe',
  },
  flashCardButton: {
    backgroundColor: '#fef3c7',
  },
  actionButtonText: {
    fontSize: 12,
    color: '#374151',
    fontWeight: '500',
  },
  modalContainer: {
    flex: 1,
    backgroundColor: 'white',
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 24,
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#e5e7eb',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
  },
  cancelButton: {
    padding: 4,
  },
  cancelButtonText: {
    color: '#6b7280',
    fontSize: 16,
  },
  saveButton: {
    padding: 4,
  },
  saveButtonText: {
    color: '#4f46e5',
    fontSize: 16,
    fontWeight: '600',
  },
  modalContent: {
    flex: 1,
    padding: 24,
  },
  inputGroup: {
    marginBottom: 24,
  },
  inputLabel: {
    fontSize: 16,
    fontWeight: '600',
    color: '#374151',
    marginBottom: 8,
  },
  textInput: {
    borderWidth: 1,
    borderColor: '#d1d5db',
    borderRadius: 8,
    paddingHorizontal: 16,
    paddingVertical: 12,
    fontSize: 16,
    backgroundColor: 'white',
  },
  textArea: {
    height: 100,
    textAlignVertical: 'top',
  },
  helpBox: {
    backgroundColor: '#f0f9ff',
    padding: 16,
    borderRadius: 8,
    borderLeftWidth: 4,
    borderLeftColor: '#0ea5e9',
  },
  helpTitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0369a1',
    marginBottom: 4,
  },
  helpText: {
    fontSize: 14,
    color: '#0369a1',
    lineHeight: 20,
  },
});