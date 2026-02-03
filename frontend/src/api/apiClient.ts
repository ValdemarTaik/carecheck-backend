// src/api/apiClient.ts
import axios from 'axios';

// Базовый URL для API запросов
const API_BASE_URL = 'http://localhost:8080/api';

// Создаем экземпляр axios с настройками
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Интерцептор для добавления токена (если будет авторизация)
apiClient.interceptors.request.use(
  (config) => {
    // Здесь можно добавить токен авторизации
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Интерцептор для обработки ошибок
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Сервер ответил с ошибкой
      console.error('API Error:', {
        status: error.response.status,
        data: error.response.data,
        url: error.config.url,
      });
      
      // Можно обработать определенные статусы
      if (error.response.status === 401) {
        // Не авторизован - перенаправить на логин
        window.location.href = '/login';
      }
    } else if (error.request) {
      // Запрос был сделан, но нет ответа
      console.error('No response received:', error.request);
    } else {
      // Ошибка при настройке запроса
      console.error('Request setup error:', error.message);
    }
    
    return Promise.reject(error);
  }
);

// Вспомогательные функции для HTTP методов
export const api = {
  // GET запрос
  get: (url: string, params?: any) => 
    apiClient.get(url, { params }),
  
  // POST запрос
  post: (url: string, data?: any) => 
    apiClient.post(url, data),
  
  // PUT запрос
  put: (url: string, data?: any) => 
    apiClient.put(url, data),
  
  // DELETE запрос
  delete: (url: string) => 
    apiClient.delete(url),
  
  // PATCH запрос
  patch: (url: string, data?: any) => 
    apiClient.patch(url, data),
};

// Экспортируем клиент для кастомных запросов
export default apiClient;