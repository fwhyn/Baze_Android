package com.fwhyn.baze.data.model.memory

data class ManageMemoryParam(
    val action: ManageMemoryAction,
    val sourceType: DataSourceType = DataSourceType.CACHE,
)