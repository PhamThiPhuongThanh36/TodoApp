# 📌 TodoApp

Ứng dụng quản lý công việc được viết bằng **Kotlin** với **Jetpack Compose** và **Room**.  
TodoApp cho phép quản lý công việc theo **Project → List → Task**, hỗ trợ **đồng hồ đếm ngược (countdown timer)** cho từng Task.  

---

## 🚀 Tính năng chính
- **Quản lý Project**
  - Thêm, sửa, xóa Project.
- **Quản lý List**
  - Mỗi Project chứa nhiều List.
  - Thêm, sửa, xóa List.
- **Quản lý Task**
  - Thêm, sửa, xóa Task.
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
