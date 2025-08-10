/*
import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform,
  SafeAreaView,
} from 'react-native';
import { Link, router } from 'expo-router';
import { LinearGradient } from 'expo-linear-gradient';
import { useAuthStore } from '../../src/store/authStore';

export default function LoginScreen() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuthStore();

  const handleLogin = async () => {
    if (!email || !password) {
      Alert.alert('Hata', 'Lütfen tüm alanları doldurun');
      return;
    }

    setIsLoading(true);
    try {
      const success = await login(email, password);
      if (success) {
        router.replace('/(tabs)');
      } else {
        Alert.alert('Hata', 'Giriş başarısız. Lütfen bilgilerinizi kontrol edin.');
      }
    } catch (error) {
      Alert.alert('Hata', 'Bir hata oluştu. Lütfen tekrar deneyin.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <LinearGradient
        colors={['#667eea', '#764ba2']}
        style={styles.gradient}
      >
        <KeyboardAvoidingView
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
          style={styles.keyboardView}
        >
          <View style={styles.content}>
            <View style={styles.header}>
              <Text style={styles.title}>Learnexa</Text>
              <Text style={styles.subtitle}>Öğrenme yolculuğuna başla</Text>
            </View>

            <View style={styles.form}>
              <View style={styles.inputContainer}>
                <TextInput
                  style={styles.input}
                  placeholder="E-posta"
                  placeholderTextColor="#9CA3AF"
                  value={email}
                  onChangeText={setEmail}
                  keyboardType="email-address"
                  autoCapitalize="none"
                  autoCorrect={false}
                />
              </View>

              <View style={styles.inputContainer}>
                <TextInput
                  style={styles.input}
                  placeholder="Şifre"
                  placeholderTextColor="#9CA3AF"
                  value={password}
                  onChangeText={setPassword}
                  secureTextEntry
                  autoCapitalize="none"
                />
              </View>

              <TouchableOpacity
                style={[styles.button, isLoading && styles.buttonDisabled]}
                onPress={handleLogin}
                disabled={isLoading}
              >
                <Text style={styles.buttonText}>
                  {isLoading ? 'Giriş yapılıyor...' : 'Giriş Yap'}
                </Text>
              </TouchableOpacity>

              <View style={styles.footer}>
                <Text style={styles.footerText}>Hesabın yok mu? </Text>
                <Link href="/(auth)/register" style={styles.link}>
                  <Text style={styles.linkText}>Kayıt Ol</Text>
                </Link>
              </View>
            </View>
          </View>
        </KeyboardAvoidingView>
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
  },
  keyboardView: {
    flex: 1,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 32,
  },
  header: {
    alignItems: 'center',
    marginBottom: 48,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    textAlign: 'center',
  },
  form: {
    gap: 16,
  },
  inputContainer: {
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    borderRadius: 12,
    marginBottom: 16,
  },
  input: {
    paddingHorizontal: 16,
    paddingVertical: 16,
    fontSize: 16,
    color: '#374151',
  },
  button: {
    backgroundColor: '#4F46E5',
    borderRadius: 12,
    paddingVertical: 16,
    alignItems: 'center',
    marginTop: 8,
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 24,
  },
  footerText: {
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: 14,
  },
  link: {
    marginLeft: 4,
  },
  linkText: {
    color: 'white',
    fontSize: 14,
    fontWeight: '600',
    textDecorationLine: 'underline',
  },
});*/


import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform,
  SafeAreaView,
} from 'react-native';
import { Link, router, useLocalSearchParams } from 'expo-router';
import { LinearGradient } from 'expo-linear-gradient';
import { useAuthStore } from '../../src/store/authStore';

