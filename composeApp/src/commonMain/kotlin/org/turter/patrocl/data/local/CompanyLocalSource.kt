package org.turter.patrocl.data.local

interface CompanyLocalSource<T> : LocalSource<T> {
    fun count(): Long
}