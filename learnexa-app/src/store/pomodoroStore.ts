import { create } from 'zustand';

interface PomodoroState {
  timeLeft: number;
  isRunning: boolean;
  isBreak: boolean;
  workTime: number; // minutes
  breakTime: number; // minutes
  longBreakTime: number; // minutes
  pomodoroCount: number;
  totalPomodoros: number;
  startTimer: () => void;
  pauseTimer: () => void;
  resetTimer: () => void;
  setWorkTime: (minutes: number) => void;
  setBreakTime: (minutes: number) => void;
  setLongBreakTime: (minutes: number) => void;
  tick: () => void;
}

export const usePomodoroStore = create<PomodoroState>((set, get) => ({
  timeLeft: 25 * 60, // 25 minutes in seconds
  isRunning: false,
  isBreak: false,
  workTime: 25,
  breakTime: 5,
  longBreakTime: 15,
  pomodoroCount: 0,
  totalPomodoros: 0,

  startTimer: () => {
    set({ isRunning: true });
  },

  pauseTimer: () => {
    set({ isRunning: false });
  },

  resetTimer: () => {
    const { workTime, isBreak, breakTime } = get();
    set({
      timeLeft: isBreak ? breakTime * 60 : workTime * 60,
      isRunning: false,
    });
  },

  setWorkTime: (minutes) => {
    const { isBreak } = get();
    set({
      workTime: minutes,
      timeLeft: isBreak ? get().timeLeft : minutes * 60,
    });
  },

  setBreakTime: (minutes) => {
    const { isBreak } = get();
    set({
      breakTime: minutes,
      timeLeft: isBreak ? minutes * 60 : get().timeLeft,
    });
  },

  setLongBreakTime: (minutes) => {
    set({ longBreakTime: minutes });
  },

  tick: () => {
    const state = get();
    if (!state.isRunning) return;

    if (state.timeLeft > 0) {
      set({ timeLeft: state.timeLeft - 1 });
    } else {
      // Timer finished
      if (!state.isBreak) {
        // Work session finished, start break
        const newPomodoroCount = state.pomodoroCount + 1;
        const isLongBreak = newPomodoroCount % 4 === 0;
        const breakDuration = isLongBreak ? state.longBreakTime : state.breakTime;
        
        set({
          isBreak: true,
          timeLeft: breakDuration * 60,
          pomodoroCount: newPomodoroCount,
          totalPomodoros: state.totalPomodoros + 1,
          isRunning: false,
        });
      } else {
        // Break finished, start work session
        set({
          isBreak: false,
          timeLeft: state.workTime * 60,
          isRunning: false,
        });
      }
    }
  },
}));