// src/api/services.ts
import apiClient from './apiClient';

// Типы данных
export interface User {
  id: number;
  username: string;
  email: string;
  name: string;
  phoneNumber?: string;
  userType: 'WARD' | 'RELATIVE';
}

export interface CheckIn {
  id: number;
  wardId: number;
  wardName: string;
  checkInTime: string;
  status: string;
  notes?: string;
  location?: string;
}

// API функции для работы с пользователями
export const userService = {
  // Создание тестовых данных
  createTestData: () => 
    apiClient.get('/users/test-data'),
  
  // Получение количества пользователей
  getUserCount: () => 
    apiClient.get('/users/count'),
};

// API функции для работы с чекинами
export const checkInService = {
  // Создание чекина
  createCheckIn: (wardId: number, notes?: string, location?: string) =>
    apiClient.post(`/checkins?wardId=${wardId}&notes=${notes || ''}&location=${location || ''}`),
  
  // Создание чекина (новый способ)
  createCheckInV2: (data: { wardId: number; notes?: string; location?: string }) =>
    apiClient.post('/checkins/v2', data),
};

// Экспортируем все сервисы
export default {
  user: userService,
  checkIn: checkInService,
};