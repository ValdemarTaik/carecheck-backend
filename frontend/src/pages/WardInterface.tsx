// src/pages/WardInterface.tsx
import React, { useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  TextField,
  Alert,
  CircularProgress,
  Chip,
  Divider,
} from '@mui/material';
import {
  CheckCircle as CheckIcon,
  AccessTime as TimeIcon,
  LocationOn as LocationIcon,
} from '@mui/icons-material';

const WardInterface: React.FC = () => {
  const [notes, setNotes] = useState('');
  const [location, setLocation] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleCheckIn = async () => {
    if (!notes.trim()) {
      setError('Пожалуйста, добавьте заметку о вашем состоянии');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      
      // Моковый запрос для тестирования
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccess(true);
      setNotes('');
      setLocation('');
      
      // Сбрасываем успех через 3 секунды
      setTimeout(() => setSuccess(false), 3000);
      
    } catch (err) {
      setError('Ошибка при отправке чекина');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 800, margin: '0 auto' }}>
      <Typography variant="h4" gutterBottom>
        👵 Интерфейс подопечного
      </Typography>
      
      <Typography variant="body1" color="text.secondary" paragraph>
        Отметьте ваше текущее состояние, чтобы родственники знали, что с вами всё в порядке.
      </Typography>
      
      {success && (
        <Alert severity="success" sx={{ mb: 3 }}>
          ✅ Чекин успешно отправлен! Ваши родственники уведомлены.
        </Alert>
      )}
      
      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}
      
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            📝 Информация о последнем чекине
          </Typography>
          
          <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
            <Chip 
              icon={<CheckIcon />} 
              label="Статус: В порядке" 
              color="success" 
              variant="outlined"
            />
            <Chip 
              icon={<TimeIcon />} 
              label="Сегодня, 10:30" 
              variant="outlined"
            />
            <Chip 
              icon={<LocationIcon />} 
              label="Дома" 
              variant="outlined"
            />
          </Box>
          
          <Typography variant="body2" color="text.secondary">
            Заметка: "Чувствую себя хорошо, завтракал, принимаю лекарства по расписанию"
          </Typography>
        </CardContent>
      </Card>
      
      <Card>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            ✏️ Новый чекин
          </Typography>
          
          <Box sx={{ mb: 3 }}>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              Как вы себя чувствуете? Что делали сегодня?
            </Typography>
            <TextField
              fullWidth
              multiline
              rows={4}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Например: Чувствую себя хорошо, позавтракал, принял лекарства, планирую прогулку..."
              variant="outlined"
            />
          </Box>
          
          <Box sx={{ mb: 4 }}>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              Где вы находитесь? (необязательно)
            </Typography>
            <TextField
              fullWidth
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="Например: Дома, в поликлинике, на прогулке в парке..."
              variant="outlined"
            />
          </Box>
          
          <Divider sx={{ mb: 3 }} />
          
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <Button
              variant="contained"
              color="primary"
              size="large"
              startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <CheckIcon />}
              onClick={handleCheckIn}
              disabled={loading}
              sx={{ px: 4, py: 1.5, fontSize: '1.1rem' }}
            >
              {loading ? 'Отправка...' : '✅ Я в порядке! Отправить чекин'}
            </Button>
          </Box>
          
          <Typography variant="body2" color="text.secondary" align="center" sx={{ mt: 2 }}>
            Эта информация будет отправлена вашим родственникам
          </Typography>
        </CardContent>
      </Card>
      
      <Alert severity="info" sx={{ mt: 3 }}>
        💡 Подсказка: Регулярно отмечайтесь, чтобы родственники не волновались.
        Если вы не отметитесь в течение 24 часов, они получат уведомление.
      </Alert>
    </Box>
  );
};

export default WardInterface;