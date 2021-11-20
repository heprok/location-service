package com.briolink.locationservice.service

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.HeaderColumnNameMappingStrategy
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.persistence.EntityManager

@Service
class CsvService(
    private val entityManager: EntityManager,
) {
    private fun throwIfFileEmpty(file: MultipartFile) {
        if (file.isEmpty)
            throw RuntimeException("Empty file")
    }

    final inline fun <reified E> upload(stream: InputStream): MutableList<E>? {
        val file = BufferedInputStream(stream)
        val strategy: HeaderColumnNameMappingStrategy<E> = HeaderColumnNameMappingStrategy()
        strategy.type = E::class.java

        val csvToBean: CsvToBean<E> = CsvToBeanBuilder<E>(file.bufferedReader())
                .withMappingStrategy(strategy)
                .withIgnoreLeadingWhiteSpace(true)
                .build()

        return csvToBean.parse()
    }
}
