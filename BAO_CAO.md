# Báo cáo — Correlation ID trong Choreography Saga

## 1. Vai trò Correlation ID

Một UUID sinh **một lần** tại MovieBookingService khi nhận request. Mọi event/log sau đó mang cùng ID → ghép được full journey của một vé, tránh "Event Spaghetti".

## 2. Vì sao gắn vào HEADER, không phải payload?

| | Header | Payload |
|---|--------|--------|
| Thay đổi schema nghiệp vụ | Không | Phải thêm field mọi event |
| Middleware / tracing | Chuẩn (Kafka headers, Zipkin B3…) | Khó tái sử dụng |
| Consumer không quan tâm tracing | Bỏ qua header | Vẫn phải deserialize field |

## 3. Cài đặt & chạy

```bash
# Kafka
docker run -d --name kafka -p 9092:9092 apache/kafka:latest

# 3 terminal
cd movie-booking-service && ./gradlew bootRun
cd seat-allocation-service && ./gradlew bootRun
cd payment-service && ./gradlew bootRun
```

POST body:
```json
{
  "cinemaBookingId": "CIN-2024-789",
  "movieCode": "AVENGERS-5",
  "showTime": "2024-12-25T19:30:00",
  "seatNumbers": ["A12", "A13"],
  "customerEmail": "tuananh@email.com",
  "totalPrice": 240000
}
```

## 4. Kết quả kỳ vọng

Cùng một CorrelationID trên mọi dòng log của 3 service.
