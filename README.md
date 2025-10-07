# TodoApp

Ứng dụng quản lý công việc được viết bằng **Kotlin** với **Jetpack Compose** và **Room**.  
TodoApp cho phép quản lý công việc theo **Project → List → Task**, hỗ trợ **đồng hồ đếm ngược (countdown timer)**.  

---

## Tính năng chính
- **Quản lý Project**
  - Thêm, sửa, xóa Project.
- **Quản lý List**
  - Mỗi Project chứa nhiều List.
  - Thêm, sửa, xóa List.
- **Quản lý Task**
  - Thêm, sửa, xóa Task.
  - Khôi phục Task đã xóa.
  - Đặt deadline, mô tả chi tiết.
  - Đánh dấu hoàn thành / chưa hoàn thành.
  - Xem danh sách Task lọc theo List, theo Tag, theo ngày đến hạn
- **Đồng hồ đếm ngược (Countdown Timer)**
  - Đặt timer.
  - Khi hết giờ → **BroadcastReceiver** phát thông báo (notification) và báo thức.
- **Lưu trữ dữ liệu với Room**
  - Dữ liệu được lưu local, không mất khi thoát app.
- **UI hiện đại với Jetpack Compose**
  - Giao diện theo phong cách declarative, tự động cập nhật khi dữ liệu thay đổi.

---

## 📱 Giao diện

### HomeScreen và Đồng hồ đếm ngược

<p align="center">
  <img src="https://github.com/user-attachments/assets/56443c00-8597-410d-b8b9-1a44ac1b8cbf" alt="Extra 6" width="250" />
  <img src="https://github.com/user-attachments/assets/ce78fcd2-5c96-431f-931b-90db9b95d7c4" alt="Extra 3" width="250" />
</p>

### Thêm Tag, thêm Task và Khôi phục Task

<p align="center">
  <img src="https://github.com/user-attachments/assets/b36c8ce0-45c5-48ad-aec2-dfb059f82cb1" alt="Extra 1" width="250" />
  <img src="https://github.com/user-attachments/assets/68892036-6b3b-4e6b-b6fe-514c78a4d64d" alt="Extra 2" width="250" />
  <img src="https://github.com/user-attachments/assets/13654209-8d82-4621-a65c-7f75ac8537be" alt="Extra 4" width="250" />
</p>

### Xem Task theo List, theo Tag, và xem Ngày đến hạn
<p align="center">
  <img src="https://github.com/user-attachments/assets/01de91a1-0d3a-40aa-92df-cec4d935df2d" alt="Extra 7" width="250" />
  <img src="https://github.com/user-attachments/assets/7478064f-6723-45d4-8079-15038a33dd08" alt="Extra 8" width="250" />
  <img src="https://github.com/user-attachments/assets/a656f684-2d6b-4c98-bc1b-fa945cea5ed3" alt="Extra 5" width="250" />
</p>

---

## 🛠 Công nghệ sử dụng
- [Kotlin – ngôn ngữ chính.
- [Jetpack Compose]– UI toolkit.
- [Room] – ORM cho database.
- [ViewModel] + [Flow] – quản lý state & dữ liệu.
- [BroadcastReceiver] + [AlarmManager] – thông báo khi countdown timer hết giờ.
- [Navigation Compose] – điều hướng giữa các màn hình.

---

## 📂 Cấu trúc dữ liệu (Room Entities)
```kotlin
@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val listId: Int? = null,
    val projectId: Int,
    val listName: String,
)

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val listId: Int? = null,
    val projectId: Int,
    val listName: String,
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val listId: Int? = null,
    val taskName: String,
    val status: Boolean = false,
    val note: String? = null,
    val statusDelete: Boolean = false,
    val createdAt: String? = null,
    val dueDate: String? = null
)

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Int? = null,
    val tagName: String,
    val tagColor: Long
)

@Entity(
    tableName = "tasks_tags",
    primaryKeys = ["taskId", "tagId"]
)
data class TaskTagEntity(
    val taskId: Int,
    val tagId: Int
)

data class TaskWithTags(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "tagId",
        associateBy = Junction(TaskTagEntity::class)
    )
    val tags: List<TagEntity>
)
