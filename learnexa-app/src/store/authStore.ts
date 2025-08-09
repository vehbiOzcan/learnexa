import { create } from 'zustand';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_BASE_URL = 'http://192.168.1.40:8080/api';

interface User {
  id: number;
  username: string;
  fullname: string;
  role: string;
  email?: string;
}

interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

interface AuthState {
  user: User | null;
  tokens: AuthTokens | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  register: (name: string, email: string, password: string) => Promise<boolean>;
  logout: () => Promise<void>;
  loadUser: () => Promise<void>;
  refreshAccessToken: () => Promise<boolean>;
}

//  API requests için yardımcı metodumuz bu
const apiRequest = async (endpoint: string, options: RequestInit = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;

  const defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    ...options.headers,
  };

  console.log('API Request:', {
    url,
    method: options.method || 'GET',
    headers: defaultHeaders,
    body: options.body
  });

  try {
    const response = await fetch(url, {
      ...options,
      headers: defaultHeaders,
    });

    console.log('API Response Status:', response.status, response.statusText);
    console.log('API Response Headers:', response.headers);


    const contentType = response.headers.get('content-type');
    let data;

    if (contentType && contentType.includes('application/json')) {
      data = await response.json();
    } else {

      const textResponse = await response.text();
      console.log('Non-JSON Response:', textResponse);
      throw new Error(`Server returned non-JSON response: ${response.status} ${response.statusText}`);
    }

    console.log('API Response Data:', data);

    if (!response.ok) {
      // Backend validation hataları
      let errorMessage = `HTTP Error: ${response.status}`;

      if (data?.exceptionInfo?.message) {
        const validationErrors = data.exceptionInfo.message;
        const errorMessages = [];

        // Validation hatalarını parse
        for (const [field, errors] of Object.entries(validationErrors)) {
          if (Array.isArray(errors)) {
            errorMessages.push(`${field}: ${errors.join(', ')}`);
          } else {
            errorMessages.push(`${field}: ${errors}`);
          }
        }

        if (errorMessages.length > 0) {
          errorMessage = errorMessages.join('\n');
        }
      } else if (data?.message) {
        errorMessage = data.message;
      } else if (data?.error) {
        errorMessage = data.error;
      }

      throw new Error(errorMessage);
    }

    return data;
  } catch (error: any) {
    console.error('API Request Error Details:', {
      message: error.message,
      name: error.name,
      stack: error.stack,
      url,
      endpoint
    });

    // Network error
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      throw new Error('Network connection failed. Please check your internet connection and server.');
    }

    throw error;
  }
};

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  tokens: null,
  isAuthenticated: false,
  isLoading: true,

  login: async (email: string, password: string) => {
    try {
      //Username'i emailden ayıkladık
      const username = email.split('@')[0];

      const response = await apiRequest('/auth/authenticate', {
        method: 'POST',
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (response.success) {
        const tokens: AuthTokens = {
          accessToken: response.data.accessToken,
          refreshToken: response.data.refreshToken,
        };

        const user: User = {
          id: response.data.user.id,
          username: response.data.user.username,
          fullname: response.data.user.fullname,
          role: response.data.user.role,
          email, // Email'i frontend'den ekle
        };

        //  tokens ve user data
        await AsyncStorage.setItem('tokens', JSON.stringify(tokens));
        await AsyncStorage.setItem('user', JSON.stringify(user));

        set({
          user,
          tokens,
          isAuthenticated: true
        });

        return true;
      }

      return false;
    } catch (error) {
      console.error('Login error:', error);
      return false;
    }
  },

  register: async (name: string, email: string, password: string) => {
    try {
      const response = await apiRequest('/auth/register', {
        method: 'POST',
        body: JSON.stringify({
          fullname: name,
          email,
          password,
        }),
      });

      if (response.success) {
        const user: User = {
          id: response.data.id,
          username: response.data.username,
          fullname: response.data.fullname,
          role: response.data.role,
          email,
        };

        // Register işleminden sonra token almıyoruz sadece user bilgisini saklarız çünkü zaten kullanıcının login yapması gerekecek
        await AsyncStorage.setItem('user', JSON.stringify(user));

        set({
          user,
          isAuthenticated: false, // Register sonrası login gerekli
          tokens: null
        });

        return true;
      }

      return false;
    } catch (error) {
      console.error('Register error:', error);
      return false;
    }
  },

  logout: async () => {
    try {
      await AsyncStorage.multiRemove(['user', 'tokens']);
      set({
        user: null,
        tokens: null,
        isAuthenticated: false
      });
    } catch (error) {
      console.error('Logout error:', error);
    }
  },

  loadUser: async () => {
    try {
      const [userData, tokensData] = await AsyncStorage.multiGet(['user', 'tokens']);

      const user = userData[1] ? JSON.parse(userData[1]) : null;
      const tokens = tokensData[1] ? JSON.parse(tokensData[1]) : null;

      if (user && tokens) {

        const { refreshAccessToken } = get();


        const isTokenValid = await refreshAccessToken();

        if (isTokenValid) {
          set({
            user,
            tokens,
            isAuthenticated: true,
            isLoading: false
          });
        } else {

          await AsyncStorage.multiRemove(['user', 'tokens']);
          set({
            user: null,
            tokens: null,
            isAuthenticated: false,
            isLoading: false
          });
        }
      } else {
        set({ isLoading: false });
      }
    } catch (error) {
      console.error('Load user error:', error);
      set({ isLoading: false });
    }
  },

  refreshAccessToken: async () => {
    try {
      const { tokens } = get();

      if (!tokens?.refreshToken) {
        return false;
      }


      const payload = JSON.parse(atob(tokens.accessToken.split('.')[1]));
      const currentTime = Date.now() / 1000;


      if (payload.exp > currentTime + 300) {
        return true;
      }


      return payload.exp > currentTime - 3600;

    } catch (error) {
      console.error('Refresh token error:', error);
      return false;
    }
  },
}));