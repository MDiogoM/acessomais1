package pt.ipvc.acessomais.data.repo

import pt.ipvc.acessomais.data.local.LocalEntity
import pt.ipvc.acessomais.data.model.Local

fun LocalEntity.toDomain(): Local =
    Local(
        id = id,
        userEmail = userEmail,
        nome = nome,
        tipo = tipo,
        latitude = latitude,
        longitude = longitude,
        temRampa = temRampa,
        temElevador = temElevador,
        larguraEntradaCm = larguraEntradaCm,
        temWcAdaptado = temWcAdaptado,
        comentario = comentario,
        rating = rating,
        updatedAt = updatedAt,
        pendingSync = pendingSync
    )

fun Local.toEntity(): LocalEntity =
    LocalEntity(
        id = id,
        userEmail = userEmail,
        nome = nome,
        tipo = tipo,
        latitude = latitude,
        longitude = longitude,
        temRampa = temRampa,
        temElevador = temElevador,
        larguraEntradaCm = larguraEntradaCm,
        temWcAdaptado = temWcAdaptado,
        comentario = comentario,
        rating = rating,
        updatedAt = updatedAt,
        pendingSync = pendingSync
    )