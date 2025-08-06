import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { router } from 'expo-router';
import { useAuthStore } from '../../src/store/authStore';
import { useCourseStore } from '../../src/store/courseStore';
import { usePomodoroStore } from '../../src/store/pomodoroStore';

const { width } = Dimensions.get('window');

export default function HomeScreen() {
  const { user } = useAuthStore();
  const { courses } = useCourseStore();
  const { totalPomodoros } = usePomodoroStore();

  const quickActions = [
    {
      title: 'Yeni Ders',
      subtitle: 'Ders oluştur',
      color: ['#667eea', '#764ba2'],
      onPress: () => router.push('/(tabs)/courses'),
    },
    {
      title: 'Pomodoro',
      subtitle: 'Odaklan',
      color: ['#f093fb', '#f5576c'],
      onPress: () => router.push('/(tabs)/pomodoro'),
    },
    {
      title: 'Sıralama',
      subtitle: 'Durumunu gör',
      color: ['#4facfe', '#00f2fe'],
      onPress: () => router.push('/(tabs)/ranking'),
    },
  ];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>Merhaba, {user?.name}! 👋</Text>
            <Text style={styles.subtitle}>Bugün ne öğrenmeye hazırsın?</Text>
          </View>
        </View>

        {/* Stats Cards */}
        <View style={styles.statsContainer}>
          <View style={styles.statCard}>
            <Text style={styles.statNumber}>{courses.length}</Text>
            <Text style={styles.statLabel}>Aktif Ders</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statNumber}>{totalPomodoros}</Text>
            <Text style={styles.statLabel}>Pomodoro</Text>
          </View>
          <View style={styles.statCard}>
            <Text style={styles.statNumber}>{user?.points || 0}</Text>
            <Text style={styles.statLabel}>Puan</Text>
          </View>
        </View>

        {/* Quick Actions */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Hızlı Erişim</Text>
          <View style={styles.actionsGrid}>
            {quickActions.map((action, index) => (
              <TouchableOpacity
                key={index}
                style={styles.actionCard}
                onPress={action.onPress}
              >
                <LinearGradient
                  colors={action.color}
                  style={styles.actionGradient}
                  start={{ x: 0, y: 0 }}
                  end={{ x: 1, y: 1 }}
                >
                  <Text style={styles.actionTitle}>{action.title}</Text>
                  <Text style={styles.actionSubtitle}>{action.subtitle}</Text>
                </LinearGradient>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Recent Courses */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Son Dersler</Text>
          {courses.length === 0 ? (
            <View style={styles.emptyState}>
              <Text style={styles.emptyText}>Henüz ders oluşturmadın</Text>
              <TouchableOpacity
                style={styles.createButton}
                onPress={() => router.push('/(tabs)/courses')}
              >
                <Text style={styles.createButtonText}>İlk Dersini Oluştur</Text>
              </TouchableOpacity>
            </View>
          ) : (
            <View style={styles.coursesList}>
              {courses.slice(0, 3).map((course) => (
                <TouchableOpacity key={course.id} style={styles.courseCard}>
                  <View style={styles.courseInfo}>
                    <Text style={styles.courseTitle}>{course.title}</Text>
                    <Text style={styles.courseDescription} numberOfLines={2}>
                      {course.description}
                    </Text>
                  </View>
                  <View style={styles.courseBadge}>
                    <Text style={styles.courseBadgeText}>
                      {new Date(course.createdAt).toLocaleDateString('tr-TR')}
                    </Text>
                  </View>
                </TouchableOpacity>
              ))}
            </View>
          )}
        </View>

        {/* Daily Motivation */}
        <View style={styles.motivationCard}>
          <LinearGradient
            colors={['#ff9a9e', '#fecfef']}
            style={styles.motivationGradient}
          >
            <Text style={styles.motivationTitle}>Günün Motivasyonu</Text>
            <Text style={styles.motivationText}>
              "Başarı, günlük küçük çabalarının toplamıdır." 💪
            </Text>
          </LinearGradient>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8fafc',
  },
  scrollView: {
    flex: 1,
  },
  header: {
    padding: 24,
    paddingTop: 16,
  },
  greeting: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 16,
    color: '#6b7280',
  },
  statsContainer: {
    flexDirection: 'row',
    paddingHorizontal: 24,
    marginBottom: 24,
    gap: 12,
  },
  statCard: {
    flex: 1,
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#4f46e5',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 12,
    color: '#6b7280',
  },
  section: {
    paddingHorizontal: 24,
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 16,
  },
  actionsGrid: {
    flexDirection: 'row',
    gap: 12,
  },
  actionCard: {
    flex: 1,
    height: 100,
  },
  actionGradient: {
    flex: 1,
    borderRadius: 12,
    padding: 16,
    justifyContent: 'center',
  },
  actionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 4,
  },
  actionSubtitle: {
    fontSize: 12,
    color: 'rgba(255, 255, 255, 0.8)',
  },
  emptyState: {
    alignItems: 'center',
    padding: 32,
    backgroundColor: 'white',
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  emptyText: {
    fontSize: 16,
    color: '#6b7280',
    marginBottom: 16,
  },
  createButton: {
    backgroundColor: '#4f46e5',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 8,
  },
  createButtonText: {
    color: 'white',
    fontWeight: '600',
  },
  coursesList: {
    gap: 12,
  },
  courseCard: {
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  courseInfo: {
    flex: 1,
  },
  courseTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#1f2937',
    marginBottom: 4,
  },
  courseDescription: {
    fontSize: 14,
    color: '#6b7280',
  },
  courseBadge: {
    backgroundColor: '#f3f4f6',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
  },
  courseBadgeText: {
    fontSize: 12,
    color: '#6b7280',
  },
  motivationCard: {
    marginHorizontal: 24,
    marginBottom: 100,
  },
  motivationGradient: {
    padding: 20,
    borderRadius: 12,
    alignItems: 'center',
  },
  motivationTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 8,
  },
  motivationText: {
    fontSize: 14,
    color: 'white',
    textAlign: 'center',
    lineHeight: 20,
  },
});