package com.wiser.animationlistdemo.textanim.utils

import com.wiser.animationlistdemo.textanim.text.CharacterDiffResult

object CharacterUtils {
    fun diff(oldText: CharSequence, newText: CharSequence): List<CharacterDiffResult> {
        val differentList: MutableList<CharacterDiffResult> = ArrayList()
        val skip: MutableSet<Int> = HashSet()
        for (i in oldText.indices) {
            val c = oldText[i]
            for (j in newText.indices) {
                if (!skip.contains(j) && c == newText[j]) {
                    skip.add(j)
                    val different = CharacterDiffResult()
                    different.c = c
                    different.fromIndex = i
                    different.moveIndex = j
                    differentList.add(different)
                    break
                }
            }
        }
        return differentList
    }

    fun needMove(index: Int, differentList: List<CharacterDiffResult>): Int {
        for ((_, fromIndex, moveIndex) in differentList) {
            if (fromIndex == index) {
                return moveIndex
            }
        }
        return -1
    }

    fun stayHere(index: Int, differentList: List<CharacterDiffResult>): Boolean {
        for ((_, _, moveIndex) in differentList) {
            if (moveIndex == index) {
                return true
            }
        }
        return false
    }

    /**
     * @return
     */
    fun getOffset(
        from: Int,
        move: Int,
        progress: Float,
        startX: Float,
        oldStartX: Float,
        gaps: FloatArray,
        oldGaps: FloatArray
    ): Float {
        var dist = startX
        for (i in 0 until move) {
            dist += gaps[i]
        }
        var cur = oldStartX
        for (i in 0 until from) {
            cur += oldGaps[i]
        }
        return cur + (dist - cur) * progress
    }
}