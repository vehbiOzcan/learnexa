import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  TouchableOpacity,
  FlatList,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { useAuthStore } from '../../src/store/authStore';

interface RankUser {
  id: string;
  name: string;
  points: number;
  rank: number;
  avatar: string;
  level: string;
}

const mockUsers: RankUser[] = [
  { id: '1', name: 'Ali Yılmaz', points: 2450, rank: 1, avatar: '🥇', level: 'Master' },
  { id: '2', name: 'Ayşe Kaya', points: 2180, rank: 2, avatar: '🥈', level: 'Expert' },
  { id: '3', name: 'Mehmet Demir', points: 1950, rank: 3, avatar: '🥉', level: 'Advanced' },
  { id: '4', name: 'Fatma Çelik', points: 1720, rank: 4, avatar: '🏆', level: 'Intermediate' },
  { id: '5', name: 'Ahmet Özkan', points: 1580, rank: 5, avatar: '⭐', level: 'Intermediate' },
  { id: '6', name: 'Sen', points: 150, rank: 15, avatar: '👤', level: 'Beginner' },
  { id: '7', name: 'Zeynep Ateş', points: 1420, rank: 6, avatar: '⭐', level: 'Intermediate' },
  { id: '8', name: 'Burak Yıldız', points: 1350, rank: 7, avatar: '⭐', level: 'Intermediate' },
];

