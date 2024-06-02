import com.example.vashopify.data.CategoryResponse

sealed class CategoryEvent{
    class Success(val list:List<CategoryResponse>):CategoryEvent()
    class Failure:CategoryEvent()
    object Loading:CategoryEvent()
    object Empty:CategoryEvent()
}