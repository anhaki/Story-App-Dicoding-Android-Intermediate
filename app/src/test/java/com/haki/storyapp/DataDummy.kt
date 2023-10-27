package com.haki.storyapp

import com.haki.storyapp.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photoUri + $i",
                "createdAt + $i",
                "name $i",
                "description $i",
                i.toDouble(),
                "id $i",
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}