export default function RankingScreen() {
  const { user } = useAuthStore();
  
  const currentUser =  {
    id: user?.id || '6',
    name: user?.fullname || 'Sen',
    points: user?.points || 150,
    rank: user?.rank || 15,
    avatar: '👤',
    level: 'Beginner'
  };

  const getLevelColor = (level: string) => {
    switch (level) {
      case 'Master':
        return ['#67E5FF', '#68CAFF'];
      case 'Expert':
        return ['#FFD700', '#FFA500'];
      case 'Advanced':
        return ['#C0C0C0', '#A0A0A0'];
      case 'Intermediate':
        return ['#CD7F32', '#B8860B'];
      default:
        return ['#4ADE80', '#22C55E'];
    }
  };

  const renderRankItem = ({ item, index }: { item: RankUser; index: number }) => {
    const isCurrentUser = item.name === 'Sen' || item.id === user?.id;
    
    return (
      <TouchableOpacity
        style={[
          styles.rankItem,
          isCurrentUser && styles.currentUserItem
        ]}
      >
        <View style={styles.rankLeft}>
          <View style={[styles.rankNumber, isCurrentUser && styles.currentUserRank]}>
            <Text style={[styles.rankNumberText, isCurrentUser && styles.currentUserRankText]}>
              {item.rank}
            </Text>
          </View>
          <Text style={styles.avatar}>{item.avatar}</Text>
          <View style={styles.userInfo}>
            <Text style={[styles.userName, isCurrentUser && styles.currentUserName]}>
              {item.name}
            </Text>
            <LinearGradient
              colors={getLevelColor(item.level)}
              style={styles.levelBadge}
            >
              <Text style={styles.levelText}>{item.level}</Text>
            </LinearGradient>
          </View>
        </View>
        <View style={styles.rankRight}>
          <Text style={[styles.points, isCurrentUser && styles.currentUserPoints]}>
            {item.points.toLocaleString()}
          </Text>
          <Text style={styles.pointsLabel}>puan</Text>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Header */}
        <LinearGradient
          colors={['#667eea', '#764ba2']}
          style={styles.header}
        >
          <Text style={styles.headerTitle}>🏆 Sıralama</Text>
          <Text style={styles.headerSubtitle}>
            Öğrenme yolculuğunda nerede duruyorsun?
          </Text>
        </LinearGradient>

        {/* Current User Stats */}
        <View style={styles.userStatsContainer}>
          <LinearGradient
            colors={getLevelColor(currentUser.level)}
            style={styles.userStatsGradient}
          >
            <View style={styles.userStats}>
              <Text style={styles.userStatsAvatar}>{currentUser.avatar}</Text>
              <View style={styles.userStatsInfo}>
                <Text style={styles.userStatsName}>{currentUser.name}</Text>
                <Text style={styles.userStatsLevel}>{currentUser.level}</Text>
              </View>
              <View style={styles.userStatsNumbers}>
                <Text style={styles.userStatsRank}>#{currentUser.rank}</Text>
                <Text style={styles.userStatsPoints}>{currentUser.points} puan</Text>
              </View>
            </View>
          </LinearGradient>
        </View>

        {/* Level Progress */}
        <View style={styles.progressContainer}>
          <Text style={styles.progressTitle}>Seviye İlerlemesi</Text>
          <View style={styles.progressBar}>
            <LinearGradient
              colors={['#4ade80', '#22c55e']}
              style={[styles.progressFill, { width: '30%' }]}
            />
          </View>
          <View style={styles.progressInfo}>
            <Text style={styles.progressText}>Intermediate seviyesine 350 puan kaldı</Text>
          </View>
        </View>

        {/* Achievement Highlights */}
        <View style={styles.achievementsContainer}>
          <Text style={styles.achievementsTitle}>🎯 Bu Hafta</Text>
          <View style={styles.achievementsList}>
            <View style={styles.achievementItem}>
              <Text style={styles.achievementIcon}>🔥</Text>
              <Text style={styles.achievementText}>3 günlük seri!</Text>
            </View>
            <View style={styles.achievementItem}>
              <Text style={styles.achievementIcon}>📚</Text>
              <Text style={styles.achievementText}>2 ders tamamlandı</Text>
            </View>
            <View style={styles.achievementItem}>
              <Text style={styles.achievementIcon}>⏰</Text>
              <Text style={styles.achievementText}>5 pomodoro</Text>
            </View>
          </View>
        </View>

        {/* Ranking List */}
        <View style={styles.rankingContainer}>
          <Text style={styles.rankingTitle}>🏅 Genel Sıralama</Text>
          <FlatList
            data={mockUsers.sort((a, b) => a.rank - b.rank)}
            renderItem={renderRankItem}
            keyExtractor={(item) => item.id}
            scrollEnabled={false}
            style={styles.rankingList}
          />
        </View>

        {/* Motivational Section */}
        <View style={styles.motivationContainer}>
          <LinearGradient
            colors={['#ff9a9e', '#fecfef']}
            style={styles.motivationGradient}
          >
            <Text style={styles.motivationTitle}>💪 Motivasyon</Text>
            <Text style={styles.motivationText}>
              Her gün biraz daha ilerle! Küçük adımlar büyük başarılara götürür.
            </Text>
            <TouchableOpacity style={styles.motivationButton}>
              <Text style={styles.motivationButtonText}>Çalışmaya Başla</Text>
            </TouchableOpacity>
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
    paddingBottom: 32,
    alignItems: 'center',
  },
  headerTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 8,
  },
  headerSubtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    textAlign: 'center',
  },
  userStatsContainer: {
    paddingHorizontal: 24,
    marginTop: 24,
    marginBottom: 24,
  },
  userStatsGradient: {
    borderRadius: 16,
    padding: 2,
  },
  userStats: {
    backgroundColor: 'white',
    borderRadius: 14,
    padding: 20,
    flexDirection: 'row',
    alignItems: 'center',
  },
  userStatsAvatar: {
    fontSize: 48,
    marginRight: 16,
  },
  userStatsInfo: {
    flex: 1,
  },
  userStatsName: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 4,
  },
  userStatsLevel: {
    fontSize: 14,
    color: '#6b7280',
  },
  userStatsNumbers: {
    alignItems: 'flex-end',
  },
  userStatsRank: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#4f46e5',
    marginBottom: 4,
  },
  userStatsPoints: {
    fontSize: 14,
    color: '#6b7280',
  },
  progressContainer: {
    paddingHorizontal: 24,
    marginBottom: 24,
  },
  progressTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 12,
  },
  progressBar: {
    height: 8,
    backgroundColor: '#e5e7eb',
    borderRadius: 4,
    overflow: 'hidden',
    marginBottom: 8,
  },
  progressFill: {
    height: '100%',
    borderRadius: 4,
  },
  progressInfo: {
    alignItems: 'center',
  },
  progressText: {
    fontSize: 14,
    color: '#6b7280',
  },
  achievementsContainer: {
    paddingHorizontal: 24,
    marginBottom: 24,
  },
  achievementsTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 16,
  },
  achievementsList: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  achievementItem: {
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 12,
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  achievementIcon: {
    fontSize: 24,
    marginBottom: 8,
  },
  achievementText: {
    fontSize: 12,
    color: '#6b7280',
    textAlign: 'center',
  },
  rankingContainer: {
    paddingHorizontal: 24,
    marginBottom: 24,
  },
  rankingTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 16,
  },
  rankingList: {
    backgroundColor: 'white',
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  rankItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#f3f4f6',
  },
  currentUserItem: {
    backgroundColor: '#f0f9ff',
  },
  rankLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  rankNumber: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: '#f3f4f6',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  currentUserRank: {
    backgroundColor: '#4f46e5',
  },
  rankNumberText: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#6b7280',
  },
  currentUserRankText: {
    color: 'white',
  },
  avatar: {
    fontSize: 32,
    marginRight: 12,
  },
  userInfo: {
    flex: 1,
  },
  userName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#1f2937',
    marginBottom: 4,
  },
  currentUserName: {
    color: '#4f46e5',
  },
  levelBadge: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 6,
    alignSelf: 'flex-start',
  },
  levelText: {
    fontSize: 12,
    color: 'white',
    fontWeight: '600',
  },
  rankRight: {
    alignItems: 'flex-end',
  },
  points: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#1f2937',
    marginBottom: 2,
  },
  currentUserPoints: {
    color: '#4f46e5',
  },
  pointsLabel: {
    fontSize: 12,
    color: '#6b7280',
  },
  motivationContainer: {
    paddingHorizontal: 24,
    marginBottom: 100,
  },
  motivationGradient: {
    borderRadius: 16,
    padding: 24,
    alignItems: 'center',
  },
  motivationTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 12,
  },
  motivationText: {
    fontSize: 16,
    color: 'white',
    textAlign: 'center',
    lineHeight: 24,
    marginBottom: 20,
  },
  motivationButton: {
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 8,
  },
  motivationButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
});