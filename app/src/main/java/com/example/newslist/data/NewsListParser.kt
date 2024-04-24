package com.example.newslist.data

import android.util.Log
import android.util.Xml
import com.example.newslist.MainActivity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsListParser {
// We don't use namespaces.
private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
fun parse(inputStream: InputStream): List<NewsList> {
    inputStream.use { inputStream ->
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        parser.nextTag()
        Log.i(MainActivity.LOG_TAG, "parser: $parser")
        return readFeed(parser)
    }
}

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<NewsList> {
        val items = mutableListOf<NewsList>()

        parser.require(XmlPullParser.START_TAG, ns, "rss")
        while (parser.next() != XmlPullParser.END_DOCUMENT) { // Continue until the end of the document
            if (parser.eventType == XmlPullParser.START_TAG) {
                if (parser.name == "channel") {
                    // We are now inside the <channel> tag, continue to next tag
                    parser.nextTag()
                    while (parser.name != "channel") { // Process everything inside <channel>
                        if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
                            items.add(readEntry(parser))
                        } else if (parser.eventType == XmlPullParser.START_TAG) {
                            skip(parser) // Skip any tags that are not <item>
                        }
                        parser.nextTag() // Move to next tag inside <channel>
                    }
                }
            }
        }
        return items
    }


    private fun readEntry(parser: XmlPullParser): NewsList {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var id: String? = null
        var title: String? = null
        var description: String? = null
        var imageUrl: String? = null
        var author: String? = null
        var publicationDate: Date? = null
        var articleLink: String? = null
        var keywords: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "guid" -> id = readSimpleTag(parser, "guid")
                "title" -> title = readSimpleTag(parser, "title")
                "description" -> description = readSimpleTag(parser, "description")
                "link" -> articleLink = readSimpleTag(parser, "link")
                "dc:creator" -> author = readSimpleTag(parser, "dc:creator")
                "pubDate" -> {
                    val dateString = readSimpleTag(parser, "pubDate")
                    publicationDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(dateString)
                }
                "media:content" -> {
                    if (imageUrl == null) {  // Only pick the first image URL if there are multiple
                        imageUrl = parser.getAttributeValue(null, "url")
                    }
                }
                "category" -> {
                    keywords = keywords?.let { it + ", " + readSimpleTag(parser, "category") } ?: readSimpleTag(parser, "category")
                }
                else -> skip(parser)
            }
        }

        return NewsList(
            id ?: throw IllegalArgumentException("Missing id"),
            title ?: throw IllegalArgumentException("Missing title"),
            description ?: throw IllegalArgumentException("Missing description"),
            imageUrl ?: "",
            author ?: "",
            publicationDate ?: Date(),
            articleLink ?: "",
            keywords ?: ""
        )
    }

    private fun readSimpleTag(parser: XmlPullParser, tagName: String): String {
        parser.require(XmlPullParser.START_TAG, ns, tagName)
        val result = if (parser.next() == XmlPullParser.TEXT) parser.text else ""
        parser.nextTag()  // move to next tag
        return result
    }
    /**
     * Skips tags the parser isn't interested in. Uses depth to handle nested tags.
     * Assumes that the parser is currently at a START_TAG.
     */
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException("Attempting to skip from a position that is not a start tag.")
        }

        var depth = 1 // We start at 1 because we're currently at a start tag
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth-- // We've reached an end tag, decrement depth
                XmlPullParser.START_TAG -> depth++ // A new start tag, increment depth
            }
        }
    }
}