import React, { useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  Dimensions,
  Vibration,
  Alert,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { usePomodoroStore } from '../../src/store/pomodoroStore';

const { width } = Dimensions.get('window');

export default function PomodoroScreen() {
  const {
    timeLeft,
    isRunning,
    isBreak,
    workTime,
    breakTime,
    pomodoroCount,
    totalPomodoros,
    startTimer,
    pauseTimer,
    resetTimer,
    tick,
  } = usePomodoroStore();

  useEffect(() => {
    let interval: NodeJS.Timeout;
    
    if (isRunning) {
      interval = setInterval(() => {
        tick();
      }, 1000);
    }

    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, [isRunning, tick]);

  // Check if timer finished
  useEffect(() => {
    if (timeLeft === 0 && !isRunning) {
      Vibration.vibrate([500, 200, 500]);
      
      if (isBreak) {
        Alert.alert(
          '⏰ Mola Bitti!',
          'Mola süren tamamlandı. Çalışmaya devam etmeye hazır mısın?',
          [{ text: 'Başla', onPress: startTimer }]
        );
      } else {
        const isLongBreak = (pomodoroCount + 1) % 4 === 0;
        Alert.alert(
          '🎉 Pomodoro Tamamlandı!',
          `Harika! ${isLongBreak ? 'Uzun bir mola' : 'Kısa bir mola'} zamanı.`,
          [{ text: 'Molaya Başla', onPress: startTimer }]
        );
      }
    }
  }, [timeLeft, isRunning, isBreak, pomodoroCount, startTimer]);

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  const getProgressPercentage = () => {
    const totalTime = isBreak ? breakTime * 60 : workTime * 60;
    return ((totalTime - timeLeft) / totalTime) * 100;
  };

  const handleStartPause = () => {
    if (isRunning) {
      pauseTimer();
    } else {
      startTimer();
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <LinearGradient
        colors={isBreak ? ['#4facfe', '#00f2fe'] : ['#667eea', '#764ba2']}
        style={styles.gradient}
      >
        <View style={styles.header}>
          <Text style={styles.headerTitle}>
            {isBreak ? '☕ Mola Zamanı' : '💪 Çalışma Zamanı'}
          </Text>
          <Text style={styles.headerSubtitle}>
            {isBreak 
              ? `${pomodoroCount % 4 === 0 ? 'Uzun' : 'Kısa'} mola` 
              : 'Odaklan ve çalış'
            }
          </Text>
        </View>

        <View style={styles.timerContainer}>
          <View style={styles.circularProgress}>
            <LinearGradient
              colors={['rgba(255,255,255,0.3)', 'rgba(255,255,255,0.1)']}
              style={styles.progressBackground}
            >
              <View style={styles.timerInner}>
                <Text style={styles.timerText}>{formatTime(timeLeft)}</Text>
                <Text style={styles.timerLabel}>
                  {isBreak ? 'mola' : 'çalışma'}
                </Text>
              </View>
            </LinearGradient>
            
            {/* Progress indicator */}
            <View 
              style={[
                styles.progressIndicator,
                { transform: [{ rotate: `${(getProgressPercentage() * 3.6) - 90}deg` }] }
              ]} 
            />
          </View>
        </View>

        <View style={styles.controls}>
          <TouchableOpacity
            style={[styles.controlButton, styles.resetButton]}
            onPress={resetTimer}
          >
            <Text style={styles.controlButtonText}>↻</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.controlButton, styles.playPauseButton]}
            onPress={handleStartPause}
          >
            <LinearGradient
              colors={['rgba(255,255,255,0.9)', 'rgba(255,255,255,0.7)']}
              style={styles.playPauseGradient}
            >
              <Text style={styles.playPauseText}>
                {isRunning ? '⏸️' : '▶️'}
              </Text>
            </LinearGradient>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.controlButton, styles.resetButton]}
            onPress={() => {
              // Skip current session
              if (isBreak) {
                resetTimer();
              } else {
                resetTimer();
              }
            }}
          >
            <Text style={styles.controlButtonText}>⏭️</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.stats}>
          <View style={styles.statBox}>
            <Text style={styles.statNumber}>{pomodoroCount}</Text>
            <Text style={styles.statLabel}>Bu Oturum</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={styles.statNumber}>{totalPomodoros}</Text>
            <Text style={styles.statLabel}>Toplam</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={styles.statNumber}>{Math.floor(pomodoroCount / 4)}</Text>
            <Text style={styles.statLabel}>Tamamlanan Set</Text>
          </View>
        </View>

        <View style={styles.settings}>
          <Text style={styles.settingsTitle}>Ayarlar</Text>
          <View style={styles.settingsRow}>
            <View style={styles.settingItem}>
              <Text style={styles.settingLabel}>Çalışma</Text>
              <Text style={styles.settingValue}>{workTime} dk</Text>
            </View>
            <View style={styles.settingItem}>
              <Text style={styles.settingLabel}>Kısa Mola</Text>
              <Text style={styles.settingValue}>{breakTime} dk</Text>
            </View>
            <View style={styles.settingItem}>
              <Text style={styles.settingLabel}>Uzun Mola</Text>
              <Text style={styles.settingValue}>15 dk</Text>
            </View>
          </View>
        </View>
      </LinearGradient>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  gradient: {
    flex: 1,
    paddingHorizontal: 24,
  },
  header: {
    alignItems: 'center',
    paddingTop: 20,
    paddingBottom: 40,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 8,
  },
  headerSubtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
  },
  timerContainer: {
    alignItems: 'center',
    marginBottom: 40,
  },
  circularProgress: {
    width: width * 0.6,
    height: width * 0.6,
    position: 'relative',
  },
  progressBackground: {
    width: '100%',
    height: '100%',
    borderRadius: width * 0.3,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: 'rgba(255, 255, 255, 0.3)',
  },
  timerInner: {
    alignItems: 'center',
  },
  timerText: {
    fontSize: 48,
    fontWeight: 'bold',
    color: 'white',
    fontFamily: 'SpaceMono',
  },
  timerLabel: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    marginTop: 8,
  },
  progressIndicator: {
    position: 'absolute',
    top: -2,
    left: '50%',
    width: 4,
    height: width * 0.3,
    backgroundColor: 'white',
    borderRadius: 2,
    transformOrigin: `2px ${width * 0.3}px`,
  },
  controls: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 40,
    gap: 20,
  },
  controlButton: {
    width: 60,
    height: 60,
    borderRadius: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  resetButton: {
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
  },
  controlButtonText: {
    fontSize: 24,
    color: 'white',
  },
  playPauseButton: {
    width: 80,
    height: 80,
    borderRadius: 40,
  },
  playPauseGradient: {
    width: '100%',
    height: '100%',
    borderRadius: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },
  playPauseText: {
    fontSize: 32,
  },
  stats: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: 40,
  },
  statBox: {
    alignItems: 'center',
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    paddingVertical: 16,
    paddingHorizontal: 20,
    borderRadius: 12,
    minWidth: 80,
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 12,
    color: 'rgba(255, 255, 255, 0.8)',
    textAlign: 'center',
  },
  settings: {
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    borderRadius: 12,
    padding: 20,
    marginBottom: 40,
  },
  settingsTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 16,
    textAlign: 'center',
  },
  settingsRow: {
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
  settingItem: {
    alignItems: 'center',
  },
  settingLabel: {
    fontSize: 14,
    color: 'rgba(255, 255, 255, 0.8)',
    marginBottom: 4,
  },
  settingValue: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'white',
  },
});