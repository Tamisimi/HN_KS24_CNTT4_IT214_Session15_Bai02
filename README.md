# Bài 2 Session 15 — Correlation ID & Tracing (đặt vé xem phim)

## Luồng

```
Client → MovieBookingService → topic booking-events
              (sinh correlationId, gắn HEADER)
                    ↓
         SeatAllocationService → topic seat-confirmed-events
              (đọc header, giữ ghế, forward header)
                    ↓
              PaymentService
              (đọc header, thanh toán, log cùng ID)
```

## Topics Kafka

- `booking-events`
- `seat-confirmed-events`

## Chạy

1. Kafka `localhost:9092`
2. Tạo topic (hoặc để auto-create)
3. Chạy lần lượt 3 service
4. POST `http://localhost:8081/api/bookings` với JSON đề bài

Log phải cùng một `CorrelationID` xuyên suốt.

Báo cáo: `BAO_CAO.md`
