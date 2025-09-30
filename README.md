# 📌 TodoApp

Ứng dụng quản lý công việc được viết bằng **Kotlin** với **Jetpack Compose** và **Room**.  
TodoApp cho phép quản lý công việc theo **Project → List → Task**, hỗ trợ **đồng hồ đếm ngược (countdown timer)** cho từng Task.  

---

## 🚀 Tính năng chính
- **Quản lý Project**
  - Tạo, đổi tên, xóa Project.
- **Quản lý List**
  - Mỗi Project chứa nhiều List.
  - Thêm, sửa, xóa List.
- **Quản lý Task**
  - Thêm, sửa, xóa Task.
  - Đặt deadline, mô tả chi tiết.
  - Đánh dấu hoàn thành / chưa hoàn thành.
- **Đồng hồ đếm ngược (Countdown Timer)**
  - Đặt timer cho Task.
  - Khi hết giờ → **BroadcastReceiver** phát thông báo (notification).
- **Lưu trữ dữ liệu với Room**
  - Dữ liệu được lưu local, không mất khi thoát app.
- **UI hiện đại với Jetpack Compose**
  - Giao diện theo phong cách declarative, tự động cập nhật khi dữ liệu thay đổi.

---

## 🛠 Công nghệ sử dụng
- [Kotlin](https://kotlinlang.org/) – ngôn ngữ chính.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) – UI toolkit.
- [Room](https://developer.android.com/training/data-storage/room) – ORM cho database.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) + [Flow](https://kotlinlang.org/docs/flow.html) – quản lý state & dữ liệu.
- [BroadcastReceiver](https://developer.android.com/guide/components/broadcasts) + [AlarmManager](https://developer.android.com/training/scheduling/alarms) – thông báo khi countdown timer hết giờ.
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) – điều hướng giữa các màn hình.

---

## 📂 Cấu trúc dữ liệu (Room Entities)
```kotlin
@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val projectId: Int = 0,
    val name: String
)

@Entity(foreignKeys = [ForeignKey(
    entity = Project::class,
    parentColumns = ["projectId"],
    childColumns = ["projectOwnerId"],
    onDelete = CASCADE
)])
data class TaskList(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    val name: String,
    val projectOwnerId: Int
)

@Entity(foreignKeys = [ForeignKey(
    entity = TaskList::class,
    parentColumns = ["listId"],
    childColumns = ["listOwnerId"],
    onDelete = CASCADE
)])
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val deadline: Long? = null,
    val countdownSeconds: Int? = null,
    val listOwnerId: Int
)
