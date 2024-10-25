package otus.gpb.recyclerview

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import otus.gpb.recyclerview.adapter.MessageListAdapter
import otus.gpb.recyclerview.persistence.Datasource

class MainActivity : AppCompatActivity() {

    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val adapter = MessageListAdapter(Datasource.getMessages(pageSize, currentPage))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val customView: View = LayoutInflater.from(this).inflate(R.layout.swipe_archive_view, null)

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
                    adapter.removeItem(viewHolder.absoluteAdapterPosition)

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean,
                ) {
                    val itemView = viewHolder.itemView
                    customView.measure(
                        View.MeasureSpec.makeMeasureSpec(itemView.width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(itemView.height, View.MeasureSpec.EXACTLY)
                    )
                    customView.layout(0, 0, customView.measuredWidth, customView.measuredHeight)

                    c.save()
                    if (dX < 0) {
                        c.translate(itemView.right.toFloat() + dX, itemView.top.toFloat())
                        customView.draw(c)
                    }
                    c.restore()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            })
            .attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && !isLoading) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemsCount = Datasource.total()
                    val loadedItemsCount = adapter.itemCount

                    if (totalItemsCount > (lastVisibleItem + 1) && totalItemsCount > loadedItemsCount) {
                        isLoading = true
                        recyclerView.post {
                            loadMoreItems(adapter)
                            isLoading = false
                        }
                    }
                }
            }
        })
    }

    private fun loadMoreItems(adapter: MessageListAdapter) {
        currentPage++
        val newMessages = Datasource.getMessages(pageSize, currentPage)
        if (newMessages.isNotEmpty()) {
            adapter.addMessages(newMessages)
        }
    }
}