export default function LoginScreen() {
  const { email: paramEmail } = useLocalSearchParams<{ email?: string }>();
  const [email, setEmail] = useState(paramEmail || '');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuthStore();

  const validateEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return email.includes("@") ? emailRegex.test(email) : true;
  };

  const handleLogin = async () => {
    // Input validations
    if (!email.trim() || !password) {
      Alert.alert('Hata', 'Lütfen tüm alanları doldurun');
      return;
    }

    if (!validateEmail(email)) {
      Alert.alert('Hata', 'Lütfen geçerli bir e-posta adresi girin');
      return;
    }

    setIsLoading(true);
    try {
      const success = await login(email.toLowerCase().trim(), password);

      if (success) {
        router.replace('/(tabs)');
      } else {
        Alert.alert(
            'Giriş Başarısız',
            'E-posta veya şifre hatalı. Lütfen bilgilerinizi kontrol edin.'
        );
      }
    } catch (error: any) {
      console.error('Login error:', error);

      let errorMessage = 'Bir hata oluştu. Lütfen tekrar deneyin.';

      if (error.message?.includes('network') || error.message?.includes('fetch')) {
        errorMessage = 'Bağlantı hatası. İnternet bağlantınızı kontrol edin.';
      } else if (error.message?.includes('unauthorized')) {
        errorMessage = 'Giriş bilgileri hatalı. Lütfen kontrol edin.';
      }

      Alert.alert('Hata', errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
      <SafeAreaView style={styles.container}>
        <LinearGradient
            colors={['#667eea', '#764ba2']}
            style={styles.gradient}
        >
          <KeyboardAvoidingView
              behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
              style={styles.keyboardView}
          >
            <View style={styles.content}>
              <View style={styles.header}>
                <Text style={styles.title}>Learnexa</Text>
                <Text style={styles.subtitle}>Öğrenme yolculuğuna başla</Text>
              </View>

              <View style={styles.form}>
                <View style={styles.inputContainer}>
                  <TextInput
                      style={styles.input}
                      placeholder="E-posta veya Kullanıcı Adı"
                      placeholderTextColor="#9CA3AF"
                      value={email}
                      onChangeText={setEmail}
                      keyboardType="email-address"
                      autoCapitalize="none"
                      autoCorrect={false}
                      editable={!isLoading}
                      maxLength={100}
                  />
                </View>

                <View style={styles.inputContainer}>
                  <TextInput
                      style={styles.input}
                      placeholder="Şifre"
                      placeholderTextColor="#9CA3AF"
                      value={password}
                      onChangeText={setPassword}
                      secureTextEntry
                      autoCapitalize="none"
                      editable={!isLoading}
                      maxLength={50}
                  />
                </View>

                <TouchableOpacity
                    style={[styles.button, isLoading && styles.buttonDisabled]}
                    onPress={handleLogin}
                    disabled={isLoading}
                    activeOpacity={0.8}
                >
                  <Text style={styles.buttonText}>
                    {isLoading ? 'Giriş yapılıyor...' : 'Giriş Yap'}
                  </Text>
                </TouchableOpacity>

                <View style={styles.footer}>
                  <Text style={styles.footerText}>Hesabın yok mu? </Text>
                  <Link href="/(auth)/register" style={styles.link}>
                    <Text style={styles.linkText}>Kayıt Ol</Text>
                  </Link>
                </View>
              </View>
            </View>
          </KeyboardAvoidingView>
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
  },
  keyboardView: {
    flex: 1,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 32,
  },
  header: {
    alignItems: 'center',
    marginBottom: 48,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    textAlign: 'center',
  },
  form: {
    gap: 16,
  },
  inputContainer: {
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    borderRadius: 12,
    marginBottom: 16,
  },
  input: {
    paddingHorizontal: 16,
    paddingVertical: 16,
    fontSize: 16,
    color: '#374151',
  },
  button: {
    backgroundColor: '#4F46E5',
    borderRadius: 12,
    paddingVertical: 16,
    alignItems: 'center',
    marginTop: 8,
    elevation: 3,
    shadowColor: '#4F46E5',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 24,
  },
  footerText: {
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: 14,
  },
  link: {
    marginLeft: 4,
  },
  linkText: {
    color: 'white',
    fontSize: 14,
    fontWeight: '600',
    textDecorationLine: 'underline',
  },
});