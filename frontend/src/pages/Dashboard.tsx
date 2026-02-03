// src/pages/Dashboard.tsx
import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Grid,
  Typography,
  Button,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Person as PersonIcon,
  CheckCircle as CheckIcon,
  Warning as WarningIcon,
  Error as ErrorIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';

interface Stats {
  totalUsers: number;
  activeWards: number;
  missedCheckIns: number;
  lastUpdate: string;
}

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchStats = async () => {
    try {
      setLoading(true);
      // Моковые данные для тестирования
      const mockStats: Stats = {
        totalUsers: 15,
        activeWards: 8,
        missedCheckIns: 2,
        lastUpdate: new Date().toLocaleTimeString(),
      };
      setStats(mockStats);
      setError(null);
    } catch (err) {
      setError('Ошибка загрузки данных');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, []);

  if (loading && !stats) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        📊 Панель управления
      </Typography>
      
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between' }}>
        <Typography variant="body1" color="text.secondary">
          Последнее обновление: {stats?.lastUpdate}
        </Typography>
        <Button
          variant="outlined"
          startIcon={<RefreshIcon />}
          onClick={fetchStats}
          disabled={loading}
        >
          Обновить
        </Button>
      </Box>
      
      <Grid container spacing={3}>
        {/* Карточка 1 */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" mb={1}>
                <PersonIcon color="primary" sx={{ fontSize: 40, mr: 1 }} />
                <Box>
                  <Typography color="text.secondary" variant="body2">
                    Всего пользователей
                  </Typography>
                  <Typography variant="h4">
                    {stats?.totalUsers || 0}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        {/* Карточка 2 */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" mb={1}>
                <CheckIcon color="success" sx={{ fontSize: 40, mr: 1 }} />
                <Box>
                  <Typography color="text.secondary" variant="body2">
                    Активных подопечных
                  </Typography>
                  <Typography variant="h4">
                    {stats?.activeWards || 0}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        {/* Карточка 3 */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" mb={1}>
                <WarningIcon color="warning" sx={{ fontSize: 40, mr: 1 }} />
                <Box>
                  <Typography color="text.secondary" variant="body2">
                    Пропущенных чекинов
                  </Typography>
                  <Typography variant="h4">
                    {stats?.missedCheckIns || 0}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        {/* Карточка 4 */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" mb={1}>
                <ErrorIcon color="error" sx={{ fontSize: 40, mr: 1 }} />
                <Box>
                  <Typography color="text.secondary" variant="body2">
                    Требуют внимания
                  </Typography>
                  <Typography variant="h4">
                    {Math.min(stats?.missedCheckIns || 0, 5)}
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      
      <Grid container spacing={3} sx={{ mt: 1 }}>
        <Grid item xs={12} md={6}>
          <Card sx={{ mt: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                🚀 Быстрые действия
              </Typography>
              <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                <Button variant="contained" color="primary">
                  Добавить подопечного
                </Button>
                <Button variant="outlined" color="primary">
                  Создать тестовые данные
                </Button>
                <Button variant="outlined">
                  Проверить все чекины
                </Button>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Card sx={{ mt: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                ℹ️ Информация о системе
              </Typography>
              <Typography variant="body2" paragraph>
                • Система проверяет чекины каждые 5 минут
              </Typography>
              <Typography variant="body2" paragraph>
                • Уведомления отправляются при пропуске чекина
              </Typography>
              <Typography variant="body2" paragraph>
                • Статусы: OK (все хорошо), WARNING (внимание), DANGER (опасность)
              </Typography>
              <Button 
                variant="text" 
                size="small" 
                href="http://localhost:8080/api/debug/status"
                target="_blank"
              >
                Проверить API статус
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;